
<%@page import="com.app.model.two.MinimumPrice"%>
<%@page import="com.app.model.two.MinimumPriceDAO"%>
<%@page import="com.app.model.FailedBidDAO"%>
<%@include file="protect.jsp"%>
<%@page import="com.app.model.two.SuddenDeathDAO"%>
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
        
        <!--typeahead-->
        <script src="js/typeahead.min.js"></script>
        <link href="css/typeahead.css" rel="stylesheet" media="screen">
        
        <!--Logo-->
        <link rel="shortcut icon" href="img/logo_icon.png">
        <title>BIOS - Timetable</title>
    </head>
    
    <body id="timetable" onload="selectDiv(1)">
       
        <!--Navigation bar-->
        <%@include file="includes/navigationbar.jsp"%>
        <div style="margin-top: 14px">
         
            <!--Search bar-->
            <%@include file="includes/searchbar.jsp" %>
        </div>
        
        <!--Main content comes here-->
        <div class="col-sm-12 col-md-12 col-lg-12">
            <%
                
                ArrayList<Bid> list = BidDAO.retrieveByUserId(theStudent.getUserId());
                ArrayList<Bid> suddenDeathList = SuddenDeathDAO.retrieveByUserId(theStudent.getUserId());
                
                list.addAll(suddenDeathList);
                
                Bid bid = null;
                String sect = null;
                String c = null;
                Course course = null;
                Section section = null;
                HashMap<Integer, Section> lessonTimeMorn = new HashMap<Integer, Section>();
                HashMap<Integer, Section> lessonTimeAft = new HashMap<Integer, Section>();
                HashMap<Integer, Section> lessonTimeEven = new HashMap<Integer, Section>();
                HashMap<Integer, String> colors = new HashMap<Integer, String>();
                String approved = "#9ded8c";
                String pending = "#fffeb0";
                String failed = "#D13F31";
                
                ArrayList<Bid> approvedBids = SectionStudentDAO.retrieveByUserId(theStudent.getUserId());
                ArrayList<Bid> failedBids = FailedBidDAO.retrieveByUserId(theStudent.getUserId());
                
                ArrayList<Bid> passSuddenDeath = SuddenDeathDAO.retrieveByUserIdPass(theStudent.getUserId());
                ArrayList<Bid> failSuddenDeath = SuddenDeathDAO.retrieveByUserIdFail(theStudent.getUserId());
                
                
                //Check for all classes time
                list.addAll(approvedBids); //add all successful bid to the bid list to be processed
                for (int i = 0; i < list.size(); i++) {
                    bid = list.get(i);
                    sect = bid.getSection();
                    c = bid.getCode();
                    course = CourseDAO.retrieveByCode(c);
                    section = SectionDAO.retrieveSection(c, sect);
                    String colorCode = pending;
                    //check the bidded course status
                    for (Bid approvedBid : approvedBids) {
                        if (approvedBid.getCode().equals(bid.getCode())) {
                            colorCode = approved;
                        }
                    }
                    
                    if(Round.getRound() == 2){
                        for(Bid failSuddenDeathBid : failSuddenDeath){
                            if (failSuddenDeathBid.getCode().equals(bid.getCode())) {
                                colorCode = failed;
                            }   
                        }
                    }
                    // Check for morning class
                    if (section.getStart().getHours() == 8 && section.getStart().getMinutes() == 30) {
                        
                        //Sort by day
                        if (section.getDay() == 1) {
                            lessonTimeMorn.put(1, section);
                            colors.put(1, colorCode);
                        } else if (section.getDay() == 2) {
                            lessonTimeMorn.put(2, section);
                            colors.put(2, colorCode);
                        } else if (section.getDay() == 3) {
                            lessonTimeMorn.put(3, section);
                            colors.put(3, colorCode);
                        } else if (section.getDay() == 4) {
                            lessonTimeMorn.put(4, section);
                            colors.put(4, colorCode);
                        } else if (section.getDay() == 5) {
                            lessonTimeMorn.put(5, section);
                            colors.put(5, colorCode);
                        } else if (section.getDay() == 6) {
                            lessonTimeMorn.put(6, section);
                            colors.put(6, colorCode);
                        } else if (section.getDay() == 7) {
                            lessonTimeMorn.put(7, section);
                            colors.put(7, colorCode);
                        }
                    }
                    if (section.getStart().getHours() == 12 && section.getStart().getMinutes() == 00) {
                        if (section.getDay() == 1) {
                            lessonTimeAft.put(1, section);
                            colors.put(8, colorCode);
                        } else if (section.getDay() == 2) {
                            lessonTimeAft.put(2, section);
                            colors.put(9, colorCode);
                        } else if (section.getDay() == 3) {
                            lessonTimeAft.put(3, section);
                            colors.put(10, colorCode);
                        } else if (section.getDay() == 4) {
                            lessonTimeAft.put(4, section);
                            colors.put(11, colorCode);
                        } else if (section.getDay() == 5) {
                            lessonTimeAft.put(5, section);
                            colors.put(12, colorCode);
                        } else if (section.getDay() == 6) {
                            lessonTimeAft.put(6, section);
                            colors.put(13, colorCode);
                        } else if (section.getDay() == 7) {
                            lessonTimeAft.put(7, section);
                            colors.put(14, colorCode);
                        }
                        // Check for Evening class
                    }
                    if (section.getStart().getHours() == 15 && section.getStart().getMinutes() == 30) {
                        if (section.getDay() == 1) {
                            lessonTimeEven.put(1, section);
                            colors.put(15, colorCode);
                        } else if (section.getDay() == 2) {
                            lessonTimeEven.put(2, section);
                            colors.put(16, colorCode);
                        } else if (section.getDay() == 3) {
                            lessonTimeEven.put(3, section);
                            colors.put(17, colorCode);
                        } else if (section.getDay() == 4) {
                            lessonTimeEven.put(4, section);
                            colors.put(18, colorCode);
                        } else if (section.getDay() == 5) {
                            lessonTimeEven.put(5, section);
                            colors.put(19, colorCode);
                        } else if (section.getDay() == 6) {
                            lessonTimeEven.put(6, section);
                            colors.put(20, colorCode);
                        } else if (section.getDay() == 7) {
                            lessonTimeEven.put(7, section);
                            colors.put(21, colorCode);
                        }
                    }
                }
            %>
            <div class="col-sm-9 col-md-9 col-lg-9">
                <div  class="content">
                    <!--Slider Tab-->
                    
                    <%@include file="includes/slider.jsp"%>
                    
                    <!-- Lesson timetable division-->
                    <div  id="timetable1">
                        
                        <!--Lesson Timetable comes here (Sample below) -->
                        <table class="table table-bordered table-responsive">
                            <tr>
                                <th>Time / Date</th>
                                <th>Monday</th>
                                <th>Tuesday</th>
                                <th>Wednesday</th>
                                <th>Thursday</th>
                                <th>Friday</th>
                                <th>Saturday</th>
                                <th>Sunday</th>
                            </tr>
                            <tr>
                                <th style="background-color: #e7e7e7; color:#102E37">08:30 - 10:00</th>
                                    <%
                                        //print all morning class
                                        for (int i = 0; i < 7; i++) {
                                            if (lessonTimeMorn.get(i + 1) != null) {
                                                String code = colors.get(i + 1);
                                                Course courseInList = CourseDAO.retrieveByCode(lessonTimeMorn.get(i + 1).getCourse());
                                                Section sectionChoosen = lessonTimeMorn.get(i + 1);
                                                String title = courseInList.getTitle() + " <br>" + sectionChoosen.getSection() + " <br>  " + sectionChoosen.getInstructor();
                                    %>
                                <td rowspan="2" style="background-color:<%=code%>" >
                                    <div style="height:100%" id="morning" title="<%=title%>">
                                        <%
                                            out.println("<br>");
                                            out.println(sectionChoosen.getCourse());
                                            out.println("(" + sectionChoosen.getSection() + ")");
                                            out.println(section.getVenue());
                                            //check if it is outbid in round 2, to show the clearing price
                                            if(Round.getRound() == 2 && code.equals(failed)){
                                                MinimumPrice mp = MinimumPriceDAO.retrieveBySectionId(sectionChoosen.getCourse(), sectionChoosen.getSection());
                                                out.println("<br>");
                                                out.println("<div class='minPrice'>Minimum bid now : "+mp.getPrice()+"</div>");
                                            }
                                        %>
                                    </div>
                                </td>
                                <%  } else {%>
                                <td></td>
                                <%  }
                                    }
                                %>
                            </tr>
                            <tr>
                                <th style="background-color: #e7e7e7; color:#102E37">10:15 - 11:45</th>
                                    <%
                                        for (int i = 0; i < 7; i++) {
                                            if (lessonTimeMorn.get(i + 1) == null) {
                                    %>
                                <td></td>
                                <%      }
                                    }
                                %>
                            </tr>
                            <tr>
                                <th style="background-color: #e7e7e7; color:#102E37">12:00 - 13:30</th>
                                    <%
                                        //print all afternoon class
                                        for (int i = 0; i < 7; i++) {
                                            if (lessonTimeAft.get(i + 1) != null) {
                                                String code = colors.get(i + 8);
                                                Course courseInList = CourseDAO.retrieveByCode(lessonTimeAft.get(i + 1).getCourse());
                                                Section sectionChoosen = lessonTimeAft.get(i + 1);
                                                String title = courseInList.getTitle() + " <br>" + sectionChoosen.getSection() + " <br>  " + sectionChoosen.getInstructor();
                                    %>
                                <td rowspan="2" style="background-color:<%=code%>"> 
                                    <div style="height:100%" id="afternoon" title="<%=title%>">
                                        <%
                                            out.println("<br>");
                                            out.println(sectionChoosen.getCourse());
                                            out.println("(" + sectionChoosen.getSection() + ")");
                                            out.println(section.getVenue());
                                            if(Round.getRound() == 2 && code.equals(failed)){
                                                MinimumPrice mp = MinimumPriceDAO.retrieveBySectionId(sectionChoosen.getCourse(), sectionChoosen.getSection());
                                                out.println("<br>");
                                                out.println("<div class='minPrice'>Minimum bid now : "+mp.getPrice()+"</div>");
                                            }
                                        %>
                                    </div>
                                </td>
                                <%  } else {%>
                                <td></td>
                                <%  }
                                    }
                                %>
                            </tr>
                            <tr>
                                <th style="background-color: #e7e7e7; color:#102E37">13:45 - 15:15</th>
                                    <%
                                        for (int i = 0; i < 7; i++) {
                                            if (lessonTimeAft.get(i + 1) == null) {
                                    %>
                                <td></td>
                                <%      }
                                    }
                                %>
                            </tr>
                            <tr>
                                <th style="background-color: #e7e7e7; color:#102E37">15:30 - 17:00</th>
                                    <%
                                        //print all evening class
                                        for (int i = 0; i < 7; i++) {
                                            if (lessonTimeEven.get(i + 1) != null) {
                                                String code = colors.get(i + 15);
                                                Course courseInList = CourseDAO.retrieveByCode(lessonTimeEven.get(i + 1).getCourse());
                                                Section sectionChoosen = lessonTimeEven.get(i + 1);
                                                String title = courseInList.getTitle() + " <br>" + sectionChoosen.getSection() + " <br>  " + sectionChoosen.getInstructor();
                                    %>
                                <td rowspan="2" style="background-color:<%=code%>">
                                    <div style="height:100%" id="evening" title="<%=title%>">
                                        <%
                                            out.println("<br>");
                                            out.println(sectionChoosen.getCourse());
                                            out.println("(" + sectionChoosen.getSection() + ")");
                                            out.println(section.getVenue());
                                            if(Round.getRound() == 2 && code.equals(failed)){
                                                MinimumPrice mp = MinimumPriceDAO.retrieveBySectionId(sectionChoosen.getCourse(), sectionChoosen.getSection());
                                                out.println("<br>");
                                                out.println("<div class='minPrice'>Minimum bid now : "+mp.getPrice()+"</div>");
                                            }
                                        %>
                                    </div>
                                </td>
                                        <%  } else {%>
                                        <td></td>
                                        <%  }       
                                        }
                                %>
                            </tr>
                            <tr>
                                <th style="background-color: #e7e7e7; color:#102E37">17:15 - 18:45</th>
                                    <%
                                        for (int i = 0; i < 7; i++) {
                                            if (lessonTimeEven.get(i + 1) == null) {
                                    %>
                                <td></td>
                                <%      }
                                    }
                                %>
                            </tr>
                        </table>  
                        <!--Legend color code-->
                        <tfoot>
                        <font style="float:right">Enrolled</font><div class="success"></div>
                        <font style="float:right; position: relative; left:-7px;">Pending</font><div class="pending"></div>
                        <%
                           if(Round.getRound()==2){
                        %>
                        <font style="float:right; position: relative; left:-10px;">OutBid</font><div class="outBid"></div>
                        <%
                        }
                        %>
                        </tfoot>
                    </div>
                            
                    <!--Exam Timetable division-->
                    <div id="timetable2">
                        
                        <!--Exam Timetable comes here -->
                        <table class="table table-bordered">
                            <tr>
                                <th>Course Code</th>
                                <th>Course Name</th>
                                <th>Exam Date</th>
                                <th>Exam Start</th>
                                <th>Exam End</th>
                            </tr>
                            <%
                                for (Bid b : list) {
                                    course = CourseDAO.retrieveByCode(b.getCode());
                            %>
                            <tr id="examtimetable">
                                <td><%= course.getCourse()%></td>
                                <td><%= course.getTitle()%></td>
                                <td><%= new SimpleDateFormat("dd-MMM-yyyy").format(course.getExamDate())%></td>
                                <td><%= new SimpleDateFormat("H:mm").format(course.getExamStart())%></td>
                                <td><%= new SimpleDateFormat("H:mm").format(course.getExamEnd())%></td>
                            </tr>
                            <%
                                }
                            %>
                        </table>
                        
                    </div>
                        
                        
                    <!--Bid status division-->
                    <div id="timetable3">
                        
                        <!--Bid Status comes here -->
                        <table class="table table-bordered">
                            <tr>
                                <th>Course Code</th>
                                <th>Course Name</th>
                                <th>Status</th>
                                <th>Bid Amount</th>
                                <th>View Details</th>
                            </tr>
                            <%
                               ArrayList<Bid> bidList = new ArrayList<Bid>();
                               String status = "Pending";
                                if( Round.getRound() == 1){
                                    bidList = list;
                                }else if( Round.getRound() == 0 || Round.getRound() == -1){
                                    bidList = approvedBids;
                                    bidList.addAll(failedBids);
                                    
                                }else if (Round.getRound() == 2){
                                    bidList = list;
                                    bidList.addAll(failedBids);
                                }
                                for (Bid b : bidList) {
                                    if(approvedBids!=null&&approvedBids.contains(b)){
                                        status = "Successful";
                                    }
                                    course = CourseDAO.retrieveByCode(b.getCode());
                                    if(Round.getRound()==0 || Round.getRound() == -1){
                                        if(failedBids!=null&&failedBids.contains(b)){
                                            status = "Unsuccessful";
                                        }
                                    }else if(Round.getRound()==2){
                                        if(failSuddenDeath !=null&&failSuddenDeath.contains(b)){
                                            status = "Please Bid Higher";   //changed msg by zach
                                        }
                                        if(failedBids!=null&&failedBids.contains(b)){
                                            status = "Unsuccessful in Round 1";
                                        }   //added by zach
                                    }
                            %>
                            <tr id="examtimetable">
                                <td><%= course.getCourse()%></td>
                                <td><%= course.getTitle()%></td>
                                <td><%= status %></td>
                                <td><%= b.getAmount()%></td>
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
                        
                    </div>    
                </div>
            </div>
                        
            <!--For Round information / Bidded Course / Enrolled course-->
            <%@include file="includes/otherinformation.jsp"%>
        </div>
            
        <script>
        $(function() {
            $(document).tooltip({
                content: function() {
                    var element = $(this);
                    if (element.is("[title]")) {
                        return element.attr("title");
                    }
                },
                position: {my: "left bottom-3", at: "right bottom"}}
            );
        });
        </script>
        
        <!--Footer-->
        <div style="height: 200px"></div>
    <%@include file="includes/footer.jsp"%>
</body>
</html>