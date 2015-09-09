package com.app.model;


import com.google.gson.annotations.SerializedName;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Section {
    private String course;
    private String section;
    private transient int day;     //transient so that it IS NOT "SERIALIZED" by JSON
    @SerializedName("day") private String dayStr; //JSON OUTPUT NEEDS TO BE E.g. "Monday", "Tuesday" instead of int.
    private transient Date start;
    @SerializedName("start") private String startStr;
    private transient Date end;
    @SerializedName("end") private String endStr;
    private String instructor;
    private String venue;
    private int size;

    /**
     * FOR JSON SERIALIZATION PURPOSES.
     */
    private Section(){
    }
    
    /**
     * Specific constructor to allow day to be represented as a String E.g. "Monday" instead of 1.
     * Mainly used by the Dump Tables.
     * @param dumpFormat
     *      - set to true if you want dumpFormat to be displayed.
     * @param course
     * @param section
     * @param day
     * @param start
     * @param end
     * @param instructor
     * @param venue
     * @param size 
     */
    public Section (boolean dumpFormat, String course, String section, int day, Date start, Date end, String instructor, String venue, int size ) {
        //process only if true
        this.course = course;
        this.section = section;
        this.day = day;
        this.start = start;
        this.end = end;
        this.instructor = instructor;
        this.venue = venue;
        this.size = size;
        
        if (dumpFormat) {
            switch (day) {
                case 1:
                    this.dayStr = "Monday";
                    break;
                case 2:
                    this.dayStr = "Tuesday";
                    break;
                case 3:
                    this.dayStr = "Wednesday";
                    break;
                case 4:
                    this.dayStr = "Thursday";
                    break;
                case 5:
                    this.dayStr = "Friday";
                    break;
                case 6:
                    this.dayStr = "Saturday";
                    break;
                case 7:
                    this.dayStr = "Sunday";
                    break;
                default:
                    this.dayStr = "Wrong day";
                    break;
            }
            
            SimpleDateFormat sdf = new SimpleDateFormat("Hmm");
            this.startStr = sdf.format(start);
            this.endStr = sdf.format(end);
        }
    }
    
    /**
     * Constructor for Section
     * @param course Id of course
     * @param section Id of section
     * @param day Name of the day
     * @param start Time the section start 
     * @param end Time the section end
     * @param instructor Name of the instructor
     * @param venue Venue of the class
     * @param size Size of the class
     */
    public Section(String courseId, String section, int day, Date start, Date end, String instructor, String venue, int size) {
        this.course = courseId;
        this.section = section;
        this.day = day;
        this.start = start;
        this.end = end;
        this.instructor = instructor;
        this.venue = venue;
        this.size = size;
    }

    /**
     * Get the course
     * @return the course
     */
    public String getCourse() {
        return course;
    }

    /**
     * Set the course of the selected section
     * @param course id of the Course
     */
    public void setCourse(String course) {
        this.course = course;
    }

    /**
     * Get the course section
     * @return section of the class
     */
    public String getSection() {
        return section;
    }

    /**
     * Set the section of the section
     * @param section 
     */
    public void setSection(String section) {
        this.section = section;
    }

    /**
     * Get the day the section is conducted
     * @return day of the conducted section
     */
    public int getDay() {
        return day;
    }

    /**
     * Set the day of the section
     * @param day
     */
    public void setDay(int day) {
        this.day = day;
    }
    
    /**
     * 
     * @return the Day as a String. E.g. Day 1 as "Monday"
     */
    public String getDayStr() {
        return dayStr;
    }

    public void setDayStr(String dayStr) {
        this.dayStr = dayStr;
    }

    /**
     * Get the time the section started 
     * @return the time the section started 
     */
    public Date getStart() {
        return start;
    }

    /**
     * Set the start time of the section
     * @param start new start time of the course
     */
    public void setStart(Date start) {
        this.start = start;
    }

    /**
     * Get the end time of the section
     * @return end time
     */
    public Date getEnd() {
        return end;
    }

    /**
     * Set the end time of the section 
     * @param end new end time
     */
    public void setEnd(Date end) {
        this.end = end;
    }

    /**
     * Get the instructor teaching the section
     * @return instructor name
     */
    public String getInstructor() {
        return instructor;
    }

    /**
     * Set the name of the instructor for the section
     * @param instructor name of instructor
     */
    public void setInstructor(String instructor) {
        this.instructor = instructor;
    }

    /**
     * Get the section venue for the section
     * @return venue
     */
    public String getVenue() {
        return venue;
    }

    /**
     * Set the new venue for the section
     * @param venue new venue
     */
    public void setVenue(String venue) {
        this.venue = venue;
    }

    /**
     * Get the size of the section
     * @return size of the class
     */
    public int getSize() {
        return size;
    }

    /**
     * Set the size for the section
     * @param size size of the section
     */
    public void setSize(int size) {
        this.size = size;
    }
    
    @Override
    public boolean equals(Object obj) {
        
        if (!(obj instanceof Section)) {
            return false;
        }
        
        Section otherSection = (Section)obj;
        
        return course.equals(otherSection.getCourse()) && section.equals(otherSection.getSection())
                && day==otherSection.getDay() && start.equals(otherSection.getStart())
                && end.equals(otherSection.getEnd()) && instructor.equals(otherSection.getInstructor())
                && venue.equals(otherSection.getVenue()) && size==otherSection.getSize();
    }
}
