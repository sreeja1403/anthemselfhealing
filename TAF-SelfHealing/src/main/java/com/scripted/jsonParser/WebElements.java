package com.scripted.jsonParser;
 import java.util.*;
 
public class WebElements {
	
	private String elementName;
	private Locators locators;
	public String getElementName() {
		return elementName;
	}
	public void setElementName(String elementName) {
		this.elementName = elementName;
	}
	public Locators getLocators() {
		return locators;
	}
	public void setLocators(Locators locators) {
		this.locators = locators;
	}
	@Override
	public String toString() {
		return "WebElements [elementName=" + elementName + ", locators=" + locators + "]";
	}


}
