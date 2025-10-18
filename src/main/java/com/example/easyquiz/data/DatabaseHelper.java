package com.example.easyquiz.data;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.*;
import java.sql.*;
import java.util.stream.Collectors;

public class DatabaseHelper {
    public static final String DB_FILE = System.getProperty("user.dir") + "/data/easyquiz.db";
    private static final String JDBC_URL = "jdbc:sqlite:" + DB_FILE;

    // gọi khi app khởi động
    public static void initDatabase() {
        try {
            // ensure directory exists
            Path dbPath = Paths.get(DB_FILE).toAbsolutePath();
            Files.createDirectories(dbPath.getParent());

            // open connection (will create file if not exists)
            try (Connection conn = getConnection()) {
                // load schema.sql from resources
                InputStream is = DatabaseHelper.class.getResourceAsStream("/sql/schema.sql");
                if (is == null) {
                    System.err.println("schema.sql not found in resources!");
                    return;
                }
                String sql = new BufferedReader(new InputStreamReader(is))
                        .lines().collect(Collectors.joining("\n"));
                // execute statements (split by ';')
                for (String stmt : sql.split(";")) {
                    String s = stmt.trim();
                    if (!s.isEmpty()) {
                        try (Statement st = conn.createStatement()) {
                            st.execute(s);
                        }
                    }
                }
            }
            System.out.println("Database initialized at: " + dbPath);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to initialize database", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(JDBC_URL);
    }
}
