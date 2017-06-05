package bing.model.search.results.components;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BingCognitiveImageInsightsSourcesSummary {
    String shoppingSourcesCount;
    String recipeSourcesCount;

    @JsonProperty("shoppingSourcesCount")
    public void setShoppingSourcesCount(String shoppingSourcesCount) {
        this.shoppingSourcesCount = shoppingSourcesCount;
    }

    @JsonProperty("recipeSourcesCount")
    public void setRecipeSourcesCount(String recipeSourcesCount) {
        this.recipeSourcesCount = recipeSourcesCount;
    }
}
