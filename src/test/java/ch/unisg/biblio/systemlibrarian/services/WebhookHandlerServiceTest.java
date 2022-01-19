package ch.unisg.biblio.systemlibrarian.services;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import ch.unisg.biblio.systemlibrarian.clients.AlmaClient;
import ch.unisg.biblio.systemlibrarian.clients.models.AlmaItemResponse;
import ch.unisg.biblio.systemlibrarian.clients.models.AlmaWebhookLoanItem;
import io.micronaut.http.HttpResponse;
import io.micronaut.test.annotation.MockBean;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;

@MicronautTest(environments = {
		"test"
})
public class WebhookHandlerServiceTest {

	private static final String JSON_BODY = "{\"id\":\"4716072711972883959\",\"action\":\"LOAN\",\"institution\":{\"value\":\"41SLSP_HSG\",\"desc\":\"Universität St.Gallen\"},\"time\":\"2022-01-12T08:08:56.198Z\",\"event\":{\"value\":\"LOAN_CREATED\",\"desc\":\"Loan created\"},\"item_loan\":{\"loan_id\":\"2410068350005506\",\"circ_desk\":{\"value\":\"DEFAULT_CIRC_DESK\",\"desc\":\"Circulation Desk\"},\"return_circ_desk\":{\"value\":\"\",\"desc\":null,\"link\":null},\"library\":{\"value\":\"HSG\",\"desc\":\"HSG\"},\"user_id\":\"868256628995@eduid.ch\",\"item_barcode\":\"HM00007496\",\"due_date\":\"2022-02-09T22:00:00Z\",\"loan_status\":\"ACTIVE\",\"loan_date\":\"2022-01-12T08:08:54.624Z\",\"returned_by\":{\"value\":\"\",\"link\":null},\"process_status\":\"NORMAL\",\"mms_id\":\"992149060105506\",\"holding_id\":\"2248954680005506\",\"item_id\":\"2348954660005506\",\"title\":\"Egalité les garanties de la Constitution fédérale du 18 avril 1999 Etienne Grisel\",\"author\":\"Grisel, Etienne\",\"description\":null,\"publication_year\":\"2000\",\"location_code\":{\"value\":\"OG\",\"name\":\"Upper Floor\"},\"item_policy\":{\"value\":\"01\",\"description\":\"01 Loan 28 days\"},\"call_number\":\"PL 440 G869\",\"request_id\":{\"value\":\"2406591220005506\",\"link\":\"/almaws/v1/users/868256628995@eduid.ch/requests/2406591220005506\"},\"renewable\":true,\"last_renew_status\":{\"value\":\"\",\"desc\":\"\"}},\"bib\":null,\"holding\":null,\"item\":null,\"portfolio\":null,\"representation\":null}";
	private static final String SIGNATURE = "t7Dj0H8qWqjRR54hYkznOFAGkhreUWyaHcvZ4Yvb0Ac=";

	@Inject
	WebhookHandlerService webhookHandlerService;

	@MockBean(AlmaClient.class)
	AlmaClient almaClient() {
		AlmaClient almaClientMock = mock(AlmaClient.class);
		when(almaClientMock.getItems(any(), any(), any(), any(), any()))
				.thenReturn(new AlmaItemResponse());
		return almaClientMock;
	}

	@Test
	void testProcessWebhook() {
		var response = webhookHandlerService.processWebhook(JSON_BODY, SIGNATURE);
		Assertions.assertEquals(HttpResponse.ok().code(), response.code());
	}

	@Test
	void testConvert() {
		Optional<AlmaWebhookLoanItem> itemOptional = webhookHandlerService.convert(JSON_BODY);
		Assertions.assertTrue(itemOptional.isPresent());
		var item = itemOptional.get();
		Assertions.assertEquals("4716072711972883959", item.getId());
		Assertions.assertEquals("LOAN_CREATED", item.getEvent().getValue());
		Assertions.assertEquals("HM00007496", item.getItemLoan().getItemBarcode());
	}
}
