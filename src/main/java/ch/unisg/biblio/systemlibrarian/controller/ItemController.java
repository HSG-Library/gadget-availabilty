package ch.unisg.biblio.systemlibrarian.controller;

import ch.unisg.biblio.systemlibrarian.controller.dtos.GadgetItem;
import ch.unisg.biblio.systemlibrarian.services.GadgetProviderService;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;

import java.util.Collection;
import java.util.Locale;

@Controller("/gadgets")
public class ItemController {

	private final GadgetProviderService gadgetProviderService;

	public ItemController(
			final GadgetProviderService gadgetProviderService
	) {
		this.gadgetProviderService = gadgetProviderService;
	}

	@Get("/{lang}/all")
	public Collection<GadgetItem> all(
			final @PathVariable String lang
	) {
		return gadgetProviderService.getGadgets(Locale.forLanguageTag(lang));
	}
}
