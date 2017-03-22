import com.aylien.textapi.TextAPIClient;
import com.aylien.textapi.parameters.*;
import com.aylien.textapi.responses.*;
import java.net.URL;

public class App {

    public static void main(String[] args) throws Exception {

        TextAPIClient client = new TextAPIClient("b5c4e688", "5d5fb206e2803c3a7e117f596c0fbe79");
        URL url = new URL("http://www.bbc.com/news/science-environment-30097648");

        ConceptsParams.Builder builder = ConceptsParams.newBuilder();
        builder.setUrl(url);
        Concepts concepts = client.concepts(builder.build());
        System.out.println(concepts.getText());
        for (Concept c: concepts.getConcepts()) {
            System.out.print(c.getUri() + ": ");
            for (SurfaceForm sf: c.getSurfaceForms()) {
                System.out.print(sf.getString() + " ");
            }
            System.out.println();
        }

        LanguageParams languageParams = new LanguageParams(null, url);
        Language language = client.language(languageParams);
        System.out.printf("\nLanguage is: %s (%f)\n", language.getLanguage(), language.getConfidence());
    }

}
