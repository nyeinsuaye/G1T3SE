/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.app.utility;
import java.io.InputStream;
import java.util.Properties;
/**
 * Class that holds all the server file-systems path to be required for deployment
 * @author Zachery
 */
public class PropertiesUtility {
    private static Properties props = new Properties();
    private static String REAL_PATH;
    
    static {
        
        //Take the file/data "path.properties" to be used as "input"(in bytes) by Java
        InputStream is = PropertiesUtility.class.getResourceAsStream("/path.properties");
        /* We are able to indicate "/path.properties" because
         * the ROOT folder in Java is "WEB-INF/classes", where path.properties is stored.
         * (Note: "classes" is a reserved folder name for Tomcat)
         */
        
        InputStream is2 = PropertiesUtility.class.getResourceAsStream("/connection.properties");
        
        try {
            
            //the Properties.load(InputStream) processes the bytes(binary code) into unicode(e.g. abc123)
            //and stores it into the "Map<K,V>" within the Properties class
            props.load(is);
            props.load(is2);
        } catch (Exception e) {
            // USE specific exception name
            
            //Exception handling
            String msg = "Zip file not found!";
        }
    }
    /**
     * Get value from the property file
     * @param propertyKeyStr
     * @return 
     */
    public static String getProperty(String propertyKeyStr) {
        
        //if application is deployed on openshift
        if (props.getProperty("openshift-WEB-INF").equals(REAL_PATH)) {
            return props.getProperty(propertyKeyStr);   //return openshift connection properties
        } else {
            return props.getProperty("local." +propertyKeyStr); //return localhost properties
        }
    }
    
    /**
     * Get the property path
     * @param propertyKeyStr
     * @return property path
     */
    public static String getPathProperty(String propertyKeyStr) {
        return REAL_PATH + props.getProperty(propertyKeyStr);
    }
    
    /**
     * Initialize root for openshift
     * @param realPath 
     */
    public static void initializeRoot(String realPath) {
        if (realPath == null) {
            REAL_PATH = props.getProperty("openshift-WEB-INF");
            return;
        }
        
        REAL_PATH = realPath;
    }
    
}