package servlets;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import data_access_object.CommentDAO;
import data_access_object.GoodsDAO;
import data_access_object.PageViewsDAO;
import data_access_object.UserDAO;
import entity.Goods;
import entity.GoodsDetails;
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
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 设置响应内容类型
        response.setContentType("text/html;charset=utf-8");
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");

        System.out.println("HandleGoodsInfo:");
        Goods goods = new Goods();
        Gson gson = new Gson();

        try (PrintWriter out = response.getWriter()) {

            //获得请求中传来的Goods对象
            BufferedReader reader = request.getReader();
            String json = reader.readLine();
            JsonObject jsonObject = gson.fromJson(json,JsonObject.class);
            int sign = Integer.parseInt(jsonObject.get("sign").toString());
            System.out.println(json);
            reader.close();

            goods = gson.fromJson(jsonObject.get("Goods"),Goods.class);
            try {
                switch (sign) {
                    case 0://发布商品
                        //结果
                        int result = 0;
                        Goods retGoods = GoodsDAO.insertGoods(goods);
                        if (retGoods == null)
                            result = 1;
                        Map<String, Integer> params = new HashMap<>();
                        gson = new Gson();
                        String str=",'goods':" + gson.toJson(retGoods) + "}";
                        String retJson = "{'status':" + result;
                        retJson+=str;
                        out.write(retJson);
                        break;
                    case 1://查询已发布的商品列表
                        result = 2;
                        String userId = jsonObject.get("userId").toString();
                        ArrayList<Goods> goodsArrayList = new ArrayList<>();
                        goodsArrayList = GoodsDAO.queryGoodsList(userId);
                        if (goodsArrayList == null)
                            result = 3;
                        retJson = "{'status':" + result + ",'ArrayList<Goods>':" + gson.toJson(goodsArrayList) + "}";
                        out.write(retJson);
                        break;
                    case 2://修改商品信息
                        result = 4;
                        if (GoodsDAO.updateGoods(goods) == 0)
                            result = 5;
                        params = new HashMap<>();
                        params.put("status", result);
                        retJson = gson.toJson(params);
                        out.write(retJson);
                        break;
                    case 3://删除商品
                        result = 6;
                        int goodsId = Integer.parseInt(jsonObject.get("goods").toString());
                        if (GoodsDAO.deleteGoods(goodsId) == 0)
                            result = 7;
                        params = new HashMap<>();
                        params.put("status", result);
                        retJson = gson.toJson(params);
                        out.write(retJson);
                    case 4://查询推荐商品
                        result = 8;
                        goodsArrayList = GoodsDAO.queryGoodsList();
                        if (goodsArrayList == null)
                            result = 9;
                        retJson = "{'status':" + result + ",'ArrayList<Goods>':" + gson.toJson(goodsArrayList) + "}";
                        out.write(retJson);
                        break;
                    case 5://查询商品详情
                        result = 10;
                        GoodsDetails goodsDetails = new GoodsDetails();
                        goodsId = Integer.parseInt(jsonObject.get("goodsId").toString());
                        userId = jsonObject.get("userId").toString();
                        int i = PageViewsDAO.addGoodsPageViews(userId,goodsId);
                        if(i == -1) {
                            result = 11;
                            params = new HashMap<>();
                            params.put("status",result);
                            out.write(gson.toJson(params));
                            break;
                        }else if(i==1)
                            GoodsDAO.updatePageViews(goodsId);
                        goods = GoodsDAO.queryGoods(goodsId);
                        User user = UserDAO.queryUser(goods.getUserId());
                        goodsDetails.setSaler(user);
                        goodsDetails.setChoosedGoods(goods);
                        goodsDetails.setLatestComment(CommentDAO.queryComment(goodsId).get(0).getComment());
                        goodsDetails.setRecommendGoods(GoodsDAO.queryGoodsList());
                        retJson = "{'status':" + result + ",'goodsDetails':" + gson.toJson(goodsDetails) + "}";
                        out.write(retJson);
                        break;
                }
            }catch (NullPointerException e){
                out.write("{'status':"+-1+"}");
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
