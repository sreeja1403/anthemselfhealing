package com.scripted.jsonParser;

import java.util.List;

public class WebObjectRepo {

	private List<Pages> pages;

	@Override
	public String toString() {
		return "WebObjectRepo [pages=" + pages + "]";
	}

	public List<Pages> getPages() {
		return pages;
	}

	public void setPages(List<Pages> pages) {
		this.pages = pages;
	}
	
	
}
