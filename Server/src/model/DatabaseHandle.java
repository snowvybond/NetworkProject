package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseHandle {

    public static void connect() {
        Connection conn = null;
        try {
            // db parameters
            String url = "jdbc:sqlite:filename.db";
            // create a connection to the database
            conn = DriverManager.getConnection(url);
            System.out.println("Connection to SQLite has been established.");
        }
        catch (SQLException e) {
                e.printStackTrace();
        }
        finally {
            try {
                if (conn != null) {
                    conn.close();
                    System.out.println("close");
                }
            }
            catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }
}
