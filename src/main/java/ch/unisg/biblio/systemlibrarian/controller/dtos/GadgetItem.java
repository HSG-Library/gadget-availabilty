package ch.unisg.biblio.systemlibrarian.controller.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GadgetItem {
	private int total;
	private int available;
	private String callno;
	@JsonProperty("img_id")
	private String imgId;
	private String note;
	private String volume;

	public GadgetItem(
			int total,
			int available,
			String callno,
			String imgId,
			String note,
			String volume) {
		this.total = total;
		this.available = available;
		this.callno = callno;
		this.imgId = imgId;
		this.note = note;
		this.volume = volume;
	}

	public int getTotal() {
		return this.total;
	}

	public void incrementTotal() {
		this.total++;
	}

	public int getAvailable() {
		return available;
	}

	public void addToAvailable(int increment) {
		this.available = this.available + increment;
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

	public String getVolume() {
		return this.volume;
	}
}
