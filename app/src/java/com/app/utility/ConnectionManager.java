package com.app.utility;
import java.sql.*;

/**
 * A class that manages connections to the database. It also
 * has a utility method that close connections, statements and
 * resultsets
 */
public class ConnectionManager {
    private static String JDBC_DRIVER;
    private static String JDBC_URL;
    private static String JDBC_USER;
    private static String JDBC_PASSWORD;
    
    static {
        JDBC_DRIVER = PropertiesUtility.getProperty("jdbc.driver");
        JDBC_URL = PropertiesUtility.getProperty("jdbc.url");
        JDBC_USER = PropertiesUtility.getProperty("jdbc.user");
        JDBC_PASSWORD = PropertiesUtility.getProperty("jdbc.password");
        
        //INITIALIZE DRIVER INTO TOMCAT, DriverManager will automatically search for it.
        try {
            Class.forName(JDBC_DRIVER).newInstance();  
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Gets a connection to the database
     *
     * @return the connection
     * @throws SQLException if an error occurs when connecting
     */
    public static Connection getConnection() {
        Connection conn = null;
        
        try {
            conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
        } catch (SQLException e) {
            //Log it here
        }
        
        return conn;
    }
	
    /**
     * close the given connection, statement and resultset
     *
     * @param conn the connection object to be closed
     * @param stmt the statement object to be closed
     * @param rs   the resultset object to be closed
     */
    public static void close(Connection conn, Statement stmt, ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (stmt != null) {
                stmt.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}