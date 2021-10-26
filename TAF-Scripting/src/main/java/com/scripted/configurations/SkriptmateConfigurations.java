package com.scripted.configurations;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scripted.dataload.PropertyDriver;
import com.scripted.generic.FileUtils;
import com.scripted.impactanalysis.impactReportGeneration;
import com.scripted.jsonParser.JsonEncryptor;
import com.scripted.jsonParser.JsonObjectParser;
import com.scripted.jsonParser.Project;
import com.scripted.jsonWriter.JsonHealingInfoWriter;
import com.scripted.reporting.selfhealing.ReportGenerationDummy;
import com.scripted.selfhealing.HealingConfig;
import com.scripted.selfhealing.SMWebHealer;
import com.scripted.selfhealing.SMMobHealer;

public class SkriptmateConfigurations {

	static String log4j_Path = "src\\main\\resources\\logConf\\log4j.properties";
	static String extentProFile_Path = "src\\main\\resources\\extent.properties";
	static String skrimateConfig_Path = "SkriptmateConfigurations\\SkriptmateConfig.Properties";
	static String logPath = "SkriptmateLogs\\Skriptmate.log";
	private static String cdir = System.getProperty("user.dir");
	static Boolean shFlag;
	static Boolean impactFlg;

	public static Logger LOGGER = Logger.getLogger(SkriptmateConfigurations.class);

	public static void beforeSuite() {
		try {

			Map<String, String> configMap = readConf();
			configMap.forEach((k, v) -> {
				if (k.equalsIgnoreCase("Skriptmate.Self.Healing")) {
					if (v.equalsIgnoreCase("true"))
						shFlag = true;
					else
						shFlag = false;

				}
				if (k.equalsIgnoreCase("Skriptmate.Impact.Report")) {
					if (v.equalsIgnoreCase("true"))
						impactFlg = true;
					else
						impactFlg = false;

				}
				if (k.equalsIgnoreCase("Skriptmate.allure.clear") && v.equalsIgnoreCase("true")) {
					File dirPath = new File(FileUtils.getCurrentDir() + "\\" + "allure-results");
					FileUtils.deleteDirectory(dirPath);
					File ocrDirPath = new File(cdir + "\\OCROutput" + "\\temp");
					FileUtils.deleteDirectory(ocrDirPath);
				}
				if (k.equalsIgnoreCase("Skriptmate.extent") && v.equalsIgnoreCase("true")) {
					try {
						// ExtentUtils.setExtentPropValue(extentProFile_Path);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				if (k.equalsIgnoreCase("Skriptmate.Jira.Xray.ExtractScenatio") && v.equalsIgnoreCase("true")) {
					/*
					 * try { //XrayAPIClient client = new XrayAPIClient(); PropertyDriver propDriver
					 * = new PropertyDriver();
					 * propDriver.setPropFilePath("src/main/resources/JiraXray/jiraXray.properties")
					 * ; String id = PropertyDriver.readProp("TC_id"); String[] arrOfId =
					 * id.split(",");
					 * 
					 * for (String a : arrOfId) XrayAPIClient.getScenario(a);
					 * 
					 * } catch (Exception e) {
					 * 
					 * e.printStackTrace(); }
					 */
				}
				// com.scripted.selfhealing.smHealer.setTime()

			});
			HealingConfig.setSlfHelngProp(shFlag,impactFlg );
		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	public static void afterSuite() {
		// try {
		Map<String, String> configMap = readConf();
		HealingConfig.afterSuite();
		// BrowserDriver.closeBrowser();

		// }

		// }
		// catch(Exception e)
		// {
		// System.out.println("Exception in aftersuite: "e);
		// }
		// ReportGeneration.reportWriter();
		/*
		 * try { configMap.forEach((k, v) -> { if
		 * (k.equalsIgnoreCase("Skriptmate.allure") && v.equalsIgnoreCase("true")) try {
		 * AllureReport.customizeReport(); } catch (Exception e) { e.printStackTrace();
		 * } if (k.equalsIgnoreCase("Skriptmate.extent") && v.equalsIgnoreCase("true"))
		 * { try { ExtentUtils.copyExtentLogo(); } catch (Exception e) {
		 * e.printStackTrace(); } }
		 * 
		 * if (k.equalsIgnoreCase("Skriptmate.Jira.Zephyr.ExecutionStatusUpdate") &&
		 * v.equalsIgnoreCase("true")) {
		 * JiraUtils.uploadResultsFromCucumberJson("target/cucumber.json"); } if
		 * (k.equalsIgnoreCase("Skriptmate.TestRail.ExecutionStatusUpdate") &&
		 * v.equalsIgnoreCase("true")) {
		 * TestRailUtils.uploadResultsFromCucumberJson("target/cucumber.json"); } if
		 * (k.equalsIgnoreCase("Skriptmate.Jira.Xray.ExecutionStatusUpdate") &&
		 * v.equalsIgnoreCase("true")) {
		 * XrayUtils.uploadResultsFromCucumberJson("target/cucumber.json"); } if
		 * (k.equalsIgnoreCase("Skriptmate.Report.enableMail") &&
		 * v.equalsIgnoreCase("true")) { try {
		 * //Sendmail.send("src/main/resources/Email/mail.properties",
		 * "target/cucumber.json"); } catch (IOException e) { e.printStackTrace(); } }
		 * 
		 * }); // Will kill all the open driver after the suite execution if any
		 * killDrivers(); } catch (Exception e) {
		 * 
		 * }
		 */
	}

	public static Map<String, String> readConf() {
		PropertyDriver propertyDriver = new PropertyDriver();
		propertyDriver.setPropFilePath(skrimateConfig_Path);
		return propertyDriver.readProp();
	}

	public static void setLog4jPropValue(String log4jPropFilePath) throws Exception {
		PropertiesConfiguration conf = new PropertiesConfiguration(log4jPropFilePath);
		conf.setProperty("log4j.appender.file.File", cdir + "\\" + logPath);
		conf.save();
	}

	public static void setlog4jConfig(String log4jPropFileLoc) throws Exception {
		String log4jPropFilePath = cdir + "\\" + log4jPropFileLoc;
		setLog4jPropValue(log4jPropFilePath);
		InputStream log4j = new FileInputStream(log4jPropFilePath);
		PropertyConfigurator.configure(log4j);
	}

	public static void killDrivers() {
		Process process;
		try {
			String line;
			String pidInfo = "";
			process = Runtime.getRuntime().exec(System.getenv("windir") + "\\system32\\" + "tasklist.exe");
			BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
			while ((line = input.readLine()) != null) {
				pidInfo += line;
			}
			input.close();
			if (pidInfo.contains("ChromeDriver.exe")) {
				Runtime.getRuntime().exec("taskkill /f /im ChromeDriver.exe");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
