/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.app.model;

import com.app.utility.ConnectionManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * static object to reflect the "current state" of the bidding. (Whether round 1 or 2)
 *  
 */
public class Round {
    
    // 0 when Bidding is CLOSED (after round 1). 1 for Round 1. 2 for Round 2.
    //-1 for CLOSED (after round 2 ended, no more future bidding rounds)
    private static int round;   
    
    //static variables last as long as the application exists and exists "independent" of the class it is declared in
    //therefore it works like a context. Must confirm.
    //EVEN IF Tomcat garbage collects the static variable. Upon calling the Round obj again, 
    //the static initializer block below will immediate SYNC with the round_num in the database
    
    //Note: static variables/static blocks are only initialized when the class is first called. E.g. when Round is called  
    //IMMEDIATELY INITIALIZED
    static {
        Connection conn = null; 
        PreparedStatement pstmt = null; 
        ResultSet rs = null;
        
        try{ 
            conn = ConnectionManager.getConnection(); 
            pstmt = conn.prepareStatement("SELECT * FROM round"); 

            rs = pstmt.executeQuery(); 
            
            rs.next();
            round = rs.getInt("round_num");
              
        }catch(SQLException e){ 
            e.printStackTrace(); 
        }finally{ 
            ConnectionManager.close(conn, pstmt, rs); 
        }
    }
    /**
     * Get the current bidding round
     * @return current round 
     */
    public static int getRound() {
        Connection conn = null; 
        PreparedStatement pstmt = null; 
        ResultSet rs = null;
        
        try{ 
            conn = ConnectionManager.getConnection(); 
            pstmt = conn.prepareStatement("SELECT * FROM round"); 

            rs = pstmt.executeQuery(); 
            
            rs.next();
            round = rs.getInt("round_num");
              
        }catch(SQLException e){ 
            e.printStackTrace(); 
        }finally{ 
            ConnectionManager.close(conn, pstmt, rs); 
        }
        
        return round;
    }

    /**
     * Set the round number in database
     * @param round 
     */
    public static void setRound(int round) {        
        //Record state change in the DB
        Connection conn = null; 
        PreparedStatement pstmt = null; 
        ResultSet rs = null;
        
        try{ 
            conn = ConnectionManager.getConnection(); 
            String sqlStmt = "UPDATE round SET round_num=?"; 
              
            pstmt = conn.prepareStatement(sqlStmt); 
            pstmt.setInt(1,round); 
              
            pstmt.executeUpdate();
            
            //set in the static variable
            Round.round = round;
              
        }catch(SQLException e){ 
            e.printStackTrace(); 
        }finally{ 
            ConnectionManager.close(conn, pstmt, rs); 
        }
        
    }
    
    
}
