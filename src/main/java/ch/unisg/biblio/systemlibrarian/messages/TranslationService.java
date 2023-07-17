package ch.unisg.biblio.systemlibrarian.messages;

import io.micronaut.context.MessageSource;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import java.util.Locale;

@Singleton
public class TranslationService {

	private final MessageSource messageSource;

	@Inject
	public TranslationService(
			final MessageSource messageSource
	) {
		this.messageSource = messageSource;
	}

	public String translate(final String key, final Locale locale) {
		return messageSource.getMessage(key, key, locale);
	}
}
