package com.scripted.jsonParser;

public class ObjectRepository {
	
	private WebObjectRepo webObjectRepo;

	@Override
	public String toString() {
		return "ObjectRepository [webObjectRepo=" + webObjectRepo + "]";
	}

	public WebObjectRepo getWebObjectRepo() {
		return webObjectRepo;
	}

	public void setWebObjectRepo(WebObjectRepo webObjectRepo) {
		this.webObjectRepo = webObjectRepo;
	}

}
