package servlets;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import data_access_object.OrderDAO;
import entity.Goods;
import entity.Order;
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

@WebServlet(name = "OrderServlet")
public class OrderServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Gson gson=new Gson();
        System.out.println("order:");
        Goods goods = new Goods();

        // 设置响应内容类型
        response.setContentType("text/html;charset=utf-8");
        response.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");

        try (PrintWriter out = response.getWriter()) {

            //获得请求中传来的用户名和密码
            BufferedReader reader = request.getReader();
            String json = reader.readLine();
            System.out.println(json);
            reader.close();
            JsonObject jsonObject = gson.fromJson(json, JsonObject.class);
            int sign = Integer.parseInt(jsonObject.get("sign").toString());
            switch (sign){
                case 0: //添加订单
                    int result = 0;
                    Order order = new Order();
                    order = gson.fromJson(jsonObject.get("Order").toString(),Order.class);
                    if(OrderDAO.addOrder(order) == null) result=1;
                    HashMap<String,Integer> param = new HashMap<>();
                    param.put("status",result);
                    String retStr = gson.toJson(param);
                    out.write(retStr);
                    break;
                case 1://修改订单
                    result = 2;
                    order = gson.fromJson(jsonObject.get("Order").toString(),Order.class);
                    if(OrderDAO.updateOrder(order)==0)result = 3;
                    param = new HashMap<>();
                    param.put("status",result);
                    retStr = gson.toJson(param);
                    out.write(retStr);
                    break;
                case 2://删除订单
                    result = 4;
                    order = gson.fromJson(jsonObject.get("Order").toString(),Order.class);
                    if(OrderDAO.deleteOrder(order)==0)result = 5;
                    param = new HashMap<>();
                    param.put("status",result);
                    retStr = gson.toJson(param);
                    out.write(retStr);
                    break;
                case 3://查询订单列表
                    result = 6;
                    String userId = jsonObject.get("userId").toString();
                    int status = Integer.parseInt(jsonObject.get("status").toString());
                    ArrayList<Order> orders = new ArrayList<>();
                    orders = OrderDAO.queryOrder(userId,status);
                    retStr = "{'status':"+ result +",'ArrayList<Order>':"+gson.toJson(orders)+"}";
                    out.write(retStr);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
}
