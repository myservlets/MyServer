package data_access_object;

import db_connecter.DBManager;
import entity.Recruitment;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RecruitmentDAO {
    public static Recruitment insertRecru(Recruitment recruitment){
        //获得数据库的连接对象
        Connection connection = DBManager.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;


        //生成SQL代码
        StringBuilder sqlStatement = new StringBuilder();
        sqlStatement.append("INSERT INTO recruitment VALUES(?,?,?,?,?,?,?,?,?)");

        try{
            preparedStatement = connection.prepareStatement(sqlStatement.toString());
            preparedStatement.setString(1, recruitment.getRecruId());
            preparedStatement.setString(2, recruitment.getRecruName());
            preparedStatement.setString(3, recruitment.getRecruDescribe());
            preparedStatement.setDouble(4,recruitment.getReward());
            preparedStatement.setInt(5,recruitment.getType());
            preparedStatement.setInt(6,recruitment.getRequestNum());
            preparedStatement.setString(7,recruitment.getAddress());
            preparedStatement.setString(8,recruitment.getContactInfo());
            preparedStatement.setString(9,recruitment.getUserId());

            preparedStatement.execute();
            return recruitment;

        }
        catch (SQLException ex) {
            Logger.getLogger(RecruitmentDAO.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } finally {
            DBManager.closeAll(connection, preparedStatement, resultSet);
        }
    }

    public static Recruitment updateRecru(Recruitment recruitment){
        //获得数据库的连接对象
        Connection connection = DBManager.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;


        //生成SQL代码
        StringBuilder sqlStatement = new StringBuilder();
        sqlStatement.append("UPDATE recruitment SET " +
                "recruName=?," +
                "recruDescribe=?," +
                "reward=?," +
                "type=?," +
                "requestNum=?," +
                "address=?," +
                "contactInfo=?," +
                "userId=? " +
                "WHERE recruId = ?");

        try{
            preparedStatement = connection.prepareStatement(sqlStatement.toString());
            preparedStatement.setString(9, recruitment.getRecruId());

            preparedStatement.setString(1, recruitment.getRecruName());
            preparedStatement.setString(2, recruitment.getRecruDescribe());
            preparedStatement.setDouble(3,recruitment.getReward());
            preparedStatement.setInt(4,recruitment.getType());
            preparedStatement.setInt(5,recruitment.getRequestNum());
            preparedStatement.setString(6,recruitment.getAddress());
            preparedStatement.setString(7,recruitment.getContactInfo());
            preparedStatement.setString(8,recruitment.getUserId());

            if(preparedStatement.executeUpdate()==1){
                return recruitment;
            }
        }
        catch (SQLException ex) {
            Logger.getLogger(RecruitmentDAO.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } finally {
            DBManager.closeAll(connection, preparedStatement, resultSet);
        }
        return null;
    }

    public static Recruitment deleteRecru(Recruitment recruitment){
        //获得数据库的连接对象
        Connection connection = DBManager.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;


        //生成SQL代码
        StringBuilder sqlStatement = new StringBuilder();
        sqlStatement.append("DELETE FROM recruitment WHERE recruId = ?)");

        try{
            preparedStatement = connection.prepareStatement(sqlStatement.toString());
            preparedStatement.setString(1, recruitment.getRecruId());

            if(preparedStatement.execute()){
                return recruitment;
            }
        }
        catch (SQLException ex) {
            Logger.getLogger(RecruitmentDAO.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } finally {
            DBManager.closeAll(connection, preparedStatement, resultSet);
        }
        return null;
    }

    //TODO:
    public static Recruitment queryRecru(Recruitment recruitment){
        //获得数据库的连接对象
        Connection connection = DBManager.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;


        //生成SQL代码
        StringBuilder sqlStatement = new StringBuilder();
        sqlStatement.append("DELETE FROM recruitment WHERE recruId = ?)");

        try{
            preparedStatement = connection.prepareStatement(sqlStatement.toString());
            preparedStatement.setString(1, recruitment.getRecruId());

            if(preparedStatement.execute()){
                return recruitment;
            }
        }
        catch (SQLException ex) {
            Logger.getLogger(RecruitmentDAO.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } finally {
            DBManager.closeAll(connection, preparedStatement, resultSet);
        }
        return null;
    }

    public static ArrayList<Recruitment> fuzzySearchRecru(String keyword){
        //获得数据库的连接对象
        Connection connection = DBManager.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        ArrayList<Recruitment> recruList = new ArrayList<>();

        //生成SQL代码
        StringBuilder sqlStatement = new StringBuilder();
        sqlStatement.append("SELECT * FROM recruitment WHERE LOCATE(?, concat(recruName,recruDescribe,address))>0");

        try{
            preparedStatement = connection.prepareStatement(sqlStatement.toString());
            preparedStatement.setString(1, keyword);

            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                Recruitment r= new Recruitment();
                r.setRecruId(resultSet.getString(1));
                r.setRecruName(resultSet.getString(2));
                r.setRecruDescribe(resultSet.getString(3));
                r.setReward(resultSet.getDouble(4));
                r.setType(resultSet.getInt(5));
                r.setRequestNum(resultSet.getInt(6));
                r.setAddress(resultSet.getString(7));
                r.setContactInfo(resultSet.getString(8));
                r.setUserId(resultSet.getString(9));
                recruList.add(r);
            }
            return recruList;
        }
        catch (SQLException ex) {
            Logger.getLogger(RecruitmentDAO.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } finally {
            DBManager.closeAll(connection, preparedStatement, resultSet);
        }
    }
}
