package model;


import java.sql.*;
import java.util.ArrayList;

public class DatabaseHandle {

    public static ArrayList<String> browserName() {
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
                String query = "Select name from filename";
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
                    System.out.println("closeDB");
                    return filename;
                }
            }
            catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
        return filename;
    }

    public static String browseData(String fname) {
        Connection conn = null;
        String data = "";
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
                String query = "Select data from filename where name = \""+fname+"\"";
                Statement statement = conn.createStatement();
                ResultSet resultSet = statement.executeQuery(query);
                data = resultSet.getString(1);
                System.out.println("Finish query");
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            try {
                if (conn != null) {
                    conn.close();
                    System.out.println("closeDB");
                    return data;
                }
            }
            catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
        return data;
    }


    public static void updateData(String name, String data) {
        Connection conn = null;
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
                String query = "INSERT INTO filename(name, data) VALUES (?, ?)";
                PreparedStatement pstmt = conn.prepareStatement(query);
                pstmt.setString(1,name);
                pstmt.setString(2,data);
                pstmt.executeUpdate();
                System.out.println("Insert finish");
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        finally {
            try {
                if (conn != null) {
                    conn.close();
                    System.out.println("closeDB");
                }
            }
            catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }


    public static void deleteData(String fname) {
        Connection conn = null;
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
                String query = "delete from filename where name = \""+fname+"\"";
                Statement statement = conn.createStatement();
                statement.execute(query);
                System.out.println("Delete finish");
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        finally {
            try {
                if (conn != null) {
                    conn.close();
                    System.out.println("closeDB");
                }
            }
            catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }
}
