import bing.controllers.BingController;
import bing.model.search.results.BingWebResponseData;
import bing.model.search.results.result.BingNewsResponse;
import bing.model.search.results.result.BingNewsResult;
import com.aylien.textapi.responses.*;
import db.api.DBController;
import db.api.UserFeedbackManager;
import db.api.UserManager;
import db.configuration.DBConfiguration;
import model.User;
import model.UserFeedback;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import static java.lang.System.out;
@SpringBootApplication
public class App {

    private static final String APPLE = "Apple";

    public static void main(String[] args) throws Exception {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(bing.configuration.BingConfiguration.class, DBConfiguration.class);
        DBController dbController = ctx.getBean(DBController.class);
        UserManager userManager = dbController.getUserManager();
        User user = new User();
        user.setEmail("user@email.com");
        user.setFirstName("user-fname");
        user.setLastName("user-lname");
        user = userManager.createUser(user);

        UserFeedbackManager userFeedbackManager = dbController.getUserFeedbackManager();
        userFeedbackManager.sendUserFeedback(user, UserFeedback.ActivityType.BLOCK_DOMAIN);
        dbController.shutdown();

        TextAnalyser analyser = new TextAnalyser();
        BingController bingController = ctx.getBean(BingController.class);

        BingNewsResponse results = bingController.searchNews(APPLE);

        ArrayList<String> haveToAppear = new ArrayList<>();
        ArrayList<String> cantAppear = new ArrayList<>();
        haveToAppear.add("IAB19");
        cantAppear.add("IAB1-6");

        ResultsFilter resultsFilter = new ResultsFilter(haveToAppear, cantAppear);
        if (results != null) {
            System.out.println("Results for: " + APPLE);
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

                System.out.println("URL Taxonomy Categories:");
                for (TaxonomyCategory c: taxonomyClassifications.getCategories()) {
                    System.out.println(c);
                }
                System.out.println();

                Sentiment sentiment = analyser.GetSentiment(taxonomyClassifications.getText());
                System.out.println("URL Sentiment:");
                System.out.println(sentiment);
            }
        }

    }
}
