package com.workshop.service;

import com.workshop.model.User;

public interface UserService {
	User findUserByEmail(String email);
	void saveUser(User user, boolean isAdmin);
	boolean isAdminByEMail(String email);
}
