package com.scripted.jsonWriter;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({ "TestCaseName", "testStep"})
public class TestCases {
	String TestCaseName;
	List<TestStep> testSteps;
	
	public TestCases() {
		TestCaseName = "";
		testSteps = new ArrayList<TestStep>();
	}
	
	public String getTestCaseName() {
		return TestCaseName;
	}
	public void setTestCaseName(String testCaseName) {
		TestCaseName = testCaseName;
	}
	public List<TestStep> getTestStep() {
		return testSteps;
	}
	public void setTestStep(List<TestStep> testStep) {
		this.testSteps = testStep;
	}
	@Override
	public String toString() {
		return "TestCase [TestCaseName=" + TestCaseName + ", testStep=" + testSteps + "]";
	}
	
	
	

}
