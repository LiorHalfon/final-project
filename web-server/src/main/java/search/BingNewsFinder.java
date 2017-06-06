package search;

import bing.controllers.BingController;
import bing.model.search.results.BingWebResponseData;
import bing.model.search.results.result.BingNewsResponse;
import bing.model.search.results.result.BingNewsResult;
import com.aylien.textapi.TextAPIException;
import com.aylien.textapi.responses.Sentiment;
import com.aylien.textapi.responses.Summarize;
import com.aylien.textapi.responses.TaxonomyCategory;
import com.aylien.textapi.responses.TaxonomyClassifications;
import db.configuration.DBConfiguration;
import model.User;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class BingNewsFinder implements NewsFinder {

    private ArrayList<String> _queries;
    private User _user;
    private TextAnalyser _textAnalyser;
    private BingController _bingController;
    private ResultsFilter _resultFilter;
    private ArrayList<RelevantNews> _relevantNewsList;
    private Boolean isSearchFinished = false;

    @Override
    public void Init(ArrayList<String> haveToAppearCategories,
                     ArrayList<String> cantAppearCategories,
                     ArrayList<String> blacklistDomains,
                     ArrayList<String> queries,
                     User user) {
        _queries=queries;
        _user = user;

        _textAnalyser = new TextAnalyser();
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(bing.configuration.BingConfiguration.class, DBConfiguration.class);
        _bingController = ctx.getBean(BingController.class);
        _resultFilter = new ResultsFilter(haveToAppearCategories,cantAppearCategories,blacklistDomains);
        _relevantNewsList = new ArrayList<>();
    }

    @Override
    public void Start() throws Exception {
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
                TaxonomyClassifications taxonomyClassifications = _textAnalyser.ClassifyUrlByTaxonomy(url);
                if(_resultFilter.isUrlRelevant(url,taxonomyClassifications)){
                    AddResultToRelevantNews(url, taxonomyClassifications);
                }

                //Wait, so we won't exceed hit rate limit
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                }
            }
        }
        isSearchFinished = true;
    }

    public List<RelevantNews> GetResults() {return _relevantNewsList;}

    public Boolean IsSearchFinished() {return isSearchFinished;}

    public String PrintResults(){
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

        return output;
    }

    private void AddResultToRelevantNews(URL url, TaxonomyClassifications taxonomyClassifications) throws TextAPIException {
        Summarize summary = _textAnalyser.Summarize(url);
        Sentiment sentiment = _textAnalyser.GetSentiment(summary.getText());
        _relevantNewsList.add(new RelevantNews(summary, sentiment, url, taxonomyClassifications));
    }
}
