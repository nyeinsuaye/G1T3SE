<!--For Slider Lessontimetable <==> Examtimetable-->

<script>
    function selectDiv(n) {
        if (n === 1) {
            $(".slider-tab").animate({marginLeft: '2px'}, 200);
        } else {
            $(".slider-tab").animate({marginLeft: '132px'}, 200);
        }
        if (n !== 2) {
            $("#table2").hide();
            $("#table" + n).show();
        } else {
            $("#table1").hide();
            $("#table" + n).show();
        }
    }
</script>


<!--The selector for User Information & Course Completed-->
<div class="col-sm-6  col-md-6 col-lg-6">
    <div class="progress-container">
        <a href="#" class="tab" onclick="selectDiv(1);
        return false;">User Information</a>
        <a href="#" class="tab" onclick="selectDiv(2);
        return false;">Course Completed</a>
        <div class="slider-tab"></div>
    </div>
</div>

<!--In place of offsetting-->
<div class="col-md-5 col-lg-5"></div>
<span class="row"></span>
