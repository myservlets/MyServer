package data_access_object;

import com.mysql.jdbc.Statement;
import utils.DoFiles;
import configuration_files.Source;
import db_connecter.DBManager;
import entity.Goods;

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
     * @param goodsId 给定的用户ID
     * @return 查询到的封装了详细信息的Goods对象
     */
    public static Goods queryGoods(int goodsId) {
        //获得数据库的连接对象
        Connection connection = DBManager.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        //生成SQL代码
        StringBuilder sqlStatement = new StringBuilder();
        sqlStatement.append("SELECT * FROM goods WHERE goodsId=?");

        //设置数据库的字段值
        try {
            preparedStatement = connection.prepareStatement(sqlStatement.toString());
            preparedStatement.setInt(1, goodsId);

            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
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
                return goods;
            } else {
                return null;
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } finally {
            DBManager.closeAll(connection, preparedStatement, resultSet);
        }
    }


    //这个值不能被其它客户端（Connection）影响，保证了你能够找回自己的 ID 而不用担心其它客户端的活动，而且不需要加锁
    public static int queryLastGoods() {
        //获得数据库的连接对象
        Connection connection = DBManager.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        //生成SQL代码
        StringBuilder sqlStatement = new StringBuilder();
        sqlStatement.append("SELECT LAST_INSERT_ID()");

        //设置数据库的字段值
        try {
            preparedStatement = connection.prepareStatement(sqlStatement.toString());

            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Goods goods = new Goods();
                System.out.println(resultSet);
                return resultSet.getInt(1);
            } else {
                return -1;
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, ex);
            return -1;
        } finally {
            DBManager.closeAll(connection, preparedStatement, resultSet);
        }
    }

    /**
     * 查询给定用户名的用户的详细信息
     *
     * @return 查询到该用户发布的所有商品
     */
    public static ArrayList<Goods> queryGoodsList() {
        //获得数据库的连接对象
        Connection connection = DBManager.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        //生成SQL代码
        StringBuilder sqlStatement = new StringBuilder();
        sqlStatement.append("SELECT * FROM goods");

        //设置数据库的字段值
        try {
            preparedStatement = connection.prepareStatement(sqlStatement.toString());

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
    /**
     * 查询给定用户名的用户的详细信息
     *
     * @param userId 给定的用户ID
     * @return 查询到该用户发布的所有商品
     */
    public static ArrayList<Goods> queryGoodsList(String userId) {
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
            preparedStatement = connection.prepareStatement(sqlStatement.toString(),Statement.RETURN_GENERATED_KEYS);
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
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();  //取返回的自增主键的id
            if (generatedKeys.next()){
                int goodsId = generatedKeys.getInt(1);
                goods.setGoodsId(goodsId);
            }
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

    public static int updateGoodsPic(Goods goods,int sequence_number){
        //获得数据库的连接对象
        Connection connection = DBManager.getConnection();
        PreparedStatement preparedStatement = null;

        Goods goods1 = queryGoods(goods.getGoodsId());
        //生成SQL代码
        StringBuilder sqlStatement = new StringBuilder();
        sqlStatement.append("UPDATE goods set picAddress"+sequence_number+ " = ? where goodsId = ?");
        //设置数据库的字段值
        try {
            preparedStatement = connection.prepareStatement(sqlStatement.toString());
            preparedStatement.setString(1, goods.getPicAddress1());
            preparedStatement.setInt(2, goods.getGoodsId());
            return preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(GoodsDAO.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        } finally {
            DBManager.closeAll(connection, preparedStatement,null);
        }
    }
    public static int updatePageViews(int goodsId){
        //获得数据库的连接对象
        Connection connection = DBManager.getConnection();
        PreparedStatement preparedStatement = null;

        //生成SQL代码
        StringBuilder sqlStatement = new StringBuilder();
        sqlStatement.append("UPDATE goods set pageViews = pageViews+'1' where goodsId=?");
        //设置数据库的字段值
        try {
            preparedStatement = connection.prepareStatement(sqlStatement.toString());
            preparedStatement.setInt(1, goodsId);
            return preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(GoodsDAO.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
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
