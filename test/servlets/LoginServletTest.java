package servlets;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import conn_interface.ServletsConn;
import entity.User;

public class LoginServletTest {

    @org.junit.Test
    public void doPost() {
        User user0 = new User();
        //user0.setNickname("ZhangSan");
        user0.setPassword("123456");
        user0.setUserId("z002");
        User user1 = new User();
        //user1.setNickname("hu1");
        user1.setPassword("123456");
        user1.setUserId("2");
        handleLogin(login(user1));
    }

    public static String login(User user){
        Gson gson=new Gson();
        String json1=gson.toJson(user);
        return ServletsConn.connServlets("login",json1);
    }

    public static void handleLogin(String json) {
        int flag=0;//用于标识的信号量
        if(json != null){
            Gson gson=new Gson();
            JsonObject jsonObject=gson.fromJson(json,JsonObject.class);
            flag=Integer.parseInt(jsonObject.get("status").toString());
            if(flag==0){
                //登录成功
                System.out.println("登录成功！！！");
            }else if (flag==1){
                //登录失败
                System.out.println("登录失败！！！");
            }
            else if(flag == 2){
                System.out.println("用户名或密码错误！！！");
            }
        }
        else {
            System.out.println("登录失败！！！");
        }
    }
}