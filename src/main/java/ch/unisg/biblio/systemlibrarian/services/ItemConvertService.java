package ch.unisg.biblio.systemlibrarian.services;

import ch.unisg.biblio.systemlibrarian.clients.models.AlmaItem;
import ch.unisg.biblio.systemlibrarian.clients.models.AlmaItem.AlmaItemData;
import ch.unisg.biblio.systemlibrarian.controller.dtos.GadgetItem;
import io.micronaut.context.annotation.Value;
import jakarta.inject.Singleton;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
					groupedItems.computeIfPresent(itemData.getEnumerationA(), (key, currentItem) -> currentItem
							.incrementTotal()
							.addToAvailable(itemData.getBaseStatus().getValue())
							.updateDescription(itemData.getPublicNote())
							.addDetails(itemData));
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
		String alternativeCallNumber = StringUtils.defaultString(itemData.getAlternativeCallNumber());
		return StringUtils.substring(alternativeCallNumber, 1, alternativeCallNumber.length() - 2);
	}

	private String convertImgId(AlmaItemData itemData) {
		return StringUtils.defaultString(itemData.getEnumerationA()) + imgExt;
	}
}
