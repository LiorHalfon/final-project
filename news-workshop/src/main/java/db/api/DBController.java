package db.api;

import db.dao.UserDao;
import db.dao.UserFeedbackDao;
import model.UserFeedback;

/**
 * Created by ndayan on 03/06/2017.
 */
public interface DBController {

    UserManager getUserManager();
    UserFeedbackManager getUserFeedbackManager();
    void shutdown();
}
