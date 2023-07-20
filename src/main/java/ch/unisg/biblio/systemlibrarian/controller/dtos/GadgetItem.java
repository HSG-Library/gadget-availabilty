package ch.unisg.biblio.systemlibrarian.controller.dtos;

import ch.unisg.biblio.systemlibrarian.clients.models.AlmaItem.AlmaItemData;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class GadgetItem {
	private final String title;
	private final String id;
	private final String sortKey;
	private final List<AlmaItemData> details;
	private int total;
	private int available;
	@JsonProperty("img_id")
	private String imgId;
	private String description;

	public GadgetItem(
			int total,
			int available,
			String title,
			String imgId,
			String description,
			String id,
			String sortKey,
			AlmaItemData almaItemData) {
		this.total = total;
		this.available = available;
		this.title = title;
		this.imgId = imgId;
		this.description = description;
		this.id = id;
		this.sortKey = sortKey;
		this.details = new ArrayList<>();
		details.add(almaItemData);
	}

	public int getTotal() {
		return this.total;
	}

	public GadgetItem incrementTotal() {
		this.total++;
		return this;
	}

	public int getAvailable() {
		return this.available;
	}

	public GadgetItem addToAvailable(int increment) {
		this.available = this.available + increment;
		return this;
	}

	public String getTitle() {
		return this.title;
	}

	public String getImgId() {
		return this.imgId;
	}

	public String getDescription() {
		return this.description;
	}

	public GadgetItem updateDescription(String description) {
		if (StringUtils.isBlank(this.description)) {
			this.description = description;
		}
		return this;
	}

	public String getId() {
		return this.id;
	}

	public String getSortKey() {
		return this.sortKey;
	}

	public List<AlmaItemData> getDetails() {
		return List.copyOf(this.details);
	}

	public GadgetItem addDetails(AlmaItemData details) {
		this.details.add(details);
		return this;
	}
}
