package ch.unisg.biblio.systemlibrarian.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Singleton;

import org.apache.commons.lang3.StringUtils;

import ch.unisg.biblio.systemlibrarian.clients.models.AlmaItem;
import ch.unisg.biblio.systemlibrarian.clients.models.AlmaItem.AlmaItemData;
import ch.unisg.biblio.systemlibrarian.controller.dtos.GadgetItem;
import io.micronaut.context.annotation.Value;

@Singleton
public class ItemConvertService {

	@Value("${gadget-availability.image-ext:.png}")
	private String imgExt;

	public List<GadgetItem> convert(List<AlmaItem> items) {
		Map<String, GadgetItem> groupedItems = new HashMap<>();
		items.stream()
				.map(AlmaItem::getItemData)
				.forEach(itemData -> {
					groupedItems.putIfAbsent(itemData.getEnumerationA(), createGadgetItem(itemData));
					groupedItems.computeIfPresent(itemData.getEnumerationA(), (key, currentItem) -> {
						currentItem.incrementTotal();
						currentItem.addToAvailable(itemData.getBaseStatus().getValue());
						return currentItem;
					});
				});

		List<GadgetItem> gadgetItems = new ArrayList<>(groupedItems.values());
		gadgetItems.sort((o1, o2) -> {
			return o1.getDescription().compareTo(o2.getDescription());
		});

		return List.copyOf(gadgetItems);
	}

	private GadgetItem createGadgetItem(AlmaItemData itemData) {
		return new GadgetItem(
				1,
				itemData.getBaseStatus().getValue(),
				convertName(itemData),
				convertImgId(itemData),
				StringUtils.defaultIfBlank(itemData.getPublicNote(), "-"),
				StringUtils.defaultString(itemData.getEnumerationA()),
				StringUtils.defaultString(itemData.getDescription()));
	}

	private String convertName(AlmaItemData itemData) {
		String callno = StringUtils.defaultString(itemData.getAlternativeCallNumber());
		return StringUtils.substring(callno, 1, callno.length() - 2);
	}

	private String convertImgId(AlmaItemData itemData) {
		return StringUtils.defaultString(itemData.getEnumerationA()) + imgExt;
	}
}
