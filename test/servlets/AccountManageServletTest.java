package servlets;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import conn_interface.ServletsConn;
import entity.ReceiveInfo;
import entity.User;
import org.junit.Test;

import static org.junit.Assert.*;

public class AccountManageServletTest {

    private static int sign;// 0/修改昵称 1/修改密码 2/修改头像;
    @Test
    public void doPost() {
        User user1 = new User();
        user1.setNickname("hu2");
        user1.setPassword("123456");
        user1.setUserid("2");
        String s = "hu222";
        sign = 0;
        handleResult(doReceiveInfo(user1,s));
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
    private String doReceiveInfo(User user, String s) {
        Gson gson = new Gson();
        if(sign!=0&&sign!=1)
            return null;
        String json = "{'sign':"+ sign +",'String':"+ s +",'User':"+gson.toJson(user)+"}";
        return ServletsConn.connServlets("AccountManage",json);
    }
}