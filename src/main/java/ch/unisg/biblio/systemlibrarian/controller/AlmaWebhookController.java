package ch.unisg.biblio.systemlibrarian.controller;

import java.lang.invoke.MethodHandles;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.unisg.biblio.systemlibrarian.services.WebhookHandlerService;
import io.micronaut.context.annotation.ConfigurationProperties;
import io.micronaut.context.annotation.Requires;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Header;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.QueryValue;
import jakarta.inject.Inject;

@ConfigurationProperties("HMAC")
@Requires(property = "HMAC")
@Controller("/alma")
public class AlmaWebhookController {

	private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	private WebhookHandlerService webhookHandlerService;

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
		LOG.info("Webhook recieved ('{}')", signature);
		String body = request.getBody().orElse("empty");
		LOG.trace("Signature: '{}' Body: '{}'", signature, body);
		return webhookHandlerService.processWebhook(body, signature);
	}
}
