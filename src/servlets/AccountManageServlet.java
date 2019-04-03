package servlets;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import data_access_object.ReceiveInfoDAO;
import data_access_object.UserDAO;
import entity.ReceiveInfo;
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

@WebServlet(name = "AccountManageServlet")
public class AccountManageServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("AccountManage:");

        // 设置响应内容类型
        response.setContentType("text/html;charset=utf-8");
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");

        try (PrintWriter out = response.getWriter()) {

            //获得请求中传来的sign和ReceiveInfo对象
            BufferedReader reader = request.getReader();
            String infoStr = reader.readLine();
            System.out.println(infoStr);
            Gson gson=new Gson();
            JsonObject jsonObject=gson.fromJson(infoStr,JsonObject.class);
            int sign=Integer.parseInt(jsonObject.get("sign").toString());
            String s = gson.fromJson(jsonObject.get("String").toString(),String.class);
            User user = gson.fromJson(jsonObject.get("User").toString(),User.class);
            reader.close();
            int result = UserDAO.updateUser(sign,s,user);
            Map<String, Integer> params = new HashMap<>();
            params.put("result",result);

            s = gson.toJson(params);
            out.write(s);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }
}
