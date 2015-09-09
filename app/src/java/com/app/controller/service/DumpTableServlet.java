/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.app.controller.service;

import com.app.json.JsonResponse;
import com.app.model.*;
import com.app.model.two.SuddenDeathDAO;
import com.app.utility.JsonServiceUtility;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Allows the administrator to use this platform to dump tables.
 * 
 */
public class DumpTableServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP
     * <code>GET</code> and
     * <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        //response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        //PARAMETERS IN THIS ORDER
        //JsonResponse(String status, Course[] course, Section[] section, Student[] student, 
                //Prerequisite[] prerequisite, Bid[] bid, CompletedCourse[] completedCourse, Bid[] sectionStudent)
        
        //Will not have NullPointerException because an empty ArrayList will still be returned.
        Course[] course = CourseDAO.retrieveAllNoPrerequisitesJsonOrdered().toArray(new Course[0]);
        
        Section[] section = SectionDAO.retrieveAllWithDayAndDatesAsStringOrdered().toArray(new Section[0]);
        
        Student[] student = StudentDAO.retrieveAllNoCourseCompletedOrdered().toArray(new Student[0]);
        
        Prerequisite[] prerequisite = CourseDAO.retrievePrerequisitesOfAllCoursesOrdered().toArray(new Prerequisite[0]);
        
        CompletedCourse[] completedCourse = StudentDAO.retrieveCourseCompletedOfAllStudentsOrdered().toArray(new CompletedCourse[0]);
        
        /*
         * IMPORTANT PART BELOW HERE
         */
        Bid[] bid = null;
        
        int roundNum = Round.getRound();
        //NO ACTIVE ROUND, retrieve from bid_temp table
        if (roundNum == 1) {
            bid = BidDAO.retrieveAllJsonOrdered().toArray(new Bid[0]);  //SCALED
            //Round 1: Only the bid details for the current round should be shown 
        } else if (roundNum == 0 || roundNum == -1) {
            //the bids (whether successful or unsuccessful) for the most recently concluded round should be shown.
            bid = BidTempDAO.retrieveAllJsonOrdered().toArray(new Bid[0]);  //SCALED
        } else if (roundNum == 2) {
            //list the last bid made by the user in each section. 
            bid = SuddenDeathDAO.retrieveForDumpTable().toArray(new Bid[0]);    //SCALED
        }
        
        
        //THIS IS NECESSARY BECAUSE REQUIREMENT CHANGED THE NAME OF THE ATTRIBUTES FOR SECTIONSTUDENT FROM BID..SIGH
        ArrayList<Bid> sectionStudentBidList = new ArrayList<>();
        if (roundNum == 0 || roundNum == 2) {
            sectionStudentBidList = SectionStudentDAO.retrieveAllJsonOrderedRound1();   //SCALED
        } else if (roundNum == -1) {
            sectionStudentBidList = SectionStudentDAO.retrieveAllJsonOrderedRound2();   //SCALED
        }
        ArrayList<SectionStudent> sectionStudentList = new ArrayList<>();
        for (Bid bidObj : sectionStudentBidList) {
            sectionStudentList.add(new SectionStudent(bidObj));
        }
        
        SectionStudent[] sectionStudent = sectionStudentList.toArray(new SectionStudent[0]);
        
        /*
        if (course.length == 0) {
            course = null;
        } 
        if (section.length == 0) {
            section = null;
        } 
        if (prerequisite.length == 0) {
            prerequisite = null;
        } 
        if(completedCourse.length == 0) {
            completedCourse = null;
        }
        if (bid.length == 0) {
            bid = null;
        } 
        if (sectionStudent.length == 0) {
            sectionStudent = null;
        }
        */
        
        String status = null;
        JsonResponse jsonResponse = null;
        
        boolean shouldNotHappen = false;    //DONT KNOW... THis should not happen.
        if ( shouldNotHappen ) {
            status = "error";
            jsonResponse = new JsonResponse(status);
        } else {
            status = "success";
            jsonResponse = new JsonResponse(status,course,section,student,prerequisite,bid,completedCourse,sectionStudent);
        }
        
        
        //For creating JSON output
        JsonServiceUtility jsonUtility = new JsonServiceUtility();

        String jsonOutput = jsonUtility.serialize(jsonResponse);

        out.print(jsonOutput);
        out.close();

    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP
     * <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP
     * <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
