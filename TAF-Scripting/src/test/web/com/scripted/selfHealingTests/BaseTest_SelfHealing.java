package com.scripted.selfHealingTests;

import java.io.File;
import java.util.logging.Level;

import org.apache.log4j.Logger;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;

import com.scripted.configurations.SkriptmateConfigurations;
import com.scripted.generic.FileUtils;
import com.scripted.jsonWriter.JsonHealingInfoWriter;
import com.scripted.reporting.AllureReport;
import com.scripted.reporting.selfhealing.ReportGenerationDummy;
import com.scripted.selfhealing.HealingConfig;
import com.scripted.selfhealing.SMWebHealer;
import com.scripted.web.BrowserDriver;
import com.scripted.web.WebDriverPathUtil;

public class BaseTest_SelfHealing {
	public static Logger LOGGER = Logger.getLogger(BaseTest_SelfHealing.class);
	ThreadLocal<String> testCaseName = new ThreadLocal<String>();

	@BeforeSuite
	public void setup() {
		SkriptmateConfigurations.beforeSuite();
		
	}

	
	  @BeforeMethod public void beforeMethod(ITestResult result) {
	  testCaseName.set(result.getMethod().getMethodName()) ; SMWebHealer sm = new
	  SMWebHealer(); sm.setTstCseNmeTstNG(testCaseName.get()); }
	 

	@AfterTest
	public void afterTest() {
		LOGGER.info("aftertest");
		// LaunchBrowsers.closeBrowser();
	}
	
	
	  @AfterMethod public void aftermethod() { BrowserDriver.closeBrowser(); }
	 

	@AfterSuite
	public void teardown() {
		try {
			LOGGER.info("After suite");
			SkriptmateConfigurations.afterSuite();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
