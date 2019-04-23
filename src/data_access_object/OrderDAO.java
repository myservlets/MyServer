package data_access_object;

import db_connecter.DBManager;
import entity.Goods;
import entity.Order;
import entity.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OrderDAO {
    /**
     * 查询给定用户名的用户的详细信息
     *
     * @param userId 给定的用户ID
     * @return 查询到的封装了详细信息的User对象
     */
    public static ArrayList<Order> queryOrder(String userId, int status) {
        //获得数据库的连接对象
        Connection connection = DBManager.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        //生成SQL代码
        StringBuilder sqlStatement = new StringBuilder();
        sqlStatement.append("SELECT * FROM `myapp_schema`.`order` WHERE userId=? and status=?");

        //设置数据库的字段值
        try {
            preparedStatement = connection.prepareStatement(sqlStatement.toString());
            preparedStatement.setString(1,userId);
            preparedStatement.setInt(2,status);

            resultSet = preparedStatement.executeQuery();
            ArrayList<Order> orders = new ArrayList<>();
            while (resultSet.next()) {
                Order order = new Order();
                order.setCost(resultSet.getDouble("cost"));
                order.setCount(resultSet.getInt("count"));
                order.setDate(resultSet.getDate("date"));
                order.setGoodsId(resultSet.getInt("goodsId"));
                order.setGoods(GoodsDAO.queryGoods(order.getGoodsId()));
                order.setRemark(resultSet.getString("remark"));
                order.setStatus(resultSet.getInt("status"));
                order.setUserId(resultSet.getString("userId"));
                order.setOrderId(resultSet.getInt("orderId"));
                orders.add(order);
            }
            return orders;
        } catch (SQLException ex) {
            Logger.getLogger(OrderDAO.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } finally {
            DBManager.closeAll(connection, preparedStatement, resultSet);
        }
    }

    public static Order addOrder(Order order) {
        //获得数据库的连接对象
        Connection connection = DBManager.getConnection();
        PreparedStatement preparedStatement = null;

        //生成SQL代码
        StringBuilder sqlStatement = new StringBuilder();
        sqlStatement.append("INSERT INTO `myapp_schema`.`order` (`userId`, `status`, `count`, `cost`, " +
                "`date`, `remark`, `goodsId`) VALUES (?, ?, ?, ?, ?, ?, ?)");

        //设置数据库的字段值
        try {
            preparedStatement = connection.prepareStatement(sqlStatement.toString());
            preparedStatement.setString(1, order.getUserId());
            preparedStatement.setInt(2, order.getStatus());
            preparedStatement.setInt(3, order.getCount());
            preparedStatement.setDouble(4, order.getCost());
            preparedStatement.setDate(5, (Date) order.getDate());
            preparedStatement.setString(6, order.getRemark());
            preparedStatement.setInt(7, order.getGoodsId());

            int i = preparedStatement.executeUpdate();
            if (i==1) {
                return order;
            } else {
                return null;
            }
        } catch (SQLException ex) {
            Logger.getLogger(OrderDAO.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } finally {
            DBManager.closeAll(connection, preparedStatement,null);
        }
    }

    public static int deleteOrder(Order order) {
        //获得数据库的连接对象
        Connection connection = DBManager.getConnection();
        PreparedStatement preparedStatement = null;

        //生成SQL代码
        StringBuilder sqlStatement = new StringBuilder();
        sqlStatement.append("DELETE FROM myapp_schema.order WHERE orderId=?");
        //设置数据库的字段值
        try {
            preparedStatement = connection.prepareStatement(sqlStatement.toString());
            preparedStatement.setInt(1, order.getOrderId());
            return preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(OrderDAO.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        } finally {
            DBManager.closeAll(connection, preparedStatement,null);
        }
    }

    public static int updateOrder(Order order){
        //获得数据库的连接对象
        Connection connection = DBManager.getConnection();
        PreparedStatement preparedStatement = null;

        //生成SQL代码
        StringBuilder sqlStatement = new StringBuilder();
        sqlStatement.append("UPDATE `myapp_schema`.`order` set status = "+ order.getStatus() +
                ",count = "+order.getCount()+
                ",cost = "+order.getCost()+
                ",remark = "+order.getRemark()+
                ",goodsId = "+order.getGoodsId()+
                " where orderId =?");
        //设置数据库的字段值
        try {
            preparedStatement = connection.prepareStatement(sqlStatement.toString());
            preparedStatement.setInt(1, order.getOrderId());
            return preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(OrderDAO.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        } finally {
            DBManager.closeAll(connection, preparedStatement,null);
        }
    }
}
