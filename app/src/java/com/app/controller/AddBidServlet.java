
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

/**
 * Provides a platform for users to add bids.
 * 
 */
public class AddBidServlet extends HttpServlet {

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
         * JSON: /blabblabblab/add-bid
         * WEB USER: /add-bid.do
         * 
         */
         
        String uri = request.getRequestURI();
        String action = uri.substring(uri.lastIndexOf("/") + 1);
        boolean isJsonRequest = "add-bid".equals(action);
        
        /*
         *  JSON JSON JSON
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
            
                System.out.println(userId);
                System.out.println(amountStr);
                System.out.println(course);
                System.out.println(section);
                
                student = StudentDAO.retrieveById(userId);
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
 
        ArrayList<String> messageList = new ArrayList<>();
        ArrayList<String> userMessageList = new ArrayList<>();
        CRUDValidator validator = new CRUDValidator(messageList,userMessageList);
        
        //NECESSARY JSON Validations
        boolean isUpdateBidValid = validator.isUpdateBidValid(userId,amountStr,course,section);
        boolean isUserAddBidValid = true;
        
        /*
         * IF JSON, SET JSON STATUS
         */
        String status = null;
        if (isJsonRequest && isUpdateBidValid) {
            status = "success";
        } else {
            status = "error";
        } 
        
        /*
         * Additional User input validations, IF IT IS NOT A JSON REQUEST
         */
        if (!isJsonRequest) {
            isUserAddBidValid = validator.isUserAddBidValid(userId, amountStr, course, section);
        }
        
        //if user request fails
        if (!isUserAddBidValid) {
            session.setAttribute("messageList",messageList);
            session.setAttribute("userMessageList",userMessageList);
            response.sendRedirect("bidSession.jsp");
            return;
        }
        /*
         * END: Input validations
         */
        
        
        
        
        if(isUpdateBidValid){
            BigDecimal amount = new BigDecimal(amountStr);
            
            
            /*
             * 
             * CHECK FOR ROUND 2
             * 
             */
            int roundNum = Round.getRound();
            boolean isAddedInRound2 = false;
            if (roundNum == 2) {
                Bid existingBid = SuddenDeathDAO.retrieveUserBidOfCourse(userId, course);
                //if there is an existingBid, we need to credit back previous bid amount to student account before debiting
                if (existingBid != null) {
                    BigDecimal currentBalance = student.getEDollars();
                    
                    currentBalance = currentBalance.add(existingBid.getAmount());
                    
                    StudentDAO.updateEDollars(userId, currentBalance);
                }
                
                
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
            if (roundNum == 1 || isAddedInRound2) {
                //GET STUDENT's CURRENT BALANCE
                BigDecimal currentBalance = student.getEDollars();

                //current balance, MINUS AMOUNT OF NEW BID
                currentBalance = currentBalance.subtract(amount);

                //Update the accounts
                StudentDAO.updateEDollars(userId, currentBalance);
            }
            
            /*
             * Add to bid table if round 1.
             */
            if (roundNum == 1) {
                BidDAO.add(userId, amount, course, section);
            }
             
            /*  
             * IF REQUEST WAS SENT via JSON 
             */
            if (isJsonRequest) {
                JsonResponse jsonResponseObj = new JsonResponse(status);
                String jsonOutput = jsonUtility.serialize(jsonResponseObj);
                
                out.print(jsonOutput);
                //dump json response
                return;
            }
            
            response.sendRedirect("timetable.jsp");
        }else{
            
            
            
            //IF REQUEST WAS SENT via JSON
            if (isJsonRequest) {
                String[] message = messageList.toArray(new String[0]);
                JsonResponse jsonResponseObj = new JsonResponse(status,message);
                String jsonOutput = jsonUtility.serialize(jsonResponseObj);
                
                out.print(jsonOutput);
                
                //dump json response
                return;
            }
            
            
            
            //store error messages NOT NECESSARY.
            session.setAttribute("messageList",messageList);
            session.setAttribute("userMessageList",userMessageList);
            //to be merged!
            
            response.sendRedirect("bidSession.jsp");
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
