/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.app.utility.validation;

import com.app.model.Course;
import com.app.model.Section;
import com.app.model.Student;
import java.math.BigDecimal;
import java.util.ArrayList;

/**
 *
 * @author Zachery
 */
public class CRUDValidator {
    /**
     * ArrayList<String> for storing error messages for JSON output.
     */
    private final ArrayList<String> messageList;
    
    /**
     * ArrayList<String> for storing error messages for client-side output
     */
    private final ArrayList<String> userMessageList;    //additional error messages for client side
    
    /**
     * The CRUDValidationTool associated with this Validator instance. It performs all validations for each input in the request
     */
    private final CRUDValidationTool tool;
    
    /**
     * ArrayList<String> for storing status messages (for client-side use)
     */
    private final ArrayList<String> statusList;
    
    /**
     * Constructor used when doing server-side validation.
     * @param messageList used to store error messages for json output
     * @param userMessageList used to store error messages for client-side output
     */
    public CRUDValidator(ArrayList<String> messageList, ArrayList<String> userMessageList) {
        this.messageList = messageList;
        this.userMessageList = userMessageList;
        this.tool = new CRUDValidationTool(messageList,userMessageList);
        
        this.statusList = null;
    }
    
    /**
     * Creates a new instance of CRUDValidator for client-side use
     * @param statusList the arraylist of status messages
     */
    public CRUDValidator(ArrayList<String> statusList) {
        //variables initialized to null by default
        this.statusList = statusList;
        
        this.messageList = null;
        this.userMessageList = null;
        this.tool = new CRUDValidationTool(null, null);
    }
    
    
    
    
    /**
     * SERVER SIDE VALIDATION: to be used with EditBidServlet, and AddBidServlet
     * @param userId user id of the user
     * @param amountStr amount of the bid
     * @param code course code of the course being bidded
     * @param section section code of the course being bidded
     * @return true if the incoming request is valid and can be processed
     */
    public boolean isUpdateBidValid (String userId, String amountStr, String code, String section) {
        
        boolean isAmountValid = tool.isAmountValid(amountStr);
        boolean isCourseValid = tool.isCourseValid(code);
        
        boolean isSectionValid = true;
        if (isCourseValid) {
            isSectionValid = tool.isSectionValid(code,section);
        }
          
        boolean isUserIdValid = tool.isUserIdValid(userId);
        
        if ( !isAmountValid || !isCourseValid || !isSectionValid || !isUserIdValid ) {
            return false;
        }

        //ONLY FOR ROUND 2
        boolean isBidEnoughForRound2 = tool.isBidEnoughForRound2(amountStr,code,section);
        
        //continue general validation
        BigDecimal amount = new BigDecimal(amountStr);
        boolean hasEnoughEDollars = tool.hasEnoughEDollars(userId, amount,code,section);
        boolean hasNoClassTimeTableClash = tool.hasNoClassTimeTableClash(userId,code,section);
        boolean hasNoExamTimeTableClash = tool.hasNoExamTimeTableClash(userId,code,section);
        boolean hasCompletedPrerequisites = tool.hasCompletedPrerequisites(userId, code);
        boolean isRoundStillRunning = tool.isRoundStillRunning();
        boolean hasNotCompletedCourse = tool.hasNotCompletedCourse(userId,code);
        boolean isNotEnrolled = tool.isNotEnrolled(userId,code);
        boolean isWithinSectionLimit = tool.isWithinSectionLimitJson(userId,code,section);       
        boolean isBiddableThisRound = tool.isBiddableThisRound(userId, code);
        boolean isNotFull = tool.isNotFull(code,section);
        
        return isBidEnoughForRound2 && hasEnoughEDollars && hasNoClassTimeTableClash
                && hasNoExamTimeTableClash && hasCompletedPrerequisites && isRoundStillRunning
                && hasNotCompletedCourse && isNotEnrolled && isWithinSectionLimit && isBiddableThisRound
                && isNotFull;
        
      
    }
    
    /**
     * Returns true if the client-side incoming bid request is valid
     * @param userId user id of the user
     * @param amountStr amount of the bid
     * @param code course code of the bid
     * @param section section code of the bid
     * @return true if the client-side incoming bid request is valid
     */
    public boolean isUserAddBidValid (String userId, String amountStr, String code, String section) {
        
        boolean isNotAlreadyBidded = tool.isNotAlreadyBidded(userId,code);
        
        return isNotAlreadyBidded;
    }

    /**
     * SERVER SIDE VALIDATION: to be used with DropBidServlet
     * @param userId user id of the user
     * @param course course code of the course being bidded
     * @param section section code of the course being bidded
     * @return true if the incoming request is valid and can be deleted
     */
    public boolean isDeleteBidValid(String userid, String course, String section) {
        boolean isCourseValid = tool.isCourseValid(course);
        //STUPID IDIOTS CANNOT STANDARDIZE THEIR VALIDATION MESSAGES. 
        if (!isCourseValid) {       //HAVE TO REMOVE THE MESSAGE AND ADD A DIFFERENT MESSAGE, BECAUSE MESSAGE IS DIFFERENT FOR UPDATE BID AND DELETE BID
            messageList.remove(0);
            messageList.add("invalid course");
        }
        
        boolean isUserIdValid = tool.isUserIdValid(userid);
        
        boolean isSectionValid = true;
        if (isCourseValid) {
            isSectionValid = tool.isSectionValid(course,section);
        }
        
        
        boolean roundStillRunning = tool.isRoundStillRunning();
        
        if (!isCourseValid || !isUserIdValid || !isSectionValid || !roundStillRunning) {
            return false;
        }
        
        boolean doesBidExist = tool.doesBidExist(userid,course,section);
        
        return doesBidExist;
    }

    /**
     * SERVER SIDE VALIDATION: to be used with DropSectionServlet
     * @param userId user id of the user
     * @param course course code of the course being bidded
     * @param section section code of the course being bidded
     * @return true if the incoming request is valid and enrolled course can be dropped
     */
    public boolean isDropSectionValid(String userid, String course, String section) {
        
        boolean isEnrollmentFound = tool.isEnrollmentFound(userid,course,section);
        
        return isEnrollmentFound;
    }
    
    /*
     * SERVER SIDE VALIDATION SERVER SIDE VALIDATION SERVER SIDE VALIDATION
     */
    
    
    /*
     * CLIENT SIDE VALIDATION CLIENT SIDE VALIDATION CLIENT SIDE VALIDATION
     */
    
    /**
     * The method is used by bid-session.jsp to find out the reason why the Section of the particular course
     * is NOT biddable by the student. If null is returned, there is no reason, in other words,
     * the Student CAN bid for the section / section is biddable by the student.
     * @param student student object representing the user
     * @param section section object of the section being bidded
     * @return 
     *  - the reason for why the section cannot be bidded by the student
     *  - null if the section IS BIDDABLE
     */
    public String getReasonIfNotBiddable(Student studentObj, Section sectionObj) {
        String userId = studentObj.getUserId();
        String code = sectionObj.getCourse();
        String section = sectionObj.getSection();
        BigDecimal amount = studentObj.getEDollars();
        
        boolean isNotFull = tool.isNotFull(code,section);
        if (!isNotFull) {
            return "FULL";
        }
        
        boolean isRoundStillRunning = tool.isRoundStillRunning();
        if (!isRoundStillRunning) {
            return "Window is Closed";
        }
        
        boolean isNotEnrolled = tool.isNotEnrolled(userId,code);
        if (!isNotEnrolled) {
            return "Already Enrolled";
        }
        
        boolean isNotAlreadyBidded = tool.isNotAlreadyBidded(userId,code);
        if (!isNotAlreadyBidded) {
            return "Already Bidded";
        }
        
        boolean isBiddableThisRound = tool.isBiddableThisRound(userId,code);
        if (!isBiddableThisRound) {
            return "Different School";
        }
        
        //continue general validation
        boolean hasNoClassTimeTableClash = tool.hasNoClassTimeTableClash(userId,code,section);
        if (!hasNoClassTimeTableClash) {
            return "Timetable Clash";
        }
        
        boolean hasNoExamTimeTableClash = tool.hasNoExamTimeTableClash(userId,code,section);
        if (!hasNoExamTimeTableClash) {
            return "Exam Clash";
        }
        boolean hasCompletedPrerequisites = tool.hasCompletedPrerequisites(userId, code);
        if (!hasCompletedPrerequisites) {
            return "Incomplete Prerequisites";
        }
        
        boolean hasNotCompletedCourse = tool.hasNotCompletedCourse(userId,code);
        if (!hasNotCompletedCourse) {
            return "Already Completed";
        }

        boolean isWithinSectionLimit = tool.isWithinSectionLimit(userId);
        if (!isWithinSectionLimit) {
            return "Bid Limit Reached";
        }
        
        return null;
    }
    
    /**
     * The method is used by bid.jsp to find out the reason why the particular course
     * is NOT biddable by the student. If null is returned, there is no reason, in other words,
     * the Student CAN bid for the section / section is biddable by the student.
     * @param studentObj student object representing the user
     * @param courseObj course object of the course being bidded
     * @return 
     *  - the reason for why the section cannot be bidded by the student
     *  - null if the section IS BIDDABLE
     */
    public String getReasonIfNotBiddableCourse(Student studentObj, Course courseObj) {
        String userId = studentObj.getUserId();
        String code = courseObj.getCourse();
        BigDecimal amount = studentObj.getEDollars();
        
        boolean isRoundStillRunning = tool.isRoundStillRunning();
        if (!isRoundStillRunning) {
            return "Window is Closed";
        }
        
        boolean isNotEnrolled = tool.isNotEnrolled(userId,code);
        if (!isNotEnrolled) {
            return "Already Enrolled";
        }
        
        boolean isNotAlreadyBidded = tool.isNotAlreadyBidded(userId,code);
        if (!isNotAlreadyBidded) {
            return "Already Bidded";
        }
        
        boolean isBiddableThisRound = tool.isBiddableThisRound(userId,code);
        if (!isBiddableThisRound) {
            return "Different School";
        }
        
        boolean hasNoExamTimeTableClash = tool.hasNoExamTimeTableClashSearchCourse(userId,code);
        if (!hasNoExamTimeTableClash) {
            return "Exam Clash";
        }
        boolean hasCompletedPrerequisites = tool.hasCompletedPrerequisites(userId, code);
        if (!hasCompletedPrerequisites) {
            return "Incomplete Prerequisites";
        }
        
        boolean hasNotCompletedCourse = tool.hasNotCompletedCourse(userId,code);
        if (!hasNotCompletedCourse) {
            return "Already Completed";
        }

        boolean isWithinSectionLimit = tool.isWithinSectionLimit(userId);
        if (!isWithinSectionLimit) {
            return "Bid Limit Reached";
        }
        
        return null;
    }
    /*
     * CLIENT SIDE VALIDATION CLIENT SIDE VALIDATION CLIENT SIDE VALIDATION
     */
}
