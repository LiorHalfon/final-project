package com.workshop.service;

import com.workshop.model.User;
import com.workshop.model.UserFeedback;

import java.util.List;

public interface UserFeedbackService {

	void sendFeedback(User user, UserFeedback.ActivityType activityType);
	List<UserFeedback> getAllUserFeedbacks(User user);
	List<UserFeedback> getUserFeedbacks(User user, UserFeedback.ActivityType activityType);

}
