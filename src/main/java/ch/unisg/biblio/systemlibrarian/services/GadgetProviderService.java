package ch.unisg.biblio.systemlibrarian.services;

import java.lang.invoke.MethodHandles;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.unisg.biblio.systemlibrarian.clients.models.AlmaItem;
import ch.unisg.biblio.systemlibrarian.controller.dtos.GadgetItem;
import io.micronaut.context.event.ApplicationEventListener;
import io.micronaut.runtime.server.event.ServerStartupEvent;
import io.micronaut.scheduling.annotation.Scheduled;
import jakarta.inject.Singleton;

@Singleton
public class GadgetProviderService implements ApplicationEventListener<ServerStartupEvent> {

	private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	private Map<String, AlmaItem> items = Collections.emptyMap();
	private List<GadgetItem> gadgets = Collections.emptyList();
	private ItemFetchService itemFetchService;
	private ItemConvertService itemConvertService;

	public GadgetProviderService(ItemFetchService itemFetchService, ItemConvertService itemConvertService) {
		this.itemFetchService = itemFetchService;
		this.itemConvertService = itemConvertService;
	}

	@Override
	public void onApplicationEvent(ServerStartupEvent event) {
		LOG.info("ServerStartupEvent: call initGadgets()");
		initGadgets();
	}

	/**
	 * Re-initialize gadgets (call Alma API).
	 * 
	 * Scheduled every 30min from 07:00 to 20:00.
	 */
	@Scheduled(cron = "0 */30 7-20 * * *")
	public void reInitGadgets() {
		LOG.info("Scheduled: call initGadgets()");
		initGadgets();
	}

	public int initGadgets() {
		LOG.info("Initialize Gadgets");
		List<AlmaItem> almaItems = itemFetchService.fetchAll();
		this.items = almaItems.stream()
				.collect(Collectors.toMap(
						i -> i.getItemData().getBarcode(),
						Function.identity()));
		List<GadgetItem> gadgetItems = itemConvertService.convert(almaItems);
		this.gadgets = List.copyOf(gadgetItems);
		return this.gadgets.size();
	}

	public Map<String, AlmaItem> getAlmaItems() {
		return this.items;
	}

	public List<GadgetItem> getGadgets() {
		return this.gadgets;
	}

	public void updateAvailability(String barcode, boolean available) {
		LOG.info("Update availabilty of '{}' to '{}'", barcode, available);
		Optional<AlmaItem> item = Optional.ofNullable(items.get(barcode));
		// barcode unknown, reload complete list
		if (item.isEmpty()) {
			initGadgets();
			return;
		}
		// barcode known, update availabity
		item.map(i -> i.updateAvaliabilty(available))
				.ifPresent(i -> items.put(barcode, i));
		// convert and sort almaItems
		List<GadgetItem> gadgetItems = itemConvertService.convert(List.copyOf(items.values()));
		this.gadgets = List.copyOf(gadgetItems);
	}
}
