package com.scripted.reporting;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.FileUtils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityModelProvider;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;

public class ExtentUtils {
	private static ExtentReports extent;
	private static ExtentHtmlReporter htmlReporter;
	public static ExtentTest test;
	private static String cdir = System.getProperty("user.dir");

	/**
	 * ************* Function to declare extent config *************
	 */
	public void setExtentConfig() {

		// Specify the report location
		String timeStamp = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
		htmlReporter = new ExtentHtmlReporter(
				System.getProperty("user.dir") + "/SkriptmateReport/Extent/" + timeStamp + "/Skripmatereport.html");
		htmlReporter.loadXMLConfig(System.getProperty("user.dir") + "/test-output/config.xml");

		extent = new ExtentReports();
		extent.attachReporter(htmlReporter);

		// Passing General information
		extent.setSystemInfo("OS Name", System.getProperty("os.name"));
		extent.setSystemInfo("User Name", System.getProperty("user.name"));
		extent.setSystemInfo("Time Zone", System.getProperty("user.timezone"));
		extent.setSystemInfo("Host name", "Spartans");
		extent.setSystemInfo("Environemnt", "QA");
	}

	/**
	 * ************* Function to close extent stream *************
	 */
	public static void closeExtent() {
		extent.flush();
	}

	/**
	 * ************* Function to set step as pass *************
	 * 
	 * @param message
	 */
	public static void eStepPass(String message) {
		test.log(Status.PASS, MarkupHelper.createLabel(message, ExtentColor.GREEN));
	}

	/**
	 * ************* Function to set step as info *************
	 * 
	 * @param message
	 */
	public static void eStepInfo(String message) {
		test.log(Status.INFO, MarkupHelper.createLabel(message, ExtentColor.CYAN));
	}

	/**
	 * ************* Function to set step as fail *************
	 * 
	 * @param message
	 */
	public static void addStepScreenshot(String message, MediaEntityModelProvider mediaModel) {
		test.fail(message, mediaModel);

	}

	/**
	 * Function to set step as skip
	 * 
	 * @param message
	 */
	public static void eStepSkip(String message) {
		test.log(Status.SKIP, MarkupHelper.createLabel(message, ExtentColor.TEAL));
	}

	/**
	 * Function to set step as fail
	 * 
	 * @param message
	 */
	public static void eStepFail(String message) {
		test.log(Status.FAIL, MarkupHelper.createLabel(message, ExtentColor.RED));
	}

	/**
	 * ************* Function to set step as debug *************
	 * 
	 * @param message
	 */
	public static void eStepDebug(String message) {
		test.log(Status.DEBUG, MarkupHelper.createLabel(message, ExtentColor.ORANGE));

	}

	/**
	 * ************* Function to create test *************
	 * 
	 * @param testName
	 */
	public static void eCreateTest(String testName) {
		test = extent.createTest(testName);
		ExtentListener.setTestObject(test);
	}

	/**
	 * ************* Function to create test step *************
	 * 
	 * @param stepName
	 */
	public static void eCreateStep(String stepName) {

		test.createNode(stepName);
		// test.eCreateStep("Login with Valid input");
	}

	/*public static void setExtentPropValue(String extentPropFilePath) throws Exception {
		PropertiesConfiguration conf = new PropertiesConfiguration(extentPropFilePath);
		String timeStamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm").format(new Date());
		conf.setProperty("extent.reporter.html.out",
				cdir + "\\SkriptmateReport\\Extent\\cucumberReport\\" + timeStamp + "_Report.html");
		conf.save();
	}*/

	public static void copyExtentLogo() throws Exception {
		File f1 = new File(cdir + "\\src\\main\\resources\\SkriptmateArtefacts\\Logo.png");
		File f2 = new File(cdir + "\\SkriptmateReport\\Extent\\Cucumber\\Logo.png");
		FileUtils.copyFile(f1, f2);
	}
}
