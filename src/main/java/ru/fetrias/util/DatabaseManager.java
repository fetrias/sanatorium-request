package ru.fetrias.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager {

    public static Connection getConnection() throws SQLException {
        String password = "1234";
        String user = "postgres";
        String url = "jdbc:postgresql://localhost:5432/sanatorium_request?currentSchema=sanatorium";
        return DriverManager.getConnection(url, user, password);
    }
}
