/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.app.utility.validation;


import com.app.json.BootstrapError;
import com.app.model.Course;
import com.app.model.CourseDAO;
import com.app.model.Section;
import com.app.model.SectionDAO;
import com.app.model.Student;
import com.app.model.StudentDAO;
import com.app.utility.ErrorUtility;
import java.math.BigDecimal;
import java.math.MathContext;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *  This class contains all the validation logic that validates EACH row of data in the List<String> returned by
 *  OpenCSV tool. A BootstrapValidator is required to be instantiated to make use of this class.
 * @author Zachery
 */
class BootstrapValidationTool {      //only instantiable by Validator
    /**
     * The List<String[]> object returned by OpenCSV that contains the data in the CSV file
     */
    private List<String[]> entries;     //for checking against the list
    /**
     * the name of each column. The first row of the csv file contains the name of the column of values
     */
    private String[] fieldHeaders;      //for cycling
    
    
    /**
     * ArrayList of BootStrapError objects to be used to be printed in json format.
     */
    private ArrayList<BootstrapError> errorList;    //unused in validationtool.
    /**
     * an internal list to black list the entries needed to be remove before inserting into database.
     */
    private ArrayList<String[]> blacklistedEntries;
    
    /**
     * Constructor used by BootstrapValidator to instantiate BootstrapValidationTool
     * @param entries data entries of String[] type, from OpenCSV
     * @param fieldHeaders first line, column names
     * @param errorList list of BootstrapError objects, to facilitate Json output
     * @param blacklistedEntries list of String[] entries that are blacklisted so that they are no overlap validation and removed from the original entries
     */
    BootstrapValidationTool(List<String[]> entries, String[] fieldHeaders, 
            ArrayList<BootstrapError> errorList, ArrayList<String[]> blacklistedEntries) {
        this.entries = entries;
        this.fieldHeaders = fieldHeaders;
        this.errorList = errorList;
        
        this.blacklistedEntries = blacklistedEntries;
    }
    
    
    /*
     * COMMON VALIDATION METHODS FOR ALL FILES!
     */
    //TRIMS ROW AS WELL :)
    /**
     * Check for missing field in the csv rows
     * @param row
     * @param messageList
     * @return validity of the row
     */
    boolean rowHasMissingFields(String[] row, ArrayList<String> messageList) {
        boolean rowHasMissingFields = false;
      
        //iterate each cell. We NEED the cell number to identify the cell's fieldheader (cell means field)
        for(int c = 0; c < fieldHeaders.length; c++) {
             
            String str = row[c];                     
            if (hasMissingField(str)) {
                //since str is empty, replace with error msg!
                messageList.add(fieldHeaders[c] +" " + ErrorUtility.getProperty("missing-field"));
                rowHasMissingFields = true; //when 1 or more cells are missing.
            }
            
                
        }   //end of iterating each cell/field of the row.
        
        
        return rowHasMissingFields;
    }
    
    /**
     * Checks the cell for missing field
     * @param cell
     * @return
     *      - true if cell is empty or null
     *      - false if cell is filled
     */
    boolean hasMissingField(String cell) {
        boolean cellHasMissingField = false;
        
        if (cell == null) {
         cellHasMissingField = true;
        } else {
            cell = cell.trim(); //remove all whitespaces. 
            //Empty string will be returned if string was made up of multiple whitespaces.

            if (cell.isEmpty()) {
                cellHasMissingField = true;
            }
        }
        
        return cellHasMissingField;
    }
    
    
    
    /**
     * Check whether there is duplicated entries in the row
     * @param primaryKeyHeader
     *          - takes in e.g. userid, course
     * @param row
     * @return 
     *          - true, if there is duplicated entry
     */
    boolean rowHasDuplicatePrimaryKey(String primaryKeyHeader, String[] row, int line, ArrayList<String> messageList) {
        boolean rowHasDuplicatePrimaryKey = false;
        
        //identify the column that holds the userId value
        int colPrimaryKey = getColumnNumber(primaryKeyHeader);
        
        String primaryKeyCheck = row[colPrimaryKey];
        
        //count the number of times the userId appears on the "entries"
        int countOccurences = 0;
        for (int rowNum = 0; rowNum < entries.size(); rowNum++) {
            int currentLine = rowNum + 2;
            if (currentLine == line) {
                break;   //break the for-loop to prevent going ahead. Test only up til current row's position
                //therefore, countoccurence will NOT ++ on itself. Only ++ on entries having already occurred.
            }
            
            String[] entriesRow = entries.get(rowNum);
            
            if (primaryKeyCheck.equals(entriesRow[colPrimaryKey])) {
                countOccurences++;
            }
        }
        
        //if countOccurences greater than 1, there are duplicates.
        //countOccurences = 1, no duplicates, because it would match with itself once.
        if (countOccurences > 0) {
            rowHasDuplicatePrimaryKey = true;
            //WHEN INVALID replace cell with error msg!
            messageList.add("duplicate " +primaryKeyHeader);
        }
        //the bottomest row will remain, without values corrupted.
        
        return rowHasDuplicatePrimaryKey;
    }
    
    /**
     * Used to validate duplicates in section.csv, prerequisite.csv, course_completed.csv
     * @param foreignKeyHeader
     * @param primaryKeyHeader
     * @param row
     * @return 
     */
    boolean rowHasDuplicatePrimaryKey(String foreignKeyHeader, String primaryKeyHeader, String[] row,
            ArrayList<String> messageList) {
        boolean rowHasDuplicatePrimaryKey = false;
        
        //identify the column that holds the userId value
        int colForeignKey = getColumnNumber(foreignKeyHeader);
        int colPrimaryKey = getColumnNumber(primaryKeyHeader);
        
        String foreignKeyCheck = row[colForeignKey];
        String primaryKeyCheck = row[colPrimaryKey];
        
        //count the number of times the userId appears on the "entries"
        int countOccurences = 0;
        for (int rowNum = 0; rowNum < entries.size(); rowNum++) {
            String[] entriesRow = entries.get(rowNum);
            
            if (foreignKeyCheck.equals(entriesRow[colForeignKey]) && primaryKeyCheck.equals(entriesRow[colPrimaryKey])) {
                countOccurences++;
            }
        }
        
        //if countOccurences greater than 1, there are duplicates.
        //countOccurences = 1, no duplicates, because it would match with itself once.
        if (countOccurences > 1) {
            rowHasDuplicatePrimaryKey = true;
            //WHEN INVALID replace cell with error msg!
            messageList.add("duplicate" +foreignKeyHeader);
            messageList.add("duplicate " +primaryKeyHeader);
        }
        //the bottomest row will remain, without values corrupted.
        
        return rowHasDuplicatePrimaryKey;
    }
    
    /**
     * Used to validate duplicates in bid.csv
     * @param foreignKeyHeader1
     * @param foreignKeyHeader2
     * @param primaryKeyHeader
     * @param row
     * @return 
     */
    boolean rowHasDuplicatePrimaryKey(String foreignKeyHeader1, String foreignKeyHeader2, String primaryKeyHeader,
            String[] row, ArrayList<String> messageList) {
        boolean rowHasDuplicatePrimaryKey = false;
        
        //identify the column that holds the userId value
        int colForeignKey1 = getColumnNumber(foreignKeyHeader1);
        int colForeignKey2 = getColumnNumber(foreignKeyHeader2);
        int colPrimaryKey = getColumnNumber(primaryKeyHeader);
        
        String foreignKeyCheck1 = row[colForeignKey1];
        String foreignKeyCheck2 = row[colForeignKey2];
        String primaryKeyCheck = row[colPrimaryKey];
        
        //count the number of times the userId appears on the "entries"
        int countOccurences = 0;
        for (int rowNum = 0; rowNum < entries.size(); rowNum++) {
            String[] entriesRow = entries.get(rowNum);
            
            if (foreignKeyCheck1.equals(entriesRow[colForeignKey1]) 
			&& foreignKeyCheck2.equals(entriesRow[colForeignKey2])
			&& primaryKeyCheck.equals(entriesRow[colPrimaryKey])) {
                countOccurences++;
            }
        }
        
        //if countOccurences greater than 1, there are duplicates.
        //countOccurences = 1, no duplicates, because it would match with itself once.
        if (countOccurences > 1) {
            rowHasDuplicatePrimaryKey = true;
            //WHEN INVALID replace cell with error msg!
            messageList.add("duplicate" +foreignKeyHeader1);
            messageList.add("duplicate " +foreignKeyHeader2);
            messageList.add("duplicate " +primaryKeyHeader);
        }
        //the bottomest row will remain, without values corrupted.
        
        return rowHasDuplicatePrimaryKey;
    }
    
    /**
     * Check for time validity
     * @param timeValue
     * @return true if time NOT of valid format
     */
    private boolean hasInvalidTime(String timeValue) {
        Date time = null;
        SimpleDateFormat sdf = new SimpleDateFormat("H:mm");   //Hour in day (0-23)
        sdf.setLenient(false);
        try {
            time = sdf.parse(timeValue);
        } catch (ParseException e) {
            //if val is not a valid representation of a ExamStart.
            return true;
        }
        
        return false;
    }

    /**
     * Check for invalid start time
     * @param columnHeader
     * @param row
     * @param messageList
     * @return true if start time is NOT valid format
     */
    boolean rowHasInvalidStart(String columnHeader, String[] row, ArrayList<String> messageList) {
        boolean rowHasInvalidStart = false;
        
        //identify the column that holds the eDollar value
        int colNumOfStart = getColumnNumber(columnHeader);
        
        String startStr = row[colNumOfStart];
        rowHasInvalidStart = hasInvalidTime(startStr);
        
        if (rowHasInvalidStart) {
            //WHEN INVALID replace cell with error msg!
            messageList.add("invalid " +columnHeader);
        }
        
        return rowHasInvalidStart;
    }
    /**
     * Check whether the row in section csv have invalid time
     * @param columnHeader
     * @param row
     * @param messageList
     * @return true when time is NOT valid format
     */
    boolean rowOfSectionHasInvalidStart(String columnHeader, String[] row, ArrayList<String> messageList) {
        boolean rowHasInvalidStart = false;
        
        //identify the column that holds the eDollar value
        int colNumOfStart = getColumnNumber(columnHeader);
        
        String startStr = row[colNumOfStart];
        rowHasInvalidStart = hasInvalidTime(startStr);
        
        if (!rowHasInvalidStart) {
            if (!"8:30".equals(startStr) && !"12:00".equals(startStr) && !"15:30".equals(startStr)) {
                rowHasInvalidStart = true;
            }
        }
        
        if (rowHasInvalidStart) {
            //WHEN INVALID replace cell with error msg!
            messageList.add("invalid " +columnHeader);
        }
        
        return rowHasInvalidStart;
    }
    
    /**
     * hasInvalidEnd is different from hasInvalidStart because it further test if end is earlier than start is true
     * @param row
     * @return 
     *      - true, when string representation of time does not follow "HH:mm",
     *      - true, when end is before start
     */
    boolean rowHasInvalidEnd(String startColumnHeader, String endColumnHeader, String[] row, ArrayList<String> messageList) {
        boolean rowHasInvalidEnd = false;
        
        //identify the column that holds the eDollar value
        int colNumOfEnd = getColumnNumber(endColumnHeader);
        int colNumOfStart = getColumnNumber(startColumnHeader);
        
        String end = row[colNumOfEnd];
        String start = row[colNumOfStart];
                        
        rowHasInvalidEnd = hasInvalidEnd(end,start);
        
        if (rowHasInvalidEnd) {
            //WHEN INVALID replace cell with error msg!
            messageList.add("invalid " +endColumnHeader);
        }
        
        return rowHasInvalidEnd;
    }
    
    /**
     * hasInvalidEnd is different from hasInvalidStart because it further test if end is earlier than start is true
     * @param row
     * @return 
     *      - true, when string representation of time does not follow "HH:mm",
     *      - true, when end is before start
     */
    boolean rowOfSectionHasInvalidEnd(String startColumnHeader, String endColumnHeader, String[] row, ArrayList<String> messageList) {
        boolean rowHasInvalidEnd = false;
        
        //identify the column that holds the eDollar value
        int colNumOfEnd = getColumnNumber(endColumnHeader);
        int colNumOfStart = getColumnNumber(startColumnHeader);
        
        String end = row[colNumOfEnd];
        String start = row[colNumOfStart];
                        
        rowHasInvalidEnd = hasInvalidEnd(end,start);
        
        if (!rowHasInvalidEnd) {
            if (!"11:45".equals(end) && !"15:15".equals(end) && !"18:45".equals(end)) {
                rowHasInvalidEnd = true;
            }
        }
        
        if (rowHasInvalidEnd) {
            //WHEN INVALID replace cell with error msg!
            messageList.add("invalid " +endColumnHeader);
        }
        
        return rowHasInvalidEnd;
    }
    
    /**
     * Check for invalid end
     * @param end
     * @param start
     * @return true if end time NOT valid format OR happens before start time
     */
    boolean hasInvalidEnd(String end, String start) {
        boolean endEarlierThanStart = false;
        
        SimpleDateFormat sdf = new SimpleDateFormat("H:mm");
        sdf.setLenient(false);
        Date startTime = null;
        try {
            startTime = sdf.parse(start);
        } catch (ParseException e) {
            //catch and ignore. Since if it fails, it would have invalid start already.
        }
        
        try {
            endEarlierThanStart = sdf.parse(end).before(startTime);
        } catch (ParseException e) {
            e.printStackTrace();
            return true;
        } catch (NullPointerException npe) {
            //ignore
        }
        
        return hasInvalidTime(end) || endEarlierThanStart;
    }
    
    /**
     * Used to validate if course code exists in the original list of courses uploaded to the DB.
     * Does a method call to the CourseDAO to query the DB.
     * Used by validateSection,Prerequisites,courseCompleted
     * @param row
     * @return true if course DOES NOT exists in the database
     */
    
    boolean rowHasInvalidCourse(String[] row, ArrayList<String> messageList) {
        boolean rowHasInvalidCourse = false;
        
        //identify the column that holds the course value
        int colNumOfCourse = getColumnNumber("course");
        String headerUsedInCSV = null;
        if (colNumOfCourse == -1) {
            colNumOfCourse = getColumnNumber("code");
            headerUsedInCSV = "code";
        } else if (colNumOfCourse > -1) {
            headerUsedInCSV = "course";
        }
        
        rowHasInvalidCourse = hasInvalidCourse(row[colNumOfCourse]);
           
        if (rowHasInvalidCourse) {
            //WHEN INVALID replace cell with error msg!
            messageList.add("invalid " +headerUsedInCSV);
        }
        
        return rowHasInvalidCourse;
    }    
    
    /**
     * Check whether the course exist
     * @param cell
     * @return true, when the specified course is not found
     */
    boolean hasInvalidCourse(String cell) {
        return CourseDAO.retrieveByCode(cell) == null;
    }
    
    /**
     * Check whether the there is any clash in timetable
     * @param startA
     * @param endA
     * @param startB
     * @param endB
     * @return true, if any of the class time conflicted
     */
    private boolean hasTimeClash(Date startA, Date endA, Date startB, Date endB) {
         return startA.before(endB) && endA.after(startB);  
    }
    
    /*
     * END OF COMMON VALIDATION METHODS
     */
    
    
    /*
     * STUDENT VALIDATIONS ONLY!
     */        
    /**
     * Check whether there is invalid eDollar input in the row
     * @param row
     * @param messageList
     * @return true, if there is illegal input or unknown format for eDollar in the row
     */
    boolean rowHasInvalidEDollar(String[] row, ArrayList<String> messageList) {
        
        //identify the column that holds the eDollar value
        int colNumOfEDollar = getColumnNumber("edollar");
        String headerUsed = "e-dollar";
        
        //execute validation method.
        boolean hasInvalidEDollar = hasInvalidEDollar(row[colNumOfEDollar]);
        boolean rowHasInvalidEDollar = hasInvalidEDollar;
        
        if (hasInvalidEDollar && "e-dollar".equals(headerUsed)) {
            //WHEN INVALID replace cell with error msg!
            messageList.add(ErrorUtility.getProperty("invalid-e-dollar"));
        }
        
        return rowHasInvalidEDollar;
    }
    
    /**
     * Check th validity of eDollar
     * @param cell
     * @return if String is not a in decimal representation or when EDollar is negative.
     */
    boolean hasInvalidEDollar(String cell) {
        BigDecimal eDollar = null;
        
        try {
            eDollar = new BigDecimal(cell);
        } catch (NumberFormatException e) {
            //if val is not a valid representation of a BigDecimal.
            return true;
        }
        
        BigDecimal zeroValue = new BigDecimal("0.00",new MathContext(2));  //initialize a BigDecimal obj for comparison of value
        return eDollar != null && eDollar.compareTo(zeroValue) < 0;
            //executes when eDollar is < 0.00 return true.
    }
    /*
     * END: STUDENT VALIDATIONS ONLY!
     */

    
    /*
     * COURSE VALIDATIONS ONLY!
     */
    /**
     * Check whether the row of data have invalid exam date
     * @param row
     * @param messageList
     * @return true, if invalid exam date is found
     */
    boolean rowHasInvalidExamDate(String[] row, ArrayList<String> messageList) {
        boolean rowHasInvalidExamDate = false;
        
        //identify the column that holds the eDollar value
        int colNumOfExamDate = getColumnNumber("exam date");
        
        rowHasInvalidExamDate = hasInvalidExamDate(row[colNumOfExamDate]);
        
        
        if (rowHasInvalidExamDate) {
            //WHEN INVALID replace cell with error msg!
            messageList.add(ErrorUtility.getProperty("invalid-exam-date"));
        }
       
        return rowHasInvalidExamDate;
    }
    
    /**
     * Check for validity of exam date
     * @param cell
     * @return true, if the date format is not in yyyyMMdd
     */
    boolean hasInvalidExamDate(String cell) {
        Date examDate = null;
        //Exam format is yyyyMMdd
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        sdf.setLenient(false);
        
        try {
            examDate = sdf.parse(cell);
        } catch (ParseException e) {
            //if val is not a valid representation of a ExamDate.
            return true;
        }
        
        return false;
    }
    /**
     * @deprecated
     *      -in favor of hasInvalidEnd(String startColumnHeader, String endColumnHeader, String[] row).
     * @param row
     * @return 
     */
    boolean hasInvalidExamEnd(String[] row) {
        boolean rowHasInvalidExamEnd = false;
        
        //identify the column that holds the eDollar value
        int colNumOfExamEnd = -1;
        
        for (int c = 0; c < fieldHeaders.length; c++) {
            String header = fieldHeaders[c];
            if ("exam end".equals(header)) {
                colNumOfExamEnd = c;
            }
        }
        
        String examEndStr = row[colNumOfExamEnd];
        rowHasInvalidExamEnd = hasInvalidTime(examEndStr);
        
        if (rowHasInvalidExamEnd) {
            //WHEN INVALID replace cell with error msg!
            row[colNumOfExamEnd] = "invalid exam End";
        }
        
        return rowHasInvalidExamEnd;
    }    
    
    /*
     * END: COURSE VALIDATIONS ONLY!
     */    
    
    
    
    /*
     * SECTION VALIDATIONS ONLY!
     */    
    /**
     * Check whether there is invalid section in the row
     * @param row
     * @param messageList
     * @return true, if invalid status if found in the row
     */
    boolean rowHasInvalidSection(String[] row, ArrayList<String> messageList) {
        boolean rowHasInvalidSection = false;
        
        //identify the column that holds the course value
        int colNumOfSection = getColumnNumber("section");
        
        rowHasInvalidSection = hasInvalidSection(row[colNumOfSection]);
        
        if (rowHasInvalidSection) {
            //WHEN INVALID replace cell with error msg!
            messageList.add(ErrorUtility.getProperty("invalid-section"));
        }
        
        return rowHasInvalidSection;
    }
    
    /**
     * Check the validity of the section
     * @param cell
     * @return true, if the section is not in the correct format
     */
    boolean hasInvalidSection(String cell) {
        boolean numberIsGreaterThanZero = false;
        
        try {
            int number = Integer.parseInt(cell.substring(1));
            
            numberIsGreaterThanZero = number > 0;
        } catch (Exception e) {
            
        }
        
        return !cell.matches("S\\d\\d?") || !numberIsGreaterThanZero;
    }
    
    /**
     * Check whether row have invalid day
     * @param row
     * @param messageList
     * @return true, if invalid day is found
     */
    boolean rowHasInvalidDay(String[] row, ArrayList<String> messageList) {
        boolean rowHasInvalidDay = false;
        
        //identify the column that holds the course value
        int colNumOfDay = getColumnNumber("day");
        
        String day = row[colNumOfDay];
        
        rowHasInvalidDay = hasInvalidDay(day);
        
        if (rowHasInvalidDay) {
            //WHEN INVALID replace cell with error msg!
            messageList.add(ErrorUtility.getProperty("invalid-day"));
        }
        
        return rowHasInvalidDay;
    }
    
    /**
     * Check validity of day format
     * @param cell
     * @return true, if the day is not a number between 1-7
     */
    boolean hasInvalidDay(String cell) {
        return !cell.matches("[1-7]");
    }
    
    /**
     * Check whether there is invalid size in the rows
     * @param row
     * @param messageList
     * @return true, if invalid size is found
     */
    boolean rowHasInvalidSize(String[] row, ArrayList<String> messageList) {
        boolean rowHasInvalidSize = false;
        
        //identify the column that holds the course value
        int colNumOfSize = getColumnNumber("size");
        
        String sizeStr = row[colNumOfSize];
        
        rowHasInvalidSize = !sizeStr.matches("\\d+");
        
        if (!rowHasInvalidSize) {
            int size = Integer.parseInt(sizeStr);
            rowHasInvalidSize = size < 1;
        }
        
        if (rowHasInvalidSize) {
            //WHEN INVALID replace cell with error msg!
            messageList.add(ErrorUtility.getProperty("invalid-size"));
        }
        
        return rowHasInvalidSize;
    }
    /*
     * END: SECTION VALIDATIONS ONLY!
     */    
    
    /*
     * START : PREREQUISITE VALIDATIONS
     */
    
    /**
     * Returns true if the prerequisite is NOT a Course existing in the database
     * @param row
     * @param messageList
     * @return true if the prerequisite is NOT a Course existing in the database
     */
    boolean rowHasInvalidPrerequisite(String[] row, ArrayList<String> messageList) {
        boolean rowHasInvalidPrerequisite = false;
        
        //identify the column that holds the course value
        //also identify the col that hold the course value. 
        //We need to make a check if the prerequisite value is the same as course value. Which should NOT be.
        int colNumOfPrerequisite = getColumnNumber("prerequisite");
        int colNumOfCourse = getColumnNumber("course");
        
        String prerequisiteStr = row[colNumOfPrerequisite];
        String courseStr = row[colNumOfCourse];
        
        rowHasInvalidPrerequisite = hasInvalidPrerequisite(prerequisiteStr,courseStr);
                
        if (rowHasInvalidPrerequisite) {
            //WHEN INVALID replace cell with error msg!
            messageList.add(ErrorUtility.getProperty("invalid-prerequisite"));
        }
        
        return rowHasInvalidPrerequisite;
    }
        
    /**
     * Returns true if the prerequisite is NOT a Course existing in the database
     * @param prerequisite
     * @param course
     * @return true if the prerequisite is NOT a Course existing in the database
     */
    boolean hasInvalidPrerequisite(String prerequisite, String course) {
        //if the such prerequisite does not exist && if prereq and course is the same code.
        return CourseDAO.retrieveByCode(prerequisite) == null || prerequisite.equals(course);
    }
    /*
     * END: PREREQUISITE VALIDATIONS ONLY!
     */  
        
        
    /*
     * START : COURSECOMPLETED VALIDATIONS
     */
    
    /**
     * Return true if user does not exist in database
     * @param row
     * @param messageList
     * @return true if user does not exist in database
     */
    boolean rowHasInvalidUserId(String[] row, ArrayList<String> messageList) {
        boolean rowHasInvalidUserId = false;

        //identify the column that holds the course value
        int colNumOfUserId = getColumnNumber("userid");

        String userId = row[colNumOfUserId];
        
        rowHasInvalidUserId = hasInvalidUserId(userId);
 
        if (rowHasInvalidUserId) {
            //WHEN INVALID replace cell with error msg!
            messageList.add(ErrorUtility.getProperty("invalid-userid"));
        }

        return rowHasInvalidUserId;
    }
    
    /**
     * Returns true if user does not exist in database
     * @param cell
     * @return true if user does not exist in database
     */
    boolean hasInvalidUserId(String cell) {
        return StudentDAO.retrieveById(cell) == null;
    }
    
    /**
     * Identify the userid OF THIS ROW, and store all courses (in takenList)taken by student in course_completed.csv
     * Take the code OF THIS ROW, and identify its associated prerequisites (prereqList)
     * Match the two lists. Matches must add up to the total num of prereqs.
     * 
     * @param row
     * @return 
     */
    boolean rowHasInvalidCourseCompleted(String[] row, ArrayList<String> messageList){
        boolean rowHasInvalidCourseCompleted = false;
            
        int colNumOfUserId = getColumnNumber("userid");
        int colNumOfCode = getColumnNumber("code");

        String userId = row[colNumOfUserId];
        String courseCompletedStr = row[colNumOfCode];
        
        //Identify prereqs associated to this course.
        ArrayList<String> prerequisites = CourseDAO.retrievePrerequisites(courseCompletedStr);
        ArrayList<String> takenCourses  = new ArrayList<String>();
        
        //Isolate the rows from the "entries" with only this userid,
        //add all the courses taken under this userid into a takenCourses list.
        for (String[] rowFromEntries : entries) {
            if (rowFromEntries[colNumOfUserId].equals(userId)) {
                takenCourses.add(rowFromEntries[colNumOfCode]);
            }
        }
        
        //retain only those courses in takenCourses collection where it matches the prerequisites collection.
        takenCourses.retainAll(prerequisites);
        if (takenCourses.size() < prerequisites.size()) {
            //2nd condition is false (shortcircuit: when 1st condition is true)
            rowHasInvalidCourseCompleted = true;
        }
        

        if (rowHasInvalidCourseCompleted) {
            //WHEN INVALID replace cell with error msg!
            messageList.add(ErrorUtility.getProperty("invalid-course-completed"));
        }

       return rowHasInvalidCourseCompleted;
    }
    /*
     * END: COURSECOMPLETED VALIDATIONS
     */  
    
    
    /*
     * BID VALIDATIONS ONLY!
     */ 
    
    /**
     * Return true if amount is NOT valid format, NOT greater than $10, NOT at most 2 decimal place
     * @param row
     * @param messageList
     * @return true if amount is NOT valid format, NOT greater than $10, NOT at most 2 decimal place
     */
    boolean rowHasInvalidAmount(String[] row, ArrayList<String> messageList) {
        boolean rowHasInvalidAmount = false;

        //identify the column that holds the course value
        int colNumOfAmount = getColumnNumber("amount");
        
        String amountStr = row[colNumOfAmount];
        
        rowHasInvalidAmount = hasInvalidEDollar(amountStr) ||  hasInvalidMinimumBid(amountStr)
                || hasInvalidDecimalPlace(amountStr);
        
        if (rowHasInvalidAmount) {
            messageList.add(ErrorUtility.getProperty("invalid-amount"));
        }

        return rowHasInvalidAmount;
    }
    
    /**
     * Return true if amount not valid 2 decimal place
     * @param cell
     * @return true if amount not valid 2 decimal place
     */
    boolean hasInvalidDecimalPlace(String cell) {
        BigDecimal testObj = new BigDecimal(cell);
        int scale = testObj.scale();
        
        if (scale > 2) {
            return true;
        }
        return false;
    }
    
    /**
     * return true if amount not at least $10
     * @param cell
     * @return true if amount not at least $10
     */
    boolean hasInvalidMinimumBid(String cell) {
        BigDecimal minimumAmount = new BigDecimal("10.00");
        
        BigDecimal amount = new BigDecimal(cell);
        
        return amount.compareTo(minimumAmount) < 0 ;
    }
    
    
    /**
     * Test if section does not exist in the DB
     * @param row
     * @return 
     *      - true, if section, with corresponding course, does not exist in the section table of the DB
     *      - otherwise false
     */
    boolean rowHasInvalidSectionExistence(String[] row, ArrayList<String> messageList){
        boolean rowHasInvalidSectionExistence = false;
        
        //identify the column that holds the course value
        int colNumOfSection = getColumnNumber("section");
        int colNumOfCourse = getColumnNumber("code");
        
        String course = row[colNumOfCourse];
        String section = row[colNumOfSection];
        
        rowHasInvalidSectionExistence = hasInvalidSectionExistence(course, section);
        
        if (rowHasInvalidSectionExistence) {
            messageList.add(ErrorUtility.getProperty("invalid-section"));
        }
        return rowHasInvalidSectionExistence;
    }
    
    /**
     * Returns true if Section does not exist in database
     * @param code
     * @param section
     * @return true if Section does not exist in database
     */
    boolean hasInvalidSectionExistence(String code, String section) {
        //check if the section exist in DB
        return SectionDAO.retrieveSection(code,section) == null;
    }
    
    /**
     * Return true if this is an UPDATE of PREVIOUS BID
     * @param row
     * @param line
     * @param alreadyBiddedList
     * @return true if this is an UPDATE of PREVIOUS BID
     */
    boolean rowHasAlreadyBiddedForCourse(String[] row, int line, ArrayList<String[]> alreadyBiddedList) {
        boolean rowHasAlreadyBiddedForCourse = false;
        
        //identify the column that holds the userId value
        int colUserId = getColumnNumber("userid");
        int colCourse = getColumnNumber("code");
        int colSection = getColumnNumber("section");
        
        String userId = row[colUserId];
        String course = row[colCourse];
        String section = row[colSection];   //this is the section of comparison
        
        ArrayList<String[]> userBidsSameCourse = new ArrayList<String[]>();   //to account for cancellations/duplicate bid (same userid, course, section)
        
        int currentLine = 1;
        int previousBidLine = 0;
        toNextRow:
        for (String[] entriesRow : entries) {
            currentLine++;  //++ immediately, so first row is line 2.
            if (currentLine == line) {
                break;   //break the for-loop to prevent going ahead. Test only up til current row's position
                //therefore, countoccurence will NOT ++ on itself. Only ++ on entries having already occurred.
            }
            
            if (blacklistedEntries.contains(entriesRow)) {
                continue toNextRow;
            }
            
            if (userId.equals(entriesRow[colUserId]) && course.equals(entriesRow[colCourse])) {
                userBidsSameCourse.add(entriesRow);
                previousBidLine = currentLine;  //will constantly be replaced with the last occuring
            }
        }
        
        //if userBidsSameCourse > 0. Repeats even at least once.   
        if (userBidsSameCourse.size() > 0) {
            rowHasAlreadyBiddedForCourse = true;
            
            //add back previous amount.
            String[] previousBid = entries.get(previousBidLine - 2);
            alreadyBiddedList.add(previousBid); //STORE THESE UPDATED BID
            
            int colAmount = getColumnNumber("amount");
            BigDecimal amount = new BigDecimal(previousBid[colAmount]);
            
            BigDecimal balance = null;
            Student student = StudentDAO.retrieveById(userId);
            if (student != null) {
                balance = student.getEDollars().add(amount);
            }
            //UPDATE DB ONLY AT THE END TO REDUCE NUMBER OF CONNECTIONS.
            StudentDAO.updateEDollars(userId, balance);
            
        } 
        //the bottomest row will remain, without values corrupted.
        
        return rowHasAlreadyBiddedForCourse;
    }
    
    /**
     * Return true if there is a clash in timetable
     * @param row
     * @param line
     * @param messageList
     * @return true if there is a clash in timetable
     */
    boolean rowHasBidClassClash(String[] row, int line, ArrayList<String> messageList) {
        boolean rowHasBidClassClash = false;
        
        //identify the column that holds the values
        int colNumOfSection = getColumnNumber("section");
        int colNumOfCourse = getColumnNumber("code");
        int colNumOfUserId = getColumnNumber("userid");
        
        String userid = row[colNumOfUserId];
        String courseStr = row[colNumOfCourse];
        String sectionStr = row[colNumOfSection];
        
        //retrieve the section (in the row) to be validated for clashes
        Section sectionToBeValidated = SectionDAO.retrieveSection(courseStr,sectionStr);
        java.util.Date start = sectionToBeValidated.getStart();
        java.util.Date end = sectionToBeValidated.getEnd();
        int day = sectionToBeValidated.getDay();
        
        //Identify all of the users bids from the bidEntries
        ArrayList<String[]> userBids = new ArrayList<String[]>();
        
        int currentLine = 1;
        toNextRow:
        for (String[] entriesRow : entries) {
            currentLine++;  //++ immediately, so first row is line 2.
            if (currentLine == line) {
                break;   //break the for-loop to prevent going ahead. Test only up til current row's position
                //therefore, countoccurence will NOT ++ on itself. Only ++ on entries having already occurred.
            }
            
            if (blacklistedEntries.contains(entriesRow)) {
                continue toNextRow;
            }
            
            if (courseStr.equals(entriesRow[colNumOfCourse])) {
                continue toNextRow;
            }
            
            if (userid.equals(entriesRow[colNumOfUserId])) {
                userBids.add(entriesRow);
            }
        }
        
        //check for time clashes
        //MAKE SURE YOU DON"T CHECK WITH ITSELF!!!! LIKE THAT CONFIRM CLASH
        for (String[] bid: userBids) {
            Section sectionCheck = SectionDAO.retrieveSection(bid[colNumOfCourse],bid[colNumOfSection]);
            java.util.Date sectionCheckStart = sectionCheck.getStart();
            java.util.Date sectionCheckEnd = sectionCheck.getEnd();
            int sectionCheckDay = sectionCheck.getDay();
            
            if (!sectionToBeValidated.equals(sectionCheck)) {
                rowHasBidClassClash = day == sectionCheckDay &&
                        hasTimeClash(start,end,sectionCheckStart,sectionCheckEnd);
            }
            
            if (rowHasBidClassClash) {
                //break loop if true.
                break;
            }
        }
        
        if (rowHasBidClassClash) {
            messageList.add(ErrorUtility.getProperty("class-timetable-clash"));
        }
        
        
        return rowHasBidClassClash;
    }
    
    /**
     * Return true when row has exam timetable clash
     * @param row
     * @param line
     * @param messageList
     * @return true when row has exam timetable clash
     */
    boolean rowHasBidExamClash(String[] row, int line, ArrayList<String> messageList) {
        boolean rowHasBidExamClash = false;
        
        //identify the column that holds the values
        int colNumOfCourse = getColumnNumber("code");
        int colNumOfUserId = getColumnNumber("userid");
        
        String userid = row[colNumOfUserId];
        String courseStr = row[colNumOfCourse];
        
        //retrieve the course (in the row) to be validated for clashes
        Course courseToBeValidated = CourseDAO.retrieveByCode(courseStr);
        java.util.Date examDate = courseToBeValidated.getExamDate();
        java.util.Date examStart = courseToBeValidated.getExamStart();
        java.util.Date examEnd = courseToBeValidated.getExamEnd();
        
        //Identify all of the users bids from the bidEntries
        ArrayList<String[]> userBids = new ArrayList<String[]>();
        
        int currentLine = 1;
        toNextRow:
        for (String[] entriesRow : entries) {
            currentLine++;  //++ immediately, so first row is line 2.
            if (currentLine == line) {
                break;   //break the for-loop to prevent going ahead. Test only up til current row's position
                //therefore, countoccurence will NOT ++ on itself. Only ++ on entries having already occurred.
            }
            
            if (blacklistedEntries.contains(entriesRow)) {
                continue toNextRow;
            }
            
            if (userid.equals(entriesRow[colNumOfUserId])) {
                userBids.add(entriesRow);
            }
        }
        
        //check for time clashes
        //MAKE SURE YOU DON"T CHECK WITH ITSELF!!!! LIKE THAT CONFIRM CLASH
        for (String[] bid: userBids) {
            Course courseCheck = CourseDAO.retrieveByCode(bid[colNumOfCourse]);
            
            java.util.Date courseCheckExamDate = courseCheck.getExamDate();
            java.util.Date courseCheckExamStart = courseCheck.getExamStart();
            java.util.Date courseCheckExamEnd = courseCheck.getExamEnd();
            
            if (!courseToBeValidated.equals(courseCheck)) {
                rowHasBidExamClash = examDate.equals(courseCheckExamDate) && hasTimeClash(examStart,examEnd,courseCheckExamStart,courseCheckExamEnd);
            }
            
            
            if (rowHasBidExamClash) {
                break;
            }
        }
        
        if (rowHasBidExamClash) {
            messageList.add(ErrorUtility.getProperty("exam-timetable-clash"));
        }
        
        return rowHasBidExamClash;
    }
    //---------------------------------------
    
    /**
     * Return true if the user of the bid has not completed prerequisites
     * @param row
     * @param messageList
     * @return true if the user of the bid has not completed prerequisites
     */
    boolean rowHasIncompletePrerequisites(String[] row, ArrayList<String> messageList) {
        boolean rowHasIncompletePrerequisites = false;
        
        //identify the column that holds the course value
        int colNumOfCourse = getColumnNumber("code");
        int colNumOfUserId = getColumnNumber("userid");

        
        //check if all prerequisites are completed
        ArrayList<String> courseCompleted = StudentDAO.retrieveCourseCompleted(row[colNumOfUserId]);
        ArrayList<String> prerequisites = CourseDAO.retrievePrerequisites(row[colNumOfCourse]);
        
        int verifications = 0;
        for(String prereq : prerequisites){
            for(String completed : courseCompleted){
                if(completed.equals(prereq)){
                    verifications++;
                }
            }
        }
        
        if(verifications < prerequisites.size()){
            rowHasIncompletePrerequisites = true;
             messageList.add(ErrorUtility.getProperty("incomplete-prerequisites"));
        }
        
        return rowHasIncompletePrerequisites;
    }
    
    /**
     * Returns true if user has completed the course already
     * @param row
     * @param messageList
     * @return true if user has completed the course already
     */
    boolean rowHasAlreadyCompletedCourse(String[] row, ArrayList<String> messageList) {
        boolean rowHasAlreadyCompletedCourse = false;
        
        //identify the column that holds the course value
        int colNumOfCourse = getColumnNumber("code");
        int colNumOfUserId = getColumnNumber("userid");
        
        //check if all prerequisites are completed
        ArrayList<String> courseCompleted = StudentDAO.retrieveCourseCompleted(row[colNumOfUserId]);
        
        //course to be validated
        String courseStr = row[colNumOfCourse];
        
        if(courseCompleted.contains(courseStr)){
            rowHasAlreadyCompletedCourse = true;
            messageList.add(ErrorUtility.getProperty("already-completed"));
        }
        
        return rowHasAlreadyCompletedCourse;
    }
    
    /**
     * Returns true if user has already 5 bids
     * @param row
     * @param line
     * @param messageList
     * @return true if user has already 5 bids
     */
    boolean rowHasSectionLimitReached(String[] row, int line, ArrayList<String> messageList) {
        boolean rowHasSectionLimitReached = false;
        
        //identify the column that holds the userId value
        int colNumOfUserId = getColumnNumber("userid");
        int colNumOfCode = getColumnNumber("code");
        
        String userid = row[colNumOfUserId];
        String code = row[colNumOfCode];
        
        //identify all of user bids
        ArrayList<String[]> userBids = new ArrayList<String[]>();
        
        int currentLine = 1;
        boolean toMinusOne = false;
        toNextRow:
        for (String[] entriesRow : entries) {
            currentLine++;  //++ immediately, so first row is line 2.
            if (currentLine == line) {                
                break;   //break the for-loop to prevent going ahead. Test only up til current row's position
                //therefore, countoccurence will NOT ++ on itself. Only ++ on entries having already occurred.
            }
            
            if (blacklistedEntries.contains(entriesRow)) {
                continue toNextRow;
            }
            
            //add only if its not equal to the same course code. (same codes are simply bid updates)
            if (userid.equals(entriesRow[colNumOfUserId]) && !code.equals(entriesRow[colNumOfCode])) {
                userBids.add(entriesRow);
            }
            
            //must check the current line also with all the previous lines. 
            //Since it breaks before reaching itself, it will not check with itself!
            if (userid.equals(entriesRow[colNumOfUserId]) && code.equals(entriesRow[colNumOfCode])) {
                toMinusOne = true;
            }
                
        }
        
        
        /*
        //identify all of user bids OF THE SAME COURSE
        ArrayList<String[]> userBidsSameCourse = new ArrayList<>();
        ArrayList<String> courseCodesThatRepeat = new ArrayList<>();
        for (String[] userCheckRow : userBids) {
            int colCourse = getColumnNumber("code");
            String checkCourse = userCheckRow[colCourse];
            
            int countOccurences = 0;
            
            for (String[] userRow : userBids) {

                if (checkCourse.equals(userRow[colCourse])) {
                    countOccurences++;
                }
            }
        
            //if countOccurences greater than 1, there are duplicates.
            //countOccurences = 1, no duplicates, because it would match with itself once.
            if (countOccurences > 1) {
                userBidsSameCourse.add(userCheckRow);
                
                //if there different courses with repeats. Add only once when unique.
                if ( !courseCodesThatRepeat.contains(checkCourse) ) {
                    courseCodesThatRepeat.add(checkCourse);
                }
            }
            
        }
        
        
        userBids.removeAll(userBidsSameCourse);
        
        
        if(userBids.size() + courseCodesThatRepeat.size() > 5 ){
        */
        
        int size = userBids.size();
        if (toMinusOne) {
            size--;
        }
        
        //the current row is excluded from the size. therefore, if at the current row, there is ALREADY 5, limit has reached.
        if (size >= 5) {
            rowHasSectionLimitReached = true;
            messageList.add(ErrorUtility.getProperty("section-limit-reached"));
        }
        
        return rowHasSectionLimitReached;
    }
    
    /**
     * Returns true if user has not enough e$ to process the bid
     * @param row
     * @param messageList
     * @param line
     * @return true if user has not enough e$ to process the bid
     */
    boolean rowHasNotEnoughEDollars(String[] row, ArrayList<String> messageList, int line) {
        boolean rowHasNotEnoughEDollars = false;
        
        //identify the column that holds the userId value
        int colNumOfUserId = getColumnNumber("userid");
        int colNumOfAmount = getColumnNumber("amount");
        int colNumOfCourse = getColumnNumber("code");
               
        String userid = row[colNumOfUserId];
        String course = row[colNumOfCourse];
        
        BigDecimal userEDollars = StudentDAO.retrieveById(userid).getEDollars();
        
        ArrayList<String[]> userBidsSameCourse = new ArrayList<String[]>();   //to account for cancellations/duplicate bid (same userid, course, section)
        
        int currentLine = 1;
        int previousBidLine = 0;
        toNextRow:
        for (String[] entriesRow : entries) {
            currentLine++;  //++ immediately, so first row is line 2.
            if (currentLine == line) {
                break;   //break the for-loop to prevent going ahead. Test only up til current row's position
                //therefore, countoccurence will NOT ++ on itself. Only ++ on entries having already occurred.
            }
            
            if (blacklistedEntries.contains(entriesRow)) {
                continue toNextRow;
            }
            
            if (userid.equals(entriesRow[colNumOfUserId]) && course.equals(entriesRow[colNumOfCourse])) {
                userBidsSameCourse.add(entriesRow);
                previousBidLine = currentLine;  //will constantly be replaced with the last occuring
            }
        }
        
        //if userBidsSameCourse > 0. Repeats even at least once.   
        if (userBidsSameCourse.size() > 0) {
            //rowHasAlreadyBiddedForCourse = true;
            
            //THIS IS AN UPDATE BID
            
            //add back previous amount.
            String[] previousBid = entries.get(previousBidLine - 2);
            
            int colAmount = getColumnNumber("amount");
            BigDecimal previousAmount = new BigDecimal(previousBid[colAmount]);
            
            userEDollars = userEDollars.add(previousAmount);
            
            
        }
        
        
        
        BigDecimal amount = new BigDecimal(row[colNumOfAmount]);
        BigDecimal difference = userEDollars.subtract(amount);
        
        //BigDecimal totalAmount = new BigDecimal("0.00");
        
        /*
        //SUM ALL AMOUNTS (DUPLICATED ALREADY REMOVED)
        toNextRow:
        for (String[] entriesRow : entries) {
            
            if (blacklistedEntries.contains(entriesRow)) {
                continue toNextRow;
            }
            
            if (userid.equals(entriesRow[colNumOfUserId])) {
                totalAmount = totalAmount.add(new BigDecimal(entriesRow[colNumOfAmount]));
            }
        }
        
        //account for the DUPLICATED BIDS (identified by same section of same course)
        ArrayList<String[]> userBidsSameSection = new ArrayList<String[]>();
        toNextRow:
        for (String[] entriesRow : entries) {
            
            if (blacklistedEntries.contains(entriesRow)) {
                continue toNextRow;
            }
            
            if (userid.equals(entriesRow[colNumOfUserId]) && course.equals(entriesRow[colNumOfCourse])) { //no need to test for section since different sections are removed in earlier validations
                userBidsSameSection.add(entriesRow);
            }
        }
        
        if (userBidsSameSection.size() > 1) {
            //deduct the duplicated bids from total amount
            // -1 because the last one is the most updated.
            for (int bidNum = 0; bidNum < userBidsSameSection.size() - 1; bidNum++) {
                totalAmount.subtract(new BigDecimal(userBidsSameSection.get(bidNum)[colNumOfAmount]));
            }
        }
        */
        
        if(difference.compareTo(new BigDecimal("0.00")) < 0){
            rowHasNotEnoughEDollars = true;
            if (messageList != null) {
                messageList.add(ErrorUtility.getProperty("not-enough-e-dollar"));
            }
        }
        
        return rowHasNotEnoughEDollars;
    }
    
    /**
     * Returns true if user is bidding for a course from a different school (round 1 bid logic)
     * @param row
     * @param messageList
     * @return true if user is bidding for a course from a different school (round 1 bid logic)
     */
    boolean rowHasNotOwnSchoolCourse(String[] row, ArrayList<String> messageList) {
        boolean rowHasNotOwnSchoolCourse = false;
        
        //identify the column that holds the values
        int colNumOfCourse = getColumnNumber("code");
        int colNumOfUserId = getColumnNumber("userid");
        
        String userid = row[colNumOfUserId];
        String courseStr = row[colNumOfCourse];
        
        //retrieve the course (in the row) to be validated for clashes
        Course courseToBeValidated = CourseDAO.retrieveByCode(courseStr);
        Student studentToBeValidated = StudentDAO.retrieveById(userid);
        
        if( !studentToBeValidated.getSchool().equals(courseToBeValidated.getSchool()) ){
            rowHasNotOwnSchoolCourse = true;
            messageList.add(ErrorUtility.getProperty("not-own-school-course"));
        }
        
        return rowHasNotOwnSchoolCourse;
    }
    /*
     * END: BID VALIDATIONS ONLY!
     */ 
    
    
    /**
     * 
     * @param header
     * @param fieldHeaders
     * @return 
     *      - columnNumber that represents the order dictated in the CSV file
     *      - value of -1, if no such header is found within the row of "column names"/"field headers"
     */
    public int getColumnNumber(String header) {
        //----------------------------------------
        //Get fieldHeaders and identify their corresponding column numbers.
        //(the order in which fieldHeaders are arranged in the String[] represent the order in the CSV file.
                
        int headerColNum = -1;
                
        for (int c = 0; c < fieldHeaders.length; c++) {
            String headerCheck = fieldHeaders[c];
            if (header.equals(headerCheck)) {
                headerColNum = c;
            }
        }
        
        return headerColNum;
    }
}
