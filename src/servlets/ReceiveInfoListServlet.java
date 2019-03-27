package servlets;

import com.google.gson.Gson;
import data_access_object.ReceiveInfoDAO;
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
import java.util.ArrayList;

@WebServlet(name = "ReceiveInfoListServlet")
public class ReceiveInfoListServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Gson gson=new Gson();
        System.out.println("ReceiveInfoList:");


        // 设置响应内容类型
        response.setContentType("text/html;charset=utf-8");
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");

        try (PrintWriter out = response.getWriter()) {

            //获得请求中传来的userId
            BufferedReader reader = request.getReader();
            String userStr = reader.readLine();
            System.out.println(userStr);
            reader.close();

            User user = new User();
            ArrayList<ReceiveInfo> receiveInfos = new ArrayList<ReceiveInfo>();
            ReceiveInfo receiveInfo = new ReceiveInfo();
            user=gson.fromJson(userStr,User.class);
            receiveInfos = ReceiveInfoDAO.queryInfo(user.getUserId());

            String s = "{'status':0,'ArrayList<ReceiveInfo>':"+gson.toJson(receiveInfos)+"}";
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
