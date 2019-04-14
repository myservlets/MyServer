package servlets;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import data_access_object.CommentDAO;
import entity.Comment;
import entity.CommentItem;
import entity.Goods;

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

@WebServlet(name = "FavoritesServlet")
public class FavoritesServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Gson gson=new Gson();
        System.out.println("Favorites:");
        Goods goods = new Goods();

        // 设置响应内容类型
        response.setContentType("text/html;charset=utf-8");
        response.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");

        try (PrintWriter out = response.getWriter()) {

            BufferedReader reader = request.getReader();
            String json = reader.readLine();
            System.out.println(json);
            reader.close();
            JsonObject jsonObject = gson.fromJson(json, JsonObject.class);
            int sign = Integer.parseInt(jsonObject.get("sign").toString());
            switch (sign){
                case 0: //评论商品
                    int result = 0;
                    Comment comment = new Comment();
                    comment = gson.fromJson(jsonObject.get("comment").toString(),Comment.class);
                    if(CommentDAO.addComment(comment) == null) result=1;
                    HashMap<String,Integer> param = new HashMap<>();
                    param.put("status",result);
                    String retStr = gson.toJson(param);
                    out.write(retStr);
                    break;
                case 1://查询商品的评论信息
                    result = 2;
                    int goodsId = Integer.parseInt(jsonObject.get("goodsId").toString());
                    ArrayList<CommentItem> commentItems = CommentDAO.queryComment(goodsId);
                    retStr = "{'status':"+ result +",'ArrayList<CommentItem>':"+gson.toJson(commentItems)+"}";
                    out.write(retStr);
                    break;
                case 2://查询该用户发布过的评论
                    result = 3;
                    String userId = jsonObject.get("userId").toString();
                    ArrayList<Comment> comments = CommentDAO.queryCommentFromUser(userId);
                    retStr = "{'status':"+ result +",'ArrayList<Comment>':"+gson.toJson(comments)+"}";
                    out.write(retStr);
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
