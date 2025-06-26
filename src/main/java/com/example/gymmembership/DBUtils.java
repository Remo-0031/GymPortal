package com.example.gymmembership;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtils {
    private static final String URL = "jdbc:mysql://localhost:3306/gym";
    private static final String user = "root";
    private static final String pass = "";

    public static Connection connect() throws SQLException{
        return DriverManager.getConnection(URL,user,pass);
    }
}
