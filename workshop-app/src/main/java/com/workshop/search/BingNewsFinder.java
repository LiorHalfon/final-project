package com.workshop.search;

import com.aylien.textapi.TextAPIException;
import com.aylien.textapi.responses.*;
import com.workshop.bing.configuration.BingConfiguration;
import com.workshop.bing.controllers.BingController;
import com.workshop.bing.model.search.results.BingWebResponseData;
import com.workshop.bing.model.search.results.result.BingNewsResponse;
import com.workshop.bing.model.search.results.result.BingNewsResult;
import com.workshop.model.User;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class BingNewsFinder implements NewsFinder {

    private List<String> _queries;
    private User _user;
    private TextAnalyser _textAnalyser;
    private BingController _bingController;
    private ResultsFilter _resultFilter;
    private ArrayList<RelevantNews> _relevantNewsList;
    private Boolean isSearchFinished = false;

    @Override
    public void init(List<String> haveToAppearCategories,
                     List<String> cantAppearCategories,
                     List<String> blacklistDomains,
                     List<String> queries,
                     User user) {
        _queries=queries;
        _user = user;

        _textAnalyser = new TextAnalyser();
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(BingConfiguration.class);
        _bingController = ctx.getBean(BingController.class);
        _resultFilter = new ResultsFilter(haveToAppearCategories,cantAppearCategories,blacklistDomains);
        _relevantNewsList = new ArrayList<>();
    }

    @Override
        public void start() throws MalformedURLException{
        while (!_queries.isEmpty())
        {
            String query = _queries.remove(0);
            BingNewsResponse searchResults = _bingController.searchNews(query);

            if (searchResults == null){
                System.err.println("Got empty search results for query: " + query);
                continue;
            }

            BingWebResponseData<BingNewsResult> data = searchResults.getData();
            List<BingNewsResult> resultsList = data.getResults();

            for (BingNewsResult resultItem : resultsList) {
                URL url = new URL(resultItem.getUrl());
                try {
                    TaxonomyClassifications taxonomyClassifications = _textAnalyser.ClassifyUrlByTaxonomy(url);
                    if(_resultFilter.isUrlRelevant(url,taxonomyClassifications)){
                        AddResultToRelevantNews(url, taxonomyClassifications);
                    }
                } catch (TextAPIException e) {
                    e.printStackTrace();
                    _textAnalyser.ConnectToServerWithNewCredentials();
                }
            }
        }
        isSearchFinished = true;
    }

    public List<RelevantNews> getResults() {return _relevantNewsList;}

    public Boolean isSearchFinished() {return isSearchFinished;}

    public String printResults(){
        String output = "";
        for (RelevantNews news: _relevantNewsList) {
            output += "\n";
            output += "URL:\n";
            output += news.url + "\n";
            output += "\n";

            output += "Classifications:\n";
            for (TaxonomyCategory c: news.classifications.getCategories()) {
                output += c.getLabel() + " " + c.getId() + "\n";
            }
            output += "\n";

            output += "Summary:\n";
            String[] summSentences = news.summary.getSentences();
            for (String s: summSentences) {
                output += s + "\n";
            }
            output += "\n";

            output += "Sentiment:\n";
            output += news.sentiment.getPolarity() + ", Confidence: " + news.sentiment.getPolarityConfidence()+ "\n";
            output += "\n";
            output += "---------------------------------------------------------\n";
        }

        if(output.isEmpty())
            output = "No relevant results found";

        return output;
    }

    private void AddResultToRelevantNews(URL url, TaxonomyClassifications taxonomyClassifications) throws TextAPIException {
        Article article = _textAnalyser.ExtractArticle(url);
        Summarize summary = _textAnalyser.Summarize(url);
        Sentiment sentiment = _textAnalyser.GetSentiment(summary.getText());
        _relevantNewsList.add(new RelevantNews(summary, sentiment, url, taxonomyClassifications, article));
    }
}
