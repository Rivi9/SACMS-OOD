package com.sacms.sacmsood.Models;

import java.sql.*;

public class mysqlConnector {
    private static Connection connection;

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());
            connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/sacms",
                    "root",
                    "");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace(System.out);
        }
    }

    public static ResultSet execute(String query) {
        try {
            Statement statement = connection.createStatement();
            ResultSet result;
            if (query.startsWith("SELECT")) {
                result = statement.executeQuery(query);
            } else {
                statement.executeUpdate(query);
                result = null;
            }
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Connection getConnection() {
        return connection;
    }

    public void close() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
