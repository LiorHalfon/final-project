package bing.model.search.results;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class BingCognitiveWebPageResponse {
    String webSearchUrl;
    Long totalEstimatedMatches;
    List<BingCognitiveWebResult> value;

    @JsonProperty("webSearchUrl")
    public void setWebSearchUrl(String webSearchUrl) { this.webSearchUrl = webSearchUrl; }

    @JsonProperty("totalEstimatedMatches")
    public void setTotalEstimatedMatches(Long totalEstimatedMatches) { this.totalEstimatedMatches = totalEstimatedMatches; }

    @JsonProperty("value")
    public void setValue(List<BingCognitiveWebResult> value) { this.value = value; }
}
