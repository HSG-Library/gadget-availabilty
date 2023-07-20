package ch.unisg.biblio.systemlibrarian;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.time.ZoneId;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import ch.unisg.biblio.systemlibrarian.clients.AlmaClient;
import ch.unisg.biblio.systemlibrarian.clients.models.AlmaItemResponse;
import io.micronaut.runtime.EmbeddedApplication;
import io.micronaut.scheduling.cron.CronExpression;
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

	@Test
	void testCronExpression() {
		CronExpression cronExpression = CronExpression.create("0 */30 7-21 * * *");
		System.out.println(cronExpression.getExpression());
		var timeAfter = LocalDateTime.now().atZone(ZoneId.systemDefault());
		for (int i = 0; i < 5; i++) {
			var nextTime = cronExpression.nextTimeAfter(timeAfter);
			timeAfter = nextTime;
			System.out.println(nextTime);
		}
	}

	@MockBean(AlmaClient.class)
	AlmaClient almaClient() {
		AlmaClient almaClientMock = mock(AlmaClient.class);
		when(almaClientMock.getItems(any(), any(), any(), any(), any(), any()))
				.thenReturn(new AlmaItemResponse());
		return almaClientMock;
	}
}
