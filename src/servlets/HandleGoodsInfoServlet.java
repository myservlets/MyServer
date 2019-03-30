package servlets;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import data_access_object.GoodsDAO;
import data_access_object.UserDAO;
import entity.Goods;
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

@WebServlet(name = "HandleGoodsInfoServlet")
public class HandleGoodsInfoServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("HandleGoodsInfo:");
        Goods goods = new Goods();
        Gson gson = new Gson();

        try (PrintWriter out = resp.getWriter()) {

            //获得请求中传来的Goods对象
            BufferedReader reader = req.getReader();
            String json = reader.readLine();
            JsonObject jsonObject = gson.fromJson(json,JsonObject.class);
            int sign = Integer.parseInt(jsonObject.get("sign").toString());
            System.out.println(json);
            reader.close();

            goods = gson.fromJson(jsonObject.get("Goods"),Goods.class);
            switch (sign){
                case 0://发布商品
                    //结果
                    int result = 0;
                    Goods resGoods = GoodsDAO.insertGoods(goods);
                    if(resGoods == null)
                        result = 1;
                    Map<String, Integer> params = new HashMap<>();

                    params.put("status",result);
                    String retJson = gson.toJson(params);
                    out.write(retJson);
                    break;
                case 1://查询已发布的商品列表
                    result = 2;
                    String userId = jsonObject.get("userId").toString();
                    ArrayList<Goods> goodsArrayList = new ArrayList<>();
                    goodsArrayList = GoodsDAO.queryGoods(userId);
                    if(goodsArrayList == null)
                        result = 3;
                    retJson = "{'status':"+ result +",'ArrayList<Goods>':"+gson.toJson(goodsArrayList)+"}";
                    out.write(retJson);
                    break;
                case 2://修改商品信息
                    result = 4;
                    if (GoodsDAO.updateGoods(goods)==0)
                        result = 5;
                    params = new HashMap<>();
                    params.put("status",result);
                    retJson = gson.toJson(params);
                    out.write(retJson);
                    break;
                case 3://删除商品
                    result=6;
                    int goodsId = Integer.parseInt(jsonObject.get("goodsId").toString());
                    if(GoodsDAO.deleteGoods(goodsId)==0)
                        result = 7;
                    params = new HashMap<>();
                    params.put("status",result);
                    retJson = gson.toJson(params);
                    out.write(retJson);
                    break;
                    default:
                        break;
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
