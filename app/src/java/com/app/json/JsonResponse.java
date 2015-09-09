/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.app.json;

import com.app.json.counters.Counter;
import com.app.model.Bid;
import com.app.model.CompletedCourse;
import com.app.model.Course;
import com.app.model.Prerequisite;
import com.app.model.Section;
import com.app.model.SectionStudent;
import com.app.model.Student;
import com.google.gson.annotations.SerializedName;
import java.math.BigDecimal;
import java.util.HashMap;
 
/**
 * The wrapper to format a Json Response String output
 * @author Zachery
 */
public class JsonResponse {
    
    // GENERAL/UNIFORM VARIABLES FOR ALL JSON OUTPUTS
    private String status;
    private String[] message;
    
    // variables for bootstrap status
    @SerializedName("num-record-loaded") private Counter[] numRecordLoaded;
    private BootstrapError[] error;
    
    // variables for DUMP TABLE
    private Course[] course;
    private Section[] section;
    private Student[] student;
    private Prerequisite[] prerequisite;  
    private Bid[] bid;
    @SerializedName("completed-course") private CompletedCourse[] completedCourse;
    @SerializedName("section-student") private SectionStudent[] sectionStudent;
    
    //variables for DUMP (User)
    private String userid;
    private String password;
    private String name;
    private String school;
    private BigDecimal edollar;
    
    //variables for START ROUND, (using Integer wrapper so that it is by default initialized to null instead of 0)
    private Integer round;  //null values are not serialize by json. That is what we want.
    
    /**
     * returns the status
     * @return the status
     */
    public String getStatus() {
        return status;
    }
    
    /**
     * returns the message
     * @return an array of message
     */
    public String[] getMessage() {
        return message;
    }
    
    /**
     * returns the current round
     * @return the current round
     */
    public Integer getRound() {
        return round;
    }
    
    //variables for DUMP (Bid)
    private JsonBidObject[] bids;
    
    //variables for DUMP (Section)
    private JsonStudentObject[] students;
    
    public JsonResponse() {
    }
    
    /**
     * constructs a JsonResponse that matches the "Bootstrap Status" output
     * @param status
     * @param numRecordLoaded
     * @param error
     */
    public JsonResponse(String status, Counter[] numRecordLoaded, BootstrapError[] error) {
        this.status = status;
        this.numRecordLoaded = numRecordLoaded;
        this.error = error;
    }
    
    /**
     * constructs a JsonResponse that matches the "Dump Table" output
     * @param status 
     * @param course
     * @param section
     * @param student
     * @param prerequisite
     * @param bid
     * @param completedCourse
     * @param sectionStudent 
     */
    public JsonResponse(String status, Course[] course, Section[] section, Student[] student,
            Prerequisite[] prerequisite, Bid[] bid, CompletedCourse[] completedCourse, SectionStudent[] sectionStudent) {
        this.status = status;
        this.course = course;
        this.section = section;
        this.student = student;
        this.prerequisite = prerequisite;       
        this.bid = bid;  
        this.completedCourse = completedCourse;
        this.sectionStudent = sectionStudent;
    }
    
    /**
     * constructs a JsonResponse that only shows status
     * @param status 
     */
    public JsonResponse(String status) {
        this.status = status;
    }

    /**
     * constructs a JsonResponse that only shoes status and message (where there is an errors)
     * @param status
     * @param message 
     */
    public JsonResponse(String status, String[] message) {
        this.status = status;
        this.message = message;
    }

    /**
     * constructs a JsonResponse that facilitates Dump (User)
     * @param status
     * @param message
     * @param userid
     * @param password
     * @param name
     * @param school
     * @param edollar 
     */
    public JsonResponse(String status, String[] message, String userid, String password, String name, String school, BigDecimal edollar) {
        this.status = status;
        this.message = message;
        this.userid = userid;
        this.password = password;
        this.name = name;
        this.school = school;
        this.edollar = edollar;
    }
    
    /**
     * constructs a JsonResponse when Round is started successfully.
     * @param status
     * @param round 
     */
    public JsonResponse(String status, int round) {
        this.status = status;
        this.round = round;
    }
    
    /**
     * constructs a JsonResponse according to the Dump (Bid) format
     * @param status
     * @param bids 
     */
    public JsonResponse(String status, JsonBidObject[] bids) {
        this.status = status;
        this.bids = bids;
    }
    
    /**
     * constructs a JsonResponse according to the Dump (Section) format
     * @param status
     * @param bids 
     */
    public JsonResponse(String status, JsonStudentObject[] students) {
        this.status = status;
        this.students = students;
    }
    
    
    
}
