package ch.unisg.biblio.systemlibrarian;

import java.lang.management.ManagementFactory;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.reactivestreams.Publisher;

import io.micronaut.context.env.PropertySource;
import io.micronaut.core.async.publisher.Publishers;
import io.micronaut.management.endpoint.info.InfoSource;
import jakarta.inject.Singleton;

@Singleton
public class CustomInfoSource implements InfoSource {
	@Override
	public Publisher<PropertySource> getSource() {
		long uptimeMillis = System.currentTimeMillis() - ManagementFactory.getRuntimeMXBean().getStartTime();

		Map<String, Object> info = new HashMap<>();
		info.put("java.version", System.getProperty("java.version"));
		info.put("app.uptime.millis", uptimeMillis);
		info.put("app.uptime.human", formatDuration(uptimeMillis));
		return Publishers.just(PropertySource.of("custom-info", info));
	}

	private String formatDuration(long millis) {
		long days = TimeUnit.MILLISECONDS.toDays(millis);
		millis -= TimeUnit.DAYS.toMillis(days);
		long hours = TimeUnit.MILLISECONDS.toHours(millis);
		millis -= TimeUnit.HOURS.toMillis(hours);
		long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
		millis -= TimeUnit.MINUTES.toMillis(minutes);
		long seconds = TimeUnit.MILLISECONDS.toSeconds(millis);

		return String.format("%d days, %d hours, %d minutes, %d seconds", days, hours, minutes, seconds);
	}
}