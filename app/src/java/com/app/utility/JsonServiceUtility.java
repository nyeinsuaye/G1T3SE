/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.app.utility;

import com.app.json.JsonVariableNaming;
import com.google.gson.FieldNamingStrategy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Class that has all the json service configurations (GsonBuilder)
 * @author Zachery
 */
public class JsonServiceUtility {
    private GsonBuilder configuration;  //GsonBuilder holds configurations/settings to create our gson obj
    private Gson translator;            //The gson obj translates/parse/converts between java objects and json objects
    
    public JsonServiceUtility() {
        this.configuration = new GsonBuilder()
                .setPrettyPrinting()
                .setFieldNamingStrategy(JsonVariableNaming.LOWER_CASE_WITH_SPACES)
                .disableHtmlEscaping();
        
        this.translator = configuration.create();
                
    }
    
    public String serialize(Object obj) {
        return translator.toJson(obj);
    }
    
    /**
     * 
     *
     * @param jsonStr
     * @param objType
     * - this parameter can be returned by calling someObject.class to get the class type of someObject
     * @return 
     */
    
    public <T> T deserialize(String jsonStr, Class<T> objType) {
        return translator.fromJson(jsonStr, objType);
    }
    
    public JsonObject getValidJsonObject(String incomingRequest) {
       JsonElement jElement = new JsonParser().parse(incomingRequest);
       
       JsonObject jObject = jElement.getAsJsonObject();
       
       return jObject;
       
    }
    
    /**
     * Check whether the json input is valid 
     * @param incomingRequest
     * @param action
     * @param messageList
     * @return validity status
     */
    public boolean isJsonParametersValid(String incomingRequest, String action, ArrayList<String> messageList) {
        JsonElement jElement = new JsonParser().parse(incomingRequest);
        
        if (jElement.isJsonNull() || !jElement.isJsonObject()) {
           return false;
        }
        
        JsonObject jObject = jElement.getAsJsonObject();
        
        if ("update-bid".equals(action) || "add-bid".equals(action)) {
           JsonElement useridElement = jObject.get("userid");
           JsonElement amountStrElement = jObject.get("amount");
           JsonElement courseElement = jObject.get("code");
           JsonElement sectionElement = jObject.get("section");
           
           //--- CHECK FOR MISSING/BLANK VALUES---
           boolean hasNoAmountError = isNotMissingOrBlank(amountStrElement,"amount",messageList);
           boolean hasNoCourseError = isNotMissingOrBlank(courseElement,"code",messageList);
           boolean hasNoSectionError = isNotMissingOrBlank(sectionElement,"section",messageList);
           boolean hasNoUseridError = isNotMissingOrBlank(useridElement,"userid",messageList);
           
           if ( hasNoAmountError && hasNoCourseError && hasNoSectionError && hasNoUseridError ) {
               return true;
           }
        }
        
        if ("delete-bid".equals(action) || "drop-section".equals(action)) {
           JsonElement useridElement = jObject.get("userid");
           JsonElement courseElement = jObject.get("code");
           JsonElement sectionElement = jObject.get("section");
           
           //--- CHECK FOR MISSING/BLANK VALUES---
           boolean hasNoCourseError = isNotMissingOrBlank(courseElement,"code",messageList);
           boolean hasNoSectionError = isNotMissingOrBlank(sectionElement,"section",messageList);
           boolean hasNoUseridError = isNotMissingOrBlank(useridElement,"userid",messageList);
           
           if ( hasNoCourseError && hasNoSectionError && hasNoUseridError ) {
               return true;
           }
        }
        
        if ("user-dump".equals(action)) {
            JsonElement useridElement = jObject.get("userid");
            
            //--- CHECK FOR MISSING/BLANK VALUES---
            boolean hasNoUseridError = isNotMissingOrBlank(useridElement,"userid",messageList);
            
            if ( hasNoUseridError ) {
               return true;
           }
        }
        
        if ("bid-dump".equals(action) || "section-dump".equals(action)) {
            JsonElement courseElement = jObject.get("course");
            JsonElement sectionElement = jObject.get("section");
            
            //--- CHECK FOR MISSING/BLANK VALUES---
            boolean hasNoCourseError = isNotMissingOrBlank(courseElement,"course",messageList);
            boolean hasNoSectionError = isNotMissingOrBlank(sectionElement,"section",messageList);
            
            if ( hasNoCourseError && hasNoSectionError ) {
               return true;
           }
        }
        
        
        //final part
        return false;
    }
    
    /**
     * Check whether there is any missing or blank element
     * @param element
     * @param parameterName
     * @param messageList
     * @return 
     */
    private boolean isNotMissingOrBlank(JsonElement element, String parameterName, ArrayList<String> messageList) {
        if (element == null) {
            messageList.add(parameterName +" is missing");
            return false;
        }
        
        if ("".equals(element.getAsString())) {
            messageList.add(parameterName +" is blank");
            return false;
        }
        
        return true;
    }
}
