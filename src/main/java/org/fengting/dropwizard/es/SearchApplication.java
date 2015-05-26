package org.fengting.dropwizard.es;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.fengting.dropwizard.es.config.SearchConfiguration;
import org.fengting.dropwizard.es.health.SearchHealthCheck;
import org.fengting.dropwizard.es.manager.SearchManager;
import org.fengting.dropwizard.es.resource.SearchIndexResource;
import org.fengting.dropwizard.es.resource.SearchQueryResource;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

public class SearchApplication extends Application<SearchConfiguration> {
    private static final Logger logger = LoggerFactory.getLogger(SearchApplication.class);

    public static void main(String[] args) throws Exception {
        new SearchApplication().run(args);
    }

    @Override
    public String getName() {
        return "dropwizard-es";
    }

    @Override
    public void initialize(Bootstrap<SearchConfiguration> ElasticSearchConfigurationBootstrap) {
    }

    @Override
    public void run(SearchConfiguration config, Environment environment) throws Exception {
        SearchManager searchManager = new SearchManager(config);
        environment.lifecycle().manage(searchManager);

        logger.info("Adding Index Resource");
        final SearchIndexResource indexResource = new SearchIndexResource(searchManager);
        environment.jersey().register(indexResource);

        logger.info("Adding Query Resource");
        final SearchQueryResource queryResource = new SearchQueryResource(searchManager);
        environment.jersey().register(queryResource);

        logger.info("Adding Health Check");
        final SearchHealthCheck esHealthCheck = new SearchHealthCheck(searchManager);
        environment.healthChecks().register("elasticsearch", esHealthCheck);
    }
}
