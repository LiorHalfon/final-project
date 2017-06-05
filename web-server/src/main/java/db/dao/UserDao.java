package db.dao;

import model.User;

import java.util.List;

/**
 * Created by ndayan on 02/06/2017.
 */

public interface UserDao {

    User createUser(User user);
    User findUserById(String id);
    User findUserByEmail(String email);
    List<User> findUserByFirstName(String fname);
    List<User> findUserByLastName(String lname);

}
