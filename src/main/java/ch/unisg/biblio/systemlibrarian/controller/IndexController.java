package ch.unisg.biblio.systemlibrarian.controller;

import ch.unisg.biblio.systemlibrarian.AlmaClientConfig;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.views.View;
import jakarta.inject.Inject;

import java.util.Map;

@Controller()
public class IndexController {

	private final AlmaClientConfig config;

	@Inject
	public IndexController(AlmaClientConfig config) {
		this.config = config;
	}

	@View("index")
	@Get
	public HttpResponse<Map<String, Object>> index() {
		return HttpResponse.ok(Map.of(
				"mmsId", config.getMmsId(),
				"holdingId", config.getHoldingId()
		));
	}

	@View("info")
	@Get("/info")
	public HttpResponse<Map<String, Object>> info() {
		return HttpResponse.ok(Map.of(
				"mmsId", config.getMmsId(),
				"holdingId", config.getHoldingId()));
	}
}
