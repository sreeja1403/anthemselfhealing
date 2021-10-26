package com.scripted.testscripts;

import java.io.File;

import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;

import com.scripted.generic.FileUtils;
import com.scripted.reporting.AllureReport;
import com.scripted.web.BrowserDriver;

public class BaseTest {
	@BeforeSuite
	public void setup() {
		File dirPath = new File(FileUtils.getCurrentDir() + "\\" + "allure-results");
		FileUtils.deleteDirectory(dirPath);
	}

	@BeforeTest
	public void beforeTest() {
		System.out.println("beforetest");
	}

	@AfterTest
	public void afterTest() {
		System.out.println("aftertest");
		//LaunchBrowsers.closeBrowser();
	}
	
	

	@AfterSuite
	public void teardown() {
		try {
			BrowserDriver.closeBrowser();
			AllureReport.customizeReport();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	@BeforeMethod
	public void afterMethod(ITestResult result) {
			
		  System.out.println("method name:" + result.getMethod().getMethodName());
		}
	
}
