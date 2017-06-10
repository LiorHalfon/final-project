package com.workshop.bing.model.search.results;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class BingCognitiveNewsResponse extends BingCognitiveResponse<BingCognitiveNewsResult> {

    @JsonProperty
    public void setValue(List<BingCognitiveNewsResult> value) {
        this.value = value;
    }
}
