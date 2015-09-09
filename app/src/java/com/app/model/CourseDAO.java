package com.app.model;

/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
*/

import com.app.utility.ConnectionManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
* The CourseDAO which has all the methods and manages all of the courses
*  
*/

public class CourseDAO {

    /**
     *retrieve list of prerequisites
     * @param courseCode
     * @return
     */
    public static ArrayList<String> retrievePrerequisites(String courseCode){
		ArrayList<String> result = new ArrayList<String>();
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try{
			conn = ConnectionManager.getConnection();
			pstmt = conn.prepareStatement("SELECT * from prerequisite WHERE course=?");
			pstmt.setString(1,courseCode);
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				result.add(rs.getString("prerequisite"));
			}
		}catch(SQLException e){
			e.printStackTrace();
		}finally{
			ConnectionManager.close(conn, pstmt, rs);
		}
		
		return result;
	}
        
        /**
         *  Retrieve an ArrayList<Prerequisite> 
         * @return all Prerequisite objects in the database in an ArrayList
         */
        public static ArrayList<Prerequisite> retrievePrerequisitesOfAllCoursesOrdered(){
		ArrayList<Prerequisite> result = new ArrayList<Prerequisite>();
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try{
			conn = ConnectionManager.getConnection();
			pstmt = conn.prepareStatement("SELECT * from prerequisite ORDER BY course, prerequisite");
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				result.add(new Prerequisite(rs.getString("course"), rs.getString("prerequisite")));
			}
		}catch(SQLException e){
			e.printStackTrace();
		}finally{
			ConnectionManager.close(conn, pstmt, rs);
		}
		
		return result;
	}
	
    /**
     *retrieve list of all course
     * @return ArrayList<Course> of all the courses
     */
    public static ArrayList<Course> retrieveAll(){
		ArrayList<Course> result = new ArrayList<Course>();
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try{
			conn = ConnectionManager.getConnection();
			pstmt = conn.prepareStatement("SELECT * FROM course");
			rs = pstmt.executeQuery();
			while (rs.next()) {
                            String courseCode = rs.getString("course");
                            String school = rs.getString("school");
                            String title = rs.getString("title");
                            String description = rs.getString("description");
                            java.util.Date examDate = new java.util.Date(rs.getDate("exam_date").getTime());
                            java.util.Date examStart = new java.util.Date(rs.getTime("exam_start").getTime());
                            java.util.Date examEnd = new java.util.Date(rs.getTime("exam_end").getTime());
                            ArrayList<String> prerequisites = retrievePrerequisites(courseCode);
			
                            result.add(new Course(courseCode,school,title,description,examDate,examStart,examEnd,prerequisites));
			}
		}catch(SQLException e){
			e.printStackTrace();
		}finally{
			ConnectionManager.close(conn, pstmt, rs);
		}
		
		return result;
	}
        
    /**
     * retrieve list of all course of the specified school
     * @param schoolStr the school
     * @return an ArrayList<Courses> offered by the school
     */
    public static ArrayList<Course> retrieveBySchool(String schoolStr){
		ArrayList<Course> result = new ArrayList<Course>();
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try{
			conn = ConnectionManager.getConnection();
			pstmt = conn.prepareStatement("SELECT * FROM course WHERE school=?");
                        pstmt.setString(1,schoolStr);
                        
			rs = pstmt.executeQuery();
			while (rs.next()) {
                            String courseCode = rs.getString("course");
                            String school = rs.getString("school");
                            String title = rs.getString("title");
                            String description = rs.getString("description");
                            java.util.Date examDate = new java.util.Date(rs.getDate("exam_date").getTime());
                            java.util.Date examStart = new java.util.Date(rs.getTime("exam_start").getTime());
                            java.util.Date examEnd = new java.util.Date(rs.getTime("exam_end").getTime());
                            ArrayList<String> prerequisites = retrievePrerequisites(courseCode);
			
                            result.add(new Course(courseCode,school,title,description,examDate,examStart,examEnd,prerequisites));
			}
		}catch(SQLException e){
			e.printStackTrace();
		}finally{
			ConnectionManager.close(conn, pstmt, rs);
		}
		
		return result;
	}
        
     /**
     * Retrieve all courses from database
     * @return ArrayList<Course> 
     */
    public static ArrayList<Course> retrieveAllNoPrerequisitesOrdered(){
		ArrayList<Course> result = new ArrayList<Course>();
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try{
			conn = ConnectionManager.getConnection();
			pstmt = conn.prepareStatement("SELECT * FROM course ORDER BY course");
			rs = pstmt.executeQuery();
			while (rs.next()) {
                            String courseCode = rs.getString("course");
                            String school = rs.getString("school");
                            String title = rs.getString("title");
                            String description = rs.getString("description");
                            java.util.Date examDate = new java.util.Date(rs.getDate("exam_date").getTime());
                            java.util.Date examStart = new java.util.Date(rs.getTime("exam_start").getTime());
                            java.util.Date examEnd = new java.util.Date(rs.getTime("exam_end").getTime());
			
                            result.add(new Course(courseCode,school,title,description,examDate,examStart,examEnd));
			}
		}catch(SQLException e){
			e.printStackTrace();
		}finally{
			ConnectionManager.close(conn, pstmt, rs);
		}
		
		return result;
	}
        
     /**
     * retrieve all course for JSON purpose
     * @return ArrayList<Course>
     */
    public static ArrayList<Course> retrieveAllNoPrerequisitesJsonOrdered(){
		ArrayList<Course> result = new ArrayList<Course>();
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try{
			conn = ConnectionManager.getConnection();
			pstmt = conn.prepareStatement("SELECT * FROM course ORDER BY course");
			rs = pstmt.executeQuery();
			while (rs.next()) {
                            String courseCode = rs.getString("course");
                            String school = rs.getString("school");
                            String title = rs.getString("title");
                            String description = rs.getString("description");
                            java.util.Date examDate = new java.util.Date(rs.getDate("exam_date").getTime());
                            java.util.Date examStart = new java.util.Date(rs.getTime("exam_start").getTime());
                            java.util.Date examEnd = new java.util.Date(rs.getTime("exam_end").getTime());
			
                            result.add(new Course(true,courseCode,school,title,description,examDate,examStart,examEnd));
			}
		}catch(SQLException e){
			e.printStackTrace();
		}finally{
			ConnectionManager.close(conn, pstmt, rs);
		}
		
		return result;
	}
        
    /**
     *retrieve Map of Course obj with course code as key.
     * @return HashMap of course obj 
     */
    public static HashMap<String, Course> retrieveMap() {
            HashMap<String,Course> courseMap = new HashMap<String,Course>();
            ArrayList<Course> courses = retrieveAll();
            
            for (Course course : courses) {
                courseMap.put(course.getCourse(),course);
            }
            
            return courseMap;
        }
	
    /**
     * retrieve a list of course with the same title
     * @param courseTitle the course title
     * @return an ArrayList<Course> with the particular course title
     */
    public static ArrayList<Course> retrieveByTitle(String courseTitle){
		ArrayList<Course> result = new ArrayList<Course>();
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try{
			conn = ConnectionManager.getConnection();
			pstmt = conn.prepareStatement("SELECT * FROM course WHERE title =?");
			pstmt.setString(1,courseTitle);
			rs = pstmt.executeQuery();
			while (rs.next()) {
                            String courseCode = rs.getString("course");
                            String school = rs.getString("school");
                            String title = rs.getString("title");
                            String description = rs.getString("description");
                            java.util.Date examDate = new java.util.Date(rs.getDate("exam_date").getTime());
                            java.util.Date examStart = new java.util.Date(rs.getTime("exam_start").getTime());
                            java.util.Date examEnd = new java.util.Date(rs.getTime("exam_end").getTime());
                            ArrayList<String> prerequisites = retrievePrerequisites(courseCode);
			
                            result.add(new Course(courseCode,school,title,description,examDate,examStart,examEnd,prerequisites));
			}
		}catch(SQLException e){
			e.printStackTrace();
		}finally{
			ConnectionManager.close(conn, pstmt, rs);
		}
		
		return result;
	}
	
    /**
     * retrieve course of a particular course code
     * @param code the course code of the course
     * @return the Course with the particular course code
     */
    public static Course retrieveByCode(String code){
		Course theCourse = null;
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try{
			conn = ConnectionManager.getConnection();
			pstmt = conn.prepareStatement("SELECT * FROM course WHERE course =?");
			pstmt.setString(1,code);
			rs = pstmt.executeQuery();
			while (rs.next()) {
                            String courseCode = rs.getString("course");
                            String school = rs.getString("school");
                            String title = rs.getString("title");
                            String description = rs.getString("description");
                            java.util.Date examDate = new java.util.Date(rs.getDate("exam_date").getTime());
                            java.util.Date examStart = new java.util.Date(rs.getTime("exam_start").getTime());
                            java.util.Date examEnd = new java.util.Date(rs.getTime("exam_end").getTime());
                            ArrayList<String> prerequisites = retrievePrerequisites(courseCode);
                            
                            theCourse = new Course(courseCode,school,title,description,examDate,examStart,examEnd,prerequisites);
			}
		}catch(SQLException e){
			e.printStackTrace();
		}finally{
			ConnectionManager.close(conn, pstmt, rs);
		}
		
		return theCourse;
	}
	
    /**
     * Adds new Course
     * @param courseCode courseCode of the course
     * @param school school that offers this course
     * @param title title of the course
     * @param description the description of the course
     * @param examDate the exam date in DDMMYY
     * @param examStart the starting time of the exam
     * @param examEnd the ending time of the exam
     * @param prerequisites the prerequisites before qualified to take the course
     */
    public static void add(String courseCode, String school, String title, String description, java.util.Date examDate, java.util.Date examStart, java.util.Date examEnd, ArrayList<String> prerequisites) {
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sqlstatement = null;
		try{
                    conn = ConnectionManager.getConnection();
                    pstmt = conn.prepareStatement("INSERT INTO Course (course,school,title,description,exam_date,exam_start,exam_end) VALUES(?,?,?,?,?,?,?)");
                    pstmt.setString(1,courseCode);
                    pstmt.setString(2,school);
                    pstmt.setString(3,title);
                    pstmt.setString(4,description);
                    java.sql.Date examSqlDate = new java.sql.Date(examDate.getTime());
                    java.sql.Time examSqlStart = new java.sql.Time(examStart.getTime());
                    java.sql.Time examSqlEnd =  new java.sql.Time(examEnd.getTime());

                    pstmt.setDate(5,examSqlDate);
                    pstmt.setTime(6,examSqlStart);
                    pstmt.setTime(7,examSqlEnd);
                    pstmt.executeUpdate();
			
			for(String thePrerequisite : prerequisites){
				sqlstatement = "INSERT INTO prerequisite (course,prerequisite) VALUES(?,?)";
				pstmt = conn.prepareStatement(sqlstatement);
				pstmt.setString(1,courseCode);
				pstmt.setString(2,thePrerequisite);
				pstmt.executeUpdate();
			}
			
		}catch(SQLException e){
			e.printStackTrace();
		}finally{
			ConnectionManager.close(conn, pstmt, rs);
		}
		
		
	}
	
    /**
     * Update Course information to the following
     * @param courseCode courseCode of the course
     * @param school school that offers this course
     * @param title title of the course
     * @param description the description of the course
     * @param examDate the exam date in DDMMYY
     * @param examStart the starting time of the exam
     * @param examEnd the ending time of the exam
     * @param prerequisites the prerequisites before qualified to take the course
     */
    public static void update(String courseCode, String school, String title, String description, Date examDate, Date examStart, Date examEnd, ArrayList<String> prerequisites) {
	
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sqlstatement = null;
		try{
                    conn = ConnectionManager.getConnection();
                    pstmt = conn.prepareStatement("UPDATE course SET course=?, school=?, title=?, description=?, exam_date=?, exam_start=?, exam_end =? WHERE course =?");
                    pstmt.setString(1,courseCode);
                    pstmt.setString(2,school);
                    pstmt.setString(3,title);
                    pstmt.setString(4,description);
                    java.sql.Date examSqlDate = new java.sql.Date(examDate.getTime());
                    java.sql.Time examSqlStart = new java.sql.Time(examStart.getTime());
                    java.sql.Time examSqlEnd =  new java.sql.Time(examEnd.getTime());

                    pstmt.setDate(5,examSqlDate);
                    pstmt.setTime(6,examSqlStart);
                    pstmt.setTime(7,examSqlEnd);
                    pstmt.setString(8,courseCode);      //WHERE clause
                    pstmt.executeUpdate();
			
			//check again!!!!! wht if the user wants to delete the prerequisite ?
                        //WE JUST UPDATE. (previous prerequisite WILL BE deleted.)
                        //if user wants to add more prereqs, have a method called addPrequisite()
			for(String thePrerequisite : prerequisites){
                            sqlstatement = "UPDATE prerequisite set (prerequisite) VALUES ('?') WHERE course=?";
                            pstmt = conn.prepareStatement(sqlstatement);
                            pstmt.setString(1,thePrerequisite);
                            pstmt.setString(2,courseCode);
                            pstmt.executeUpdate();
			}
			
		}catch(SQLException e){
			e.printStackTrace();
		}finally{
			ConnectionManager.close(conn, pstmt, rs);
		}
	}

    /**
     * remove a Course with the specific course code
     * @param courseCode the courseCode of the course
     */
    public static void remove(String courseCode) {
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sqlstatement = null;
		try{
			conn = ConnectionManager.getConnection();
			
                        //delete prereq first because it has dependencies.
                        //MySQL will not allow a course to be deleted if it is being used as FK
                        //in prerequisite table
                        sqlstatement = "DELETE FROM prerequisite WHERE course =?";
			pstmt = conn.prepareStatement(sqlstatement);
			pstmt.setString(1,courseCode);
			pstmt.executeUpdate();
                        
                        pstmt = conn.prepareStatement("DELETE FROM section WHERE course =?");
			pstmt.setString(1,courseCode);
			
			pstmt.executeUpdate();
			
		}catch(SQLException e){
			e.printStackTrace();
		}finally{
			ConnectionManager.close(conn, pstmt, rs);
		}
		
	}

    /**
     * remove a particular prerequisite
     * therefore no need to retrieve list
     * @param courseCode the courseCode of the course
     * @param prerequisite the prerequisite to be removed
     */
    public static void removePrerequisite(String courseCode, String prerequisite){
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sqlstatement = null;
		try{
			conn = ConnectionManager.getConnection();
                        
			sqlstatement = "DELETE FROM prerequisite WHERE course = ? AND prerequisite = ?";
				
			pstmt = conn.prepareStatement(sqlstatement);
			pstmt.setString(1,courseCode);
			pstmt.setString(2,prerequisite);
			pstmt.executeUpdate();
			
		}catch(SQLException e){
			e.printStackTrace();
		}finally{
			ConnectionManager.close(conn, pstmt, rs);
		}
	}
}

