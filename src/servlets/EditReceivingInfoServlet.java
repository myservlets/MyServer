package servlets;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.sun.deploy.net.HttpRequest;
import com.sun.deploy.net.HttpResponse;
import data_access_object.ReceiveInfoDAO;
import entity.ReceiveInfo;

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

@WebServlet(name = "EditReceivingInfoServlet")
public class EditReceivingInfoServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException,IOException {
        Gson gson = new Gson();
        ReceiveInfo receiveInfo1 = new ReceiveInfo();
        ReceiveInfo receiveInfo2 = new ReceiveInfo();
        System.out.println("EditReceivingInfo");

        //设置响应内容类型
        response.setContentType("text/html;charset=utf-8");
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");

        try(PrintWriter out = response.getWriter()){

            BufferedReader reader = request.getReader();
            String infoStr = reader.readLine();
            System.out.println(infoStr);
            reader.close();

            JsonObject jsonObject=gson.fromJson(infoStr,JsonObject.class);
            String s = jsonObject.get("ReceiveInfo1").toString();
            receiveInfo1 = gson.fromJson(s,ReceiveInfo.class);
            s = jsonObject.get("ReceiveInfo2").toString();
            receiveInfo2 = gson.fromJson(s,ReceiveInfo.class);

            int result = ReceiveInfoDAO.editReceiveInfo(receiveInfo1,receiveInfo2);
            Map<String, Integer> params = new HashMap<>();
            params.put("result",result);

            s = gson.toJson(params);
            out.write(s);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req,resp);
    }
}
