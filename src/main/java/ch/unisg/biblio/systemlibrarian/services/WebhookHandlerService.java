package ch.unisg.biblio.systemlibrarian.services;

import ch.unisg.biblio.systemlibrarian.AlmaClientConfig;
import ch.unisg.biblio.systemlibrarian.clients.models.AlmaWebhookLoanItem;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micronaut.context.annotation.ConfigurationProperties;
import io.micronaut.context.annotation.Requires;
import io.micronaut.http.HttpResponse;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

@ConfigurationProperties("HMAC")
@Requires(property = "HMAC")
@Singleton
public class WebhookHandlerService {

	private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	private static final String LOAN_ACTION = "LOAN";
	private static final String EVENT_LOAN_CREATED = "LOAN_CREATED";
	private static final String EVENT_LOAN_RETURNED = "LOAN_RETURNED";
	private final HMACService hmacService;
	private final AlmaClientConfig almaClientConfig;
	private final GadgetProviderService gadgetProviderService;
	// injected from config properties
	private String algorithm;
	private String secret;

	@Inject
	public WebhookHandlerService(
			HMACService hmacService,
			AlmaClientConfig config,
			GadgetProviderService gadgetProviderService) {
		this.hmacService = hmacService;
		this.almaClientConfig = config;
		this.gadgetProviderService = gadgetProviderService;
	}

	public void setAlgorithm(String algorithm) {
		this.algorithm = algorithm;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public HttpResponse<String> processWebhook(String body, String signature) {
		boolean webhookValid = checkHMAC(body, signature);
		if (!webhookValid) {
			LOG.warn("The received webhook was not correctly signed ('{}')", signature);
			return HttpResponse.badRequest();
		}
		Optional<AlmaWebhookLoanItem> item = convert(body);
		if (item.isEmpty()) {
			LOG.warn("The received webhook could not be transformed into an item ('{}')", signature);
			return HttpResponse.badRequest("No Item");
		}
		item.filter(i -> filterWebhook(i, signature))
				.ifPresent(i -> {
					boolean available;
					if (EVENT_LOAN_CREATED.equals(i.getEvent().getValue())) {
						available = false;
					} else if (EVENT_LOAN_RETURNED.equals(i.getEvent().getValue())) {
						available = true;
					} else {
						// event other than created/returned -> availability stays the same
						return;
					}
					gadgetProviderService.updateAvailability(i.getItemLoan().getItemBarcode(), available);
				});
		return HttpResponse.ok();
	}

	private boolean checkHMAC(String body, String signature) {
		try {
			return hmacService.checkSignature(algorithm, body, secret, signature);
		} catch (InvalidKeyException | NoSuchAlgorithmException e) {
			LOG.error("Could not validate webhook ('{}'), checking the signature failed", signature, e);
			return false;
		}
	}

	protected Optional<AlmaWebhookLoanItem> convert(String jsonBody) {
		ObjectMapper objectMapper = new ObjectMapper()
				.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		try {
			return Optional.of(objectMapper.readValue(jsonBody, AlmaWebhookLoanItem.class));
		} catch (JsonProcessingException e) {
			LOG.error("Could not convert webhook request body to object", e);
			return Optional.empty();
		}
	}

	protected boolean filterWebhook(AlmaWebhookLoanItem item, String signature) {
		boolean isGadgetWebhook = isLoanAction(item) && isCorrectMmsId(item) && isCorrectHoldingId(item);
		LOG.info("Filter webhook ('{}'): isGadgetWebhook: '{}'", signature, isGadgetWebhook);
		return isGadgetWebhook;
	}

	private boolean isLoanAction(AlmaWebhookLoanItem item) {
		return LOAN_ACTION.equals(item.getAction());
	}

	private boolean isCorrectMmsId(AlmaWebhookLoanItem item) {
		return almaClientConfig.getMmsId().equals(item.getItemLoan().getMmsId());
	}

	private boolean isCorrectHoldingId(AlmaWebhookLoanItem item) {
		return almaClientConfig.getHoldingId().equals(item.getItemLoan().getHoldingId());
	}
}
