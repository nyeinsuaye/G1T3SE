/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.app.json.counters;

import com.google.gson.annotations.SerializedName;

public class StudentCounter implements Counter {
   
    @SerializedName ("student.csv") private int count;

    /**
     * For Gson's serialization/deserialization operations
     */
    private StudentCounter() {
    }
    
    /**
     * Initializing student counter and assign the count value with no of successful record
     * @param count
     */
    public StudentCounter(int count) {
        this.count = count;
    }
    
    /**
     * Get the no of successful record of student csv
     * @return no of record for student csv
     */
    public int getCount() {
        return count;
    }
}
