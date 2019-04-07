package servlets;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import conn_interface.ServletsConn;
import entity.Goods;
import entity.Province;
import entity.User;
import org.junit.Test;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HandleGoodsInfoServletTest {


    private int sign = 0;// 0/发布商品 1/查看已发布的商品信息 2/修改商品信息 3//删除 4/查询所有已发布商品
    @Test
    public void doPost() {
        Goods goods = new Goods();
        goods.setPicAddress1("picAddress1");
        goods.setPicAddress2("picAddress2");
        goods.setPicAddress3("picAddress3");
        goods.setGoodsName("asdf");
        goods.setQuantity(2);
        goods.setInitNum(3);
        goods.setPrice(11.5);
        goods.setUserId("2");
        //handle(publish(goods));//发布
        String userId = "2";
        //handle(query(userId));//查询
        goods.setPrice(18.2);
        goods.setGoodsId(1);
        //handle(update(goods));//修改
        //handle(delete(goods.getGoodsId()));//删除
        handle(queryAll());
    }

    private void handle(String json) {
        int result = 2;
        if(json == null) return;
            Gson gson = new Gson();
            JsonObject jsonObject = gson.fromJson(json, JsonObject.class);
            result = Integer.parseInt(jsonObject.get("status").toString());
        switch (result){
            case 0:
                System.out.println("发布成功");
                break;
            case 1:
                System.out.println("发布失败");
                break;
            case 2:
                System.out.println("查询成功");
                ArrayList<Goods> goodsArrayList = new ArrayList<>();
                Type listType = new TypeToken<ArrayList<Goods>>() {}.getType();
                goodsArrayList = gson.fromJson(jsonObject.get("ArrayList<Goods>").toString(),listType);
                System.out.println(goodsArrayList);
                break;
            case 3:
                System.out.println("查询失败");
                break;
            case 4:
                System.out.println("修改成功");
                break;
            case 5:
                System.out.println("修改失败");
                break;
            case 6:
                System.out.println("删除成功");
                break;
            case 7:
                System.out.println("删除失败");
            case 8:
                System.out.println("查询成功");
                goodsArrayList = new ArrayList<>();
                listType = new TypeToken<ArrayList<Goods>>() {}.getType();
                goodsArrayList = gson.fromJson(jsonObject.get("ArrayList<Goods>").toString(),listType);
                System.out.println(goodsArrayList);
                break;
            case 9:
                System.out.println("查询失败");
                break;
        }
    }

    private String publish(Goods goods) {
        Gson gson = new Gson();
        sign = 0;//发布商品
        String json = "{'sign':"+ sign +",'Goods':"+gson.toJson(goods)+"}";
        return ServletsConn.connServlets("HandleGoodsInfo",json);
    }
    private String query(String userId) {
        Gson gson = new Gson();
        sign = 1;//查询商品
        String json = "{'sign':"+ sign +",'userId':"+userId+"}";
        return ServletsConn.connServlets("HandleGoodsInfo",json);
    }
    private String queryAll() {
        Gson gson = new Gson();
        sign = 4;//查询所有已发布商品
        String json = "{'sign':"+ sign +"}";
        return ServletsConn.connServlets("HandleGoodsInfo",json);
    }
    private String update(Goods goods) {
        Gson gson = new Gson();
        sign = 2;//修改商品
        String json = "{'sign':"+ sign +",'Goods':"+gson.toJson(goods)+"}";
        return ServletsConn.connServlets("HandleGoodsInfo",json);
    }
    private String delete (int goodsId){
        Gson gson = new Gson();
        sign = 3;//删除
        String json = "{'sign':"+ sign +",'goodsId':"+ goodsId +"}";
        return ServletsConn.connServlets("HandleGoodsInfo",json);
    }
}