package com.scripted.jsonParser;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class Locators {

	private String id;
	private String name;
	private String classname;
	private String iframeElement;
	private String css;
	private ConcurrentHashMap<String,String> xpath;
	private ConcurrentHashMap<String,String> attributes;
	private ConcurrentHashMap<String,String> selectedLocator;
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
	public String getIframeElement() {
		return iframeElement;
	}
	public void setIframeElement(String iframeElement) {
		this.iframeElement = iframeElement;
	}
	public String getCss() {
		return css;
	}
	public void setCss(String css) {
		this.css = css;
	}
	public ConcurrentHashMap<String, String> getXpath() {
		return xpath;
	}
	public void setXpath(ConcurrentHashMap<String, String> xpath) {
		this.xpath = xpath;
	}
	public ConcurrentHashMap<String, String> getAttributes() {
		return attributes;
	}
	public void setAttributes(ConcurrentHashMap<String, String> attributes) {
		this.attributes = attributes;
	}
	public ConcurrentHashMap<String, String> getSelectedLocator() {
		return selectedLocator;
	}
	public void setSelectedLocator(ConcurrentHashMap<String, String> selectedLocator) {
		this.selectedLocator = selectedLocator;
	}
	@Override
	public String toString() {
		return "Locators [id=" + id + ", name=" + name + ", classname=" + classname + ", iframeElement=" + iframeElement
				+ ", css=" + css + ", xpath=" + xpath + ", attributes=" + attributes + ", selectedLocator="
				+ selectedLocator + "]";
	}
	
	
}
