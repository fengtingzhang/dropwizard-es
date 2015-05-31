package org.fengting.dropwizard.es.config;

import io.dropwizard.configuration.ConfigurationException;
import io.dropwizard.configuration.ConfigurationFactory;
import io.dropwizard.jackson.Jackson;
import org.junit.Test;

import javax.validation.Validation;
import javax.validation.Validator;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for {@link SearchConfiguration}.
 */
public class SearchConfigurationTest {
    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
    private final ConfigurationFactory<SearchConfiguration> configFactory =
            new ConfigurationFactory(SearchConfiguration.class, validator, Jackson.newObjectMapper(), "dropwizard-es");

    @Test
    public void defaultConfigShouldBeValid() throws IOException, ConfigurationException {
        configFactory.build();
    }

    @Test(expected = ConfigurationException.class)
    public void invalidCausesAnException() throws IOException, ConfigurationException, URISyntaxException {
        URL configFileUrl = this.getClass().getResource("/elasticsearch_error.yml");
        File configFile = new File(configFileUrl.toURI());
        configFactory.build(configFile);
    }

    @Test
    public void configurationIsValid() throws IOException, ConfigurationException, URISyntaxException {
        URL configFileUrl = this.getClass().getResource("/elasticsearch.yml");
        File configFile = new File(configFileUrl.toURI());
        SearchConfiguration configuration = configFactory.build(configFile);

        assertEquals(configuration.getCluster(), "elasticsearch_brew");
        assertEquals(configuration.getHost(), "localhost");
        assertEquals(configuration.getPort(), "9300");
    }
}
