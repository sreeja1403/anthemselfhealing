package com.scripted.reporting;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;



import io.qameta.allure.Allure;
import junit.framework.Assert;

public class AllureReport {
	
	public static String screenshotPath = "";
	public static WebDriver driver = null;
	private static String cdir = System.getProperty("user.dir");
	public static Logger LOGGER = Logger.getLogger(AllureReport.class);
	public static String currentTimeStamp="";

	public static void addStep(String stepDescription) {
		Allure.step(stepDescription);
	}

	public static void addScreenshot(String screenShotname) {

		try {
			// Save screenshot in the SkriptMate Report
			driver = AllureListener.getDriver();
			TakesScreenshot scrShot = ((TakesScreenshot) driver);
			File SrcFile = scrShot.getScreenshotAs(OutputType.FILE);
			String timeStamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm").format(new Date());
			screenshotPath = cdir + "/src/main/resources/Screenshots/" + timeStamp + File.separator;
			com.scripted.generic.FileUtils.makeDirs(screenshotPath);
			File DestFile = new File(screenshotPath + SrcFile.getName());
			FileUtils.copyFile(SrcFile, DestFile);
			Path content = Paths.get(DestFile.getPath());
			try (InputStream is = Files.newInputStream(content)) {
				Allure.addAttachment(screenShotname, is);
			}
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("Error while saving screenshot in the SkriptMate Report ");
			Assert.fail("Error while saving screenshot in the SkriptMate Report ");
		}
	}

	

	public static void customizeReport() throws Exception {
		try {
			// Customize the Skriptmate Report
			LOGGER.info("Generating Skriptmate Allure Report");
			String workingDir = com.scripted.generic.FileUtils.getCurrentDir();
			String reportGenFolder = "SkriptmateReport/Allure";
			currentTimeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
			String filePath = workingDir + File.separator + reportGenFolder + File.separator + currentTimeStamp
					+ "/widgets/environment.json";
			
			// Execute the command to generate allure report
			String command = "allure generate -c " + "\"" + workingDir + "/allure-results" + "\"" + " -o " + "\""
					+ workingDir + File.separator + reportGenFolder + File.separator + currentTimeStamp + File.separator;
			final Process p = Runtime.getRuntime().exec(String.format("cmd /c %s", command));
			final ProcessResultReader stderr = new ProcessResultReader(p.getErrorStream(), "STDERR");
			final ProcessResultReader stdout = new ProcessResultReader(p.getInputStream(), "STDOUT");
			stderr.start();
			stdout.start();
			LOGGER.info("------------------Generating Allure Report---------------");
			final int exitValue = p.waitFor();
			if (exitValue == 0) {
			
			LOGGER.info("Report Folder " + reportGenFolder + File.separator + currentTimeStamp);
			
			com.scripted.generic.FileUtils.waitforFile(
					workingDir + File.separator + reportGenFolder + File.separator + currentTimeStamp +File.separator + "styles.css", 1000);

			com.scripted.generic.FileUtils
					.deleteFile(workingDir + File.separator + reportGenFolder + File.separator + currentTimeStamp + "/styles.css");
			com.scripted.generic.FileUtils.fileCopy("src/main/resources/Reporting/Artefacts/styles.css",
					workingDir + File.separator + reportGenFolder + File.separator + currentTimeStamp + "/styles.css");

			File f1 = new File("src/main/resources/Reporting/Artefacts/Logo.png");
			File f2 = new File(workingDir + File.separator + reportGenFolder + File.separator + currentTimeStamp + "/Logo.png");
			FileUtils.copyFile(f1, f2);

			summaryJsonEdit(workingDir + File.separator + reportGenFolder + File.separator + currentTimeStamp);

			String apackageName = "Skriptmate Package";
			String start = "---";
			String end = "---";
			String totalcount = "---";
			String passedcount = "---";
			String failedcount = "---";
			String bname = "Skriptmate";

			StringBuffer sb = new StringBuffer();
			sb.append("[{");
			sb.append("\n");
			sb.append("\"name\"");
			sb.append(" :\"" + "Project Name :" + apackageName + "\"");
			sb.append("\n");
			 sb.append(" }");

			sb.append(",{");

			sb.append("\n");
			sb.append("\"name\"");
			sb.append(" :\"" + "Start Date and Time :" + start + "\"");
			sb.append("\n");
			sb.append(" }");

			sb.append(",{");

			sb.append("\n");
			sb.append("\"name\"");
			sb.append(" :\"" + "End Date and Time :" + end + "\"");
			sb.append("\n");
			 sb.append(" }");

			sb.append(",{");

			sb.append("\n");
			sb.append("\"name\"");
			sb.append(" :\"" + "Total Number of Test Cases :" + totalcount + "\"");
			sb.append("\n");
			 sb.append(" }");

			sb.append(",{");

			sb.append("\n");
			sb.append("\"name\"");
			sb.append(" :\"" + "Passed :" + passedcount + "\"");
			sb.append("\n");
			sb.append(" }");

			sb.append(",{");

			sb.append("\n");
			sb.append("\"name\"");
			sb.append(" :\"" + "Failed :" + failedcount + "\"");
			sb.append("\n");
			 sb.append(" }");

			sb.append(",{");

			sb.append("\n");
			sb.append("\"name\"");
			sb.append(" :\"" + "Execution browser :" + bname + "\"");
			sb.append("\n");
			 sb.append("\n");

			sb.append("}]");

			
			Files.write(new File(filePath).toPath(), sb.toString().getBytes());
			LOGGER.info("Skriptmate Allure Report Generated Successfully");
			}
			else{
				String errMsg = stderr.toString();
				LOGGER.info("Error Message :" +errMsg);
			}
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("Error while generating skriptMate Allure report" + e.getMessage());
			Assert.fail("Error while generating skriptMate Allure report" + e.getMessage());
		}

	}

	public static String getCurrentTimeStamp() {
		return currentTimeStamp;
	}

	

	public static void summaryJsonEdit(String reportPath) {
		try {

			JSONParser parser = new JSONParser();
			String summaryJsonPath = reportPath + "/widgets/summary.json";
			com.scripted.generic.FileUtils.waitforFile(summaryJsonPath, 1000);

			Reader reader = new FileReader(summaryJsonPath);

			JSONObject jsonObject = (JSONObject) parser.parse(reader);
			jsonObject.put("reportName", "Skriptmate Allure Report");

			FileWriter file = new FileWriter(summaryJsonPath);
			file.write(jsonObject.toString());
			file.flush();
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("Error while summarizing Skriptmate Allure Report");
			Assert.fail("Error while summarizing Skriptmate Allure Report");
		}
	}

}
class ProcessResultReader extends Thread {
	final InputStream is;
	final String type;
	final StringBuilder sb;
	public static Logger LOGGER = Logger.getLogger(ProcessResultReader.class);
	ProcessResultReader(final InputStream is, String type) {
		this.is = is;
		this.type = type;
		this.sb = new StringBuilder();
	}              

	public void run() {
		try {
			final InputStreamReader isr = new InputStreamReader(is);
			final BufferedReader br = new BufferedReader(isr);
			String line = null;
			while ((line = br.readLine()) != null) {
				this.sb.append(line).append("\n");
			}
		} catch (final IOException ioe) {
			System.err.println(ioe.getMessage());
			LOGGER.error("Error while generating the report ");
			throw new RuntimeException(ioe);
		}
	}

	@Override
	public String toString() {
		return this.sb.toString();
	}
}

class IllegalCommandException extends Exception {
	private static final long serialVersionUID = 1L;

	public IllegalCommandException() {
	}
}

