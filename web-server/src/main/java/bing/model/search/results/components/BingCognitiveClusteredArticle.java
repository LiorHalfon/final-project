package bing.model.search.results.components;

import bing.model.search.results.BingCognitiveNewsResult;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class BingCognitiveClusteredArticle extends BingCognitiveNewsResult {
    String category;

    @JsonProperty("category")
    public void setCategory(String category) {
        this.category = category;
    }

}