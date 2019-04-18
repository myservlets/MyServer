package data_access_object;

import utils.DoFiles;
import configuration_files.Source;
import db_connecter.DBManager;
import entity.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;


public class UserDAO {
    /**
     * 查询给定用户名的用户的详细信息
     *
     * @param userId 给定的用户ID
     * @return 查询到的封装了详细信息的User对象
     */
    public static User queryUser(String userId) {
        //获得数据库的连接对象
        Connection connection = DBManager.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        //生成SQL代码
        StringBuilder sqlStatement = new StringBuilder();
        sqlStatement.append("SELECT * FROM users WHERE userId=?");

        //设置数据库的字段值
        try {
            preparedStatement = connection.prepareStatement(sqlStatement.toString());
            preparedStatement.setString(1, userId);

            resultSet = preparedStatement.executeQuery();
            User user = new User();
            if (resultSet.next()) {
                user.setNickname(resultSet.getString("nickName"));
                user.setPassword(resultSet.getString("password"));
                user.setUserId(resultSet.getString("userId"));
                user.setIcon(resultSet.getString("icon"));
                return user;
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

    public static ArrayList<User> fuzzySearchUser(String keyword){
        //获得数据库的连接对象
        Connection connection = DBManager.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        ArrayList<User> users = new ArrayList<>();

        //生成SQL代码
        StringBuilder sqlStatement = new StringBuilder();
        sqlStatement.append("SELECT * FROM users WHERE LOCATE(?, concat(nickname,userId))>0");

        //设置数据库的字段值
        try {
            preparedStatement = connection.prepareStatement(sqlStatement.toString());
            preparedStatement.setString(1, keyword);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                User user = new User();
                user.setNickname(resultSet.getString("nickName"));
                user.setUserId(resultSet.getString("userId"));
                user.setIcon(resultSet.getString("icon"));
                users.add(user);
            }
            return users;
        } catch (SQLException ex) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } finally {
            DBManager.closeAll(connection, preparedStatement, resultSet);
        }
    }

    public static User insertUser(String userName,String password, String userId) {
        //获得数据库的连接对象
        Connection connection = DBManager.getConnection();
        PreparedStatement preparedStatement = null;

        //生成SQL代码
        StringBuilder sqlStatement = new StringBuilder();
        sqlStatement.append("INSERT INTO users VALUES(?,?,?)");

        //设置数据库的字段值
        try {
            preparedStatement = connection.prepareStatement(sqlStatement.toString());
            preparedStatement.setString(1, userName);
            preparedStatement.setString(2, password);
            preparedStatement.setString(3, userId);

            int i = preparedStatement.executeUpdate();
            User user = new User();
            if (i==1) {
                user.setNickname(userName);
                user.setPassword(password);
                user.setUserId(userId);
                return user;
            } else {
                return null;
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } finally {
            DBManager.closeAll(connection, preparedStatement,null);
        }
    }



    public static int updateUser(int sign,String s,User user){
        //获得数据库的连接对象
        Connection connection = DBManager.getConnection();
        PreparedStatement preparedStatement = null;
        String[] strings = new String[3];
        strings[0]="nickname";
        strings[1]="password";
        strings[2]="icon";

        //生成SQL代码
        StringBuilder sqlStatement = new StringBuilder();
        sqlStatement.append("UPDATE users set "+ strings[sign] +" = ? where " +
                "userid = ?");

        //设置数据库的字段值
        try {
            preparedStatement = connection.prepareStatement(sqlStatement.toString());
            preparedStatement.setString(1, s);
            preparedStatement.setString(2, user.getUserId());
            return preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        } finally {
            DBManager.closeAll(connection, preparedStatement,null);
        }
    }

    public static int updateIcon(String userId,String iconAddress){
        User user = queryUser(userId);
        //获得数据库的连接对象
        Connection connection = DBManager.getConnection();
        PreparedStatement preparedStatement = null;

        //生成SQL代码
        StringBuilder sqlStatement = new StringBuilder();
        sqlStatement.append("UPDATE users set icon = ? where " +
                "userid = ?");
        //设置数据库的字段值
        try {
            preparedStatement = connection.prepareStatement(sqlStatement.toString());
            preparedStatement.setString(1, iconAddress);
            preparedStatement.setString(2, userId);
            DoFiles.deleteFile(Source.iconSource + userId + "/" +user.getIcon());
            return preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        } finally {
            DBManager.closeAll(connection, preparedStatement,null);
        }
    }
}
