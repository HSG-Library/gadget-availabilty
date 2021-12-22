package ch.unisg.biblio.systemlibrarian.services;

import java.util.ArrayList;
import java.util.List;

import ch.unisg.biblio.systemlibrarian.AlmaClientConfig;
import ch.unisg.biblio.systemlibrarian.clients.AlmaClient;
import ch.unisg.biblio.systemlibrarian.clients.models.AlmaItem;
import ch.unisg.biblio.systemlibrarian.clients.models.AlmaItemResponse;
import io.micronaut.context.annotation.Value;
import jakarta.inject.Singleton;

@Singleton
public class ItemFetchService {

	private AlmaClient almaClient;
	private AlmaClientConfig config;

	@Value("${alma-api.page-size:100}")
	private int pageSize;

	public ItemFetchService(AlmaClient almaClient, AlmaClientConfig config) {
		this.almaClient = almaClient;
		this.config = config;
	}

	public List<AlmaItem> fetchAll() {
		int currentPage = 1;
		List<AlmaItem> pages = new ArrayList<>();
		AlmaItemResponse initalPage = fetch(0);
		pages.addAll(initalPage.getItems());
		int totalRecordCount = initalPage.getTotalRecordCount();

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
				pageSize,
				offset,
				config.getApiKey());
	}

	private int pageOffset(int currentPage, int totalRecordCount) {
		return Math.min(currentPage * pageSize, totalRecordCount);
	}

	private boolean hasMoreItems(int currentPage, int totalRecordCount) {
		return totalRecordCount > currentPage * pageSize;
	}
}
