/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.app.model.two;

import com.app.model.Bid;
import com.app.utility.ConnectionManager;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ListIterator;
 
/**
 * Separate DAO for separate table to manage Round 2 bids
 * @author Zachery
 */
public class SuddenDeathDAO {
    //retrieve list of all bids 
    public static ArrayList<Bid> retrieveAll(){ 
        ArrayList<Bid> result = new ArrayList<Bid>(); 
        Connection conn = null; 
        PreparedStatement pstmt = null; 
        ResultSet rs = null; 
        try{ 
            conn = ConnectionManager.getConnection(); 
            pstmt = conn.prepareStatement("SELECT * FROM sudden_death LOCK IN SHARE MODE"); 
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
            pstmt = conn.prepareStatement("SELECT * FROM sudden_death ORDER BY course, section, amount DESC LOCK IN SHARE MODE"); 
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
     * Retrieve all bids that is still above clearing price in round 2
     * @return list of pass bids
     */
    public static ArrayList<Bid> retrieveAllInOrderPass(){ 
        ArrayList<Bid> result = new ArrayList<Bid>(); 
        Connection conn = null; 
        PreparedStatement pstmt = null; 
        ResultSet rs = null; 
        try{ 
            conn = ConnectionManager.getConnection(); 
            pstmt = conn.prepareStatement("SELECT * FROM sudden_death WHERE is_successful=? ORDER BY course, section, amount DESC LOCK IN SHARE MODE"); 
            pstmt.setInt(1,1);
            
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
     * Retrieve all bids that is lower than the clearing price in round 2
     * @return list of failed bids
     */
    public static ArrayList<Bid> retrieveAllInOrderFail(){ 
        ArrayList<Bid> result = new ArrayList<Bid>(); 
        Connection conn = null; 
        PreparedStatement pstmt = null; 
        ResultSet rs = null; 
        try{ 
            conn = ConnectionManager.getConnection(); 
            pstmt = conn.prepareStatement("SELECT * FROM sudden_death WHERE is_successful=? ORDER BY course, section, amount DESC LOCK IN SHARE MODE"); 
            pstmt.setInt(1,0);
            
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
            pstmt = conn.prepareStatement("SELECT * FROM sudden_death WHERE user_id=? LOCK IN SHARE MODE"); 
            pstmt.setString(1,uId); 
            rs = pstmt.executeQuery(); 
              
            while(rs.next()){ 
                String userId = rs.getString("user_id"); 
                BigDecimal amount = rs.getBigDecimal("amount"); 
                String course = rs.getString("course"); 
                String section = rs.getString("section");
                
                int successful = rs.getInt("is_successful");
                boolean isSuccessful = true;
                if (successful != 1) {
                    isSuccessful = false;
                }
                  
                result.add(new Bid(userId, amount, course, section, isSuccessful)); 
            } 
              
        }catch(SQLException e){ 
            e.printStackTrace(); 
        }finally{ 
            ConnectionManager.close(conn, pstmt, rs); 
        } 
          
        return result; 
    } 
    
    /**
     * Retrieve bids from user that is still above clearing price
     * @param uId
     * @return list of bids
     */
    public static ArrayList<Bid> retrieveByUserIdPass(String uId){ 
        ArrayList<Bid> result = new ArrayList<Bid>(); 
        Connection conn = null; 
        PreparedStatement pstmt = null; 
        ResultSet rs = null; 
          
        try{ 
            conn = ConnectionManager.getConnection(); 
            pstmt = conn.prepareStatement("SELECT * FROM sudden_death WHERE user_id=? AND is_successful=? LOCK IN SHARE MODE"); 
            pstmt.setString(1,uId); 
            pstmt.setInt(2,1);
            rs = pstmt.executeQuery(); 
              
            while(rs.next()){ 
                String userId = rs.getString("user_id"); 
                BigDecimal amount = rs.getBigDecimal("amount"); 
                String course = rs.getString("course"); 
                String section = rs.getString("section"); 
                  
                result.add(new Bid(userId, amount, course, section, true)); 
            } 
              
        }catch(SQLException e){ 
            e.printStackTrace(); 
        }finally{ 
            ConnectionManager.close(conn, pstmt, rs); 
        } 
          
        return result; 
    }
    
    /**
     * Retrieve list of bids of specific user that is lower than clearing price
     * @param uId
     * @return list of bids
     */
    public static ArrayList<Bid> retrieveByUserIdFail(String uId){ 
        ArrayList<Bid> result = new ArrayList<Bid>(); 
        Connection conn = null; 
        PreparedStatement pstmt = null; 
        ResultSet rs = null; 
          
        try{ 
            conn = ConnectionManager.getConnection(); 
            pstmt = conn.prepareStatement("SELECT * FROM sudden_death WHERE user_id=? AND is_successful=? LOCK IN SHARE MODE"); 
            pstmt.setString(1,uId); 
            pstmt.setInt(2,0);
            rs = pstmt.executeQuery(); 
              
            while(rs.next()){ 
                String userId = rs.getString("user_id"); 
                BigDecimal amount = rs.getBigDecimal("amount"); 
                String course = rs.getString("course"); 
                String section = rs.getString("section"); 
                  
                result.add(new Bid(userId, amount, course, section, false)); 
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
     * @param userIdStr
     * @param courseStr
     * @param sectionStr
     * @return list of bids
     */
    public static Bid retrieveSpecificBid(String userIdStr, String courseStr, String sectionStr){ 
        Bid result = null;
        Connection conn = null; 
        PreparedStatement pstmt = null; 
        ResultSet rs = null; 
          
        try{ 
            conn = ConnectionManager.getConnection(); 
            pstmt = conn.prepareStatement("SELECT * FROM sudden_death WHERE user_id=? AND course=? AND section=? LOCK IN SHARE MODE"); 
            pstmt.setString(1,userIdStr); 
            pstmt.setString(2,courseStr);
            pstmt.setString(3,sectionStr);
            rs = pstmt.executeQuery(); 
              
            while(rs.next()){ 
                String userId = rs.getString("user_id"); 
                BigDecimal amount = rs.getBigDecimal("amount"); 
                String course = rs.getString("course"); 
                String section = rs.getString("section"); 
                  
                result = new Bid(userId, amount, course, section); 
            } 
              
        }catch(SQLException e){ 
            e.printStackTrace(); 
        }finally{ 
            ConnectionManager.close(conn, pstmt, rs); 
        } 
          
        return result; 
    } 
    
    /**
     * Retrieve user bids on specific course in round 2
     * @param userIdStr
     * @param courseStr
     * @return bid object
     */
    public static Bid retrieveUserBidOfCourse(String userIdStr, String courseStr){ 
        Bid result = null;
        Connection conn = null; 
        PreparedStatement pstmt = null; 
        ResultSet rs = null; 
          
        try{ 
            conn = ConnectionManager.getConnection(); 
            pstmt = conn.prepareStatement("SELECT * FROM sudden_death WHERE user_id=? AND course=? LOCK IN SHARE MODE"); 
            pstmt.setString(1,userIdStr); 
            pstmt.setString(2,courseStr);
            rs = pstmt.executeQuery(); 
              
            while(rs.next()){ 
                String userId = rs.getString("user_id"); 
                BigDecimal amount = rs.getBigDecimal("amount"); 
                String course = rs.getString("course"); 
                String section = rs.getString("section"); 
                  
                result = new Bid(userId, amount, course, section); 
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
     * retrieve list of bids for a particular course 
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
            pstmt = conn.prepareStatement("SELECT * FROM sudden_death WHERE course = ? LOCK IN SHARE MODE"); 
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
     * "LOCK IN SHARE MODE" - rows are returned and incoming DML are put on hold until query ends
     * @param courseId
     * @param sectionId
     * @return 
     */
    public static ArrayList<Bid> retrieveBySectionId(String courseId, String sectionId){ 
        ArrayList<Bid> result = new ArrayList<Bid>(); 
        Connection conn = null; 
        PreparedStatement pstmt = null; 
        ResultSet rs = null; 
        try{ 
              
            conn = ConnectionManager.getConnection(); 
            pstmt = conn.prepareStatement("SELECT * FROM sudden_death WHERE course = ? AND section=? LOCK IN SHARE MODE"); 
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
     * "LOCK IN SHARE MODE" - rows are returned and incoming DML are put on hold until query ends
     * @param courseId
     * @param sectionId
     * @return 
     */
    public static ArrayList<Bid> retrieveBySectionIdInOrderOfAmount(String courseId, String sectionId){ 
        ArrayList<Bid> result = new ArrayList<Bid>(); 
        Connection conn = null; 
        PreparedStatement pstmt = null; 
        ResultSet rs = null; 
        try{ 
              
            conn = ConnectionManager.getConnection(); 
            pstmt = conn.prepareStatement("SELECT * FROM sudden_death WHERE course=? AND section=? ORDER BY amount DESC LOCK IN SHARE MODE"); 
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
            pstmt = conn.prepareStatement("SELECT * FROM sudden_death WHERE course = ? AND section=? ORDER BY amount DESC, user_id LOCK IN SHARE MODE"); 
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
    
    public static ArrayList<Bid> retrieveForDumpTable() {
        ArrayList<Bid> result = new ArrayList<Bid>(); 
        Connection conn = null; 
        PreparedStatement pstmt = null; 
        ResultSet rs = null; 
        try{ 
              
            conn = ConnectionManager.getConnection(); 
            pstmt = conn.prepareStatement("SELECT * FROM sudden_death WHERE is_latest=1 ORDER BY course, section, amount DESC, user_id LOCK IN SHARE MODE"); 
            rs = pstmt.executeQuery(); 
              
            while(rs.next()){ 
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
  
    //add new bid
    /**
     * Add new bid to database
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
            String sqlStmt = "INSERT INTO sudden_death (user_id, amount, course, section) VALUES (?,?,?,?)"; 
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
            String sqlStmt = "INSERT INTO sudden_death (user_id, amount, course, section) VALUES (?,?,?,?)"; 
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
     * Update a specific row of data in database
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
            String sqlStmt = "UPDATE sudden_death SET amount=? WHERE course=? AND section=? AND user_id =?"; 
              
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
            String sqlStmt = "DELETE FROM sudden_death WHERE user_id =? AND course =? AND section =?"; 
              
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
     * @param course
     * @param section 
     */
    public static void removeBidsFromSection(String course, String section) { 
          
        Connection conn = null; 
        PreparedStatement pstmt = null; 
        ResultSet rs = null; 
          
        try{ 
              
            conn = ConnectionManager.getConnection(); 
            String sqlStmt = "DELETE FROM sudden_death WHERE course =? AND section =?"; 
              
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
            String sqlStmt = "TRUNCATE `sudden_death`"; 
              
            pstmt = conn.prepareStatement(sqlStmt);
              
            pstmt.executeUpdate(); 
              
        }catch(SQLException e){ 
            e.printStackTrace(); 
        }finally{ 
            ConnectionManager.close(conn, pstmt, rs); 
        }
        
   }

   
    /*
     * NEVER REMOVE NEVER REMOVE NEVER REMOVE NEVER REMOVE NEVER REMOVE
     */
    /**
     * add bid to sudden_death table, reflecting new changes (transaction-based).
     * @param userId
     * @param amount
     * @param code
     * @param section 
     */
    public static boolean addBid(String userId, BigDecimal amount, String code, String section) {
        Connection conn = null; 
        PreparedStatement pstmt = null; 
        ResultSet rs = null; 
        
        try{ 
              
            conn = ConnectionManager.getConnection(); 
            
            //Set autocommit to false
            conn.setAutoCommit(false);
            
            Bid incomingBid = new Bid(userId,amount,code,section);
            
            /*
            Bid existingBid = null;
            
            boolean incomingBidIsForDifferentSection = false;
            pstmt = conn.prepareStatement("SELECT * FROM sudden_death WHERE user_id=? AND course=?");
            pstmt.setString(1,userId);
            pstmt.setString(2,code);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                String sectionCheck = rs.getString("section");
                BigDecimal amountCheck = rs.getBigDecimal("amount");
                
                existingBid = new Bid(userId,amountCheck,code,sectionCheck);
                
                if (!incomingBid.equals(existingBid)) {
                    incomingBidIsForDifferentSection = true;
                }
            }
            
            pstmt.close();
            
            //IF incomingBidIsForDifferentSection, DELETE 
            if (incomingBidIsForDifferentSection) {
                System.out.println("incomingBidIsForDifferentSection: " +incomingBidIsForDifferentSection);
            }
            */
            
            //lock minimum_price and sudden_death tables, prevent checking and updating
            pstmt = conn.prepareStatement("SELECT user_id, amount, vacancy_left, price, is_successful FROM minimum_price m LEFT OUTER JOIN sudden_death s ON m.course=s.course AND m.section=s.section WHERE m.course=? AND m.section =? ORDER BY amount DESC FOR UPDATE");
            pstmt.setString(1, code); 
            pstmt.setString(2, section);
            rs = pstmt.executeQuery();
            
            //INITIALIZE VALUES
            ArrayList<Bid> suddenDeathBids = new ArrayList<>();
            ArrayList<Bid> suddenDeathBidsFail = new ArrayList<>();
            MinimumPrice minimumPrice = null;
            while (rs.next()) {
                String userIdTemp = rs.getString("user_id");
                BigDecimal amountTemp = rs.getBigDecimal("amount");
                int isSuccessful = rs.getInt("is_successful");
                
                //there will be no userid and amount, if sudden death bids is 0
                if (userIdTemp != null) { 
                    
                    if (isSuccessful == 1) {
                        suddenDeathBids.add(new Bid(userIdTemp,amountTemp,code,section));
                    }
                    
                    if (isSuccessful == 0) {
                        suddenDeathBidsFail.add(new Bid(userIdTemp,amountTemp,code,section));
                    }
                }
                if (minimumPrice == null) {
                    minimumPrice = new MinimumPrice(code,section,rs.getInt("vacancy_left"),rs.getBigDecimal("price"));
                }
            }
            System.out.println("Line540: bids.size=" +suddenDeathBids.size());
            
            //DO VALIDATION TO CHECK FOR CLEARING PRICE
            if (amount.compareTo(minimumPrice.getPrice()) < 0) {
                ConnectionManager.close(conn, pstmt, rs);
                return false;
            }
            
            
            //------- IF THERE ARE EMPTY SLOTS, JUST ADD
            int biddableVacancies = minimumPrice.getVacancyLeft();
            boolean hasEmptySlots = biddableVacancies - suddenDeathBids.size() > 0;
            
            
            
            
            /*
             * 
             * SIDE QUEST: CHECK IF THE INCOMING BID IS AN UPDATE OF PREVIOUS BID
             * 
             */
            boolean isUpdateOfPreviousBid = false;
            for (Bid bidCheck : suddenDeathBids) {
                if (userId.equals(bidCheck.getUserId())) {
                    isUpdateOfPreviousBid = true;
                    System.out.println("Line535: isUpdateOfPreviousBid = " +isUpdateOfPreviousBid);
                    break;
                }
            }
            
            if (isUpdateOfPreviousBid) {
                ListIterator<Bid> iter = suddenDeathBids.listIterator();
                
                while (iter.hasNext()) {
                    Bid bid = iter.next();
                    if (userId.equals(bid.getUserId())) {
                        iter.remove();
                        break;
                    }
                }
                //after removing. Take the incomingBid (which is an update) and re-rank it.
                //Continue main-flow
            }
            
            /*
             * BACK TO MAIN-FLOW OF RANKING
             */
            //Amount is NOW at least minimum price. (Line 543 has confirmed this) Add bid into list
            for (int i = 0; i < suddenDeathBids.size(); i++) {
                Bid bid = suddenDeathBids.get(i);

                if (amount.compareTo(bid.getAmount()) > 0) {    //MUST BE GREATER, to add. therefore, it may be same as previous bid in the loop, but it will be at the end
                    suddenDeathBids.add(i,incomingBid);                  
                    break;
                }

            }
            /*
             * CORNER CASE: When there are empty slots, and incomingBid is still small, but not added.
             * Also applies for FIRST MOVERS. (When he is the first person to bid in round 2)
             */ 
            if (!suddenDeathBids.contains(incomingBid)) {
                //its ok to just add to the end. Since it is the last
                suddenDeathBids.add(incomingBid);
            }

            /*
             * 
             * 
             * END OF RANKING
             * 
             * 
             */
            
            
            //CASE 1: HAS EMPTY SLOTS
            //PROCEED TO UPDATE EXISTING BIDS AND ADD
            
            //CASE 2: LARGE OPERATION ONLY WHEN THERE ARE NO EMPTY SLOTS.
            if (!hasEmptySlots) {
            
                //-------FIND NEW CLEARING PRICE, and update minimum_price table
                Bid newMinimumBid = suddenDeathBids.get(biddableVacancies - 1);
                BigDecimal newClearingPrice = newMinimumBid.getAmount().add(new BigDecimal("1.00"));
                minimumPrice.setPrice(newClearingPrice);
                
                
                pstmt = conn.prepareStatement("UPDATE minimum_price SET price=? WHERE course=? AND section=?");
                pstmt.setBigDecimal(1,newClearingPrice);
                pstmt.setString(2,code);
                pstmt.setString(3,section);
                pstmt.executeUpdate();
                System.out.println("LINE630: UPDATE minimum_price successful. New minimum price =" +newClearingPrice.toString());
                
                pstmt.close();
                

                

                //---------- MODIFY LIST TO REMOVE BIDS THAT ARE FORCIBLY DROPPED
                //if the bid lying outside the vacancy is the same as the last bid, all the bids at the end, that are same, are dropped
                Bid outlierBid = suddenDeathBids.get(suddenDeathBids.size() - 1);
                if (outlierBid.getAmount().compareTo(newMinimumBid.getAmount()) == 0) {
                    //proceed to remove them
                    ListIterator<Bid> iter = suddenDeathBids.listIterator();

                    while (iter.hasNext()) {
                        Bid bid = iter.next();

                        if (bid.getAmount().compareTo(outlierBid.getAmount()) == 0) {
                            pstmt = conn.prepareStatement("INSERT INTO sudden_death (user_id, amount, course,section,is_successful,is_latest) VALUES(?,?,?,?,?,0) ON DUPLICATE KEY UPDATE is_successful=?, is_latest=0");
                            pstmt.setString(1,bid.getUserId());
                            pstmt.setBigDecimal(2,bid.getAmount()); 
                            pstmt.setString(3,bid.getCode());
                            pstmt.setString(4,bid.getSection());
                            pstmt.setInt(5,0);
                            pstmt.setInt(6,0);
                            pstmt.executeUpdate();
                            System.out.println("Line654: delete of 2 or more bids successful(is_successful set to 0");
                            
                            pstmt.close();

                            iter.remove();
                        }
                    }

                } else { //else if the outlierBid is less than the last bid, just remove the last bid.

                    ListIterator<Bid> iter = suddenDeathBids.listIterator(biddableVacancies);

                    //should only remove 1
                    while (iter.hasNext()) {
                        Bid bid = iter.next();
                        System.out.println(bid.getUserId());
                        System.out.println(bid.getCode());
                        System.out.println(bid.getSection());

                        pstmt = conn.prepareStatement("INSERT INTO sudden_death (user_id, amount, course,section,is_successful,is_latest) VALUES(?,?,?,?,?,0) ON DUPLICATE KEY UPDATE is_successful=?, is_latest=0");
                        pstmt.setString(1,bid.getUserId());
                        pstmt.setBigDecimal(2,bid.getAmount()); 
                        pstmt.setString(3,bid.getCode());
                        pstmt.setString(4,bid.getSection());
                        pstmt.setInt(5,0);
                        pstmt.setInt(6,0);
                        pstmt.executeUpdate();
                        System.out.println("Line674: delete is ok. Outlier bid is_successful is set to 0");
                        
                        pstmt.close();

                        iter.remove();
                    }
                }
                
                
                
            }   //END OF CASE 2: NO EMPTY SLOTS
            
            
            
            //-------- UPDATE THE EXISTING BIDS AND INSERT NEW BID
            for (Bid bid : suddenDeathBids) {
                //identify existing bids from the incomingBid. IF EXISTING, UPDATE
                    System.out.println("LINE 688: reached");
                    //IF INCOMING BID, INSERT
                    pstmt = conn.prepareStatement("INSERT INTO sudden_death (user_id, amount, course,section,is_successful,is_latest) VALUES(?,?,?,?,1,1) ON DUPLICATE KEY UPDATE amount=?, is_latest=?, is_successful=1, section=?");
                    pstmt.setString(1,bid.getUserId());
                    pstmt.setBigDecimal(2,bid.getAmount()); 
                    pstmt.setString(3,bid.getCode());
                    pstmt.setString(4,bid.getSection());
                    pstmt.setBigDecimal(5,bid.getAmount());
                    
                    int numLatest = 0;
                    if (//isUpdateOfPreviousBid //|| suddenDeathBidsFail.contains(incomingBid) 
                            bid.equals(incomingBid)) {    //this indicates that the person that was kicked out is now reinstating his bid with a higher amount
                        numLatest = 1;  //if so, he becomes the latest bid. the rest is old.
                    }
                    
                    pstmt.setInt(6,numLatest);
                    pstmt.setString(7,bid.getSection());
                    
                    pstmt.executeUpdate();
                    System.out.println("Line588: Insert/Update succeed");
                    
                    pstmt.close();
            }
            
            //ACCOUNT FOR MINIMUM PRICE WHEN THERE IS NO MORE EMPTY SLOTS. AFTER BID IS ADDED. WE NEED TO CHECK ONCE MORE
            hasEmptySlots = biddableVacancies - suddenDeathBids.size() > 0;
            if (!hasEmptySlots) {
                //-------FIND NEW CLEARING PRICE, and update minimum_price table
                Bid newMinimumBid = suddenDeathBids.get(biddableVacancies - 1);
                BigDecimal newClearingPrice = newMinimumBid.getAmount().add(new BigDecimal("1.00"));
                minimumPrice.setPrice(newClearingPrice);

                pstmt = conn.prepareStatement("UPDATE minimum_price SET price=? WHERE course=? AND section=?");
                pstmt.setBigDecimal(1,newClearingPrice);
                pstmt.setString(2,code);
                pstmt.setString(3,section);
                pstmt.executeUpdate();
                System.out.println("LINE734: UPDATE minimum_price successful. New minimum price =" +newClearingPrice.toString());
                
                pstmt.close();
            }   //if after dropping/ and adding, there are emptySlots, the earlier price update remains.
                    
            
            System.out.println("LINE 702:BEFORE COMMIT");
            conn.commit();
            
            
            
            ConnectionManager.close(conn, pstmt, rs); 
            return true;
            
        } catch(SQLException e){ 
            //WHEN COMMIT FAILS, ROLL BACK CHANGES
            if (conn != null) {
                try {
                    conn.rollback();
                    System.out.println("ROLLBACK SUCCEED");
                    
                } catch (SQLException se) {

                } finally {
                    System.out.println("FINALLY BLOCK");
                    ConnectionManager.close(conn, pstmt, rs);
                    return false;
                }
            }
            
            e.printStackTrace(); 
            ConnectionManager.close(conn, pstmt, rs);
            return false;
        }
        
    }
   
}
