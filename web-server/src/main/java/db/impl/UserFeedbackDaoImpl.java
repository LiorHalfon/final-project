package db.impl;

import db.dao.UserFeedbackDao;
import model.User;
import model.UserFeedback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * Created by ndayan on 03/06/2017.
 */
public class UserFeedbackDaoImpl extends JdbcDaoSupport implements UserFeedbackDao {

//    @Autowired
//    private DataSource dataSource;
//
//    @PostConstruct
//    private void initialize() {
//        setDataSource(dataSource);
//    }

    public UserFeedbackDaoImpl(JdbcTemplate jdbcTemplate) {
        setJdbcTemplate(jdbcTemplate);
    }

    @Override
    public void sendFeedback(User user, UserFeedback.ActivityType activityType) {
        UserFeedback userFeedback = new UserFeedback();
        userFeedback.setUserId(user.getId());
        userFeedback.setActivityType(activityType);
        Timestamp timestamp = new Timestamp((new Date()).getTime());
        userFeedback.setTimestamp(timestamp);

        getJdbcTemplate().update(
                "INSERT INTO UserFeedback (userId, timestamp, activityType) VALUES (?,?,?)",
                userFeedback.getUserId(),
                userFeedback.getTimestamp(),
                userFeedback.getActivityType().name()
        );
    }

    @Override
    public List<UserFeedback> getAllUserFeedbacks(User user) {
        //TODO: implement
        return null;
    }

    @Override
    public List<UserFeedback> getUserFeedbacks(User user, UserFeedback.ActivityType activityType) {
        //TODO: implement
        return null;
    }

    private static final class UserFeedbackMapper implements RowMapper<UserFeedback> {

        @Override
        public UserFeedback mapRow(ResultSet resultSet, int rowNum) throws SQLException {
            UserFeedback userFeedback = new UserFeedback();
            userFeedback.setActivityType(UserFeedback.ActivityType.valueOf(resultSet.getString("activityType")));
            userFeedback.setTimestamp(resultSet.getTimestamp("timestamp"));
            userFeedback.setUserId(resultSet.getString("userId"));

            return userFeedback;
        }
    }
}
