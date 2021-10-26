package com.scripted.selfHealingTests;

import com.scripted.web.BrowserDriver;


public class SampleUserName {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		BrowserDriver.funcGetWebdriver();
		BrowserDriver.launchWebURL("http://localhost:9001/MyApp/");
		System.out.println(BrowserDriver.getDriver().getPageSource());
	}

}
