package com.leyou;

import com.leyou.client.UserQueryClient;
import com.leyou.user.pojo.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SearchApp.class)
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