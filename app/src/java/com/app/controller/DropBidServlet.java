/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.app.controller;

import com.app.json.JsonResponse;
import com.app.model.Bid;
import com.app.model.BidDAO;
import com.app.model.Round;
import com.app.model.Student;
import com.app.model.StudentDAO;
import com.app.model.two.SuddenDeathDAO;
import com.app.utility.JsonServiceUtility;
import com.app.utility.validation.CRUDValidator;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class DropBidServlet extends HttpServlet {

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
        JsonServiceUtility jsonUtility = new JsonServiceUtility();

        /*
         *  JSON JSON JSON
         */
         
        String uri = request.getRequestURI();
        String action = uri.substring(uri.lastIndexOf("/") + 1);
        boolean isJsonRequest = "delete-bid".equals(action);
        
        /*
         *  JSON JSON JSON
         */ 
        
        
        //Retrieve session user
        HttpSession session = request.getSession();
        String userid = (String) session.getAttribute("userid");
        Student student = StudentDAO.retrieveById(userid);
        
        //Prevent NullPointer Exception
        if (!isJsonRequest && student == null) {
            response.sendRedirect("login.jsp");
            return;
        }
        
        String course = null;
        String section = null;
        
        if (!isJsonRequest) {
            //Retrieve Form Inputs
            userid = student.getUserId();
            course = request.getParameter("course");
            section = request.getParameter("section");
        }
        /*
         *  JSON JSON JSON
         */
        
        //(SHOULD BE INTEGRATED by using the same parameter names as JSON inputs. Oh well, for now.)
        //check if the action comes from a CLIENT USER or JSON INPUTs
        if (isJsonRequest) {
            String r = request.getParameter("r");
            //output is {"userid": "ada.goh.2012", "code": "IS100", "section": "S1"}
             
            ArrayList<String> messageList = new ArrayList<>();
            boolean isJsonParametersValid = jsonUtility.isJsonParametersValid(r, action, messageList);
            
            if (isJsonParametersValid) {
                JsonObject jObject = jsonUtility.getValidJsonObject(r);          

                userid = jObject.get("userid").getAsString();
                course = jObject.get("code").getAsString();
                section = jObject.get("section").getAsString();

                System.out.println(userid);
                System.out.println(course);
                System.out.println(section);
            }

            if (isJsonParametersValid) {

                student = StudentDAO.retrieveById(userid);

            } else {
                String status = "error";

                String[] message = messageList.toArray(new String[0]);

                JsonResponse jsonResponseObj = new JsonResponse(status,message);
                String jsonOutput = jsonUtility.serialize(jsonResponseObj);

                out.print(jsonOutput);

                //dump json response
                return;
            }
            
        }
        /*
         *  JSON JSON JSON
         */
        
        //Error messages
        ArrayList<String> messageList = new ArrayList<>();
        ArrayList<String> userMessageList = new ArrayList<>();
        
        CRUDValidator validator= new CRUDValidator(messageList,userMessageList);
        boolean isDeleteBidValid = validator.isDeleteBidValid(userid,course,section);
        System.out.println("Line110: isDeleteBidValid: " +isDeleteBidValid);

        String status = null;
        if (isJsonRequest && isDeleteBidValid) {
            status = "success";
        } else {
            status = "error";
        }

        //GET ROUND NUMBER
        int roundNum = Round.getRound();
        
        if(isDeleteBidValid){
            if (roundNum == 1) {
                Bid bid = BidDAO.retrieveSpecificBid(userid,course,section);
                BigDecimal amount = bid.getAmount();

                BigDecimal currentBalance = student.getEDollars().add(amount);
                StudentDAO.updateEDollars(userid, currentBalance);

                BidDAO.remove(userid,course,section);
            
            } else if (roundNum == 2) {
                Bid bid = SuddenDeathDAO.retrieveSpecificBid(userid,course,section);
                BigDecimal amount = bid.getAmount();
                
                BigDecimal currentExcessBalance = StudentDAO.retrieveByIdExcessEDollars(userid);
                StudentDAO.updateExcessDollars(userid, BigDecimal.ZERO);
                
                BigDecimal currentBalance = student.getEDollars().add(currentExcessBalance).add(amount);
                StudentDAO.updateEDollars(userid, currentBalance);
                
                SuddenDeathDAO.remove(userid, course, section);
            }
            
            //IF REQUEST WAS SENT via JSON
            if (isJsonRequest) {
                JsonResponse jsonResponseObj = new JsonResponse(status);
                String jsonOutput = jsonUtility.serialize(jsonResponseObj);
                
                out.print(jsonOutput);
                out.close();
                //dump json response
                return;
            }
            response.sendRedirect("timetable.jsp");
        } else {
            //IF REQUEST WAS SENT via JSON
            if (isJsonRequest) {
                String[] message = messageList.toArray(new String[0]);
                JsonResponse jsonResponseObj = new JsonResponse(status,message);
                String jsonOutput = jsonUtility.serialize(jsonResponseObj);
                
                out.print(jsonOutput);
                out.close();
                
                //dump json response
                return;
            }
            
            response.sendRedirect("timetable.jsp");
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
