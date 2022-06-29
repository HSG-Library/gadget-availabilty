package ch.unisg.biblio.systemlibrarian.clients.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.micronaut.core.annotation.Introspected;

@Introspected
public class AlmaItemResponse {

	@JsonProperty("total_record_count")
	private Integer totalRecordCount = 0;

	@JsonProperty("item")
	private List<AlmaItem> items = List.of();

	public Integer getTotalRecordCount() {
		return totalRecordCount;
	}

	public List<AlmaItem> getItems() {
		return items;
	}
}
