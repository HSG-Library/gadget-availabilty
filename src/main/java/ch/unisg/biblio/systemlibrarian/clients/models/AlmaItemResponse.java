package ch.unisg.biblio.systemlibrarian.clients.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.micronaut.core.annotation.Introspected;

import java.util.List;

@Introspected
public class AlmaItemResponse {

	@JsonProperty("total_record_count")
	private final Integer totalRecordCount = 0;

	@JsonProperty("item")
	private final List<AlmaItem> items = List.of();

	public Integer getTotalRecordCount() {
		return totalRecordCount;
	}

	public List<AlmaItem> getItems() {
		return items;
	}
}
