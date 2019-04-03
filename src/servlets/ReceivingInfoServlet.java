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

        // 设置响应内容类型
        response.setContentType("text/html;charset=utf-8");
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");

        try (PrintWriter out = response.getWriter()) {
            //获得请求中传来的sign和ReceiveInfo对象
            BufferedReader reader = request.getReader();
            String infoStr = reader.readLine();
            reader.close();
            System.out.println(infoStr);
            int flag=0;//用于标识的信号量
            int status = 0;;
            Map<String, Integer> params = new HashMap<>();
            if(infoStr != null){
                JsonObject jsonObject=gson.fromJson(infoStr,JsonObject.class);
                flag=Integer.parseInt(jsonObject.get("sign").toString());// 0/添加 1/删除 2/编辑收货信息 3/查询收货信息列表 ;
                switch (flag){
                    case 0:
                        System.out.println("请求添加收货信息");
                        ReceiveInfo receiveInfo=gson.fromJson(jsonObject.get("ReceiveInfo").toString(),ReceiveInfo.class);
                        if(ReceiveInfoDAO.insertReceiveInfo(receiveInfo.getUserId(),receiveInfo.getPhone(),receiveInfo.getAddress())==0)
                            status = 1;
                        params = new HashMap<>();
                        params.put("status",status);
                        String retStr = gson.toJson(params);
                        out.write(retStr);
                        break;
                    case 1:
                        status = 2;
                        System.out.println("请求删除收货信息");
                        receiveInfo=gson.fromJson(jsonObject.get("ReceiveInfo").toString(),ReceiveInfo.class);
                        if(ReceiveInfoDAO.deleteReceiveInfo(receiveInfo)==0)
                            status = 3;
                        params = new HashMap<>();
                        params.put("status",status);
                        retStr = gson.toJson(params);
                        out.write(retStr);
                        break;
                    case 2:
                        status = 4;
                        System.out.println("请求编辑收货信息");
                        receiveInfo=gson.fromJson(jsonObject.get("ReceiveInfo").toString(),ReceiveInfo.class);
                        String s = jsonObject.get("ReceiveInfo2").toString();
                        ReceiveInfo receiveInfo2 = gson.fromJson(s,ReceiveInfo.class);
                        int result = ReceiveInfoDAO.editReceiveInfo(receiveInfo,receiveInfo2);
                        if(result == 0)
                            status = 5;
                        params = new HashMap<>();
                        params.put("status",status);
                        retStr = gson.toJson(params);
                        out.write(retStr);
                        break;
                    case 3:
                        status = 6;
                        System.out.println("请求查询收货信息列表");
                        User user = new User();
                        ArrayList<ReceiveInfo> receiveInfos = new ArrayList<ReceiveInfo>();
                        user=gson.fromJson(jsonObject.get("User").toString(),User.class);
                        receiveInfos = ReceiveInfoDAO.queryInfo(user.getUserId());
                        retStr = "{'status':"+status+",'ArrayList<ReceiveInfo>':"+gson.toJson(receiveInfos)+"}";
                        out.write(retStr);
                        break;
                }
            }
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
