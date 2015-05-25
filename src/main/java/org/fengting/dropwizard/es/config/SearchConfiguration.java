package org.fengting.dropwizard.es.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import org.hibernate.validator.constraints.NotEmpty;

public class SearchConfiguration extends Configuration {
    @NotEmpty
    private String host = "localhost";

    @NotEmpty
    private String port = "9300";

    @NotEmpty
    private String cluster = "elasticsearch_brew";

    @JsonProperty
    public String getHost() {
        return host;
    }

    @JsonProperty
    public void setHost(String host) {
        this.host = host;
    }

    @JsonProperty
    public String getPort() {
        return port;
    }

    @JsonProperty
    public void setPort(String port) {
        this.port = port;
    }

    @JsonProperty
    public String getCluster() {
        return cluster;
    }

    @JsonProperty
    public void setCluser(String cluster) {
        this.cluster = cluster;
    }
}
