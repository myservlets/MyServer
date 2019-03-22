package servlets;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import conn_interface.ServletsConn;
import entity.ReceiveInfo;
import org.junit.Test;

import static org.junit.Assert.*;

public class EditReceivingInfoServletTest {

    @Test
    public void doPost() {
        ReceiveInfo receiveInfo = new ReceiveInfo();//编辑前的收货信息
        receiveInfo.setUserId("2");
        receiveInfo.setPhone("123456");
        receiveInfo.setAddress("湖北武汉");
        ReceiveInfo receiveInfo1 = new ReceiveInfo();//编辑后的收货信息
        receiveInfo1.setUserId("2");
        receiveInfo1.setPhone("172839");
        receiveInfo1.setAddress("四川绵阳");
        handleResult(editReceiveInfo(receiveInfo,receiveInfo1));
    }
    private void handleResult(String json) {
        int result = 0;
        if (json == null) {
            System.out.println("编辑失败401");
            return;
        }
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(json, JsonObject.class);
        result = Integer.parseInt(jsonObject.get("result").toString());
        switch (result){
            case 1:
                System.out.println("编辑成功");
                break;
            case 0:
                System.out.println("编辑失败402");
                break;
        }
    }
    private String editReceiveInfo(ReceiveInfo receiveInfo1,ReceiveInfo receiveInfo2) {
        Gson gson = new Gson();
        String json = "{'ReceiveInfo1':"+ gson.toJson(receiveInfo1) +",'ReceiveInfo2':"+gson.toJson(receiveInfo2)+"}";
        return ServletsConn.connServlets("EditReceivingInfo",json);
    }
}