package test;


import com.leyou.auth.AuthApp;
import com.leyou.auth.client.UserQueryClient;
import com.leyou.user.pojo.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author zhoumo
 * @datetime 2018/8/3 18:14
 * @desc
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = AuthApp.class)
public class TestClient {

    @Autowired
    private UserQueryClient userQueryClient;

    @Test
    public void test1() {
        System.out.println("userClient = " + userQueryClient);
        User user = userQueryClient.query("景甜", "123");
        System.out.println("user = " + user);
    }
}