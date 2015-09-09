package com.app.model; 
  
import static com.app.model.StudentDAO.retrieveCourseCompleted;
import com.app.utility.ConnectionManager; 
import java.math.BigDecimal; 
import java.sql.*; 
import java.util.ArrayList; 
  
public class StudentDAO { 
       
    /**
     * Retrieve all course completed by all student in order 
     * @return ArrayList of Completed Courses for ALL students. Returns an empty ArrayList when there are no completed courses
     */
    public static ArrayList<CompletedCourse> retrieveCourseCompletedOfAllStudentsOrdered(){ 
        ArrayList<CompletedCourse> result = new ArrayList<CompletedCourse>(); 

        Connection conn = null; 
        PreparedStatement pstmt = null; 
        ResultSet rs = null; 

        try{ 
            conn = ConnectionManager.getConnection(); 
            pstmt = conn.prepareStatement("select * from course_completed ORDER BY course, user_id"); 

            rs = pstmt.executeQuery(); 
            while (rs.next()) { 
                result.add(new CompletedCourse(rs.getString("user_id"),rs.getString("course"))); 
            } 
        } catch(SQLException e){ 
            e.printStackTrace(); 
        }finally{ 
            ConnectionManager.close(conn, pstmt, rs); 
        } 
          
        return result; 
    } 
    
    /**
     * Retrieve course completed by specific student
     * @param userId
     * @return ArrayList of courses completed by the student of userid
     */
    public static ArrayList<String> retrieveCourseCompleted(String userId){ 
        ArrayList<String> result = new ArrayList<String>(); 

        Connection conn = null; 
        PreparedStatement pstmt = null; 
        ResultSet rs = null; 

        try{ 
            conn = ConnectionManager.getConnection(); 
            pstmt = conn.prepareStatement("select * from course_completed where user_id=?"); 
            pstmt.setString(1,userId); 
            rs = pstmt.executeQuery(); 
            while (rs.next()) { 
                result.add(rs.getString("course")); 
            } 
        } catch(SQLException e){ 
            e.printStackTrace(); 
        }finally{ 
            ConnectionManager.close(conn, pstmt, rs); 
        } 
          
        return result; 
    } 
      
    //retrieve list of all students 
    /**
     * Retrieve all Student from database
     * @return list of students
     */
    public static ArrayList<Student> retrieveAll(){ 
            ArrayList<Student> result = new ArrayList<Student>(); 
            Connection conn = null; 
            PreparedStatement pstmt = null; 
            ResultSet rs = null; 
          
            try{ 
        conn = ConnectionManager.getConnection(); 
                pstmt = conn.prepareStatement("SELECT * FROM student"); 
                rs = pstmt.executeQuery(); 
                while (rs.next()) {      
                    String userid = rs.getString("user_id"); 
                    String name = rs.getString("name"); 
                    String password = rs.getString("password"); 
                    BigDecimal edollar = rs.getBigDecimal("edollar"); 
                    String school = rs.getString("school"); 
                                  
                    ArrayList<String> courseCompleted = retrieveCourseCompleted(userid); 
                    result.add(new Student(userid, name, password,edollar, school,  courseCompleted)); 
        } 
        }catch(SQLException e){ 
            e.printStackTrace(); 
        }finally{ 
            ConnectionManager.close(conn, pstmt, rs); 
        } 
        return result; 
    } 
    
    //retrieve list of all students 
    /**
     * retrieve list of all students 
     * @return list of students
     */
    public static ArrayList<Student> retrieveAllNoCourseCompletedOrdered(){ 
            ArrayList<Student> result = new ArrayList<Student>(); 
            Connection conn = null; 
            PreparedStatement pstmt = null; 
            ResultSet rs = null; 
          
            try{ 
        conn = ConnectionManager.getConnection(); 
                pstmt = conn.prepareStatement("SELECT * FROM student ORDER BY user_id"); 
                rs = pstmt.executeQuery(); 
                while (rs.next()) {      
                    String userid = rs.getString("user_id"); 
                    String name = rs.getString("name"); 
                    String password = rs.getString("password"); 
                    BigDecimal edollar = rs.getBigDecimal("edollar"); 
                    
                    try {
                    edollar = edollar.setScale(1);
                } catch (ArithmeticException ae) {
                    //means it is 2 dp, so just leave it.
                }
                    
                    String school = rs.getString("school"); 
                                   
                    result.add(new Student(userid, password, name,school, edollar)); 
        } 
        }catch(SQLException e){ 
            e.printStackTrace(); 
        }finally{ 
            ConnectionManager.close(conn, pstmt, rs); 
        } 
        return result; 
    }
      
    /**
     * Retrieve specific student by their user id
     * @param userId
     * @return Student with matching userId as the argument. Returns null if student does not exist.
     */
    public static Student retrieveById(String userId){ 
            Student theStudent = null; 
            Connection conn = null; 
            PreparedStatement pstmt = null; 
            ResultSet rs = null; 
          
            try{ 
                conn = ConnectionManager.getConnection(); 
                pstmt = conn.prepareStatement("select * from student where user_id=?"); 
                pstmt.setString(1,userId); 
                rs = pstmt.executeQuery(); 
                while (rs.next()) { 
                    String userid = rs.getString("user_id"); 
                    String name = rs.getString("name"); 
                    String password = rs.getString("password"); 
                    BigDecimal edollar = rs.getBigDecimal("edollar"); 
                    String school = rs.getString("school"); 
                                  
                    ArrayList<String> courseCompleted = retrieveCourseCompleted(userid); 
                    
                    //if this is not checked, student obj with null values will be created.
                    if (userid != null) {
                        theStudent = new Student(userid, name, password,edollar, school,  courseCompleted); 
                    }
                 } 
            }catch(SQLException e){ 
        e.printStackTrace(); 
            }finally{ 
        ConnectionManager.close(conn, pstmt, rs); 
            } 
      
            return theStudent; 
    } 
    
    /**
     * Retrieve specific student by their id in JSON
     * @param userId
     * @return Student object
     */
    public static Student retrieveByIdJson(String userId){ 
            Student theStudent = null; 
            Connection conn = null; 
            PreparedStatement pstmt = null; 
            ResultSet rs = null; 
          
            try{ 
                conn = ConnectionManager.getConnection(); 
                pstmt = conn.prepareStatement("select * from student where user_id=?"); 
                pstmt.setString(1,userId); 
                rs = pstmt.executeQuery(); 
                while (rs.next()) { 
                    String userid = rs.getString("user_id"); 
                    String name = rs.getString("name"); 
                    String password = rs.getString("password"); 
                    BigDecimal edollar = rs.getBigDecimal("edollar"); 
                    
                    try {
                        edollar = edollar.setScale(1);
                    } catch (ArithmeticException e) {
                        
                    }
                    
                    String school = rs.getString("school"); 
                                  
                    ArrayList<String> courseCompleted = retrieveCourseCompleted(userid); 
                    
                    //if this is not checked, student obj with null values will be created.
                    if (userid != null) {
                        theStudent = new Student(userid, name, password,edollar, school,  courseCompleted); 
                    }
                 } 
            }catch(SQLException e){ 
        e.printStackTrace(); 
            }finally{ 
        ConnectionManager.close(conn, pstmt, rs); 
            } 
      
            return theStudent; 
    } 
    
    /**
     * Retrieve excess eDollar by specific student
     * @param userId
     * @return Student with matching userId as the argument. Returns null if student does not exist.
     */
    public static BigDecimal retrieveByIdExcessEDollars(String userId){ 
            BigDecimal excessEDollars = null; 
            Connection conn = null; 
            PreparedStatement pstmt = null; 
            ResultSet rs = null; 
          
            try{ 
                conn = ConnectionManager.getConnection(); 
                pstmt = conn.prepareStatement("select excess_edollar from student where user_id=?"); 
                pstmt.setString(1,userId); 
                rs = pstmt.executeQuery(); 
                
                while (rs.next()) { 
                    excessEDollars = rs.getBigDecimal("excess_edollar");  
                    if (excessEDollars == null) {
                        excessEDollars = BigDecimal.ZERO;
                    }
                 } 
            }catch(SQLException e){ 
        e.printStackTrace(); 
            }finally{ 
        ConnectionManager.close(conn, pstmt, rs); 
            } 
      
            return excessEDollars;
    } 
      
    //Add new student along with the course completed 
    /**
     * Add new student along with the course completed
     * @param userId
     * @param password
     * @param name
     * @param school
     * @param eDollars
     * @param courseCompleted 
     */
    public static void add(String userId, String password, String name, String school, BigDecimal eDollars, ArrayList<String> courseCompleted) { 
            Connection conn = null; 
            PreparedStatement pstmt = null; 
            ResultSet rs = null; 
            try { 
                conn = ConnectionManager.getConnection(); 
                String sqlstatement = "INSERT INTO student (user_id,password,name,school,edollar) VALUES(?,?,?,?,?)"; 
                pstmt = conn.prepareStatement(sqlstatement); 
                pstmt.setString(1,userId); 
        pstmt.setString(2,password); 
        pstmt.setString(3,name); 
        pstmt.setString(4,school); 
        pstmt.setBigDecimal(5,eDollars); 
        pstmt.executeUpdate(sqlstatement); 
                  
                for(String course : courseCompleted){ 
                    sqlstatement = "INSERT INTO course_completed (user_id,course) VALUES(?,?)"; 
                    pstmt = conn.prepareStatement(sqlstatement); 
                    pstmt.setString(1,userId); 
                    pstmt.setString(2,course); 
                    pstmt.executeUpdate(); 
        } 
            } catch (SQLException e) { 
                e.printStackTrace(); 
            } finally { 
                ConnectionManager.close(conn, pstmt, rs); 
            } 
          
    } 
      
    //Update student information 
    /**
     * Update student information 
     * @param userId
     * @param password
     * @param name
     * @param school
     * @param eDollars
     * @param courseCompleted 
     */
    public static void update(String userId,String password, String name, String school, BigDecimal eDollars, ArrayList<String> courseCompleted) { 
        Connection conn = null; 
        PreparedStatement pstmt = null; 
        ResultSet rs = null; 
  
        try { 
            conn = ConnectionManager.getConnection(); 
            String sqlstatement = "UPDATE student SET password=?, name=?, school=?, edollar=? WHERE user_id=?"; 
                          
            pstmt = conn.prepareStatement(sqlstatement); 
            pstmt.setString(1,password); 
            pstmt.setString(2,name); 
            pstmt.setString(3,school); 
            pstmt.setBigDecimal(4,eDollars); 
            pstmt.setString(5,userId); 
            pstmt.executeUpdate();
            //update course completed 
            for(String course : courseCompleted){ 
                sqlstatement = "INSERT INTO course_completed (user_id,course) VALUES(?,?)"; 
                pstmt = conn.prepareStatement(sqlstatement); 
                pstmt.setString(1,userId); 
                pstmt.setString(2, course); 
                pstmt.executeQuery(); 
            } 
        }catch(SQLException e){ 
            e.printStackTrace(); 
        }finally{ 
            ConnectionManager.close(conn, pstmt, rs); 
        } 
    } 
    /**
     * Update student eDollar in database
     * @param userid
     * @param eDollars 
     */
    public static void updateEDollars(String userid, BigDecimal eDollars) {
        Connection conn = null; 
        PreparedStatement pstmt = null; 
        ResultSet rs = null; 
  
        try { 
            conn = ConnectionManager.getConnection(); 
            String sqlstatement = "UPDATE student SET edollar=? WHERE user_id=?"; 
                          
            pstmt = conn.prepareStatement(sqlstatement); 
            
            pstmt.setBigDecimal(1,eDollars); 
            pstmt.setString(2,userid); 
            pstmt.executeUpdate();
  
        }catch(SQLException e){ 
            e.printStackTrace(); 
        }finally{ 
            ConnectionManager.close(conn, pstmt, rs); 
        } 
    }
    
    /**
     * Debit EDollars from all the students in the specified bidList
     * @param bidList 
     */
    public static void batchUpdateEDollars(ArrayList<Student> studentList) {
        Connection conn = null; 
        PreparedStatement pstmt = null; 
        ResultSet rs = null; 
  
        try { 
            conn = ConnectionManager.getConnection(); 
            String sqlstatement = "UPDATE student SET edollar=? WHERE user_id=?"; 

            pstmt = conn.prepareStatement(sqlstatement); 
            
            int batchSize = 0;
            for (Student student : studentList) {
                BigDecimal eDollars = student.getEDollars();
                String userid = student.getUserId();

                pstmt.setBigDecimal(1,eDollars); 
                pstmt.setString(2,userid); 
                
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
     * Update the excess of eDollar
     * @param userid
     * @param excessDollars 
     */
      public static void updateExcessDollars(String userid, BigDecimal excessDollars) {
        Connection conn = null; 
        PreparedStatement pstmt = null; 
        ResultSet rs = null; 
        
        try { 
            conn = ConnectionManager.getConnection(); 
            String sqlstatement = "UPDATE student SET excess_edollar=? WHERE user_id=?"; 
                          
            pstmt = conn.prepareStatement(sqlstatement); 
            
            pstmt.setBigDecimal(1,excessDollars); 
            pstmt.setString(2,userid); 
            pstmt.executeUpdate();
  
        }catch(SQLException e){ 
            e.printStackTrace(); 
        }finally{ 
            ConnectionManager.close(conn, pstmt, rs); 
        } 
    }
      /**
       * Refund student eDollar 
       */
    public static void refundExcessDollars() {
        Connection conn = null; 
        PreparedStatement pstmt = null; 
        ResultSet rs = null;
        
        try { 
            conn = ConnectionManager.getConnection();
            
            conn.setAutoCommit(false);
            
            pstmt = conn.prepareStatement("SELECT user_id, edollar, excess_edollar FROM student FOR UPDATE");
            rs = pstmt.executeQuery();
            
            
            while (rs.next()) {
                String userid = rs.getString("user_id");
                BigDecimal balance = rs.getBigDecimal("edollar");
                BigDecimal excessDollars = rs.getBigDecimal("excess_edollar");
                
                if(excessDollars != null) {
                    balance = balance.add(excessDollars);
                    System.out.println(excessDollars);
                    
                    String sqlstatement = "UPDATE student SET edollar=? WHERE user_id=?"; 
                    pstmt = conn.prepareStatement(sqlstatement); 

                    pstmt.setBigDecimal(1,balance);
                    pstmt.setString(2,userid); 
                    pstmt.executeUpdate();
                    
                    pstmt.close();
                    
                    pstmt = conn.prepareStatement("UPDATE student SET excess_edollar=? WHERE user_id=?");
                    pstmt.setBigDecimal(1,BigDecimal.ZERO);
                    pstmt.setString(2, userid);
                    pstmt.executeUpdate();
                    pstmt.close();
                }
                
                
            }
            
            conn.commit();
  
        }catch(SQLException e){ 
            e.printStackTrace(); 
        }finally{ 
            ConnectionManager.close(conn, pstmt, rs); 
        }
    }
    
    //remove the student with the userId 
    /**
     * Remove a specific row in database
     * @param userId 
     */
    public static void remove(String userId) { 
            Connection conn = null; 
            PreparedStatement pstmt = null; 
            ResultSet rs = null; 
          
            try{ 
        conn = ConnectionManager.getConnection(); 
        String sqlstatement = "DELETE FROM student WHERE user_id =?"; 
        pstmt = conn.prepareStatement(sqlstatement); 
        pstmt.setString(1,userId); 
        pstmt.executeUpdate(); 
            }catch(SQLException e){ 
        e.printStackTrace(); 
            }finally{ 
        ConnectionManager.close(conn, pstmt, rs); 
            } 
          
          
    } 
      
    //validate student userId and password 
    /**
     * Authenticate student to check their validity
     * @param userId
     * @param password
     * @return 
     */
    public static boolean authenticateStudent(String userId, String password){ 
            Connection conn = null; 
            PreparedStatement pstmt = null; 
            ResultSet rs = null; 
            boolean valid = false;
            try{ 
                conn = ConnectionManager.getConnection(); 
                String sqlstatement = "SELECT name FROM student where user_id=? and password=?"; 
                pstmt = conn.prepareStatement(sqlstatement); 
                pstmt.setString(1,userId); 
                pstmt.setString(2,password); 
                rs = pstmt.executeQuery(); 

                if(rs.next()){ 
                    valid = true;
                } 
            }catch(SQLException e){ 
                 e.printStackTrace(); 
            }finally{  
                 ConnectionManager.close(conn, pstmt, rs); 
            } 
            return valid;
    } 
} 
