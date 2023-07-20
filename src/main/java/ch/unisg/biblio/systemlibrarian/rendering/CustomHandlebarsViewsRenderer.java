package ch.unisg.biblio.systemlibrarian.rendering;

import com.github.jknack.handlebars.Context;
import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import io.micronaut.context.annotation.Replaces;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.core.io.Writable;
import io.micronaut.core.io.scan.ClassPathResourceLoader;
import io.micronaut.core.util.ArgumentUtils;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.server.util.locale.HttpLocaleResolver;
import io.micronaut.views.ViewUtils;
import io.micronaut.views.ViewsConfiguration;
import io.micronaut.views.exceptions.ViewRenderingException;
import io.micronaut.views.handlebars.HandlebarsViewsRenderer;
import io.micronaut.views.handlebars.HandlebarsViewsRendererConfiguration;
import jakarta.inject.Singleton;

import java.util.Locale;
import java.util.Map;
import java.util.Optional;

@Singleton
@Replaces(HandlebarsViewsRenderer.class)
public final class CustomHandlebarsViewsRenderer<T> extends HandlebarsViewsRenderer<T> {

	private final HttpLocaleResolver localeResolver;

	public CustomHandlebarsViewsRenderer(
			final ViewsConfiguration viewsConfiguration,
			final ClassPathResourceLoader resourceLoader,
			final HandlebarsViewsRendererConfiguration handlebarsViewsRendererConfiguration,
			final Handlebars handlebars,
			final HttpLocaleResolver localeResolver,
			final I18nHelper i18nHelper
	) {
		super(viewsConfiguration, resourceLoader, handlebarsViewsRendererConfiguration, handlebars);
		this.localeResolver = localeResolver;
		this.handlebars.registerHelper("i18n", i18nHelper);
	}

	@NonNull
	@Override
	public Writable render(@NonNull String viewName,
	                       @Nullable T data,
	                       @Nullable HttpRequest<?> request) {
		ArgumentUtils.requireNonNull("viewName", viewName);
		return (writer) -> {
			String location = viewLocation(viewName);
			try {
				final Template template = handlebars.compile(location);
				final Optional<Locale> locale = Optional.ofNullable(request)
						.flatMap(localeResolver::resolve);
				final Context context = Context
						.newBuilder(data)
						.combine(Map.of(
								"request", Optional.ofNullable(request),
								"locale", locale
						))
						.build();
				template.apply(context, writer);
			} catch (Throwable e) {
				throw new ViewRenderingException("Error rendering Handlebars view [" + viewName + "]: " + e.getMessage(), e);
			}
		};
	}

	private String viewLocation(final String name) {
		return folder + ViewUtils.normalizeFile(name, extension());
	}

	private String extension() {
		return handlebarsViewsRendererConfiguration.getDefaultExtension();
	}
}
