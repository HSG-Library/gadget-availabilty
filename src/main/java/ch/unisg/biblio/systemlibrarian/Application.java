package ch.unisg.biblio.systemlibrarian;

import io.micronaut.runtime.Micronaut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;

public class Application {

	private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	public static void main(String[] args) {
		if (System.getProperty("alma-api.apiKey") == null) {
			LOG.error("API key not set (-Dalma-api.apiKey=\"<key>\")");
			System.err.println("API key not set (-Dalma-api.apiKey=\"<key>\")");
			System.exit(1);
		}
		Micronaut.run(Application.class, args);
	}
}
