package com.cslg.dao.impl;

import com.cslg.dao.UserDao;
import com.cslg.vo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * <h3>shiro-test</h3>
 * <p></p>
 *
 * @author:MIKU
 * @date : 2020-05-24 18:03
 **/
@Repository("userDao")
public class UserDaoImpl implements UserDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public User getUser(String username) {
        String sql = "select username,password from user where username = ?";
        User user = jdbcTemplate.queryForObject(sql, new UserRowMapper(), username);
        return user;
    }

    private class UserRowMapper implements RowMapper<User>{
        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            User user = new User();
            user.setUsername(rs.getString("username"));
            user.setPassword(rs.getString("password"));
            return user;
        }
    }

}
