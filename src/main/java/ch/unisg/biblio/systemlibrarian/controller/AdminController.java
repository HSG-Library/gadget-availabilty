package ch.unisg.biblio.systemlibrarian.controller;

import java.lang.invoke.MethodHandles;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.unisg.biblio.systemlibrarian.services.GadgetProviderService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import jakarta.inject.Inject;

@Secured(SecurityRule.IS_AUTHENTICATED)
@Controller("/admin")
public class AdminController {

	private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	private GadgetProviderService gadgetProviderService;

	@Inject
	public AdminController(GadgetProviderService gadGatgetProviderService) {
		this.gadgetProviderService = gadGatgetProviderService;
	}

	@Get("/reset")
	public HttpResponse<String> resetGadgets() {
		LOG.warn("Manually reset gadgets");
		int gadgetsCount = this.gadgetProviderService.initGadgets();
		LOG.info("Recieved '{}' gadgets", gadgetsCount);
		return HttpResponse.ok("Reset '" + gadgetsCount + "' gadgets");
	}

	@Get("/items")
	public HttpResponse<Object> almaItems() {
		var items = this.gadgetProviderService.getAlmaItems();
		return HttpResponse.ok(items);
	}
}