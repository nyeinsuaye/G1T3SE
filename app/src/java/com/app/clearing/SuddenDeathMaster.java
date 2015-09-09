/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.app.clearing;

import com.app.model.Bid;
import com.app.model.Section;
import com.app.model.SectionDAO;
import com.app.model.SectionStudentDAO;
import com.app.model.two.MinimumPrice;
import com.app.model.two.MinimumPriceDAO;
import com.app.model.two.SuddenDeathDAO;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.ListIterator;

/**
 *
 * @author Zachery
 */
public class SuddenDeathMaster {
    
    /**
     * PREPARE APPLICATION / DATABASE FOR ROUND 2 BIDDING
     */
    
    public static void initializeEnvironment() {
        //Retrieve ALL sections
        ArrayList<Section> sections = SectionDAO.retrieveAll();
        
        //Determine vacancies for each section and initialize minimum_price table in DB
        String course = null;
        String section = null;
        int vacancyLeft = 0;
        BigDecimal price = BigDecimal.TEN;     //default price of $10 to be initialized
        
        ArrayList<MinimumPrice> minimumPriceList = new ArrayList<>();
        for (Section sectionObj : sections) {
            course = sectionObj.getCourse();
            section = sectionObj.getSection();
            vacancyLeft = getBiddableVacanciesForSection(course,section);
            
            //populate data into minimum_price table.. INCLUDING THOSE THAT HAVE VACANCY 0
            minimumPriceList.add(new MinimumPrice(course,section,vacancyLeft,price));
        }
        
        MinimumPriceDAO.addAll(minimumPriceList);
        
    }
    
    /**
     * EVERY TIME, an enrolled course is DROPPED during ACTIVE round 2, we need to open it up for bidding.
     * by increasing its vacancy number in minimum_price table
     * @param courseStr the particular course
     * @param sectionStr the particular section
     */
    public static void increaseVacancy(String course, String section) {
        int vacancyLeft = getBiddableVacanciesForSection(course,section);
        
        MinimumPriceDAO.updateVacancy(course, section, vacancyLeft);
    }
    
    /*
     * ROUND 2 METHODS 
     */
    
    /*
     * Students will be provided with real-time bid information.
     * After every bid, the system sort the bids from the highest to the lowest and displays the following information:
     * Total Available Seats: Number of seats available for this section at the start of the round, after accounting for the number of seats successfully taken up during the first round.
     * Minimum bid value: This is the current minimum bid value that qualifies for a seat in the section. This value starts at 10 and never goes down.
     * The clearing price is the lowest successful bid that will get you a seat for this round for this section.
     * After each bid, do the following processing to re-compute the minimum bid value:
     * Case 1: If there are less than N bids for the section (where N is the total available seats), the Current Vacancies are (N - number of bids). The minimum bid value remains the same as there are still unfilled vacancies in this section that students can bid for using the minimum bid value.
     * Case 2: If there are N or more bids for the section, the minimum bid value is 1 dollar more than the Nth bid. If there are other bids with the same bid price as the Nth bid and the class is unable to accommodate all of them, all bids with the same price will be unsuccessful.
     * Your system should reflect the real-time status of the bids. I.e., if your bid is no longer successful (because other people have outbid you), this has to be reflected along with the new minimum bid price. The student's e$ balance will also be replenished with the e$ from the unsuccessful bid. This allows students to dynamically re-bid for the class using a higher e-dollar amount.
     * At the end of the round, all successful bids will be confirmed.
     */
    
    
    
    /**
     * Returns the number of remaining vacancies of the section.
     * @param courseStr the particular course
     * @param sectionStr the particular section
     * @return the number of remaining vacancies available
     */
    private static int getBiddableVacanciesForSection(String courseStr, String sectionStr) {
        Section section = SectionDAO.retrieveSection(courseStr, sectionStr);
        
        int totalSize = section.getSize();
        
        ArrayList<Bid> successfulBidList = SectionStudentDAO.retrieveBySectionId(courseStr, sectionStr);
        
        int occupied = successfulBidList.size();
        
        return totalSize - occupied;
    }
    
    /**
     * Returns the number of existing bids that are currently contending for the remaining vacancies of the section.
     * @param courseStr the particular course
     * @param sectionStr the particular section
     * @return the number of existing bids currently contending for the remaining vacancies
     */
    public static int getNumberOfSuddenDeathBidsForSection(String courseStr, String sectionStr) {
        ArrayList<Bid> suddenDeathBids = SuddenDeathDAO.retrieveBySectionId(courseStr, sectionStr);
        
        return suddenDeathBids.size();
        
    }
    
    /**
     * Returns the minimum price needed to be bidded by the student to ensure that he gets a spot within the remaining biddable vacancies.
     * In the front end, this should be updated, upon a change in the DB state by another user.
     * Prompt user press the "refresh" function for the rankings to be updated before submitting their bids.
     * @param courseStr the particular course
     * @param sectionStr the particular section
     * @return a BidDecimal of the clearing price
     */
    public static BigDecimal getClearingPriceNeededToEnrollForSection(String courseStr, String sectionStr) {
        MinimumPrice minimumPrice = MinimumPriceDAO.retrieve(courseStr, sectionStr);
        
        //if the vacancy is full, return null.
        if (minimumPrice.getVacancyLeft() == 0) {
            return null;
        }
        return minimumPrice.getPrice();
    }
    
    /**
     * Add the bid to the sudden_death table, when it meets the clearing price (Transaction-based).
     * @param userId userId of the user
     * @param amount the amount bid
     * @param courseStr the course
     * @param sectionStr the section
     * @return a boolean whether the bid has been added
     */
    public static boolean addBid(String userId, BigDecimal amount, String courseStr, String sectionStr) {
        return SuddenDeathDAO.addBid(userId,amount,courseStr,sectionStr);
    }
    
}
