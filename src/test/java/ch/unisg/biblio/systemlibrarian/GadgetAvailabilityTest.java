package ch.unisg.biblio.systemlibrarian;

import static org.mockito.ArgumentMatchers.any;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;

import ch.unisg.biblio.systemlibrarian.clients.AlmaClient;
import ch.unisg.biblio.systemlibrarian.clients.models.AlmaItemResponse;
import io.micronaut.runtime.EmbeddedApplication;
import io.micronaut.test.annotation.MockBean;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;

@MicronautTest(environments = {
		"test"
})
class GadgetAvailabilityTest {

	@Inject
	EmbeddedApplication<?> application;

	@Test
	void testItWorks() {
		Assertions.assertTrue(application.isRunning());
	}

	@MockBean(AlmaClient.class)
	AlmaClient almaClient() {
		AlmaClient almaClientMock = mock(AlmaClient.class);
		when(almaClientMock.getItems(any(),any(), any(), any(), any()))
				.thenReturn(new AlmaItemResponse());
		return almaClientMock;
	}
}
