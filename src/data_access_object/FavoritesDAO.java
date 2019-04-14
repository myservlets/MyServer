package data_access_object;

import Utils.DoFiles;
import configuration_files.Source;
import db_connecter.DBManager;
import entity.FavoritesItem;
import entity.Order;
import entity.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FavoritesDAO {
    /**
     * 查询用户收藏夹中的信息
     *
     * @param userId 给定的用户ID
     * @param sign 0/商品收藏夹 1/招聘收藏夹
     * @return 返回用户收藏夹信息
     */
    public static ArrayList<FavoritesItem> queryGoodsFavor(String userId,int sign) {
        //获得数据库的连接对象
        Connection connection = DBManager.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        //生成SQL代码
        StringBuilder sqlStatement = new StringBuilder();
        sqlStatement.append("SELECT * FROM favorites WHERE userId=? and flag = ?");

        //设置数据库的字段值
        try {
            preparedStatement = connection.prepareStatement(sqlStatement.toString());
            preparedStatement.setString(1, userId);
            preparedStatement.setInt(2, sign);

            resultSet = preparedStatement.executeQuery();
            ArrayList<FavoritesItem> favoritesItems = new ArrayList<>();
            while (resultSet.next()) {
                FavoritesItem favoritesItem = new FavoritesItem();
                favoritesItem.setfId(resultSet.getInt("fid"));
                favoritesItem.setGoods(GoodsDAO.queryGoods(resultSet.getInt("goodsId")));
                favoritesItem.setUserId(userId);
                favoritesItems.add(favoritesItem);
            }
            return favoritesItems;
        } catch (SQLException ex) {
            Logger.getLogger(FavoritesDAO.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } finally {
            DBManager.closeAll(connection, preparedStatement, resultSet);
        }
    }
    /**
     * 添加到收藏夹
     *
     * @param userId 给定的用户ID
     * @param goodsId 给定的商品ID
     * @param recruitmentId 给定的招聘ID
     * @param sign 0/商品收藏夹 1/招聘收藏夹
     * @return 0/添加成功 1/添加失败
     */
    public static int addToFavor(String userId,int goodsId, int sign,int recruitmentId) {
        //获得数据库的连接对象
        Connection connection = DBManager.getConnection();
        PreparedStatement preparedStatement = null;

        //生成SQL代码
        StringBuilder sqlStatement = new StringBuilder();
        sqlStatement.append("INSERT INTO favorites (userId, goodsId, flag, recruitmentId)" +
                " VALUES(?,?,?,?)");

        //设置数据库的字段值
        try {
            preparedStatement = connection.prepareStatement(sqlStatement.toString());
            preparedStatement.setString(1, userId);
            preparedStatement.setInt(2, goodsId);
            preparedStatement.setInt(3, sign);
            preparedStatement.setInt(4,recruitmentId);
            return preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(FavoritesDAO.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        } finally {
            DBManager.closeAll(connection, preparedStatement,null);
        }
    }
    /**
     * 移除收藏夹
     *
     * @param favoritesItem 收藏夹Item
     * @return 0/删除失败 1/删除成功
     */
    public static int deleteFavor(FavoritesItem favoritesItem) {
        //获得数据库的连接对象
        Connection connection = DBManager.getConnection();
        PreparedStatement preparedStatement = null;

        //生成SQL代码
        StringBuilder sqlStatement = new StringBuilder();
        sqlStatement.append("DELETE FROM myapp_schema.favorites WHERE fid=?");
        //设置数据库的字段值
        try {
            preparedStatement = connection.prepareStatement(sqlStatement.toString());
            preparedStatement.setInt(1, favoritesItem.getfId());
            return preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(FavoritesDAO.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        } finally {
            DBManager.closeAll(connection, preparedStatement,null);
        }
    }
}
