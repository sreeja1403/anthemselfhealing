package com.scripted.mobile;

import io.appium.java_client.remote.MobileCapabilityType;
import io.appium.java_client.remote.MobilePlatform;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.log4j.Logger;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

public class IOSDeviceSettings {
	private DesiredCapabilities capability = null;
	private AppiumDriverLocalService appiumService;
	private URL url = null;
	private String huburl = "http://127.0.0.1:4723/wd/hub";

	public IOSDeviceSettings() {
		this.capability = new DesiredCapabilities();
		this.capability.setCapability(MobileCapabilityType.PLATFORM_NAME, MobilePlatform.IOS);
		this.capability.setCapability(MobileCapabilityType.AUTOMATION_NAME, "XCUITest");
		// this.capability.setCapability(MobileCapabilityType.HAS_TOUCHSCREEN,
		// true);
		this.capability.setCapability("autoGrantPermissions", "true");
		// this.capability.setCapability(CapabilityType.PROXY, new
		// Proxy().setAutodetect(true));
		this.capability.setCapability(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR, UnexpectedAlertBehaviour.IGNORE);
		// this.capability.setCapability("browserConnectionEnabled", true);
		// this.capability.setCapability("takesHeapSnapshot", true);
		// this.capability.setCapability(MobileCapabilityType.ROTATABLE, true);
		// this.capability.setCapability(MobileCapabilityType.ELEMENT_SCROLL_BEHAVIOR,
		// true);
	}

	public IOSDeviceSettings(DesiredCapabilities capability) {
		this.capability = capability;
	}

	public void setDeviceUDID(String udid) {
		this.capability.setCapability("udid", udid);
	}

	public void setDeviceName(String deviceName) {
		this.capability.setCapability(MobileCapabilityType.DEVICE_NAME, deviceName);
	}

	public void setBrowserName(String browserName) {

		if (browserName.length() > 1 && !browserName.toLowerCase().startsWith("s")) {
			this.capability.setCapability(MobileCapabilityType.BROWSER_NAME, browserName);
			this.capability.setCapability(MobileCapabilityType.SUPPORTS_JAVASCRIPT, true);
			this.capability.setCapability(CapabilityType.TAKES_SCREENSHOT, true);
			this.capability.setCapability(MobileCapabilityType.SUPPORTS_NETWORK_CONNECTION, true);
			this.capability.setCapability(MobileCapabilityType.ACCEPT_SSL_CERTS, true);
		}
	}

	public void setAppPath(String appPath) {
		this.capability.setCapability(MobileCapabilityType.APP, appPath);
	}

	public void setAppBundleId(String appPackage) {
		this.capability.setCapability("bundleId", appPackage);
	}

	public void setIOSVersion(String version) {
		if (version != null)
			this.capability.setCapability(MobileCapabilityType.PLATFORM_VERSION, version);
	}
	
	public String getHuburl() {
		return huburl;
	}

	public void setHuburl(String huburl) {
		this.huburl = huburl;
	}

	public DesiredCapabilities getDesiredCapabilities() {
		return capability;
	}
}
