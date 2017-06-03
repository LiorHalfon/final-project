package db.dao;

import model.User;
import model.UserFeedback;

import java.util.List;

/**
 * Created by ndayan on 02/06/2017.
 */

public interface UserFeedbackDao {

    void sendFeedback(User user, UserFeedback.ActivityType activityType);
    List<UserFeedback> getAllUserFeedbacks(User user);
    List<UserFeedback> getUserFeedbacks(User user, UserFeedback.ActivityType activityType);
}
