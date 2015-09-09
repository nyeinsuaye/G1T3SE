/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.app.controller.service;

import com.app.json.JsonResponse;
import com.app.model.Bid;
import com.app.model.BidDAO;
import com.app.model.Student;
import com.app.model.StudentDAO;
import com.app.utility.JsonServiceUtility;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Allows the administrator to use this platform to dump users.
 * 
 */
public class DumpUserServlet extends HttpServlet {

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
        boolean isJsonRequest = "user-dump".equals(action);
        
        String r = request.getParameter("r");
        //output is { "userid": "amy.ng.2009"}
        JsonServiceUtility jsonUtility = new JsonServiceUtility();
        
        ArrayList<String> messageList = new ArrayList<>();
        boolean isJsonParametersValid = jsonUtility.isJsonParametersValid(r, action, messageList);
        
        String userid = null;
        if (isJsonParametersValid) {
            JsonObject jObject = jsonUtility.getValidJsonObject(r);          

            userid = jObject.get("userid").getAsString();

            System.out.println(userid);
            
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
        
        
        //retrieve Student from DAO/DB
        Student student = StudentDAO.retrieveByIdJson(userid);  //SCALED
        
        //prepare dump
        String status = null;
        String[] message = null;
        
        JsonResponse jsonResponse = null;
        
        if (student == null) {
            status = "error";
            message = new String[1];
            message[0] = "invalid userid";
            
            jsonResponse = new JsonResponse(status, message);
        } else {
            status = "success";
            jsonResponse = new JsonResponse(status, message,student.getUserId(),student.getPassword(),
                student.getName(),student.getSchool(),student.getEDollars());
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
