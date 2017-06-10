package com.workshop.bing.model.search.results.result;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.Date;

@Getter
public class BingNewsResult extends BingArticleResult {

    protected String source;
    protected Date date;

    @JsonProperty("Source")
    public void setSource(String source) {
        this.source = source;
    }

    @JsonProperty("Date")
    public void setDate(Date date) {
        this.date = date;
    }
}
