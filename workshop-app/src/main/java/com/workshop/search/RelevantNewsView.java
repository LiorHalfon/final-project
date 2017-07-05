package com.workshop.search;

import com.aylien.textapi.responses.Sentiment;
import com.aylien.textapi.responses.TaxonomyCategory;

import java.net.URL;

public class RelevantNewsView {
    private String title;
    private String summary;
    private Sentiment sentiment;
    private String url;
    private String domain;
    private String classifications;
    private String imageUrl;
    private String sentimentImageUrl;

    private static final String thumbUpUrl = "https://cdn2.iconfinder.com/data/icons/social-buttons-2/512/thumb_up-32.png";
    private static final String thumbDownUrl = "https://cdn2.iconfinder.com/data/icons/social-buttons-2/512/thumb_down-32.png";

    public RelevantNewsView(RelevantNews news) {
        title = news.article.getTitle();
        String[] images = news.article.getImages();
        if(images.length < 1)
        {
            imageUrl = "https://upload.wikimedia.org/wikipedia/commons/4/47/Comic_image_missing.png"; //TODO: "no image" image url
        }
        else{
            imageUrl = news.article.getImages()[0];
        }


        summary = "";
        String[] sentences = news.summary.getSentences();
        if(sentences != null) {
            for (String s : sentences) {
                summary += s + " \n\n";
            }
        }

        sentiment = news.sentiment;
        sentimentImageUrl = sentiment.getPolarity().equalsIgnoreCase("positive")?
                thumbUpUrl : thumbDownUrl;

        url = news.url.toString();
        domain = getDomainName(news.url);

        classifications = "";
        TaxonomyCategory[] categories = news.classifications.getCategories();
        if(categories != null) {
            for (TaxonomyCategory c : categories) {
                classifications += c.getLabel() + " " + c.getId() + "\n\n";
            }
        }
    }

    private static String getDomainName(URL url){
        String domain = url.getHost();
        return domain.startsWith("www.") ? domain.substring(4) : domain;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public Sentiment getSentiment() {
        return sentiment;
    }

    public void setSentiment(Sentiment sentiment) {
        this.sentiment = sentiment;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getClassifications() {
        return classifications;
    }

    public void setClassifications(String classifications) {
        this.classifications = classifications;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getSentimentImageUrl() {
        return sentimentImageUrl;
    }
}
