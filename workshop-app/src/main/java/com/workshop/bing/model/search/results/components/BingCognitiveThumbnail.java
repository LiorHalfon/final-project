package com.workshop.bing.model.search.results.components;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class BingCognitiveThumbnail {
    String contentUrl;
    Integer width;
    Integer height;

    @JsonProperty("contentUrl")
    public void setContentUrl(String contentUrl) {
        this.contentUrl = contentUrl;
    }

    @JsonProperty("width")
    public void setWidth(Integer width) {
        this.width = width;
    }

    @JsonProperty("height")
    public void setHeight(Integer height) {
        this.height = height;
    }
}
