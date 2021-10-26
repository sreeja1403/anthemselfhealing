package com.scripted.mobile;

import java.io.File;
import java.io.FileReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.google.common.collect.ImmutableMap;
import com.ssts.pcloudy.Connector;
import com.ssts.pcloudy.Version;
import com.ssts.pcloudy.appium.PCloudyAppiumSession;
import com.ssts.pcloudy.dto.appium.booking.BookingDtoDevice;
import com.ssts.pcloudy.dto.device.MobileDevice;
import com.ssts.pcloudy.dto.file.PDriveFileDTO;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;

public class PcloudyDeviceSettings {

	public static Logger LOGGER = Logger.getLogger(PcloudyDeviceSettings.class);
	public static AndroidDriver<WebElement> androidDriver = null;

	public static IOSDriver<WebElement> iosDriver = null;
	public static PCloudyAppiumSession pCloudySession;
	DesiredCapabilities capability =null;
	
public static ThreadLocal<AndroidDriver<WebElement>> thDriver = new ThreadLocal<AndroidDriver<WebElement>>();

	public PcloudyDeviceSettings() {
		this.capability = new DesiredCapabilities();
	}
	

	public static AndroidDriver<WebElement> getConnectionByDeviceVersion(Properties mobConfigProp) {
		boolean autoSelectDevices = false;
		ArrayList<MobileDevice> selectedDevices = new ArrayList<>();
		URL appiumEndpoint;
		try {
			Connector con = new Connector("https://device.pcloudy.com/api/");
			String authToken = con.authenticateUser(mobConfigProp.getProperty("Username"),
					mobConfigProp.getProperty("ApiKey"));
			File fileToBeUploaded = new File(mobConfigProp.getProperty("appPath"));
			PDriveFileDTO alreadyUploadedApp = con.getAvailableAppIfUploaded(authToken, fileToBeUploaded.getName());
			if (alreadyUploadedApp == null) {
				System.out.println("Uploading App: " + fileToBeUploaded.getAbsolutePath());
				PDriveFileDTO uploadedApp = con.uploadApp(authToken, fileToBeUploaded, false);
				System.out.println("App uploaded");
				alreadyUploadedApp = new PDriveFileDTO();
				alreadyUploadedApp.file = uploadedApp.file;
			} else {
				System.out.println("App already present. Not uploading... ");
			}

			selectedDevices.addAll(
					con.chooseDevices(authToken, "android", new Version(mobConfigProp.getProperty("DeviceVersion_Min")),
							new Version(mobConfigProp.getProperty("DeviceVersion_Max")), 1));

			// Book the selected devices in pCloudy
			String sessionName = "Appium Session " + new Date();
			BookingDtoDevice bookedDevice = con.AppiumApis().bookDevicesForAppium(authToken, selectedDevices, 2,
					sessionName)[0];
			System.out.println("Devices booked successfully");

			con.AppiumApis().initAppiumHubForApp(authToken, alreadyUploadedApp);
			pCloudySession = new PCloudyAppiumSession(con, authToken, bookedDevice);

			DesiredCapabilities capabilities = new DesiredCapabilities();
			capabilities.setCapability("newCommandTimeout", mobConfigProp.getProperty("newCommandTimeout"));
			capabilities.setCapability("launchTimeout", mobConfigProp.getProperty("launchTimeout"));
			capabilities.setCapability("deviceName", pCloudySession.getDto().capabilities.deviceName);
			capabilities.setCapability("platformName", mobConfigProp.getProperty("platformName"));
			capabilities.setCapability("appPackage", mobConfigProp.getProperty("appPackage"));
			capabilities.setCapability("appActivity", mobConfigProp.getProperty("appActivity"));
			capabilities.setCapability("rotatable", true);
			appiumEndpoint = pCloudySession.getConnector().AppiumApis()
					.getAppiumEndpoint(pCloudySession.getAuthToken());
			androidDriver = new AndroidDriver<WebElement>(appiumEndpoint, capabilities);
		} catch (Exception e) 
		{
			e.printStackTrace();
		}

		return androidDriver;

	}

	public AndroidDriver<WebElement> getConnectionPcloudyAndroidNativeApp(Properties mobConfigProp) {
		try {
			capability.setCapability("pCloudy_Username", mobConfigProp.getProperty("Username"));
			capability.setCapability("pCloudy_ApiKey", mobConfigProp.getProperty("ApiKey"));
			capability.setCapability("pCloudy_ApplicationName", mobConfigProp.getProperty("appName"));
			capability.setCapability("pCloudy_DurationInMinutes", mobConfigProp.getProperty("DurationInMinutes"));
			capability.setCapability("pCloudy_DeviceManafacturer", mobConfigProp.getProperty("DeviceManafacturer"));
			capability.setCapability("pCloudy_DeviceVersion", mobConfigProp.getProperty("DeviceVersion"));
			capability.setCapability("pCloudy_DeviceFullName", mobConfigProp.getProperty("DeviceFullName"));
			capability.setCapability("newCommandTimeout", mobConfigProp.getProperty("newCommandTimeout"));
			capability.setCapability("launchTimeout", mobConfigProp.getProperty("launchTimeout"));
			capability.setCapability("appPackage", mobConfigProp.getProperty("appPackage"));
			capability.setCapability("appActivity", mobConfigProp.getProperty("appActivity"));
			capability.setBrowserName("");
			androidDriver = new AndroidDriver<WebElement>(new URL("https://device.pcloudy.com/appiumcloud/wd/hub"),
					capability);
			androidDriver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return androidDriver;
	}
	


	public AndroidDriver<WebElement> getConnectionPcloudyAndroidWeb(Properties mobConfigProp) {
		try {
			capability.setCapability("pCloudy_Username", mobConfigProp.getProperty("Username"));
			capability.setCapability("pCloudy_ApiKey", mobConfigProp.getProperty("ApiKey"));
			capability.setCapability("pCloudy_DurationInMinutes", mobConfigProp.getProperty("DurationInMinutes"));
			capability.setCapability("pCloudy_DeviceManafacturer", mobConfigProp.getProperty("DeviceManafacturer"));
			capability.setCapability("pCloudy_DeviceVersion", mobConfigProp.getProperty("DeviceVersion"));
			capability.setCapability("pCloudy_DeviceFullName", mobConfigProp.getProperty("DeviceFullName"));
			capability.setCapability("newCommandTimeout", mobConfigProp.getProperty("newCommandTimeout"));
			capability.setCapability("launchTimeout", mobConfigProp.getProperty("launchTimeout"));
			capability.setCapability("automationName", "UiAutomator2");
			capability.setCapability("appium:chromeOptions",ImmutableMap.of("w3c",false));
			capability.setCapability("pCloudy_WildNet","false");
			capability.setBrowserName(mobConfigProp.getProperty("browserName"));
			androidDriver = new AndroidDriver<WebElement>(new URL("https://device.pcloudy.com/appiumcloud/wd/hub"),
					capability);
			androidDriver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return androidDriver;
	}
	

	public void getConnectionPcloudyAndroidWebTh(String mobConfigPropf) {
		Properties mobConfigProp = new Properties();
		try {
			System.out.println(mobConfigPropf);
			FileReader reader = new FileReader("src/main/resources/MobileConfigurations/"+mobConfigPropf+".properties");
			mobConfigProp.load(reader);
			capability.setCapability("pCloudy_Username", mobConfigProp.getProperty("Username"));
			capability.setCapability("pCloudy_ApiKey", mobConfigProp.getProperty("ApiKey"));
			capability.setCapability("pCloudy_DurationInMinutes", mobConfigProp.getProperty("DurationInMinutes"));
			capability.setCapability("pCloudy_DeviceManafacturer", mobConfigProp.getProperty("DeviceManafacturer"));
			capability.setCapability("pCloudy_DeviceVersion", mobConfigProp.getProperty("DeviceVersion"));
			capability.setCapability("newCommandTimeout", mobConfigProp.getProperty("newCommandTimeout"));
			capability.setCapability("launchTimeout", mobConfigProp.getProperty("launchTimeout"));
			capability.setCapability("pCloudy_DeviceFullName", mobConfigProp.getProperty("DeviceFullName"));
			capability.setCapability("automationName", "UiAutomator2");
			capability.setCapability("appium:chromeOptions",ImmutableMap.of("w3c",false));
			capability.setCapability("pCloudy_WildNet","false");
			capability.setBrowserName("Chrome");
			thDriver.set(new AndroidDriver<WebElement>(new URL("https://device.pcloudy.com/appiumcloud/wd/hub"),
					capability));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}























	public IOSDriver<WebElement> getConnectionPcloudyIOSNativeApp(Properties mobConfigProp ) {
		try {
			capability.setCapability("pCloudy_Username",mobConfigProp.getProperty("Username"));
			capability.setCapability("pCloudy_ApiKey",mobConfigProp.getProperty("ApiKey") );
			capability.setCapability("pCloudy_ApplicationName", mobConfigProp.getProperty("appName"));
			capability.setCapability("pCloudy_DeviceManafacturer", mobConfigProp.getProperty("DeviceManafacturer"));
			capability.setCapability("automationName", "XCUITest");
			capability.setCapability("pCloudy_DeviceVersion",mobConfigProp.getProperty("DeviceVersion"));
			capability.setCapability("pCloudy_DurationInMinutes",mobConfigProp.getProperty("DurationInMinutes"));
			capability.setCapability("newCommandTimeout", mobConfigProp.getProperty("newCommandTimeout"));
			capability.setCapability("launchTimeout", mobConfigProp.getProperty("launchTimeout"));
			capability.setCapability("bundleId",  mobConfigProp.getProperty("bundleId"));
			capability.setCapability("pCloudy_DeviceFullName", mobConfigProp.getProperty("DeviceFullName"));
			capability.setCapability("platformVersion", "13.4.1");
			System.out.println("Capabilities :"+capability);
			iosDriver = new IOSDriver<WebElement>(new URL("https://device.pcloudy.com/appiumcloud/wd/hub"),
					capability);
			iosDriver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return iosDriver;
	}
	
	public IOSDriver<WebElement> getConnectionPcloudyIOSWeb(Properties mobConfigProp){
		try {
			capability.setCapability("pCloudy_Username",mobConfigProp.getProperty("Username"));
			capability.setCapability("pCloudy_ApiKey",mobConfigProp.getProperty("ApiKey") );
			capability.setCapability("pCloudy_DeviceManafacturer", mobConfigProp.getProperty("DeviceManafacturer"));
			capability.setCapability("pCloudy_DeviceVersion",mobConfigProp.getProperty("DeviceVersion"));
		//	capability.setCapability("pCloudy_DeviceVersion",mobConfigProp.getProperty("DeviceVersion"));
			capability.setCapability("pCloudy_DurationInMinutes",mobConfigProp.getProperty("DurationInMinutes"));
			capability.setCapability("newCommandTimeout", mobConfigProp.getProperty("newCommandTimeout"));
			capability.setCapability("platformVersion","13.3");
			//capability.setCapability("browserName",mobConfigProp.getProperty("browserName"));
			//capability.setBrowserName(mobConfigProp.getProperty("browserName"));		
			capability.setBrowserName("Safari");			
			capability.setCapability("automationName", "XCUITest");
			iosDriver = new IOSDriver<WebElement>(new URL("https://device.pcloudy.com/appiumcloud/wd/hub"),
					capability);
			iosDriver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return iosDriver;
	}
	

	public static PCloudyAppiumSession getAppiumPCloudySession() {
		return pCloudySession;
	}
	
	
	
	public DesiredCapabilities getPcloudyCapabilities() {
		return capability;
	}

}
