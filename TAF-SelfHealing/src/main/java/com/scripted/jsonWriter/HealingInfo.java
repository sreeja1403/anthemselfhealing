package com.scripted.jsonWriter;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({ "TestRunName", "testCase"})
public class HealingInfo {
	
	String TestRunName;
	int noSuchEleCount;
	int stleTmeOutCount;
	int netwrkErrCount;
	int othersCount;
	List<TestCases> testCase;
	
	public HealingInfo()
	{
		TestRunName = "";
		testCase = new ArrayList<TestCases>();
	} 
	public String getTestRunName() {
		return TestRunName;
	}
	public void setTestRunName(String testRunName) {
		TestRunName = testRunName;
	}
	public int getNoSuchEleCount() {
		return noSuchEleCount;
	}
	public void setNoSuchEleCount(int noSuchEleCount) {
		this.noSuchEleCount = noSuchEleCount;
	}
	public int getStleTmeOutCount() {
		return stleTmeOutCount;
	}
	public void setStleTmeOutCount(int stleTmeOutCount) {
		this.stleTmeOutCount = stleTmeOutCount;
	}
	public int getNetwrkErrCount() {
		return netwrkErrCount;
	}
	public void setNetwrkErrCount(int netwrkErrCount) {
		this.netwrkErrCount = netwrkErrCount;
	}
	public int getOthersCount() {
		return othersCount;
	}
	public void setOthersCount(int othersCount) {
		this.othersCount = othersCount;
	}
	public List<TestCases> getTestCase() {
		return testCase;
	}
	public void setTestCase(List<TestCases> testCase) {
		this.testCase = testCase;
	}
	@Override
	public String toString() {
		return "HealingInfo [TestRunName=" + TestRunName + ", noSuchEleCount=" + noSuchEleCount + ", stleTmeOutCount="
				+ stleTmeOutCount + ", netwrkErrCount=" + netwrkErrCount + ", othersCount=" + othersCount
				+ ", testCase=" + testCase + "]";
	}
	
	

	

}
