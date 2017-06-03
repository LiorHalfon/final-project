package db.impl;

import db.api.UserFeedbackManager;
import db.dao.UserFeedbackDao;
import model.User;
import model.UserFeedback;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Created by ndayan on 03/06/2017.
 */
public class UserFeedbackManagerImpl implements UserFeedbackManager {

    UserFeedbackDao userFeedbackDao;

    public UserFeedbackManagerImpl(JdbcTemplate jdbcTemplate) {
        userFeedbackDao = new UserFeedbackDaoImpl(jdbcTemplate);
    }

    @Override
    public void sendUserFeedback(User user, UserFeedback.ActivityType activityType) {
        userFeedbackDao.sendFeedback(user, activityType);
    }
}
