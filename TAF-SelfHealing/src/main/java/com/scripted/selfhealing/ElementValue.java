package com.scripted.selfhealing;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.openqa.selenium.WebElement;

public class ElementValue {

	String eleType;
	String eleValue;
	WebElement eleLocVle;
	ConcurrentHashMap<String ,String> locatorsMap;

	public ElementValue(String eleType, String eleValue, WebElement eleLocVle) {
		this.eleType = eleType;
		this.eleValue = eleValue;
		this.eleLocVle = eleLocVle;
		
	}
	
	public ElementValue(String eleType, String eleValue, WebElement eleLocVle,ConcurrentHashMap<String,String> locatorsMap) {
		this.eleType = eleType;
		this.eleValue = eleValue;
		this.eleLocVle = eleLocVle;
		this.locatorsMap=locatorsMap;
		
	}

	public ElementValue() {
		
	}

	@Override
	public String toString() {
		return "ElementValue [eleType=" + eleType + ", eleValue=" + eleValue + ", eleLocVle=" + eleLocVle
				+ ", locatorsMap=" + locatorsMap + "]";
	}

	

}
