package com.app.model;

import com.google.gson.annotations.SerializedName;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


/**
 * Course object class
 * 
 */
public class Course {
    private String course;
    private String school;
    private String title;
    private String description;
    private transient Date examDate;
    @SerializedName("exam date") private String examDateStr;
    private transient Date examStart;
    @SerializedName("exam start") private String examStartStr;
    private transient Date examEnd;
    @SerializedName("exam end") private String examEndStr;
    private transient ArrayList<String> prerequisites;
    
    /**
     * FOR JSON SERIALIZATION PURPOSES.
     */
    private Course() {
    }
    
    /**
     * For constructing Courses with no Prerequisites parameter.
     * @param course courseId of the course
     * @param school name of the school that offers the course
     * @param title course title
     * @param description description of course
     * @param examDate the date of the exam in DDMMYY
     * @param examStart start time of the exam
     * @param examEnd end time of the exam
     */
    public Course(String course, String school, String title, String description, Date examDate, Date examStart, Date examEnd) {
        this.course = course;
        this.school = school;
        this.title = title;
        this.description = description;
        this.examDate = examDate;
        this.examStart = examStart;
        this.examEnd = examEnd;
    }
    
    /**
     *  Create a constructor with the following
     * @param dumpFormat
     * @param course courseId of the course
     * @param school name of the school that offers the course
     * @param title course title
     * @param description description of course
     * @param examDate the date of the exam in DDMMYY
     * @param examStart start time of the exam
     * @param examEnd end time of the exam
     */
    public Course(boolean dumpFormat, String course, String school, String title, String description, Date examDate, Date examStart, Date examEnd) {
        this.course = course;
        this.school = school;
        this.title = title;
        this.description = description;
        this.examDate = examDate;
        this.examStart = examStart;
        this.examEnd = examEnd;
        
        if (dumpFormat) {
            SimpleDateFormat sdfDate = new SimpleDateFormat("yyyyMMdd");
            SimpleDateFormat sdfTime = new SimpleDateFormat("Hmm");
            
            this.examDateStr = sdfDate.format(examDate);
            this.examStartStr = sdfTime.format(examStart);
            this.examEndStr = sdfTime.format(examEnd);
        }
        
    }
    
    
    /**
     *
     * Creates a Course with the following
     * @param courseCode the courseCode of the course
     * @param school which school offers the course
     * @param title title of the course
     * @param description description of the course
     * @param examDate the date of the exam in DDMMYY
     * @param examStart start time of the exam
     * @param examEnd end time of the exam
     * @param prerequisites the prerequisites of the course
     */
    public Course(String courseCode, String school, String title, String description, Date examDate, Date examStart, Date examEnd, ArrayList<String> prerequisites) {
        this.course = courseCode;
        this.examDate = examDate;
        this.description = description;
        this.school = school;
        this.title = title;
        this.prerequisites = prerequisites;
        this.examStart = examStart;
        this.examEnd = examEnd;
    }

    /**
     * Gets the course of the course
     * @return the course 
     */
    public String getCourse() {
        return course;
    }

    /**
     * Sets the course of the course
     * @param course the course code of the course
     */
    public void setCourse(String course) {
        this.course = course;
    }

    /**
     * Gets the examDate for the course
     * @return examDate for the course
     */
    public Date getExamDate() {
        return examDate;
    }

    /**
     * Sets the examDate for the course
     * @param examDate the examDate for the course
     */
    public void setExamDate(Date examDate) {
        this.examDate = examDate;
    }

    /**
     * Gets the description of the course
     * @return description of the course
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of the course
     * @param description of the course
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets the school which offers the course
     * @return the school which offers the course
     */
    public String getSchool() {
        return school;
    }

    /**
     * Sets the school which offers the course
     * @param school the school offering
     */
    public void setSchool(String school) {
        this.school = school;
    }

    /**
     * Gets the title of the course
     * @return title of the course
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title of the course
     * @param title the tile of the course
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets the prerequisite of the courses
     * @return the prerequisites of the courses
     */
    public ArrayList<String> getPrerequisites() {
        return prerequisites;
    }

    /**
     * Sets the prerequisite of the course
     * @param prerequisites 
     */
    public void setPrerequisites(ArrayList<String> prerequisites) {
        this.prerequisites = prerequisites;
    }

    /**
     * Gets the examStart time of the course
     * @return examStart time of the course
     */
    public Date getExamStart() {
        return examStart;
    }

    /**
     * Sets the examStart time of the course
     * @param examStart time of the course
     */
    public void setExamStart(Date examStart) {
        this.examStart = examStart;
    }

    /**
     * Gets the examEnd time of the course
     * @return examEnd time of the course
     */
    public Date getExamEnd() {
        return examEnd;
    }

    /**
     * Sets the examEnd time of the course
     * @param examEnd of the course
     */
    public void setExamEnd(Date examEnd) {
        this.examEnd = examEnd;
    }
        
        @Override
    public boolean equals(Object obj) {
        
        if (!(obj instanceof Course)) {
            return false;
        }
        
        Course otherCourse = (Course)obj;
        
        return course.equals(otherCourse.getCourse()) && school.equals(otherCourse.getSchool())
                && title.equals(otherCourse.getTitle()) && description.equals(otherCourse.getDescription())
                && examDate.equals(otherCourse.getExamDate()) && examStart.equals(otherCourse.getExamStart())
                && examEnd.equals(otherCourse.getExamEnd());
    }
}
