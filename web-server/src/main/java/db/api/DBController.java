package db.api;

/**
 * Created by ndayan on 03/06/2017.
 */
public interface DBController {

    UserManager getUserManager();
    UserFeedbackManager getUserFeedbackManager();
    void shutdown();
}
