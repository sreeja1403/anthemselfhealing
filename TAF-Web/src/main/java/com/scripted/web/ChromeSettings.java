package com.scripted.web;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;

public class ChromeSettings {
	public static Logger LOGGER = Logger.getLogger(ChromeSettings.class);
	private ChromeOptions chromeOptionsObj = new ChromeOptions();
	Map<String, Object> chromePrefs = new HashMap<String, Object>();
	List<String> chromeOptions = new ArrayList<>();

	public ChromeOptions setBychromeOptions(File fileName) {
		try {
		System.setProperty("webdriver.chrome.driver", WebDriverPathUtil.getChromeDriverPath());
		chromePrefs.put("profile.default_content_settings.popups", 0);
		chromePrefs.put("profile.content_settings.pattern_pairs.*.multiple-automatic-downloads", 1);
		chromePrefs.put("download.prompt_for_download", false);
		chromePrefs.put("credentials_enable_service", false);
		chromePrefs.put("password_manager_enabled", false);
		chromeOptions.add("disable-plugins");
		chromeOptions.add("disable-extensions");
		chromeOptions.add("allow-running-insecure-content");
		chromeOptions.add("ignore-certificate-errors");
		chromeOptions.add("--always-authorize-plugins");
//		chromeOptions.add("--disable-notifications");
		chromeOptions.add("disable-infobars");
		chromeOptions.add("--test-type");
		setChromeOptionsPropFile(fileName);
		chromeOptionsObj.addArguments(chromeOptions);
		chromeOptionsObj.setExperimentalOption("prefs", chromePrefs);
		
		}catch(Exception e)
		{
			LOGGER.error("Error occurred while initialising chrome browser, Exception :"+e);
			e.printStackTrace();
			Assert.fail("Error occurred while initialising chrome browser, Exception :"+e);
		}
		return this.chromeOptionsObj;
	}

	public void setChromeOptionsPropFile(File fileName) {
		Properties props = new Properties();
		try {
			props.load(new FileInputStream(fileName));
			if (props.getProperty("chrome.chromeoptions") != null) {
				String[] chromeOptionspropfile = props.getProperty("chrome.chromeoptions").split(",");
				Collections.addAll(chromeOptions, chromeOptionspropfile);
				chromeOptions = chromeOptions.stream().distinct().collect(Collectors.toList());
			}
			for (Enumeration<?> e = props.propertyNames(); e.hasMoreElements();) {
				String name = (String) e.nextElement();
				String value = props.getProperty(name);
				if (name.toLowerCase().contains("chromeprefs") && !(chromePrefs
						.containsKey(StringUtils.substringAfter(name.toLowerCase(), "chrome.chromeprefs.")))) {
					this.chromePrefs.put(StringUtils.substringAfter(name.toLowerCase(), "chrome.chromeprefs."), value);
				}
			}
		} catch (Exception e) {
			LOGGER.error("Error occurred while initialising chrome browser, Exception :"+e);
			e.printStackTrace();
			Assert.fail("Error occurred while initialising chrome browser, Exception :"+e);
		}
	}

}
