package ch.unisg.biblio.systemlibrarian.clients.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.micronaut.core.annotation.Introspected;

@Introspected
public class AlmaItem {
	@JsonProperty("item_data")
	private AlmaItemData itemData;

	public AlmaItemData getItemData() {
		return this.itemData;
	}

	public AlmaItem updateAvailability(boolean available) {
		if (available) {
			this.itemData.baseStatus.setValue(1);
		} else {
			this.itemData.baseStatus.setValue(0);
		}
		return this;
	}

	@Introspected
	public static class AlmaItemData {
		@JsonProperty("pid")
		private String pid;

		@JsonProperty("barcode")
		private String barcode;

		@JsonProperty("enumeration_a")
		private String enumerationA;

		@JsonProperty("enumeration_b")
		private String enumerationB;

		@JsonProperty("base_status")
		private BaseStatus baseStatus;

		@JsonProperty("alternative_call_number")
		private String alternativeCallNumber;

		@JsonProperty("public_note")
		private String publicNote;

		@JsonProperty("description")
		private String description;

		@JsonProperty("statistics_note_1")
		private String statisticsNote1;

		@JsonProperty("statistics_note_2")
		private String statisticsNote2;

		public String getAlternativeCallNumber() {
			return alternativeCallNumber;
		}

		public String getBarcode() {
			return barcode;
		}

		public BaseStatus getBaseStatus() {
			return baseStatus;
		}

		public String getEnumerationA() {
			return enumerationA;
		}

		public String getEnumerationB() {
			return enumerationB;
		}

		public String getPid() {
			return pid;
		}

		public String getPublicNote() {
			return publicNote;
		}

		public String getDescription() {
			return description;
		}

		public String getStatisticsNote1() {
			return statisticsNote1;
		}

		public String getStatisticsNote2() {
			return statisticsNote2;
		}
	}

	@Introspected
	public static class BaseStatus {
		@JsonProperty("value")
		private int value;

		@JsonProperty("desc")
		private String desc;

		public String getDesc() {
			return desc;
		}

		public int getValue() {
			return value;
		}

		private void setValue(int value) {
			this.value = value;
		}
	}
}
