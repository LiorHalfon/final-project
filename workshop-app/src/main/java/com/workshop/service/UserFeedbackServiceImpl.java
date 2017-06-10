package com.workshop.service;

import com.workshop.model.User;
import com.workshop.model.UserFeedback;
import com.workshop.repository.UserFeedbackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * Created by ndayan on 10/06/2017.
 */
@Service("userFeedbackService")
public class UserFeedbackServiceImpl implements UserFeedbackService {

    @Autowired
    UserFeedbackRepository userFeedbackRepository;

    @Override
    public void sendFeedback(User user, UserFeedback.ActivityType activityType) {
        UserFeedback uf = new UserFeedback();
        uf.setUser(user);
        uf.setTimestamp(new Timestamp(new Date().getTime()));
        uf.setActivityType(activityType);
        userFeedbackRepository.save(uf);
    }

    @Override
    public List<UserFeedback> getAllUserFeedbacks(User user) {
        return userFeedbackRepository.findByUser(user);
    }

    @Override
    public List<UserFeedback> getUserFeedbacks(User user, UserFeedback.ActivityType activityType) {
        return null;
    }
}
