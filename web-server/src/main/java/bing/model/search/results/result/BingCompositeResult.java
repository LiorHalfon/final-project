package bing.model.search.results.result;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;

@JsonIgnoreProperties(value = {
        "VideoTotal",
        "VideoOffset",
        "SpellingSuggestionsTotal",
        "Video",
        "RelatedSearch",
        "SpellingSuggestions"
})
@Getter
public class BingCompositeResult extends BingResult {

    private Long webTotal;
    private Long webOffset;

    private Long imageTotal;
    private Long ImageOffset;

    private Long newsTotal;
    private Long newsOffset;

    private String alteredQuery;
    private String alterationOverrideQuery;

    private List<BingWebResult> web;
    private List<BingNewsResult> news;
    private List<BingImageResult> image;

    @JsonProperty("WebTotal")
    public void setWebTotal(Long webTotal) {
        this.webTotal = webTotal;
    }
    @JsonProperty("WebOffset")
    public void setWebOffset(Long webOffset) {
        this.webOffset = webOffset;
    }
    @JsonProperty("ImageTotal")
    public void setImageTotal(Long imageTotal) {
        this.imageTotal = imageTotal;
    }
    @JsonProperty("ImageOffset")
    public void setImageOffset(Long imageOffset) {
        ImageOffset = imageOffset;
    }
    @JsonProperty("NewsTotal")
    public void setNewsTotal(Long newsTotal) {
        this.newsTotal = newsTotal;
    }
    @JsonProperty("NewsOffset")
    public void setNewsOffset(Long newsOffset) {
        this.newsOffset = newsOffset;
    }
    @JsonProperty("AlteredQuery")
    public void setAlteredQuery(String alteredQuery) {
        this.alteredQuery = alteredQuery;
    }
    @JsonProperty("AlterationOverrideQuery")
    public void setAlterationOverrideQuery(String alterationOverrideQuery) {
        this.alterationOverrideQuery = alterationOverrideQuery;
    }
    @JsonProperty("Web")
    public void setWeb(List<BingWebResult> web) {
        this.web = web;
    }
    @JsonProperty("News")
    public void setNews(List<BingNewsResult> news) {
        this.news = news;
    }
    @JsonProperty("Image")
    public void setImage(List<BingImageResult> image) {
        this.image = image;
    }



}
