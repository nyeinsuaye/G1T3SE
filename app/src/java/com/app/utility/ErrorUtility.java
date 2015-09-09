/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.app.utility;
import java.io.InputStream;
import java.util.Properties;

/**
 * Properties utility that retrieves the error.properties file that contains error messages
 * @author Zachery
 */
public class ErrorUtility {
    private static Properties props = new Properties();
    private static String REAL_PATH;
      static {
        
        //Take the file/data "error.properties" to be used as "input"(in bytes) by Java
        InputStream is = PropertiesUtility.class.getResourceAsStream("/error.properties");
        /* We are able to indicate "/path.properties" because
         * the ROOT folder in Java is "WEB-INF/classes", where path.properties is stored.
         * (Note: "classes" is a reserved folder name for Tomcat)
         */
                
        try {
            
            //the Properties.load(InputStream) processes the bytes(binary code) into unicode(e.g. abc123)
            //and stores it into the "Map<K,V>" within the Properties class
            props.load(is);
        } catch (Exception e) {
            // USE specific exception name
            System.out.println("Error: ErrorUtility: line 29");
            //Exception handling
        }
    }
    
    public static String getProperty(String error) { 
        return props.getProperty(error);
    }
   
   
}
