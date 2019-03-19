package servlets;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import entity.User;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginServletTest {

    @org.junit.Test
    public void doPost() {
        User user0 = new User();
        user0.setUsername("ZhangSan");
        user0.setPassword("123456");
        user0.setUserid("z001");
        User user1 = new User();
        user1.setUsername("hu1");
        user1.setPassword("123456");
        user1.setUserid("2");
        handleLogin(login(user1));
    }

    public static String login(User user){
        HttpURLConnection conn = null;
        String url=null;
        String json=null;
        try {
            conn = (HttpURLConnection) new URL("http://192.168.1.102:8080/testServer_war_exploded/login").openConnection();
            conn.setConnectTimeout(50000);
            conn.setReadTimeout(30000);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            OutputStream out = conn.getOutputStream();
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(out));
            Gson gson=new Gson();
            String json1=gson.toJson(user);
            System.out.println(json1);
            bw.write(json1);
            bw.flush();
            out.close();
            bw.close();

            InputStream in = conn.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String str = null;
            StringBuffer buffer = new StringBuffer();
            while((str = br.readLine())!=null){
                buffer.append(str);
            }
            in.close();
            br.close();
            json=buffer.toString();


        }catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 意外退出时进行连接关闭保护
            if (conn != null) {
                conn.disconnect();
            }
        }
        System.out.println(json);
        return json;
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
                System.out.println("连接数据库失败！！！");
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