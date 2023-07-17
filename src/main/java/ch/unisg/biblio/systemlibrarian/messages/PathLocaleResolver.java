package ch.unisg.biblio.systemlibrarian.messages;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.server.util.locale.HttpAbstractLocaleResolver;
import io.micronaut.http.server.util.locale.HttpLocaleResolutionConfiguration;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.apache.commons.lang3.StringUtils;

import java.util.Locale;
import java.util.Optional;

@Singleton
public class PathLocaleResolver extends HttpAbstractLocaleResolver {
	@Inject
	public PathLocaleResolver(
			final HttpLocaleResolutionConfiguration httpLocaleResolutionConfiguration
	) {
		super(httpLocaleResolutionConfiguration);
	}

	@Override
	public Optional<Locale> resolve(HttpRequest<?> context) {
		final String path = context.getUri().getPath();
		if (StringUtils.startsWith(path, "/de")) {
			return Optional.of(Locale.GERMAN);
		}
		return Optional.of(Locale.ENGLISH);
	}

	@Override
	public int getOrder() {
		return HIGHEST_PRECEDENCE;
	}
}
