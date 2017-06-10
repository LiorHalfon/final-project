package com.workshop.bing.model.search.results;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

// This is a response for Images, News, Video searches - Web searches have a different format
@Data
public class BingCognitiveResponse<T> {
    String error;
    String type;
    String readLink;
    Integer totalEstimatedMatches;
    List<T> value;

    @JsonProperty("_type")
    public void setType(String type) {
        this.type = type;
    }

    @JsonProperty("readLink")
    public void setReadLink(String readLink) {
        this.readLink = readLink;
    }

    @JsonProperty("totalEstimatedMatches")
    public void setTotalEstimatedMatches(Integer totalEstimatedMatches) {
        this.totalEstimatedMatches = totalEstimatedMatches;
    }
}
