package servlets;

import com.google.gson.Gson;
import conn_interface.ServletsConn;
import entity.User;
import org.junit.Test;

public class FriendsServletTest {

    @Test
    public void doPost() {
        User user0 = new User();
        user0.setNickname("ZhangSan");
        user0.setPassword("123456");
        user0.setUserId("z002");
        User user1 = new User();
        //user1.setNickname("hu1");
        user1.setUserId("1");
        System.out.println(getFriends(user1));
    }

    private String getFriends(User user) {
        Gson gson = new Gson();
        String json = "{'sign':2,'User':"+gson.toJson(user)+"}";
        return ServletsConn.connServlets("friends",json);
    }
}