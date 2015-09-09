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
 * DAO to access a table storing past bid data for dump purposes
 *  
 */
public class BidTempDAO {

    /**
     *retrieve list of all bids 
     * @return an ArrayList<Bid> with all the bids
     */
    public static ArrayList<Bid> retrieveAll(){ 
        ArrayList<Bid> result = new ArrayList<Bid>(); 
        Connection conn = null; 
        PreparedStatement pstmt = null; 
        ResultSet rs = null; 
        try{ 
            conn = ConnectionManager.getConnection(); 
            pstmt = conn.prepareStatement("SELECT * FROM bid_temp"); 
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
     * Retrieves an arrayList<Bid> in the order specified in Json DUMP
     * @return an ArrayList<Bid> in the order specified
     */
    public static ArrayList<Bid> retrieveAllJsonOrdered(){ 
        ArrayList<Bid> result = new ArrayList<Bid>(); 
        Connection conn = null; 
        PreparedStatement pstmt = null; 
        ResultSet rs = null; 
        try{ 
            conn = ConnectionManager.getConnection(); 
            pstmt = conn.prepareStatement("SELECT * FROM bid_temp ORDER BY course, section, amount DESC, user_id"); 
            rs = pstmt.executeQuery(); 
            while (rs.next()) { 
                String userId = rs.getString("user_id"); 
                BigDecimal amount = rs.getBigDecimal("amount"); 
                
                try {
                    amount = amount.setScale(1);
                } catch (ArithmeticException ae) {
                    //means it is 2 dp, so just leave it.
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
    
    /**
     * retrieves the bids in order of code (ASC), section (ASC), amount (DESC)
     * @return an ArrayList<Bid> of Bids with code (ASC) sorted first, section (ASC) sorted second and amount (DESC) sorted third
     */
    public static ArrayList<Bid> retrieveAllInOrder(){ 
        ArrayList<Bid> result = new ArrayList<Bid>(); 
        Connection conn = null; 
        PreparedStatement pstmt = null; 
        ResultSet rs = null; 
        try{ 
            conn = ConnectionManager.getConnection(); 
            pstmt = conn.prepareStatement("SELECT * FROM bid_temp ORDER BY course, section, amount DESC"); 
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
     *retrieve list of bids by a particular user 
     * @param uId userId of the user
     * @return an ArrayList<Bid> bid by the user
     */
    public static ArrayList<Bid> retrieveByUserId(String uId){ 
        ArrayList<Bid> result = new ArrayList<Bid>(); 
        Connection conn = null; 
        PreparedStatement pstmt = null; 
        ResultSet rs = null; 
          
        try{ 
            conn = ConnectionManager.getConnection(); 
            pstmt = conn.prepareStatement("SELECT * FROM bid_temp WHERE user_id=?"); 
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

    /**
     *retrieve list of bids for a particular course 
     * @param courseId the courseId of the course
     * @return an ArrayList<Bid> of the course
     */
    public static ArrayList<Bid> retrieveByCourseId(String courseId){ 
        ArrayList<Bid> result = new ArrayList<Bid>(); 
          
        Connection conn = null; 
        PreparedStatement pstmt = null; 
        ResultSet rs = null; 
          
        try{ 
            conn = ConnectionManager.getConnection(); 
            pstmt = conn.prepareStatement("SELECT * FROM bid_temp WHERE course = ?"); 
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
      

    /**
     *retrieve list of bids for a particular section 
     * @param courseId the courseId of the course
     * @param sectionId the sectionId of the section
     * @return an ArrayList<Bid> for the particular section
     */
    public static ArrayList<Bid> retrieveBySectionId(String courseId, String sectionId){ 
        ArrayList<Bid> result = new ArrayList<Bid>(); 
        Connection conn = null; 
        PreparedStatement pstmt = null; 
        ResultSet rs = null; 
        try{ 
              
            conn = ConnectionManager.getConnection(); 
            pstmt = conn.prepareStatement("SELECT * FROM bid_temp WHERE course = ? AND section=?"); 
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
     * Returns a ArrayList of bids in the "Sudden Death" round in order of descending bid amounts.
     * @param courseId the courseId of the course
     * @param sectionId the sectionId of the section
     * @return an ArrayList<Bid> in the order of descending bid amount
     */
    public static ArrayList<Bid> retrieveBySectionIdInOrderOfAmount(String courseId, String sectionId){ 
        ArrayList<Bid> result = new ArrayList<Bid>(); 
        Connection conn = null; 
        PreparedStatement pstmt = null; 
        ResultSet rs = null; 
        try{ 
              
            conn = ConnectionManager.getConnection(); 
            pstmt = conn.prepareStatement("SELECT * FROM bid_temp WHERE course = ? AND section=? ORDER BY amount DESC"); 
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
     * Retrieve by section. In order of userid(ASC) and bid amount (DESC)
     * @param courseId the courseId of the course
     * @param sectionId the sectionId of the section
     * @return an ArrayList<Bid> in the order of userid and bid amount
     */
    public static ArrayList<Bid> retrieveBySectionIdInOrderOfAmountUserId(String courseId, String sectionId){ 
        ArrayList<Bid> result = new ArrayList<Bid>(); 
        Connection conn = null; 
        PreparedStatement pstmt = null; 
        ResultSet rs = null; 
        try{ 
              
            conn = ConnectionManager.getConnection(); 
            pstmt = conn.prepareStatement("SELECT * FROM bid_temp WHERE course = ? AND section=? ORDER BY amount DESC, user_id"); 
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
     * Retrieves the bid specified by the userid, code, and section
     * @param userId the userId of the user
     * @param courseId the courseId of the course
     * @param sectionId the sectionId of the section
     * @return an ArrayList<Bid> of the specified userId, course and section
     */
    public static Bid retrieveSpecificBid(String userId, String courseId, String sectionId) {
        Bid bid = null;
        
        Connection conn = null; 
        PreparedStatement pstmt = null; 
        ResultSet rs = null; 
        try{ 
              
            conn = ConnectionManager.getConnection(); 
            pstmt = conn.prepareStatement("SELECT * FROM bid_temp WHERE user_id=? AND course = ? AND section=?"); 
            pstmt.setString(1, userId);
            pstmt.setString(2, courseId); 
            pstmt.setString(3, sectionId); 
            rs = pstmt.executeQuery(); 
              
            while(rs.next()){ 
                String userid = rs.getString("user_id"); 
                BigDecimal amount = rs.getBigDecimal("amount"); 
                String course = rs.getString("course"); 
                String section = rs.getString("section"); 
                  
                bid = new Bid(userid, amount, course, section);
            } 
              
        }catch(SQLException e){ 
            e.printStackTrace(); 
        }finally{ 
            ConnectionManager.close(conn, pstmt, rs); 
        } 
          
        return bid; 
    }
  

    /**
     * adds new bid with the following
     * @param userId the userId of the usre
     * @param amount the bid amount
     * @param courseId courseId of the course
     * @param sectionId sectionIf of the section
     */
    public static void add(String userId, BigDecimal amount, String courseId, String sectionId ) { 
        Connection conn = null; 
        PreparedStatement pstmt = null; 
        ResultSet rs = null; 
        try{ 
              
            conn = ConnectionManager.getConnection(); 
            String sqlStmt = "INSERT INTO bid_temp (user_id, amount, course, section) VALUES (?,?,?,?)"; 
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
     * @param bidList an ArrayList<bid> which are considered successful
     */
    public static void addAll(ArrayList<Bid> bidList) { 
        Connection conn = null; 
        PreparedStatement pstmt = null; 
        ResultSet rs = null; 
        try{ 
              
            conn = ConnectionManager.getConnection(); 
            String sqlStmt = "INSERT INTO bid_temp (user_id, amount, course, section) VALUES (?,?,?,?)"; 
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

    /**
     * Updates an existing bid to the following
     * @param userId the userId of the user
     * @param amount the bid amount
     * @param courseId the courseId of the course
     * @param sectionId the sectionId of the section
     */
    public static void update(String userId, BigDecimal amount, String courseId, String sectionId ) { 
      
        Connection conn = null; 
        PreparedStatement pstmt = null; 
        ResultSet rs = null; 
          
        try{ 
              
            conn = ConnectionManager.getConnection(); 
            String sqlStmt = "UPDATE bid_temp SET amount=? WHERE course=? AND section=? AND user_id =?"; 
              
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

    /**
     * removes the bid with the following
     * @param userId the userId of the user
     * @param course the courseId of the course
     * @param section the sectionId of the section
     */
    public static void remove(String userId, String course, String section) { 
          
        Connection conn = null; 
        PreparedStatement pstmt = null; 
        ResultSet rs = null; 
          
        try{ 
              
            conn = ConnectionManager.getConnection(); 
            String sqlStmt = "DELETE FROM bid_temp WHERE user_id =? AND course =? AND section =?"; 
              
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
     * Remove all bids for the specified Section
     * @param course the courseId of the course
     * @param section the sectionId of the section
     */
    public static void removeBidsFromSection(String course, String section) { 
          
        Connection conn = null; 
        PreparedStatement pstmt = null; 
        ResultSet rs = null; 
          
        try{ 
              
            conn = ConnectionManager.getConnection(); 
            String sqlStmt = "DELETE FROM bid_temp WHERE course =? AND section =?"; 
              
            pstmt = conn.prepareStatement(sqlStmt); 
            pstmt.setString(1, course); 
            pstmt.setString(2, section); 
              
            pstmt.executeUpdate(); 
              
        }catch(SQLException e){ 
            e.printStackTrace(); 
        }finally{ 
            ConnectionManager.close(conn, pstmt, rs); 
        } 
          
    }
       
   /**
    * 1. "If there is no active round, the bids (whether successful or unsuccessful) for the most recently concluded round should be shown."
    * 2. "The system does not need to maintain a history of bidding results from previous bidding rounds."
    * 
    * Therefore, only to be executed upon the start of Round 2.
    * 
    * All the successful bids are already transferred to section_student table.
    * 
    */
   public static void emptyTable() {
        Connection conn = null; 
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try{ 
              
            conn = ConnectionManager.getConnection(); 
            String sqlStmt = "TRUNCATE `bid_temp`"; 
              
            pstmt = conn.prepareStatement(sqlStmt);
              
            pstmt.executeUpdate(); 
              
        }catch(SQLException e){ 
            e.printStackTrace(); 
        }finally{ 
            ConnectionManager.close(conn, pstmt, rs); 
        }
        
   }
}
