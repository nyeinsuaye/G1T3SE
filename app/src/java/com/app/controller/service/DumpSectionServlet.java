/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.app.controller.service;

import com.app.json.JsonResponse;
import com.app.json.JsonStudentObject;
import com.app.model.Bid;
import com.app.model.Course;
import com.app.model.CourseDAO;
import com.app.model.Section;
import com.app.model.SectionDAO;
import com.app.model.SectionStudentDAO;
import com.app.utility.JsonServiceUtility;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *  Allows the administrator to use this platform to dump sections.
 * 
 */
public class DumpSectionServlet extends HttpServlet {

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
        boolean isJsonRequest = "section-dump".equals(action);
        
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
            
            //RETRIEVE SUCCESSFUL BIDs of the section
            ArrayList<Bid> sectionStudentList = SectionStudentDAO.retrieveBySectionIdInOrderOfUserId(course, section);  //SCALED

            //ADD THEM INTO A COMBINED LIST OF Json-formatted Bid Objects
            ArrayList<JsonStudentObject> jsonStudentObjList = new ArrayList<>();
            for (Bid bid : sectionStudentList) {
                jsonStudentObjList.add(new JsonStudentObject(bid));
            }

            JsonStudentObject[] jsonStudentObjArr = jsonStudentObjList.toArray(new JsonStudentObject[0]);


            status = "success";
            jsonResponse = new JsonResponse(status, jsonStudentObjArr);
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
