package com.cslg.filter;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authz.AuthorizationFilter;


import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * <h3>shiro-test</h3>
 * <p></p>
 *
 * @author:MIKU
 * @date : 2020-05-25 18:40
 **/
public class RolesOrFliter extends AuthorizationFilter {
    @Override
    protected boolean isAccessAllowed(ServletRequest servletRequest, ServletResponse servletResponse, Object o) throws Exception {
        Subject subject = getSubject(servletRequest,servletResponse);
        String[] roles = (String[])o;
        if (roles.length==0||roles==null){
            return true;
        }
        for (String role:roles){
            if(subject.hasRole(role)){
                return true;
            }
        }
        return false;
    }
}
