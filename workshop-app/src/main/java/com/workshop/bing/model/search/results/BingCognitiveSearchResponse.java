package com.workshop.bing.model.search.results;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
// This is a response for Web searches - Images, News, Video searches have a different format
public class BingCognitiveSearchResponse {
    String error;
    String type;
    BingCognitiveWebPageResponse webPages;

    @JsonProperty("_type")
    public void setType(String type) {
        this.type = type;
    }

    @JsonProperty("webPages")
    public void setWebPages(BingCognitiveWebPageResponse webPages) { this.webPages = webPages; }
}

/* Example:
{
    "_type" : "SearchResponse",
    "webPages" : {
        "webSearchUrl" : "",
        "totalEstimatedMatches" : 15200000,
        "value" : [{
            "id" : "",
            "name" : "",
            "url" : "",
            "displayUrl" : "",
            "snippet" : "",
            "deepLinks" : [{
                "name" : "",
                "url" : "",
                "snippet" : "."
            },
            "dateLastCrawled" : ""
        }]
    },
    . . .
}

 */