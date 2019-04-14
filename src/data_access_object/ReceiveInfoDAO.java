package data_access_object;

import db_connecter.DBManager;
import entity.ReceiveInfo;
import entity.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ReceiveInfoDAO {
    /**
     * 查询给定用户名的用户的收货信息
     *
     * @param userid 给定的用户名
     * @return ReceiveInfo 查询到的封装了详细信息的ReceiveInfo对象
     */
    public static ArrayList<ReceiveInfo> queryInfo(String userid) {
        //获得数据库的连接对象
        Connection connection = DBManager.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        //生成SQL代码
        StringBuilder sqlStatement = new StringBuilder();
        sqlStatement.append("SELECT * FROM receiveinfo WHERE userid=?");

        ArrayList<ReceiveInfo> receiveInfos = new ArrayList<ReceiveInfo>();
        //设置数据库的字段值
        try {
            preparedStatement = connection.prepareStatement(sqlStatement.toString());
            preparedStatement.setString(1, userid);

            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                ReceiveInfo receiveInfo = new ReceiveInfo();
                receiveInfo.setUserId(resultSet.getString("userId"));
                receiveInfo.setPhone(resultSet.getString("phone"));
                receiveInfo.setAddress(resultSet.getString("address"));
                receiveInfo.setrId(resultSet.getInt("rId"));
                receiveInfo.setIsDefault(resultSet.getInt("isdefault"));
                receiveInfos.add(receiveInfo);
            }
            return receiveInfos;
        } catch (SQLException ex) {
            Logger.getLogger(ReceiveInfoDAO.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } finally {
            DBManager.closeAll(connection, preparedStatement, resultSet);
        }
    }

    public static int setDefault(ReceiveInfo receiveInfo) {
        //获得数据库的连接对象
        Connection connection = DBManager.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        //生成SQL代码
        StringBuilder sqlStatement = new StringBuilder();
        sqlStatement.append("update receiveInfo set isDefault = 1 where rId = ?");

        //设置数据库的字段值
        try {
            preparedStatement = connection.prepareStatement(sqlStatement.toString());
            preparedStatement.setInt(1, receiveInfo.getrId());
            return preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(ReceiveInfoDAO.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        } finally {
            DBManager.closeAll(connection, preparedStatement, resultSet);
        }
    }

    public static ReceiveInfo getDefault(String userId) {
        //获得数据库的连接对象
        Connection connection = DBManager.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        //生成SQL代码
        StringBuilder sqlStatement = new StringBuilder();
        sqlStatement.append("SELECT * FROM receiveinfo WHERE userId=? and isdefault = 1");

        //设置数据库的字段值
        try {
            preparedStatement = connection.prepareStatement(sqlStatement.toString());
            preparedStatement.setString(1, userId);

            resultSet = preparedStatement.executeQuery();
            ReceiveInfo receiveInfo = new ReceiveInfo();
            if (resultSet.next()) {
                receiveInfo.setUserId(userId);
                receiveInfo.setrId(resultSet.getInt("rid"));
                receiveInfo.setAddress(resultSet.getString("address"));
                receiveInfo.setPhone(String.valueOf(resultSet.getInt("phone")));
                receiveInfo.setIsDefault(resultSet.getInt("isDefault"));
                return receiveInfo;
            } else {
                return null;
            }
        } catch (SQLException ex) {
            Logger.getLogger(ReceiveInfoDAO.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } finally {
            DBManager.closeAll(connection, preparedStatement, resultSet);
        }
    }

    public static int insertReceiveInfo(String userId, String phone, String address) {
        //获得数据库的连接对象
        Connection connection = DBManager.getConnection();
        PreparedStatement preparedStatement = null;

        //生成SQL代码
        StringBuilder sqlStatement = new StringBuilder();
        sqlStatement.append("INSERT INTO receiveInfo (userId, phone, address) VALUES(?,?,?)");

        //设置数据库的字段值
        try {
            preparedStatement = connection.prepareStatement(sqlStatement.toString());
            preparedStatement.setString(1, userId);
            preparedStatement.setString(2, phone);
            preparedStatement.setString(3, address);

            return preparedStatement.executeUpdate();

        } catch (SQLException ex) {
            Logger.getLogger(ReceiveInfoDAO.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        } finally {
            DBManager.closeAll(connection, preparedStatement,null);
        }
    }
    public static int deleteReceiveInfo(ReceiveInfo receiveInfo) {
        //获得数据库的连接对象
        Connection connection = DBManager.getConnection();
        PreparedStatement preparedStatement = null;

        //生成SQL代码
        StringBuilder sqlStatement = new StringBuilder();
        sqlStatement.append("DELETE from receiveInfo where rId = ?");

        //设置数据库的字段值
        try {
            preparedStatement = connection.prepareStatement(sqlStatement.toString());
            preparedStatement.setInt(1, receiveInfo.getrId());

            return preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(ReceiveInfoDAO.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        } finally {
            DBManager.closeAll(connection, preparedStatement,null);
        }
    }

    /**
     *
     * @param receiveInfo1 编辑前的收货信息
     * @param receiveInfo2 编辑后的收货信息
     * @return
     */
    public static int editReceiveInfo(ReceiveInfo receiveInfo1,ReceiveInfo receiveInfo2) {
        //获得数据库的连接对象
        Connection connection = DBManager.getConnection();
        PreparedStatement preparedStatement = null;

        //生成SQL代码
        StringBuilder sqlStatement = new StringBuilder();
        sqlStatement.append("UPDATE receiveInfo set userid = ?,phone = ?,address = ? where " +
                "rId = ?");

        //设置数据库的字段值
        try {
            preparedStatement = connection.prepareStatement(sqlStatement.toString());
            preparedStatement.setString(1, receiveInfo2.getUserId());
            preparedStatement.setString(2, receiveInfo2.getPhone());
            preparedStatement.setString(3, receiveInfo2.getAddress());
            preparedStatement.setInt(4, receiveInfo1.getrId());
            return preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(ReceiveInfoDAO.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        } finally {
            DBManager.closeAll(connection, preparedStatement,null);
        }
    }
}
