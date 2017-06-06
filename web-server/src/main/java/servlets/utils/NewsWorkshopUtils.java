package servlets.utils;

import bing.configuration.BingConfiguration;
import db.api.DBController;
import db.api.UserFeedbackManager;
import db.api.UserManager;
import db.configuration.DBConfiguration;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import search.BingNewsFinder;
import search.NewsFinder;

public class NewsWorkshopUtils {
    public UserManager userManager;
    public UserFeedbackManager userFeedbackManager;
    public NewsFinder newsFinder;

    public NewsWorkshopUtils() {
        Class<BingConfiguration> c = BingConfiguration.class;
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(c, DBConfiguration.class);
        DBController dbController = ctx.getBean(DBController.class);
        userManager = dbController.getUserManager();
        userFeedbackManager = dbController.getUserFeedbackManager();
        newsFinder = new BingNewsFinder();
    }
}
