package ch.unisg.biblio.systemlibrarian.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import ch.unisg.biblio.systemlibrarian.clients.models.AlmaItem;
import ch.unisg.biblio.systemlibrarian.clients.models.AlmaItem.AlmaItemData;
import ch.unisg.biblio.systemlibrarian.controller.dtos.GadgetItem;
import io.micronaut.context.annotation.Value;
import jakarta.inject.Singleton;

@Singleton
public class ItemConvertService {

	@Value("${gadget-availability.image-ext:.png}")
	private String imgExt;

	public List<GadgetItem> convert(List<AlmaItem> items) {
		Map<String, GadgetItem> groupedItems = new HashMap<>();
		items.stream()
				.map(AlmaItem::getItemData)
				.forEach(itemData -> {
					// if an item with the same enum A is in the map increment total and avail count
					groupedItems.computeIfPresent(itemData.getEnumerationA(), (key, currentItem) -> {
						currentItem
								.incrementTotal()
								.addToAvailable(itemData.getBaseStatus().getValue())
								.updateNote(itemData.getPublicNote())
								.addDetails(itemData);
						return currentItem;
					});
					// if an item is not in the map, add it with total 1 and avail status
					groupedItems.putIfAbsent(itemData.getEnumerationA(), createGadgetItem(itemData));
				});

		return List.copyOf(groupedItems.values());
	}

	private GadgetItem createGadgetItem(AlmaItemData itemData) {
		return new GadgetItem(
				1,
				itemData.getBaseStatus().getValue(),
				convertName(itemData),
				convertImgId(itemData),
				StringUtils.defaultString(itemData.getPublicNote()),
				StringUtils.defaultString(itemData.getEnumerationA()),
				StringUtils.defaultString(itemData.getDescription()),
				itemData);
	}

	private String convertName(AlmaItemData itemData) {
		String callno = StringUtils.defaultString(itemData.getAlternativeCallNumber());
		return StringUtils.substring(callno, 1, callno.length() - 2);
	}

	private String convertImgId(AlmaItemData itemData) {
		return StringUtils.defaultString(itemData.getEnumerationA()) + imgExt;
	}
}
