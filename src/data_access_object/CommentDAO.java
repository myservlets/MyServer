package data_access_object;

import db_connecter.DBManager;
import entity.Comment;
import entity.CommentItem;
import entity.Order;
import entity.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CommentDAO {
    /**
     * 查询给定用户名的用户的详细信息
     *
     * @param goodsId 给定的商品ID
     * @return 该商品的评论列表
     */
    public static ArrayList<CommentItem> queryComment(int goodsId) {
        //获得数据库的连接对象
        Connection connection = DBManager.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        //生成SQL代码
        StringBuilder sqlStatement = new StringBuilder();
        sqlStatement.append("SELECT * FROM `myapp_schema`.`comment` WHERE goodsId=?" +
                " Order BY 'Date' DESC;");//

        //设置数据库的字段值
        try {
            preparedStatement = connection.prepareStatement(sqlStatement.toString());
            preparedStatement.setInt(1,goodsId);

            resultSet = preparedStatement.executeQuery();
            ArrayList<CommentItem> commentItems = new ArrayList<>();
            while (resultSet.next()) {
                Comment comment = new Comment();
                comment.setContent(resultSet.getString("content"));
                comment.setDate(resultSet.getDate("date"));
                comment.setUserId(resultSet.getString("userId"));
                comment.setGoodsId(resultSet.getInt("goodsId"));
                comment.setScore(resultSet.getInt("score"));
                User user = UserDAO.queryUser(comment.getUserId());
                CommentItem commentItem = new CommentItem();
                commentItem.setComment(comment);
                commentItem.setUser(user);
                commentItems.add(commentItem);
            }
            return commentItems;
        } catch (SQLException ex) {
            Logger.getLogger(CommentDAO.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } finally {
            DBManager.closeAll(connection, preparedStatement, resultSet);
        }
    }

    public static ArrayList<Comment> queryCommentFromUser(String userId) {
        //获得数据库的连接对象
        Connection connection = DBManager.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        //生成SQL代码
        StringBuilder sqlStatement = new StringBuilder();
        sqlStatement.append("SELECT * FROM `myapp_schema`.`comment` WHERE userId=?");

        //设置数据库的字段值
        try {
            preparedStatement = connection.prepareStatement(sqlStatement.toString());
            preparedStatement.setString(1,userId);

            resultSet = preparedStatement.executeQuery();
            ArrayList<Comment> comments = new ArrayList<>();
            while (resultSet.next()) {
                Comment comment = new Comment();
                comment.setContent(resultSet.getString("content"));
                comment.setDate(resultSet.getDate("date"));
                comment.setUserId(resultSet.getString("userId"));
                comment.setGoodsId(resultSet.getInt("goodsId"));
                comment.setScore(resultSet.getInt("score"));
                comments.add(comment);
            }
            return comments;
        } catch (SQLException ex) {
            Logger.getLogger(CommentDAO.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } finally {
            DBManager.closeAll(connection, preparedStatement, resultSet);
        }
    }

    public static Comment addComment(Comment comment) {
        //获得数据库的连接对象
        Connection connection = DBManager.getConnection();
        PreparedStatement preparedStatement = null;

        //生成SQL代码
        StringBuilder sqlStatement = new StringBuilder();
        sqlStatement.append("INSERT INTO `myapp_schema`.`comment` (`userId`, `content`, `Date`, `score`," +
                " `goodsId`) VALUES (?, ?, ?, ?, ?)");

        //设置数据库的字段值
        try {
            preparedStatement = connection.prepareStatement(sqlStatement.toString());
            preparedStatement.setString(1, comment.getUserId());
            preparedStatement.setString(2, comment.getContent());
            preparedStatement.setDate(3, (Date) comment.getDate());
            preparedStatement.setInt(4, comment.getScore());
            preparedStatement.setInt(5,  comment.getGoodsId());
            int i = preparedStatement.executeUpdate();
            if (i==1) {
                return comment;
            } else {
                return null;
            }
        } catch (SQLException ex) {
            Logger.getLogger(CommentDAO.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } finally {
            DBManager.closeAll(connection, preparedStatement,null);
        }
    }
}
