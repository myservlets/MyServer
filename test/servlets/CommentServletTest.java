package servlets;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import conn_interface.ServletsConn;
import entity.Comment;
import entity.CommentItem;
import entity.Order;
import org.junit.Test;

import java.lang.reflect.Type;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class CommentServletTest {

    private int sign;

    @Test
    public void doPost() {
        handle(queryComment(5));
    }


    private void handle(String json) {
        if(json == null) return;
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(json, JsonObject.class);
        int result = Integer.parseInt(jsonObject.get("status").toString());
        switch (result){
            case 0:
                System.out.println("评论成功");//评论商品
                break;
            case 1:
                System.out.println("评论失败");
                break;
            case 2:
                System.out.println("查询成功");//查询商品的评论信息
                ArrayList<CommentItem> commentItems = new ArrayList<>();
                Type listType = new TypeToken<ArrayList<CommentItem>>() {}.getType();
                commentItems = gson.fromJson(jsonObject.get("ArrayList<CommentItem>").toString(),listType);
                System.out.println(commentItems.size());
                break;
            case 3:
                System.out.println("查询成功");//查询该用户发布过的评论
                ArrayList<Comment> comments = new ArrayList<>();
                listType = new TypeToken<ArrayList<Comment>>() {}.getType();
                comments = gson.fromJson(jsonObject.get("ArrayList<Comment>").toString(),listType);
                System.out.println(comments);
                break;
        }
    }

    private String doComment(Comment comment) {
        Gson gson = new Gson();
        sign = 0;//评论商品
        String json = "{'sign':"+ sign +",'comment':"+gson.toJson(comment)+"}";
        return ServletsConn.connServlets("comment",json);
    }
    private String queryComment (int goodsId){
        Gson gson = new Gson();
        sign = 1;//查询商品的评论信息
        String json = "{'sign':"+ sign +",'goodsId':"+goodsId+"}";
        return ServletsConn.connServlets("comment",json);
    }
    private String queryCommentFromUser(String userId) {
        Gson gson = new Gson();
        sign = 2;//查询该用户发布过的评论
        String json = "{'sign':"+ sign +",'userId':"+userId+"}";
        return ServletsConn.connServlets("comment",json);
    }

}