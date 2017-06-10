package com.workshop.bing.model.search.results.result;

import com.workshop.bing.model.BingMetadata;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public abstract class BingResult {

    protected BingMetadata metadata;

    protected String id;


    @JsonProperty("__metadata")
    public void setMetadata(BingMetadata metadata) {
        this.metadata = metadata;
    }

    @JsonProperty("ID")
    public void setId(String id) {
        this.id = id;
    }

}
