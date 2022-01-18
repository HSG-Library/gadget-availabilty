package ch.unisg.biblio.systemlibrarian.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import ch.unisg.biblio.systemlibrarian.controller.dtos.GadgetItem;
import ch.unisg.biblio.systemlibrarian.services.GatgetProviderService;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;

@Controller("/gadgets")
public class ItemController {

	private GatgetProviderService gadgetProviderService;

	public ItemController(GatgetProviderService gatgetProviderService) {
		this.gadgetProviderService = gatgetProviderService;
	}

	@Get("/all")
	public Collection<GadgetItem> all() {
		return sortGadgetItems(gadgetProviderService.getItems());
	}

	private List<GadgetItem> sortGadgetItems(Collection<GadgetItem> items) {
		List<GadgetItem> gadgetItems = new ArrayList<>(items);
		gadgetItems.sort((o1, o2) -> {
			return o1.getDescription().compareTo(o2.getDescription());
		});
		return gadgetItems;
	}

}
