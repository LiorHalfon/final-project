package bing.model.search.results;

import bing.model.search.results.components.BingCognitiveClusteredArticle;
import bing.model.search.results.components.BingCognitiveImage;
import bing.model.search.results.components.BingCognitiveProvider;
import bing.model.search.results.components.BingCognitiveTopic;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class BingCognitiveNewsResult extends BingCognitiveResult {
    String name;
    String url;
    BingCognitiveImage image;
    String description;
    List<BingCognitiveProvider> provider;
    String datePublished;
    List<BingCognitiveTopic> topics;
    List<BingCognitiveClusteredArticle> clusteredArticles;

    @JsonProperty("name")
    public void setName(String name) { this.name = name;}

    @JsonProperty("url")
    public void setUrl(String url) { this.url = url;}

    @JsonProperty("image")
    public void setImage(BingCognitiveImage image) { this.image = image;}

    @JsonProperty("description")
    public void setDescription(String description) { this.description = description;}

    @JsonProperty("provider")
    public void setProvider(List<BingCognitiveProvider> provider) { this.provider = provider;}

    @JsonProperty("datePublished")
    public void setDatePublished(String datePublished) { this.datePublished = datePublished;}

    @JsonProperty("topics")
    public void setTopics(List<BingCognitiveTopic> topics) { this.topics = topics;}

    @JsonProperty("clusteredArticles")
    public void setClusteredArticles(List<BingCognitiveClusteredArticle> clusteredArticles) { this.clusteredArticles = clusteredArticles;}
}
