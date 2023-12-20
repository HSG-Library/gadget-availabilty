package ch.unisg.biblio.systemlibrarian.security;

import io.micronaut.context.annotation.Replaces;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MutableHttpResponse;
import io.micronaut.http.server.exceptions.response.ErrorResponseProcessor;
import io.micronaut.security.authentication.AuthorizationException;
import io.micronaut.security.authentication.DefaultAuthorizationExceptionHandler;
import io.micronaut.security.config.RedirectConfiguration;
import io.micronaut.security.config.RedirectService;
import io.micronaut.security.errors.PriorToLoginPersistence;
import jakarta.inject.Singleton;

import static io.micronaut.http.HttpHeaders.WWW_AUTHENTICATE;
import static io.micronaut.http.HttpStatus.FORBIDDEN;
import static io.micronaut.http.HttpStatus.UNAUTHORIZED;

@Singleton
@Replaces(DefaultAuthorizationExceptionHandler.class)
public class DefaultAuthorizationExceptionHandlerReplacement extends DefaultAuthorizationExceptionHandler {

	/**
	 * @param errorResponseProcessor  ErrorResponse processor API
	 * @param redirectConfiguration   Redirect configuration
	 * @param redirectService         Redirection Service
	 * @param priorToLoginPersistence Persistence mechanism to redirect to prior login url
	 */
	public DefaultAuthorizationExceptionHandlerReplacement(ErrorResponseProcessor<?> errorResponseProcessor, RedirectConfiguration redirectConfiguration, RedirectService redirectService, @Nullable PriorToLoginPersistence priorToLoginPersistence) {
		super(errorResponseProcessor, redirectConfiguration, redirectService, priorToLoginPersistence);
	}

	@Override
	protected MutableHttpResponse<?> httpResponseWithStatus(
			HttpRequest request,
			AuthorizationException e) {
		if (e.isForbidden()) {
			return HttpResponse.status(FORBIDDEN);
		}
		return HttpResponse.status(UNAUTHORIZED)
				.header(WWW_AUTHENTICATE, "Basic realm=\"Gadgets Admin\"");
	}
}
