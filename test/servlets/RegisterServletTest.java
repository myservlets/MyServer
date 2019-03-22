package servlets;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import conn_interface.ServletsConn;
import entity.User;
import org.junit.Test;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class RegisterServletTest {

    @Test
    public void doPost() {
        User user0 = new User();
        user0.setNickname("ZhangSan");
        user0.setPassword("123456");
        user0.setUserid("z001");
        User user1 = new User();
        user1.setNickname("hu2");
        user1.setPassword("123456");
        user1.setUserid("2");
        handleRegist(regist(user1));
    }

    private void handleRegist(String json) {
        int result = 2;
        if(json != null) {
            Gson gson = new Gson();
            JsonObject jsonObject = gson.fromJson(json, JsonObject.class);
            result = Integer.parseInt(jsonObject.get("result").toString());
            result = Integer.parseInt(jsonObject.get("status").toString());
        }

        switch (result){
            case 2:
                System.out.println("注册失败");
                break;
            case 1:
                System.out.println("用户名已存在");
                break;
            case 0:
                System.out.println("注册成功");
                break;

        }
    }

    private String regist(User user) {
        Gson gson=new Gson();
        String json1=gson.toJson(user);
        return ServletsConn.connServlets("register",json1);
    }
}