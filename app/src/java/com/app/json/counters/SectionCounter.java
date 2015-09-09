/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.app.json.counters;

import com.google.gson.annotations.SerializedName;

public class SectionCounter implements Counter {
  
    @SerializedName ("section.csv") private int count;

    /**
     * For Gson's serialization/deserialization operations
     */
    private SectionCounter() {
    }
    
    /**
     * Initializing section counter and assign the count value with no of successful record
     * @param count
     */
    public SectionCounter(int count) {
        this.count = count;
    }
    
    /**
     * Get the no of successful record of section csv
     * @return no of record for section csv
     */
    public int getCount() {
        return count;
    }
}
