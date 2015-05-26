package org.fengting.dropwizard.es.resource;

import com.codahale.metrics.annotation.Timed;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.common.xcontent.ToXContent;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.QueryBuilders;
import org.fengting.dropwizard.es.manager.SearchManager;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.IOException;

@Path("/search")
@Produces(MediaType.APPLICATION_JSON)
public class SearchQueryResource {
    private final SearchManager manager;

    public SearchQueryResource(SearchManager manager) {
        this.manager = manager;
    }

    @GET
    @Timed
    public String showTweets() {
        SearchResponse response = manager.obtainClient().prepareSearch("twitter")
                .setTypes("tweet")
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(QueryBuilders.termQuery("user", "kimchy"))             // Query
                .execute()
                .actionGet();

        try {
            XContentBuilder builder = XContentFactory.jsonBuilder();
            builder.startObject();
            response.toXContent(builder, ToXContent.EMPTY_PARAMS);
            builder.endObject();
            builder.close();

            return builder.string();
        } catch (IOException e) {
            return e.toString();
        }
    }
}
