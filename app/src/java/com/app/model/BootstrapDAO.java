
package com.app.model;

import au.com.bytecode.opencsv.CSVReader;
import com.app.json.BootstrapError;
import com.app.utility.ConnectionManager;
import com.app.utility.PropertiesUtility;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import com.app.utility.ScriptRunner;
import com.app.utility.validation.BootstrapValidator;
import java.io.BufferedReader;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
 
/**
 * Important DAO to manage connections and access to bootstrap csv data into application database
 * @author Zachery
 */
public class BootstrapDAO {
    
    private static ArrayList<BootstrapError> errorList;
    
    private static ArrayList<String[]> blacklistedEntries;
    private static List<String[]> entries;
    private static String[] fieldHeaders;
    
    private static ArrayList<String[]> alreadyBiddedList;

    /**
     * Retrieve all the error messages from the current csv that being process
     * @return an ArrayList<BootstrapError> of the current CSV file being loaded/staged into the DAO
     */
    public static ArrayList<BootstrapError> getErrorList() {
        return errorList;
    }
    /**
     * Retrieve the list of invalid csv rows from the current csv that being process
     * @return an ArrayList<BootstrapError> of the current CSV file being loaded/staged into the DAO
     */ 
    public static ArrayList<String[]> getBlacklistedEntries() {
        return blacklistedEntries;
    }
    /**
     * Retrieve list of entries that is valid from th csv
     * @return an entries of the current CSV file being loaded/staged into the DAO
     */ 
    public static List<String[]> getEntries() {
        return entries;
    }
    /**
     * Retrieve the header of the csv file
     * @return an fieldHeaders of the current CSV file being loaded/staged into the DAO
     */ 
    public static String[] getFieldHeaders() {
        return fieldHeaders;
    }
    
    public static void initializeRoot(String realPath) {
        PropertiesUtility.initializeRoot(realPath);
    }
    
    /**
     * Upload file to the specific location
     * @param request
     * @throws Exception
     */
    public static void upload(HttpServletRequest request) throws Exception{
        
        String uploadDirPath = null;
        
        //check if encoding is multipart. 
        boolean isMultipart = ServletFileUpload.isMultipartContent(request);
        
        if(isMultipart){
            
            //temp directory where the incoming zip file will be store
            uploadDirPath = PropertiesUtility.getPathProperty("upload-to.dir");
            File uploadDir = new File(uploadDirPath);
            
            
            //create a factory for adisk-based file items
            DiskFileItemFactory factory = new DiskFileItemFactory();
            factory.setRepository(uploadDir);
            
            //create a new file upload handler
            ServletFileUpload upload = new ServletFileUpload(factory);
            
            List<FileItem> fileItems = null;
            
            try{
                
                fileItems = upload.parseRequest(request);
                
            }catch(FileUploadException fue){
                throw fue;
            }
            
            //run through the items. if it is a file, download it to temp folder
            if(fileItems != null){
                for(FileItem item: fileItems){
                    
                    //if item is not a simple form input
                    if(!item.isFormField()){
                        
                        String fileName = item.getName();
                        
                        uploadDirPath = uploadDirPath + "/data.zip";
                        
                        try{
                            item.write(new File(uploadDirPath));
                        }catch(Exception e){
                            throw e;
                        }
                    }
                }
            }
        }
    }
    
    
    /**
     * Get the location path for the zip file that has been uploaded
     * @return path to the zip file location
     */
    public static String getZipFileLocation(){
        
        return PropertiesUtility.getPathProperty("upload-to.dir") +"/data.zip";
        
    }

    /**
     * Execute the command to extract all file from zip file and put the data in specified location
     * @param from
     * @param to
     * @throws ZipException
     */
    public static void extractAllFiles(String from, String to) throws ZipException {
        try{
            //Initialize ZipFile object with the path/name of the zip file.
            ZipFile zipFile = new ZipFile(from);
            
            //Extract all files to the path specifies
            zipFile.extractAll(to);
            
        }catch(ZipException e){
            throw e;
        }
    }
    
    /**
     * Retrieve the location of data that being extracted from the zip file
     * @return path to the data file
     */
    public static String getDataDirectory(){
        return PropertiesUtility.getPathProperty("unzip-to.dir");
    }
    
    /**
     * Retrieve the list of name for the data
     * @return file name of the data
     */
    public static String[] getDataFileHeaders(){
        File dataDirectory = new File(getDataDirectory());
        
        //this only returns the file names. NOT the paths.
        return dataDirectory.list();
    }
    
    /**
     * Execute SQL query to create table
     * @throws SQLException
     */
    public static void createTables() throws SQLException{
        
        Connection conn = ConnectionManager.getConnection();
        
        //(Connection, autocommit, stoponerror)
        ScriptRunner runner = new ScriptRunner(conn, true, true);
        
        //retrieve the path to the create table script
        String sqlScript = PropertiesUtility.getPathProperty("create-tables.sql");
        
        try{
            runner.runScript(new BufferedReader(new FileReader(sqlScript)));
        }catch(FileNotFoundException e){
            //handle exception with logger
        }catch(IOException e){
            //handle IOException thrown by ScriptRunner
        } finally {
            ConnectionManager.close(conn,null,null);
        }
        
        
    }
    
    /**
     * Takes in the fileHeader(filename.extension) of the csv file, and parses it into the environment
     * @param fileHeader
     * @return List<String[]>
     *          - a List of String[], with each String[] representing a line of the file.
     * 
     */
    public static List<String[]> read(String fileHeader){
        String filePath = getDataDirectory() +"/" +fileHeader;
        
        List<String[]> myEntries = null;
        try {    
            CSVReader reader = new CSVReader(new FileReader(filePath));
            myEntries = reader.readAll();
            
        }catch(FileNotFoundException ex){
            Logger.getLogger(BootstrapDAO.class.getName()).log(Level.SEVERE, null, ex);
        }catch (IOException ex) {
            Logger.getLogger(BootstrapDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return myEntries;
    }
    
    /**
     * To reset the static "staging area" before the start of each validation
     * Used by BootstrapServlet to clear all data AFTER bootstrapping is done.
     */
    public static void resetStage() {
        errorList = null;
        blacklistedEntries = null;
        entries = null;
        fieldHeaders = null;
    }
    
    /**
     * 
     * @param whichCSV
     *      - to indicate which CSV file you are validating
     *      - e.g. student.csv, course.csv, section.csv, etc.
     * @param entries 
     *      - the List<String[]> returned by the read()
     */
    public static void validate(String whichCSV, List<String[]> someEntries) {
        
        //initialize "Staging area" the List attributes in BootstrapDAO
        errorList = new ArrayList<BootstrapError>();      
        blacklistedEntries = new ArrayList<String[]>();
        entries = someEntries;
        
        //trim! and STORE
        for (String[] row : entries) {
            for (int i = 0; i < row.length; i++) {
                row[i] = row[i].trim();
            }
        }
        
        fieldHeaders = entries.remove(0);  //row 0 is the headers!
        //this causes the first row (index 0) to become the data.
        //we remove it so that we do not need to validate the first row.
        
        //trim fieldHeaders
        for(int c = 0; c < fieldHeaders.length; c++) {
            fieldHeaders[c] = fieldHeaders[c].trim();   //trim and store it BACK into same location.
        }
        
        
        //Create validator and validate
        BootstrapValidator validator = new BootstrapValidator();
        
        switch (whichCSV) {
            case "student.csv":
                validator.validateStudent();
                break;
            case "course.csv":
                validator.validateCourse();
                break;
            case "section.csv":
                validator.validateSection();
                break;
            case "prerequisite.csv":
                validator.validatePrerequisites();
                break;
            case "course_completed.csv":
                validator.validateCourseCompleted();
                break;
            case "bid.csv":
                validator.validateBid();
                alreadyBiddedList = validator.getAlreadyBiddedList();
                break;
            default:
                break;
        }
        
        //remove from original entries all of the error rows.
        entries.removeAll(blacklistedEntries);
        
    }
    /**
     * validate and insert Students data from csv files to database.
     *  
     */
    public static void insertStudents() {  
        
        //----------------------------------------
        //Get fieldHeaders, and identify their corresponding column numbers.
        int useridColNum = getColumnNumber("userid",fieldHeaders);
        int passwordColNum = getColumnNumber("password",fieldHeaders);
        int nameColNum = getColumnNumber("name",fieldHeaders);
        int schoolColNum = getColumnNumber("school",fieldHeaders);
        int edollarColNum = getColumnNumber("edollar",fieldHeaders);
                
        
        //-----------------------
        //SET UP MySQL overheads
        Connection conn = ConnectionManager.getConnection();
        PreparedStatement pstmt = null;
        try {
            pstmt = conn
                    .prepareStatement("INSERT INTO student(user_id,password,name,school,edollar) VALUES (?,?,?,?,?)");        
        } catch (SQLException e) {
            //if fail access database.
        }
        
        //---------------------------
        //BATCH AND EXECUTE INSERT
        int batchSize = 0;
        
        for (String[] row : entries) {    
            
            try {
                pstmt.setString(1,row[useridColNum]);
                pstmt.setString(2,row[passwordColNum]);
                pstmt.setString(3,row[nameColNum]);
                pstmt.setString(4,row[schoolColNum]);
                pstmt.setBigDecimal(5,new BigDecimal(row[edollarColNum]));
                    
                pstmt.addBatch();
                
                if (++batchSize == 1000) {
                    pstmt.executeBatch();
                    batchSize = 0;
                }
            } catch (SQLException e) {
                //if set"Type" does not match the order and type indicated in the preparedStatement.
            }
        }
        
        //execute batch
        try {
            pstmt.executeBatch();
            //insert remaining records
        } catch (SQLException e) {
            //If error, or timeout(too many commands) 
        } finally {
            ConnectionManager.close(conn,pstmt,null);
        }
        
    }   //end of validateAndInsertStudents()
    
    /**
     * validate and insert courses to database
     *  
     */
    public static void insertCourses() {
        
        //----------------------------------------
        //Get fieldHeaders, and identify their corresponding column numbers.
        int courseColNum = getColumnNumber("course",fieldHeaders);
        int schoolColNum = getColumnNumber("school",fieldHeaders);
        int titleColNum = getColumnNumber("title",fieldHeaders);
        int descriptionColNum = getColumnNumber("description",fieldHeaders);
        int examDateColNum = getColumnNumber("exam date",fieldHeaders);
        int examStartColNum = getColumnNumber("exam start",fieldHeaders);
        int examEndColNum = getColumnNumber("exam end",fieldHeaders);
        
        //------------------------------
        //SET UP MySQL overheads
        Connection conn = ConnectionManager.getConnection();
        PreparedStatement pstmt = null;
        try {
            pstmt = conn
                    .prepareStatement("INSERT INTO course(course,school,title,description,exam_date,exam_start,exam_end) VALUES (?,?,?,?,?,?,?)");        
        } catch (SQLException e) {
            //if fail access database.
            System.out.println(e.getMessage());
        }

  
        //---------------------------
        //BATCH AND EXECUTE INSERT
        int batchSize = 0;
        
        SimpleDateFormat examDateFormat = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat examTimeFormat = new SimpleDateFormat("H:mm");
        
        for (String[] row : entries) {    
            
            try {
                pstmt.setString(1,row[courseColNum]);
                pstmt.setString(2,row[schoolColNum]);
                pstmt.setString(3,row[titleColNum]);
                pstmt.setString(4,row[descriptionColNum]);
                
                //convert from String to java.util.Date, getTime() and convert to java.sql.Date
                java.sql.Date examDate = new java.sql.Date(examDateFormat.parse(row[examDateColNum]).getTime());
                java.sql.Time examStart = new java.sql.Time(examTimeFormat.parse(row[examStartColNum]).getTime());
                java.sql.Time examEnd =  new java.sql.Time(examTimeFormat.parse(row[examEndColNum]).getTime());

                pstmt.setDate(5,examDate);
                pstmt.setTime(6,examStart);
                pstmt.setTime(7,examEnd);
                
                pstmt.addBatch();
                
                if (++batchSize == 1000) {
                    pstmt.executeBatch();
                    batchSize = 0;
                }
            } catch (SQLException e) {
                //if set"Type" does not match the order and type indicated in the preparedStatement.
                System.out.println(e.getMessage());
            } catch (ParseException e) {
                System.out.println(e.getMessage());
            }
        }
        
        //execute batch
        try {
            pstmt.executeBatch();
            //insert remaining records
        } catch (SQLException e) {
            //If error, or timeout(too many commands) 
            System.out.println(e.getMessage());
        } finally {
            ConnectionManager.close(conn,pstmt,null);
        }
        
    }   //end of validateAndInsertCourses()

    /**
     * validate and insert sections to database
     * 
     */
    public static void insertSections() {
        
        //----------------------------------------
        //Get fieldHeaders and identify their corresponding column numbers.
        //(the order in which fieldHeaders are arranged in the String[] represent the order in the CSV file.
        int courseColNum = getColumnNumber("course",fieldHeaders);
        int sectionColNum = getColumnNumber("section",fieldHeaders);
        int dayColNum = getColumnNumber("day",fieldHeaders);
        int startColNum = getColumnNumber("start",fieldHeaders);
        int endColNum = getColumnNumber("end",fieldHeaders);
        int instructorColNum = getColumnNumber("instructor",fieldHeaders);
        int venueColNum = getColumnNumber("venue",fieldHeaders);
        int sizeColNum = getColumnNumber("size",fieldHeaders);      
        
        
        //----------------------------
        //SET UP MySQL overheads
        Connection conn = ConnectionManager.getConnection();
        PreparedStatement pstmt = null;
        try {
            pstmt = conn
                    .prepareStatement("INSERT INTO section(course,section,day,start,end,instructor,venue,size) VALUES (?,?,?,?,?,?,?,?)");        
        } catch (SQLException e) {
            //if fail access database.
            System.out.println(e.getMessage());
        }
        

        
        //---------------------------
        //BATCH AND EXECUTE INSERT
        int batchSize = 0;
        
        SimpleDateFormat timeFormat = new SimpleDateFormat("H:mm");
        try {  
            for (String[] row : entries) {    
            
                pstmt.setString(1,row[courseColNum]);
                pstmt.setString(2,row[sectionColNum]);
                pstmt.setInt(3,Integer.parseInt(row[dayColNum]));
                
                //convert from String to java.util.Date, getTime() and convert to java.sql.Date
                java.sql.Time start = new java.sql.Time(timeFormat.parse(row[startColNum]).getTime());
                java.sql.Time end = new java.sql.Time(timeFormat.parse(row[endColNum]).getTime());
                
                pstmt.setTime(4,start);
                pstmt.setTime(5,end);
                pstmt.setString(6,row[instructorColNum]);
                pstmt.setString(7,row[venueColNum]);
                pstmt.setInt(8,Integer.parseInt(row[sizeColNum]));
                
                pstmt.addBatch();
                
                if (++batchSize == 1000) {
                    pstmt.executeBatch();
                    batchSize = 0;
                }
                
            }   //end for-loop
            
            //insert remaining records
            pstmt.executeBatch();
            
        } catch (SQLException e) {
            //if set"Type" does not match the order and type indicated in the preparedStatement.
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } finally {
            ConnectionManager.close(conn,pstmt,null);
        }
        
    }   //end of validateAndInsertSections()
    
    /**
     * validate and insert Prerequisite data from csv files to database.
     */
    public static void insertPrerequisites() {
        
        //----------------------------------------
        //Get fieldHeaders and identify their corresponding column numbers.
        //(the order in which fieldHeaders are arranged in the String[] represent the order in the CSV file.                
        int courseColNum = getColumnNumber("course",fieldHeaders);
        int prerequisiteColNum = getColumnNumber("prerequisite",fieldHeaders);    
        
        
        //----------------------------
        //SET UP MySQL overheads
        Connection conn = ConnectionManager.getConnection();
        PreparedStatement pstmt = null;
        try {
            pstmt = conn
                    .prepareStatement("INSERT INTO prerequisite(course,prerequisite) VALUES (?,?)");        
        } catch (SQLException e) {
            //if fail access database.
            System.out.println(e.getMessage());
        }
        

        
        //---------------------------
        //BATCH AND EXECUTE INSERT
        int batchSize = 0;
        
        try {  
            for (String[] row : entries) {    
            
                pstmt.setString(1,row[courseColNum]);
                pstmt.setString(2,row[prerequisiteColNum]);
                
                pstmt.addBatch();
                
                if (++batchSize == 1000) {
                    pstmt.executeBatch();
                    batchSize = 0;
                }
                
            }   //end for-loop
            
            //insert remaining records
            pstmt.executeBatch();
            
        } catch (SQLException e) {
            //if set"Type" does not match the order and type indicated in the preparedStatement.
            e.printStackTrace();
        } finally {
            ConnectionManager.close(conn,pstmt,null);
        }
        
    }   //end of insertPrerequisites()

    /**
     * validate and insert Course completed data from csv files to database.
     */
    public static void insertCourseCompleted() {
        
        //----------------------------------------
        //Get fieldHeaders and identify their corresponding column numbers.
        //(the order in which fieldHeaders are arranged in the String[] represent the order in the CSV file.
        int courseColNum = getColumnNumber("code",fieldHeaders);
        int studentColNum = getColumnNumber("userid",fieldHeaders);    
        
        
        //----------------------------
        //SET UP MySQL overheads
        Connection conn = ConnectionManager.getConnection();
        PreparedStatement pstmt = null;
        try {
            pstmt = conn
                    .prepareStatement("INSERT INTO course_completed(user_id,course) VALUES (?,?)");        
        } catch (SQLException e) {
            //if fail access database.
            System.out.println(e.getMessage());
        }
        

        
        //---------------------------
        //BATCH AND EXECUTE INSERT
        int batchSize = 0;
        
        try {  
            for (String[] row : entries) {    
            
                pstmt.setString(1,row[studentColNum]);
                pstmt.setString(2,row[courseColNum]);
                
                pstmt.addBatch();
                
                if (++batchSize == 1000) {
                    pstmt.executeBatch();
                    batchSize = 0;
                }
                
            }   //end for-loop
            
            //insert remaining records
            pstmt.executeBatch();
            
        } catch (SQLException e) {
            //if set"Type" does not match the order and type indicated in the preparedStatement.
            e.printStackTrace();
        } finally {
            ConnectionManager.close(conn,pstmt,null);
        }
        
    }   //end of validateAndInsertCourseCompleted()
    
    /**
     * validate and insert Bids data from csv files to database.
     */
    public static void insertBids() {
        entries.removeAll(alreadyBiddedList);
        //----------------------------------------
        //Get fieldHeaders and identify their corresponding column numbers.
        //(the order in which fieldHeaders are arranged in the String[] represent the order in the CSV file.
                
        int userIdColNum = getColumnNumber("userid",fieldHeaders);
        int amountColNum = getColumnNumber("amount",fieldHeaders);
        int codeColNum = getColumnNumber("code",fieldHeaders);
        int sectionColNum = getColumnNumber("section",fieldHeaders);
        
        
        //----------------------------
        //SET UP MySQL overheads
        Connection conn = ConnectionManager.getConnection();
        PreparedStatement pstmt = null;
        try {
            pstmt = conn
                    .prepareStatement("INSERT INTO bid(user_id,amount,course,section) VALUES (?,?,?,?)");        
        } catch (SQLException e) {
            //if fail access database.
            System.out.println(e.getMessage());
        }
        

        
        //---------------------------
        //BATCH AND EXECUTE INSERT
        int batchSize = 0;
        
        try {  
            for (String[] row : entries) {    
            
                pstmt.setString(1,row[userIdColNum]);
                pstmt.setBigDecimal(2,new BigDecimal(row[amountColNum]));
                pstmt.setString(3,row[codeColNum]);
                pstmt.setString(4,row[sectionColNum]);
                pstmt.addBatch();
                
                if (++batchSize == 1000) {
                    pstmt.executeBatch();
                    batchSize = 0;
                }
                
            }   //end for-loop
            
            //insert remaining records
            pstmt.executeBatch();
            
        } catch (SQLException e) {
            //if set"Type" does not match the order and type indicated in the preparedStatement.
            e.printStackTrace();
        } finally {
            ConnectionManager.close(conn,pstmt,null);
        }
        
    }   //end of validateAndInsertBids()    
    
    
    /**
     * Get fieldHeaders and identify their corresponding column numbers.
     * @param header
     * @param fieldHeaders
     * @return 
     *      - columnNumber that represents the order dictated in the CSV file
     *      - value of -1, if no such header is found within the row of "column names"/"field headers"
     */
    private static int getColumnNumber(String header, String[] fieldHeaders) {
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
    
        //clear tables
    /**
     * Execute SQL query to drop and clear database table
     */
    public static void clearTables() {

	Connection conn = null;   
	PreparedStatement pstmt = null;
	
	   try{
			String sqlStatement = "DROP TABLE IF EXISTS round, minimum_price, sudden_death, failed_bid, section_student, bid_temp, bid, course_completed, prerequisite, section, course, student";
			conn = ConnectionManager.getConnection();
			pstmt = conn.prepareStatement(sqlStatement);
			System.out.println("Deleting table in given database...");
			pstmt.executeUpdate();
			System.out.println("Table deleted in given database...");
		}catch(SQLException e){
			//Handle blacklistedEntries 
			e.printStackTrace();
		}finally{
			//finally block used to close resources
			ConnectionManager.close(conn, pstmt, null);
		}
	}
    /**
     * Get the number of successfully validate records
     * @return the total number of successfully validated records
     *     
     */
    public static int getNumRecordLoaded() {
        return entries.size();
    }
    
}