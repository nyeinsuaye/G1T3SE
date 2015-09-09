
<%@page import="com.app.json.BootstrapError"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.app.json.counters.StudentCounter"%>
<%@page import="com.app.json.counters.SectionCounter"%>
<%@page import="com.app.json.counters.PrerequisiteCounter"%>
<%@page import="com.app.json.counters.CourseCompletedCounter"%>
<%@page import="com.app.json.counters.CourseCounter"%>
<%@page import="com.app.json.counters.BidCounter"%>
<%@page import="com.app.json.counters.Counter"%>
<%@page import="com.app.model.BootstrapDAO"%>
<%@page import="com.app.json.JsonResponse"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.app.utility.JsonServiceUtility"%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>

<%
    //GET numRecordLoaded / errors / errorRowNumbers
    //get the numRecordLoaded
    HashMap<String, Integer> numRecordLoadedMap = (HashMap<String, Integer>) session.getAttribute("numRecordLoadedMap");

    //get error
    ArrayList<BootstrapError> errorList = (ArrayList<BootstrapError>) session.getAttribute("errorList");
    BootstrapError[] error = errorList.toArray(new BootstrapError[0]);

    session.removeAttribute("numRecordLoadedMap");
    session.removeAttribute("errorList");

    //Prepare status
    String status = null;
    if (error != null && error.length > 0) {
        status = "error";
    } else if (error.length == 0) {
        status = "success";
        error = null;   //set to null so that empty arr is not printed.
    } else {
        status = "exception";   // for exceptional cases
    }



    //Should convert Collection/Map into an array form. (Consider integration with javascript data types)
    //a Collection/Map is an object with attributes of other objects.
    //an array contains references to objects.
    /*
     Counter[] numRecordLoadedArr = {    new BidCounter(numRecordLoadedMap.get("bid.csv")),
     new CourseCounter(numRecordLoadedMap.get("course.csv")),
     new CourseCompletedCounter(numRecordLoadedMap.get("course_completed.csv")),
     new PrerequisiteCounter(numRecordLoadedMap.get("prerequisite.csv")),
     new SectionCounter(numRecordLoadedMap.get("section.csv")),
     new StudentCounter(numRecordLoadedMap.get("student.csv"))   };
  
     //For creating JSON output
     JsonServiceUtility jsonUtility = new JsonServiceUtility();
    
     String jsonOutput = jsonUtility.serialize(new JsonResponse(status, numRecordLoadedArr, error));
    
     out.print(jsonOutput);
     */

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
        
        
        <title>Bootstrap Status</title>
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

        <!--
        THREE TABLES ARE CREATED
        1. status
        2. num-record-loaded
        3. error
        -->
        
        <span class="row"></span>
        <span class="row"></span>
        <span class="row"></span>
        <span class="row"></span>
        
        <div class="col-sm-12 col-md-12 col-lg-offset-1 col-lg-10" id="content">
        <table class="table table-bordered">
            <tr><th>Status</th><td><%= status%></td></tr> 
        </table>

        </br>
        Num-record-loaded:
        <hr>
        <table class="table table-bordered">
            <tr><th>bid.csv</th><td><%=numRecordLoadedMap.get("bid.csv")%></td></tr>
            <tr><th>course.csv</th><td><%=numRecordLoadedMap.get("course.csv")%></td></tr>
            <tr><th>course_completed.csv</th><td><%=numRecordLoadedMap.get("course_completed.csv")%></td></tr>
            <tr><th>prerequisite.csv</th><td><%=numRecordLoadedMap.get("prerequisite.csv")%></td></tr>
            <tr><th>section.csv</th><td><%=numRecordLoadedMap.get("section.csv")%></td></tr>
            <tr><th>student.csv</th><td><%=numRecordLoadedMap.get("student.csv")%></td></tr>
        </table>

        </br>
        <br/>
        <%
            if (error != null) {
        %>
        error:
        <hr>

        <%
            for (BootstrapError errorObj : error) {
                String file = errorObj.getFile();
                int line = errorObj.getLine();
                String[] message = errorObj.getMessage();
        %>        
        <table class="table table-bordered">
            <tr><th>File:</th><td><%=file%></td></tr>
            <tr><th>Line:</th><td><%=line%></td></tr>
            <tr><th>Message:</th>
                    <%
                        for (String msg : message) {

                    %>
                <td><%=msg%></td>

                <%
                    }
                %>
            </tr>
        </table>
        <%
                }
            }
        %>

        <button type="submit" class="btn btn-primary"><a href="bootstrap.jsp"><font style="color : white"><span class="glyphicon glyphicon-arrow-left"></span>  Back</a></button>
        </div>
        <%@include file="/includes/footer.jsp"%> 
    </body>
</html>


