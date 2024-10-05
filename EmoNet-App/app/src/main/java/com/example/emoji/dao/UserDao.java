package com.example.emoji.dao;

import com.example.emoji.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.example.emoji.util.DBConnection;

/*
*   用户数据处理类：存储在远程数据库之中，主要用于验证用户的身份信息
*   1. 添加用户
*   2. 查找用户
* */
public class UserDao {

    //注册-添加用户
    // 添加用户
    public void addUser(User user) {
        boolean result = false;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {

            connection = DBConnection.getConnection();
            String sql = "INSERT INTO user (username, password, phone, identity) VALUES (?, ?, ?, ?)";
            preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, user.getPhone());
            preparedStatement.setInt(4, user.getIdentity());

            int rowsInserted = preparedStatement.executeUpdate();
            result = rowsInserted > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                closeResources(connection, preparedStatement, resultSet);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }
    //根据username查看个人信息
    public User getUserByUsername(String username) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        User user = null;
        try {
            connection = DBConnection.getConnection();
            String sql = "SELECT * FROM user WHERE username = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int uid = resultSet.getInt("uid");
                String password = resultSet.getString("password");
                String phone = resultSet.getString("phone");
                int identity = resultSet.getInt("identity");
                user = new User(username, password, phone, identity);
                user.setUid(uid);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }finally {
            try {
                closeResources(connection, preparedStatement, null);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return user;
    }
    // 辅助方法：关闭资源
    private void closeResources(Connection connection, PreparedStatement preparedStatement, ResultSet resultSet) throws SQLException {

        if (resultSet != null) resultSet.close();
        if (preparedStatement != null) preparedStatement.close();
        if (connection != null) connection.close();
    }

}
