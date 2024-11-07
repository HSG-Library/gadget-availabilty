package ch.unisg.biblio.systemlibrarian.controller;

import ch.unisg.biblio.systemlibrarian.AlmaClientConfig;
import ch.unisg.biblio.systemlibrarian.controller.dtos.GadgetItem;
import ch.unisg.biblio.systemlibrarian.services.GadgetProviderService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.views.View;
import jakarta.inject.Inject;

import java.net.URI;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Controller()
public class IndexController {

	private final AlmaClientConfig config;
	private GadgetProviderService gadgetProviderService;

	@Inject
	public IndexController(
			final AlmaClientConfig config,
			final GadgetProviderService gadgetProviderService
	) {
		this.config = config;
		this.gadgetProviderService = gadgetProviderService;
	}

	@Get
	public HttpResponse<Map<String, Object>> index(final Locale locale) {
		final URI languagePage;
		if (locale.getLanguage().equals("de")) {
			languagePage = URI.create("/de");
		} else {
			languagePage = URI.create("/en");
		}
		return HttpResponse.redirect(languagePage);
	}

	@View("index")
	@Get("de")
	public HttpResponse<Map<String, Object>> de(final Locale locale) {
		return HttpResponse.ok(Map.of(
				"common", commonInfo(locale),
				"gadgets", getGadgets(locale)));
	}

	@View("index")
	@Get("en")
	public HttpResponse<Map<String, Object>> en(final Locale locale) {
		return HttpResponse.ok(Map.of(
				"common", commonInfo(locale),
				"gadgets", getGadgets(locale)));
	}

	@View("info")
	@Get("en/info")
	public HttpResponse<Map<String, Object>> infoEn(final Locale locale) {
		return HttpResponse.ok(Map.of("common", commonInfo(locale)));
	}

	@View("info")
	@Get("de/info")
	public HttpResponse<Map<String, Object>> infoDe(final Locale locale) {
		return HttpResponse.ok(Map.of("common", commonInfo(locale)));
	}

	private Map<String, Object> commonInfo(final Locale locale) {
		return Map.of(
				"mmsId", config.getMmsId(),
				"holdingId", config.getHoldingId(),
				"lang", locale.getLanguage()
		);
	}

	private Map<String, List<GadgetItem>> getGadgets(final Locale locale) {
		return Map.of("all", gadgetProviderService.getGadgets(locale));
	}
}
