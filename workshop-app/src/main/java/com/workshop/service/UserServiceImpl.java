package com.workshop.service;

import java.util.Arrays;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.workshop.model.Role;
import com.workshop.model.User;
import com.workshop.repository.RoleRepository;
import com.workshop.repository.UserRepository;

@Service("userService")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public void saveUser(User user, boolean isAdmin) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setActive(1);
        Role userRole = roleRepository.findByRole(isAdmin ? "ADMIN" : "USER");
        user.setRoles(new HashSet<Role>(Arrays.asList(userRole)));
        userRepository.save(user);
    }

    @Override
    public boolean isAdminByEMail(String email) {
        User user = userRepository.findByEmail(email);
        return user.getRoles().stream()
                .map(role -> role.getRole())
                .filter(r -> r.equalsIgnoreCase("ADMIN"))
                .count() > 0;
    }

}
