package com.app.model; 
  
import com.google.gson.annotations.SerializedName;
import java.math.BigDecimal; 
import java.util.ArrayList; 
  
/** 
 * Represents a student with eDollars, userId, password, school, name and course_completed 
 */
  
  
public class Student { 
      
    @SerializedName("userid") private String userId; 
    private String password;
    private String name;  
    private String school; 
    @SerializedName("edollar") private BigDecimal eDollars; 
    private transient ArrayList<String> courseCompleted; //"transient" so that it will not be serialized
    private transient BigDecimal excessEDollars;
      
    /**
     * FOR JSON SERIALIZATION PURPOSES.
     */
    private Student() {
    }

    /**
     * For constructing Courses with no courseCompleted parameter. Used by DumpTableServlet
     * @param userId
     * @param password
     * @param name
     * @param school
     * @param eDollars 
     */
    public Student(String userId, String password, String name, String school, BigDecimal eDollars) {
        this.userId = userId;
        this.password = password;
        this.name = name;
        this.school = school;
        this.eDollars = eDollars;
    }

    
    
    
    
    
/** 
 * Creates a Student Object with the specified eDollar, userId, password, school and name 
 * @param eDollars the eDollar that the student has 
 * @param username the username which the student use to login 
 * @param password the password which the student use to login 
 * @param school the school the student is from 
 * @param name the Student's name 
 * @param courseCompleted arraylist of courses that the student have completed 
 */
      
    public Student(String userId, String name, String password, BigDecimal eDollars, String school) { 
          
        this.userId = userId; 
        this.password = password; 
        this.name = name; 
        this.school = school; 
        this.eDollars = eDollars; 
    } 
  
      
    public Student(String userId, String name, String password, BigDecimal eDollars, String school, ArrayList<String> courseCompleted) { 
        this.userId = userId; 
        this.password = password; 
        this.name = name; 
        this.school = school; 
        this.eDollars = eDollars; 
        this.courseCompleted = courseCompleted; 
    } 

    public Student(String userId, String password, String name, String school, BigDecimal eDollars, BigDecimal excessEDollars) {
        this.userId = userId;
        this.password = password;
        this.name = name;
        this.school = school;
        this.eDollars = eDollars;
        this.excessEDollars = excessEDollars;
    }
    
    
/** 
 * Gets the eDollars of the student 
 * @return the eDollars of the student 
 */
    public BigDecimal getEDollars() { 
        return eDollars; 
    } 
      
/** 
 * Sets the eDollars of the student 
 * @param eDollars the student's eDollars 
 */
  
    public void setEDollars(BigDecimal eDollars) { 
        this.eDollars = eDollars; 
    } 
      
/** 
 * Gets the username of the student 
 * @return the username of the student 
 */
      
    public String getUserId() { 
        return userId; 
    } 
  
/** 
 * Sets the username of the student 
 * @param username the student's userId 
 */    
      
    public void setUserId(String userId) { 
        this.userId = userId; 
    } 
      
/** 
 * Gets the password of the student 
 * @return the password of the student 
 */
      
    public String getPassword() { 
        return password; 
    } 
  
/** 
 * Sets the password of the student 
 * @param password the student's password 
 */    
      
    public void setPassword(String password) { 
        this.password = password; 
    } 
      
/** 
 * Gets the school of the student 
 * @return the school of the student 
 */
          
    public String getSchool() { 
        return school; 
    } 
  
/** 
 * Sets the school of the student 
 * @param school the student's school 
 */
      
    public void setSchool(String school) { 
        this.school = school; 
    } 
  
/** 
 * Gets the name of the student 
 * @return the name of the student 
 */
  
    public String getName() { 
        return name; 
    } 
  
/** 
 * Sets the name of the student 
 * @param name the student's name 
 */
      
    public void setName(String name) { 
        this.name = name; 
    } 
  
 /** 
 * Gets arraylist of course that the student have completed 
 * @return the arraylist of course that the student have completed 
 */
      
    public ArrayList<String> getCourseCompleted() { 
        return courseCompleted; 
    }  

    /**
     * Retrieve the number of excess eDollars
     * @return excess eDollars
     */
    public BigDecimal getExcessEDollars() {
        return excessEDollars;
    }

    /**
     * Set the amount of excess eDollars
     * @param excessEDollars 
     */
    public void setExcessEDollars(BigDecimal excessEDollars) {
        this.excessEDollars = excessEDollars;
    }
      
} 