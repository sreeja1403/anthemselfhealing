package com.scripted.selfhealing;

import java.util.concurrent.ConcurrentHashMap;

import org.openqa.selenium.WebElement;

public class ElementDetails {
	
	String status;
	String exceptionType;
	String FailedLoc;
	String HealedEleTyp;
	String HealedEleVle;
	WebElement HealedLoc;
	String scrnShtPath;
	String exptnMsg;
	ConcurrentHashMap<String, String> locDataMap = new ConcurrentHashMap<String, String>();
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getExceptionType() {
		return exceptionType;
	}
	public void setExceptionType(String exceptionType) {
		this.exceptionType = exceptionType;
	}
	public String getFailedLoc() {
		return FailedLoc;
	}
	public void setFailedLoc(String failedLoc) {
		FailedLoc = failedLoc;
	}
	public String getHealedEleTyp() {
		return HealedEleTyp;
	}
	public void setHealedEleTyp(String healedEleTyp) {
		HealedEleTyp = healedEleTyp;
	}
	public String getHealedEleVle() {
		return HealedEleVle;
	}
	public void setHealedEleVle(String healedEleVle) {
		HealedEleVle = healedEleVle;
	}
	public WebElement getHealedLoc() {
		return HealedLoc;
	}
	public void setHealedLoc(WebElement healedLoc) {
		HealedLoc = healedLoc;
	}
	public String getScrnShtPath() {
		return scrnShtPath;
	}
	public void setScrnShtPath(String scrnShtPath) {
		this.scrnShtPath = scrnShtPath;
	}
	public String getExptnMsg() {
		return exptnMsg;
	}
	public void setExptnMsg(String exptnMsg) {
		this.exptnMsg = exptnMsg;
	}
	public ConcurrentHashMap<String, String> getLocDataMap() {
		return locDataMap;
	}
	public void setLocDataMap(ConcurrentHashMap<String, String> locDataMap) {
		this.locDataMap = locDataMap;
	}
	@Override
	public String toString() {
		return "ElementDetails [status=" + status + ", exceptionType=" + exceptionType + ", FailedLoc=" + FailedLoc
				+ ", HealedEleTyp=" + HealedEleTyp + ", HealedEleVle=" + HealedEleVle + ", HealedLoc=" + HealedLoc
				+ ", scrnShtPath=" + scrnShtPath + ", exptnMsg=" + exptnMsg + ", locDataMap=" + locDataMap + "]";
	}
	
	
	
	
	
	
	
	
	
	

}
