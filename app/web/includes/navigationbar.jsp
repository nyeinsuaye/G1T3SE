<%@page import="com.app.model.StudentDAO"%>
<%@page import="com.app.model.Student"%>

<%
    String userid = (String) session.getAttribute("userid");
    Student theStudent = StudentDAO.retrieveById(userid);
%>

<nav class="navbar navbar-default navbar-fixed-top" role="navigation" id='theNavBar'>
    <div class="navbar-header">
        <a href="timetable.jsp"><img src="/img/logo_white.png"></a>
    </div>
    <div class="navbar-collapse collapse">
        <ul class="nav navbar-nav">
            <li id="nav_timetable"><a href="timetable.jsp"><strong>Timetable</strong></a></li>
            <li id="nav_bid"><a href="bid.jsp"><strong>Bid</strong></a></li>
        </ul>
        <ul class="nav navbar-nav navbar-right">
            <li><a><span class="glyphicon glyphicon-user"></span><font style="color: #4385f5"><%=theStudent.getName()%></font> </a></li>
            <li><a><span class="glyphicon glyphicon-usd"></span><font style="color: #43A621"><%=theStudent.getEDollars()%></font></a></li>
            <li id="nav_userInfo"><a href="user_info.jsp" id="userInfo"><strong>Profile</strong></a></li>
            <li id="logout"><a href="logout.jsp"><strong>Logout</strong></a></li>
        </ul>
    </div><!--/.nav-collapse -->
</nav>
<span class="row"></span>