package com.scripted.jsonParser;

import java.util.LinkedHashMap;

public class Attributes {

	private String id;
	private String name;
	private String classname;
	private String xpath;
	private String css;
	private String position;
	private String selectorMethod;	
	private String selectorValue;
	private LinkedHashMap<String, String> attributeMap;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getClassname() {
		return classname;
	}
	public void setClassname(String classname) {
		this.classname = classname;
	}
	public String getXpath() {
		return xpath;
	}
	public void setXpath(String xpath) {
		this.xpath = xpath;
	}
	public String getCss() {
		return css;
	}
	public void setCss(String css) {
		this.css = css;
	}
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	public String getSelectorMethod() {
		return selectorMethod;
	}
	public void setSelectorMethod(String selectorMethod) {
		this.selectorMethod = selectorMethod;
	}
	public String getSelectorValue() {
		return selectorValue;
	}
	public void setSelectorValue(String selectorValue) {
		this.selectorValue = selectorValue;
	}
	public LinkedHashMap<String, String> getAttributeMap() {
		return attributeMap;
	}
	public void setAttributeMap(LinkedHashMap<String, String> attributeMap) {
		this.attributeMap = attributeMap;
	}
	@Override
	public String toString() {
		return "Attributes [id=" + id + ", name=" + name + ", classname=" + classname + ", xpath=" + xpath + ", css="
				+ css + ", position=" + position + ", selectorMethod=" + selectorMethod + ", selectorValue="
				+ selectorValue + ", attributeMap=" + attributeMap + "]";
	}
	

	
}
