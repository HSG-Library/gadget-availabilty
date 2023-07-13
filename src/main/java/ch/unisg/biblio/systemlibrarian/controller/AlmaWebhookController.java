package ch.unisg.biblio.systemlibrarian.controller;

import ch.unisg.biblio.systemlibrarian.services.WebhookHandlerService;
import io.micronaut.context.annotation.ConfigurationProperties;
import io.micronaut.context.annotation.Requires;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.Map;

@ConfigurationProperties("HMAC")
@Requires(property = "HMAC")
@Controller("/alma")
public class AlmaWebhookController {

	private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	private final WebhookHandlerService webhookHandlerService;

	@Inject
	public AlmaWebhookController(WebhookHandlerService webhookHandlerService) {
		this.webhookHandlerService = webhookHandlerService;
	}

	@Get("/webhook")
	public HttpResponse<Object> challenge(@QueryValue String challenge) {
		LOG.warn("Webhook Challenge initiated");
		LOG.debug("Challenge value: {}", challenge);
		var response = Map.of("challenge", challenge);
		return HttpResponse.ok(response);
	}

	@Post("/webhook")
	public HttpResponse<String> webhook(HttpRequest<String> request, @Header("X-Exl-Signature") String signature) {
		LOG.info("Webhook received ('{}')", signature);
		String body = request.getBody().orElse("empty");
		LOG.trace("Signature: '{}' Body: '{}'", signature, body);
		return webhookHandlerService.processWebhook(body, signature);
	}
}
