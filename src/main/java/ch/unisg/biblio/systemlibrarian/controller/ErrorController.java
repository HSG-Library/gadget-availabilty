package ch.unisg.biblio.systemlibrarian.controller;

import io.micronaut.core.io.Writable;
import io.micronaut.http.*;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Error;
import io.micronaut.views.ViewsRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.Map;

@Controller("/error")
public class ErrorController {

	private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	private final ViewsRenderer<Map<String, String>, HttpRequest<?>> viewsRenderer;

	public ErrorController(ViewsRenderer<Map<String, String>, HttpRequest<?>> viewsRenderer) {
		this.viewsRenderer = viewsRenderer;
	}

	@Error(status = HttpStatus.NOT_FOUND, global = true)
	public MutableHttpResponse<Writable> error404(HttpRequest<?> request) {
		LOG.info("404 status at: '{}'", request.getUri());
		return HttpResponse.notFound(viewsRenderer.render("error/404", Map.of(), request))
				.contentType(MediaType.TEXT_HTML);
	}

	@Error(status = HttpStatus.INTERNAL_SERVER_ERROR, global = true)
	public MutableHttpResponse<Writable> error500(HttpRequest<?> request, Throwable e) {
		LOG.error("An error occurred: '{}'", request.getUri(), e);
		return HttpResponse.serverError(viewsRenderer.render("error/500", Map.of("msg", e.getClass().getSimpleName()), request))
				.contentType(MediaType.TEXT_HTML);
	}
}
