/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.app.model;

/**
 *  DO NOT USE WITHIN APP. ONLY FOR JSON SERIALIZATION PURPOSES
 *  
 */
public class Prerequisite {
    private String course;
    private String prerequisite;
    
    /**
     * FOR JSON SERIALIZATION PURPOSES.
     */
    private Prerequisite() {
    }
    /**
     * Constructor for Prerequisite
     * @param course
     * @param prerequisite 
     */
    public Prerequisite(String course, String prerequisite) {
        this.course = course;
        this.prerequisite = prerequisite;
    }
    
}
