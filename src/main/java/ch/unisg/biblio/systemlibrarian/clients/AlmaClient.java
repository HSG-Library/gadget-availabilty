package ch.unisg.biblio.systemlibrarian.clients;

import ch.unisg.biblio.systemlibrarian.clients.models.AlmaItemResponse;
import io.micronaut.http.HttpHeaders;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Header;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.QueryValue;
import io.micronaut.http.client.annotation.Client;

@Client("${alma-api.url}")
@Header(name = HttpHeaders.ACCEPT, value = MediaType.APPLICATION_JSON)
public interface AlmaClient {

	@Get("/bibs/{mmsId}/holdings/{holdingId}/items?order_by=none&direction=desc&view=brief")
	AlmaItemResponse getItems(
			@PathVariable String mmsId,
			@PathVariable String holdingId,
			@QueryValue("limit") Integer limit,
			@QueryValue("offset") Integer offset,
			@QueryValue("current_location") String currentLocation,
			@QueryValue("apiKey") String apiKey);
}
