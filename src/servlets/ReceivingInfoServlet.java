package servlets;


import com.google.gson.Gson;
import com.google.gson.JsonObject;
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
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "ReceivingInfoServlet")
public class ReceivingInfoServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Gson gson=new Gson();
        System.out.println("ReceiveInfo:");
        User user = new User();
        ReceiveInfo receiveInfo = new ReceiveInfo();

        // 设置响应内容类型
        response.setContentType("text/html;charset=utf-8");
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");

        try (PrintWriter out = response.getWriter()) {

            //获得请求中传来的sign和ReceiveInfo对象
            BufferedReader reader = request.getReader();
            String infoStr = reader.readLine();
            System.out.println(infoStr);
            int result = handleSign(infoStr);
            reader.close();
            Map<String, Integer> params = new HashMap<>();
            params.put("result",result);

            String s = gson.toJson(params);
            out.write(s);
        }
        catch (Exception e){
            e.printStackTrace();
        }


    }

    public static int handleSign(String json) {
        int flag=0;//用于标识的信号量
        if(json != null){
            Gson gson=new Gson();
            JsonObject jsonObject=gson.fromJson(json,JsonObject.class);
            flag=Integer.parseInt(jsonObject.get("sign").toString());
            ReceiveInfo receiveInfo=gson.fromJson(jsonObject.get("ReceiveInfo").toString(),ReceiveInfo.class);
            if(flag==0){
                System.out.println("请求添加收货信息");
                return ReceiveInfoDAO.insertReceiveInfo(receiveInfo.getUserId(),receiveInfo.getPhone(),receiveInfo.getAddress());
            }
            else if(flag==1){
                System.out.println("请求删除收货信息");
                return ReceiveInfoDAO.deleteReceiveInfo(receiveInfo.getUserId(),receiveInfo.getPhone(),receiveInfo.getAddress());
            }
        }
        return 0;
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }

}
