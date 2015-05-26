package org.fengting.dropwizard.es.resource;

import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.action.index.IndexResponse;
import org.fengting.dropwizard.es.api.Tweet;
import org.fengting.dropwizard.es.manager.SearchManager;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.Date;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

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
    public String addTweets() {
        //String json = getTestDataFromString();
        byte[] json = getTestDataFromJackson(new Tweet("kimchy", new Date(), "trying out ElasticSearch"));
        IndexResponse response = manager.obtainClient().prepareIndex("twitter", "tweet")
                .setSource(json)
                .execute()
                .actionGet();

        try {
            return jsonBuilder()
                    .startObject()
                    .field("index", response.getIndex())
                    .field("type", response.getType())
                    .field("id", response.getId())
                    .field("version", response.getVersion())
                    .field("created", response.isCreated())
                    .endObject().string();
        } catch (IOException e) {
            return e.toString();
        }
    }

    private String getTestDataFromString() {
        return "{" +
                "\"user\":\"kimchy\"," +
                "\"postDate\":\"2013-01-30\"," +
                "\"another\":\"anoterfield\"," +
                "\"message\":\"trying out Elasticsearch\"" +
                "}";
    }

    private byte[] getTestDataFromJackson(Object value) {
        byte[] json = new byte[0];
        try {
            json = mapper.writeValueAsBytes(value);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return json;
    }
}
