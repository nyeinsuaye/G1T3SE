/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.app.clearing;

import com.app.model.Bid;
import com.app.model.BidDAO;
import com.app.model.BidTempDAO;
import com.app.model.FailedBidDAO;
import com.app.model.Section;
import com.app.model.SectionDAO;
import com.app.model.SectionStudentDAO;
import com.app.model.Student;
import com.app.model.StudentDAO;
import com.app.model.two.SuddenDeathDAO;
import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Students will only be informed of their bid status after the end of the round.
 * After the end of the round, sort the bids from the highest to lowest.
 * Derive the minimum clearing price based on the number of vacancies, i.e. if the class has 35 vacancies, the 35th highest bid is the clearing price.
 * If there is only one bid at the clearing price, it will be successful. Otherwise, all bids at the clearing price will be dropped regardless of whether they can technically all be accommodated (refer to example).
 * Students are charged based on the amount they bid.
 * Student will be refunded e$ for unsuccessful bids.
 * The minimum bid for round 1 is $10.
 * 

 * @author Zachery
 */
public class RoundClearingMaster {
    
    /**
     * Executes Round 1 clearing process with its associated clearing logic.
     * It will execute changes in the MySQL DB. Upon populating section_student table (for successful bids) and failed_bid table (for failed bids), bid table will be emptied.
     * @throws IllegalStateException 
     */
    public static void executeRound1ClearingProcess() throws IllegalStateException {
        
        //1.
        //retrieve all bids from the DB
        //Bids are sorted in order of code (ASC), section (ASC), amount (DESC)
        //It is more efficient for a MySQL query to return a sorted ResultSet than to sort it in using Collections
        ArrayList<Bid> bidList = BidDAO.retrieveAllInOrder();
        
        //SIDE: FOR DUMP TABLE, add all to bid_temp
        BidTempDAO.addAll(bidList);
        
        
        //2. Test for exceptional cases
        if (bidList.isEmpty()) {
            throw new IllegalStateException("There are no bids to be cleared");
            //Signals that a method has been invoked at an illegal or inappropriate time. 
            //In other words, the Java environment or Java application is not in an appropriate state for the requested operation.
        }
        
        
        //3. Perform clearing. Process will iterate UNTIL all the bids have been processed, therefore becomes empty.
        while(!bidList.isEmpty()) {
            
            //pull out the first Bid in index 0 of the ArrayList.
            //It will serve as a template to retrieve all other bids of the same course and section
            Bid bidElement = bidList.get(0);
            
            //4. Isolate the specificBids from a specific section of a specfic course
            //a specificBidList to store bids for a specific section of a specific course.
            ArrayList<Bid> specificBidList = isolateSpecificBids(bidList,bidElement);
            
            //5. retreive the Section for more information associated with these bids.
            Section specificSection = SectionDAO.retrieveSection(bidElement.getCode(), bidElement.getSection());
            
            //6. determine clearing price in the specificBidList using information from the specificSection
            BigDecimal clearingPrice = getClearingPrice(specificBidList, specificSection);
            
            //7. retrieve the successfulBids at the clearing price.
            ArrayList<Bid> successfulBids = retrieveSuccessfulBids(specificBidList, clearingPrice);
            
            //8. Only unsuccessfulBids will have the EDollars refunded.
            specificBidList.removeAll(successfulBids);
            creditEDollars(specificBidList);
            
            //people with successfulBids, already paid for the bids during bidding. Nothing has to be updated.
            
            //9. add successfulBids to section_student table
            SectionStudentDAO.addAll(successfulBids);
            FailedBidDAO.addAll(specificBidList);   //add failed bids so that we can generate bid status for students
            
        }
        
        //Once clearing is done, clear bid table.
        BidDAO.emptyTable();
        
    }
    
    /**
     * Executes Round 2 clearing process with its associated clearing logic.
     */
    public static void executeRound2ClearingProcess() {
        FailedBidDAO.emptyTable();
        
        //1.
        //retrieve all bids from the DB
        //Bids are sorted in order of code (ASC), section (ASC), amount (DESC)
        //It is more efficient for a MySQL query to return a sorted ResultSet than to sort it in using Collections
        ArrayList<Bid> suddenDeathListPass = SuddenDeathDAO.retrieveAllInOrderPass();
        
        //3. Perform clearing. Process will iterate UNTIL all the bids have been processed, therefore becomes empty.
        if (!suddenDeathListPass.isEmpty()) {
            
            //add successfulBids to section_student table
            SectionStudentDAO.addAll(suddenDeathListPass);
        }
        
        //REFUND those that fail
        ArrayList<Bid> suddenDeathListFail = SuddenDeathDAO.retrieveAllInOrderFail();
        FailedBidDAO.addAll(suddenDeathListFail);
        
        creditEDollars(suddenDeathListFail);
        
        //credit excessEdollars too. THIS IS FOR ALL STUDENTS
        StudentDAO.refundExcessDollars();
        
        
        ArrayList<Bid> combinedList = new ArrayList<>();
        combinedList.addAll(suddenDeathListPass);
        combinedList.addAll(suddenDeathListFail);
        
        //Once clearing is done, clear bid table.
        BidTempDAO.addAll(combinedList);    //NEED TO ADD for PASS and ADD FOR FAIL! TAKE NOTE
        
        SuddenDeathDAO.emptyTable();
        
    }
       
    /**
     * Returns an ArrayList of Bids that has the same course and section as the specified Bid element, from the main list specified.
     * The bids that are isolated and returned, will be removed from the main list upon the execution of this method.
     * @param mainList ArrayList<Bid>
     * @param bidElement Bid 
     * @return ArrayList<Bid> ArrayList<Bid> which has the same course and section as the specified Bid element
     */
    private static ArrayList<Bid> isolateSpecificBids(ArrayList<Bid> mainList, Bid bidElement) {
        //a specificBidList to store bids for a specific section of a specific course.
            ArrayList<Bid> specificBidList = new ArrayList<Bid>();
            
            String codeOfBidElement = bidElement.getCode();
            String sectionOfBidElement = bidElement.getSection();

            //iterate through main bidList and separate those that belong to the same section of the same course
            for (Bid bid: mainList) {
                if (codeOfBidElement.equals(bid.getCode()) && sectionOfBidElement.equals(bid.getSection())) {
                    specificBidList.add(bid);
                } else {
                    //break for-loop immediately upon the first mismatch. Since list has already been ordered.
                    break;
                }
            }

            //removeAll from bidList. Pass-by value ensures that the original list was being manipulated.
            mainList.removeAll(specificBidList);
            
            return specificBidList;
    }
    
    /**
     * Returns the clearing price of the specified list of bids, determined by the class size of the specified Section element
     * @param specificBidList the bid list that you want the clearing price to be determined for.
     * @param sectionElement the Section element that corresponds to the list of bids specified.
     * @return ArrayList<Bid> the clearing price of the specified list of bids, determined by the class size of the specified Section element
     */
    private static BigDecimal getClearingPrice(ArrayList<Bid> specificBidList, Section sectionElement) {
        
        //determine class/section size
        int classSize = sectionElement.getSize();
        
        //corner case where size is 0
        if (classSize == 0) {
            return null;
        }
        
        //determine num of bids for the class/section
        int numBids = specificBidList.size();
        
        BigDecimal clearingPrice = null;
        
        //More bids than size
        if (numBids - classSize > 0) {
            //if class size is 35, retrieve the bid at index 34 (index starts from 0) and determine the clearing price
            clearingPrice = specificBidList.get(classSize - 1).getAmount();
        } else {
            //the logical logic (old)
            //clearingPrice = BigDecimal.TEN;
            
            //the illogical logic (new)
            clearingPrice = specificBidList.get(numBids - 1).getAmount();
            //clearing price is the last bid, (even if there are empty spaces)
        }
         
        return clearingPrice;
        
    }
    
    /**
     * Return a list of bids that is greater than the clearing price. These bids are successful.
     * Bids equal to the clearing price WILL NOT be successful
     * @param specificBidList
     * @param clearingPrice
     * @return 
     *      a list of bids that is greater than the clearing price.
     */
    private static ArrayList<Bid> retrieveSuccessfulBids(ArrayList<Bid> specificBidList, 
            BigDecimal clearingPrice) {
        
        if (clearingPrice == null) {
            return new ArrayList<Bid>();    //if clearingPrice is null, return empty arraylist
        }
        
        ArrayList<Bid> successfulBids = new ArrayList<>();
        
        ArrayList<Bid> bidsAtClearingPrice = new ArrayList<>();
        
        for (Bid bid: specificBidList) {
            if ( bid.getAmount().compareTo(clearingPrice) == 1 ) {
                successfulBids.add(bid);
            } else if ( bid.getAmount().compareTo(clearingPrice) == 0 ) {
                bidsAtClearingPrice.add(bid);
            } else {
                break;
                //can break immediately
                //since they are NOT successful, and the list is already in descending order.
            }
        }
        
        //if there is ONLY 1 bid that matches the clearing price, the bid will be successful
        // more than 1, NO GO. everyone don't get.
        if (bidsAtClearingPrice.size() == 1) {
            successfulBids.addAll(bidsAtClearingPrice);
        }
        
        return successfulBids;
    }
    
    private static void creditEDollars(ArrayList<Bid> unsuccessfulBids) {
        ArrayList<Student> studentList = new ArrayList<Student>();
        
        for (Bid bid : unsuccessfulBids) {
            String userid = bid.getUserId();
            
            Student student = StudentDAO.retrieveById(userid);
            
            student.setEDollars(student.getEDollars().add(bid.getAmount()));
            
            studentList.add(student);
        }
        
        StudentDAO.batchUpdateEDollars(studentList);
        
    }
}
