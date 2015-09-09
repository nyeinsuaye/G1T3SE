/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.app.model;

import com.google.gson.annotations.SerializedName;
import java.math.BigDecimal;

/**
 *  THIS CLASS IS FOR JSON RESPONSE ONLY. DO NO INITIALIZE ANY SectionStudent objects in CRUD functions Use Bid
 * 
 */
public class SectionStudent {
    @SerializedName("userid") private String userId;
    @SerializedName("course")private String code;
    private String section;
    private BigDecimal amount;
    
    /**
     * FOR JSON SERIALIZATION PURPOSES.
     */
    private SectionStudent(){
    }
    
    /**
     * Create a bid object with 
     * @param userId Id of student
     * @param code id of course
     * @param section id of section
     * @param amount bid amount put for the section
     */
    public SectionStudent(Bid bid) {
        this.userId = bid.getUserId();
        this.amount = bid.getAmount();
        this.code = bid.getCode();
        this.section = bid.getSection();
        
    }
}
