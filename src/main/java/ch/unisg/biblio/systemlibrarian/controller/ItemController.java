package ch.unisg.biblio.systemlibrarian.controller;

import java.util.List;

import ch.unisg.biblio.systemlibrarian.clients.models.AlmaItem;
import ch.unisg.biblio.systemlibrarian.controller.dtos.GadgetItem;
import ch.unisg.biblio.systemlibrarian.services.ItemConvertService;
import ch.unisg.biblio.systemlibrarian.services.ItemFetchService;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;

@Controller("/gadgets")
public class ItemController {

	private ItemFetchService itemFetchService;
	private ItemConvertService itemConvertService;

	public ItemController(ItemFetchService itemFetchService, ItemConvertService itemConvertService) {
		this.itemFetchService = itemFetchService;
		this.itemConvertService = itemConvertService;
	}

	@Get("/all")
	public List<GadgetItem> all() {
		List<AlmaItem> almaItems = itemFetchService.fetchAll();
		return itemConvertService.convert(almaItems);
	}
}
