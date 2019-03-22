package servlets;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import data_access_object.FriendDAO;
import data_access_object.UserDAO;
import entity.*;

@WebServlet(name = "FriendsServlet")
public class FriendsServlet extends HttpServlet {
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
            User user = gson.fromJson(jsonObject.get("User").toString(), User.class);

            ArrayList<User> userArrayList = new ArrayList<>();
            ArrayList<FriendShip> friendShips;
            switch (sign){
                case 0:
                    friendShips = FriendDAO.queryFriend("*",user.getUserid(),0);
                    for(int i=0;i<friendShips.size();i++){
                        userArrayList.add(UserDAO.queryUser(friendShips.get(i).getUid()));
                        userArrayList.get(i).setPassword("");
                    }
                    break;
                case 1:
                    friendShips = FriendDAO.queryFriend(user.getUserid(),"*",0);
                    for(int i=0;i<friendShips.size();i++){
                        userArrayList.add(UserDAO.queryUser(friendShips.get(i).getCid()));
                        userArrayList.get(i).setPassword("");
                    }
                    break;
                case 2:
                    friendShips = FriendDAO.queryFriend(user.getUserid(),"*",1);
                    for(int i=0;i<friendShips.size();i++){
                        User via = UserDAO.queryUser(friendShips.get(i).getCid());
                        via.setPassword("");
                        userArrayList.add(via);
                    }
                    friendShips = FriendDAO.queryFriend("*",user.getUserid(),1);
                    for(int i=0;i<friendShips.size();i++){
                        User via = UserDAO.queryUser(friendShips.get(i).getUid());
                        via.setPassword("");
                        userArrayList.add(via);
                    }
                    break;
            }

            s=gson.toJson(userArrayList);
            out.write(s);
        }
        catch (Exception e){
            e.printStackTrace();
        }



    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
