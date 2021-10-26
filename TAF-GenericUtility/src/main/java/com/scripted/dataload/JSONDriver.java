package com.scripted.dataload;

import java.io.FileReader;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;



import junit.framework.Assert;

public class JSONDriver {
	
	/**read the values to JSONArray 
	 * @param filePath
	 *
	 */
	public static Logger LOGGER = Logger.getLogger(JSONDriver.class);
	public static JSONArray readJSONToArray(String filePath) {
		try {
			JSONParser parser = new JSONParser();
			FileReader reader = new FileReader(filePath);
			Object object = parser.parse(reader);
			JSONArray jsonArray = new JSONArray();
			jsonArray.put(object);
			return jsonArray;
		}catch(Exception e) {
			LOGGER.error("Error while parsing json file "+"Exception: "+e);
			Assert.fail("Error while parsing json file"+"Exception: "+e);
		}
		return null;
	}
	
	/**print the values in JSONArray 
	 * @param filePath
	 *
	 */
	public void printJSONArrayValues(String filePath) {
		try {
			JSONArray jsonArray = readJSONToArray(filePath);
			for(int i =0;i<jsonArray.length();i++) {
				System.out.println(jsonArray.get(i).toString());				
			}			
		}catch(Exception e) {
			LOGGER.error("Error while printing json array values "+"Exception: "+e);
			Assert.fail("Error while printing json array values"+"Exception: "+e);
		}
	}
	
	/**print the values in JSON file based on key entries 
	 * @param filePath
	 * @param key - value to search for in the json file
	 */	
	public org.json.simple.JSONObject readJSONByKey(String filePath, String key) {
		try {
			JSONParser parser = new JSONParser();
			FileReader reader = new FileReader(filePath);
			Object object = parser.parse(reader);
			org.json.simple.JSONObject jsonObject =  (org.json.simple.JSONObject) object;
			org.json.simple.JSONObject objKey= (org.json.simple.JSONObject) jsonObject.get(key); 
			return objKey;
		}catch(Exception e){
			LOGGER.error("Error while reading  json file by key value "+"Exception: "+e);
			Assert.fail("Error while reading  json file by key value "+"Exception: "+e);
		}
		return null;
	}
	
	/**print the values in JSON file based on key entries 
	 * @param jsonObject - json object
	 * @param key - value to search for in the json file
	 */	
	public org.json.simple.JSONObject readJSONSubset(org.json.simple.JSONObject jsonObject, String key) {
		try {
			org.json.simple.JSONObject objKey= (org.json.simple.JSONObject) jsonObject.get(key);
			return objKey;
		}catch(Exception e){
			LOGGER.error("Error while reading  json object "+"Exception: "+e);
			Assert.fail("Error while reading  json object "+"Exception: "+e);
		}
		return null;
	}
}