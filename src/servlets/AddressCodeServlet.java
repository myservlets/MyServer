package servlets;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import data_access_object.FriendDAO;
import data_access_object.UserDAO;
import db_connecter.DBManager;
import entity.City;
import entity.County;
import entity.Province;
import entity.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AddressCodeServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("AddressCodeServlet:");
        // 设置响应内容类型
        response.setContentType("text/html;charset=utf-8");
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");

        PrintWriter out = response.getWriter();
        ArrayList<Province> provinces = new ArrayList<Province>();
        ArrayList<City> cities = new ArrayList<City>();
        ArrayList<County> counties = new ArrayList<County>();

        //获得数据库的连接对象
        Connection connection = DBManager.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
//        ResultSet resultSet1 = null;

        //生成SQL代码
        StringBuilder sqlStatement = new StringBuilder();
        sqlStatement.append("SELECT * FROM address_code WHERE pid=?");
//        StringBuilder sqlStatement1 = new StringBuilder();
//        sqlStatement1.append("SELECT * FROM address_code WHERE pid=?");
        try {
            //省
            preparedStatement = connection.prepareStatement(sqlStatement.toString());
            preparedStatement.setString(1, "0");
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Province province = new Province();
                province.setName(resultSet.getString("name"));
                province.setId(resultSet.getString("id"));
                provinces.add(province);
            }
            for (int i = 0;i<provinces.size();i++){
                //市
                preparedStatement = connection.prepareStatement(sqlStatement.toString());
                preparedStatement.setString(1,provinces.get(i).getId() );
                resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    City city = new City();
                    city.setName(resultSet.getString("name"));
                    city.setId(resultSet.getString("id"));
                    cities.add(city);
                }
                provinces.get(i).setCities(cities);
                cities = new ArrayList<>();
            }
            for (int i = 0;i<provinces.size();i++) {
                cities = provinces.get(i).getCities();
                for (int j = 0; j < cities.size(); j++) {
                    //县
                    preparedStatement = connection.prepareStatement(sqlStatement.toString());
                    preparedStatement.setString(1, cities.get(j).getId());
                    resultSet = preparedStatement.executeQuery();
                    while (resultSet.next()) {
                        County county = new County();
                        county.setName(resultSet.getString("name"));
                        counties.add(county);
                    }
                    cities.get(j).setCounties(counties);
                    counties = new ArrayList<>();
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(AddressCodeServlet.class.getName()).log(Level.SEVERE, null, ex);
            return;
        } finally {
            DBManager.closeAll(connection, preparedStatement,null);
        }
            Gson gson = new Gson();
            String json = gson.toJson(provinces);
            out.write(json);
        }
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }
}
