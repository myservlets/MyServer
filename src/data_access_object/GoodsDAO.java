package data_access_object;

import db_connecter.DBManager;
import entity.Goods;
import entity.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GoodsDAO {
    /**
     * 查询给定用户名的用户的详细信息
     *
     * @param userId 给定的用户ID
     * @return 查询到该用户发布的所有商品
     */
    public static ArrayList<Goods> queryGoods(String userId) {
        //获得数据库的连接对象
        Connection connection = DBManager.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        //生成SQL代码
        StringBuilder sqlStatement = new StringBuilder();
        sqlStatement.append("SELECT * FROM goods WHERE userId=?");

        //设置数据库的字段值
        try {
            preparedStatement = connection.prepareStatement(sqlStatement.toString());
            preparedStatement.setString(1,userId);

            resultSet = preparedStatement.executeQuery();
            ArrayList<Goods> goodsArrayList = new ArrayList<>();
            while (resultSet.next()) {
                Goods goods = new Goods();
                goods.setPrice(resultSet.getDouble("price"));
                goods.setGoodsName(resultSet.getString("goodsName"));
                goods.setQuantity(resultSet.getInt("quantity"));
                goods.setUserId(resultSet.getString("userId"));
                goods.setPicAddress1(resultSet.getString("picAddress1"));
                goods.setPicAddress2(resultSet.getString("picAddress2"));
                goods.setPicAddress3(resultSet.getString("picAddress3"));
                goods.setGoodsId(resultSet.getInt("goodsId"));
                goods.setContent(resultSet.getString("content"));
                goods.setType(resultSet.getString("type"));
                goodsArrayList.add(goods);
            }
            return goodsArrayList;
        } catch (SQLException ex) {
            Logger.getLogger(GoodsDAO.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } finally {
            DBManager.closeAll(connection, preparedStatement, resultSet);
        }
    }

    public static ArrayList<Goods> fuzzySearchGoods(String keyword) {
        //获得数据库的连接对象
        Connection connection = DBManager.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        ArrayList<Goods> goodsArrayList = new ArrayList<>();

        //生成SQL代码
        StringBuilder sqlStatement = new StringBuilder();
        sqlStatement.append("SELECT * FROM goods WHERE LOCATE(?, concat(content,type,goodsName))>0");

        //设置数据库的字段值
        try {
            preparedStatement = connection.prepareStatement(sqlStatement.toString());
            preparedStatement.setString(1, keyword);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Goods goods = new Goods();
                goods.setPrice(resultSet.getDouble("price"));
                goods.setGoodsName(resultSet.getString("goodsName"));
                goods.setQuantity(resultSet.getInt("quantity"));
                goods.setUserId(resultSet.getString("userId"));
                goods.setPicAddress1(resultSet.getString("picAddress1"));
                goods.setPicAddress2(resultSet.getString("picAddress2"));
                goods.setPicAddress3(resultSet.getString("picAddress3"));
                goods.setGoodsId(resultSet.getInt("goodsId"));
                goods.setContent(resultSet.getString("content"));
                goods.setType(resultSet.getString("type"));
                goodsArrayList.add(goods);
            }
            return goodsArrayList;
        } catch (SQLException ex) {
            Logger.getLogger(GoodsDAO.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } finally {
            DBManager.closeAll(connection, preparedStatement, resultSet);
        }
    }

    public static int deleteGoods(int goodsId) {
        //获得数据库的连接对象
        Connection connection = DBManager.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        //生成SQL代码
        StringBuilder sqlStatement = new StringBuilder();
        sqlStatement.append("DELETE FROM goods WHERE goodsId=?");

        //设置数据库的字段值
        try {
            preparedStatement = connection.prepareStatement(sqlStatement.toString());
            preparedStatement.setInt(1, goodsId);
            int i = preparedStatement.executeUpdate();
            return i;
        } catch (SQLException ex) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        } finally {
            DBManager.closeAll(connection, preparedStatement, resultSet);
        }
    }

    public static Goods insertGoods(Goods goods) {
        //获得数据库的连接对象
        Connection connection = DBManager.getConnection();
        PreparedStatement preparedStatement = null;

        //生成SQL代码
        StringBuilder sqlStatement = new StringBuilder();
        sqlStatement.append("INSERT INTO goods (initNum,quantity,userId,goodsName,price," +
                "picAddress1,picAddress2,picAddress3,content,type) VALUES (?,?,?,?,?,?,?,?,?,?)");

        //设置数据库的字段值
        try {
            preparedStatement = connection.prepareStatement(sqlStatement.toString());
            preparedStatement.setInt(1, goods.getInitNum());
            preparedStatement.setInt(2, goods.getQuantity());
            preparedStatement.setString(3, goods.getUserId());
            preparedStatement.setString(4, goods.getGoodsName());
            preparedStatement.setDouble(5, goods.getPrice());
            preparedStatement.setString(6, goods.getPicAddress1());
            preparedStatement.setString(7, goods.getPicAddress2());
            preparedStatement.setString(8, goods.getPicAddress3());
            preparedStatement.setString(9, goods.getContent());
            preparedStatement.setString(10, goods.getType());

            int i = preparedStatement.executeUpdate();
            if (i==1) {
                return goods;
            } else {
                return null;
            }
        } catch (SQLException ex) {
            Logger.getLogger(GoodsDAO.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } finally {
            DBManager.closeAll(connection, preparedStatement,null);
        }
    }

    public static int updateGoods(Goods goods){
        //获得数据库的连接对象
        Connection connection = DBManager.getConnection();
        PreparedStatement preparedStatement = null;

        //生成SQL代码
        StringBuilder sqlStatement = new StringBuilder();
        sqlStatement.append("UPDATE goods set initNum = "+ goods.getInitNum() +
                ",quantity = "+goods.getQuantity()+
                ",price = "+goods.getPrice()+
                ",userId = "+goods.getUserId()+
                ",goodsName = '"+goods.getGoodsName()+
                "',picAddress1 = "+goods.getPicAddress1()+
                ",picAddress2 = "+goods.getPicAddress2()+
                ",picAddress3 = "+goods.getPicAddress3()+
                ",content = "+goods.getContent()+
                ",type = "+goods.getType()+
                " where goodsId = ?");
        //设置数据库的字段值
        try {
            preparedStatement = connection.prepareStatement(sqlStatement.toString());
            preparedStatement.setInt(1, goods.getGoodsId());
            return preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(GoodsDAO.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        } finally {
            DBManager.closeAll(connection, preparedStatement,null);
        }
    }
}
