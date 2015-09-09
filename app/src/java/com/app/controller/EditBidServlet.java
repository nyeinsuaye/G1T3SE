
package com.app.controller;

import com.app.clearing.SuddenDeathMaster;
import com.app.json.JsonResponse;
import com.app.model.Bid;
import com.app.model.BidDAO;
import com.app.model.Round;
import com.app.model.Student;
import com.app.model.StudentDAO;
import com.app.model.two.SuddenDeathDAO;
import com.app.utility.JsonServiceUtility;
import com.app.utility.validation.CRUDValidator;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class EditBidServlet extends HttpServlet {

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
        
        /*
         * HTML OUTPUT PRINTER
         */
        
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        JsonServiceUtility jsonUtility = new JsonServiceUtility();
        
        
        /*
         *  JSON JSON JSON
         * 
         *  GET URI TO FIND OUT WHERE THE REQUEST COMES FROM
         * JSON: /blabblabblab/update-bid
         * WEB USER: /edit-bid.do
         * 
         */
        String uri = request.getRequestURI();
        String action = uri.substring(uri.lastIndexOf("/") + 1);
        boolean isJsonRequest = "update-bid".equals(action);
            
        
        
        /*
         * INITIALIZE PARAMETERS FROM WEB USER'S REQUEST (RETRIEVE SESSION)
         */
        
        
        
        /*
         * INITIALIZE PARAMETERS FROM WEB USER'S REQUEST (RETRIEVE SESSION)
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
        
        
        String userId = null;
        String course = null;
        String section = null;
        String amountStr = null;
        
        if (!isJsonRequest) {
            //Retrieve Form Inputs
            userId = student.getUserId();
            course = request.getParameter("course");
            section = request.getParameter("section");
            amountStr = request.getParameter("amount");
        }
        
         /*
         *  JSON JSON JSON
         * 
         * IF JSON, INITIALIZE JSON PARAMETERS
         */
        
        //(SHOULD BE INTEGRATED by using the same parameter names as JSON inputs. Oh well, for now.)
        //check if the action comes from a CLIENT USER or JSON INPUTs
        
        if (isJsonRequest) {
            String r = request.getParameter("r");
            //output is {"userid": "ada.goh.2012", "amount": 11.0, "code": "IS100", "section": "S1"}
            
            ArrayList<String> messageList = new ArrayList<>();
            boolean isJsonParametersValid = jsonUtility.isJsonParametersValid(r, action, messageList);
            
            if (isJsonParametersValid) {
                JsonObject jObject = jsonUtility.getValidJsonObject(r);          
            
                userId = jObject.get("userid").getAsString();
                amountStr = jObject.get("amount").getAsString();
                course = jObject.get("code").getAsString();
                section = jObject.get("section").getAsString();
            
                /*
                System.out.println(userId);
                System.out.println(amountStr);
                System.out.println(course);
                System.out.println(section);
                */
                
                student = StudentDAO.retrieveById(userId);
            } 
            
            if (isJsonParametersValid) {
                Bid existingBid = BidDAO.retrieveUserBidOfCourse(userId,course);

                //IF BID DOES NOT EXIST, PROCEED TO ADD BID.
                if (existingBid == null) {
                    //unique prefix for JSON requsts only.
                    RequestDispatcher rd = request.getRequestDispatcher("/blabblabblab/add-bid");
                    rd.forward(request,response);
                    return;
                }
                
               
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
        
        
        
         /*
         * 
         * COLLECTION OF INPUT PARAMETERS END END END
         * 
         * 
         */
        
        
        /*
         * BEGIN VALIDATION OF USER/JSON INPUTS
         */
        
        //Error messages
        ArrayList<String> messageList = new ArrayList<>();
        ArrayList<String> userMessageList = new ArrayList<>();
        CRUDValidator validator = new CRUDValidator(messageList,userMessageList);
        
        //NECESSARY JSON Validations
        boolean isUpdateBidValid = validator.isUpdateBidValid(userId,amountStr,course,section);
        
        /*
         * VALIDATION COMPLETE
         */
        
        
        /*
        * IF JSON, SET JSON STATUS
        */
        String status = null;
        if (isJsonRequest && isUpdateBidValid) {
            status = "success";
        } else {
            status = "error";
        }            
        
        
        if(isUpdateBidValid){        
            int roundNum = Round.getRound();
            
            BigDecimal amount = new BigDecimal(amountStr);
            
            
            //get existing Bid information
            Bid existingBid = null;
            BigDecimal existingBidAmount = null;
            
            if (roundNum == 1) {
                existingBid = BidDAO.retrieveUserBidOfCourse(userId, course);
                existingBidAmount = existingBid.getAmount();
                System.out.println(existingBidAmount);
            }
            
            /*
             * 
             * CHECK FOR ROUND 2
             * 
             */
            boolean isAddedInRound2 = false;
            if (roundNum == 2) {
                existingBid = SuddenDeathDAO.retrieveUserBidOfCourse(userId,course);
                existingBidAmount = existingBid.getAmount();
                
                isAddedInRound2 = SuddenDeathMaster.addBid(userId, amount, course, section);
                System.out.println(isAddedInRound2);
            }
            
            
            /*
             * 
             * END: ROUND 2 HANDLING
             * 
             */
            
            
            
            /*
             * CREDIT STUDENT E$ WOOHOO!
             */
            if (roundNum == 1) {
                //get student's current balance
                BigDecimal currentBalance = student.getEDollars();

                //current balance, add back amount from old bid, minus amount of new bid
                currentBalance = currentBalance.add(existingBidAmount).subtract(amount);

                //Update the accounts
                StudentDAO.updateEDollars(userId, currentBalance);
                BidDAO.update(userId, amount, course, section);
            } else if (roundNum == 2 && isAddedInRound2) {
                
                //IN ROUND 2, AMOUNT IS NOT REFUNDED UNTIL ROUND IS STOPPED. THE BID MUST THEREFORE ACCOUNT THE DIFFERENCE.
                BigDecimal amountInExcess = existingBidAmount.subtract(amount);
                if (amountInExcess.compareTo(BigDecimal.ZERO) > 0) {
                    StudentDAO.updateExcessDollars(userid, amountInExcess);
                } else {
                    //get student's current balance
                    BigDecimal currentBalance = student.getEDollars();
                    
                    //current balance, credit balance
                    //POSITIVE + NEGATIVE BECOMES subtract
                    currentBalance = currentBalance.add(amountInExcess);
                    
                    //Update the accounts
                    StudentDAO.updateEDollars(userid, currentBalance);
                }
            }
            
            
            //IF REQUEST WAS SENT via JSON
            if (isJsonRequest) {
                JsonResponse jsonResponseObj = new JsonResponse(status);
                String jsonOutput = jsonUtility.serialize(jsonResponseObj);
                
                out.print(jsonOutput);
                //dump json response
                return;
            }
            
            response.sendRedirect("timetable.jsp");
        }else{
            
            //store error messages
            session.setAttribute("messageList",messageList);
            
            //IF REQUEST WAS SENT via JSON
            if (isJsonRequest) {
                String[] message = messageList.toArray(new String[0]);
                JsonResponse jsonResponseObj = new JsonResponse(status,message);
                String jsonOutput = jsonUtility.serialize(jsonResponseObj);
                
                out.print(jsonOutput);
                
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
