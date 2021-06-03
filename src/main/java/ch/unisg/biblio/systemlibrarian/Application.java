package ch.unisg.biblio.systemlibrarian;

import java.lang.invoke.MethodHandles;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.micronaut.runtime.Micronaut;

public class Application {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public static void main(String[] args) {
        if (System.getProperty("apiKey") == null) {
            LOG.error("API key not set (-DapiKey=\"<key>\")");
            System.err.println("API key not set (-DapiKey=\"<key>\")");
            System.exit(1);
        }
        Micronaut.run(Application.class, args);
    }
}
