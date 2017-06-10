package com.workshop.bing.model.search.results.components;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.workshop.bing.model.search.results.BingCognitiveNewsResult;
import lombok.Data;

@Data
public class BingCognitiveClusteredArticle extends BingCognitiveNewsResult {
    String category;

    @JsonProperty("category")
    public void setCategory(String category) {
        this.category = category;
    }

}