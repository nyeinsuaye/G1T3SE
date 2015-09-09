package com.app.utility.validation;

import com.app.json.BootstrapError;
import com.app.model.BootstrapDAO;
import com.app.model.Student;
import com.app.model.StudentDAO;
import java.math.BigDecimal;
import java.util.*;


/**
 * BootstrapValidator validates each List of entries representing the CSV file.
 * All methods invoked by BootstrapValidator affects the original data collection.
 * @author Kevin
 */
public class BootstrapValidator {
    /**
     * ArrayList of BootStrapError objects to be used to be printed in json format.
     */
    private ArrayList<BootstrapError> errorList;    //TO STORE ERROR MESSAGES TO BE PRINTED BY JSON
    
    /**
     * an internal list to black list the entries needed to be remove before inserting into database.
     */
    private ArrayList<String[]> blacklistedEntries;     //TO BLACKLIST AND REMOVE EVENTUALLY
    
    /**
     * The List<String[]> object returned by OpenCSV that contains the data in the CSV file
     */
    private List<String[]> entries;     
    
    /**
     * the name of each column. The first row of the csv file contains the name of the column of values
     */
    private String[] fieldHeaders;
    
    /**
     * attribute to hold the ValidationTool used to validate the entire CSV file
     */
    private final BootstrapValidationTool tool;      //each validator can only have 1 tool, final to prevent users from instantiating on their own.
    
    /**
     * to hold the entries that are updates of previous entries in the CSV / list
     */
    private ArrayList<String[]> alreadyBiddedList = new ArrayList<String[]>();
    
    /**
     * getter for alreadyBiddedList
     * @return an ArrayList of String[] that is the row of data that are old bid that are being overridden my bids appearing later in the list
     */
    public ArrayList<String[]> getAlreadyBiddedList() {
        return alreadyBiddedList;
    }
    
    /**
     * The default constructor that creates a new instance of BootstrapValidator
     */
    public BootstrapValidator() {
        errorList = BootstrapDAO.getErrorList();    
        
        blacklistedEntries = BootstrapDAO.getBlacklistedEntries();  
        entries = BootstrapDAO.getEntries();
        fieldHeaders = BootstrapDAO.getFieldHeaders();
        
        tool =  new BootstrapValidationTool(entries, fieldHeaders, errorList, blacklistedEntries);   //Validation tools instantiated here.
    }
    
    /**
     * Return list of blacklisted entries
     * @return list of blacklisted entries
     */
    public ArrayList<String[]> getBlacklistedEntries() {
        return blacklistedEntries;
    }

    /**
     * Return list of entries data from OpenCSV
     * @return list of entries data from OpenCSV
     */
    public List<String[]> getEntries() {
        return entries;
    }

    /**
     * Return column titles
     * @return column titles 
     */
    public String[] getFieldHeaders() {
        return fieldHeaders;
    }
    
    
    
    /**
     * Validate Student entries
     */
    public void validateStudent() {         
        String file = "student.csv";
        
        //CHECK ALL ENTRIES FOR BLANK FIELDS
        for (int i = 0; i < entries.size(); i++) {
            boolean rowHasMissingFields = false;
            //Line Number. First row (index 0) is data row. So +2 to get line number.
            int line = i + 2;
            ArrayList<String> rowMessageList = new ArrayList<>();
            String[] row = entries.get(i);
            
            rowHasMissingFields = tool.rowHasMissingFields(row, rowMessageList);
            
            if (rowHasMissingFields) {
                blacklistedEntries.add(row);
                
                String[] message = rowMessageList.toArray(new String[0]);
                errorList.add(new BootstrapError(file,line,message));
            }
        }
        
        //STEP 2: CHECK ALL FOR duplicateUserid and invalidEDollar
        for (int i = 0; i < entries.size(); i++) {
            boolean rowHasDuplicateUserId = false;        
            boolean rowHasInvalidEDollar = false;
            
            int line = i + 2;
            ArrayList<String> rowMessageList = new ArrayList<>();
            String[] row = entries.get(i);
            
            //if not already blacklisted due to error
            if (!blacklistedEntries.contains(row)) {
                
                //if there are duplicates, the latest/bottomest row will be accepted.
                //The earlier rows will be errored.
                rowHasDuplicateUserId = tool.rowHasDuplicatePrimaryKey("userid",row,line, rowMessageList);
                rowHasInvalidEDollar = tool.rowHasInvalidEDollar(row, rowMessageList);
                
                //Blacklist row if any of the validations blacklistedEntries are "true"
                if ( rowHasInvalidEDollar || rowHasDuplicateUserId) {
                    blacklistedEntries.add(row);                //add row to blackList
                    
                    String[] message = rowMessageList.toArray(new String[0]);
                    errorList.add(new BootstrapError(file,line,message));
                }
            
            }
   
        }
        
        //method ends
    }
    
    /**
     * Validate course entries (course.csv)
     */
    public void validateCourse() {
        String file = "course.csv";
        
        //CHECK ALL ENTRIES FOR BLANK FIELDS
        for (int i = 0; i < entries.size(); i++) {
            boolean rowHasMissingFields = false;
            //Line Number. First row (index 0) is data row. So +2 to get line number.
            int line = i + 2;
            ArrayList<String> rowMessageList = new ArrayList<>();
            String[] row = entries.get(i);
            
            rowHasMissingFields = tool.rowHasMissingFields(row, rowMessageList);
            
            if (rowHasMissingFields) {
                blacklistedEntries.add(row);
                
                String[] message = rowMessageList.toArray(new String[0]);
                errorList.add(new BootstrapError(file,line,message));
            }
        }
        
        
        //STEP 2: CHECK ALL FOR invalidExamDate invalidStart, invalidEnd
        for (int i = 0; i < entries.size(); i++) {
            boolean rowHasInvalidExamDate = false;
            boolean rowHasInvalidExamStart = false;
            boolean rowHasInvalidExamEnd = false;
            boolean rowHasDuplicateCourse = false;
            
            int line = i + 2;
            ArrayList<String> rowMessageList = new ArrayList<>();
            String[] row = entries.get(i);
            
            //if not already blacklisted due to error
            if (!blacklistedEntries.contains(row)) {
                
                rowHasInvalidExamDate = tool.rowHasInvalidExamDate(row, rowMessageList);
                rowHasInvalidExamStart = tool.rowHasInvalidStart("exam start",row,rowMessageList);
                rowHasInvalidExamEnd = tool.rowHasInvalidEnd("exam start", "exam end", row, rowMessageList);
                
                //if there are duplicates, the latest/bottomest row will be accepted.
                //The earlier rows will be errored.
                //rowHasDuplicateCourse = tool.rowHasDuplicatePrimaryKey("course",row,rowMessageList);
                /*
                 * NO NEED TO TEST FOR DUPLICATES
                 */
                
                //Blacklist row if any of the validations blacklistedEntries are "true"
                if ( rowHasInvalidExamDate || rowHasInvalidExamStart || rowHasInvalidExamEnd
                        || rowHasDuplicateCourse) {
                    blacklistedEntries.add(row);                //add row to blackList
                    
                    String[] message = rowMessageList.toArray(new String[0]);
                    errorList.add(new BootstrapError(file,line,message));
                }
            
            }
   
        }   //end of 2nd for-loop
        
        //method ends        
    }
    
    /**
     * Validate section entries (section.csv)
     */
    public void validateSection() {
        String file = "section.csv";
        
        //CHECK ALL ENTRIES FOR BLANK FIELDS
        for (int i = 0; i < entries.size(); i++) {
            boolean rowHasMissingFields = false;
            //Line Number. First row (index 0) is data row. So +2 to get line number.
            int line = i + 2;
            ArrayList<String> rowMessageList = new ArrayList<>();
            String[] row = entries.get(i);
            
            rowHasMissingFields = tool.rowHasMissingFields(row, rowMessageList);
            
            if (rowHasMissingFields) {
                blacklistedEntries.add(row);
                
                String[] message = rowMessageList.toArray(new String[0]);
                errorList.add(new BootstrapError(file,line,message));
            }
        }
        
        
        //STEP 2: CHECK ALL FOR invalidExamDate invalidStart, invalidEnd
        for (int i = 0; i < entries.size(); i++) {
            boolean rowHasInvalidCourse = false;
            boolean rowHasInvalidSection = false;
            boolean rowHasInvalidDay = false;
            boolean rowHasInvalidStart = false;
            boolean rowHasInvalidEnd = false;
            boolean rowHasInvalidSize = false;
            boolean rowHasDuplicatePrimaryKey = false;
            
            int line = i + 2;
            ArrayList<String> rowMessageList = new ArrayList<>();
            String[] row = entries.get(i);
            
            //if not already blacklisted due to error
            if (!blacklistedEntries.contains(row)) {
                
                rowHasInvalidCourse = tool.rowHasInvalidCourse(row, rowMessageList);
                if (!rowHasInvalidCourse) {
                    rowHasInvalidSection = tool.rowHasInvalidSection(row,rowMessageList);
                }
                rowHasInvalidDay = tool.rowHasInvalidDay(row, rowMessageList);
                rowHasInvalidStart = tool.rowOfSectionHasInvalidStart("start", row, rowMessageList);
                rowHasInvalidEnd = tool.rowOfSectionHasInvalidEnd("start","end",row,rowMessageList);
                rowHasInvalidSize = tool.rowHasInvalidSize(row,rowMessageList);
                
                //if there are duplicates, the latest/bottomest row will be accepted.
                //The earlier rows will be errored.
                //String foreignKey = "course";
                //String primaryKey = "section";
                //rowHasDuplicatePrimaryKey = tool.rowHasDuplicatePrimaryKey(foreignKey,primaryKey,row,rowMessageList);
                /*
                 * NO NEED TO TEST FOR DUPLICATES
                 */
                
                //Blacklist row if any of the validations blacklistedEntries are "true"
                if ( rowHasInvalidCourse|| rowHasInvalidSection || rowHasInvalidDay
                        || rowHasInvalidStart || rowHasInvalidEnd || rowHasInvalidSize) {
                    blacklistedEntries.add(row);                //add row to blackList
                    
                    String[] message = rowMessageList.toArray(new String[0]);
                    errorList.add(new BootstrapError(file,line,message));
                }
            
            }
   
        }   //end of 2nd for-loop
        
        //method ends        
    }
    
    /**
     * Validate prerequisites entries (prerequisite.csv)
     */
    public void validatePrerequisites() {
        String file = "prerequisite.csv";
        
        //CHECK ALL ENTRIES FOR BLANK FIELDS
        for (int i = 0; i < entries.size(); i++) {
            boolean rowHasMissingFields = false;
            //Line Number. First row (index 0) is data row. So +2 to get line number.
            int line = i + 2;
            ArrayList<String> rowMessageList = new ArrayList<>();
            String[] row = entries.get(i);
            
            rowHasMissingFields = tool.rowHasMissingFields(row, rowMessageList);
            
            if (rowHasMissingFields) {
                blacklistedEntries.add(row);
                
                String[] message = rowMessageList.toArray(new String[0]);
                errorList.add(new BootstrapError(file,line,message));
            }
        }
        
        //STEP 2: CHECK ALL FOR invalidExamDate invalidStart, invalidEnd
        for (int i = 0; i < entries.size(); i++) {
            boolean rowHasInvalidCourse = false;
            boolean rowHasInvalidPrerequisite = false;
            boolean rowHasDuplicatePrimaryKey = false;
            
            int line = i + 2;
            ArrayList<String> rowMessageList = new ArrayList<>();
            String[] row = entries.get(i);
            
            //if not already blacklisted due to error
            if (!blacklistedEntries.contains(row)) {
                
                rowHasInvalidCourse = tool.rowHasInvalidCourse(row, rowMessageList);
                rowHasInvalidPrerequisite = tool.rowHasInvalidPrerequisite(row, rowMessageList);
                
                //if there are duplicates, the latest/bottomest row will be accepted.
                //The earlier rows will be errored.
                String foreignKey = "course";
                String primaryKey = "prerequisite";
                //rowHasDuplicatePrimaryKey = tool.rowHasDuplicatePrimaryKey(foreignKey,primaryKey,row,rowMessageList);
                /*
                 * NO NEED TO TEST FOR DUPLICATES
                 */
                
                //Blacklist row if any of the validations blacklistedEntries are "true"
                if ( rowHasInvalidCourse|| rowHasInvalidPrerequisite || rowHasDuplicatePrimaryKey ) {
                    blacklistedEntries.add(row);                //add row to blackList
                    
                    String[] message = rowMessageList.toArray(new String[0]);
                    errorList.add(new BootstrapError(file,line,message));
                }
            
            }
   
        }   //end of 2nd for-loop
        
        //method ends        
    }
    
    /**
     * Validate course_completed entries (course_completed.csv)
     */
    public void validateCourseCompleted() {
        String file = "course_completed.csv";
        
        //CHECK ALL ENTRIES FOR BLANK FIELDS
        for (int i = 0; i < entries.size(); i++) {
            boolean rowHasMissingFields = false;
            //Line Number. First row (index 0) is data row. So +2 to get line number.
            int line = i + 2;
            ArrayList<String> rowMessageList = new ArrayList<>();
            String[] row = entries.get(i);
            
            rowHasMissingFields = tool.rowHasMissingFields(row, rowMessageList);
            
            if (rowHasMissingFields) {
                blacklistedEntries.add(row);
                
                String[] message = rowMessageList.toArray(new String[0]);
                errorList.add(new BootstrapError(file,line,message));
            }
        }
        
        //STEP 2: CHECK ALL FOR invalidExamDate invalidStart, invalidEnd
        for (int i = 0; i < entries.size(); i++) {
            boolean rowHasInvalidUserId = false;
            boolean rowHasInvalidCourse = false;
            boolean rowHasDuplicatePrimaryKey = false;
            
            int line = i + 2;
            ArrayList<String> rowMessageList = new ArrayList<>();
            String[] row = entries.get(i);
            
            //if not already blacklisted due to error
            if (!blacklistedEntries.contains(row)) {
                
                rowHasInvalidUserId = tool.rowHasInvalidUserId(row, rowMessageList);
                rowHasInvalidCourse = tool.rowHasInvalidCourse(row, rowMessageList);
                
                //if there are duplicates, the latest/bottomest row will be accepted.
                //The earlier rows will be errored.
                String foreignKey = "userid";
                String primaryKey = "code";
                //rowHasDuplicatePrimaryKey = tool.rowHasDuplicatePrimaryKey(foreignKey,primaryKey,row,rowMessageList);
                /*
                 * NO NEED TO TEST FOR DUPLICATES
                 */
                
                //Blacklist row if any of the validations blacklistedEntries are "true"
                if ( rowHasInvalidCourse|| rowHasInvalidUserId || rowHasDuplicatePrimaryKey ) {
                    blacklistedEntries.add(row);                //add row to blackList
                    
                    String[] message = rowMessageList.toArray(new String[0]);
                    errorList.add(new BootstrapError(file,line,message));
                }
            
            }
   
        }   //end of 2nd for-loop
        
        //STEP 2: CHECK ALL FOR invalidExamDate invalidStart, invalidEnd
        for (int i = 0; i < entries.size(); i++) {
            boolean rowHasInvalidCourseCompleted = false;
            
            int line = i + 2;
            ArrayList<String> rowMessageList = new ArrayList<>();
            String[] row = entries.get(i);
            
            //if not already blacklisted due to error
            if (!blacklistedEntries.contains(row)) {
                
                rowHasInvalidCourseCompleted = tool.rowHasInvalidCourseCompleted(row, rowMessageList);
                
                //Blacklist row if any of the validations blacklistedEntries are "true"
                if ( rowHasInvalidCourseCompleted ) {
                    blacklistedEntries.add(row);                //add row to blackList
                    
                    String[] message = rowMessageList.toArray(new String[0]);
                    errorList.add(new BootstrapError(file,line,message));
                }
            
            }
   
        }   //end of 3rd for-loop
        //method ends        
    }
    
    /**
     * Validate bid entries (bid.csv)
     */
    public void validateBid() {
        String file = "bid.csv";
        
        //CHECK ALL ENTRIES FOR BLANK FIELDS
        for (int i = 0; i < entries.size(); i++) {
            boolean rowHasMissingFields = false;
            //Line Number. First row (index 0) is data row. So +2 to get line number.
            int line = i + 2;
            ArrayList<String> rowMessageList = new ArrayList<>();
            String[] row = entries.get(i);
            
            rowHasMissingFields = tool.rowHasMissingFields(row, rowMessageList);
            
            if (rowHasMissingFields) {
                blacklistedEntries.add(row);
                
                String[] message = rowMessageList.toArray(new String[0]);
                errorList.add(new BootstrapError(file,line,message));
            }
        }
        
        //STEP 2: CHECK ALL FOR invalid formatting and duplicates
        for (int i = 0; i < entries.size(); i++) {
            boolean rowHasInvalidUserId = false;
            boolean rowHasInvalidAmount = false;
            boolean rowHasInvalidCode = false;
            boolean rowHasInvalidSection = false;
            
            boolean rowHasDuplicatePrimaryKey = false;
            
            int line = i + 2;
            ArrayList<String> rowMessageList = new ArrayList<>();
            String[] row = entries.get(i);
            
            //if not already blacklisted due to error
            if (!blacklistedEntries.contains(row)) {
                
                rowHasInvalidUserId = tool.rowHasInvalidUserId(row, rowMessageList);
                rowHasInvalidAmount = tool.rowHasInvalidAmount(row,rowMessageList);
                rowHasInvalidCode = tool.rowHasInvalidCourse(row,rowMessageList);
                if (!rowHasInvalidCode) {
                    rowHasInvalidSection = tool.rowHasInvalidSection(row,rowMessageList);   //checks format
                    if (!rowHasInvalidSection) {    //check existence only if format is CORRECT
                        rowHasInvalidSection = tool.rowHasInvalidSectionExistence(row, rowMessageList); //checks existence
                    }
                }
                
                //if there are duplicates, the latest/bottomest row will be accepted.
                //The earlier rows will be errored.
                //String foreignKey1 = "code";
                //String foreignKey2 = "section";
                //String primaryKey = "userid";
                //rowHasDuplicatePrimaryKey = tool.rowHasDuplicatePrimaryKey(foreignKey1,foreignKey2,primaryKey,row,rowMessageList);
                /*
                 * NO NEED TO TEST FOR DUPLICATES
                 */
                
                //Blacklist row if any of the validations blacklistedEntries are "true"
                if ( rowHasInvalidUserId || rowHasInvalidAmount || rowHasInvalidCode
                        || rowHasInvalidSection || rowHasDuplicatePrimaryKey ) {
                    blacklistedEntries.add(row);                //add row to blackList
                    
                    String[] message = rowMessageList.toArray(new String[0]);
                    errorList.add(new BootstrapError(file,line,message));
                }
            
            }
   
        }   //end of 2nd for-loop
        
        /*
         * 
         * FROM HERE ONWARDS, ALL THE BIDS ARE CONSIDERED VALID, but have not met logical validation
         * Therefore, we should debit the student EDollar before Validation, 
         * and only credit back if validation fails.
         * (Because debit/credit needs to be done on a per row basis, 
         * and they need to constantly check if student has enough EDollar to place bid,
         * GIVEN that the previous bids are all accounted for.
         * 
         */
        
        
        
        //3rd loop
        for (int i = 0; i < entries.size(); i++) {
            boolean rowHasNotOwnSchoolCourse = false;            
            boolean rowHasClassClash = false;
            boolean rowHasExamClash = false;
            boolean rowHasIncompletePrerequisites = false;
            boolean rowHasAlreadyCompletedCourse = false;
                   
            //THIS IS NOT SPECIFIED
            boolean rowHasAlreadyBiddedForCourse = false;   //test for bids for different sections in the same course
            
            boolean rowHasSectionLimitReached = false;
            boolean rowHasNotEnoughEDollars = false;
            
            int line = i + 2;
            ArrayList<String> rowMessageList = new ArrayList<>();
            String[] row = entries.get(i);
            
            //if not already blacklisted due to error
            if (!blacklistedEntries.contains(row)) {
                
                rowHasNotOwnSchoolCourse = tool.rowHasNotOwnSchoolCourse(row,rowMessageList);
                rowHasClassClash = tool.rowHasBidClassClash(row,line,rowMessageList);
                rowHasExamClash = tool.rowHasBidExamClash(row, line, rowMessageList); 
                rowHasIncompletePrerequisites = tool.rowHasIncompletePrerequisites(row,rowMessageList);
                rowHasAlreadyCompletedCourse = tool.rowHasAlreadyCompletedCourse(row,rowMessageList);
                
                /* 
                 * HERE WE START TO ACCOUNT FOR BID AMOUNT, because the above validations has passed.
                 * (note: it doesnt matter if this line is on top, or below. Put here for the sake of easier visualizing)
                 */
                int colNumOfAmount = tool.getColumnNumber("amount");
                BigDecimal amount = new BigDecimal(row[colNumOfAmount]);
                int colNumOfUserId = tool.getColumnNumber("userid");
                String userid = row[colNumOfUserId];
                
                //UGLY PATCH: if student not enough e$, ignore the student's bid update
                boolean toIgnore = tool.rowHasNotEnoughEDollars(row,null,line);
                
                if (!toIgnore) {
                    rowHasAlreadyBiddedForCourse = tool.rowHasAlreadyBiddedForCourse(row, line, alreadyBiddedList);
                }
                
                //YOU ARE ADDING THE CURRENT ROW WHICH IS WRONG.
                /*
                if (rowHasAlreadyBiddedForCourse) {
                    alreadyBiddedList.add(row);
                }  
                */
                
                //MUST CHECK WITH PROF if you still need to check section limit, not enough e-dollars after
                // already bidded for course. SINCE THEY ARE CONFLICTING.
                
                rowHasSectionLimitReached = tool.rowHasSectionLimitReached(row, line, rowMessageList);
                rowHasNotEnoughEDollars = tool.rowHasNotEnoughEDollars(row, rowMessageList, line);
                            
                //Blacklist row if any of the validations blacklistedEntries are "true"
                if ( rowHasNotOwnSchoolCourse || rowHasClassClash || rowHasExamClash || rowHasIncompletePrerequisites
                        || rowHasAlreadyCompletedCourse || rowHasSectionLimitReached || rowHasNotEnoughEDollars ) {
                    blacklistedEntries.add(row);                //add row to blackList
                    
                    String[] message = rowMessageList.toArray(new String[0]);
                    errorList.add(new BootstrapError(file,line,message));
                                      
                } else {
                    
                    BigDecimal balance = null;
                    Student student = StudentDAO.retrieveById(userid);
                    if (student != null) {
                        balance = student.getEDollars().subtract(amount);
                    }
                    //UPDATE DB ONLY AT THE END TO REDUCE NUMBER OF CONNECTIONS.
                    StudentDAO.updateEDollars(userid, balance);
                    System.out.println(userid + ":" +balance);
                }
                
                /*
                if (rowHasAlreadyBiddedForCourse) {
                    blacklistedEntries.add(row);
                }
                */
                
            
            }
   
        }   //end of 3rd for-loop
        
        //blacklistedEntries.removeAll(alreadyBiddedList);
        
              
        /*
         * THE FOURTH AND FIFTH LOOPS BELOW ARE JOINED INTO THE THIRD LOOP. DUE TO REQUIREMENTS
         */
        
        /*
        //4th loop  MAY NOT BE NECESSARY. But it will be safer i think. Just DO IT. (CHECK BACK)
        for (int i = 0; i < entries.size(); i++) {
                        
            boolean rowHasAlreadyBiddedForCourse = false;   //test for bids for different sections in the same course
            
            String line = Integer.toString(i+2);
            ArrayList<String> rowMessageList = new ArrayList<>();
            String[] row = entries.get(i);
            
            //if not already blacklisted due to error
            if (!blacklistedEntries.contains(row)) {
                
                rowHasAlreadyBiddedForCourse = tool.rowHasAlreadyBiddedForCourse(row, rowMessageList);
                
                //Blacklist row if any of the validations blacklistedEntries are "true"
                if ( rowHasAlreadyBiddedForCourse ) {
                    blacklistedEntries.add(row);                //add row to blackList
                    
                    String[] message = rowMessageList.toArray(new String[0]);
                    errorList.add(new BootstrapError(file,line,message));
                }
            
            }
   
        }   //end of 4th for-loop
        */
        
        
        /*
        //5th loop
        for (int i = 0; i < entries.size(); i++) {
            boolean rowHasSectionLimitReached = false;
            boolean rowHasNotEnoughEDollars = false;
            
            String line = Integer.toString(i+2);
            ArrayList<String> rowMessageList = new ArrayList<>();
            String[] row = entries.get(i);
            
            //if not already blacklisted due to error
            if (!blacklistedEntries.contains(row)) {
                
                rowHasSectionLimitReached = tool.rowHasSectionLimitReached(row, rowMessageList);
                rowHasNotEnoughEDollars = tool.rowHasNotEnoughEDollars(row, rowMessageList);
               
                
                //Blacklist row if any of the validations blacklistedEntries are "true"
                if ( rowHasSectionLimitReached || rowHasNotEnoughEDollars) {
                    blacklistedEntries.add(row);                //add row to blackList
                    
                    String[] message = rowMessageList.toArray(new String[0]);
                    errorList.add(new BootstrapError(file,line,message));
                }
            
            }
   
        }   //end of 4th for-loop
        */
              
        //method ends       
    }
    
}
