/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.app.model;

import com.google.gson.annotations.SerializedName;

/**
 *  DO NOT USE WITHIN APP. ONLY FOR JSON SERIALIZATION PURPOSES
 *  
 */
public class CompletedCourse {
    @SerializedName("userid") private String userId;
    private String code;
    /**
     * FOR JSON SERIALIZATION PURPOSES.
     */
    private CompletedCourse(){
    }
    
    /**
     * Creates a CompletedCourse constructor with the following
     * @param userId the userId of the user
     * @param code the courseId of the course
     */
    public CompletedCourse(String userId, String code) {
        this.userId = userId;
        this.code = code;
    }
}
