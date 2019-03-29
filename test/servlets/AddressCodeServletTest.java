package servlets;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import conn_interface.ServletsConn;
import entity.City;
import entity.Province;
import org.junit.Test;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class AddressCodeServletTest {

    @Test
    public void doPost() throws IOException {
        String json = ServletsConn.connServlets("AddressCode","");
        if(json == null)
            return;
        Gson gson = new Gson();
        Type listType = new TypeToken<ArrayList<Province>>() {}.getType();
        ArrayList<Province> provinces = gson.fromJson(json,listType);
        System.out.println(provinces.get(0).getCities().get(0).getName());
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("a.txt"));
        for (Province province : provinces) {
            bufferedWriter.write(province.toString());
            bufferedWriter.newLine();
            bufferedWriter.flush();
        }
    }
}