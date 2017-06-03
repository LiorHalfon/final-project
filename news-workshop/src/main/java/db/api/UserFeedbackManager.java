package db.api;

import model.User;
import model.UserFeedback;

/**
 * Created by ndayan on 03/06/2017.
 */
public interface UserFeedbackManager {
    void sendUserFeedback(User user, UserFeedback.ActivityType activityType);
}
