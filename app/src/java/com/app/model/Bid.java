package com.app.model;

import com.google.gson.annotations.SerializedName;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * Bid object class
 *  
 */
public class Bid implements Comparable<Bid> {
    @SerializedName("userid") private String userId;
    private BigDecimal amount;
    private String code;
    private String section;
    
    private transient boolean latest;
    private transient boolean successful;
    /**
     * FOR JSON SERIALIZATION PURPOSES.
     */
    private Bid(){
    }

    /**
     * Creates a bid object with 
     * @param userId Id of student
     * @param sectionId sectionId of the class
     * @param courseId courseId od the course
     * @param amount bid amount put for the section
     */
    public Bid(String userId, BigDecimal amount, String courseId, String sectionId) {
        this.userId = userId;
        this.amount = amount;
        this.code = courseId;
        this.section = sectionId;
        
    }

    /**
     *  Creates a bid object with
     * @param userId Id of student
     * @param amount the bid amount
     * @param code the courseId of the course
     * @param section the sectionId of the class
     * @param latest
     * @param successful
     */
    public Bid(String userId, BigDecimal amount, String code, String section, boolean isLatest, boolean isSuccessful) {
        this.userId = userId;
        this.amount = amount;
        this.code = code;
        this.section = section;
        this.latest = isLatest;
        this.successful = isSuccessful;
    }

    /**
     *  Creates a bid object with for use with round 2
     * @param userId Id of student
     * @param amount the bid amount
     * @param code the courseId of the course
     * @param section the sectionId of the class
     * @param successful
     */
    public Bid(String userId, BigDecimal amount, String code, String section, boolean isSuccessful) {
        this.userId = userId;
        this.amount = amount;
        this.code = code;
        this.section = section;
        this.successful = isSuccessful;
    }
    
    

    /**
     * Get the userId from the bid object
     * @return userId of the student
     */
    public String getUserId() {
        return userId;
    }

    /**
     * set the userId of the student of the bid object
     * @param userId id of the student
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * Get the id of the course from the bid object
     * @return code of the course
     */
    public String getCode() {
        return code;
    }

    /**
     * Set the id of the course of the bid object
     * @param code  id of the course
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * Get the id of the section from the bid object
     * @return section id
     */
    public String getSection() {
        return section;
    }

    /**
     * Set the id of the section in the bid object
     * @param sectionId 
     */
    public void setSection(String sectionId) {
        this.section = sectionId;
    }

    /**
     * Get the amount of bid from the bid object
     * @return bid amount
     */
    public BigDecimal getAmount() {
        return amount;
    }

    /**
     * Set the amount of bid from the bid object
     * @param amount 
     */
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public boolean isSuccessful() {
        return successful;
    }
    
    
    
    /**
     * Compares this Bid object with the specified Bid object based on the amount of each Bid.
     * @param bid
     * @return 
     *   -1, 0, or 1 as this Bid object's amount is numerically less than, equal to, or greater than the specified bid amount.
     */
    @Override
    public int compareTo(Bid bid) {
        //return amount.compareTo(bid.getAmount());
        
        //THIS METHOD FOR USE ONLY IN bid-dump
        if (this.amount.compareTo(bid.getAmount()) > 0) {
            return -1;  //if bigger, placed before, therefore negative
        } else if (this.amount.compareTo(bid.getAmount()) < 0) {
            return 1;
        } else {
            return this.userId.compareTo(bid.getUserId());
        }
    }

    @Override
    public int hashCode() {
        int hash = 7;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Bid other = (Bid) obj;
        if (!Objects.equals(this.userId, other.userId)) {
            return false;
        }
        if (!Objects.equals(this.amount, other.amount)) {
            return false;
        }
        if (!Objects.equals(this.code, other.code)) {
            return false;
        }
        if (!Objects.equals(this.section, other.section)) {
            return false;
        }
        return true;
    }
    
    
    
}
