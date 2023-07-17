package ch.unisg.biblio.systemlibrarian;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Base64;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import ch.unisg.biblio.systemlibrarian.clients.AlmaClient;
import ch.unisg.biblio.systemlibrarian.clients.models.AlmaItemResponse;
import ch.unisg.biblio.systemlibrarian.controller.AdminController;
import ch.unisg.biblio.systemlibrarian.controller.IndexController;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.test.annotation.MockBean;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;

@MicronautTest(environments = {
		"test"
})
public class SecurityTest {

	@Inject
	@Client("/")
	HttpClient client;

	@Inject
	AdminController adminController;

	@Inject
	IndexController indexController;

	@MockBean(AlmaClient.class)
	AlmaClient almaClient() {
		AlmaClient almaClientMock = mock(AlmaClient.class);
		when(almaClientMock.getItems(any(), any(), any(), any(), any(), any()))
				.thenReturn(new AlmaItemResponse());
		return almaClientMock;
	}

	@Test
	public void testIndexAnonymous() {
		var response = client.toBlocking().exchange(HttpRequest.GET("/"));
		Assertions.assertEquals(HttpStatus.OK, response.status());
	}

	@Test
	public void testGadgetsAnonymous() {
		var response = client.toBlocking().exchange(HttpRequest.GET("/gadgets/de/all"));
		Assertions.assertEquals(HttpStatus.OK, response.status());
	}

	@Test
	public void testAdminAnonymous() {
		Assertions.assertThrows(HttpClientResponseException.class,
				() -> client.toBlocking().exchange(HttpRequest.GET("/admin/items")),
				"Forbidden	");
	}

	@Test
	public void testAdminAuthorized() {
		String credsEncoded = Base64.getEncoder().encodeToString("unit-test:test-unit".getBytes());
		var request = HttpRequest.GET("/admin/items")
				.header("Authorization", "Basic " + credsEncoded);
		var response = client.toBlocking().exchange(request);
		Assertions.assertEquals(HttpStatus.OK, response.status());
	}
}
