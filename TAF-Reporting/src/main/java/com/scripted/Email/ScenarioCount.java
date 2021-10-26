package com.scripted.Email;

import java.io.IOException;
import java.util.Map;

import org.apache.commons.collections.map.HashedMap;
import org.apache.log4j.Logger;
import org.json.JSONObject;

public class ScenarioCount {

	org.json.simple.JSONObject body = new org.json.simple.JSONObject();
	org.json.simple.JSONObject response = null;
	public static Logger LOGGER = Logger.getLogger(ScenarioCount.class);

	public static Map scenarioCountdetails(String cucumberJsonPath) throws IOException {

		Map<String, String> resultmap = new HashedMap();

		CucumberJsonDataExtractor cucm = new CucumberJsonDataExtractor();
		JSONObject obj = cucm.getScenarioAndStepsStatus(cucumberJsonPath);

		int passcount = 0;
		int failcount = 0;

		for (String feature : obj.keySet()) {
			JSONObject featureObject = obj.getJSONObject(feature);
			for (String testCase : featureObject.keySet()) {
				if (featureObject.getJSONObject(testCase).getString("scenarioStatus").equalsIgnoreCase("Passed")) {
					passcount++;
				}
				if (featureObject.getJSONObject(testCase).getString("scenarioStatus").equalsIgnoreCase("failed")) {
					failcount++;
				}
			}
		}

		resultmap.put("passcount", Integer.toString(passcount));
		resultmap.put("failcount", Integer.toString(failcount));
		int totalcount = passcount + failcount;
		resultmap.put("totalcount", Integer.toString(totalcount));

		return resultmap;

	}

}
