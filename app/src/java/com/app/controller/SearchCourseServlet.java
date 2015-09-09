/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.app.controller;

import com.app.model.Course;
import com.app.model.CourseDAO;
import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SearchCourseServlet extends HttpServlet {

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
        ArrayList<Course> newCourseList = new ArrayList<Course>();

        //Search Bar
        String keyword = request.getParameter("courseKey");


        //If search is done with filter buttons
        String theSchool = "";
        if (keyword == null) {
            theSchool = request.getParameter("school");
            ArrayList<Course> coursesOfSchool = CourseDAO.retrieveBySchool(theSchool);

            request.setAttribute("courseList", coursesOfSchool);
            RequestDispatcher rd = request.getRequestDispatcher("bid.jsp");
            rd.forward(request, response);
            return;
        }



        keyword = keyword.toUpperCase();
        //If search is done with typeahead
        int index = keyword.indexOf("-");
        if (index != -1) {
            keyword = keyword.substring(0, index);
        }
        ArrayList<Course> courseList = CourseDAO.retrieveAll();

        //Check if the course title matched keyword
        for (Course c : courseList) {
            String courseCode = c.getCourse();
            String courseTitle = c.getTitle();

            if (courseCode.contains(keyword) || courseTitle.contains(keyword)) {
                newCourseList.add(c);
            }
        }

        request.setAttribute("courseList", newCourseList);
        RequestDispatcher rd = request.getRequestDispatcher("bid.jsp");
        rd.forward(request, response);
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
