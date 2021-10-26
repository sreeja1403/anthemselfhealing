package com.scripted.testscripts;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class SelenoidSample {

	@DataProvider(name = "BrowserConfig", parallel = true)

	public static Object[][] browserConfig() {

		return new Object[][] { { "chrome", "75.0" }, { "chrome", "76.0" }, { "firefox", "67.0" },
				{ "firefox", "68.0" }, { "opera", "60.0" }, { "opera", "62.0" } };

	}

	@Test(dataProvider = "BrowserConfig", priority = 0)
	public void selenoidTest(String browserName, String browserVersion) {

		try {
			DesiredCapabilities dcap = DesiredCapabilities.chrome();
			// ChromeOptions options = new ChromeOptions();
			// dcap.setCapability(ChromeOptions.CAPABILITY, options);
			dcap.setCapability("enableVideo", true);
			dcap.setCapability("enableVNC", true);
			dcap.setBrowserName(browserName);
			dcap.setVersion(browserVersion);

			URL url = new URL("http://http://104.43.246.239:4444/wd/hub");
			WebDriver driver = new RemoteWebDriver(url, dcap);

			// Get URL
			driver.get("https://www.google.com/");
			// Print Title
			System.out.println(driver.getTitle());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/*
	 * @Test (priority = 1) public void firefox2() {
	 * 
	 * try { DesiredCapabilities dcap = DesiredCapabilities.chrome(); ChromeOptions
	 * options = new ChromeOptions(); dcap.setCapability(ChromeOptions.CAPABILITY,
	 * options); dcap.setCapability("enableVideo", true);
	 * dcap.setCapability("enableVNC", true); dcap.setBrowserName("firefox");
	 * dcap.setVersion("68.0");
	 * 
	 * 
	 * URL url = new URL("http://23.99.196.111:4444/wd/hub"); WebDriver driver = new
	 * RemoteWebDriver(url, dcap);
	 * 
	 * // Get URL driver.get("https://www.google.com/"); // Print Title
	 * System.out.println(driver.getTitle()); }catch(Exception e) {
	 * e.printStackTrace(); }
	 * 
	 * }
	 */

}
