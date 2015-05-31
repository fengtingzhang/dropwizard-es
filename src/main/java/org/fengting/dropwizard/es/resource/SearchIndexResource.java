package org.fengting.dropwizard.es.resource;

import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.action.index.IndexResponse;
import org.fengting.dropwizard.es.manager.SearchManager;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import org.json.JSONArray;

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
        //byte[] json = getTestDataFromJackson(new Tweet("kimchy", new Date(), "trying out ElasticSearch"));
        JSONArray indexResults = new JSONArray();
        try {
            JSONArray data = getTestDataFromString();
            for (int i = 0; i < data.length(); i++) {
                IndexResponse response = manager.obtainClient().prepareIndex("twitter", "tweet")
                        .setSource(data.get(i).toString())
                        .execute()
                        .actionGet();
                indexResults.put(jsonBuilder()
                        .startObject()
                        .field("index", response.getIndex())
                        .field("type", response.getType())
                        .field("id", response.getId())
                        .field("version", response.getVersion())
                        .field("created", response.isCreated())
                        .endObject().string());
            }
        } catch (IOException e) {
            return e.toString();
        }

        return indexResults.toString();
    }

    private JSONArray getTestDataFromString() throws IOException {
        InputStream is = getClass().getClassLoader().getResourceAsStream("data.json");
        BufferedReader streamReader = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
        StringBuilder responseStrBuilder = new StringBuilder();

        String inputStr;
        while ((inputStr = streamReader.readLine()) != null) {
            responseStrBuilder.append(inputStr);
        }
        JSONArray results = new JSONArray(responseStrBuilder.toString());
        return results;

//
//        return "{" +
//                "\"user\":\"kimchy\"," +
//                "\"postDate\":\"2013-01-30\"," +
//                "\"another\":\"anoterfield\"," +
//                "\"message\":\"trying out Elasticsearch\"" +
//                "}";
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
