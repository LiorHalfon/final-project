package com.workshop.service;

import com.workshop.model.User;

public interface UserService {
	public User findUserByEmail(String email);
	public void saveUser(User user);
}
