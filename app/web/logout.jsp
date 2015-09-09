
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<link rel="shortcut icon" href="img/logo_icon.png">
<%
    session.removeAttribute("userid");
    response.sendRedirect("login.jsp");

%>
