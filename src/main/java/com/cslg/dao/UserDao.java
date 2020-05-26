package com.cslg.dao;

import com.cslg.vo.User;
import org.springframework.stereotype.Repository;

/**
 * <h3>shiro-test</h3>
 * <p></p>
 *
 * @author:MIKU
 * @date : 2020-05-24 17:58
 **/
public interface UserDao {
    User getUser(String username);
}
