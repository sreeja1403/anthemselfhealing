package com.scripted.selfHealingTests;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.Test;

import com.scripted.selfHealingPgObj.MyAppPage;
import com.scripted.web.BrowserDriver;

public class NetworkTest extends BaseTest_SelfHealing {
	public static Logger LOGGER = Logger.getLogger(NetworkTest.class);
  @Test
  public void tcOO6_NetworkCheck() throws InterruptedException  {
	 BrowserDriver.funcGetWebdriver();
	 BrowserDriver.launchWebURL("http://localhost:9001/MyApp/");
	 //BrowserDriver.launchWebURL("https://www.google.com");
	 //driver.get("https://www.google.com");
	  
	MyAppPage myAppPage = PageFactory.initElements(BrowserDriver.getDriver(), MyAppPage.class);
	  myAppPage.clkRadioBtn1();
	  System.out.println("clicked");
	  LOGGER.info("clicked");
	  Thread.sleep(10000);
	  BrowserDriver.getDriver().navigate().refresh();
	  Thread.sleep(10000);
	  myAppPage.clkRadioBtn2();
	  Thread.sleep(5000);
	  myAppPage.clkRadioBtn3();
	  Thread.sleep(5000);
	  myAppPage.entSuggstn();
	  Thread.sleep(5000);
	  myAppPage.sltChkBx1();
	  Thread.sleep(5000);
	  myAppPage.sltChkBx2();
	  Thread.sleep(5000);
	  myAppPage.sltChkBx3();
	  Thread.sleep(5000);
	  
  }
  
  @Test
  public void tcOO7_NetworkCheck() throws InterruptedException  {
	 BrowserDriver.funcGetWebdriver();
	 BrowserDriver.launchWebURL("http://localhost:9001/MyApp/");
	 //BrowserDriver.launchWebURL("https://www.google.com");
	 //driver.get("https://www.google.com");
	  
	MyAppPage myAppPage = PageFactory.initElements(BrowserDriver.getDriver(), MyAppPage.class);
	  myAppPage.clkRadioBtn1();
	  System.out.println("clicked");
	  LOGGER.info("clicked");
	  Thread.sleep(10000);
	  BrowserDriver.getDriver().navigate().refresh();
	  Thread.sleep(10000);
	  myAppPage.clkRadioBtn2();
	  Thread.sleep(5000);
	  myAppPage.clkRadioBtn3();
	  Thread.sleep(5000);
	  myAppPage.entSuggstn();
	  Thread.sleep(5000);
	  myAppPage.sltChkBx1();
	  Thread.sleep(5000);
	  myAppPage.sltChkBx2();
	  Thread.sleep(5000);
	  myAppPage.sltChkBx3();
	  Thread.sleep(5000);
	  
  }
}
