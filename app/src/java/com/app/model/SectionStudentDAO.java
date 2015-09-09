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
 * DAO to access table that stores all successful bids
 * @author Zachery
 */
public class SectionStudentDAO {
    //retrieve list of all bids 
    public static ArrayList<Bid> retrieveAll(){ 
        ArrayList<Bid> result = new ArrayList<Bid>(); 
        Connection conn = null; 
        PreparedStatement pstmt = null; 
        ResultSet rs = null; 
        try{ 
            conn = ConnectionManager.getConnection(); 
            pstmt = conn.prepareStatement("SELECT * FROM section_student"); 
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
     * Retrieve an arrayList<Bid> in the order specified by JSON dump
     * @return list of bid
     */
    public static ArrayList<Bid> retrieveAllJsonOrdered(){ 
        ArrayList<Bid> result = new ArrayList<Bid>(); 
        Connection conn = null; 
        PreparedStatement pstmt = null; 
        ResultSet rs = null; 
        try{ 
            conn = ConnectionManager.getConnection(); 
            pstmt = conn.prepareStatement("SELECT * FROM section_student ORDER BY course, user_id"); 
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
     * Retrieve all bid for JSON in round 1
     * @return list of bids
     */
    
    public static ArrayList<Bid> retrieveAllJsonOrderedRound1(){ 
        ArrayList<Bid> result = new ArrayList<Bid>(); 
        Connection conn = null; 
        PreparedStatement pstmt = null; 
        ResultSet rs = null; 
        try{ 
            conn = ConnectionManager.getConnection(); 
            pstmt = conn.prepareStatement("SELECT * FROM section_student WHERE round_num=1 ORDER BY course, user_id"); 
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
     * Retrieve all bid for JSON in round 2
     * @return list of bids
     */
    public static ArrayList<Bid> retrieveAllJsonOrderedRound2(){ 
        ArrayList<Bid> result = new ArrayList<Bid>(); 
        Connection conn = null; 
        PreparedStatement pstmt = null; 
        ResultSet rs = null; 
        try{ 
            conn = ConnectionManager.getConnection(); 
            pstmt = conn.prepareStatement("SELECT * FROM section_student WHERE round_num=2 ORDER BY course, user_id"); 
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
     * Retrieve list of bids by a particular user 
     * @param uId
     * @return list of bids
     */
    public static ArrayList<Bid> retrieveByUserId(String uId){ 
        ArrayList<Bid> result = new ArrayList<Bid>(); 
        Connection conn = null; 
        PreparedStatement pstmt = null; 
        ResultSet rs = null; 
          
        try{ 
            conn = ConnectionManager.getConnection(); 
            pstmt = conn.prepareStatement("SELECT * FROM section_student WHERE user_id=?"); 
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
     * Retrieve list of bids for a particular course 
     * @param courseId
     * @return list of bids
     */
    public static ArrayList<Bid> retrieveByCourseId(String courseId){ 
        ArrayList<Bid> result = new ArrayList<Bid>(); 
          
        Connection conn = null; 
        PreparedStatement pstmt = null; 
        ResultSet rs = null; 
          
        try{ 
            conn = ConnectionManager.getConnection(); 
            pstmt = conn.prepareStatement("SELECT * FROM section_student WHERE course = ?"); 
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
     * retrieve list of bids for a particular section 
     * @param courseId
     * @param sectionId
     * @return list of bids
     */
    public static ArrayList<Bid> retrieveBySectionId(String courseId, String sectionId){ 
        ArrayList<Bid> result = new ArrayList<Bid>(); 
        Connection conn = null; 
        PreparedStatement pstmt = null; 
        ResultSet rs = null; 
        try{ 
              
            conn = ConnectionManager.getConnection(); 
            pstmt = conn.prepareStatement("SELECT * FROM section_student WHERE course = ? AND section=?"); 
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
     * @return 
     */
    public static ArrayList<Bid> retrieveBySectionIdInOrderOfAmountUserId(String courseId, String sectionId){ 
        ArrayList<Bid> result = new ArrayList<Bid>(); 
        Connection conn = null; 
        PreparedStatement pstmt = null; 
        ResultSet rs = null; 
        try{ 
              
            conn = ConnectionManager.getConnection(); 
            pstmt = conn.prepareStatement("SELECT * FROM section_student WHERE course = ? AND section=? ORDER BY amount DESC, user_id"); 
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
     * Retrieve bids in order of section id, amount, user id
     * @param courseId
     * @param sectionId
     * @param roundNum
     * @return list of bids
     */
    public static ArrayList<Bid> retrieveBySectionIdInOrderOfAmountUserIdOfRound(String courseId, String sectionId, int roundNum){ 
        ArrayList<Bid> result = new ArrayList<Bid>(); 
        Connection conn = null; 
        PreparedStatement pstmt = null; 
        ResultSet rs = null; 
        try{ 
              
            conn = ConnectionManager.getConnection(); 
            pstmt = conn.prepareStatement("SELECT * FROM section_student WHERE course = ? AND section=? AND round_num=? ORDER BY amount DESC, user_id"); 
            pstmt.setString(1, courseId); 
            pstmt.setString(2, sectionId); 
            pstmt.setInt(3,roundNum);
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
    
    /**
     * Retrieve bids in order of User id
     * @param courseId
     * @param sectionId
     * @return list of bids
     */
    public static ArrayList<Bid> retrieveBySectionIdInOrderOfUserId(String courseId, String sectionId){ 
        ArrayList<Bid> result = new ArrayList<Bid>(); 
        Connection conn = null; 
        PreparedStatement pstmt = null; 
        ResultSet rs = null; 
        try{ 
              
            conn = ConnectionManager.getConnection(); 
            pstmt = conn.prepareStatement("SELECT * FROM section_student WHERE course =? AND section=? ORDER BY user_id"); 
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
    
    /**
     * Retrieves the bid specified by the userid, code, and section
     * @param userId
     * @param courseId
     * @param sectionId
     * @return specific bid
     */
    public static Bid retrieveSpecificBid(String userId, String courseId, String sectionId) {
        Bid bid = null;
        
        Connection conn = null; 
        PreparedStatement pstmt = null; 
        ResultSet rs = null; 
        try{ 
              
            conn = ConnectionManager.getConnection(); 
            pstmt = conn.prepareStatement("SELECT * FROM section_student WHERE user_id=? AND course = ? AND section=?"); 
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
  
    //add new bid
    /**
     * Add new bid to sectionStudent table in database
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
            String sqlStmt = "INSERT INTO section_student (user_id, amount, course, section) VALUES (?,?,?,?)"; 
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
            String sqlStmt = "INSERT INTO section_student (user_id, amount, course, section, round_num) VALUES (?,?,?,?,?)"; 
            pstmt = conn.prepareStatement(sqlStmt); 
            
            int roundNum = Round.getRound();
            int roundNumAdded = 0;
            if (roundNum == 0) {
                roundNumAdded = 1;
            } else if (roundNum == -1) {
                roundNumAdded = 2;
            }
            
            int batchSize = 0;
            for (Bid bid : bidList) {
            
                pstmt.setString(1, bid.getUserId()); 
                pstmt.setBigDecimal(2, bid.getAmount()); 
                pstmt.setString(3, bid.getCode()); 
                pstmt.setString(4, bid.getSection());
                pstmt.setInt(5,roundNumAdded);
              
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
     * Update specific row of data in database
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
            String sqlStmt = "UPDATE section_student SET amount=? WHERE course=? AND section=? AND user_id =?"; 
              
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
     * Remove a specific row of data in database
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
            String sqlStmt = "DELETE FROM section_student WHERE user_id =? AND course =? AND section =?"; 
              
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
   
}
