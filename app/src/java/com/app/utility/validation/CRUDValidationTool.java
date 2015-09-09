/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.app.utility.validation;

import com.app.clearing.SuddenDeathMaster;
import com.app.model.Bid;
import com.app.model.BidDAO;
import com.app.model.Course;
import com.app.model.CourseDAO;
import com.app.model.Round;
import com.app.model.Section;
import com.app.model.SectionDAO;
import com.app.model.SectionStudentDAO;
import com.app.model.Student;
import com.app.model.StudentDAO;
import com.app.model.two.MinimumPrice;
import com.app.model.two.MinimumPriceDAO;
import com.app.model.two.SuddenDeathDAO;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.ListIterator;

/**
 * This class stores validation methods for validations done on SPECIFIC inputs of an incoming CRUD request.
 * Unless special bypass is required, a CRUDValidator object should be instantiated to validate the incoming request. 
 * The CRUDValidator will call methods in this class to validate each request.
 * @author Zachery
 */
public class CRUDValidationTool {
    /**
     * ArrayList<String> for storing error messages for JSON output.
     */
    private ArrayList<String> messageList;  //for storing error messages for JSON output
    
    /**
     * ArrayList<String> for storing error messages for printing on jsp files.
     */
    private ArrayList<String> userMessageList;  //for storing error messages for user interface (in addition to messageList)
   
    /**
     * Creates a new CRUDValidationTool
     * @param messageList the messageList object to store JSON error messages
     * @param userMessageList the userMessageList object to store error messages to be displayed for client-side use
     */
    public CRUDValidationTool(ArrayList<String> messageList, ArrayList<String> userMessageList) {
        this.messageList = messageList;
        this.userMessageList = userMessageList;
    }
    
    /**
     * Default constructor to create a new CRUDValidationTool (for client-side use)
     */
    public CRUDValidationTool() {
        
    }
    
    /**
     * Test the format of the amount input. Input must be greater than 10 and contain at most 2 decimal places.
     * @param amountStr the bid amount as String value
     * @return  - true, if amount has no formatting errors
     */
    public boolean isAmountValid (String amountStr) {
        boolean isAmountValid = true;
        BigDecimal amount = null;
        
        //Check for parsable by big decimal
        try {
            amount = new BigDecimal(amountStr);
        } catch (NumberFormatException e) {
            isAmountValid = false;
        }
        
        if (amount != null) {
            //Check for minimum bid
            if (amount.compareTo(new BigDecimal("10.00")) < 0) {
                isAmountValid = false;
            }

            //Check for 2 decimal places
            if (amount.scale() > 2) {         
                isAmountValid = false;
            }
        }
        
        if (!isAmountValid) {
             if (messageList != null) {
                messageList.add("invalid amount");
            }
        }
        
        return isAmountValid;
    }
    
    /**
     * Returns true if user of the specified userId has not completed the course of the specified course code.
     * @param userId - userId of the user
     * @param course -  course code of the course
     * @return - true if the user has NOT COMPLETED the course
     */
    public boolean hasNotCompletedCourse(String userId, String course) {
        ArrayList<String> courseCompletedList = StudentDAO.retrieveCourseCompleted(userId); 
        

        if (courseCompletedList.contains(course)) {
            if (messageList != null) {
                messageList.add("course completed");
            }
            return false;
        }
               
        return true;
    }
    
    /**
     * Returns true if user has completed the prerequisites required for the specified course
     * @param userId the userId of the user
     * @param course the course whose prerequisites are being checked againsts
     * @return - true if user has completed the prerequisites required for the specified course
     */
    public boolean hasCompletedPrerequisites(String userId, String course) {
        //check if student has completed prerequisites
        ArrayList<String> prerequisiteList = CourseDAO.retrievePrerequisites(course);
        ArrayList<String> courseCompletedList = StudentDAO.retrieveCourseCompleted(userId);
        
        if(!prerequisiteList.isEmpty()){
            boolean listModified = courseCompletedList.retainAll(prerequisiteList);
            
            //when student has not completed the required prerequisites
            if(courseCompletedList.size() < prerequisiteList.size()){
               
                if (messageList != null) {
                    messageList.add("incomplete prerequisites");
                }
                return false;
            }
        }
        
        return true;
    }
    
    /**
     * Returns true if user has enough eDollars to place the bid with the specified amount
     * @param userId the user
     * @param amount the amount placed for the bid
     * @param course the course that is bidded for
     * @param section the section of the specified course that is bidded for
     * @return true if user has enough eDollars to place the bid with the specified amount
     */
    public boolean hasEnoughEDollars(String userId, BigDecimal amount, String course, String section) {
        Student student = StudentDAO.retrieveById(userId);
        BigDecimal balance = student.getEDollars();
        
        //account for e$ gained back from previous bid
        int roundNum = Round.getRound();          
            
        //get existing Bid information
        Bid existingBid = null;
        BigDecimal existingBidAmount = null;
            
        if (roundNum == 1) {
            existingBid = BidDAO.retrieveUserBidOfCourse(userId, course);
            
            if (existingBid != null) {
                existingBidAmount = existingBid.getAmount();
            }
        }
        
        if (roundNum == 2) {
            existingBid = SuddenDeathDAO.retrieveUserBidOfCourse(userId, course);
            
            if  (existingBid != null) {
                existingBidAmount = existingBid.getAmount();
            }
        }
        
        //add balance back for checking
        if (existingBidAmount != null) {
            balance = balance.add(existingBidAmount);
        }
        
        //check if student has sufficient edollars
        if(balance.compareTo(amount) < 0){
            if (messageList != null) {
                messageList.add("insufficient e$");
            }
            return false;
        }
        
        return true;
    }
    
    /**
     * Returns true if course of the specified course code exists in the Database
     * @param course the course code input
     * @return true if course of the specified course code exists in the Database
     */
    public boolean isCourseValid(String course) {
        Course courseObj = CourseDAO.retrieveByCode(course);
        
        if (courseObj == null) {
            if (messageList != null) {
                messageList.add("invalid code");
            }
            return false;
        }
        
        return true;
    }
    
    /**
     * Returns true if section of course (specified by the course code) exists in the database
     * @param course the course code
     * @param section the section code
     * @return true if section of course (specified by the course code) exists in the database
     */
    public boolean isSectionValid(String course, String section){
        Section sectionObj = SectionDAO.retrieveSection(course,section);
        
        if (sectionObj == null) {
            if (messageList != null) {
                messageList.add("invalid section");
            }
            return false;
        }
        
        return true;
    }
    
    /**
     * Returns true if user exists in the database
     * @param userId - user id of the user
     * @return  true if user exists in the database
     */
    public boolean isUserIdValid(String userId) {
        Student student = StudentDAO.retrieveById(userId);
        
        if (student == null) {
            if (messageList != null) {
                messageList.add("invalid userid");
            }
            return false;
        }
        
        return true;
    }
    
    /**
     * Returns true if the amount of the bid is at the clearing price ($1 more than the lowest bid)
     * See Round 2 Logic.
     * @param amountStr amount of the bid
     * @param course course code of course being bidded
     * @param section section of the course being bidded
     * @return true if the amount of the bid is at the clearing price ($1 more than the lowest bid)
     */
    public boolean isBidEnoughForRound2(String amountStr, String course, String section) {
        int round = Round.getRound();
        
        if (round == 2) {
            BigDecimal clearingPrice = SuddenDeathMaster.getClearingPriceNeededToEnrollForSection(course, section);
            //clearingPrice is NULL when there is NO VACANCY
            
            BigDecimal amount = new BigDecimal(amountStr);
            
            boolean isEnough = true;
                    
            if (clearingPrice != null) {
                isEnough = amount.compareTo(clearingPrice) >= 0;
            }
            
            if (!isEnough) {
                if (messageList != null) {
                messageList.add("bid too low");
                }
                return false;
            }
        }
        
        return true;
    }
    
    /**
     * Returns true if the user has not already placed a bid for this course
     * @param userId user id of the user
     * @param course course code of the course being bidded
     * @return true if the user has not already placed a bid for this course
     */
    public boolean isNotAlreadyBidded(String userId,String course){
        boolean isNotBidded = true;
        ArrayList<Bid> bidList = BidDAO.retrieveByUserId(userId);
        ArrayList<Bid> suddenDeathList = SuddenDeathDAO.retrieveByUserIdPass(userId);
        
        bidList.addAll(suddenDeathList);
        
        for (Bid b: bidList){
            if(b.getCode().equals(course)){
                if (userMessageList != null) {
                    userMessageList.add("already bidded");
                }
                
                isNotBidded = false;
                break;
            }
        }
        return isNotBidded;
    }
    
    /**
     * Returns true if user has not yet been enrolled in the course from previous rounds
     * @param userId user id of the user
     * @param course course code of the course being bidded
     * @return true if user has not yet been enrolled in the course from previous rounds
     */
    public boolean isNotEnrolled(String userId, String course){
        boolean isNotEnrolled = true;
        ArrayList<Bid> bidList = SectionStudentDAO.retrieveByUserId(userId);
        for (Bid b: bidList){
            if(b.getCode().equals(course)){
                isNotEnrolled = false;
                if (messageList != null) {
                    messageList.add("course enrolled");
                }
                break;
            }
        }
        return isNotEnrolled;
    }
     
    /**
     * Returns true if user has no time table clashes between current bid and other bids, enrolled course
     * @param userId user id of the user
     * @param course course code of the course being bidded
     * @param section section of the course being bidded
     * @return Returns true if user has no time table clashes between current bid and other bids, enrolled course
     */
    public boolean hasNoClassTimeTableClash(String userId, String course, String section){
        boolean noTimeClash = true;
        
        Bid existingBid = BidDAO.retrieveUserBidOfCourse(userId,course);
        ArrayList<Bid> bidList = BidDAO.retrieveByUserId(userId);
        ArrayList<Bid> suddenDeathList = SuddenDeathDAO.retrieveByUserId(userId);
        
        ArrayList<Bid> studentSectionList = SectionStudentDAO.retrieveByUserId(userId);
        
        //COMBINE LIST
        bidList.addAll(studentSectionList);
        bidList.addAll(suddenDeathList);
        
        //REMOVE THE SAME BIDS LA! If it is an update
        ListIterator<Bid> iter = bidList.listIterator();
        while (iter.hasNext()) {
            Bid b = iter.next();
            if (b.equals(existingBid)) {
                iter.remove();
            } else if (b.getCode().equals(course)) {
                //REMOVE THE bid with same userid, same course, different section
                iter.remove();
            }
        }
        
        Section currentBid = SectionDAO.retrieveSection(course, section);
        
        for(Bid b: bidList){
            Section sectionCheck = SectionDAO.retrieveSection(b.getCode(), b.getSection());
            java.util.Date sectionCheckStart = sectionCheck.getStart();
            java.util.Date sectionCheckEnd = sectionCheck.getEnd();
            
            //Check whether the time of the current bid doesn't clash with any of other pending bid
            //CONDITION THAT HAS TIME CLASH
            if(sectionCheckStart.before(currentBid.getEnd()) && sectionCheckEnd.after(currentBid.getStart())){
                if(currentBid.getDay() == sectionCheck.getDay()){
                    noTimeClash = false;
                }
            }
        }
        
        if (!noTimeClash) {
            if (messageList != null) {
                messageList.add("class timetable clash");
                }
        }
        
        return noTimeClash;
    }
    
    /**
     * Returns true if there are NO exam clashes
     * @param userId user id of the user
     * @param course course code of the course being bidded
     * @param section section code of the course being bidded
     * @return true if there are NO exam clashes
     */
    public boolean hasNoExamTimeTableClash(String userId, String course, String section){
        boolean noExamClash = true;
        Bid existingBid = BidDAO.retrieveUserBidOfCourse(userId,course);
        ArrayList<Bid> bidList = BidDAO.retrieveByUserId(userId);
        ArrayList<Bid> suddenDeathList = SuddenDeathDAO.retrieveByUserId(userId);

        ArrayList<Bid> studentSectionList = SectionStudentDAO.retrieveByUserId(userId);
        
        //COMBINE LIST
        bidList.addAll(studentSectionList);
        bidList.addAll(suddenDeathList);
        
        //REMOVE THE SAME BIDS LA! If it is an update
        ListIterator<Bid> iter = bidList.listIterator();
        while (iter.hasNext()) {
            Bid b = iter.next();
            if (b.equals(existingBid)) {
                iter.remove();
            } else if (b.getCode().equals(course)) {
                //REMOVE THE bid with same userid, same course, different section
                iter.remove();
            }
        }
        
        Course currentCourse = CourseDAO.retrieveByCode(course);
        java.util.Date examDate = currentCourse.getExamDate();
        java.util.Date examStart = currentCourse.getExamStart();
        java.util.Date examEnd = currentCourse.getExamEnd();
        
        for(Bid b: bidList){
            Course courseCheck = CourseDAO.retrieveByCode(b.getCode());
            java.util.Date courseCheckExamDate = courseCheck.getExamDate();
            java.util.Date courseCheckStart = courseCheck.getExamStart();
            java.util.Date courseCheckEnd = courseCheck.getExamEnd();
            
            if(examDate.equals(courseCheckExamDate)){
                if(examStart.before(courseCheckEnd) && examEnd.after(courseCheckStart)){
                    noExamClash = false;
                }
            }
        }
        
        if (!noExamClash) {
            if (messageList != null) {
                messageList.add("exam timetable clash");
            }
        }
        
        return noExamClash;
        
    }
    
    /**
     * Returns true if no time table clashes (for usage at bidSession.jsp)
     * @param userId user id of the user
     * @param course course code of the course being bidded
     * @return true if no time table clashes (for usage at bidSession.jsp)
     */
    public boolean hasNoExamTimeTableClashSearchCourse(String userId, String course){
        boolean noExamClash = true;
        ArrayList<Bid> bidList = BidDAO.retrieveByUserId(userId);
        ArrayList<Bid> suddenDeathList = SuddenDeathDAO.retrieveByUserIdPass(userId);

        ArrayList<Bid> studentSectionList = SectionStudentDAO.retrieveByUserId(userId);
        
        //COMBINE LIST
        bidList.addAll(studentSectionList);
        bidList.addAll(suddenDeathList);
        
        Course currentCourse = CourseDAO.retrieveByCode(course);
        java.util.Date examDate = currentCourse.getExamDate();
        java.util.Date examStart = currentCourse.getExamStart();
        java.util.Date examEnd = currentCourse.getExamEnd();
        
        for(Bid b: bidList){
            Course courseCheck = CourseDAO.retrieveByCode(b.getCode());
            java.util.Date courseCheckExamDate = courseCheck.getExamDate();
            java.util.Date courseCheckStart = courseCheck.getExamStart();
            java.util.Date courseCheckEnd = courseCheck.getExamEnd();
            
            if(examDate.equals(courseCheckExamDate)){
                if(examStart.before(courseCheckEnd) && examEnd.after(courseCheckStart)){
                    noExamClash = false;
                }
            }
        }
        
        if (!noExamClash) {
            if (messageList != null) {
                messageList.add("exam timetable clash");
            }
        }
        
        return noExamClash;
        
    }
    
    /**
     * Returns true if number of user bids is at most 5 (for handling JSON requests)
     * @param userId user id of the user
     * @param course course code of the course bidded
     * @param section section code of the course bidded
     * @return true if number of user bids is at most 5 (for handling JSON requests)
     */
    public boolean isWithinSectionLimitJson(String userId, String course, String section){
        boolean withinLimit = true;
        
        ArrayList<Bid> bidList = BidDAO.retrieveByUserId(userId);
        ArrayList<Bid> suddenDeathList = SuddenDeathDAO.retrieveByUserId(userId);
        ArrayList<Bid> studentSectionList = SectionStudentDAO.retrieveByUserId(userId);
        
        //account for existing bid
        int roundNum = Round.getRound();          
            
        //get existing Bid information
        Bid existingBid = null;
            
        if (roundNum == 1) {
            existingBid = BidDAO.retrieveUserBidOfCourse(userId, course);
        }
        if (roundNum == 2) {
            existingBid = SuddenDeathDAO.retrieveUserBidOfCourse(userId, course);
        }
        
        if (existingBid != null) {
            bidList.remove(existingBid);
            suddenDeathList.remove(existingBid);
        }
        
        if(bidList.size() + suddenDeathList.size() + studentSectionList.size() >= 5){
            withinLimit = false;
            if (messageList != null) {
                messageList.add("section limit reached");
            }
        }
        
        return withinLimit;
    }
    
    /**
     * Returns true if number of user bids is at most 5 (for handling client-side validation)
     * @param userId user id of the user
     * @return true if number of user bids is at most 5 (for handling client-side validation)
     */
    public boolean isWithinSectionLimit(String userId){
        boolean withinLimit = true;
        
        ArrayList<Bid> bidList = BidDAO.retrieveByUserId(userId);
        ArrayList<Bid> suddenDeathList = SuddenDeathDAO.retrieveByUserId(userId);
        ArrayList<Bid> studentSectionList = SectionStudentDAO.retrieveByUserId(userId);
        
        if(bidList.size() + suddenDeathList.size() + studentSectionList.size() >= 5){
            withinLimit = false;
            if (messageList != null) {
                messageList.add("section limit reached");
            }
        }
        
        return withinLimit;
    }
    
    /**
     * Returns true if the bidding window is open
     * @return true if the bidding window is open
     */
    public boolean isRoundStillRunning(){
        boolean roundValid = true;
        int round = Round.getRound();
        
        if(round <= 0){
            roundValid = false;
            if (messageList != null) {
                messageList.add("round ended");
            }
        }
        return roundValid;
    }
    
    /**
     * Returns true if user is allowed to bid for this course in the current window (user must have the same school as the course bidded in round 1)
     * @param userId user id of the user
     * @param course course code of the course being bidded
     * @return true if user is allowed to bid for this course in the current window (user must have the same school as the course bidded in round 1)
     */
    public boolean isBiddableThisRound(String userId, String course) {
        int round = Round.getRound();
        
        if (round == 1) {
            Student student = StudentDAO.retrieveById(userId);
            Course courseObj = CourseDAO.retrieveByCode(course);
            
            if (!student.getSchool().equals(courseObj.getSchool())) {
                if (messageList != null) {
                    messageList.add("not own school course");
                }
                return false;
            }
        }
        
        return true;
    }
    
    /**
     * Returns true if the current bid already exists in the the database (JSON use)
     * @param userid user id of the user in the bid
     * @param course course code of the bid
     * @param section section code of the course in the the bid
     * @return true if the current bid already exists in the the database
     */
    boolean doesBidExist(String userid, String course, String section) {
        Bid bid = null;
        
        int roundNum = Round.getRound();
        if (roundNum == 1) {
            bid = BidDAO.retrieveUserBidOfCourse(userid, course);
        } else if (roundNum == 2) {
            bid = SuddenDeathDAO.retrieveUserBidOfCourse(userid,course);
        }
        if (bid == null) {
            if (messageList != null) {
                messageList.add("no such bid");
            }
            return false;
        }
        
        return true;
    }

    /**
     * Returns true if the bid is a successful enrollment
     * @param userid user id of the user
     * @param course course code of the course
     * @param section section code of the course
     * @return true if the bid is a successful enrollment
     */
    boolean isEnrollmentFound(String userid, String course, String section) {
        Bid bid = SectionStudentDAO.retrieveSpecificBid(userid,course,section);
        
        if (bid == null) {
            if (messageList != null) {
                messageList.add("no such enrollment record");
            }
            return false;
        }
        
        return true;
    }

    /**
     * Returns true if the current section still has vacancies 
     * @param code course code of the course being bidded
     * @param section section code of the course being bidded
     * @return true if the current section still has vacancies 
     */
    boolean isNotFull(String code, String section) {
        boolean isNotFull = true;
        int roundNum = Round.getRound();
        
        if (roundNum == 2) {
            MinimumPrice minimumPrice = MinimumPriceDAO.retrieve(code, section);
            
            if (minimumPrice.getVacancyLeft() == 0) {
                isNotFull = false;
                
            }
        }
        
        if (roundNum == 1) {
            Section sectionObj = SectionDAO.retrieveSection(code, section);
            if (sectionObj.getSize() == 0) {
                isNotFull = false;
            }
        }
        
        if (!isNotFull) {
            if (messageList != null) {
                    messageList.add("no vacancy");
                }
        }
        
        return isNotFull;
    }
}
