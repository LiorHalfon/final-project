package com.workshop.bing.model.search.results.result;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public abstract class BingArticleResult extends BingSimpleResult {
    protected String description;
    protected String url;

    @JsonProperty("Description")
    public void setDescription(String description) {
        this.description = description;
    }

    @JsonProperty("Url")
    public void setUrl(String url) {
        this.url = url;
    }
}
