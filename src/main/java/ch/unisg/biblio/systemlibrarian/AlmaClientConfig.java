package ch.unisg.biblio.systemlibrarian;

import javax.annotation.PostConstruct;

import io.micronaut.context.annotation.ConfigurationProperties;
import io.micronaut.context.annotation.Requires;

@ConfigurationProperties(AlmaClientConfig.PREFIX)
@Requires(property = AlmaClientConfig.PREFIX)
public class AlmaClientConfig {
	public static final String PREFIX = "alma-api";

	private String mmsId;
	private String holdingId;
	private String apiKey;

	@PostConstruct
	public void init() {
		this.apiKey = System.getProperty("apiKey");
	}

	public String getMmsId() {
		return mmsId;
	}

	public String getApiKey() {
		return apiKey;
	}

	public String getHoldingId() {
		return holdingId;
	}

	public void setHoldingId(String holdingId) {
		this.holdingId = holdingId;
	}

	public void setMmsId(String mmsId) {
		this.mmsId = mmsId;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}
}
