package db.impl;

import db.dao.UserDao;
import model.User;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by ndayan on 02/06/2017.
 */
public class UserDaoImpl extends JdbcDaoSupport implements UserDao {

//    @Autowired
//    private DataSource dataSource;

//    @PostConstruct
//    private void initialize() {
//        setDataSource(dataSource);
//    }

    public UserDaoImpl(JdbcTemplate jdbcTemplate) {
//        setDataSource(dataSource);
        setJdbcTemplate(jdbcTemplate);
    }

    @Override
    public User createUser(User user) {

        int result = getJdbcTemplate().update("INSERT INTO Users (lname, fname, email) VALUES (?,?,?)",
                user.getLastName(), user.getFirstName(), user.getEmail());


        return (result == 0 ? null : findUserByEmail(user.getEmail()));
    }

    @Override
    public User findUserById(String id) {
        String sql = "SELECT * FROM Users WHERE id = ?";
        User result = null;
        try{
            result = getJdbcTemplate().queryForObject(sql, new Object[]{id}, new UserMapper());
        } catch (DataAccessException e) {
            System.out.println(e.getMessage());
        }

        return result;
    }

    @Override
    public User findUserByEmail(String email) {
        String sql = "SELECT * FROM Users WHERE email = ?";
        User result = null;
        try{
            result = getJdbcTemplate().queryForObject(sql, new Object[]{email}, new UserMapper());
        } catch (DataAccessException e) {
            System.out.println(e.getMessage());
        }

        return result;
    }

    @Override
    public List<User> findUserByFirstName(String fname) {
        String sql = "SELECT * FROM Users WHERE fname:= ?";
        List<User> result = null;

        try {
            result = getJdbcTemplate().query(sql, new Object[]{fname}, new UserMapper());
        } catch (DataAccessException e) {

        }

        return result;
    }

    @Override
    public List<User> findUserByLastName(String lname) {
        return null;
    }

    private static final class UserMapper implements RowMapper<User> {

        public User mapRow(ResultSet rs, int rowNum) throws SQLException {

            User user = new User();
            user.setId(rs.getString("id"));
            user.setFirstName(rs.getString("fname"));
            user.setLastName(rs.getString("lname"));
            user.setEmail(rs.getString("email"));

            return user;
        }
    }
}
