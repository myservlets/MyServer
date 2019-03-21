package data_access_object;

import db_connecter.DBManager;
import entity.FriendShip;
import entity.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FriendDAO {
    public static int addFriend(String user0id,String user1id,int status){
        //获得数据库的连接对象
        Connection connection = DBManager.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        //生成SQL代码
        StringBuilder sqlStatement = new StringBuilder();
        sqlStatement.append("INSERT INTO friend VALUES(?,?,?)");
        try {
            preparedStatement = connection.prepareStatement(sqlStatement.toString());
            preparedStatement.setString(1, user0id);
            preparedStatement.setString(2, user1id);
            preparedStatement.setInt(3, status);

            int i = preparedStatement.executeUpdate();
            if (i==1) {
                return 0;
            } else {
                return 2;
            }
        } catch (SQLException ex) {
            Logger.getLogger(FriendDAO.class.getName()).log(Level.SEVERE, null, ex);
            return 2;
        } finally {
            DBManager.closeAll(connection, preparedStatement,null);
        }
    }

    public static int updateFriendStatus(String user0id,String user1id,int status){
        //获得数据库的连接对象
        Connection connection = DBManager.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        //生成SQL代码
        StringBuilder sqlStatement = new StringBuilder();
        String sqlstr = "UPDATE friend SET status=? WHERE uid=? and cid=?";
        sqlStatement.append(sqlstr);
        try {
            preparedStatement = connection.prepareStatement(sqlStatement.toString());
            preparedStatement.setInt(1, status);
            preparedStatement.setString(2, user0id);
            preparedStatement.setString(3, user1id);


            int i = preparedStatement.executeUpdate();
            if (i==1) {
                return status+3;
            } else {
                return 2;
            }
        } catch (SQLException ex) {
            Logger.getLogger(FriendDAO.class.getName()).log(Level.SEVERE, null, ex);
            return 2;
        } finally {
            DBManager.closeAll(connection, preparedStatement,null);
        }
    }

    public static ArrayList queryFriend(String user0id,String user1id,int status){
        Connection connection = DBManager.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        ArrayList<FriendShip> ships = new ArrayList<>();
        try {
            String x ="SELECT * FROM friend WHERE uid = ? and cid = ? and status =?";
            if(user0id.equals("*")){
                x="SELECT * FROM friend WHERE cid = ? and status =?";
                preparedStatement = connection.prepareStatement(x);
                preparedStatement.setString(1, user1id);
                preparedStatement.setInt(2,status);
            }
            else if(user1id.equals("*")){
                x="SELECT * FROM friend WHERE uid = ? and status =?";
                preparedStatement = connection.prepareStatement(x);
                preparedStatement.setString(1, user0id);
                preparedStatement.setInt(2,status);
            }else {
                preparedStatement = connection.prepareStatement(x);
                preparedStatement.setString(1, user0id);
                preparedStatement.setString(2, user1id);
                preparedStatement.setInt(3,status);
            }
            resultSet= preparedStatement.executeQuery();

            while (resultSet.next()){
                FriendShip fs = new FriendShip();
                fs.setUid(resultSet.getString("uid"));
                fs.setCid(resultSet.getString("cid"));
                fs.setStatus(resultSet.getInt("status"));
                ships.add(fs);
            }
            return ships;
        }catch (SQLException ex) {
            Logger.getLogger(FriendDAO.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } finally {
            DBManager.closeAll(connection, preparedStatement,resultSet);
        }
    }
}
