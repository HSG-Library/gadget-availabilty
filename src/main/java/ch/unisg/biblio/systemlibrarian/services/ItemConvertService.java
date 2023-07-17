package ch.unisg.biblio.systemlibrarian.services;

import ch.unisg.biblio.systemlibrarian.clients.models.AlmaItem;
import ch.unisg.biblio.systemlibrarian.clients.models.AlmaItem.AlmaItemData;
import ch.unisg.biblio.systemlibrarian.controller.dtos.GadgetItem;
import io.micronaut.context.annotation.Value;
import jakarta.inject.Singleton;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Singleton
public class ItemConvertService {

	@Value("${gadget-availability.image-ext:.png}")
	private String imgExt;

	public List<GadgetItem> convert(final List<AlmaItem> items, final Locale locale) {
		Map<String, GadgetItem> groupedItems = new HashMap<>();
		items.stream()
				.map(AlmaItem::getItemData)
				.forEach(itemData -> {
					// if an item with the same enum A is in the map increment total and avail count
					groupedItems.computeIfPresent(itemData.getEnumerationA(), (key, currentItem) -> currentItem
							.incrementTotal()
							.addToAvailable(itemData.getBaseStatus().getValue())
							.updateDescription(getDescriptionForLanguage(itemData, locale))
							.addDetails(itemData));
					// if an item is not in the map, add it with total 1 and avail status
					groupedItems.putIfAbsent(itemData.getEnumerationA(), createGadgetItem(itemData, locale));
				});

		return List.copyOf(groupedItems.values());
	}

	private GadgetItem createGadgetItem(final AlmaItemData itemData, final Locale locale) {
		return new GadgetItem(
				1,
				itemData.getBaseStatus().getValue(),
				getTitleForLanguage(itemData, locale),
				convertImgId(itemData),
				getDescriptionForLanguage(itemData, locale),
				StringUtils.defaultString(itemData.getEnumerationA()),
				StringUtils.defaultString(itemData.getDescription()),
				itemData);
	}

	private String convertImgId(AlmaItemData itemData) {
		return StringUtils.defaultString(itemData.getEnumerationA()) + imgExt;
	}

	private String getTitleForLanguage(final AlmaItemData itemData, final Locale locale) {
		final String titleDeRaw = StringUtils.defaultString(itemData.getAlternativeCallNumber());
		final String titleDe = StringUtils.substring(titleDeRaw, 1, titleDeRaw.length() - 2);
		if ("en".equals(locale.getLanguage())) {
			return StringUtils.defaultIfBlank(itemData.getStatisticsNote1(), titleDe);
		}
		// de is default
		return titleDe;
	}

	private String getDescriptionForLanguage(final AlmaItemData itemData, final Locale locale) {
		final String descriptionDe = StringUtils.defaultString(itemData.getPublicNote());
		if ("en".equals(locale.getLanguage())) {
			return StringUtils.defaultIfBlank(itemData.getStatisticsNote2(), descriptionDe);
		}
		//de is default
		return descriptionDe;
	}
}
