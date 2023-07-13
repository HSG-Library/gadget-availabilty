package ch.unisg.biblio.systemlibrarian.controller.dtos;

import ch.unisg.biblio.systemlibrarian.clients.models.AlmaItem.AlmaItemData;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class GadgetItem {
	private final String callno;
	private final String volume;
	private final String description;
	private final List<AlmaItemData> details;
	private int total;
	private int available;
	@JsonProperty("img_id")
	private String imgId;
	private String note;

	public GadgetItem(
			int total,
			int available,
			String callno,
			String imgId,
			String note,
			String volume,
			String description,
			AlmaItemData almaItemData) {
		this.total = total;
		this.available = available;
		this.callno = callno;
		this.imgId = imgId;
		this.note = note;
		this.volume = volume;
		this.description = description;
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

	public String getCallno() {
		return this.callno;
	}

	public String getImgId() {
		return this.imgId;
	}

	public String getNote() {
		return this.note;
	}

	public GadgetItem updateNote(String note) {
		if (StringUtils.isBlank(this.note)) {
			this.note = note;
		}
		return this;
	}

	public String getVolume() {
		return this.volume;
	}

	public String getDescription() {
		return this.description;
	}

	public List<AlmaItemData> getDetails() {
		return List.copyOf(this.details);
	}

	public GadgetItem addDetails(AlmaItemData details) {
		this.details.add(details);
		return this;
	}
}
