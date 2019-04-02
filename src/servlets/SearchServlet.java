package servlets;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import data_access_object.GoodsDAO;
import data_access_object.UserDAO;
import entity.Goods;
import entity.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class SearchServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Gson gson=new Gson();
        System.out.println("Search:");
        User user = new User();

        // 设置响应内容类型
        resp.setContentType("text/html;charset=utf-8");
        resp.setCharacterEncoding("utf-8");
        resp.setCharacterEncoding("utf-8");

        try (PrintWriter out = resp.getWriter()) {

            //获得请求中传来的关键字
            BufferedReader reader = req.getReader();
            String json = reader.readLine();
            System.out.println(json);
            reader.close();
            JsonObject jsonObject = gson.fromJson(json,JsonObject.class);
            int flag = Integer.parseInt(jsonObject.get("sign").toString());
            String keyword = jsonObject.get("keyword").getAsString();
            switch (flag){
                case 0:
                    ArrayList<User> users = new ArrayList<>();
                    users = UserDAO.fuzzySearchUser(keyword);
                    String retJson = gson.toJson(users);
                    out.write(retJson);
                    break;
                case 1:
                    ArrayList<Goods> goodsArrayList = new ArrayList<>();
                    goodsArrayList = GoodsDAO.fuzzySearchGoods(keyword);
                    retJson = gson.toJson(goodsArrayList);
                    out.write(retJson);
                    break;
            }

        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
