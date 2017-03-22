import com.aylien.textapi.responses.*;

import java.net.URL;
import static java.lang.System.out;

public class App {

    public static void main(String[] args) throws Exception {
        TextAnalyser analyser = new TextAnalyser();
        URL testUrl = new URL("http://techcrunch.com/2015/07/16/microsoft-will-never-give-up-on-mobile");

        Classifications classifications = analyser.ClassifyUrl(testUrl);
        out.println("URL text:");
        String text = classifications.getText();
        out.println(text);
        out.println();

        out.println("URL Categories:");
        for (Category category: classifications.getCategories()) {
            System.out.println(category);
        }
        out.println();

        TaxonomyClassifications taxonomyClassifications = analyser.ClassifyUrlByTaxonomy(testUrl);

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
