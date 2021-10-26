package com.scripted.web;

import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;

import com.scripted.dataload.PropertyDriver;

public class SauceDriver {
	public static WebDriver driver = null;
	public static String strBrowserName = null;
	public static String strBrowserVersion = null;
	public static String strSauceUserName = null;
	public static String strSauceAccessKey = null;
	public static String strSauceURL = null;
	public static String strPlatform = null;
	public static String strTestName = null;

	public static Logger LOGGER = Logger.getLogger(SauceDriver.class);

	public static WebDriver funcSauceLabWebdriver() {
		try {
			PropertyDriver p = new PropertyDriver();
			if (driver == null) {
				p.setPropFilePath("src/main/resources/Mobile/SauceLabs/WebConfig.properties");
				strSauceUserName = p.readProp("sauceUserName");
				strSauceAccessKey = p.readProp("sauceAccessKey");
				strBrowserName = p.readProp("browserName");
				strPlatform = p.readProp("platform");
				strBrowserVersion = p.readProp("browserVersion");
				strSauceURL = p.readProp("sauceURL");
				if (!(p.readProp("httpsproxyHost").isEmpty()) && !(p.readProp("httpsproxyPort").isEmpty())) {
					System.setProperty("https.proxyHost", p.readProp("httpsproxyHost"));
					System.setProperty("https.proxyPort", p.readProp("httpsproxyPort"));
				}
			}

			if (strBrowserName == null || strBrowserName.equals(" ")) {
				LOGGER.info("Browser name is null, please check the value of browserName in config.properties");
				System.exit(0);
			} else {
				LOGGER.info("Browser : " + strBrowserName);
				strBrowserName = strBrowserName.toLowerCase();

				DesiredCapabilities dcap = DesiredCapabilities.safari();
				dcap.setCapability("username", strSauceUserName);
				dcap.setCapability("accessKey", strSauceAccessKey);
				dcap.setCapability("platform", strPlatform);
				dcap.setBrowserName(strBrowserName);
				dcap.setVersion(strBrowserVersion);
		        URL url = new URL(strSauceURL);

				switch (strBrowserName) {

				case "safari":
					driver = new RemoteWebDriver(url, dcap);
					break;
				case "chrome":
					driver = new RemoteWebDriver(url, dcap);
					break;
				case "ie":
					driver = new RemoteWebDriver(url, dcap);
					break;
				case "firefox":
					driver = new RemoteWebDriver(url, dcap);
					break;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("Error occurred while configuring webdrivers" + "Exception :" + e);
			Assert.fail("Webdriver initialisation issues" + "Exception :" + e);
		}

		driver.manage().deleteAllCookies();
		driver.manage().window().maximize();
		driver.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);
		driver.manage().timeouts().setScriptTimeout(60, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		BrowserDriver.driver=driver;
		return driver;
	}

}
