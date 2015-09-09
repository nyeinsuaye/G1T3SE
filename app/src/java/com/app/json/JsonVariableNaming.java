/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.app.json;

import com.google.gson.FieldNamingStrategy;
import java.lang.reflect.Field;
 
/**
 * Naming conventions in JSON strings
 * @author Zachery
 */
public enum JsonVariableNaming implements FieldNamingStrategy {
    

    /**
      *  Using this naming strategy with Gson will ensure that the field name is
      * lower case and separated by spaces
      * <p>Here's a few examples of the form "Java Field Name" ---> "JSON Field Name":</p>
      * <ul>
      *   <li>someFieldName ---> some field name</li>
      *   <li>_someFieldName ---> _some field name</li>
      *   <li>aStringField ---> a string field</li>
      *   <li>aURL ---> a u r l</li>
      * </ul>
      * @param field 
      * @return String of the translated name
     */
    LOWER_CASE_WITH_SPACES() {
           public String translateName(Field f) {
               return separateCamelCase(f.getName(), " ").toLowerCase();
           }
    };
    
    //END OF ENUM TYPES
    /**
     * Converts the field name that uses camel-case define word separation into
     * separate words that are separated by the provided {@code separatorString}.
     * @param name 
     * @param separator 
     * @return a string the is in camel case.
     */
    private static String separateCamelCase(String name, String separator) {
      StringBuilder translation = new StringBuilder();
      for (int i = 0; i < name.length(); i++) {
        char character = name.charAt(i);
        if (Character.isUpperCase(character) && translation.length() != 0) {
          translation.append(separator);
        }
        translation.append(character);
      }
      return translation.toString();
    }
}
