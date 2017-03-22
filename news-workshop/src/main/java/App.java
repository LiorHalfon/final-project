import com.aylien.textapi.responses.Category;
import com.aylien.textapi.responses.Classifications;
import com.aylien.textapi.responses.TaxonomyCategory;
import com.aylien.textapi.responses.TaxonomyClassifications;
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

        out.println("URL Categories:");
        for (Category category: classifications.getCategories()) {
            System.out.println(category);
        }

        TaxonomyClassifications response = analyser.ClassifyUrlByTaxonomy(testUrl);

        out.println("URL Taxonomy:");
        out.println(response.getTaxonomy());

        out.println("URL Taxonomy Categories:");
        for (TaxonomyCategory c: response.getCategories()) {
            out.println(c);
        }
    }
}
