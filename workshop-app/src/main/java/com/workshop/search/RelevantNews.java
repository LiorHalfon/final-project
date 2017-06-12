package com.workshop.search;

import com.aylien.textapi.responses.Article;
import com.aylien.textapi.responses.Sentiment;
import com.aylien.textapi.responses.Summarize;
import com.aylien.textapi.responses.TaxonomyClassifications;

import java.net.URL;

public class RelevantNews {

    public Summarize summary;
    public Sentiment sentiment;
    public URL url;
    public Article article;
    TaxonomyClassifications classifications;

    public RelevantNews(Summarize summary, Sentiment sentiment, URL url,
                        TaxonomyClassifications taxonomyClassifications, Article article) {
        this.summary = summary;
        this.sentiment = sentiment;
        this.url = url;
        this.article = article;
        this.classifications = taxonomyClassifications;
    }
}
