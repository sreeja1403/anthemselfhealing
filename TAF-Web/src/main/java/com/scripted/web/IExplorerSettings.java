package com.scripted.web;

import java.io.File;
import java.io.FileInputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.testng.Assert;

public class IExplorerSettings {
	public static Logger LOGGER = Logger.getLogger(IExplorerSettings.class);
	private InternetExplorerOptions ieOptions = new InternetExplorerOptions();
	Map<String, Object> ieExplorerOpt = new HashMap<String, Object>();

	public InternetExplorerOptions setByIExplorerOptions(File fileName) {
		try {
		System.setProperty("webdriver.ie.driver", WebDriverPathUtil.getIEDriverPath());
		ieExplorerOpt.put("CapabilityType.ACCEPT_SSL_CERTS", true);
		ieExplorerOpt.put("CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR", UnexpectedAlertBehaviour.IGNORE);
		ieExplorerOpt.put("CapabilityType.UNHANDLED_PROMPT_BEHAVIOUR", true);
		ieExplorerOpt.put("javascriptEnabled", true);
		setIEOptionsPropFile(fileName);
		}catch(Exception e)
		{
			e.printStackTrace();
			LOGGER.error("Error occurred while configuring IExplorer , Exception: " + e);
			Assert.fail("Error occurred while configuring IExplorer , Exception: " + e);
		}
		return ieOptions;
	}

	public void setIEOptionsPropFile(File fileName) {
		Properties props = new Properties();
		try {
			props.load(new FileInputStream(fileName));
			for (Enumeration<?> e = props.propertyNames(); e.hasMoreElements();) {
				String name = (String) e.nextElement();
				String value = props.getProperty(name);
				String capabilityName = StringUtils.substringAfter(name.toLowerCase(), "ie.ieoptions.");
				if (name.toLowerCase().contains("ieoptions")) {
					ieExplorerOpt.put(StringUtils.substringAfter(name.toLowerCase(), "ie.ieoptions."), value);
				}
			}
			this.ieOptions = setIEOptionsFromMap(ieExplorerOpt);
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("Error occurred while configuring IExplorer , Exception: " + e);
			Assert.fail("Error occurred while configuring IExplorer , Exception: " + e);
		}
	}

	public InternetExplorerOptions setIEOptionsFromMap(Map<String, Object> ieOptsMap) {
		InternetExplorerOptions ieOptions = new InternetExplorerOptions();
		try {
			ieOptsMap.forEach((k, v) -> ieOptions.setCapability(k, v));
			ieOptions.ignoreZoomSettings();
			ieOptions.destructivelyEnsureCleanSession();
			ieOptions.requireWindowFocus();
			ieOptions.introduceFlakinessByIgnoringSecurityDomains();
			ieOptions.enablePersistentHovering();
		} catch (Exception e) {
			LOGGER.error("Error while setting capabilities for IE" + e);
			Assert.fail("Error while setting capabilities for IE" + e);
		}
		return ieOptions;
	}

}
