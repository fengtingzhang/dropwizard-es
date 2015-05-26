package org.fengting.dropwizard.es.manager;

import io.dropwizard.lifecycle.Managed;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.fengting.dropwizard.es.config.SearchConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.elasticsearch.common.transport.InetSocketTransportAddress;

public class SearchManager implements Managed {
    private static final Logger logger = LoggerFactory.getLogger(SearchManager.class);

    private Client client;

    private final SearchConfiguration configuration;

    public SearchManager(SearchConfiguration configuration) {
        this.configuration = configuration;
    }

    public Client obtainClient() {
        return this.client;
    }

    public void start() throws Exception {
        logger.info(configuration.getHost());
        logger.info(configuration.getPort());
        logger.info(configuration.getCluster());

        Settings settings = ImmutableSettings.settingsBuilder()
                .put("cluster.name", configuration.getCluster()).build();

        this.client = new TransportClient(settings)
                .addTransportAddress(new InetSocketTransportAddress(configuration.getHost(),
                        Integer.parseInt(configuration.getPort())));
    }

    public void stop() throws Exception {
        this.client.close();
    }
}
