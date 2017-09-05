package com.workshop.search;

import com.aylien.textapi.responses.Sentiment;
import com.aylien.textapi.responses.TaxonomyCategory;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class RelevantNewsView {
    private int id;
    private int index;
    private String title;
    private String summary;
    private Sentiment sentiment;
    private String url;
    private String domain;
    private List<String> classifications;
    private String imageUrl;
    private String sentimentImageUrl;

    private static final String positiveFaceUrl = "https://cdn2.iconfinder.com/data/icons/emoticons-17/24/emoticons-01-32.png";
    private static final String negativeFaceUrl = "https://cdn2.iconfinder.com/data/icons/emoticons-17/24/emoticons-02-32.png";
    private static final String neutralFaceUrl = "https://cdn2.iconfinder.com/data/icons/emoticons-17/24/emoticons-04-32.png";

    public RelevantNewsView(RelevantNews news, int resultId, int index) {
        this.id = resultId;
        this.index = index;
        title = news.article.getTitle();
        String[] images = news.article.getImages();
        if(images.length < 1)
        {
            imageUrl = "https://upload.wikimedia.org/wikipedia/commons/4/47/Comic_image_missing.png"; //"no image" image url
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
        switch (sentiment.getPolarity().toLowerCase()) {
            case "positive":
                sentimentImageUrl = positiveFaceUrl;
                break;
            case "negative":
                sentimentImageUrl = negativeFaceUrl;
                break;
            case "neutral":
            default:
                sentimentImageUrl = neutralFaceUrl;
                break;
        }

        url = news.url.toString();
        domain = getDomainName(news.url);

        classifications = new ArrayList<>();
        TaxonomyCategory[] categories = news.classifications.getCategories();
        if(categories != null) {
            for (TaxonomyCategory c : categories) {
                classifications.add(c.getLabel());
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

    public List<String> getClassifications() {
        return classifications;
    }

    public void setClassifications(List<String> classifications) {
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
