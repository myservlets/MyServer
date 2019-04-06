package servlets;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import data_access_object.FriendDAO;
import data_access_object.UserDAO;
import entity.ChatMSG;
import entity.FriendShip;
import entity.User;
import websocket_server.SocketServer;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;

@WebServlet(name = "AddFriendServlet")
public class AddFriendServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //初始化
        System.out.println("add friend:");
        Gson gson = new Gson();
        String s="";
        // 设置响应内容类型
        response.setContentType("text/html;charset=utf-8");
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");

        //获取传递的内容并处理
        try (PrintWriter out = response.getWriter()) {

            //获得请求中传来的信息
            BufferedReader reader = request.getReader();
            String json = reader.readLine();
            System.out.println(json);
            reader.close();

            JsonObject jsonObject = gson.fromJson(json, JsonObject.class);
            int sign = Integer.parseInt(jsonObject.get("sign").toString());
            User user0 = gson.fromJson(jsonObject.get("User0").toString(), User.class);
            User user1 = gson.fromJson(jsonObject.get("User1").toString(), User.class);

            switch (sign){
                case 0:
                    //添加好友
                    User user2 = UserDAO.queryUser(user1.getUserId());
                    if(user2 == null){
                        s = "{'status':1}";//用户不存在
                    }
                    else {
                        ArrayList<FriendShip> friendShips = FriendDAO.queryFriend(user0.getUserId(),user1.getUserId(),0);
                        if((friendShips != null)&& !friendShips.isEmpty()){
                            s = "{'status':0}";//已发送好友请求
                            //SocketServer.startService();
                            ChatMSG chatMSG = new ChatMSG();
                            chatMSG.setFromid(user0.getUserId());
                            chatMSG.setTargetid(user1.getUserId());
                            Date date = new Date();
                            date.getTime() ;
                            //chatMSG.setDate(date);
                            //chatMSG.setContent("add_friend_request_signal");
                            //SocketServer.chatMSGS.add(chatMSG);
                            break;
                        }
                        friendShips = FriendDAO.queryFriend(user0.getUserId(),user1.getUserId(),1);
                        ArrayList<FriendShip> friendShips1 = FriendDAO.queryFriend(user1.getUserId(),user0.getUserId(),1);
                        if((!friendShips.isEmpty())||(!friendShips1.isEmpty())){
                            s = "{'status':3}";//该用户已在你的好友列表
                            break;
                        }
                        int i = FriendDAO.addFriend(user0.getUserId(),user1.getUserId(),0);
                        s = "{'status':"+i+"}";//已发送好友请求或失败
                    }
                    break;
                case 1:
                    int i = FriendDAO.updateFriendStatus(user1.getUserId(),user0.getUserId(),1);
                    s = "{'status':"+i+"}";//已接受好友请求或失败
                    break;
                case 2:
                    i = FriendDAO.updateFriendStatus(user1.getUserId(),user0.getUserId(),2);
                    s = "{'status':"+i+"}";//已接受好友请求或失败
                    break;
            }


            out.write(s);
        }
        catch (Exception e){
            e.printStackTrace();
        }



    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
}
