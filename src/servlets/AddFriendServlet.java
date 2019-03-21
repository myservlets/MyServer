package servlets;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import data_access_object.UserDAO;
import entity.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "AddFriendServlet")
public class AddFriendServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //初始化
        System.out.println("add friend:");
        Gson gson = new Gson();
        String s;
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


            User user2 = UserDAO.queryUser(user1.getUserid());
            if(user2 == null){
                s = "{'status':1}";
            }
            else {
                s = "{'status':0}";
            }

            out.write(s);
        }
        catch (Exception e){
            e.printStackTrace();
        }



    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
