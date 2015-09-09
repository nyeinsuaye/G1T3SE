/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.app.controller.service;

import com.app.model.BootstrapDAO;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import com.app.json.BootstrapError;
import com.app.json.JsonResponse;
import com.app.json.counters.*;
import com.app.model.Round;
import com.app.utility.JsonServiceUtility;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Collections;

/**
 *  Allows the administrator to use this platform to bootstrap.
 * 
 */

public class BootstrapServlet extends HttpServlet {

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
        //INITIALIZE JSON PRINTER
        //response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        
        //DISTINGUISH if HUMAN OR JSON.
        String uri = request.getRequestURI();
        String who = uri.substring(0,uri.lastIndexOf("/"));
        
        boolean isJsonRequest;
        if ("/admin".equals(who)) {
            isJsonRequest = false;
        } else {
            isJsonRequest = true;
        }
        
        // INITIALIZE REAL PATH (THIS IS NECESSARY FOR BOTH CONNECTION AND PATH PROPERTIES
        String realPath = request.getServletContext().getRealPath("/");
        BootstrapDAO.initializeRoot(realPath);
        //System.out.println(realPath);
        
        //1. ArrayList<String> to store error or success messages
        ArrayList<String> msgList = new ArrayList<String>();
        
        //2. CLEAR ALL EXISTING DATA
        BootstrapDAO.clearTables();
        
        //3. Upload file to from client to server
        
        try {
            
            //reads request and download file to the servlet
            BootstrapDAO.upload(request);
            
            //4. Unzip file to data folder
            String from = BootstrapDAO.getZipFileLocation();
            String to = BootstrapDAO.getDataDirectory();
            BootstrapDAO.extractAllFiles(from, to);
            
            
        } catch(Exception e){
            
            msgList.add(e.getMessage());
        }
        
        //5. execute create-tables.sql
        try{
            BootstrapDAO.createTables();
        } catch(SQLException e){
            //handle SQL Exception
        }
        
        
        // PRE VALIDATION SET UP NEEDED TO FACILITATE JSON Status report
        // Need to store numbers into counters.
        HttpSession session = request.getSession(); //spawn a session
        HashMap<String, Integer> numRecordLoadedMap = new HashMap<String, Integer>();
        //to be converted to Error[]. Initial ArrayList, so that it will be expandable.
        ArrayList<BootstrapError> errorList = new ArrayList<BootstrapError>();
        //compile/combine errors of all 6 CSVs into one list.
        
        
        
        //6. Read CSVs, Validate, and execute Batch inserts.
        //VALIDATE INSERT STUDENTS
        BootstrapDAO.resetStage();      //reset before beginning validation. Stage is static and constantly reused.
        List<String[]> studentEntries = BootstrapDAO.read("student.csv");
        BootstrapDAO.validate("student.csv", studentEntries);
        BootstrapDAO.insertStudents();
        //get the number of successful entries added to DB
        numRecordLoadedMap.put("student.csv",BootstrapDAO.getNumRecordLoaded());
        errorList.addAll(BootstrapDAO.getErrorList());
        
        
        //VALIDATE INSERT COURSES
        BootstrapDAO.resetStage();      //reset before beginning validation. Stage is static and constantly reused.
        List<String[]> courseEntries = BootstrapDAO.read("course.csv");
        BootstrapDAO.validate("course.csv", courseEntries);
        BootstrapDAO.insertCourses();
        //get the number of successful entries added to DB
        numRecordLoadedMap.put("course.csv",BootstrapDAO.getNumRecordLoaded());
        errorList.addAll(BootstrapDAO.getErrorList());
        
        //VALIDATE INSERT SECTION
        BootstrapDAO.resetStage();      //reset before beginning validation. Stage is static and constantly reused.
        List<String[]> sectionEntries = BootstrapDAO.read("section.csv");
        BootstrapDAO.validate("section.csv", sectionEntries);
        BootstrapDAO.insertSections();
        //get the number of successful entries added to DB
        numRecordLoadedMap.put("section.csv",BootstrapDAO.getNumRecordLoaded());
        errorList.addAll(BootstrapDAO.getErrorList());

        //VALIDATE INSERT PREREQUISITE
        BootstrapDAO.resetStage();      //reset before beginning validation. Stage is static and constantly reused.
        List<String[]> prerequisiteEntries = BootstrapDAO.read("prerequisite.csv");
        BootstrapDAO.validate("prerequisite.csv", prerequisiteEntries);
        BootstrapDAO.insertPrerequisites();
        //get the number of successful entries added to DB
        numRecordLoadedMap.put("prerequisite.csv",BootstrapDAO.getNumRecordLoaded());
        errorList.addAll(BootstrapDAO.getErrorList());
       
        //VALIDATE INSERT COURSECOMPLETED
        BootstrapDAO.resetStage();      //reset before beginning validation. Stage is static and constantly reused.
        List<String[]> courseCompletedEntries = BootstrapDAO.read("course_completed.csv");
        BootstrapDAO.validate("course_completed.csv", courseCompletedEntries);
        BootstrapDAO.insertCourseCompleted();
        //get the number of successful entries added to DB
        numRecordLoadedMap.put("course_completed.csv",BootstrapDAO.getNumRecordLoaded());
        errorList.addAll(BootstrapDAO.getErrorList());
        
        //VALIDATE INSERT BIDS
        BootstrapDAO.resetStage();      //reset before beginning validation. Stage is static and constantly reused.
        List<String[]> bidEntries = BootstrapDAO.read("bid.csv");
        BootstrapDAO.validate("bid.csv", bidEntries);
        numRecordLoadedMap.put("bid.csv",BootstrapDAO.getNumRecordLoaded());       
        BootstrapDAO.insertBids();
        //get the number of successful entries added to DB  
        errorList.addAll(BootstrapDAO.getErrorList());
        
        //sort errorList by alphabetical order.
        Collections.sort(errorList);
        
        //CLEAR DATA IN BootstrapDAO
        BootstrapDAO.resetStage();
        
        //Store numRecordLoaded map into session obj
        session.setAttribute("numRecordLoadedMap", numRecordLoadedMap);
        session.setAttribute("errorList", errorList);
        
        
        //After bootstrapping, round 1 is automatically started.
        Round.setRound(1);
        //Note: static variables are only initialized when the class is first called. E.g. when Round is called
     
        
        //direct to status page.        
        if (!isJsonRequest) {
            //YOU ARE ONLY HUMAN
            response.sendRedirect("bootstrap-status.jsp");  
        } else {
            //YOU ARE MACHINE
            BootstrapError[] error = errorList.toArray(new BootstrapError[0]);
    
            session.removeAttribute("numRecordLoadedMap");
            session.removeAttribute("errorList");

            //Prepare status
            String status = null;
            if (error != null && error.length > 0) {
                status = "error";
            } else if (error.length == 0) {
                status = "success";
                error = null;   //set to null so that empty arr is not printed.
            } else {
                status = "exception";   // for exceptional cases
            }




            //Should convert Collection/Map into an array form. (Consider integration with javascript data types)
            //a Collection/Map is an object with attributes of other objects.
            //an array contains references to objects.
            Counter[] numRecordLoadedArr = {    new BidCounter(numRecordLoadedMap.get("bid.csv")),
                                                new CourseCounter(numRecordLoadedMap.get("course.csv")),
                                                new CourseCompletedCounter(numRecordLoadedMap.get("course_completed.csv")),
                                                new PrerequisiteCounter(numRecordLoadedMap.get("prerequisite.csv")),
                                                new SectionCounter(numRecordLoadedMap.get("section.csv")),
                                                new StudentCounter(numRecordLoadedMap.get("student.csv"))   };


            //For creating JSON output
            JsonServiceUtility jsonUtility = new JsonServiceUtility();
            
            String jsonOutput = jsonUtility.serialize(new JsonResponse(status, numRecordLoadedArr, error));
            
            out.print(jsonOutput);
            out.close();
        }
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
