package servlets;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import conn_interface.ServletsConn;
import entity.Goods;
import entity.ReceiveInfo;
import entity.User;
import org.junit.Test;

import java.io.*;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;


/**
 *添加或者删除收货信息
 *
 */
public class ReceivingInfoServletTest {

    private static int sign;// 0/添加 1/删除 2/编辑收货信息 3/查询收货信息列表 ;
    @Test
    public void doPost() {
        ReceiveInfo receiveInfo = new ReceiveInfo();
        receiveInfo.setUserId("2");
        receiveInfo.setPhone("654321");
        receiveInfo.setAddress("四川攀枝花");
        receiveInfo.setrId(2);
        User user = new User();
        user.setUserId("2");
        handleResult(queryReceiveInfoList(user));
    }
    private void handleResult(String json) {
        int result = 0;
        if (json == null) {
            System.out.println("操作失败401");
            return;
        }
            Gson gson = new Gson();
            JsonObject jsonObject = gson.fromJson(json, JsonObject.class);
            result = Integer.parseInt(jsonObject.get("status").toString());
        switch (result){
            case 0:
                System.out.println("添加成功");
                break;
            case 1:
                System.out.println("添加失败");
                break;
            case 2:
                System.out.println("删除成功");
                break;
            case 3:
                System.out.println("删除失败");
                break;
            case 4:
                System.out.println("编辑成功");
                break;
            case 5:
                System.out.println("编辑失败");
                break;
            case 6:
                System.out.println("查看收货信息列表");
                Type listType = new TypeToken<ArrayList<Goods>>() {}.getType();
                ArrayList<ReceiveInfo> receiveInfos = gson.fromJson(jsonObject.get("ArrayList<ReceiveInfo>").toString(),listType);
                System.out.println(receiveInfos);
                break;
            case 7:
                System.out.println("获取用户默认收货地址");
                ReceiveInfo receiveInfo = gson.fromJson(jsonObject.get("Receiveinfo").toString(),ReceiveInfo.class);
                System.out.println(receiveInfo);
                break;
            case 8:
                System.out.println("设置用户默认收货地址成功");
                break;
            case 9:
                System.out.println("设置用户默认收货地址失败");
                break;
        }
    }
    private String deleteReceiveInfo(ReceiveInfo receiveInfo) {
        sign=1;
        Gson gson = new Gson();
        String json = "{'sign':"+ sign +",'ReceiveInfo':"+gson.toJson(receiveInfo)+"}";
        return ServletsConn.connServlets("ReceiveInfo",json);
    }
    private String addReceiveInfo(ReceiveInfo receiveInfo) {
        sign=0;
        Gson gson = new Gson();
        String json = "{'sign':"+ sign +",'ReceiveInfo':"+gson.toJson(receiveInfo)+"}";
        return ServletsConn.connServlets("ReceiveInfo",json);
    }
    private String queryReceiveInfoList(User user) {
        sign=3;
        Gson gson = new Gson();
        String json = "{'sign':"+ sign +",'User':"+gson.toJson(user)+"}";
        return ServletsConn.connServlets("ReceiveInfo",json);
    }
    private String editReceiveInfo(ReceiveInfo receiveInfo1,ReceiveInfo receiveInfo2) {
        sign=2;
        Gson gson = new Gson();
        String json = "{'sign':"+ sign +",'ReceiveInfo':"+ gson.toJson(receiveInfo1) +",'ReceiveInfo2':"+gson.toJson(receiveInfo2)+"}";
        return ServletsConn.connServlets("ReceiveInfo",json);
    }
    private String setDefault(ReceiveInfo receiveInfo){
        sign=5;
        Gson gson = new Gson();
        String json = "{'sign':"+ sign +",'ReceiveInfo':"+gson.toJson(receiveInfo)+"}";
        return ServletsConn.connServlets("ReceiveInfo",json);
    }
    private String getDefault(User user){
        sign=4;
        Gson gson = new Gson();
        String json = "{'sign':"+ sign +",'User':"+gson.toJson(user)+"}";
        return ServletsConn.connServlets("ReceiveInfo",json);
    }
}