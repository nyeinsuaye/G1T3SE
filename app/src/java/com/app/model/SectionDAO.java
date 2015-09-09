package com.app.model;

import com.app.utility.ConnectionManager;
import java.sql.Connection; 
import java.sql.PreparedStatement;
import java.sql.ResultSet; 
import java.sql.SQLException; 
import java.sql.Statement; 
import java.util.ArrayList; 
import java.util.List; 
  
/**
 * DAO to access section table of Section information
 * @author Zachery
 */
public class SectionDAO { 
  
    /**
     * Retrieve all sections from database
     * @return all sections
     */
    public static ArrayList<Section> retrieveAll(){ 
        ArrayList<Section> result = new ArrayList<Section>(); 
          
        Connection conn = null; 
        PreparedStatement pstmt = null; 
        ResultSet rs = null; 
        try{ 
            conn = ConnectionManager.getConnection(); 
            pstmt = conn.prepareStatement("SELECT * FROM section"); 
            rs = pstmt.executeQuery(); 
              
            while (rs.next()) { 
                String course = rs.getString("course"); 
                  
                String section = rs.getString("section"); 
                  
                int day = rs.getInt("day"); 
                  
                java.util.Date start = new java.util.Date(rs.getTime("start").getTime()); 
                  
                java.util.Date end = new java.util.Date(rs.getTime("end").getTime()); 
                  
                String instructor = rs.getString(6); 
                  
                String venue = rs.getString(7); 
                  
                int size = rs.getInt(8); 
                  
                result.add(new Section(course, section, day, start, end, instructor, venue, size)); 
            } 
        }catch(SQLException e){ 
            e.printStackTrace(); 
        }finally{ 
            ConnectionManager.close(conn, pstmt, rs); 
        } 
          
        return result; 
    } 
    
    /**
     * Retrieve all sections with day and date as string
     * @return all sections (With Day represented as String)
     */
    public static ArrayList<Section> retrieveAllWithDayAndDatesAsStringOrdered(){ 
        ArrayList<Section> result = new ArrayList<Section>(); 
          
        Connection conn = null; 
        PreparedStatement pstmt = null; 
        ResultSet rs = null; 
        try{ 
            conn = ConnectionManager.getConnection(); 
            pstmt = conn.prepareStatement("SELECT * FROM section ORDER BY course, section"); 
            rs = pstmt.executeQuery(); 
              
            while (rs.next()) { 
                String course = rs.getString("course"); 
                  
                String section = rs.getString("section"); 
                  
                int day = rs.getInt("day"); 
                  
                java.util.Date start = new java.util.Date(rs.getTime("start").getTime()); 
                  
                java.util.Date end = new java.util.Date(rs.getTime("end").getTime()); 
                  
                String instructor = rs.getString(6); 
                  
                String venue = rs.getString(7); 
                  
                int size = rs.getInt(8); 
                  
                result.add(new Section(true, course, section, day, start, end, instructor, venue, size)); 
            } 
        }catch(SQLException e){ 
            e.printStackTrace(); 
        }finally{ 
            ConnectionManager.close(conn, pstmt, rs); 
        } 
          
        return result; 
    } 
    
      
    /**
     * 
     * @param courseId
     * @param sectionId
     * @return the section in the DB with the matching courseId and sectionId as the given parameters
     */
    public static Section retrieveSection(String courseId, String sectionId){ 
        Section sectionToBeReturned = null; 
          
        Connection conn = null; 
        PreparedStatement pstmt = null; 
        ResultSet rs = null; 
        try{ 
            conn = ConnectionManager.getConnection(); 
            pstmt = conn.prepareStatement("SELECT * FROM section WHERE course=? and section=?"); 
            pstmt.setString(1,courseId); 
            pstmt.setString(2,sectionId); 
            rs = pstmt.executeQuery(); 
              
            while (rs.next()) { 
                String course = rs.getString("course"); 
                  
                String sectionStr = rs.getString("section"); 
                  
                int day = rs.getInt("day"); 
                  
                java.util.Date start = new java.util.Date(rs.getTime("start").getTime()); 
                  
                java.util.Date end = new java.util.Date(rs.getTime("end").getTime()); 
                  
                String instructor = rs.getString("instructor"); 
                  
                String venue = rs.getString("venue"); 
                  
                int size = rs.getInt("size"); 
                  
                sectionToBeReturned = new Section(course, sectionStr, day, start, end, instructor, venue, size); 
               } 
        }catch(SQLException e){ 
            e.printStackTrace(); 
        }finally{ 
            ConnectionManager.close(conn, pstmt, rs); 
        } 
          
        return sectionToBeReturned; 
    } 
      
    //retrieve all sections of a particular course 
    /**
     * retrieve all sections of a particular course 
     * @param courseId
     * @return list of section
     */
    public static ArrayList<Section> retrieveByCourseId(String courseId){ 
        ArrayList<Section> result = new ArrayList<Section>(); 
          
        Connection conn = null; 
        PreparedStatement pstmt = null; 
        ResultSet rs = null; 
        try{ 
            conn = ConnectionManager.getConnection(); 
            pstmt = conn.prepareStatement("SELECT * FROM section WHERE course=?"); 
            pstmt.setString(1,courseId); 
            rs = pstmt.executeQuery(); 
              
            while (rs.next()) { 
                String course = rs.getString("course"); 
                  
                String section = rs.getString("section"); 
                  
                int day = rs.getInt("day"); 
                  
                java.util.Date start = new java.util.Date(rs.getTime("start").getTime()); 
                  
                java.util.Date end = new java.util.Date(rs.getTime("end").getTime()); 
                  
                String instructor = rs.getString("instructor"); 
                  
                String venue = rs.getString("venue"); 
                  
                int size = rs.getInt("size"); 
                  
                result.add(new Section(course, section, day, start, end, instructor, venue, size)); 
            } 
              
        }catch(SQLException e){ 
            e.printStackTrace(); 
        }finally{ 
            ConnectionManager.close(conn, pstmt, rs); 
        } 
          
        return result; 
    } 
      
    //Add new Section 
    /**
     * Add new Section to database
     * @param courseId
     * @param sectionId
     * @param day
     * @param start
     * @param end
     * @param instructor
     * @param venue
     * @param size 
     */
    public static void add(String courseId, String sectionId, int day, java.util.Date start, java.util.Date end, String instructor, String venue, int size) { 
          
        Connection conn = null; 
        PreparedStatement pstmt = null; 
        ResultSet rs = null; 
        try{ 
            conn = ConnectionManager.getConnection(); 
            pstmt = conn.prepareStatement("INSERT INTO Section (course,section,day,start,end,instructor,venue,size) VALUES(?,?,?,?,?,?,?,?)"); 
            pstmt.setString(1,courseId); 
            pstmt.setString(2,sectionId); 
            pstmt.setInt(3,day); 
            java.sql.Time startSqlTime = new java.sql.Time(start.getTime()); 
            pstmt.setTime(4,startSqlTime); 
              
            java.sql.Time endSqlTime = new java.sql.Time(end.getTime()); 
            pstmt.setTime(5,endSqlTime); 
            pstmt.setString(6,instructor); 
            pstmt.setString(7,venue); 
            pstmt.setInt(8,size); 
            pstmt.executeUpdate(); 
              
        }catch(SQLException e){ 
            e.printStackTrace(); 
        }finally{ 
            ConnectionManager.close(conn, pstmt, rs); 
        }        
          
    } 
      
    //Update section information 
    /**
     * Update the specific row of data from database
     * @param courseId
     * @param sectionId
     * @param day
     * @param start
     * @param end
     * @param instructor
     * @param venue
     * @param size 
     */
    public static void update(String courseId, String sectionId, int day, java.util.Date start, java.util.Date end, String instructor, String venue, int size) { 
          
        Connection conn = null; 
        PreparedStatement pstmt = null; 
        ResultSet rs = null; 
        try{ 
            conn = ConnectionManager.getConnection(); 
            pstmt = conn.prepareStatement("UPDATE section SET day=?, start=?, end=?, instructor=?, venue=?, size=? WHERE course =? AND section=?"); 
            pstmt.setInt(1,day); 
              
            java.sql.Time startSqlTime = new java.sql.Time(start.getTime()); 
            pstmt.setTime(2,startSqlTime); 
              
            java.sql.Time endSqlTime = new java.sql.Time(end.getTime()); 
            pstmt.setTime(3,endSqlTime); 
              
            pstmt.setString(4,instructor); 
            pstmt.setString(5,venue); 
            pstmt.setInt(6,size); 
            pstmt.setString(7,courseId); 
            pstmt.setString(8,sectionId); 
            pstmt.executeUpdate(); 
              
        }catch(SQLException e){ 
            e.printStackTrace(); 
        }finally{ 
            ConnectionManager.close(conn, pstmt, rs); 
        } 
          
    } 
  
    //remove a particular section  
    /**
     * Remove a specific row of data from database
     * @param courseId
     * @param sectionId 
     */
    public static void remove(String courseId, String sectionId) { 
      
        Connection conn = null; 
        PreparedStatement pstmt = null; 
        ResultSet rs = null; 
        try{ 
            conn = ConnectionManager.getConnection(); 
            pstmt = conn.prepareStatement("DELETE FROM section WHERE course =? AND section=?"); 
            pstmt.setString(1,courseId); 
            pstmt.setString(2,sectionId); 
            pstmt.executeUpdate(); 
              
        }catch(SQLException e){ 
            e.printStackTrace(); 
        }finally{ 
            ConnectionManager.close(conn, pstmt, rs); 
        } 
          
    } 
      
      
} 