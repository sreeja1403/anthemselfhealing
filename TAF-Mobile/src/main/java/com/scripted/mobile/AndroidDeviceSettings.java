package com.scripted.mobile;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.remote.BrowserType;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.google.common.collect.ImmutableMap;

import io.appium.java_client.remote.MobileBrowserType;
import io.appium.java_client.remote.MobileCapabilityType;
import io.appium.java_client.remote.MobilePlatform;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.GeneralServerFlag;

public class AndroidDeviceSettings {
	private DesiredCapabilities capability = null;
	private AppiumDriverLocalService appiumService;
	private URL url = null;
	private String huburl = "http://127.0.0.1:4723/wd/hub";
	private String serverIp = "127.0.0.1";
	private String serverPort = "4723";
	private String appActivity = "";
	private String appPath = "";
	String browserName = "";

	public AndroidDeviceSettings() {
		this.capability = DesiredCapabilities.android();
		this.capability.setCapability(MobileCapabilityType.PLATFORM_NAME, MobilePlatform.ANDROID);
		this.capability.setCapability(MobileCapabilityType.DEVICE_NAME, "Android Emulator");
		this.capability.setCapability(MobileCapabilityType.HAS_TOUCHSCREEN, true);
		this.capability.setCapability(MobileCapabilityType.BROWSER_NAME, "");
		this.capability.setCapability(MobileCapabilityType.SUPPORTS_JAVASCRIPT, true);
		this.capability.setCapability(CapabilityType.TAKES_SCREENSHOT, true);
		this.capability.setCapability(MobileCapabilityType.SUPPORTS_NETWORK_CONNECTION, true);
		this.capability.setCapability("autoGrantPermissions", "true");
		// this.capability.setCapability(CapabilityType.PROXY, new
		// Proxy().setAutodetect(true));
		this.capability.setCapability(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR, UnexpectedAlertBehaviour.IGNORE);
		// this.capability.setCapability("takesHeapSnapshot", true);
		// this.capability.setCapability(MobileCapabilityType.ROTATABLE, true);
		this.capability.setCapability(MobileCapabilityType.ELEMENT_SCROLL_BEHAVIOR, false);
	}

	public AndroidDeviceSettings(DesiredCapabilities capability) {
		this.capability = capability;
	}

	public void setChromeBinaryPath(String path) {
		this.capability.setCapability(MobileCapabilityType.BROWSER_NAME, MobileBrowserType.CHROME);
		if (path != null && path.trim().length() > 0) {
			File file = new File(path);
			this.capability.setCapability("app", file.getAbsolutePath());
		}
		this.capability.setCapability("app-package", "com.android.chrome");
		this.capability.setCapability("app-activity", "com.google.android.apps.chrome.Main");
		//this.capability.setCapability("appium:chromeOptions", ImmutableMap.of("w3c", false));
	}

	public void setFirefoxBinaryPath(String path) {

		this.capability.setCapability(MobileCapabilityType.BROWSER_NAME, "Firefox");
		if (path != null && path.trim().length() > 0) {
			File file = new File(path);
			this.capability.setCapability("app", file.getAbsolutePath());
		}
		this.capability.setCapability("app-package", "org.mozilla.firefox");
		this.capability.setCapability("app-activity", ".App");
	}

	public void setDeviceName(String deviceName) {
		this.capability.setCapability(MobileCapabilityType.DEVICE_NAME, "Android");
		this.capability.setCapability("androidDeviceSerial", deviceName);
	}

	public void setDeviceID(String deviceID) {
		this.capability.setCapability(MobileCapabilityType.DEVICE_NAME, "Android");
		this.capability.setCapability("deviceID", deviceID);
	}

	public void setEmulator(String emulatorName) {
		this.capability.setCapability(CapabilityType.TAKES_SCREENSHOT, false);
		this.capability.setCapability("mobileEmulationEnabled", true);
		this.capability.setCapability("takesHeapSnapshot", false);
		this.capability.setCapability("takesScreenshot", false);
		this.capability.setCapability(MobileCapabilityType.DEVICE_NAME, emulatorName);
		this.capability.setCapability("androidDeviceSerial", "");
	}

	public void setBrowserName(String browserName) {
		if (browserName.equalsIgnoreCase("chrome")) {
			this.capability.setCapability(MobileCapabilityType.BROWSER_NAME, BrowserType.CHROME);
			this.capability.setCapability("appium:chromeOptions", ImmutableMap.of("w3c", false));
		} else
			this.capability.setCapability(MobileCapabilityType.BROWSER_NAME, "");
		/*
		 * this.capability.setCapability(MobileCapabilityType.BROWSER_NAME,
		 * browserName);
		 * this.capability.setCapability(MobileCapabilityType.ACCEPT_SSL_CERTS,true);
		 */
	}

	public void setAppActivity(String appActivity) {
		this.appActivity = appActivity;
		this.capability.setCapability("appActivity", appActivity);
	}

	public String getAppActivity() {
		return this.appActivity;
	}

	public void setAppPath(String appPath) {
		this.appPath = appPath;
		this.capability.setCapability(MobileCapabilityType.APP, appPath);
	}

	public String getAppPath() {
		return this.appPath;
	}

	public void setAppPackage(String appPackage) {
		this.capability.setCapability("appPackage", appPackage);
	}

	public void setServerIp(String ip) {
		this.serverIp = ip;
	}

	public void setServerPort(String port) {
		this.serverPort = port;
	}

	public void setAndroidVersion(String version) {
		version = version.trim().replaceAll("\\s", "");
		if (((version.length() > 2) && (Integer.parseInt(version.substring(0, 2)) < 42))
				|| ((version.length() == 1) && (Integer.parseInt(version) < 5))) {
			this.capability.setCapability(MobileCapabilityType.AUTOMATION_NAME, "Selendroid");
			this.capability.setCapability(MobileCapabilityType.PLATFORM_VERSION, version);
		} else {
			this.capability.setCapability(MobileCapabilityType.PLATFORM_VERSION, version);
			this.capability.setCapability(MobileCapabilityType.AUTOMATION_NAME, "UiAutomator2"); 
		}

	}

	public DesiredCapabilities getDesiredCapabilities() {
		return capability;
	}

	public void setHubUrl(URL url) {
		this.url = url;
	}

	public void setAppiumUrl(URL url) {
		this.url = url;
	}

	public URL getHubUrl() {
		try {
			if (this.serverIp.length() > 0)
				huburl = "http://" + this.serverIp + ":" + this.serverPort + "/wd/hub";
			if (this.url == null)
				this.url = new URL(huburl);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return this.url;
	}

	public void setBrowserCapabilities(DesiredCapabilities desiredCapabilities) {
		this.capability = desiredCapabilities;

	}

}
