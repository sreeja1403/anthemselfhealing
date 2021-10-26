package com.scripted.selfHealingTests;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.Test;

import com.scripted.AutoPracPageobjects.NewPage;
import com.scripted.web.BrowserDriver;

public class CompleteTest_Self_Healing extends BaseTest_SelfHealing {
	
	
	@Test
	public void TC005_TimeoutCheck()
	{	
	
	
		 BrowserDriver.getCuPalDriver();
		 WebDriver driver = BrowserDriver.getDriver();
		BrowserDriver.launchWebURL("http://localhost:8080/JenkinsProjectRel1/Sample/src/main/webapp/index.html");

		NewPage homePage = PageFactory.initElements(driver, NewPage.class);
		homePage.click();
		homePage.sendkeys();
		
	
	
		
	}
	

}
