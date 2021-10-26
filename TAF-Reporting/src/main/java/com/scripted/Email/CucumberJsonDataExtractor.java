package com.scripted.Email;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import junit.framework.Assert;

public class CucumberJsonDataExtractor {
	String PASS = "passed";
	String FAIL = "failed";

	public static Logger LOGGER = Logger.getLogger(CucumberJsonDataExtractor.class);

	private JSONArray readData(String path) throws IOException {
		JSONArray cucumberJson = null;
		File file = new File(path);
		try {
			String content = FileUtils.readFileToString(file, "utf-8");
			cucumberJson = new JSONArray(content);

		} catch (Exception e) {
			LOGGER.error("Error while trying to read file " + "Exception :" + e);
			Assert.fail("Error while trying to read file " + "Exception :" + e);
		}
		return cucumberJson;
	}

	public JSONObject getScenarioAndStepsStatus(String cucumberJsonPath) throws IOException {
		JSONArray jsonArray = readData(cucumberJsonPath);
		JSONObject result = new JSONObject();
		try {

			for (Object object : jsonArray) {
				JSONObject tagObject = new JSONObject();
				JSONObject obj = (JSONObject) object;
				JSONArray scenario = obj.getJSONArray("elements");
				for (Object arr : scenario) {
					JSONObject details = new JSONObject();
					JSONObject scenarioObj = (JSONObject) arr;
					JSONArray stepsArray = scenarioObj.getJSONArray("steps");
					JSONArray tags = scenarioObj.getJSONArray("tags");
					String tagName = "";
					for (Object tag : tags) {
						JSONObject tagList = (JSONObject) tag;

						tagName = tagList.getString("name");

					}
					JSONArray stepStatusList = new JSONArray();
					details.put("scenarioStatus", PASS);
					for (Object stepObj : stepsArray) {
						JSONObject steps = (JSONObject) stepObj;
						String stepStatus = steps.getJSONObject("result").getString("status");
						if (stepStatus.equals("failed")) {
							String stepErrorDetails = steps.getJSONObject("result").getString("error_message");
							stepErrorDetails = stepErrorDetails.replaceAll("[\\n\\t\\r\"]", "");
							if(stepErrorDetails.length()>700)
								stepErrorDetails = stepErrorDetails.substring(0, 700);
							details.put("scenarioStatus", FAIL);
							details.put("scenarioError", stepErrorDetails);
						}
						stepStatusList.put(stepStatus);
					}
					details.put("stepStatus", stepStatusList);
					tagObject.put(tagName, details);
					result.put(obj.getString("name"), tagObject);
				}
			}
		} catch (Exception e) {
			LOGGER.error("Error while trying to get status of steps and scenarios " + "Exception :" + e);
			Assert.fail("Error while trying to get status of steps and scenarios " + "Exception :" + e);
		}
		return result;
	}
}
