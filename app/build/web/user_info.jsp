<%@include file="protect.jsp"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.app.model.CourseDAO"%>
<%@page import="com.app.model.SectionDAO"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.app.model.Section"%>
<%@page import="com.app.model.Section"%>
<%@page import="com.app.model.Course"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">

        <!--Required Boostrap-->
        <link href="bootstrap/dist/css/bootstrap.css" rel="stylesheet" media="screen">

        <!--Required Boostrap-->
        <link href="css/custom.css" rel="stylesheet" media="screen">

        <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
        <script src="//code.jquery.com/jquery.js"></script>

        <link href='http://fonts.googleapis.com/css?family=Roboto:400,100,100italic,300,300italic,400italic,500,500italic,700' rel='stylesheet' type='text/css'>

        <!--Jquery Library-->
        <script src="http://code.jquery.com/jquery-1.9.1.js"></script>
        <script src="http://code.jquery.com/ui/1.10.3/jquery-ui.js"></script>

        <!--Css for Jquery UI-->
        <link href="http://code.jquery.com/ui/1.10.3/themes/smoothness/jquery-ui.css" rel="stylesheet" media="screen">
        <link href='http://fonts.googleapis.com/css?family=Roboto:400,100,100italic,300,300italic,400italic,500,500italic,700' rel='stylesheet' type='text/css'>

        <script src="js/bootstrap.min.js"></script>
        
        <!--typeahead-->
        <script src="js/typeahead.min.js"></script>
        <link href="css/typeahead.css" rel="stylesheet" media="screen">
        
        <link rel="shortcut icon" href="img/logo_icon.png">
        
        <title>BIOS - User Information</title>

    </head>

    <body id="userInfo" onload="selectDiv(1)">

        <!--Navigation bar-->
        <%@include file="includes/navigationbar.jsp"%>

        <div style="margin-top: 14px">
        <!--Search bar-->
        <%@include file="includes/searchbar.jsp" %>
        </div>

        <div class="col-sm-12 col-md-12 col-lg-12 ">




            <!--Get the Student object to get student info: Username and Edollars -->
            <%
                String userid2 = (String) session.getAttribute("userid");
                Student theStudent2 = StudentDAO.retrieveById(userid2);
                ArrayList<String> messageList = (ArrayList<String>) session.getAttribute("messsageList");
                ArrayList<String> userMessageList = (ArrayList<String>) session.getAttribute("userMessageList");

            %>


            <%
                ArrayList<String> courseCompleted = theStudent2.getCourseCompleted();
                int counter = 0;

                if (courseCompleted != null) {
                    counter = courseCompleted.size();
                }
            %>


            <!--User information-->
            <div class="col-sm-9 col-md-9 col-lg-9">

                <div class="content">
                    
                    <!--Slider Tab-->
                    <%@include file="includes/slider_user.jsp"%>

                    <!-- User Info-->

                    <!--User info comes here (Sample below) -->
                    <div id="table1" style="padding-left: 20px;">
                        <h3><%=theStudent2.getName()%></h3><hr>
                        
                        <table class="table-responsive" id = "detailedInfo">

                            <tr><td><b>User ID:</b></td><td><%=theStudent2.getUserId()%></td></tr>
                            <tr><td><b>School:</b></td><td><%=theStudent2.getSchool()%></td></tr>
                            <tr><td><b>E-Dollars:</b></td><td>$<%=theStudent2.getEDollars()%></td></tr>
                            <tr>
                                <td><b>Course Completed:</b></td>
                                <%
                                    int courseCompletedCounter = 0;
                                    if (courseCompleted == null || courseCompleted.isEmpty()) {%>
                                <td>None</td>
                                <%} else {
                                    for (String course : courseCompleted) {
                                        courseCompletedCounter++;

                                    }%>
                                <td>
                                    <%=courseCompletedCounter%>
                                </td>
                                <%}%>
                            </tr>
                        </table>
                    </div>




                    <!--Course completed division-->
                    <div  id="table2">
                        <!--User info comes here (Sample below) -->
                        
                            <h3>Course Completed</h3><hr>
                            <table class="table table-responsive table-striped table-bordered">
                                <tr><th>Course Completed</th></tr>
                                        <%
                                            if (courseCompleted == null || courseCompleted.isEmpty()) {%>
                                <tr><td colspan="2"> You have not completed any course.  
                                        <a href="bid.jsp">Click to bid for a course.</a> 
                                    </td></tr>
                                    <%} else {
                                        for (String course : courseCompleted) {
                                            Course theCourse = CourseDAO.retrieveByCode(course);%>
                                <tr><td colspan="2">
                                        <%=theCourse.getTitle()%> (<%=theCourse.getCourse()%>)
                                    </td></tr>
                                    <%}
                                        }
                                    %>
                            </table>

                        </div>

                    
                </div>



                <!--End of global division-->
            </div>
            <!--For Round information / Bidded Course / Enrolled course-->
            <%@include file="includes/otherinformation.jsp"%>

        </div>

        <!--Footer-->
        <%@include file="includes/footer.jsp"%>
        <!--For usage later-->

        <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
        <script src="//code.jquery.com/jquery.js"></script>

        <!--Jquery Library-->
        <script src="http://code.jquery.com/jquery-1.9.1.js"></script>
        <script src="http://code.jquery.com/ui/1.10.3/jquery-ui.js"></script>

        <!-- Include all compiled plugins (below), or include individual files as needed -->
        
        <script src="js/app.js"></script>
    </body>
</html>






