package com.example.emoji.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/*
*  远程数据库连接工具类
* */
public class DBConnection {
    private static final String URL = "jdbc:mysql://greal.cc:3306/emoji?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "carbongo410";

    public static Connection getConnection() {
        Connection connection = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }
}
