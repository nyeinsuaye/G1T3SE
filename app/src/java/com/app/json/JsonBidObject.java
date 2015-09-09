/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.app.json;

import com.app.model.Bid;
import java.math.BigDecimal;

/**
 *  A bid object that follows the JSON format required in DUMP (Bid)
 *  
 */
public class JsonBidObject implements Comparable<JsonBidObject>{
    private int row;
    private String userid;
    private BigDecimal amount;
    private String result;
    
    /**
     * FOR JSON SERIALIZATION PURPOSES.
     */
    private JsonBidObject(){
    }
    
    /**
     * Creates a JsonBidObject
     * @param row 
     * @param bid the bid object
     * @param result the result of the bid
     */
    public JsonBidObject(int row, Bid bid, String result) {
        this.row = row;
        this.userid = bid.getUserId();
        this.amount = bid.getAmount();
        this.result = result;
    }

    /**
     *
     * @return the row
     */
    public int getRow() {
        return row;
    }

    /**
     * returns the userId of the bidder
     * @return the userID
     */
    public String getUserid() {
        return userid;
    }

    /**
     * returns the bid amount
     * @return the bid amount
     */
    public BigDecimal getAmount() {
        return amount;
    }

    /**
     * returns the bid result
     * @return the result of this bid
     */
    public String getResult() {
        return result;
    }
    
    

    @Override
    public int compareTo(JsonBidObject obj) {
        if (this.amount.compareTo(obj.getAmount()) > 0) {
            return -1;
        } else if (this.amount.compareTo(obj.getAmount()) < 0) {
            return 1;
        } else {
            return this.userid.compareTo(obj.getUserid());
        }
    }
}
