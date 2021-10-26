package com.scripted.web;

import org.apache.log4j.Logger;

import com.scripted.generic.FileUtils;

public class WebDriverPathUtil {
	private static final Logger log = Logger.getLogger(WebDriverPathUtil.class);

	public static String getIEDriverPath() throws WebAutomationException {
		log.info("Inside WebDriverUtil.getIEDriverPath method");
		return FileUtils.getFilePath("src/main/resources/Web/Drivers/IEDriverServer.exe");
	}

	public static String getChromeDriverPath() throws WebAutomationException {
		log.info("Inside WebDriverUtil.getChromeDriverPath method");
		return FileUtils.getFilePath("src/main/resources/Web/Drivers/chromedriver.exe");
	}

	public static String getGeckoDriverPath() throws WebAutomationException {
		log.info("Inside WebDriverUtil.getGeckoDriverPath method");
		return FileUtils.getFilePath("src/main/resources/Web/Drivers/geckodriver.exe");
	}
	
	public static String getPhatomJSDriverPath() throws WebAutomationException {
		log.info("Inside WebDriverUtil.getPhatomJSDriverPath method");
		return FileUtils.getFilePath("src/main/resources/Web/Drivers/phantomjs.exe");
	}

	public static String getScreenShotPath() throws WebAutomationException {
		log.info("Inside WebDriverUtil.getScreenShotPath method");
		return FileUtils.getFilePath("src/main/resources/Screenshots/");
	}
		
	public static String getWiniumDriverPath() throws WebAutomationException {
		log.info("Inside DesktopDriverUtil.getWiniumDriverPath method");
		return FileUtils.getFilePath("src/main/resources/Web/Drivers/Winium.Desktop.Driver.exe");
	}
	
	public static String getMsEdgeDriverPath() throws WebAutomationException {
		log.info("Inside MSEdgeDriverUtil.getMsEdgeDriverPath method");
		return FileUtils.getFilePath("src/main/resources/Web/Drivers/msedgedriver.exe");
	}
}
