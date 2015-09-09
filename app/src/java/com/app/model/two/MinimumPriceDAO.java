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
 
/**
 * Table storing minimum prices and vacancies left for each section
 * @author Zachery
 */
public class MinimumPriceDAO {
    //retrieve list of all MinimumPrice
    /**
     * retrieve list of all MinimumPrice
     * @return list of all the course with their minimum price
     */
    public static ArrayList<MinimumPrice> retrieveAll(){ 
        ArrayList<MinimumPrice> result = new ArrayList<>(); 
        Connection conn = null; 
        PreparedStatement pstmt = null; 
        ResultSet rs = null; 
        try{ 
            conn = ConnectionManager.getConnection(); 
            pstmt = conn.prepareStatement("SELECT * FROM minimum_price LOCK IN SHARE MODE"); 
            rs = pstmt.executeQuery(); 
            while (rs.next()) { 
                String course = rs.getString("course"); 
                String section = rs.getString("section"); 
                int vacancyLeft = rs.getInt("vacancy_left");
                BigDecimal price = rs.getBigDecimal("price");
                  
                result.add(new MinimumPrice(course,section,vacancyLeft,price)); 
            } 
        }catch(SQLException e){ 
            e.printStackTrace(); 
        }finally{ 
            ConnectionManager.close(conn, pstmt, rs); 
        } 
          
        return result; 
    } 
    
      
    //retrieve list of MinimumPrice for a particular course (each course has multiple sections)
    /**
     * retrieve list of MinimumPrice for a particular course (each course has multiple sections)
     * @param courseId
     * @return list of section from the specific course
     */
    public static ArrayList<MinimumPrice> retrieveByCourseId(String courseId){ 
        ArrayList<MinimumPrice> result = new ArrayList<>(); 
          
        Connection conn = null; 
        PreparedStatement pstmt = null; 
        ResultSet rs = null; 
          
        try{ 
            conn = ConnectionManager.getConnection(); 
            pstmt = conn.prepareStatement("SELECT * FROM minimum_price WHERE course = ? LOCK IN SHARE MODE"); 
            pstmt.setString(1,courseId); 
            rs = pstmt.executeQuery(); 
              
            while(rs.next()){ 
                String course = rs.getString("course"); 
                String section = rs.getString("section"); 
                int vacancyLeft = rs.getInt("vacancy_left");
                BigDecimal price = rs.getBigDecimal("price");
                  
                result.add(new MinimumPrice(course,section,vacancyLeft,price)); 
            } 
        }catch(SQLException e){ 
            e.printStackTrace(); 
        }finally{ 
            ConnectionManager.close(conn, pstmt, rs); 
        } 
        return result; 
    } 
    
    /**
     * Retrieve minimum price from specific section
     * @param courseId
     * @param sectionId
     * @return minimum price of the section
     */
    public static MinimumPrice retrieveBySectionId(String courseId, String sectionId){ 
        MinimumPrice minimumPriceObj = null; 
          
        Connection conn = null; 
        PreparedStatement pstmt = null; 
        ResultSet rs = null; 
          
        try{ 
            conn = ConnectionManager.getConnection(); 
            pstmt = conn.prepareStatement("SELECT * FROM minimum_price WHERE course=? AND section=? LOCK IN SHARE MODE"); 
            pstmt.setString(1,courseId);
            pstmt.setString(2,sectionId);
            rs = pstmt.executeQuery(); 
              
            rs.next(); 
            
            String course = rs.getString("course"); 
            String section = rs.getString("section"); 
            int vacancyLeft = rs.getInt("vacancy_left");
            BigDecimal price = rs.getBigDecimal("price");
                  
            minimumPriceObj = new MinimumPrice(course,section,vacancyLeft,price); 
           
        }catch(SQLException e){ 
            e.printStackTrace(); 
        }finally{ 
            ConnectionManager.close(conn, pstmt, rs); 
        } 
        return minimumPriceObj; 
    } 
      //"LOCK IN SHARE MODE" - rows are returned and incoming DML are put on hold until query ends
    /**
     * Retrieve minimum price from specific section from a course
     * @param courseId
     * @param sectionId
     * @return minimum price object
     */
    public static MinimumPrice retrieve(String courseId, String sectionId){ 
        MinimumPrice minimumPrice = null;
        Connection conn = null; 
        PreparedStatement pstmt = null; 
        ResultSet rs = null; 
        try{ 
              
            conn = ConnectionManager.getConnection(); 
            pstmt = conn.prepareStatement("SELECT * FROM minimum_price WHERE course = ? AND section=? LOCK IN SHARE MODE"); 
            pstmt.setString(1, courseId); 
            pstmt.setString(2, sectionId); 
            rs = pstmt.executeQuery(); 
              
            while(rs.next()){ 
                String course = rs.getString("course"); 
                String section = rs.getString("section"); 
                int vacancyLeft = rs.getInt("vacancy_left");
                BigDecimal price = rs.getBigDecimal("price");
                  
               minimumPrice = new MinimumPrice(course,section,vacancyLeft,price);
            } 
              
        }catch(SQLException e){ 
            e.printStackTrace(); 
        }finally{ 
            ConnectionManager.close(conn, pstmt, rs); 
        } 
          
        return minimumPrice; 
    } 
    
    /**
     * Adds a new row in the minimum_price table
     * @param course
     * @param section
     * @param vacancyLeft
     * @param price 
     */
    public static void add(String course, String section, int vacancyLeft, BigDecimal price ) { 
        Connection conn = null; 
        PreparedStatement pstmt = null; 
        ResultSet rs = null; 
        try{ 
              
            conn = ConnectionManager.getConnection(); 
            String sqlStmt = "INSERT INTO minimum_price (course, section, vacancy_left, price) VALUES (?,?,?,?)"; 
            pstmt = conn.prepareStatement(sqlStmt); 
                          
            pstmt.setString(1, course); 
            pstmt.setString(2, section);
            pstmt.setInt(3, vacancyLeft); 
            pstmt.setBigDecimal(4, price); 
              
            pstmt.executeUpdate(); 
              
        }catch(SQLException e){ 
            e.printStackTrace(); 
        }finally{ 
            ConnectionManager.close(conn, pstmt, rs); 
        } 
    } 
    
    /**
     * Add ALL MinimumPrice objects into the DB
     * @param minimumPriceList 
     */
    public static void addAll(ArrayList<MinimumPrice> minimumPriceList) { 
        Connection conn = null; 
        PreparedStatement pstmt = null; 
        ResultSet rs = null; 
        try{ 
              
            conn = ConnectionManager.getConnection(); 
            String sqlStmt = "INSERT INTO minimum_price (course, section, vacancy_left, price) VALUES (?,?,?,?)"; 
            pstmt = conn.prepareStatement(sqlStmt); 
            
            int batchSize = 0;
            for (MinimumPrice minimumPrice : minimumPriceList) {
            
                pstmt.setString(1, minimumPrice.getCourse()); 
                pstmt.setString(2, minimumPrice.getSection());
                pstmt.setInt(3, minimumPrice.getVacancyLeft()); 
                pstmt.setBigDecimal(4, minimumPrice.getPrice()); 
              
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
     * Update row ("Transaction-enabled").
     * @param course
     * @param section
     * @param vacancyLeft
     * @param price 
     */ 
    public static void update(String course, String section, int vacancyLeft, BigDecimal price ) { 
      
        Connection conn = null; 
        PreparedStatement pstmt = null; 
        ResultSet rs = null; 
          
        try{ 
              
            conn = ConnectionManager.getConnection();
            conn.setAutoCommit(false);  //enable transaction
            
            String lockStmt = "SELECT * FROM minimum_price WHERE course=? AND section =? FOR UPDATE"; 
            pstmt = conn.prepareStatement(lockStmt);
            pstmt.setString(1, course); 
            pstmt.setString(2, section);
            
            rs = pstmt.executeQuery();  //select row and lock it
            
            String updateStmt = "UPDATE minimum_price SET price=?, vancancy_left=? WHERE course=? AND section=?";
            pstmt = conn.prepareStatement(updateStmt);
             
            pstmt.setBigDecimal(1, price); 
            pstmt.setInt(2, vacancyLeft);
            pstmt.setString(3, course); 
            pstmt.setString(4, section); 
          
            pstmt.executeUpdate();
            conn.commit();
        }catch(SQLException e){ 
            e.printStackTrace(); 
        }finally{ 
            ConnectionManager.close(conn, pstmt, rs); 
        } 
    } 
    
    /**
     * Update row ("Transaction-enabled").
     * @param course
     * @param section
     * @param vacancyLeft
     * @param price 
     */ 
    public static void updateVacancy(String course, String section, int vacancyLeft) { 
      
        Connection conn = null; 
        PreparedStatement pstmt = null; 
        ResultSet rs = null; 
          
        try{ 
              
            conn = ConnectionManager.getConnection();
            conn.setAutoCommit(false);  //enable transaction
            
            String lockStmt = "SELECT * FROM minimum_price WHERE course=? AND section =? FOR UPDATE"; 
            pstmt = conn.prepareStatement(lockStmt);
            pstmt.setString(1, course); 
            pstmt.setString(2, section);
            
            rs = pstmt.executeQuery();  //select row and lock it
            
            String updateStmt = "UPDATE minimum_price SET vacancy_left=? WHERE course=? AND section=?";
            pstmt = conn.prepareStatement(updateStmt);
              
            pstmt.setInt(1, vacancyLeft);
            pstmt.setString(2, course); 
            pstmt.setString(3, section); 
          
            pstmt.executeUpdate();
            conn.commit();
        }catch(SQLException e){ 
            e.printStackTrace(); 
        }finally{ 
            ConnectionManager.close(conn, pstmt, rs); 
        } 
    } 
      
    //remove minimum price. NOT USED
    public static void remove(String course, String section) { 
          
        Connection conn = null; 
        PreparedStatement pstmt = null; 
        ResultSet rs = null; 
          
        try{ 
              
            conn = ConnectionManager.getConnection(); 
            String sqlStmt = "DELETE FROM minimum_price WHERE course =? AND section =?"; 
              
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
    * Empties the minimum_price table. NOT USED
    */
   public static void emptyTable() {
        Connection conn = null; 
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try{ 
              
            conn = ConnectionManager.getConnection(); 
            String sqlStmt = "TRUNCATE `minimum_price`"; 
              
            pstmt = conn.prepareStatement(sqlStmt);
              
            pstmt.executeUpdate(); 
              
        }catch(SQLException e){ 
            e.printStackTrace(); 
        }finally{ 
            ConnectionManager.close(conn, pstmt, rs); 
        }
        
   }
}
