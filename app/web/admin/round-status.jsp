<%-- 
    Document   : round-status
    Created on : Nov 4, 2013, 9:11:46 PM
    Author     : Zachery
--%>

<%@page import="com.app.json.JsonResponse"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%
    String password = (String) session.getAttribute("password");

    if (!"sexxxy".equals(password)) {
        response.sendRedirect("login.jsp");
        return;
    }
%>

<%
    JsonResponse jsonResponse = (JsonResponse) session.getAttribute("jsonResponse");

    if (jsonResponse == null) {
        response.sendRedirect("index.jsp");
        return;
    }

    String status = jsonResponse.getStatus();
    Integer round = jsonResponse.getRound();    //might be null if error
    String[] message = jsonResponse.getMessage();

%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

        <!--Required Boostrap-->
        <link href="/bootstrap/dist/css/bootstrap.css" rel="stylesheet" media="screen">

        <!--Required Boostrap-->
        <link href="/css/custom.css" rel="stylesheet" media="screen">
        <link href="/css/admin.css" rel="stylesheet" media="screen">
        
        <!--Roboto-->
        <link href='http://fonts.googleapis.com/css?family=Roboto:400,100,100italic,300,300italic,400italic,500,500italic,700' rel='stylesheet' type='text/css'>

        <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
        <script src="//code.jquery.com/jquery.js"></script>


        <link rel="shortcut icon" href="/img/logo_icon.png">

        <title>Round Status </title>
    </head>
    <body>

        <!--Nav bar-->
        <nav class="navbar navbar-default navbar-fixed-top" role="navigation" id='theNavBar'>
            <div class="navbar-header">
                <a href="index.jsp"><img src="/img/logo_white.png"></a>
            </div>
            <div class="navbar-collapse collapse">
                <ul class="nav navbar-nav navbar-right">
                    <li><a><span class="glyphicon glyphicon-user"></span><font style="color: #4385f5">  Administrator</font> </a></li>
                    <li id="logout"><a href="logout.jsp"><strong>Logout</strong></a></li>
                </ul>
            </div>
        </nav>


        <span class="row"></span>
        <span class="row"></span>
        <span class="row"></span>
        <span class="row"></span>
        <span class="row"></span>
        <span class="row"></span>

        <div class="col-sm-offset-1 col-sm-10 col-md-offset-1 col-md-10 col-lg-offset-1 col-lg-10" id="content">
            <h1>Round Status</h1>


            <hr>
            <table class="table table-bordered">
                <tr><th>Status:</th><td><%=status%></td></tr>
                        <%
                            if (round != null) {
                        %>
                <tr><th>Round:</th><td><%=round%></td></tr>
                        <%
                            }

                            if (message != null) {
                                for (String msg : message) {
                        %>
                <tr><th>message:</th><td><%=msg%></td></tr>
                        <%
                                }
                            }
                        %>
            </table>
            <button type="submit" class="btn btn-primary"><a href="index.jsp"><font style="color : white"><span class="glyphicon glyphicon-arrow-left"></span>  Back</a></button>
        </div>
            <%@include file="/includes/footer.jsp"%> 
    </body>
</html>
