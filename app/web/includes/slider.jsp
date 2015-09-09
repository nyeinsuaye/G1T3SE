<!--For Slider Lessontimetable <==> Examtimetable-->

<script>
    function selectDiv(n) {
        if (n === 1) {
            $(".slider-tab").animate({marginLeft: '2px'}, 200);
        } else if(n === 2) {
            $(".slider-tab").animate({marginLeft: '132px'}, 200);
        } else {
            $(".slider-tab").animate({marginLeft: '242px'}, 200);
        }
        if (n === 1) {
            $("#timetable2").hide();
            $("#timetable" + n).show();
            $("#timetable3").hide();
        } else if (n === 2){
            $("#timetable1").hide();
            $("#timetable" + n).show();
            $("#timetable3").hide();
        } else {
            $("#timetable1").hide();
            $("#timetable" + n).show();
            $("#timetable2").hide();
        }
    }
</script>


<!--The selector for Lessontimetable & Examtimetable-->
<div class="col-sm-7  col-md-7 col-lg-7">
    <div class="progress-container">
        <a href="#" class="tab" onclick="selectDiv(1);
        return false;">Lesson Timetable</a>
        <a href="#" class="tab" onclick="selectDiv(2);
        return false;">Exam Timetable</a>
    <a href="#" class="tab" onclick="selectDiv(3);
        return false;">Bid status</a>
        <div class="slider-tab"></div>
    </div>
</div>


