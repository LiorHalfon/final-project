package bing.model.search.results.components;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class BingCognitiveDeepLink {
    String name;
    String url;
    String snippet;

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("url")
    public void setUrl(String url) {
        this.url = url;
    }

    @JsonProperty("snippet")
    public void setSnippet(String snippet) {
        this.snippet = snippet;
    }

}
