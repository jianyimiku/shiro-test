import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.AuthenticatingRealm;
import org.apache.shiro.realm.SimpleAccountRealm;
import org.apache.shiro.subject.Subject;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

/**
 * <h3>shiro-test</h3>
 * <p></p>
 *
 * @author:MIKU
 * @date : 2020-05-22 16:12
 **/
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AuthenticationTest {
   SimpleAccountRealm simpleAccountRealm = new SimpleAccountRealm();

   @Test
   public void test(){
       simpleAccountRealm.addAccount("Miku","39");

       DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();

       SecurityUtils.setSecurityManager(defaultSecurityManager);
       defaultSecurityManager.setRealm(simpleAccountRealm);
       Subject subject = SecurityUtils.getSubject();
       UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken("Miku","39");
       subject.login(usernamePasswordToken);
       System.out.println(subject.isAuthenticated());
   }

}
