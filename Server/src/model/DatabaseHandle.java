package model;

import java.sql.*;
import java.util.ArrayList;

public class DatabaseHandle {

    public static ArrayList<String> browserData() {
        Connection conn = null;
        ArrayList<String> filename = new ArrayList<String>();
        try {
            // db parameters
            String url = "jdbc:sqlite:filename.db";
            // create a connection to the database
            conn = DriverManager.getConnection(url);
            System.out.println("Connection to SQLite has been established.");
            if (conn != null){
                DatabaseMetaData dm = (DatabaseMetaData)conn.getMetaData();
                System.out.println("Driver name: "+ dm.getDriverName());
                System.out.println("Product name: "+dm.getDatabaseProductName());
                System.out.println("--------DATA----------------");
                String query = "Select * from filename";
                Statement statement = conn.createStatement();
                ResultSet resultSet = statement.executeQuery(query);
                while (resultSet.next()) {
                    String name = resultSet.getString(1);
                    filename.add(name);
                    System.out.println("name: "+name);
                }
            }
        }
        catch (SQLException e) {
                e.printStackTrace();
        }
        finally {
            try {
                if (conn != null) {
                    conn.close();
                    System.out.println("close");
                    return filename;
                }
            }
            catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
        return filename;
    }
    public static void updateData(String name) {
        Connection conn = null;
        Statement stmt = null;
        try {
            // db parameters
            String url = "jdbc:sqlite:filename.db";
            // create a connection to the database
            conn = DriverManager.getConnection(url);
            System.out.println("Connection to SQLite has been established.");
            if (conn != null){
                DatabaseMetaData dm = (DatabaseMetaData)conn.getMetaData();
                System.out.println("Driver name: "+ dm.getDriverName());
                System.out.println("Product name: "+dm.getDatabaseProductName());
                System.out.println("Insert :"+name);
                String query = "Insert into filename values ("+name+")";
                stmt = conn.createStatement();
                stmt.executeUpdate(query);
            }
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
