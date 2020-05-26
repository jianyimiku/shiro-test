package com.cslg.shiro.relam;

import com.cslg.dao.UserDao;
import com.cslg.vo.User;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * <h3>shiro-test</h3>
 * <p></p>
 *
 * @author:MIKU
 * @date : 2020-05-22 17:52
 **/
public class CustomRealm extends AuthorizingRealm {
    @Autowired
    private UserDao userDao;
    //授权
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        String userName = (String) principalCollection.getPrimaryPrincipal();
        //获取角色
        Set<String> roles = getRoleByUserName(userName);
        //获取权限
        Set<String> permission = getPermissionByUserName(userName);

        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        simpleAuthorizationInfo.setRoles(roles);
        simpleAuthorizationInfo.setStringPermissions(permission);
        return simpleAuthorizationInfo;
    }

    private Set<String> getPermissionByUserName(String userName) {
        //从数据库或者缓存中 获取权限
        Set<String> set = new HashSet<>();
        set.add("user:delete");
        set.add("user:add");
        return set;
    }

    private Set<String> getRoleByUserName(String userName) {
        //从数据库或者缓存中 获取角色数据
        Set<String> set = new HashSet<>();
        set.add("admin");
        set.add("user");
        return set;
    }

    //认证
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        //从主体传过来的认证信息中 获得用户名
        String userName = (String) authenticationToken.getPrincipal();

        //通过用户名到数据库中获取凭证
        String password = getPasswordByUserName(userName);
        if (password == null){
            return null;
        }
        //认证信息
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(userName,password,"customRealm");
        //若使用加密进行了加盐 所以我们需要返回的时候添加盐进去
        authenticationInfo.setCredentialsSalt(ByteSource.Util.bytes("Mark"));
        return authenticationInfo;
    }

    private String getPasswordByUserName(String userName) {
        //MyBatis访问数据库 这里不写了 用Map做替代
        User user = userDao.getUser(userName);
        if (user!=null){
            return user.getPassword();
        }
        return null;
    }

    public static void main(String[] args) {
        //salt 为加盐操作
        Md5Hash md5Hash = new Md5Hash("1234567","Mark");
        System.out.println(md5Hash.toString());
    }
}
