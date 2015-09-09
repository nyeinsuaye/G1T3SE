/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.app.controller.service;

import com.app.json.JsonBidObject;
import com.app.json.JsonResponse;
import com.app.model.Bid;
import com.app.model.BidDAO;
import com.app.model.Course;
import com.app.model.CourseDAO;
import com.app.model.FailedBidDAO;
import com.app.model.Round;
import com.app.model.Section;
import com.app.model.SectionDAO;
import com.app.model.SectionStudentDAO;
import com.app.model.two.SuddenDeathDAO;
import com.app.utility.JsonServiceUtility;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *  Allows the administrator to use this platform to dump bids.
 * 
 */
public class DumpBidServlet extends HttpServlet {

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
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        
      
        String uri = request.getRequestURI();
        String action = uri.substring(uri.lastIndexOf("/") + 1);
        boolean isJsonRequest = "bid-dump".equals(action);
        
        String r = request.getParameter("r");
        //string  is { "course": "IS100", "section": "S1"}
        JsonServiceUtility jsonUtility = new JsonServiceUtility();
        
        ArrayList<String> messageList = new ArrayList<>();
        boolean isJsonParametersValid = jsonUtility.isJsonParametersValid(r, action, messageList);
        
        String course = null;
        String section = null;
        if (isJsonParametersValid) {
            JsonObject jObject = jsonUtility.getValidJsonObject(r);          

            course = jObject.get("course").getAsString();
            section = jObject.get("section").getAsString();
            System.out.println(course);
            System.out.println(section);
        } 

        if (!isJsonParametersValid) {
            String status = "error";

            String[] message = messageList.toArray(new String[0]);

            JsonResponse jsonResponseObj = new JsonResponse(status,message);
            String jsonOutput = jsonUtility.serialize(jsonResponseObj);

            out.print(jsonOutput);

            //dump json response
            return;
        }
        
        //something here
        
        // -------------- PREPARE DUMP ----------------
        String status = null;
        String[] message = null;
        
        JsonResponse jsonResponse = null;
        
        //DO VALIDATION ON THE course and section parameters
        //retrieve Course Obj from DAO
        Course courseObj = CourseDAO.retrieveByCode(course);
        Section sectionObj = null;
        
        //validate
        if (courseObj == null) {
            status = "error";
            message = new String[1];
            message[0] = "invalid course";
        
        } else {    //retrieve Section only if course exist
            //retrieve SectionObj from DAO/DB
            sectionObj = SectionDAO.retrieveSection(course,section);
            
            if (sectionObj == null) {
                status = "error";
                message = new String[1];
                message[0] = "invalid section"; 
            }
        }
        
        
        //------------------ CREATE JSON RESPONSE ------------
        if (courseObj == null || sectionObj == null) {                        
            jsonResponse = new JsonResponse(status, message);
        } else {
            int roundNum = Round.getRound();
            
            
            //RETRIEVE SUCCESSFUL AND UNSUCCESSFUL BIDS
            ArrayList<Bid> sectionStudentList = null;
            if (roundNum == 0) {
                sectionStudentList = SectionStudentDAO.retrieveBySectionIdInOrderOfAmountUserIdOfRound(course, section,1);  //SCALED
            } else if (roundNum == -1) {
                sectionStudentList = SectionStudentDAO.retrieveBySectionIdInOrderOfAmountUserIdOfRound(course, section,2);  //SCALED
            }
            
            ArrayList<Bid> failedBidList = FailedBidDAO.retrieveBySectionIdInOrderOfAmountUserId(course,section);   //SCALED

            //RETRIEVE PENDING BIDS
            ArrayList<Bid> bidList = BidDAO.retrieveBySectionIdInOrderOfAmountUserId(course, section);  //SCALED
            ArrayList<Bid> suddenDeathList = SuddenDeathDAO.retrieveBySectionIdInOrderOfAmountUserId(course, section);  //SCALED
            
            ArrayList<Bid> combinedList = new ArrayList<>();
            
            /*
            if (roundNum == 0 || roundNum == -1) {
                combinedList.addAll(sectionStudentList);
                combinedList.addAll(failedBidList);
            }
            */
            
            if (roundNum == 1 || roundNum == 2) {
                combinedList.addAll(bidList);   //at round 1, bidList will be full, suddendeath empty
                combinedList.addAll(suddenDeathList);   //at round 2, bidList will be empty, suddendeath full
                //only one of the list is filled btw.
            }
            //INCOMPLETE
            //MUST SORT COMBINEDLIST BY amount and userid
            
            
            //ADD THEM INTO A COMBINED LIST OF Json-formatted Bid Objects
            ArrayList<JsonBidObject> jsonBidObjList = new ArrayList<>();
            
            if (roundNum == 0 || roundNum == -1) {
                int rowNum = 0;
                for (Bid bid : sectionStudentList) {
                    rowNum++;
                    jsonBidObjList.add(new JsonBidObject(rowNum,bid,"in"));
                }

                for (Bid bid : failedBidList) {
                    rowNum++;
                    jsonBidObjList.add(new JsonBidObject(rowNum,bid,"out"));
                }
                
                Collections.sort(jsonBidObjList);
            }
            
            if (roundNum == 1 || roundNum == 2) {
                int rowNum = 0;
                for (Bid bid : combinedList) {
                    rowNum++;
                    jsonBidObjList.add(new JsonBidObject(rowNum,bid,"-"));
                }
            }

            JsonBidObject[] jsonBidObjArr = jsonBidObjList.toArray(new JsonBidObject[0]);


            status = "success";
            jsonResponse = new JsonResponse(status, jsonBidObjArr);
        }
        
        out.print(jsonUtility.serialize(jsonResponse));
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
