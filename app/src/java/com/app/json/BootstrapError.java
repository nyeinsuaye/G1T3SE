/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.app.json;

/**
 *  A "BootstrapError" represents ALL the validation errors present in 1 ROW.
 *  
 */
public class BootstrapError implements Comparable<BootstrapError> {
    private String file;
    private int line;
    private String[] message;
    
    /**
     * For Gson's serialization/deserialization operations
     */
    private BootstrapError() {
    }
    /**
     * Create a BootstrapError object with
     * @param file the file
     * @param line the line where the error occurs
     * @param message array of error messages
     */
    public BootstrapError(String file, int line, String[] message) {
        this.file = file;
        this.line = line;
        this.message = message;
    }
    
    
    /**
     * returns the location of file
     * @return string of the file location 
     */
    public String getFile() {
        return file;
    }

     /**
     * returns the line where there is error
     * @return the line which has error
     */
    public int getLine() {
        return line;
    }
    
    /**
     * returns an array of error messages 
     * @return an array of error messages
     */
    public String[] getMessage() {
        return message;
    }

    @Override
    public int compareTo(BootstrapError errorObj) {
        String selfFile = this.file;
        String targetFile = errorObj.getFile();
        
        int selfLine = this.line;
        int targetLine = errorObj.getLine();
        
        
        if (selfFile.compareTo(targetFile) < 0) {
            return -1;
        } else if (selfFile.compareTo(targetFile) > 0) {
            return 1;
        } else {
        
            //when from the same file name, compare line numbers
            if (selfLine < targetLine) {
                return -1;
            } else if (selfLine > targetLine) {
                return 1;
            }

            return 0;
        }
                 
    }
    
}
