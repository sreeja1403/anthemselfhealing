package com.scripted.selfHealingTests;

import java.util.LinkedHashMap;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.Test;

import com.scripted.PageObjects.MyAppPage;
import com.scripted.web.BrowserDriver;

public class Test_iframe extends BaseTest_SelfHealing {

	@Test
	public void TC001_iframecheck_NoSwitch() throws InterruptedException {
		BrowserDriver.getCuPalDriver();
		// WebDriver driver = BrowserDriver.funcGetWebdriver();
		MyAppPage myAppPage;
		BrowserDriver.launchWebURL("https://www.rahulshettyacademy.com/AutomationPractice/");
		myAppPage = PageFactory.initElements(BrowserDriver.getDriver(), MyAppPage.class);
		myAppPage.clkRadioBtn2();
		System.out.println("clicked");
		myAppPage.checkIframewithoutswitch();
		Thread.sleep(1000);
		
	}
	@Test
	public void TC002_iframecheck_Switch()  throws InterruptedException {
		BrowserDriver.getCuPalDriver();
		// WebDriver driver = BrowserDriver.funcGetWebdriver();
		MyAppPage myAppPage;
		BrowserDriver.launchWebURL("https://www.rahulshettyacademy.com/AutomationPractice/");
		myAppPage = PageFactory.initElements(BrowserDriver.getDriver(), MyAppPage.class);
		System.out.println("clicked");
		//myAppPage.dropdown();
		myAppPage.checkIframe();
		//myAppPage.dropdown();
		//Thread.sleep(1000);
		
	}
	
}
