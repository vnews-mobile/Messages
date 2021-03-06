package com.mobile.vnews.dao;

import com.mobile.vnews.module.Message;
import com.mobile.vnews.util.PropertiesUtils;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Create by xuantang
 * @date on 12/8/17
 */
public class Dao {
    /**
     *
     * @param newsID
     * @return
     */
    public static List<String> getRelationUsersOnNews(int newsID) {
        if (newsID < 0) {
            throw new IllegalArgumentException();
        }
        List<String> users = new ArrayList<>();
        // get connection
        Connection conn = getConnection();

        String sql = "(SELECT fromID FROM notice " +
                "WHERE newsID = ?) " +
                "UNION " +
                "(SELECT toID FROM notice " +
                "WHERE newsID = ?)";
        PreparedStatement preparedStatement;
        try {
            preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setInt(1, newsID);
            preparedStatement.setInt(2, newsID);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                users.add(rs.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }


    public static void addComment(Message message) {
        // get connection
        Connection conn = getConnection();
        // sql
        String sql = "insert into comment(newsID, fromID, toID, content, timestamp, floor) " +
                "values(?, ?, ?, ?, ?, ?)";
        PreparedStatement preparedStatement;
        try {
            // insert to mysql
            preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, message.getNewsID());
            preparedStatement.setString(2, message.getFromID());
            preparedStatement.setString(3, message.getToID());
            preparedStatement.setString(4, message.getContent());
            preparedStatement.setLong(5, System.currentTimeMillis());
            preparedStatement.setString(6, message.getFloor());
            preparedStatement.executeUpdate();
            preparedStatement.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param message
     */
    public static void addNotice(Message message) {
        // get connection
        Connection conn = getConnection();
        /**
         * newsID
         * fromID    VARCHAR(20)                         NULL,
         * toID      VARCHAR(20)                         NULL,
         * content   TEXT                                NOT NULL,
         * timestamp
         */
        // sql
        String sql = "insert into notice(newsID, fromID, toID, content, timestamp) " +
                "values(?, ?, ?, ?, ?)";
        PreparedStatement preparedStatement;
        try {
            // insert to mysql
            preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, message.getNewsID());
            preparedStatement.setString(2, message.getFromID());
            preparedStatement.setString(3, message.getToID());
            preparedStatement.setString(4, message.getContent());
            preparedStatement.setLong(5, System.currentTimeMillis());
            preparedStatement.executeUpdate();
            preparedStatement.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Get connection
     * @return
     */
    private static Connection getConnection() {
        Connection conn = null;
        String driver = "com.mysql.jdbc.Driver";
        String url = "jdbc:mysql://118.89.111.157:3306/vnews?useUnicode=true&characterEncoding=utf8&rewriteBatchedStatements=true";
        String username = "vnews";
        String password = "vnews";
        try {
            Class.forName(driver);
            conn = DriverManager.getConnection(url, username, password);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }
}
