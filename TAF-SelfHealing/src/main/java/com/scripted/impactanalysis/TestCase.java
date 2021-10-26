package com.scripted.impactanalysis;

import java.util.List;

public class TestCase {
	public List<TestStep> testStep;

    public String testCaseName;

    public void setTestStep(List<TestStep> testStep){
        this.testStep = testStep;
    }
    public List<TestStep> getTestStep(){
        return testStep;
    }
    public void setTestCaseName(String testCaseName){
        this.testCaseName = testCaseName;
    }
    public String getTestCaseName(){
        return testCaseName;
    }
}
