/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.app.json.counters;

import com.google.gson.annotations.SerializedName;

public class PrerequisiteCounter implements Counter {
    
    
    @SerializedName ("prerequisite.csv") private int count;

    /**
     * For Gson's serialization/deserialization operations
     */
    private PrerequisiteCounter() {
    }
    
    /**
     * Initializing prerequisite counter and assign the count value with no of successful record
     * @param count
     */
    public PrerequisiteCounter(int count) {
        this.count = count;
    }    
    
    /**
     * Get the no of successful record of prerequisite csv
     * @return no of record for prerequisite csv
     */
    public int getCount() {
        return count;
    }
}
