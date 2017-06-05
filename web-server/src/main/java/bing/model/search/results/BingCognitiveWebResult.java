package bing.model.search.results;

import bing.model.search.results.components.BingCognitiveDeepLink;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class BingCognitiveWebResult extends BingCognitiveResult {
    String id;
    String name;
    String url;
    String displayUrl;
    String snippet;
    List<BingCognitiveDeepLink> deepLinks;
    String dateLastCrawled;

    @JsonProperty("id")
    public void setId(String id) { this.id = id; }

    @JsonProperty("name")
    public void setName(String name) { this.name = name; }

    @JsonProperty("url")
    public void setUrl(String url) { this.url = url; }

    @JsonProperty("displayUrl")
    public void setDisplayUrl(String displayUrl) { this.displayUrl = displayUrl; }

    @JsonProperty("snippet")
    public void setSnippet(String snippet) { this.snippet = snippet; }

    @JsonProperty("deepLinks")
    public void setDeepLinks(List<BingCognitiveDeepLink> deepLinks) { this.deepLinks = deepLinks; }

    @JsonProperty("dateLastCrawled")
    public void setDateLastCrawled(String dateLastCrawled) { this.dateLastCrawled = dateLastCrawled; }
}

/*
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
