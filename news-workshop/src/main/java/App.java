import bing.controllers.BingController;
import bing.model.search.results.BingWebResponseData;
import bing.model.search.results.result.BingNewsResponse;
import bing.model.search.results.result.BingNewsResult;
import com.aylien.textapi.responses.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static java.lang.System.out;

public class App {

    private static final String APPLE = "Apple";

    public static void main(String[] args) throws Exception {
        ApplicationContext ctx = new AnnotationConfigApplicationContext(bing.configuration.BingConfiguration.class);
        TextAnalyser analyser = new TextAnalyser();
        BingController bingController = ctx.getBean(BingController.class);

        BingNewsResponse results = bingController.searchNews(APPLE);

        ArrayList<String> haveToAppear = new ArrayList<>();
        ArrayList<String> cantAppear = new ArrayList<>();
        haveToAppear.add("IAB19");
        cantAppear.add("IAB1-6");

        ResultsFilter resultsFilter = new ResultsFilter(haveToAppear, cantAppear);
        if (results != null) {
            out.println("Results for: " + APPLE);
            BingWebResponseData<BingNewsResult> data = results.getData();
            List<BingNewsResult> resultsList = data.getResults();
            for (BingNewsResult resultItem : resultsList) {
                URL url = new URL(resultItem.getUrl());

                TaxonomyClassifications taxonomyClassifications = analyser.ClassifyUrlByTaxonomy(url);
                if(!resultsFilter.isUrlRelevant(taxonomyClassifications)){
                    out.println("Got an irrelevant url");
                    continue;
                }

                out.println("URL text:");
                String text = taxonomyClassifications.getText();
                out.println(text);
                out.println();

                out.println("URL Taxonomy:");
                out.println(taxonomyClassifications.getTaxonomy());
                out.println();

                out.println("URL Taxonomy Categories:");
                for (TaxonomyCategory c: taxonomyClassifications.getCategories()) {
                    out.println(c);
                }
                out.println();

                Sentiment sentiment = analyser.GetSentiment(taxonomyClassifications.getText());
                out.println("URL Sentiment:");
                System.out.println(sentiment);
            }
        }

    }
}
