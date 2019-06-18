package ru.dsoccer1980.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DbUtils {

    public static Connection getConnection(String URL) throws SQLException {
        Connection connection = DriverManager.getConnection(URL);
        connection.setAutoCommit(false);
        return connection;
    }

    public static void createTable(Connection connection, String sql) throws SQLException {
        try (PreparedStatement pst = connection.prepareStatement(sql)) {
            pst.executeUpdate();
        }
    }
}
