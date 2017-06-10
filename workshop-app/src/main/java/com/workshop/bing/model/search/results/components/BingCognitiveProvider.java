package com.workshop.bing.model.search.results.components;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class BingCognitiveProvider {
    String type;
    String name;

    @JsonProperty("_type")
    public void setType(String type) {
        this.type = type;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }
}
