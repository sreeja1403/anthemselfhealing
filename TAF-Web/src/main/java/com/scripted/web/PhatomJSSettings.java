package com.scripted.web;

import java.io.File;

import org.apache.log4j.Logger;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.Assert;

public class PhatomJSSettings {
	private DesiredCapabilities phantomCapabilities = null;
	public static Logger LOGGER = Logger.getLogger(PhatomJSSettings.class);
	public DesiredCapabilities setByPhatomJSSettings(File fileName) {
		try {
		 phantomCapabilities = new DesiredCapabilities();
		 phantomCapabilities.setJavascriptEnabled(true);
		 phantomCapabilities.setCapability("takesScreenshot", true);
		 phantomCapabilities.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY,
					WebDriverPathUtil.getPhatomJSDriverPath());
		}catch(Exception e)
		{
			e.printStackTrace();
			LOGGER.error("Error occurred while configuring PhatomJS browser , Exception: " + e);
			Assert.fail("Error occurred while configuring PhatomJS browser , Exception: " + e);
		}
		 return phantomCapabilities;
	}
	
	
	

	
}
