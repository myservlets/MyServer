package servlets;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import conn_interface.ServletsConn;
import entity.User;
import org.junit.Test;

public class AddFriendServletTest {

    @Test
    public void doPost() {
        User user0 = new User();
        user0.setNickname("ZhangSan");
        user0.setPassword("123456");
        user0.setUserId("z002");
        User user1 = new User();
        //user1.setNickname("hu1");
        user1.setUserId("456");
        handleAddFriend(addFriend(user1,user0));

    }

    private void handleAddFriend(String json) {
        int result = 2;
        if(json != null) {
            Gson gson = new Gson();
            JsonObject jsonObject = gson.fromJson(json, JsonObject.class);
            result = Integer.parseInt(jsonObject.get("status").toString());
        }

        switch (result){
            case 5:
                System.out.println("你已拒绝该用户的好友请求");
                break;
            case 4:
                System.out.println("你已接受该用户的好友请求");
                break;
            case 3:
                System.out.println("该用户已在你的好友列表中");
                break;
            case 2:
                System.out.println("失败");
                break;
            case 1:
                System.out.println("用户名不存在");
                break;
            case 0:
                System.out.println("已成功发送申请");
                break;

        }
    }

    private String addFriend(User user0, User user1) {
        Gson gson = new Gson();
        String json = "{'sign':0,'User0':"+gson.toJson(user0)+",'User1':"+gson.toJson(user1)+"}";
        return ServletsConn.connServlets("addfriend",json);
    }
}