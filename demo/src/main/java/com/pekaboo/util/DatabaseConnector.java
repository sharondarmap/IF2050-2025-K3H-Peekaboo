package com.pekaboo.util;
import java.sql.Connection;
import java.sql.DriverManager;
// import java.sql.PreparedStatement;
// import java.sql.ResultSet;
import java.sql.SQLException;


// util/DatabaseConnector.java
public class DatabaseConnector {
    private static final String URL = "jdbc:postgresql://localhost:5432/peekaboo";
    private static final String USER = "postgres";
    private static final String PASS = "";

    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }
}

