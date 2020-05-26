package com.cslg.controller;

import com.cslg.vo.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * <h3>shiro-test</h3>
 * <p></p>
 *
 * @author:MIKU
 * @date : 2020-05-23 17:08
 **/
@Controller
public class UserController {

    @PostMapping(value = "/subLogin")
    @ResponseBody
    public String subLogin(User user) {
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(user.getUsername(), user.getPassword());
        try {
//            usernamePasswordToken.setRememberMe(true);
            subject.login(usernamePasswordToken);
        } catch (AuthenticationException e) {
            return e.getMessage();
        }
        if (subject.isAuthenticated()){
            return "成功";
        }
        return "失败";
    }


    //需要admin角色才能访问这个方法
    @RequiresRoles("admin")
    @GetMapping("/testRole")
    @ResponseBody
    public String testRole(){

        return "success";
    }

    //需要admin1或者user角色才能访问这个方法
    @RequiresRoles(value = {"user","admin1"},logical = Logical.OR)
    @GetMapping("/testRole1")
    @ResponseBody
    public String testRole1(){
        return "testrole1 success";
    }

    @GetMapping(value = "/testPerms")
    @ResponseBody
    public String testPerms(){
        return "testPerms success";
    }

    @GetMapping(value = "/testPerms1")
    @ResponseBody
    public String testPerms1(){
        return "testPerms1 success";
    }
}
