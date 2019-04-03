package servlets;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import conn_interface.ServletsConn;
import entity.Goods;
import entity.User;
import org.junit.Test;

import java.lang.reflect.Type;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class SearchServletTest {

    private int sign; // 0/检索用户 1/检索商品
    @Test
    public void doPost() {
        String s = "hhhhhhhh";
        handle(search(s));
    }

    public String search(String keyword){
        sign = 0;
        String json="{'sign':"+sign+",'keyword':"+keyword+"}";
        return ServletsConn.connServlets("Search",json);
    }

    public void handle(String json) {
        Gson gson = new Gson();
        ArrayList<User> users = new ArrayList<>();
        Type listType = new TypeToken<ArrayList<User>>() {}.getType();
        users = gson.fromJson(json,listType);
        System.out.println(users);
    }
}