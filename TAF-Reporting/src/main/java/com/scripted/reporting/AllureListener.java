package com.scripted.reporting;

import org.apache.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import io.qameta.allure.Attachment;

public class AllureListener implements ITestListener {

	public static WebDriver driver = null;
	public static String screenshotPath = "";
	private static String cdir = System.getProperty("user.dir");
	public static Logger LOGGER = Logger.getLogger(AllureListener.class);

	public static WebDriver getDriver() {
		return driver;
	}

	public static void setDriver(WebDriver driver) {
		AllureListener.driver = driver;
	}

	private static String getTestMethodName(ITestResult iTestResult) {
		return iTestResult.getMethod().getConstructorOrMethod().getName();
	}

	// Text attachments for Allure
	@Attachment(value = "Page screenshot", type = "image/png")
	public byte[] saveScreenshotPNG(WebDriver driver) throws Exception {
		return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
	}

	// Text attachments for Allure
	@Attachment(value = "{0}", type = "text/plain")
	public static String saveTextLog(String message) {
		return message;
	}

	// HTML attachments for Allure
	@Attachment(value = "{0}", type = "text/html")
	public static String attachHtml(String html) {
		return html;
	}

	@Override
	public void onTestFailure(ITestResult iTestResult) {
		LOGGER.info("I am in onTestFailure method " + getTestMethodName(iTestResult) + " failed");

		// Get driver from BaseTest and assign to local webdriver variable.
		Object testClass = iTestResult.getInstance();

		// Allure ScreenShotRobot and SaveTestLog
		if (driver instanceof WebDriver) {
			LOGGER.info("Screenshot captured for test case:" + getTestMethodName(iTestResult));
			try {
				saveScreenshotPNG(AllureListener.driver);
				saveTextLog("Log");
			} catch (Exception e) {
				e.printStackTrace();
				LOGGER.error("Error while saving screenshot "+"Exception :"+e);
				
			}
		}
	}

	@Override
	public void onTestFailedButWithinSuccessPercentage(ITestResult iTestResult) {
		LOGGER.info("Test failed but it is in defined success ratio " + getTestMethodName(iTestResult));
	}

	@Override
	public void onTestStart(ITestResult result) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTestSuccess(ITestResult result) {
		// TODO Auto-generated method stub
		LOGGER.info("Inside Test Success");
		try {
			saveTextLog("Log");
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("Error while saving Textlog "+"Exception :"+e);
		}

	}

	@Override
	public void onTestSkipped(ITestResult result) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStart(ITestContext context) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onFinish(ITestContext context) {
		// TODO Auto-generated method stub

	}

}
