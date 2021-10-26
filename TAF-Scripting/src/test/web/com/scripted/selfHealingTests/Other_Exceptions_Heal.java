package com.scripted.selfHealingTests;

import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.Test;

import com.scripted.PageObjects.MyAppPage;
import com.scripted.PageObjects.WebSampleSwitchToWindowPage;
import com.scripted.selfhealing.SMWebHealer;
import com.scripted.web.BrowserDriver;

public class Other_Exceptions_Heal extends BaseTest_SelfHealing {

	@Test
	public void TC002_Unhandledalertcheck() throws InterruptedException {
		BrowserDriver.getCuPalDriver();
		// WebDriver driver = BrowserDriver.funcGetWebdriver();
		MyAppPage myAppPage;
		BrowserDriver.launchWebURL("https://www.rahulshettyacademy.com/AutomationPractice/");
		myAppPage = PageFactory.initElements(BrowserDriver.getDriver(), MyAppPage.class);
		try {
			myAppPage.clkRadioBtn2();
			System.out.println("clicked");
			myAppPage.checkAlert();
		} catch (Exception e) {
			System.out.println("Exception is :" + e);
		}

	}

	/*@Test
	public void WindowHandlersTest() {
		ThreadLocal<WebDriver> lthwebdriver = null;
		BrowserDriver.getCuPalDriver();
		BrowserDriver.launchWebURL("https://chandanachaitanya.github.io/selenium-practice-site/");

		WebSampleSwitchToWindowPage webSampleSwitchToWindowPage = PageFactory.initElements(BrowserDriver.getDriver(),
				WebSampleSwitchToWindowPage.class);

		try {

			webSampleSwitchToWindowPage.OldWindow();
			
		}

		catch (Exception e) {
		
		}

	}*/
}
