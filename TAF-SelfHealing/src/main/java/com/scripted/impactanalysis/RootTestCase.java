package com.scripted.impactanalysis;

import java.util.List;

public class RootTestCase {
	 public List<TestCase> testCase;

	    public int noSuchEleCount;

	    public int stleTmeOutCount;

	    public int netwrkErrCount;

	    public int othersCount;

	    public String testRunName;

	    public void setTestCase(List<TestCase> testCase){
	        this.testCase = testCase;
	    }
	    public List<TestCase> getTestCase(){
	        return this.testCase;
	    }
	    public void setNoSuchEleCount(int noSuchEleCount){
	        this.noSuchEleCount = noSuchEleCount;
	    }
	    public int getNoSuchEleCount(){
	        return this.noSuchEleCount;
	    }
	    public void setStleTmeOutCount(int stleTmeOutCount){
	        this.stleTmeOutCount = stleTmeOutCount;
	    }
	    public int getStleTmeOutCount(){
	        return this.stleTmeOutCount;
	    }
	    public void setNetwrkErrCount(int netwrkErrCount){
	        this.netwrkErrCount = netwrkErrCount;
	    }
	    public int getNetwrkErrCount(){
	        return this.netwrkErrCount;
	    }
	    public void setOthersCount(int othersCount){
	        this.othersCount = othersCount;
	    }
	    public int getOthersCount(){
	        return this.othersCount;
	    }
	    public void setTestRunName(String testRunName){
	        this.testRunName = testRunName;
	    }
	    public String getTestRunName(){
	        return this.testRunName;
	    }
}
