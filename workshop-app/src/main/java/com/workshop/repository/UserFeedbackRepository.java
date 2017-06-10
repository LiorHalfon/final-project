package com.workshop.repository;

import com.workshop.model.User;
import com.workshop.model.UserFeedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("userFeedbackRepository")
public interface UserFeedbackRepository extends JpaRepository<UserFeedback, Long> {
	List<UserFeedback> findByUser(User user);
}
