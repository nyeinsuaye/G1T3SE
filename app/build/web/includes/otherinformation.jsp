
<%@page import="com.app.model.two.SuddenDeathDAO"%>
<%@page import="com.app.model.Round"%>
<%@page import="com.app.model.CourseDAO"%>
<%@page import="com.app.model.Course"%>
<%@page import="com.app.model.StudentDAO"%>
<%@page import="com.app.model.Student"%>
<%@page import="com.app.model.SectionStudentDAO"%>
<%@page import="com.app.model.BidDAO"%>
<%@page import="com.app.model.Bid"%>
<%@page import="java.util.ArrayList"%>

<!--For Round information / Bidded Course / Enrolled course-->
<div class="col-sm-3  col-md-3 col-lg-3" id="infocontainer">

    <!--Get the logged-in student-->
    <%
        String userid1 = (String) session.getAttribute("userid");
        Student theStudent1 = StudentDAO.retrieveById(userid1);
    %>

    <!--Get existing Bids of Student-->
    <%
        ArrayList<Bid> bidsOfStudent = BidDAO.retrieveByUserId(theStudent1.getUserId());
        ArrayList<Bid> suddenDeathBids = SuddenDeathDAO.retrieveByUserId(theStudent1.getUserId());
        bidsOfStudent.addAll(suddenDeathBids);
    %>
    <!--Get enrolledCourse of Student-->
    <%
        ArrayList<Bid> enrolledCourse = SectionStudentDAO.retrieveByUserId(theStudent1.getUserId());
    %>
    <%
        int roundNumber = Round.getRound();
        String roundNumberStr = "";
        if (roundNumber == 1) {
            roundNumberStr = "Round One";
        } else if (roundNumber == 2) {
            roundNumberStr = "Round Two";
        } else if (roundNumber <= 0) {
            roundNumberStr = "Bidding Window Closed";
        }
    %>

    <!--Round Status-->
    <table class="table" id="otherinfo">
        <tr>
            <th colspan="3">Round Status</th>
        </tr>
        <tr>
            <td colspan="3"><%=roundNumberStr%></td>
        </tr>
    </table>

    <!--Bidded Courses-->
    <table id="otherinfo">
        <tr>
            <th colspan="3">Bidded Courses</th>
        </tr>
        <%
            int counterBids = 0;
            if (bidsOfStudent != null && !bidsOfStudent.isEmpty()) {
                for (Bid bids : bidsOfStudent) {%>
        <tr>
            <td colspan="3" class="coursecode">
                <b><%=bids.getCode()%> - <%= bids.getSection()%></b>
            </td>
        </tr>
        <tr class="buttonbox">    
            <td>
                <%
                    if (roundNumber <= 0) {%>
                <input  type="text" size="4" id="updateBidVoid" name="amount" placeholder="<%=bids.getAmount()%>" title="You cannot update a bid after bidding round has stopped">   
                <input type="hidden" name="section" value="<%=bids.getSection()%>">
                <input type="hidden" name="course" value="<%=bids.getCode()%>"></td>
            <td><input type="submit" class="btn btn-sm btn-primary" value="Update"  title="You cannot update a bid after bidding round has stopped"></td>
                <%} else {%>
        <form action="edit-bid.do" method="get">
            <input type="text" size="4" id="updateBid" name="amount" placeholder="<%=bids.getAmount()%>" title="Please only input values greater than 10." min="10">      
            <input type="hidden" name="section" value="<%=bids.getSection()%>">
            <input type="hidden" name="course" value="<%=bids.getCode()%>"></td>
            <td><input type="submit" class="btn btn-sm btn-primary" value="Update"></td>
        </form>
        <%
            }
        %>
        </td>

        <!--Button for drop bid-->
        <%
                if (roundNumber <= 0) {%>
        <td class="dropbid"><input type="button" class="btn btn-sm" id="dropButton" value="Drop" style="background-color:grey; color:white"></td>
            <%} else {%>
        <td class="dropbid"><input type="button" class="btn btn-sm" id="dropButton" onclick="dropBid('bids' + <%=counterBids%>, 'delete-bid.do?course=<%=bids.getCode()%>&section=<%=bids.getSection()%>')" id="dropBid" value="Drop"></td>
            <%}
            %>
        </tr>
        <%
                counterBids++;
            }
        } else {%>
        <tr><td>No Bidded Courses</td></tr>
        <%}
        %>
    </table><br>


    <!--Enrolled Courses-->
    <table class="table" id="otherinfo">
        <tr style="background: #000000; color:whitesmoke">
            <th colspan="3">Enrolled Courses</th>
        </tr>
        <%
            int numEnrolled = 0;
            if (enrolledCourse != null && !enrolledCourse.isEmpty()) {
                numEnrolled = enrolledCourse.size();
            }
        %>

        <!--Information-->
        <tr>
            <td colspan="3" style="text-align:center">Enrollment: <strong><%=numEnrolled%> / 5 </strong></td>
        </tr>
        <%
            int counterEnrolled = 0;
            if (enrolledCourse != null && !enrolledCourse.isEmpty()) {
                for (Bid enrolledBids : enrolledCourse) {
        %>
        <tr> 
            <td style="text-align:center; padding-top: 12px;"><b><%=enrolledBids.getCode()%> - <%=enrolledBids.getSection()%> </b></td>
            <%
                if (roundNumber <= 0) {%>
            <td class="dropbid"><input type="button" class="btn btn-sm" id="dropButton" id="dropCourse" value="Drop" style="background-color:grey; color:white"></td>
                <%} else {%>
            <td class="dropbid"><input type="button" class="btn btn-sm" id="dropButton" onclick="dropCourse('enrolled' + <%=counterEnrolled%>, 'drop-section.do?course=<%=enrolledBids.getCode()%>&section=<%=enrolledBids.getSection()%>');" id="dropCourse" value="Drop"></td>
                <%}
                %>    
        </tr>
        <%
                counterEnrolled++;
            }
        } else {%>
        <tr><td style="border: solid #dadada 1px;">No Enrolled Courses</td></tr>
        <%}
        %>
    </table>
    <!--Number of enrolled courses-->
</div>


<!--Modal Scripts-->


<!--Drop Student Bid-->
<%
    if (bidsOfStudent != null && !bidsOfStudent.isEmpty()) {
        for (int i = 0; i < bidsOfStudent.size(); i++) {
            Bid studentBidTemp = bidsOfStudent.get(i);
            Course theCourse = CourseDAO.retrieveByCode(studentBidTemp.getCode());
%>
<div id="bids<%=i%>" title="Drop Bid?" hidden="true" style="font-family: 'Roboto', sans-serif; font-weight: 300;">
    <p><span class="ui-icon ui-icon-alert" style="float: left; margin: 0 7px 20px 0;"></span>Are you sure you want to drop <b><%=theCourse.getTitle()%> (<%=studentBidTemp.getCode()%>)</b>?</p>
</div>
<%
        }
    }
%>

<!--Drop Enrolled Course-->
<%
    if (enrolledCourse != null && !enrolledCourse.isEmpty()) {
        for (int i = 0; i < enrolledCourse.size(); i++) {
            Bid studentBidTemp = enrolledCourse.get(i);
            Course theCourse = CourseDAO.retrieveByCode(studentBidTemp.getCode());
%>
<div id="enrolled<%=i%>" title="Drop Enrolled Course?" hidden="true" style="font-family: 'Roboto', sans-serif; font-weight: 300;">
    <p><span class="ui-icon ui-icon-alert" style="float: left; margin: 0 7px 20px 0;"></span>Are you sure you want to drop <b><%=theCourse.getTitle()%> (<%=studentBidTemp.getCode()%>)</b>?</p>
</div>
<%
        }
    }
%>


<!--Initiate label for updateBid-->
<label for="updateBid"></label>
<label for="updateBidVoid"></label>

<script>
            //Drop Bidded course
            function dropBid(var1, var2) {
                var targetUrl = var2
                $('#' + var1).dialog({
                    resizable: false,
                    height: 200,
                    width: 400,
                    modal: true,
                    autoOpen: true,
                    draggable: false,
                    buttons: {
                        "Drop Bid": function() {
                            window.location.href = targetUrl;
                            $(this).dialog("close");
                        },
                        Cancel: function() {
                            $(this).dialog("close");
                        }
                    }
                });
                $('#' + var1).dialog("open");
            }
            
            //Drop Enrolled Course
            function dropCourse(var1, var2) {
                var targetUrl = var2
                $('#' + var1).dialog({
                    resizable: false,
                    height: 200,
                    width: 400,
                    modal: true,
                    autoOpen: true,
                    draggable: false,
                    buttons: {
                        "Drop Enrolled Course": function() {
                            window.location.href = targetUrl;
                            $(this).dialog("close");
                        },
                        Cancel: function() {
                            $(this).dialog("close");
                        }
                    }
                });
                $('#' + var1).dialog("open");
            }
            //Tooltip
            $(function() {
                $(document).tooltip();
            });
</script>
<!-- Modal -->