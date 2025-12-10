package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.SQLException;

public class DatabaseInitializer {
    public static void main(String[] args) {
        String dbUrl = "jdbc:h2:./dbRes/circles";
        
        try (Connection conn = DriverManager.getConnection(dbUrl);
             Statement stmt = conn.createStatement()) {
            
            // Create the circles table
            String createTableSQL = "CREATE TABLE IF NOT EXISTS circles (x INT, y INT, r INT, g INT, b INT)";
            stmt.execute(createTableSQL);
            
            System.out.println("Database initialized successfully.");
            System.out.println("Table 'circles' created.");
            
        } catch (SQLException ex) {
            System.err.println("Error initializing database:");
            ex.printStackTrace();
        }
    }
}
