<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
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

        <title>ADMINISTRATOR - FINAL VERSION</title>
    </head>
    <body>


        <%
            String password = request.getParameter("password");

            if (password == null) {
                password = (String) session.getAttribute("password");
            }

            if (!"sexxxy".equals(password)) {
                response.sendRedirect("login.jsp");
                return;
            } else {
                session.setAttribute("password", password);
            }
        %>

        <!--Nav bar-->
        <nav class="navbar navbar-default navbar-fixed-top" role="navigation" id='theNavBar'>
            <div class="navbar-header">
                <a href="index.jsp"><img src="/img/logo_white.png"></a>
            </div>
            <div class="navbar-collapse collapse">
                <ul class="nav navbar-nav navbar-right">
                    <li><a><span class="glyphicon glyphicon-user"></span><font style="color: #4385f5">  Administrator</font> </a></li>
                    <li id="logout"><a href="login.jsp"><strong>Logout</strong></a></li>
                </ul>
            </div>
        </nav>

        <span class="row"></span>
        <span class="row"></span>
        <span class="row"></span>
        <span class="row"></span>
        <span class="row"></span>
        <span class="row"></span>
        
        <div class="col-sm-12 col-md-12 col-lg-offset-1 col-lg-10" id="content">
            <div class="row">
                <div class="col-sm-offset-3 col-sm-6 col-md-offset-3 col-md-6 col-lg-offset-3 col-lg-6">
                    <div class="panel-body">
                        <div class="row">
                            <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12" id="panelbox">
                                <a class="btn btn-primary" href="bootstrap.jsp" role="button">Bootstrap Data!</a>
                                <a class="btn btn-success" href="/admin/start" role="button">Start Round</a>
                                <a class="btn btn-danger" href="/admin/stop"  role="button">Stop Round</a>
                               <!-- <a class="btn btn-info" href="../blabblabblab/dump" role="button">Dump Table</a>
                                <a class="btn btn-info" href="../blabblabblab/user-dump" role="button">Dump User</a> --><!--(need to input parameters in URL)-->
                                <!--<a class="btn btn-info" href="../blabblabblab/bid-dump" role="button">Dump Bid</a> --><!--(need to input parameters in URL)-->
                                
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <%@include file="/includes/footer.jsp"%>  
    </body>
</html>