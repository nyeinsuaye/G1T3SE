<!--For Slider Lessontimetable <==> Examtimetable-->

<script>
    function selectDiv(n) {
        if (n === 1) {
            $(".slider-tab-prerequisite").animate({marginLeft: '2px'}, 200);
        } else {
            $(".slider-tab-prerequisite").animate({marginLeft: '132px'}, 200);
        }
        if (n !== 2) {
            $("#SearchResult2").hide();
            $("#SearchResult" + n).show();
        } else {
            $("#SearchResult1").hide();
            $("#SearchResult" + n).show();
        }
    }
</script>


<!--The selector for User Information & Course Completed-->
<div class="col-sm-6  col-md-6 col-lg-6">
    <div class="progress-container-prerequisite">
        <a href="#" class="tab" onclick="selectDiv(1);
        return false;">Course Information</a>
        <a href="#" class="tab" onclick="selectDiv(2);
        return false;">Prerequisites</a>
        <div class="slider-tab-prerequisite"></div>
    </div>
</div>

<!--In place of offsetting-->
<div class="col-md-5 col-lg-5"></div>
<span class="row"></span>
