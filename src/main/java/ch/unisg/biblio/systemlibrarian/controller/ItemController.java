package ch.unisg.biblio.systemlibrarian.controller;

import ch.unisg.biblio.systemlibrarian.controller.dtos.GadgetItem;
import ch.unisg.biblio.systemlibrarian.services.GadgetProviderService;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

@Controller("/gadgets")
public class ItemController {

	private final GadgetProviderService gadgetProviderService;

	public ItemController(GadgetProviderService gadgetProviderService) {
		this.gadgetProviderService = gadgetProviderService;
	}

	@Get("/all")
	public Collection<GadgetItem> all() {
		return sortGadgetItems(gadgetProviderService.getGadgets());
	}

	private List<GadgetItem> sortGadgetItems(Collection<GadgetItem> items) {
		List<GadgetItem> gadgetItems = new ArrayList<>(items);
		gadgetItems.sort(Comparator.comparing(GadgetItem::getDescription));
		return gadgetItems;
	}

}
