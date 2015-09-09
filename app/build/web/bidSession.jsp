<%@page import="java.math.BigDecimal"%>
<%@page import="com.app.model.two.MinimumPriceDAO"%>
<%@page import="com.app.model.two.MinimumPrice"%>
<%@include file="protect.jsp"%>
<%@page import="com.app.model.two.SuddenDeathDAO"%>
<%@page import="com.app.utility.validation.CRUDValidator"%>
<%@page import="java.util.Date"%>
<%@page import="com.app.model.SectionDAO"%>
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

        <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
        <script src="//code.jquery.com/jquery.js"></script>

        <!-- Include all compiled plugins (below), or include individual files as needed -->
        <script src="js/bootstrap.min.js"></script>
        <link href='http://fonts.googleapis.com/css?family=Roboto:400,100,100italic,300,300italic,400italic,500,500italic,700' rel='stylesheet' type='text/css'>

        <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
        <script src="//code.jquery.com/jquery.js"></script>

        <!--Jquery Library-->
        <script src="http://code.jquery.com/jquery-1.9.1.js"></script>
        <script src="http://code.jquery.com/ui/1.10.3/jquery-ui.js"></script>


        <!--Css for Jquery UI-->
        <link href="http://code.jquery.com/ui/1.10.3/themes/smoothness/jquery-ui.css" rel="stylesheet" media="screen">
        <link href='http://fonts.googleapis.com/css?family=Roboto:400,100,100italic,300,300italic,400italic,500,500italic,700' rel='stylesheet' type='text/css'>

        <script src='js/smartpaginator.js'></script>
        <link href='css/smartpaginator.css' rel='stylesheet' media='screen'>

        <!--typeahead-->
        <script src="js/typeahead.min.js"></script>
        <link href="css/typeahead.css" rel="stylesheet" media="screen">

        <link rel="shortcut icon" href="img/logo_icon.png">
        <title>BIOS - Sessions</title>
    </head>
    <body onload="selectDiv(1)">


        <!--Navigation bar-->
        <%@include file="includes/navigationbar.jsp"%>
        <!--space break-->

        <!--Search bar-->
        <div style="margin-top: 14px">
            <%@include file="includes/searchbar.jsp" %>
        </div>



        <!--Main content comes here-->
        <div class="col-sm-12 col-md-12 col-lg-12">

            <!--Get the Student object to get student info: Username and Edollars -->
            <%
                String userid2 = (String) session.getAttribute("userid");
                Student theStudent2 = StudentDAO.retrieveById(userid2);
            %>

            <!--Get search results-->
            <%
                ArrayList<Section> tempSections = (ArrayList<Section>) request.getAttribute("sectionList");
                Course tempCourse = (Course) request.getAttribute("course");

                //if null, don't set.
                // why need to set to the session??
                if (tempSections != null || tempCourse != null) {
                    session.setAttribute("previousCourse", tempCourse);
                    session.setAttribute("previousSections", tempSections);
                }
                ArrayList<Section> sections = (ArrayList<Section>) session.getAttribute("previousSections");
                Course course = (Course) session.getAttribute("previousCourse");
            %>

            <!--Bidding Round Number-->
            <%
                int roundNum = Round.getRound();
            %>


            <!--Content box-->
            <div class="col-xs-12 col-sm-9 col-md-9 col-lg-9">

                <div class="content">

                    <!--prerequisite slider-->
                    <%@include file="includes/slider_prerequisite.jsp" %>


                    <!--Search Result Details-->
                    <div id="SearchResult1">

                        <!--Course Title-->
                        <h3><%=course.getTitle()%> <small>(<%=course.getCourse()%>)</small></h3>

                        <hr style="height:1px; background-color: lightgray">

                        <%
                            SimpleDateFormat sdfExamDate = new SimpleDateFormat("dd/MM/yyyy");
                            SimpleDateFormat sdfExamTime = new SimpleDateFormat("H:mm");
                            
                            ArrayList<MinimumPrice> minimumPriceList = new ArrayList<MinimumPrice>();
                        %>

                        <!--Course Detailed Info-->
                        <table id="detailedInfo">
                            <tr><td><b>Course code:</b></td><td><%=course.getCourse()%></td><td><b>Exam Date:</b></td><td><%=sdfExamDate.format(course.getExamDate())%></td></tr>
                            <tr><td><b>Course title:</b></td><td><%=course.getTitle()%></td><td><b>Exam Start:</b></td><td><%=sdfExamTime.format(course.getExamStart())%></td></tr>
                            <tr><td><b>School:</b></td><td><%=course.getSchool()%></td><td><b>Exam Start:</b></td><td><%=sdfExamTime.format(course.getExamEnd())%></td></tr>
                            <tr><td><b>Description:</b></td><td colspan="3"> <%=course.getDescription()%></td></tr>
                        </table>
                        
                    

                        <!--Section list-->
                        <table class="table table-bordered table-responsive">
                            <tr>
                                <th>Sect.</th>
                                <th>Lesson Day</th>
                                <th>Lesson Duration</th>
                                <th>Instructor</th>
                                <th>Venue</th>
                                <th>Size</th>
                                    <%
                                        if (roundNum == 2) {
                                            minimumPriceList = MinimumPriceDAO.retrieveByCourseId(course.getCourse());
                                    %>        
                                <th>Vaca ncy</th> 
                                <th>Min. Bid Price</th>
                                    <%                                    }
                                    %>
                                <th>Status</th>
                                <th>Bid</th>

                            </tr>
                            <%
                                if (sections != null && !sections.isEmpty()) {
                                    int counter = 0;
                                    int i =0;
                                    for (Section section : sections) {
                                        //Section section = sections.get(i);
                                        SimpleDateFormat sdf = new SimpleDateFormat("H:mm");
                                        Date startTime = section.getStart();
                                        Date endTime = section.getEnd();
                                        String startTimeStr = sdf.format(startTime);
                                        String endTimeStr = sdf.format(endTime);

                                        String theDay = "";
                                        int day = section.getDay();
                                        if (day == 1) {
                                            theDay = "Monday";
                                        }
                                        if (day == 2) {
                                            theDay = "Tuesday";
                                        }
                                        if (day == 3) {
                                            theDay = "Wednesday";
                                        }
                                        if (day == 4) {
                                            theDay = "Thursday";
                                        }
                                        if (day == 5) {
                                            theDay = "Friday";
                                        }
                                        if (day == 6) {
                                            theDay = "Saturday";
                                        }
                                        if (day == 7) {
                                            theDay = "Sunday";
                                        }

                            %>
                            <tr id="sessionDetails">
                                <td><%=section.getSection()%></td>
                                <td><%=theDay%></td>
                                <td><%=startTimeStr + " - " + endTimeStr%></td>
                                <td><%=section.getInstructor()%></td>
                                <td><%=section.getVenue()%></td>      
                                <td><%=section.getSize()%></td>
                                <%
                                    if (roundNum == 2) {
                                        
                                        //looping through minimumPriceList to find matching minimumPriceObj
                                        MinimumPrice minimumPriceObj = null;
                                        for (MinimumPrice priceObj : minimumPriceList) {
                                            if (section.getSection().equals(priceObj.getSection())) {
                                                minimumPriceObj = priceObj;
                                            }
                                        }
                                        
                                        //MinimumPriceDAO.retrieveBySectionId(section.getCourse(), section.getSection());
                                %> 
                                <td><%=minimumPriceObj.getVacancyLeft()%></td>
                                <td>$<%=minimumPriceObj.getPrice()%></td>
                                <%
                                    }
                                %>
                                <%
                                    ArrayList<String> statusList = new ArrayList<String>();
                                    CRUDValidator validator = new CRUDValidator(statusList);
                                    String reason = validator.getReasonIfNotBiddable(theStudent2, section);

                                    String status = null;
                                    if (reason == null) {
                                        status = "Biddable";
                                    } else {
                                        status = "Not Biddable";
                                    }

                                %>

                                <%
                                    if (status.equals("Biddable")) {
                                %>
                                <td><span class="label label-success"><%=status%></span></td>
                                    <%} else {%>
                                <td id="status" title="<%=reason%>"><span class="label label-default"><%=status%></span></td>
                                    <%
                                        }
                                    %>
                                    <%
                                        if (status.equals("Not Biddable")) {%>
                                <td><input type="button" class="btn" style="background-color:grey; color:white" value="Bid"></td>
                                    <%} else {%>
                                <td><input type="button" class="btn btn-success" onClick="addBid('theBids<%=i%>','add-bid.do?course=<%=course.getCourse()%>&section=<%=section.getSection()%>', '<%=i%>');" value="Bid"/></td>
                                    <%}
                                    %>

                            </tr>

                            

<%--THE MODAL--%>
                            <div id="theBids<%= i%>" title="Bid for <%=course.getTitle()%>" hidden="true" style="font-family: 'Roboto', sans-serif; font-weight: 300;">
                                <p class="validateTips">*Input field only accepts numeric values.</p><br>

                                <form action="add-bid" method="post">
                                    
                                
                                <fieldset>
                                    <%
                                        MinimumPrice minimumPriceObj = null;
                                    %>
                                    <b>Course:</b> <%=section.getCourse()%> <br>
                                    <b>Section:</b> <%=section.getSection()%> <br>

                                    <%
                                        if (roundNum == 2) {
                                            
                                            for (MinimumPrice priceObj : minimumPriceList) {
                                                if (section.getSection().equals(priceObj.getSection())) {
                                                    minimumPriceObj = priceObj;
                                                }
                                            }
                                                    
                                            //MinimumPriceDAO.retrieveBySectionId(section.getCourse(), section.getSection());

                                    %>        
                                    <b>Vacancy: <%=minimumPriceObj.getVacancyLeft()%></b></br>
                                    <b>Minimum Price to Bid: $<%=minimumPriceObj.getPrice()%></b></br>
                                    <%
                                        }
                                    %>
                                    <b>EDollars ($):</b>
                                        
                                    <input type="number" id="amount<%=i%>" name="amount" class="text ui-widget-content ui-corner-all" min="10" placeholder="10"/><br>
                                        <span class="error<%=i%>" aria-live="polite"></span>
                                        <input type="hidden" id="course" name="course" value="<%=section.getCourse()%>">
                                        <input type="hidden" id="section" name="section" value="<%=section.getSection()%>">        
                                        <!-- js/clientvalidation of min price -->
                                        <%
                                        
                                            BigDecimal minPrice = BigDecimal.TEN;
                                            if (roundNum == 2) {
                                                minPrice = minimumPriceObj.getPrice();
                                            }
                                        %>
                                        <input type="hidden" id="minPrice<%=i%>" value="<%=minPrice%>" />
                                        <input type="hidden" id="roundNum" value="2" />
                                        
                                </fieldset>
 </form>

                                <!--deleted the ranking details for round 2-->
                            </div>
                            <%
                                        i++;
                                }
                                i = 0;
                            } else {%>
                            <tr><td colspan="7" style="text-align:center">No Sections Available</td></tr>
                            <%}
                            %>
                        </table>
                    </div>


                    <%
                        ArrayList<String> prerequisites = course.getPrerequisites();
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    %>

                    <!--Course Prerequisites-->
                    <div id="SearchResult2">
                        <h3><%=course.getTitle()%> <small>(<%=course.getCourse()%>)</small></h3><hr style="height:1px; background-color: lightgray">
                        <table class="table table-bordered table-responsive">
                            <tr style="background-color:#F3F7F7">
                                <th>Course Code</th>
                                <th>Course Title</th>
                                <th>School</th>
                                <th>Status</th>
                                <th>View Details</th>
                            </tr>
                            <%



                                if (prerequisites != null && !prerequisites.isEmpty()) {
                                    for (int i = 0; i < prerequisites.size(); i++) {
                                        String thePrerequisite = prerequisites.get(i);
                                        Course coursePrerequisite = CourseDAO.retrieveByCode(thePrerequisite);
                                        ArrayList<Section> sectionList = SectionDAO.retrieveByCourseId(course.getCourse());
                                        int noOfSection = sectionList.size();

                                        boolean completed = false;
                                        ArrayList<String> courseCompleted = theStudent2.getCourseCompleted();
                                        if (courseCompleted.contains(thePrerequisite)) {
                                            completed = true;
                                        }

                                        String status = null;
                                        if (completed) {
                                            status = "Completed";
                                        } else {
                                            status = "Not Completed";
                                        }


                            %>
                            <tr>
                                <td><%=thePrerequisite%></td>
                                <td><%=coursePrerequisite.getTitle()%></td>
                                <td><%=coursePrerequisite.getSchool()%></td>
                                <td><%=status%></td>
                                <td>
                                    <form action="SearchSectionServlet" method="GET">
                                        <input type="submit" value="View" id="view_button" class="btn btn-sm btn-primary">
                                        <input type="hidden" value="<%= coursePrerequisite.getCourse()%>" name="course"/>
                                    </form>
                                </td>
                            </tr>
                            <%}
                            } else {%>
                            <tr><td colspan="6" style="background-color:white">This course does not have any prerequisites</td></tr>
                            <%}
                            %>
                        </table>
                    </div>



                    <!--Initiate label for updateBid-->
                    <label for="status"></label>

                    <script>
                        //check if the input value is float
                        $('input[name=amount]').keydown(function(event) {
                            var el = $(event.currentTarget), cvalue = el.val(), nvalue;
                            var k = event.which;

                            //	if(event.altKey||event.shiftKey||event.ctrlKey||event.metaKey){
                            if (event.altKey || event.ctrlKey || event.metaKey) {
                                event.preventDefault();
                                return;
                            }

                            if ((k >= 48 && k <= 57) || (k >= 96 && k <= 105) || k == 190 || k == 110 || k == 109 || k == 59) {

                                var k2 = k;
                                if (k >= 96 && k <= 105) {
                                    k2 = k - 48;
                                }

                                nvalue = cvalue.slice(0, el.getCaretPosition()) + (k2 == 190 || k2 == 110 || k2 == 59 ? '.' : (k2 == 109 ? '-' : String.fromCharCode(k2))) + cvalue.slice(el.getCaretPosition());
                                if (parseFloat(nvalue) === Number.NaN || !/^-?\d+(\.\d*)?$/.test(nvalue)) {
                                    event.preventDefault();
                                }
                            } else if (k == 189 || k == 8 || k == 46 || k == 37 || k == 39 || k == 9 || (k >= 35 && k <= 40)) {

                            } else {
                                event.preventDefault();
                            }
                        });
                        //Add bid for a course
                        function addBid(var1, var2, var3) {
                            var targetUrl = var2;
                            var id = var3
                            var bidAmt = null; //$('#amount' + id).val();   //change from var3 to id
                            var minPrice = $('#minPrice' + id);
                            
                            $(function() {
                                bidAmt = $('#amount' + var3);
                             
                            });

                            $('#' + var1).dialog({
                                resizable: false,
                                height: 400,
                                width: 400,
                                modal: true,
                                autoOpen: true,
                                draggable: false,
                                buttons: {
                                    "Add Bid": function() {
                                        
                                        $('.error' + id).empty();
                                        
                                        if (bidAmt.val() < 10) {
                                            var errorMsg = '<div class="alert alert-danger"><b>Invalid value:</b> Please bid at least $10.</div>';
                                            $('.error' + id).append(errorMsg);

                                        } else if (parseFloat(bidAmt.val()) < parseFloat(minPrice.val())) {
                                            var errorMsg = '<div class="alert alert-danger"><b>Invalid value:</b> Please bid higher than the minimum price.</div>';
                                            $('.error' + id).append(errorMsg);
                                        } else {
                                            window.location.href = var2 + '&amount=' + bidAmt.val();
                                            $(this).dialog("close");
                                        }
                                    },
                                    Cancel: function() {
                                        $(this).dialog("close");
                                    }
                                }
                            });
                            $('#' + var1).dialog("open");
                        }
                        ;

                        //Tooltip
                        $(function() {
                            $(document).tooltip();
                        });

                    </script>


                    <!--End of Course List-->
                    <br>

                </div>
            </div>

            <!--For Round information / Bidded Course / Enrolled course-->
            <%@include file="includes/otherinformation.jsp"%>
        </div>

        <!--For the footer-->
        <div style="height: 200px"></div>
        <%@include file="includes/footer.jsp"%>

    </body>
</html>
