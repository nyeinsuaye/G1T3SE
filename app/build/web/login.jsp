<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">

        <!--Required Boostrap-->
        <link href="bootstrap/dist/css/bootstrap.css" rel="stylesheet" media="screen">

        <!--Required Boostrap-->
        <link href="css/custom.css" rel="stylesheet" media="screen">

        <!--Roboto-->
        <link href='http://fonts.googleapis.com/css?family=Roboto:400,100,100italic,300,300italic,400italic,500,500italic,700' rel='stylesheet' type='text/css'>

        <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
        <script src="//code.jquery.com/jquery.js"></script>

         <link rel="shortcut icon" href="img/logo_icon.png">
        <title>BIOS - Login</title>
        
        
    </head>

    <body>
        <span class="row"></span>
        <span class="row"></span>
        <span class="row"></span>
        <div class="col-sm-offset-4 col-sm-4 col-md-offset-4 col-lg-offset-4 col-md-4 col-lg-4" id="login">
            <img src="img/merlion_logo.png" style="width:100%; height:100%;" align="middle" >
            <hr style="color:black;">

            <%
                String errorMsg = (String) request.getAttribute("errorMsg");
            %>

            <!--Login box-->
            <div class="loginBox">
                <h1 style="font-family:roboto; font-weight:700; color:#303038">Login</h1>

                <%
                    if (errorMsg != null) {%>
                <div class="alert alert-danger alert-dismissable">
                    <strong><%=errorMsg%></strong>
                </div>
                <%}
                %>

                <form method="POST" action="authenticate.do" accept-charset="UTF-8">
                    <div class="form-group">
                        <input type="text" name="username" class="form-control" placeholder="Username" autocomplete="on" required>
                    </div>
                    <div class="form-group">
                        <input type="password" name="password" class="form-control" placeholder="Password" required>
                    </div>
                    <button type="submit" name="submit" id="loginButton" class="btn btn-block">Log In</button>
                </form>
            </div>

            <!--Footer-->
            <%@include file="includes/footer.jsp"%>
        </div>
    </body>
</html>