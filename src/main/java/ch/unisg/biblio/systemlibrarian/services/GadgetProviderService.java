package ch.unisg.biblio.systemlibrarian.services;

import ch.unisg.biblio.systemlibrarian.clients.models.AlmaItem;
import ch.unisg.biblio.systemlibrarian.controller.dtos.GadgetItem;
import io.micronaut.context.event.ApplicationEventListener;
import io.micronaut.runtime.server.event.ServerStartupEvent;
import io.micronaut.scheduling.annotation.Scheduled;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Singleton
public class GadgetProviderService implements ApplicationEventListener<ServerStartupEvent> {

	private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
	private final ItemFetchService itemFetchService;
	private final ItemConvertService itemConvertService;
	private Map<String, AlmaItem> items = Collections.emptyMap();

	@Inject
	public GadgetProviderService(
			final ItemFetchService itemFetchService,
			final ItemConvertService itemConvertService) {
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
		return this.items.size();
	}

	public Map<String, AlmaItem> getAlmaItems() {
		return this.items;
	}

	public List<GadgetItem> getGadgets(final Locale locale) {
		final List<GadgetItem> gadgetItems = itemConvertService.convert(List.copyOf(items.values()), locale);
		return sortGadgetItems(gadgetItems);
	}

	public void updateAvailability(String barcode, boolean available) {
		LOG.info("Update availability of '{}' to '{}'", barcode, available);
		Optional<AlmaItem> item = Optional.ofNullable(items.get(barcode));
		// barcode unknown, reload complete list
		if (item.isEmpty()) {
			initGadgets();
			return;
		}
		// barcode known, update availability
		item.map(i -> i.updateAvailability(available))
				.ifPresent(i -> items.put(barcode, i));
	}

	private List<GadgetItem> sortGadgetItems(final Collection<GadgetItem> items) {
		List<GadgetItem> gadgetItems = new ArrayList<>(items);
		gadgetItems.sort(Comparator.comparing(GadgetItem::getSortKey));
		return gadgetItems;
	}
}
