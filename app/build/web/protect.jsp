<%@page import="com.app.model.Student"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>

<%
    String s = (String) session.getAttribute("userid");
    if (s == null){
        response.sendRedirect("login.jsp");
        return; 
    }
%>
