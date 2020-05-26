import com.alibaba.druid.pool.DruidDataSource;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.jdbc.JdbcRealm;
import org.apache.shiro.realm.text.IniRealm;
import org.apache.shiro.subject.Subject;
import org.junit.Test;

/**
 * <h3>shiro-test</h3>
 * <p></p>
 *
 * @author:MIKU
 * @date : 2020-05-22 17:27
 **/
public class jdbcRelam {

    //设置数据源
    private static DruidDataSource druidDataSource = new DruidDataSource();

    {
        druidDataSource.setUrl("jdbc:mysql://localhost:3306/shiro?serverTimezone=UTC");
        druidDataSource.setUsername("Miku");
        druidDataSource.setPassword("39");
    }

    @Test
    public void test() {
        JdbcRealm jdbcRealm = new JdbcRealm();
        jdbcRealm.setDataSource(druidDataSource);

        //使用自己的sql 不设置就是默认的sql
        String sql = "select password from test_user where username = ? ";
        jdbcRealm.setAuthenticationQuery(sql);


        DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();

        SecurityUtils.setSecurityManager(defaultSecurityManager);
        defaultSecurityManager.setRealm(jdbcRealm);
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken("Mark", "123456");
        subject.login(usernamePasswordToken);
        System.out.println(subject.isAuthenticated());
    }
}
