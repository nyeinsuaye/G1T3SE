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

        <title>Bootstrap Data!</title>
    </head>
    <body>
        <%
            String password = (String) session.getAttribute("password");
            if (!"sexxxy".equals(password)) {
                response.sendRedirect("login.jsp");
                return;
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

        <div class="col-sm-offset-4 col-sm-4 col-md-offset-4 col-lg-offset-4 col-md-4 col-lg-4" id="box">
            <h3>File Upload:</h3><hr>
            Select a file to upload: <br/><br/>
            <form action="/admin/bootstrap" method="post" enctype="multipart/form-data">
                <input type="file" name="bootstrap-file" />
                <br />
                <input type="submit" value="Upload File" />
            </form>
            <hr>
            <button type="submit" class="btn btn-primary"><a href="index.jsp"><font style="color : white"><span class="glyphicon glyphicon-arrow-left"></span>  Back</a></button>

        </div>
    </body>
</html>
