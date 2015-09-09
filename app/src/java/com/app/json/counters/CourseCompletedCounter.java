/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.app.json.counters;

import com.google.gson.annotations.SerializedName;

/**
 *  Acts as a counter for the course completed
 *  
 */
public class CourseCompletedCounter implements Counter {
    
    
    @SerializedName ("course_completed.csv") private int count;

    /**
     * For Gson's serialization/deserialization operations
     */
    private CourseCompletedCounter() {
    }
    
    /**
     * Initializing course completed counter and assign the count value with no of successful record
     * @param count
     */
    public CourseCompletedCounter(int count) {
        this.count = count;
    }    
    
    /**
     * Get the no of successful record of course completed csv
     * @return no of record for course completed csv
     */
    public int getCount() {
        return count;
    }
}
