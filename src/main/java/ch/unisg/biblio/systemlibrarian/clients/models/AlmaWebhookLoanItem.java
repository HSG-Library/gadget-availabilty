package ch.unisg.biblio.systemlibrarian.clients.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.micronaut.core.annotation.Introspected;

@Introspected
public class AlmaWebhookLoanItem {
	@JsonProperty("id")
	private String id;
	@JsonProperty("action")
	private String action;
	@JsonProperty("event")
	private Event event;
	@JsonProperty("item_loan")
	private ItemLoan itemLoan;

	public String getId() {
		return this.id;
	}

	public String getAction() {
		return this.action;
	}

	public Event getEvent() {
		return this.event;
	}

	public ItemLoan getItemLoan() {
		return this.itemLoan;
	}

	@Introspected
	public static class Event {
		@JsonProperty("value")
		private String value;
		@JsonProperty("desc")
		private String desc;

		public String getValue() {
			return this.value;
		}

		public String getDesc() {
			return this.desc;
		}
	}

	@Introspected
	public static class ItemLoan {
		@JsonProperty("item_barcode")
		private String itemBarcode;
		@JsonProperty("loan_status")
		private String loanStatus;
		@JsonProperty("call_number")
		private String callNumber;
		@JsonProperty("process_status")
		private String processStatus;
		@JsonProperty("mms_id")
		private String mmsId;
		@JsonProperty("holding_id")
		private String holdingId;
		@JsonProperty("item_id")
		private String itemId;

		public String getItemBarcode() {
			return this.itemBarcode;
		}

		public String getLoanStatus() {
			return this.loanStatus;
		}

		public String getCallNumber() {
			return this.callNumber;
		}

		public String getProcessStatus() {
			return this.processStatus;
		}

		public String getMmsId() {
			return this.mmsId;
		}

		public String getHoldingId() {
			return this.holdingId;
		}

		public String getItemId() {
			return this.itemId;
		}
	}
}
