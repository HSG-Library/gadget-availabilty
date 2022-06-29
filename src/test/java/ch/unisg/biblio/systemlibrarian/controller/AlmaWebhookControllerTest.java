package ch.unisg.biblio.systemlibrarian.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import ch.unisg.biblio.systemlibrarian.clients.AlmaClient;
import ch.unisg.biblio.systemlibrarian.clients.models.AlmaItemResponse;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MutableHttpRequest;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.annotation.MockBean;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;

@MicronautTest(environments = {
		"test"
})
class AlmaWebhookControllerTest {

	private static final String JSON_BODY = "{\"id\":\"4716072711972883959\",\"action\":\"LOAN\",\"institution\":{\"value\":\"41SLSP_HSG\",\"desc\":\"Universität St.Gallen\"},\"time\":\"2022-01-12T08:08:56.198Z\",\"event\":{\"value\":\"LOAN_CREATED\",\"desc\":\"Loan created\"},\"item_loan\":{\"loan_id\":\"2410068350005506\",\"circ_desk\":{\"value\":\"DEFAULT_CIRC_DESK\",\"desc\":\"Circulation Desk\"},\"return_circ_desk\":{\"value\":\"\",\"desc\":null,\"link\":null},\"library\":{\"value\":\"HSG\",\"desc\":\"HSG\"},\"user_id\":\"868256628995@eduid.ch\",\"item_barcode\":\"HM00007496\",\"due_date\":\"2022-02-09T22:00:00Z\",\"loan_status\":\"ACTIVE\",\"loan_date\":\"2022-01-12T08:08:54.624Z\",\"returned_by\":{\"value\":\"\",\"link\":null},\"process_status\":\"NORMAL\",\"mms_id\":\"992149060105506\",\"holding_id\":\"2248954680005506\",\"item_id\":\"2348954660005506\",\"title\":\"Egalité les garanties de la Constitution fédérale du 18 avril 1999 Etienne Grisel\",\"author\":\"Grisel, Etienne\",\"description\":null,\"publication_year\":\"2000\",\"location_code\":{\"value\":\"OG\",\"name\":\"Upper Floor\"},\"item_policy\":{\"value\":\"01\",\"description\":\"01 Loan 28 days\"},\"call_number\":\"PL 440 G869\",\"request_id\":{\"value\":\"2406591220005506\",\"link\":\"/almaws/v1/users/868256628995@eduid.ch/requests/2406591220005506\"},\"renewable\":true,\"last_renew_status\":{\"value\":\"\",\"desc\":\"\"}},\"bib\":null,\"holding\":null,\"item\":null,\"portfolio\":null,\"representation\":null}";
	private static final String SIGNATURE = "t7Dj0H8qWqjRR54hYkznOFAGkhreUWyaHcvZ4Yvb0Ac=";

	@Inject
	AlmaWebhookController almaWebhookController;

	@Inject
	@Client("/")
	HttpClient client;

	@MockBean(AlmaClient.class)
	AlmaClient almaClient() {
		AlmaClient almaClientMock = mock(AlmaClient.class);
		when(almaClientMock.getItems(any(), any(), any(), any(), any(), any()))
				.thenReturn(new AlmaItemResponse());
		return almaClientMock;
	}

	@Test
	public void testWebhookChallange() {
		MutableHttpRequest<String> webhookChallengeGetRequest = HttpRequest
				.GET("/alma/webhook?challenge=schwalbenweih");
		String response = client.toBlocking().retrieve(webhookChallengeGetRequest, String.class);
		System.out.println(response);
		Assertions.assertEquals("{\"challenge\":\"schwalbenweih\"}", response);
	}

	@Test
	public void testRecieveWebhook() {
		MutableHttpRequest<String> webhookPostRequest = HttpRequest
				.POST("/alma/webhook", JSON_BODY)
				.header("X-Exl-Signature", SIGNATURE);
		HttpResponse<String> response = client.toBlocking().exchange(webhookPostRequest, String.class);
		System.out.println(response);
		Assertions.assertEquals(HttpResponse.ok().status(), response.status());
	}
}
