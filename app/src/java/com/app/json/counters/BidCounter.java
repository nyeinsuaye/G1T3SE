/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.app.json.counters;

import com.google.gson.annotations.SerializedName;

public class BidCounter implements Counter {
    
    @SerializedName ("bid.csv") private int count;

    /**
     * For Gson's serialization/deserialization operations
     */
    private BidCounter() {
    }
    
    /**
     * Initializing bid counter and assign the count value with no of successful record
     * @param count
     */
    public BidCounter(int count) {
        this.count = count;
    }
    
    /**
     * Get the no of successful record of bid csv
     * @return no of record for bid csv
     */
    public int getCount() {
        return count;
    }
}
