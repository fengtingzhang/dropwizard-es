package org.fengting.dropwizard.es.managed;

import io.dropwizard.configuration.ConfigurationFactory;
import io.dropwizard.jackson.Jackson;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.fengting.dropwizard.es.config.SearchConfiguration;
import org.fengting.dropwizard.es.manager.SearchManager;
import org.junit.Test;

import javax.validation.Validation;
import javax.validation.Validator;
import java.io.File;
import java.net.URL;

import static org.junit.Assert.*;

/**
 * Unit tests for {@link SearchManager}.
 */
public class SearchManagerTest {
    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
    private final ConfigurationFactory<SearchConfiguration> configFactory =
            new ConfigurationFactory(SearchConfiguration.class, validator, Jackson.newObjectMapper(), "dropwizard-es");

    @Test
    public void searchManagerCreatesClient() throws Exception {
        URL configFileUrl = this.getClass().getResource("/elasticsearch.yml");
        File configFile = new File(configFileUrl.toURI());
        SearchConfiguration config = configFactory.build(configFile);

        SearchManager managed = new SearchManager(config);
        managed.start();

        Client client = managed.obtainClient();

        assertNotNull(client);
        assertTrue(client instanceof TransportClient);

        final TransportClient transportClient = (TransportClient) client;
        assertEquals(1, transportClient.transportAddresses().size());

        managed.stop();
    }
}
