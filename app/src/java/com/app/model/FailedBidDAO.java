/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.app.model;

import com.app.utility.ConnectionManager;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
 
/**
 * DAO for failed_bid table in database
 * @author Zachery
 */
public class FailedBidDAO {
    //retrieve list of all bids 
    public static ArrayList<Bid> retrieveAll(){ 
        ArrayList<Bid> result = new ArrayList<Bid>(); 
        Connection conn = null; 
        PreparedStatement pstmt = null; 
        ResultSet rs = null; 
        try{ 
            conn = ConnectionManager.getConnection(); 
            pstmt = conn.prepareStatement("SELECT * FROM failed_bid"); 
            rs = pstmt.executeQuery(); 
            while (rs.next()) { 
                String userId = rs.getString("user_id"); 
                BigDecimal amount = rs.getBigDecimal("amount"); 
                String course = rs.getString("course"); 
                String section = rs.getString("section"); 
                  
                result.add(new Bid(userId, amount, course, section)); 
            } 
        }catch(SQLException e){ 
            e.printStackTrace(); 
        }finally{ 
            ConnectionManager.close(conn, pstmt, rs); 
        } 
          
        return result; 
    } 
    
    /**
     * retrieves the bids in order of code (ASC), section (ASC), amount (DESC)
     * @return 
     *      an ArrayList<Bid> of Bids with code (ASC) sorted first, section (ASC) sorted second and amount (DESC) sorted third
     */
    public static ArrayList<Bid> retrieveAllInOrder(){ 
        ArrayList<Bid> result = new ArrayList<Bid>(); 
        Connection conn = null; 
        PreparedStatement pstmt = null; 
        ResultSet rs = null; 
        try{ 
            conn = ConnectionManager.getConnection(); 
            pstmt = conn.prepareStatement("SELECT * FROM section_student ORDER BY course, section, amount DESC"); 
            rs = pstmt.executeQuery(); 
            while (rs.next()) { 
                String userId = rs.getString("user_id"); 
                BigDecimal amount = rs.getBigDecimal("amount"); 
                String course = rs.getString("course"); 
                String section = rs.getString("section"); 
                  
                result.add(new Bid(userId, amount, course, section)); 
            } 
        }catch(SQLException e){ 
            e.printStackTrace(); 
        }finally{ 
            ConnectionManager.close(conn, pstmt, rs); 
        } 
          
        return result; 
    }
      
    //retrieve list of bids by a particular user 
    /**
     * retrieve list of bids by a particular user 
     * @param uId
     * @return List of unsuccessful bid of the user
     */
    public static ArrayList<Bid> retrieveByUserId(String uId){ 
        ArrayList<Bid> result = new ArrayList<Bid>(); 
        Connection conn = null; 
        PreparedStatement pstmt = null; 
        ResultSet rs = null; 
          
        try{ 
            conn = ConnectionManager.getConnection(); 
            pstmt = conn.prepareStatement("SELECT * FROM failed_bid WHERE user_id=?"); 
            pstmt.setString(1,uId); 
            rs = pstmt.executeQuery(); 
              
            while(rs.next()){ 
                String userId = rs.getString("user_id"); 
                BigDecimal amount = rs.getBigDecimal("amount"); 
                String course = rs.getString("course"); 
                String section = rs.getString("section"); 
                  
                result.add(new Bid(userId, amount, course, section)); 
            } 
              
        }catch(SQLException e){ 
            e.printStackTrace(); 
        }finally{ 
            ConnectionManager.close(conn, pstmt, rs); 
        } 
          
        return result; 
    } 
      
    //retrieve list of bids for a particular course 
    /**
     * retrieve list of unsuccessful bids for a particular course 
     * @param courseId
     * @return list of unsuccessful bid from the specified course
     */
    public static ArrayList<Bid> retrieveByCourseId(String courseId){ 
        ArrayList<Bid> result = new ArrayList<Bid>(); 
          
        Connection conn = null; 
        PreparedStatement pstmt = null; 
        ResultSet rs = null; 
          
        try{ 
            conn = ConnectionManager.getConnection(); 
            pstmt = conn.prepareStatement("SELECT * FROM failed_bid WHERE course = ?"); 
            pstmt.setString(1,courseId); 
            rs = pstmt.executeQuery(); 
              
            while(rs.next()){ 
                String userId = rs.getString("user_id"); 
                BigDecimal amount = rs.getBigDecimal("amount"); 
                String course = rs.getString("course"); 
                String section = rs.getString("section"); 
                  
                result.add(new Bid(userId, amount, course, section)); 
            } 
        }catch(SQLException e){ 
            e.printStackTrace(); 
        }finally{ 
            ConnectionManager.close(conn, pstmt, rs); 
        } 
        return result; 
    } 
      
    //retrieve list of bids for a particular section 
    /**
     * retrieve list of unsuccessful bids for a particular section 
     * @param courseId
     * @param sectionId
     * @return list of unsuccessful bids for the particular section
     */
    public static ArrayList<Bid> retrieveBySectionId(String courseId, String sectionId){ 
        ArrayList<Bid> result = new ArrayList<Bid>(); 
        Connection conn = null; 
        PreparedStatement pstmt = null; 
        ResultSet rs = null; 
        try{ 
              
            conn = ConnectionManager.getConnection(); 
            pstmt = conn.prepareStatement("SELECT * FROM failed_bid WHERE course = ? AND section=?"); 
            pstmt.setString(1, courseId); 
            pstmt.setString(2, sectionId); 
            rs = pstmt.executeQuery(); 
              
            while(rs.next()){ 
                String userId = rs.getString("user_id"); 
                BigDecimal amount = rs.getBigDecimal("amount"); 
                String course = rs.getString("course"); 
                String section = rs.getString("section"); 
                  
                result.add(new Bid(userId, amount, course, section)); 
            } 
              
        }catch(SQLException e){ 
            e.printStackTrace(); 
        }finally{ 
            ConnectionManager.close(conn, pstmt, rs); 
        } 
          
        return result; 
    } 
  
    /**
     * Retrieve by section. In order bid amount (DESC), and userid(ASC)
     * @param courseId
     * @param sectionId
     * @return list of section in order of user bid amount
     */
    public static ArrayList<Bid> retrieveBySectionIdInOrderOfAmountUserId(String courseId, String sectionId){ 
        ArrayList<Bid> result = new ArrayList<Bid>(); 
        Connection conn = null; 
        PreparedStatement pstmt = null; 
        ResultSet rs = null; 
        try{ 
              
            conn = ConnectionManager.getConnection(); 
            pstmt = conn.prepareStatement("SELECT * FROM failed_bid WHERE course = ? AND section=? ORDER BY amount DESC, user_id"); 
            pstmt.setString(1, courseId); 
            pstmt.setString(2, sectionId); 
            rs = pstmt.executeQuery(); 
              
            while(rs.next()){ 
                String userId = rs.getString("user_id"); 
                BigDecimal amount = rs.getBigDecimal("amount"); 
                
                try {
                    amount = amount.setScale(1);
                } catch (ArithmeticException e) {
                    
                }
                
                String course = rs.getString("course"); 
                String section = rs.getString("section"); 
                  
                result.add(new Bid(userId, amount, course, section)); 
            } 
              
        }catch(SQLException e){ 
            e.printStackTrace(); 
        }finally{ 
            ConnectionManager.close(conn, pstmt, rs); 
        } 
          
        return result; 
    }
    
    //add new bid
    /**
     * Add new bid to failed bid database
     * @param userId
     * @param amount
     * @param courseId
     * @param sectionId 
     */
    public static void add(String userId, BigDecimal amount, String courseId, String sectionId ) { 
        Connection conn = null; 
        PreparedStatement pstmt = null; 
        ResultSet rs = null; 
        try{ 
              
            conn = ConnectionManager.getConnection(); 
            String sqlStmt = "INSERT INTO failed_bid (user_id, amount, course, section) VALUES (?,?,?,?)"; 
            pstmt = conn.prepareStatement(sqlStmt); 
                          
            pstmt.setString(1, userId); 
            pstmt.setBigDecimal(2, amount); 
            pstmt.setString(3, courseId); 
            pstmt.setString(4, sectionId); 
              
            pstmt.executeUpdate(); 
              
        }catch(SQLException e){ 
            e.printStackTrace(); 
        }finally{ 
            ConnectionManager.close(conn, pstmt, rs); 
        } 
    } 
    
    /**
     * Add all bids from the specified Bid List into the DB. This bids are considered successful.
     * @param bidList 
     */
    public static void addAll(ArrayList<Bid> bidList) { 
        Connection conn = null; 
        PreparedStatement pstmt = null; 
        ResultSet rs = null; 
        try{ 
              
            conn = ConnectionManager.getConnection(); 
            String sqlStmt = "INSERT INTO failed_bid (user_id, amount, course, section) VALUES (?,?,?,?)"; 
            pstmt = conn.prepareStatement(sqlStmt); 
            
            int batchSize = 0;
            for (Bid bid : bidList) {
            
                pstmt.setString(1, bid.getUserId()); 
                pstmt.setBigDecimal(2, bid.getAmount()); 
                pstmt.setString(3, bid.getCode()); 
                pstmt.setString(4, bid.getSection()); 
              
                pstmt.addBatch();
                
                if (++batchSize == 1000) {
                    pstmt.executeBatch();
                    batchSize = 0;
                }
            
            }
            
            pstmt.executeBatch();
        }catch(SQLException e){ 
            e.printStackTrace(); 
        }finally{ 
            ConnectionManager.close(conn, pstmt, rs); 
        } 
    }
      
    //update bid 
    /**
     * Update a specific row from the failed bid database
     * @param userId
     * @param amount
     * @param courseId
     * @param sectionId 
     */
    public static void update(String userId, BigDecimal amount, String courseId, String sectionId ) { 
      
        Connection conn = null; 
        PreparedStatement pstmt = null; 
        ResultSet rs = null; 
          
        try{ 
              
            conn = ConnectionManager.getConnection(); 
            String sqlStmt = "UPDATE failed_bid SET amount=? WHERE course=? AND section=? AND user_id =?"; 
              
            pstmt = conn.prepareStatement(sqlStmt); 
            pstmt.setBigDecimal(1, amount); 
            pstmt.setString(2, courseId); 
            pstmt.setString(3, sectionId); 
            pstmt.setString(4, userId); 
            
            
            pstmt.executeUpdate(); 
              
        }catch(SQLException e){ 
            e.printStackTrace(); 
        }finally{ 
            ConnectionManager.close(conn, pstmt, rs); 
        } 
    } 
      
    //remove bid 
    /**
     * Remove a specific row from the failed bid database
     * @param userId
     * @param course
     * @param section 
     */
    public static void remove(String userId, String course, String section) { 
          
        Connection conn = null; 
        PreparedStatement pstmt = null; 
        ResultSet rs = null; 
          
        try{ 
              
            conn = ConnectionManager.getConnection(); 
            String sqlStmt = "DELETE FROM failed_bid WHERE user_id =? AND course =? AND section =?"; 
              
            pstmt = conn.prepareStatement(sqlStmt); 
            pstmt.setString(1, userId); 
            pstmt.setString(2, course); 
            pstmt.setString(3, section); 
              
            pstmt.executeUpdate(); 
              
        }catch(SQLException e){ 
            e.printStackTrace(); 
        }finally{ 
            ConnectionManager.close(conn, pstmt, rs); 
        } 
          
    }
    
    /**
     * Empty the failed bid table
     */
    public static void emptyTable() {
        Connection conn = null; 
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try{ 
              
            conn = ConnectionManager.getConnection(); 
            String sqlStmt = "TRUNCATE `failed_bid`"; 
              
            pstmt = conn.prepareStatement(sqlStmt);
              
            pstmt.executeUpdate(); 
              
        }catch(SQLException e){ 
            e.printStackTrace(); 
        }finally{ 
            ConnectionManager.close(conn, pstmt, rs); 
        }
        
   }
}
