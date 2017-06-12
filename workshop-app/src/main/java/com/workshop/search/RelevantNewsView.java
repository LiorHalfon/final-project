package com.workshop.search;

import com.aylien.textapi.responses.Sentiment;
import com.aylien.textapi.responses.TaxonomyCategory;

import java.net.URL;

public class RelevantNewsView {
    public String title;
    public String summary;
    public Sentiment sentiment;
    public String url;
    public String domain;
    public String classifications;

    public RelevantNewsView(RelevantNews news) {
        title = news.article.getTitle();

        summary = "";
        String[] sentences = news.summary.getSentences();
        if(sentences != null) {
            for (String s : sentences) {
                summary += s + " \n\n";
            }
        }

        sentiment = news.sentiment;

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
}
