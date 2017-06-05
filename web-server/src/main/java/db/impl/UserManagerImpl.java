package db.impl;

import db.api.UserManager;
import db.dao.UserDao;
import model.User;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

/**
 * Created by ndayan on 02/06/2017.
 */
public class UserManagerImpl implements UserManager {

    private UserDao userDao;

    public UserManagerImpl(JdbcTemplate jdbcTemplate) {
        userDao = new UserDaoImpl(jdbcTemplate);
    }

    @Override
    public User createUser(User user) {
        return userDao.createUser(user);
    }

    @Override
    public User findUserById(String id) {
        return userDao.findUserById(id);
    }

    @Override
    public User findUserByEmail(String email) {
        return userDao.findUserByEmail(email);
    }

    @Override
    public List<User> findUserByFirstName(String fname) {
        return userDao.findUserByFirstName(fname);
    }

    @Override
    public List<User> findUserByLastName(String lname) {
        return userDao.findUserByLastName(lname);
    }
}
