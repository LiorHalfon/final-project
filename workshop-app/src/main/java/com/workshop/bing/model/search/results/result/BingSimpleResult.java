package com.workshop.bing.model.search.results.result;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public abstract class BingSimpleResult extends BingResult {
    protected String title;
    protected String bingWrappedUrl;
    protected String displayUrl;


    @JsonProperty("Title")
    public void setTitle(String title) {
        this.title = title;
    }

    @JsonProperty("DisplayUrl")
    public void setDisplayUrl(String displayUrl) {
        this.displayUrl = displayUrl;
    }

    @JsonProperty("BingWrappedUrl")
    public void setBingWrappedUrl(String bingWrappedUrl) {
        this.bingWrappedUrl = bingWrappedUrl;
    }
}
