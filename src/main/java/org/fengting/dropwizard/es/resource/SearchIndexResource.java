package org.fengting.dropwizard.es.resource;

import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.elasticsearch.action.index.IndexResponse;
import org.fengting.dropwizard.es.api.Tweet;
import org.fengting.dropwizard.es.manager.SearchManager;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import com.fasterxml.jackson.databind.*;

import java.util.Date;

@Path("/index")
@Produces(MediaType.APPLICATION_JSON)
public class SearchIndexResource {
    private final SearchManager manager;

    ObjectMapper mapper = new ObjectMapper(); // create once, reuse

    public SearchIndexResource(SearchManager manager) {
        this.manager = manager;
    }

    @GET
    @Timed
    public String showIndexes() {
        //String json = getTestDataFromString();
        byte[] json = getTestDataFromJackson();
        IndexResponse response = manager.obtainClient().prepareIndex("twitter", "tweet")
                .setSource(json)
                .execute()
                .actionGet();

        // Index name
        String _index = response.getIndex();

        // Type name
        String _type = response.getType();

        // Document ID (generated or not)
        String _id = response.getId();

        // Version (if it's the first time you index this document, you will get: 1)
        long _version = response.getVersion();

        // isCreated() is true if the document is a new one, false if it has been updated
        boolean created = response.isCreated();

        return _index;
    }

    private String getTestDataFromString() {
        return "{" +
                "\"user\":\"kimchy\"," +
                "\"postDate\":\"2013-01-30\"," +
                "\"message\":\"trying out Elasticsearch\"" +
                "}";
    }

    private byte[] getTestDataFromJackson() {
        byte[] json = new byte[0];
        try {
            json = mapper.writeValueAsBytes(new Tweet("kimchy", new Date(), "trying out ElasticSearch"));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return json;
    }
}
