package com.scripted.mobile;

import java.net.URL;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WebDriver.Options;

import io.appium.java_client.MobileDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import junit.framework.Assert;

public class IOSUtil 
{
	public static Logger LOGGER = Logger.getLogger(IOSUtil.class);
	private IOSDriver<WebElement> iOSDriver = null;
	private WebDriver webdriver;
	private WebDriver driver;
	private IOSDeviceSettings iOSSettings;

	public IOSUtil(String remoteUrl, IOSDeviceSettings iOSSettings) {
		this.iOSSettings = iOSSettings;
		try {
			iOSDriver = new IOSDriver<WebElement>(new URL(remoteUrl), this.iOSSettings.getDesiredCapabilities());
			setIOSDriver(iOSDriver);
			this.driver = iOSDriver;
		} catch (Exception e) {
			LOGGER.error("AndrodDriver initilization issues : " + e.getMessage());
			Assert.fail("AndrodDriver initilization issues : " + e.getMessage());
			throw new WebDriverException("Driver initilization issues: " + e.getMessage());
		}
		this.setWebdriver(this.driver);
	}

	public IOSUtil() {
		throw new WebDriverException("Driver initilization issues");
	}

	public void setIOSDriver(IOSDriver<WebElement> driver) {
		this.iOSDriver = driver;
	}

	public IOSDriver<WebElement> getIOSDriver() {
		return iOSDriver;
	}

	public Options manage() {
		return this.driver.manage();
	}

	public IOSDeviceSettings getSettings() {
		return iOSSettings;
	}


	public WebDriver getWebdriver() {
		return webdriver;
	}

	public void setWebdriver(WebDriver webdriver) {
		this.webdriver = webdriver;
	}
}
