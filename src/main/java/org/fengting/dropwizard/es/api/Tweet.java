package org.fengting.dropwizard.es.api;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

public class Tweet {
    private String user;

    private Date postDate;

    private String message;

    public Tweet() {
        // Jackson deserialization
    }

    public Tweet(String user, Date postDate, String message) {
        this.user = user;
        this.postDate = postDate;
        this.message = message;
    }

    @JsonProperty
    public String getUser() {
        return user;
    }


    @JsonProperty
    public Date getPostDate() {
        return postDate;
    }

    @JsonProperty
    public String getMessage() {
        return message;
    }
}
