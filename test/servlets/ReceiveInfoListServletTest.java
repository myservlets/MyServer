package servlets;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import conn_interface.ServletsConn;
import entity.ReceiveInfo;
import entity.User;
import org.junit.Test;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * 查询收货信息列表
 *
 */

public class ReceiveInfoListServletTest {

    static ArrayList<ReceiveInfo> receiveInfos = new ArrayList<ReceiveInfo>();
    @Test
    public void doPost() {
        User user1 = new User();
        user1.setNickname("hu1");
        user1.setPassword("123456");
        user1.setUserid("2");
        handleEditReceiveInfo(editReceiveInfo(user1));
    }

    private String editReceiveInfo(User user) {
        Gson gson = new Gson();
        String json = gson.toJson(user);
        return ServletsConn.connServlets("ReceiveInfoList",json);
    }

    public static void handleEditReceiveInfo(String json) {
        int flag=0;//用于标识的信号量

        if(json != null){
            Gson gson=new Gson();
            JsonObject jsonObject=gson.fromJson(json,JsonObject.class);
            flag=Integer.parseInt(jsonObject.get("status").toString());
            if(flag==0){
                //编辑成功
                System.out.println("查询成功！！！");
            }
            receiveInfos=gson.fromJson(jsonObject.get("ArrayList<ReceiveInfo>").toString(),ArrayList.class);
            System.out.println(receiveInfos);
        }
        else {
            System.out.println("查询失败！！！");
        }
    }
}

