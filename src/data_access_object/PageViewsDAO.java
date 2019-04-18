package data_access_object;

import db_connecter.DBManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PageViewsDAO {
    public static int queryGoodsPageViews(String userId,int goodsId){
        Connection connection = DBManager.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;


        StringBuilder sqlStatement = new StringBuilder();
        sqlStatement.append("select * from pageViews where userId=? and goodsId=?");
        try{
            preparedStatement = connection.prepareStatement(sqlStatement.toString());
            preparedStatement.setString(1,userId);
            preparedStatement.setInt(2,goodsId);
            resultSet = preparedStatement.executeQuery();
            if(resultSet.next())
                return 1;
            else
                return 0;
        } catch (SQLException e) {
            Logger.getLogger(PageViewsDAO.class.getName()).log(Level.SEVERE, null, e);
            return 0;
        }finally {
            DBManager.closeAll(connection, preparedStatement,null);
        }
    }
    public static int addGoodsPageViews(String userId,int goodsId){
        Connection connection = DBManager.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        if(queryGoodsPageViews(userId,goodsId)!=1)
            return 0;

        StringBuilder sqlStatement = new StringBuilder();
        sqlStatement.append("insert into pageViews(userid,goodsId,type)" +
                "values (?,?,?)");
        try{
            preparedStatement = connection.prepareStatement(sqlStatement.toString());
            preparedStatement.setString(1,userId);
            preparedStatement.setInt(2,goodsId);
            preparedStatement.setInt(3,1);
            return preparedStatement.executeUpdate();
        } catch (SQLException e) {
            Logger.getLogger(PageViewsDAO.class.getName()).log(Level.SEVERE, null, e);
            return -1;
        }finally {
            DBManager.closeAll(connection, preparedStatement,null);
        }
    }
}
