import bing.controllers.BingController;
import bing.model.search.results.BingWebResponseData;
import bing.model.search.results.result.BingNewsResponse;
import bing.model.search.results.result.BingNewsResult;
import com.aylien.textapi.responses.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.net.URL;
import java.util.List;

import static java.lang.System.out;

public class App {

    private static final String APPLE = "Apple";

    public static void main(String[] args) throws Exception {
        ApplicationContext ctx = new AnnotationConfigApplicationContext(bing.configuration.BingConfiguration.class);
        TextAnalyser analyser = new TextAnalyser();
        BingController bingController = ctx.getBean(BingController.class);

        BingNewsResponse results = bingController.searchNews(APPLE);
        if (results != null) {
            out.println("Results for: " + APPLE);
            BingWebResponseData<BingNewsResult> data = results.getData();
            List<BingNewsResult> resultsList = data.getResults();
            for (BingNewsResult resultItem : resultsList) {
                URL url = new URL(resultItem.getUrl());
                Classifications classifications = analyser.ClassifyUrl(url);
                out.println("URL text:");
                String text = classifications.getText();
                out.println(text);
                out.println();

                out.println("URL Categories:");
                for (Category category: classifications.getCategories()) {
                    System.out.println(category);
                }
                out.println();

                TaxonomyClassifications taxonomyClassifications = analyser.ClassifyUrlByTaxonomy(url);

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
