<%@include file="protect.jsp"%>
<%@page import="com.app.utility.validation.CRUDValidationTool"%>
<%@page import="com.app.model.SectionDAO"%>
<%@page import="com.app.utility.validation.CRUDValidator"%>
<%@page import="com.app.model.Section"%>
<%@page import="com.app.model.CourseDAO"%>
<%@page import="com.app.model.Course"%>
<%@page import="java.text.SimpleDateFormat"%>
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
        <link href="css/search.css" rel="stylesheet">

        <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
        <script src="//code.jquery.com/jquery.js"></script>
        
        <!-- Include all compiled plugins (below), or include individual files as needed -->
        <script src="js/bootstrap.min.js"></script>

        <!--Jquery Library-->
        <script src="http://code.jquery.com/jquery-1.9.1.js"></script>
        <script src="http://code.jquery.com/ui/1.10.3/jquery-ui.js"></script>

        <!--Css for Jquery UI-->
        <link href="http://code.jquery.com/ui/1.10.3/themes/smoothness/jquery-ui.css" rel="stylesheet" media="screen">

        <!--Pagination-->
        <script src="js/smartpaginator.js"></script>
        <link href="css/smartpaginator.css" rel="stylesheet" media ="screen">

        <!--typeahead-->
        <script src="js/typeahead.min.js"></script>
        
        <link href="css/typeahead.css" rel="stylesheet" media="screen">
        
        <!-- Include all compiled plugins (below), or include individual files as needed -->
        <link href='http://fonts.googleapis.com/css?family=Roboto:400,100,100italic,300,300italic,400italic,500,500italic,700' rel='stylesheet' type='text/css'>

        <link rel="shortcut icon" href="img/logo_icon.png">

        <title>BIOS - Search</title>
    </head>

    <body id="bid">
        <!--Navigation bar-->
        <%@include file="includes/navigationbar.jsp"%>

        <!--Search bar-->
        <div style="margin-top: 14px">
            <%@include file="includes/searchbar.jsp" %>
        </div>

        <!--Main content comes here-->
        <div class="col-sm-12 col-md-12  col-lg-12">


            <%
                String userid2 = (String) session.getAttribute("userid");
                Student theStudent2 = StudentDAO.retrieveById(userid2);


            %>

            <%
                ArrayList<Course> courseList = CourseDAO.retrieveAll();

            %>

            <!--Course List-->
            <div class="col-sm-9 col-md-9 col-lg-9">
                <div class="content">

                    <div id="searchresult">
                        <table class ="paginated" id="paginated">
                            <tr id="header">
                                <th>Course Code</th>
                                <th>Course Title</th>
                                <th>School</th>
                                <th>Exam Date</th>
                                <th>Offered Sections</th>
                                <th>Status</th>
                                <th>View Details</th>
                            </tr>
                            <%
                                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                                ArrayList<Course> searchedCourseList = (ArrayList<Course>) request.getAttribute("courseList");
                                if (searchedCourseList != null) {
                                    courseList.clear();
                                    courseList = searchedCourseList;
                                }

                                for (Course course : courseList) {

                                    //CRUDValidationTool tool = new CRUDValidationTool();
                                    //boolean completed = tool.hasCompletedPrerequisites(theStudent2.getUserId(), course.getCourse());

                                    ArrayList<String> statusList = new ArrayList<String>();
                                    CRUDValidator validator = new CRUDValidator(statusList);
                                    String reason = validator.getReasonIfNotBiddableCourse(theStudent2, course);

                                    String status = null;
                                    if (reason == null) {
                                        status = "Biddable";
                                    } else {
                                        status = "Not Biddable";
                                    }

                            %>

                            <%
                                if (status.equals("Not Biddable")) {
                            %>
                            <tr class="data" style="background-color: #e8e7e6" name ="course" value="<%= course.getCourse()%>">
                                <%
                                } else {
                                %>
                            <tr class="data" name ="course" value="<%= course.getCourse()%>">
                                <%
                                    }
                                %>

                                <td><%= course.getCourse()%></td>
                                <td><%= course.getTitle()%></td>
                                <td><%= course.getSchool()%></td>
                                <td><%= sdf.format(course.getExamDate())%></td>
                                <%
                                    ArrayList<Section> sectionList = SectionDAO.retrieveByCourseId(course.getCourse());
                                    int noOfSection = sectionList.size();
                                %>
                                <td><%= noOfSection%></td>

                                
                                <%
                                    if (reason == null) {%>
                                <td><%=status%></td>
                                <%} else{
                                %>
                                <td title="<%=reason%>" id="status"><%=status%></td>
                                <%}%>

                                <td>
                                    <form action="SearchSectionServlet" method="GET">
                                        <input type="submit" value="View" id="view_button" class="btn btn-sm btn-primary">
                                        <input type="hidden" value="<%= course.getCourse()%>" name="course"/>
                                    </form>
                                </td>
                            </tr>
                            <%
                                }
                            %>
                        </table>

                        <br>

                        <!--End of Course List-->
                        <%                            int noOfData = courseList.size();
                            int dataPerPage = 10;
                            int noOfPageToDisplay = noOfData / dataPerPage;
                            if (noOfPageToDisplay<

                            
                                4) {
                                if (noOfPageToDisplay == 0) {
                                    noOfPageToDisplay = 1;
                                } else {
                                    if ((noOfData % dataPerPage) > 0) {
                                        noOfPageToDisplay++;
                                    }
                                }
                            }

                            
                                else {
                                noOfPageToDisplay = 3;
                            }
                        %>

                        <!--Initiate label for not biddable-->
                        <label for="status"></label>

                        <script>
                            //Tooltip
                            $(function() {
                                // getter
                                var position = $(".selector").tooltip("option", "position");

                                // setter
                                $(".selector").tooltip("option", "position", {my: "left+15 center", at: "right center"});

                                $(document).tooltip();
                            });
                        </script>


                        <script type="text/javascript">


                            $(function() {
                                $('#searchresult').smartpaginator({datacontainer: 'searchresult', dataelement: 'tr', totalrecords: <%= noOfData%>, length: <%= noOfPageToDisplay%>, recordsperpage: <%= dataPerPage%>, initval: 0, next: 'Next', prev: 'Prev', first: 'First', last: 'Last', theme: 'green', onchange: onChange
                                });
                                function onChange(newPageValue) {
                                    //alert(newPageValue);
                                }
                            });
                        </script>

                        <%                            courseList  = CourseDAO.retrieveAll();
                        %>


                    </div>

                </div>
            </div>

            <!--For Round information / Bidded Course / Enrolled course-->
            <%@include file="includes/otherinformation.jsp"%>
        </div>

        <!--For the footer-->
        <%@include file="includes/footer.jsp"%>


    </body>
</html>