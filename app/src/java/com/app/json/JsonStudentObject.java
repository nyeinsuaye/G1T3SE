/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.app.json;

import com.app.model.Bid;
import java.math.BigDecimal;

/**
 * A bid object that follows the JSON format required in DUMP (Section)
 *  
 */
public class JsonStudentObject {
    private BigDecimal amount;
    private String userid;  
    
    /**
     * FOR JSON SERIALIZATION PURPOSES.
     */
    private JsonStudentObject(){
    }
    
    /**
     * format the bid object that follows the JSON format required in DUMP
     * @param bid the bid object
     */
    public JsonStudentObject(Bid bid) {
        this.amount = bid.getAmount(); 
        this.userid = bid.getUserId();                 
    }
}
