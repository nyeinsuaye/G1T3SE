<%@page import="com.app.model.StudentDAO"%>
<%@page import="com.app.model.Student"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.app.model.CourseDAO"%>
<%@page import="com.app.model.Course"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>





<nav class="navbar navbar-default" role="navigation" id='theSearchBar'>
    <div class="col-sm-12 col-md-12 col-lg-12" id="searchbar">
        <!--Search bar comes here (Sample below) -->
        <form action="/search.html">
            <div class="input-group col-sm-7 col-md-7  col-lg-offset-1 col-lg-7" >
                <input type="text" class="form-control" id="keyIn" name="courseKey" placeholder="Search Course Here...">
                <span class="input-group-btn">
                    <button class="btn btn-primary" type="submit">Search</button>
                </span>
            </div>
        </form>

        <!--Get the logged-in student-->
        <%
            String userid3 = (String) session.getAttribute("userid");
            Student theStudent3 = StudentDAO.retrieveById(userid3);
            String studentSchool = theStudent3.getSchool();
        %>

        <div class="col-sm-5 col-md-5 col-lg-4" style='padding-top: 10px'>
            <!--Student's own faculty and other faculty-->

            <div class="btn-group pull-right">

                <button type="button" class="btn btn-default">
                    <a href="/search-course-id.do?school=<%=studentSchool%>"><%=studentSchool%></a>
                </button>

                <!--Check for number of schools-->
                <%
                    ArrayList<Course> coursesAvailable = CourseDAO.retrieveAll();
                    ArrayList<String> theSchools = new ArrayList<String>();
                    for (Course courseAvailable : coursesAvailable) {
                        if (!theSchools.contains(courseAvailable.getSchool()) && !studentSchool.equals(courseAvailable.getSchool())) {
                            theSchools.add(courseAvailable.getSchool());
                        }
                    }

                %>

                <div class="btn-group">
                    <button type="button" class="btn btn-primary dropdown-toggle" data-toggle="dropdown">
                        <font style='color:white'>Other faculties</font>
                        <span class="caret"></span>
                    </button>
                    <ul class="dropdown-menu">
                        <%
                            for (String faculty : theSchools) {%>
                        <li><a href="/search-course-id.do?school=<%=faculty%>"><%=faculty%></a></li>
                            <%}
                            %>

                    </ul>
                </div>
            </div>
            <div class='pull-right' style="padding: 7px">Search By School:</div>
        </div>

    </div>
</nav>

<!--Script for Typeahead-->
<script type="text/javascript">
    var courses = [];
    <%
        ArrayList<Course> courseList1 = CourseDAO.retrieveAll();
        for (Course c : courseList1) {
    %>
    courses.push("<%= c.getCourse() + '-' + c.getTitle()%>");
    <%
        }
    %>
    $('#keyIn').typeahead(
            {
                name: 'courses',
                minLength: 1,
                local: courses,
                limit: 5
            }
    );
</script>
<!--End of typeahead-->