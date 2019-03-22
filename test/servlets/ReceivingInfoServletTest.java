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
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;


/**
 *添加或者删除收货信息
 *
 */
public class ReceivingInfoServletTest {

    private static int sign;// 0/添加 1/删除;
    @Test
    public void doPost() {
        ReceiveInfo receiveInfo = new ReceiveInfo();
        receiveInfo.setUserId("2");
        receiveInfo.setPhone("654321");
        receiveInfo.setAddress("四川攀枝花");
        sign = 1;
        handleResult(doReceiveInfo(receiveInfo));
    }
    private void handleResult(String json) {
        int result = 0;
        if (json == null) {
            System.out.println("操作失败401");
            return;
        }
            Gson gson = new Gson();
            JsonObject jsonObject = gson.fromJson(json, JsonObject.class);
            result = Integer.parseInt(jsonObject.get("result").toString());
        switch (result){
            case 1:
                System.out.println("操作成功");
                break;
            case 0:
                System.out.println("操作失败402");
                break;
        }
    }
    private String doReceiveInfo(ReceiveInfo receiveInfo) {
        Gson gson = new Gson();
        if(sign!=0&&sign!=1)
            return null;
        String json = "{'sign':"+ sign +",'ReceiveInfo':"+gson.toJson(receiveInfo)+"}";
        return ServletsConn.connServlets("ReceiveInfo",json);
    }
}