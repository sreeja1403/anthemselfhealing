package com.scripted.jsonWriter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

//import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.scripted.selfhealing.ElementValue;

//@JsonPropertyOrder({"PageName","ObjectName","FailedElement", "TestStepFailureReason", "HealedElement", "HealStatus","locatorEleMap"})
public class TestStep {
	
	
	
	private String FailedElement;
	private String TestStepFailureReason;
	private String HealedElement;
	private String  HealStatus;
	private String PageName;
	private String ObjectName;
	private String technology;
	private String scrnShotPath;
	private String excptnMsg;
	private String action;
	private ConcurrentHashMap<String,String> locatorEleMap;
	public String getFailedElement() {
		return FailedElement;
	}
	public void setFailedElement(String failedElement) {
		FailedElement = failedElement;
	}
	public String getTestStepFailureReason() {
		return TestStepFailureReason;
	}
	public void setTestStepFailureReason(String testStepFailureReason) {
		TestStepFailureReason = testStepFailureReason;
	}
	public String getHealedElement() {
		return HealedElement;
	}
	public void setHealedElement(String healedElement) {
		HealedElement = healedElement;
	}
	public String getHealStatus() {
		return HealStatus;
	}
	public void setHealStatus(String healStatus) {
		HealStatus = healStatus;
	}
	public String getPageName() {
		return PageName;
	}
	public void setPageName(String pageName) {
		PageName = pageName;
	}
	public String getObjectName() {
		return ObjectName;
	}
	public void setObjectName(String objectName) {
		ObjectName = objectName;
	}
	public String getTechnology() {
		return technology;
	}
	public void setTechnology(String technology) {
		this.technology = technology;
	}
	public String getScrnShotPath() {
		return scrnShotPath;
	}
	public void setScrnShotPath(String scrnShotPath) {
		this.scrnShotPath = scrnShotPath;
	}
	public String getExcptnMsg() {
		return excptnMsg;
	}
	public void setExcptnMsg(String excptnMsg) {
		this.excptnMsg = excptnMsg;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public ConcurrentHashMap<String, String> getLocatorEleMap() {
		return locatorEleMap;
	}
	public void setLocatorEleMap(ConcurrentHashMap<String, String> locatorEleMap) {
		this.locatorEleMap = locatorEleMap;
	}
	@Override
	public String toString() {
		return "TestStep [FailedElement=" + FailedElement + ", TestStepFailureReason=" + TestStepFailureReason
				+ ", HealedElement=" + HealedElement + ", HealStatus=" + HealStatus + ", PageName=" + PageName
				+ ", ObjectName=" + ObjectName + ", technology=" + technology + ", scrnShotPath=" + scrnShotPath
				+ ", excptnMsg=" + excptnMsg + ", action=" + action + ", locatorEleMap=" + locatorEleMap + "]";
	}
	
	
	
	
	
	

	
	
	


}
