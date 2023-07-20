package ch.unisg.biblio.systemlibrarian.services;

import jakarta.inject.Singleton;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Singleton
public class HMACService {

	public String calculateHMACBase64(String algorithm, String data, String secret)
			throws NoSuchAlgorithmException, InvalidKeyException {
		SecretKeySpec secretKeySpec = new SecretKeySpec(secret.getBytes(), algorithm);
		Mac mac = Mac.getInstance(algorithm);
		mac.init(secretKeySpec);
		byte[] hmac = mac.doFinal(data.getBytes());
		return Base64.getEncoder().encodeToString(hmac);
	}

	public boolean checkSignature(String algorithm, String data, String secret, String signature)
			throws InvalidKeyException, NoSuchAlgorithmException {
		String hmacBase64 = this.calculateHMACBase64(algorithm, data, secret);
		return hmacBase64.equals(signature);
	}
}
