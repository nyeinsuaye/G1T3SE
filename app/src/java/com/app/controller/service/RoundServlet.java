/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.app.controller.service;

import com.app.clearing.RoundClearingMaster;
import com.app.clearing.SuddenDeathMaster;
import com.app.json.JsonResponse;
import com.app.model.BidTempDAO;
import com.app.model.Round;
import com.app.utility.JsonServiceUtility;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *  Allows administrator to use this platform start and stop rounds
 * @author Zachery
 */
public class RoundServlet extends HttpServlet {

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
        String who = uri.substring(0,uri.lastIndexOf("/"));
        
        boolean isJsonRequest;
        if ("/admin".equals(who)) {
            isJsonRequest = false;
        } else {
            isJsonRequest = true;
        }
        
        //identify "start.do" or "stop.do"
        String action = uri.substring(uri.lastIndexOf("/") + 1);
        
        //------------------- Change STATE + Prepare JSON output --------
        String status = null;
        JsonResponse jsonResponse = null;
        String[] message = new String[1];
        
        
        int roundNum = Round.getRound();
        if ("start".equals(action)) {
            switch (roundNum) {
                case 0:     
                    //clear old data
                    BidTempDAO.emptyTable();
                    
                    //initialize environment first before changing round number
                    //because if roundNum is changed first, there is no more "administrative lock"
                    SuddenDeathMaster.initializeEnvironment();
                                        
                    //when roundNum is 0, indicate that window is closed(round 1 ALREADY ended).
                    //When you start you only start round 2
                    Round.setRound(2);
                    roundNum = Round.getRound();    //reinitialize local variable roundNum to be printed
                    
                    status = "success";
                    jsonResponse = new JsonResponse(status,roundNum);
                    break;
                case 1:
                    //when roundNum is 1, 
                    //If the round is started successfully (or already started), display success message
                    status = "success";
                    jsonResponse = new JsonResponse(status,roundNum);
                    break;
                case 2:
                    //when roundNum is 2,
                    //If the round is started successfully (or already started), display success message
                    status = "success";
                    jsonResponse = new JsonResponse(status,roundNum);
                    break;
                case -1:
                    //when roundNum is -1 (or CLOSED FOREVER)
                    status = "error";
                    message[0] = "round 2 ended";
                    jsonResponse = new JsonResponse(status,message);
                    break;
                default:
                    status = "error";
                    message[0] = "should not happen";
                    jsonResponse = new JsonResponse(status,message);
                    break;               
            }
            
        } else if ("stop".equals(action)) {
            switch (roundNum) {
                case 0:     
                    //when roundNum is 0, CANNOT STOP ANYMORE
                    status = "error";
                    message[0] = "round already ended";
                    jsonResponse = new JsonResponse(status,message);
                    break;
                case 1:
                    //when roundNum is 1, STOP round
                    Round.setRound(0);
                    
                    //execute Round 1 Clearing Logic
                    try {
                        RoundClearingMaster.executeRound1ClearingProcess();
                    } catch (IllegalStateException e) {
                        //Bid list is empty
                    }
                    
                    status = "success";
                    jsonResponse = new JsonResponse(status);
                    break;
                case 2:
                    //when roundNum is 2, STOP round (CEASE FOREVER)
                    Round.setRound(-1);       
                    
                    RoundClearingMaster.executeRound2ClearingProcess();
                    
                    status = "success";
                    jsonResponse = new JsonResponse(status);
                    break;
                case -1:
                    //when roundNum is -1 (or CLOSED FOREVER), CANNOT STOP ANYMORE
                    status = "error";
                    message[0] = "round already ended";
                    jsonResponse = new JsonResponse(status,message);
                    break;
                default:
                    status = "error";
                    message[0] = "should not happen";
                    jsonResponse = new JsonResponse(status,message);
                    break;               
            }
        }
        
        //INCLUDE RESET?
        if (isJsonRequest) {
        
            JsonServiceUtility jsonUtility = new JsonServiceUtility();
            String jsonOutput = jsonUtility.serialize(jsonResponse);

            out.print(jsonOutput);
            out.close();
        } else {
            HttpSession session = request.getSession();
            session.setAttribute("jsonResponse",jsonResponse);
            
            response.sendRedirect("round-status.jsp");
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
