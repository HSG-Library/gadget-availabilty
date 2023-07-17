package ch.unisg.biblio.systemlibrarian.rendering;

import ch.unisg.biblio.systemlibrarian.messages.TranslationService;
import com.github.jknack.handlebars.Helper;
import com.github.jknack.handlebars.Options;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import java.util.Locale;
import java.util.Optional;

@Singleton
public class I18nHelper implements Helper<String> {

	private final TranslationService translationService;

	@Inject
	public I18nHelper(TranslationService translationService) {
		this.translationService = translationService;
	}

	@Override
	public Object apply(String context, Options options) {
		final Optional<Locale> locale = options.get("locale");
		return translationService.translate(context, locale.orElse(Locale.ENGLISH));
	}
}
