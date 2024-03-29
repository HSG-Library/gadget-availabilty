package ch.unisg.biblio.systemlibrarian.security;


import io.micronaut.context.annotation.ConfigurationProperties;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.security.authentication.AuthenticationProvider;
import io.micronaut.security.authentication.AuthenticationRequest;
import io.micronaut.security.authentication.AuthenticationResponse;
import jakarta.inject.Singleton;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

@ConfigurationProperties("admin")
@Singleton
public class AuthenticationProviderUserPassword<T> implements AuthenticationProvider<T> {

	private String username;
	private String password;

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public Publisher<AuthenticationResponse> authenticate(
			@Nullable final T httpRequest,
			final AuthenticationRequest<?, ?> authenticationRequest) {
		return Flux.create(emitter -> {
			if (authenticationRequest.getIdentity().equals(username) &&
					authenticationRequest.getSecret().equals(this.password)) {
				emitter.next(AuthenticationResponse.success((String) authenticationRequest.getIdentity()));
				emitter.complete();
			} else {
				emitter.error(AuthenticationResponse.exception());
			}
		}, FluxSink.OverflowStrategy.ERROR);
	}
}
