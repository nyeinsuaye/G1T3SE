<%-- 
    Document   : login
    Created on : Oct 19, 2013, 2:27:11 PM
    Author     : Zachery
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">

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
        <title>JSP Page</title>

    </head>
    <body>
        <span class="row"></span>
        <span class="row"></span>
        <span class="row"></span>
        <div class="col-sm-offset-4 col-sm-4 col-md-offset-4 col-lg-offset-4 col-md-4 col-lg-4" id="login">
            <img src="/img/merlion_logo.png" style="width:100%; height:100%;" align="middle" >
            <hr style="color:black;">
            <div class="loginBox">
                <h1 style="font-family:roboto; font-weight:700; color:#303038">ADMIN</h1>


                <form method="POST" action="index.jsp" accept-charset="UTF-8">
                    <br>
                    <div class="form-group">

                        <input type="password" class="form-control" placeholder="Password" name="password" required/>
                    </div>
                    <br>
                    <div class="form-group">
                        <button type="submit" name="submit" id="loginButton" class="btn btn-block">Press if you are cool!</button>
                    </div>
                </form>
            </div>
        </div>
    </body>
</html>
