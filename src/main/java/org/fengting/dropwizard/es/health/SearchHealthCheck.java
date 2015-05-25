package org.fengting.dropwizard.es.health;

import com.codahale.metrics.health.HealthCheck;
import org.elasticsearch.action.admin.cluster.health.ClusterHealthRequest;
import org.elasticsearch.action.admin.cluster.health.ClusterHealthResponse;
import org.fengting.dropwizard.es.manager.SearchManager;

public class SearchHealthCheck extends HealthCheck {
    private SearchManager clientManager;

    public SearchHealthCheck(SearchManager clientManager) {
        this.clientManager = clientManager;
    }

    @Override
    protected Result check() throws Exception {
        ClusterHealthResponse clusterIndexHealths = clientManager.obtainClient()
                .admin().cluster().health(new ClusterHealthRequest()).actionGet();

        switch (clusterIndexHealths.getStatus()) {
            case GREEN:
                return HealthCheck.Result.healthy();
            case YELLOW:
                return HealthCheck.Result.unhealthy("Cluster yellow");
            case RED:
            default:
                return HealthCheck.Result.unhealthy("Cluster red", clusterIndexHealths);

        }
    }
}
