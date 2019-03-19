package servlets;

import com.google.gson.Gson;
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
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "RegisterServlet")
public class RegisterServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=utf-8");
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");

        System.out.println("regist:");
        User user = new User();
        Gson gson = new Gson();
        try (PrintWriter out = response.getWriter()) {

            //获得请求中传来的用户名和密码
            BufferedReader reader = request.getReader();
            String userStr = reader.readLine();
            System.out.println(userStr);
            reader.close();

            user=gson.fromJson(userStr,User.class);


            //密码验证结果
            int result = registUser(user.getUsername(),user.getPassword(),user.getUserid());

            Map<String, Integer> params = new HashMap<>();
            params.put("result",result);

            String s = gson.toJson(params);
            out.write(s);
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }



    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }

    private int registUser(String userName, String password, String userId) {
        User user = UserDAO.queryUser(userName);
        if(user != null)
            return 1;
        user = UserDAO.insertUser(userName,password,userId);
        if(user == null)
            return 2;
        return  0;
    }

}
