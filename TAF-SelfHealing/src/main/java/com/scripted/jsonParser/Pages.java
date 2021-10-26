package com.scripted.jsonParser;

import java.util.List;

public class Pages {
	
	private String pageName;
	private List<WebElements> webElements;
	@Override
	public String toString() {
		return "Pages [pageName=" + pageName + ", webElements=" + webElements + "]";
	}
	public String getPageName() {
		return pageName;
	}
	public void setPageName(String pageName) {
		this.pageName = pageName;
	}
	public List<WebElements> getWebElements() {
		return webElements;
	}
	public void setWebElements(List<WebElements> webElements) {
		this.webElements = webElements;
	}

}
