package servlets;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import conn_interface.ServletsConn;
import entity.Goods;
import entity.Order;
import org.junit.Test;

import java.lang.reflect.Type;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class OrderServletTest {

    private int sign = 0;// 0/发布商品 1/查看已发布的商品信息 2/修改商品信息 3//删除
    @Test
    public void doPost() {
        Order order = new Order();
        order.setUserId("1");
        order.setStatus(1);
        order.setRemark(null);
        order.setGoodsId(1);
        //order.setDate();
        order.setCount(3);
        order.setCost(26.2);
        handle(addOrder(order));//添加订单
        //handle(query("1",1));//查询
        order.setStatus(2);
        order.setOrderId(3);
        handle(update(order));//修改
        handle(delete(order));//删除
    }

    private void handle(String json) {
        if(json == null) return;
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(json, JsonObject.class);
        int result = Integer.parseInt(jsonObject.get("status").toString());
        switch (result){
            case 0:
                System.out.println("添加成功");
                break;
            case 1:
                System.out.println("添加失败");
                break;
            case 2:
                System.out.println("修改成功");
                break;
            case 3:
                System.out.println("修改失败");
                break;
            case 4:
                System.out.println("删除成功");
                break;
            case 5:
                System.out.println("删除失败");
                break;
            case 6:
                System.out.println("查询成功");
                ArrayList<Order> orders = new ArrayList<>();
                Type listType = new TypeToken<ArrayList<Order>>() {}.getType();
                orders = gson.fromJson(jsonObject.get("ArrayList<Order>").toString(),listType);
                System.out.println(orders);
                break;
        }
    }

    private String addOrder(Order order) {
        Gson gson = new Gson();
        sign = 0;//添加订单
        String json = "{'sign':"+ sign +",'Order':"+gson.toJson(order)+"}";
        return ServletsConn.connServlets("Order",json);
    }
    private String update(Order order) {
        Gson gson = new Gson();
        sign = 1;//修改订单
        String json = "{'sign':"+ sign +",'Order':"+gson.toJson(order)+"}";
        return ServletsConn.connServlets("Order",json);
    }
    private String delete (Order order){
        Gson gson = new Gson();
        sign = 2;//删除
        String json = "{'sign':"+ sign +",'Order':"+gson.toJson(order)+"}";
        return ServletsConn.connServlets("Order",json);
    }
    private String query (String userId,int status){
        Gson gson = new Gson();
        sign = 3;//查询
        status = 0;// 0/购物车 1/待付款 2/待发货 3/待收货 4/待评论 5/售后
        String json = "{'sign':"+ sign +",'userId':"+userId+",'status':"+status+"}";
        return ServletsConn.connServlets("Order",json);
    }
}