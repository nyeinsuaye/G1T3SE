/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.app.model.two;

import java.math.BigDecimal;

/**
 * An object that stores the vacancy left and clearing/minimum price required to secure a position
 * in Round 2. 
 */
public class MinimumPrice {
    private String course;
    private String section;
    private int vacancyLeft;
    private BigDecimal price;

    /**
     * Constructor for minimum price
     * @param course
     * @param section
     * @param vacancyLeft
     * @param price 
     */
    public MinimumPrice(String course, String section, int vacancyLeft, BigDecimal price) {
        this.course = course;
        this.section = section;
        this.vacancyLeft = vacancyLeft;
        this.price = price;
    }
    /**
     * Get the course id
     * @return course id
     */
    public String getCourse() {
        return course;
    }
    /**
     * Set the course id
     * @param course 
     */
    public void setCourse(String course) {
        this.course = course;
    }

    /**
     * Get the section id
     * @return section id
     */
    public String getSection() {
        return section;
    }

    /**
     * Set the section id
     * @param section 
     */
    public void setSection(String section) {
        this.section = section;
    }
    
    /**
     * Get the vacancy left
     * @return no of vacancy
     */
    public int getVacancyLeft() {
        return vacancyLeft;
    }

    /**
     * Set vacancy of the course
     * @param vacancyLeft 
     */
    public void setVacancyLeft(int vacancyLeft) {
        this.vacancyLeft = vacancyLeft;
    }

    /**
     * Get the clearing price
     * @return clearing price
     */
    public BigDecimal getPrice() {
        return price;
    }
    
/**
 * Set the clearing price
 * @param price 
 */
    public void setPrice(BigDecimal price) {
        this.price = price;
    }
    
    
}
