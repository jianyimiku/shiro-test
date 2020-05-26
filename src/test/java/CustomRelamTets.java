import com.cslg.shiro.relam.CustomRealm;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.subject.Subject;

/**
 * <h3>shiro-test</h3>
 * <p></p>
 *
 * @author:MIKU
 * @date : 2020-05-23 15:48
 **/
public class CustomRelamTets {
    public static void main(String[] args) {
        DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();
        CustomRealm customRealm = new CustomRealm();
        SecurityUtils.setSecurityManager(defaultSecurityManager);
        defaultSecurityManager.setRealm(customRealm);

        //加密
        HashedCredentialsMatcher matcher = new HashedCredentialsMatcher();
        //设置MD5加密的名称
        matcher.setHashAlgorithmName("md5");
        //设置加密的次数
        matcher.setHashIterations(1);
        //自定义relam设置加密对象
        customRealm.setCredentialsMatcher(matcher);

        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken("jianyi","1234567");
        subject.login(usernamePasswordToken);
        System.out.println(subject.isAuthenticated());
    }
}
