package ch.unisg.biblio.systemlibrarian;

import io.micronaut.context.annotation.ConfigurationProperties;
import io.micronaut.context.annotation.Requires;

@ConfigurationProperties(AlmaClientConfig.PREFIX)
@Requires(property = AlmaClientConfig.PREFIX)
public class AlmaClientConfig {
	public static final String PREFIX = "alma-api";

	private String mmsId;
	private String holdingId;
	private String apiKey;
	private String location;
	private Integer pageSize;

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

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
}
