package ch.unisg.biblio.systemlibrarian.services;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class HMACServiceTest {

	@Test
	void testCalculateHMAC() throws InvalidKeyException, NoSuchAlgorithmException {
		HMACService hmacService = new HMACService();
		String algorithm = "HmacSHA256";
		String data = "{\"id\":\"295097218278611079\",\"action\":\"LOAN\",\"institution\":{\"value\":\"41SLSP_HSG\",\"desc\":\"Universität St.Gallen\"},\"time\":\"2022-01-11T08:34:33.674Z\",\"event\":{\"value\":\"LOAN_CREATED\",\"desc\":\"Loan created\"},\"item_loan\":{\"loan_id\":\"2406564260005506\",\"circ_desk\":{\"value\":\"DEFAULT_CIRC_DESK\",\"desc\":\"Circulation Desk\"},\"return_circ_desk\":{\"value\":\"\",\"desc\":null,\"link\":null},\"library\":{\"value\":\"HSG\",\"desc\":\"HSG\"},\"user_id\":\"257166317188@eduid.ch\",\"item_barcode\":\"HM00337722\",\"due_date\":\"2022-02-08T22:00:00Z\",\"loan_status\":\"ACTIVE\",\"loan_date\":\"2022-01-11T08:34:32.771Z\",\"returned_by\":{\"value\":\"\",\"link\":null},\"process_status\":\"NORMAL\",\"mms_id\":\"994889360105506\",\"holding_id\":\"2236603930005506\",\"item_id\":\"2336603630005506\",\"title\":\"Kleinmaterialien-Ausleihe\",\"author\":null,\"description\":\"LSK, 8\",\"location_code\":{\"value\":\"RESE\",\"name\":\"Circulation desk\"},\"item_policy\":{\"value\":\"01\",\"description\":\"01 Loan 28 days\"},\"call_number\":\"Kleinmaterialien\",\"renewable\":true,\"last_renew_status\":{\"value\":\"\",\"desc\":\"\"}},\"bib\":null,\"holding\":null,\"item\":null,\"portfolio\":null,\"representation\":null}";
		String key = "123456";
		String signature = "2vZUZK4aYiy+UGuBNnmA+95oFJ4T7RYlzo9rRS3aLfE=";
		String hmac = hmacService.calculateHMACBase64(algorithm, data, key);
		System.out.println("HMAC: '" + hmac + "'");
		Assertions.assertEquals(signature, hmac);
	}
}
