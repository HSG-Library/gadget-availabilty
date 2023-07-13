package ch.unisg.biblio.systemlibrarian.services;

import ch.unisg.biblio.systemlibrarian.AlmaClientConfig;
import ch.unisg.biblio.systemlibrarian.clients.AlmaClient;
import ch.unisg.biblio.systemlibrarian.clients.models.AlmaItem;
import ch.unisg.biblio.systemlibrarian.clients.models.AlmaItemResponse;
import jakarta.inject.Singleton;

import java.util.ArrayList;
import java.util.List;

@Singleton
public class ItemFetchService {

	private final AlmaClient almaClient;
	private final AlmaClientConfig config;

	public ItemFetchService(AlmaClient almaClient, AlmaClientConfig config) {
		this.almaClient = almaClient;
		this.config = config;
	}

	public List<AlmaItem> fetchAll() {
		int currentPage = 1;
		AlmaItemResponse initialPage = fetch(0);
		List<AlmaItem> pages = new ArrayList<>(initialPage.getItems());
		int totalRecordCount = initialPage.getTotalRecordCount();

		while (hasMoreItems(currentPage, totalRecordCount)) {
			List<AlmaItem> items = fetch(pageOffset(currentPage, totalRecordCount)).getItems();
			pages.addAll(items);
			currentPage++;
		}
		return pages;
	}

	public AlmaItemResponse fetch(int offset) {
		return almaClient.getItems(
				config.getMmsId(),
				config.getHoldingId(),
				config.getPageSize(),
				offset,
				config.getLocation(),
				config.getApiKey());
	}

	private int pageOffset(int currentPage, int totalRecordCount) {
		return Math.min(currentPage * config.getPageSize(), totalRecordCount);
	}

	private boolean hasMoreItems(int currentPage, int totalRecordCount) {
		return totalRecordCount > currentPage * config.getPageSize();
	}
}
