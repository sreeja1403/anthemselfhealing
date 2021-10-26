package com.scripted.jsonWriter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;



import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonWriter {

	static List<TestStep> steps = new ArrayList<TestStep>();

	public static void main(String[] args) {

	
				
		/*
		 * ObjectMapper mapper = new ObjectMapper(); HealingInfo healinginfo =
		 * createHealingInfo();
		 * 
		 * //LinkedHashMap<String,String> healingInfoJson = new
		 * LinkedHashMap<String,String>();
		 * 
		 * try { //createHealingInfo(); //mapper.writeValue(new
		 * File("C:\\Users\\u38119\\Desktop\\user.json"), healinginfo);
		 * 
		 * String jsonInString = mapper.writeValueAsString(healinginfo); // For printing
		 * json value System.out.println(jsonInString);
		 * 
		 * }
		 */
		/*
		 * catch(Exception e) { e.printStackTrace();
		 * System.out.println("Error while trying to create healing report json"); }
		 */

	}

	public static void AddStep(String Locatortype, String ElementName, String HealedLocator) {
		TestStep step = new TestStep();
		step.setFailedElement(ElementName);
		step.setTestStepFailureReason("Wrong " + Locatortype);
		step.setHealedElement(HealedLocator);
		step.setHealStatus("Success");

		steps.add(step);

	}

	private static HealingInfo createHealingInfo() {
		HealingInfo healinginfo = new HealingInfo();

		List<TestStep> teststeps = new ArrayList<TestStep>();

		return healinginfo;

	}

}
