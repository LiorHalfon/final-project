package bing.model.search.results.components;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class BingCognitiveImage {
    BingCognitiveThumbnail thumbnail;

    @JsonProperty("thumbnail")
    public void BingCognitiveThumbnail(BingCognitiveThumbnail thumbnail) {
        this.thumbnail = thumbnail;
    }
}
