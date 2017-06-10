package com.workshop.bing.model.search.results.components;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BingCognitiveTopic {
    String name;

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }
}
