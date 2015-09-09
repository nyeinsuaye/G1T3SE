<%@page import="com.app.model.SectionDAO"%>
<%@page import="com.app.model.Section"%>
<%@page import="java.util.Date"%>
<%@page import="java.util.Calendar"%>
<%@page import="com.app.model.StudentDAO"%>
<%@page import="com.app.model.Bid"%>
<%@page import="com.app.model.BidDAO"%>
<%@page import="com.app.model.Student"%>
<%@page import="java.util.ArrayList"%>
<!DOCTYPE html>
<html lang="en">
    <head>
        
        
                <!--Get the Student object to get student info: Username and Edollars -->
                
        
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <meta name="description" content="">
        <meta name="author" content="">

        <title>The New BIOS</title>

        <!-- Bootstrap core CSS -->
        <link href="theme/css/bootstrap.css" rel="stylesheet">

        <!-- Add custom CSS here -->
        <link href="theme/css/stylish-portfolio.css" rel="stylesheet">
        <link href="theme/font-awesome/css/font-awesome.min.css" rel="stylesheet">

        <!-- JQuery for Nav Bar -->
        <script src="http://code.jquery.com/jquery-1.9.1.js"></script>
        <script src="http://code.jquery.com/ui/1.10.3/jquery-ui.js"></script>

        <!-- Jquery for countdown timer -->
        <script src="http://code.jquery.com/jquery-1.7.1.min.js"></script>
        <script src="countdown/assets/countdown/jquery.countdown.js"></script>
        <script src="countdown/assets/js/script.js"></script>

        <!-- Our CSS stylesheet file -->
        <link rel="stylesheet" href="http://fonts.googleapis.com/css?family=Open+Sans+Condensed:300" />
        <link rel="stylesheet" href="countdown/assets/countdown/css/styles.css" />
        <link rel="stylesheet" href="countdown/assets/countdown/jquery.countdown.css" />


        <!-- Full calendar stylesheet file -->
        <link rel='stylesheet' type='text/css' href='fullcalendar/css/main.css' />
        <link rel='stylesheet' type='text/css' href='fullcalendar/fullcalendar.css' />
                <%
                    Student s =StudentDAO.retrieveById("ian.ng.2012");
                %>

        <!-- Full calendar Jquery file -->
        <script type='text/javascript' src='fullcalendar/fullcalendar.min.js'></script>
        <script type='text/javascript'>

            $(document).ready(function() {

                var date = new Date();
                var d = date.getDate();
                var m = date.getMonth();
                var y = date.getFullYear();
                
                var cls = function($date){
                    date = $.fullCalendar.parseDate( $date );
                    d = date.getDate();
                    m = date.getMonth();
                    y = date.getFullYear();
                };
                
                $('#calendar').fullCalendar({
                    aspectRatio: 2,
                    weekends: false,
                    minTime: 8,
                    defaultView: 'agendaWeek',
                    slotMinutes: 90,
                    header: {
                        left: '',
                        center: 'Timetable',
                        right: ''
                    },
                    editable: true,
                    events: [
                        
                        <%
                            for(Bid b: BidDAO.retrieveByUserId("ben.ng.2009")){
                                Section section = SectionDAO.retrieveSection(b.getCode(), b.getSection());
                                
                                // get today and clear time of day
                                Calendar cal = Calendar.getInstance();
                                cal.set(Calendar.HOUR_OF_DAY, 0); // ! clear would not reset the hour of day !
                                cal.clear(Calendar.MINUTE);
                                cal.clear(Calendar.SECOND);
                                cal.clear(Calendar.MILLISECOND);

                                // get start of this week in milliseconds
                                cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
                                System.out.println("Start of this week:       " + cal.getTime());
                                Date dt = cal.getTime();
                                for(int x=0;x<7;x++){
                                if(dt.getDay() != section.getDay()){
                                    cal.add(Calendar.DATE, 1);  // number of days to add
                                    dt = cal.getTime();
                                }else{
                                    cal.set(Calendar.HOUR_OF_DAY,section.getStart().getHours());
                                    cal.set(Calendar.MINUTE,section.getStart().getMinutes());
                                    cal.set(Calendar.SECOND,0);
                                    cal.set(Calendar.MILLISECOND,0);
                                    dt = cal.getTime();
                                    cal.set(Calendar.HOUR_OF_DAY,section.getEnd().getHours());
                                    cal.set(Calendar.MINUTE,section.getEnd().getMinutes());
                                    cal.set(Calendar.SECOND,0);
                                    cal.set(Calendar.MILLISECOND,0);
                                    Date dtEnd = cal.getTime();
                                    
                        %>
                        {
                            title  : "<%= b.getCode()%> - <%= b.getSection() %>",
                            start: new Date(y, <%= dt.getMonth() %>, <%= dt.getDate() %>, <%= dt.getHours() %>, <%= dt.getMinutes() %>),
                            end :   new Date(y, <%= dt.getMonth() %>, <%= dt.getDate() %>, <%= dtEnd.getHours() %>, <%= dtEnd.getMinutes() %>),
                            allDay: false
                        },
                        <%
                                    
                                    cal.add(Calendar.DATE, 1);  // number of days to add
                                    dt = cal.getTime();
                            }
                            }
                            }
                        %>
                        {
                            title: 'Meeting',
                            start: new Date(y, m, d, 10, 30),
                            allDay: false
                        }
                    ]
                });

            });

            $(function() {
                $("#dialog-confirm").dialog({
                    resizable: false,
                    height: 140,
                    modal: true,
                    buttons: {
                        "Delete all items": function() {
                            $(this).dialog("close");
                        },
                        Cancel: function() {
                            $(this).dialog("close");
                        }
                    }
                });
            });
        </script>


        <style>
            html, body {
                height: 100%;
            }
            footer {
                color: #666;
                background: #222;
                padding: 10px 0 18px 0;
                border-top: 1px solid #000;
            }
            footer a {
                color: #999;
            }
            footer a:hover {
                color: #efefef;
            }
            .wrapper {
                min-height: 100%;
                height: auto !important;
                height: 100%;
                margin: 0 auto -63px;
            }
            .push {
                height: 63px;
            }
            /* not required for sticky footer; just pushes hero down a bit */
            .wrapper > .container {
                padding-top: 40px;
            }

            hr { display: block; height: 1px;
                 border: 0; border-top: 1px solid #ccc;
                 margin: 1em 0; padding: 0; 
            }

            .sidebar-nav-fixed {
                position:fixed;
                top:100;
                left:0;
            }
        </style>
    </head>

    <body>
        <nav class="navbar navbar-inverse navbar-fixed-top" role="navigation">
            <div class="container">
                <div class="navbar-header">
                    <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-ex1-collapse">
                        <span class="sr-only">Toggle navigation</span>
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                    </button>
                    <a class="navbar-brand" href="#">BIOS, by G1T3</a>
                </div>


                <!-- Collect the nav links, forms, and other content for toggling -->
                

                <!-- Side Menu -->
                <a id="menu-toggle" href="#" class="btn btn-primary btn-default btn-lg toggle"><i class="icon-reorder"></i></a>
                <div id="sidebar-wrapper">
                    <ul class="sidebar-nav">
                        <a id="menu-close" href="#" class="btn btn-default btn-lg pull-right toggle"><i class="icon-remove"></i></a>
                        <li class="sidebar-brand"><a href="#">BIOS</a></li>
                        <li><a class="dance" href="#">About Us</a></li>
                        <li><a class="dance" href="https://oasis.smu.edu.sg/">OASIS</a></li>
                        <li><a class="dance" href="https://elearn.smu.edu.sg/?logout=1">ELearn</a></li>
                        <li><a class="dance" href="login.jsp">Logout</a></li>
                    </ul>
                </div>
                <!-- /Side Menu -->
            </div><!-- /.container -->
        </nav>

        <!-- Countdown timer -->
        <div id="top" class="clock">
            <br><br><br><br><br><br>
            <div class="theClock"></div>

            <div id="TimeHeader">
                <h1 id="login1" align="center"><b>Bidding ends in:</b></h1><br>
                <div id="countdown"></div>
                <p align="center" id="note"></p>
            </div>
        </div>

        <!--Nav Tab for 1.Summary & 2.Search/Bid-->
        <div class="col-xs-12 col-md-12 col-lg-12 pull-left">
            <ul class="nav nav-tabs">
                <li class="active">
                    <a href="#">Summary</a>
                </li>
                <li>
                    <a href="search_and_bid.jsp">Search & Bid</a>
                </li>
            </ul>
        </div>

        <!--Get existing Bids of Student-->
        <%
            ArrayList<Bid> bidsOfStudent = BidDAO.retrieveByUserId("ian.ng.2012");
        %>

        <!--Bid Summary-->
        <div class="col-xs-12 col-md-5 col-lg-5 pull-left">
            <h2>Bid Summary</h2>
            <hr><br>
            <table class="table table-striped">
                <tr>
                    <th>Course Name</th>
                    <th>Course Code</th>
                    <th>Bid Amount</th>
                    <th>Edit Bid</th>
                    <th>Delete Bid</th>
                </tr>
                <%
                    for (Bid b : bidsOfStudent) {
                %>
                <tr>
                    <td>Coursename</td>
                    <td><%=b.getCode()%></td>
                    <td><%=b.getAmount()%></td>
                    <td><a href="xxx">Edit Bid</a></td>
                    <td><a href="xxx">Delete Bid</a></td>
                </tr>
                <%}
                %>
            </table>
        </div>

        <!--Time Table-->
        <div class="col-xs-12 col-md-6 col-md-offset-1 col-lg-6 col-lg-offset-1 pull-right">
            <h2>Time-Table</h2>
            <hr>
            <!--
            <br>
            <table class="table table-bordered">
                <tr>
                    <th>Time/Day</th>
                    <th>Monday</th>
                    <th>Tuesday</th>
                    <th>Wednesday</th>
                    <th>Thursday</th>
                    <th>Friday</th>
                </tr>
                <tr>
                    <td><b>0830-1145</b></td>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td></td>
                </tr>
                <tr>
                    <td><b>1200-1515</b></td>
                    <td></td><td></td><td></td><td></td><td></td>
                </tr>
                <tr>
                    <td><b>1530-1845</b></td>
                    <td></td><td></td><td></td><td></td><td></td>
                </tr>
            </table>
            -->
            <div id='calendar'></div>
        </div>

        <span class="row"></span><span class="row"></span>
        <div class="col-xs-12 col-md-5 col-lg-5 pull-left">

        </div>

        <span class="row"></span>
        <span class="row"></span>
        <span class="row"></span>
        <span class="row"></span>
        <span class="row"></span>



        <div id="top" class="footerTransform">
            <!-- CONTACT -->
            <section class="contact" id="contact">
                <div class="container">
                    <div class="row-fluid">
                        <div class="span12">	
                            <h1 align="center">Get in touch with us:</br>
                                <br/><a href="https://www.facebook.com/groups/435719786545123/" target="_blank">facebook.com/Blab-bers</a>
                            </h1>
                            <br>

                            <em><h3 style="font-family:arial;color:#B6B6B4" align="center">Proudly rendered by, Blab-bers</h3></em>
                        </div>
                    </div>
                </div>
            </section>
        </div>
        <span class="row"></span>
        <span class="row"></span>
        <span class="row"></span>
        <!-- Footer -->

        <div class="container">
            <div class="row">
                <div class="col-md-6 col-md-offset-3 text-center">
                    <ul class="list-inline">
                        <li><i class="icon-facebook icon-2x"></i></li>
                        <li><i class="icon-twitter icon-2x"></i></li>
                    </ul>
                    <hr>
                    <p>Copyright &copy; Blab-bers (G1-T3) 2013</p>
                </div>
            </div>
        </div>

        <!-- /Footer -->


        <!-- Custom JavaScript for the Side Menu - Put in a custom JS file if you want to clean this up -->
        <script>
            $("#menu-close").click(function(e) {
                e.preventDefault();
                $("#sidebar-wrapper").toggleClass("active");
            });
        </script>
        <script>
            $("#menu-toggle").mouseover(function(e) {
                e.preventDefault();
                $("#sidebar-wrapper").toggleClass("active");
            });
        </script>
        <script>
            $(function() {
                $('a[href*=#]:not([href=#])').click(function() {
                    if (location.pathname.replace(/^\//, '') == this.pathname.replace(/^\//, '')
                            || location.hostname == this.hostname) {

                        var target = $(this.hash);
                        target = target.length ? target : $('[name=' + this.hash.slice(1) + ']');
                        if (target.length) {
                            $('html,body').animate({
                                scrollTop: target.offset().top
                            }, 1000);
                            return false;
                        }
                    }
                });
            });
        </script>
        <!--Script for countdown timer-->

    </body>
</html>
