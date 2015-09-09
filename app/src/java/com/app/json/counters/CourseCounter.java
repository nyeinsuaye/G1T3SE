/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.app.json.counters;

import com.google.gson.annotations.SerializedName;
 
public class CourseCounter implements Counter {
   
    @SerializedName ("course.csv") private int count;

    /**
     * For Gson's serialization/deserialization operations
     */
    private CourseCounter() {
    }
    
    /**
     * Initializing course counter and assign the count value with no of successful record
     * @param count
     */
    public CourseCounter(int count) {
        this.count = count;
    }    
    
    /**
     * Get the no of successful record of course csv
     * @return no of record for course csv
     */
    public int getCount() {
        return count;
    }
}
