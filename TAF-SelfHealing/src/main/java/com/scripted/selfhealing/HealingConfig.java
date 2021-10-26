package com.scripted.selfhealing;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
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
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import junit.framework.Assert;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scripted.impactanalysis.impactReportGeneration;
import com.scripted.jsonParser.JsonEncryptor;
import com.scripted.jsonParser.JsonObjectParser;
import com.scripted.jsonParser.Project;
import com.scripted.jsonWriter.JsonHealingInfoWriter;
import com.scripted.reporting.selfhealing.ReportGenerator;
import com.scripted.reporting.selfhealing.ReportGenerationDummy;

public class HealingConfig {

	public static Boolean shFlg;
	public static Boolean impctFlg;

	public static String rptFldrNme;
	public static String rptPath;
	public static String jsonRptNme;

	static File filePath;

	public static ConcurrentHashMap<String, Project> jsonFilesMap = new ConcurrentHashMap<String, Project>();
	static String cdir = System.getProperty("user.dir");

	public static int staleTime;
	public static int timeoutTime;

	public static List<String> acptAlrtTxts = new ArrayList<String>();
	public static List<String> dsmsAlrtTxts = new ArrayList<String>();
	public static List<String> dsmsWndwTtls = new ArrayList<String>();
	public static List<String> JsonPaths = new ArrayList<String>();

	public static String usrInfo;
	public static String healPrpFlPath = "src/main/resources/HealingConfigurations/properties/SelfHealing.properties";
	public static String skyFlePath = "src/main/resources/HealingConfigurations/skyFiles/";
	public static String artfctPath = "/src/main/resources/HealingConfigurations/HealingReportArtefacts";
	public static String scrshtPath = "src/main/resources/HealingConfigurations/HealingReportArtefacts/screenshots";
	public static String ntwrkJson = "src/main/resources/HealingConfigurations/NetworkError.json";

	public static Logger LOGGER = Logger.getLogger(HealingConfig.class);

	public static void setSlfHelngProp(Boolean healFlag, Boolean impactFlag) {

		SMWebHealer smWebHealer = new SMWebHealer();
		SMMobHealer smMobHealer = new SMMobHealer();
		shFlg = healFlag;
		impctFlg = impactFlag;

		if (impctFlg) {
			smWebHealer.setImpactflag(true);
			smMobHealer.setImpactflag(true);
		}

		if (shFlg) {
			smWebHealer.setShflag(true);
			smMobHealer.setShflag(true);
		}

		if (shFlg || impctFlg) {
			Properties props = new Properties();
			try {
				props.load(new FileInputStream(healPrpFlPath));
				for (Enumeration<?> e = props.propertyNames(); e.hasMoreElements();) {
					String name = (String) e.nextElement();
					String value = props.getProperty(name);
					if (name.startsWith("acceptalert")) {
						acptAlrtTxts.add(value);
					} else if (name.startsWith("dismissalert")) {
						dsmsAlrtTxts.add(value);
					} else if (name.startsWith("dismisswindow")) {
						dsmsWndwTtls.add(value);
					} else if (name.equalsIgnoreCase("healing.user.info")) {
						usrInfo = value;
					} else if (name.equalsIgnoreCase("element.stale.polling.duration")) {
						staleTime = Integer.parseInt(value);
					} else if (name.equalsIgnoreCase("element.timeout.polling.duration")) {
						timeoutTime = Integer.parseInt(value);
					}
				}

			} catch (Exception e) {
				LOGGER.error("Exception " + e.getClass().getName() + " while reading property file : " + healPrpFlPath);
			}
			File dir = new File(skyFlePath);
			File[] files = dir.listFiles();
			for (File file : files) {
				JsonPaths.add(skyFlePath + file.getName());
			}
			HealingConfig healingConfig = new HealingConfig();
			ObjectMapper objectMapper = new ObjectMapper();
			if (!JsonPaths.isEmpty()) {
				for (String jsonPath : JsonPaths) {

					try {
						byte[] jsonData = healingConfig.decryptFile(jsonPath).getBytes();
						Project project = objectMapper.readValue(jsonData, Project.class);
						jsonFilesMap.put(jsonPath, project);
					} catch (Exception e) {
						LOGGER.error("Exception " + e.getClass().getName() + " while reading file in : " + skyFlePath);
					}

				}
			} else {
				LOGGER.error("No .sky files  available in : " + skyFlePath);
			}

		}

	}

	public static void reportPathCreate() {
		try {
			DateFormat dateFormatFolder = new SimpleDateFormat("dd_MM_yyyy_hh_mm_ss_ms_aa");
			String dateStringFolder = dateFormatFolder.format(new Date()).toString();
			if (shFlg) {
				rptFldrNme = "Report_" + dateStringFolder;
				rptPath = cdir + "/SkriptMateReport/HealingReport/" + rptFldrNme;
				// scrshtPath = rptPath + "/" + "screenshots";
				makeDirs(rptPath);
				// makeDirs(scrshtPath);
				File sourceFolder = new File(cdir + artfctPath);
				File destinationFolder = new File(rptPath);
				copyFolder(sourceFolder, destinationFolder);
			}
		} catch (Exception e) {
			LOGGER.error(
					"Exception " + e.getClass().getName() + " while creating self-healing report path : " + rptPath);
		}
	}

	private static void copyFolder(File sourceFolder, File destinationFolder) throws IOException {
		try {
			if (sourceFolder.isDirectory()) {
				if (!destinationFolder.exists()) {
					destinationFolder.mkdir();
				}
				String files[] = sourceFolder.list();
				for (String file : files) {
					File srcFile = new File(sourceFolder, file);
					File destFile = new File(destinationFolder, file);
					copyFolder(srcFile, destFile);
				}
			} else {
				Files.copy(sourceFolder.toPath(), destinationFolder.toPath(), StandardCopyOption.REPLACE_EXISTING);
			}
		} catch (Exception e) {
			LOGGER.error("Exception " + e.getClass().getName() + " while copying file from source : " + sourceFolder
					+ " to destination : " + destinationFolder);
		}
	}

	public String readFile(String jsonPath) {
		String fileContent = null;
		try {
			File file = new File(jsonPath);
			FileReader reader = new FileReader(file);
			char[] chars = new char[(int) file.length()];
			reader.read(chars);
			fileContent = new String(chars);
		} catch (Exception e) {
			LOGGER.error("Exception " + e.getClass().getName() + " while reading file content :" + jsonPath);
		}

		return fileContent;
	}

	public void encryptFile(String path) {
		try {
			String enc = JsonEncryptor.encryptPass(readFile(path));
			BufferedWriter writer = new BufferedWriter(new FileWriter(path));
			writer.write(enc);
			writer.close();
		} catch (Exception e) {
			LOGGER.error("Exception " + e.getClass().getName() + " due to invalid format of file : " + path);
		}
	}

	public String decryptFile(String path) {
		String dec = "";
		try {
			dec = JsonEncryptor.decryptPass(readFile(path));
		} catch (Exception e) {
			LOGGER.error("Exception " + e.getClass().getName() + " due to invalid format of file : " + path);
		}
		return dec;
	}

	public static String readProp(String key) {
		try {
			FileReader reader = new FileReader(filePath);
			Properties pf = new Properties();
			pf.load(reader);
			return pf.getProperty(key);
		} catch (Exception e) {
			LOGGER.error("Exception " + e.getClass().getName() + " while reading " + key + " from file :" + filePath);
			Assert.fail("Exception " + e.getClass().getName() + " while reading " + key + " from file :" + filePath);
			return null;
		}
	}

	public static String getFilePath(String fileName) {

		String filePath = cdir + "/" + fileName;
		try {
			File file = new File(filePath);
		} catch (Exception e) {
			LOGGER.error("Exception " + e.getClass().getName() + "Error while trying to read file path : " + filePath);
			Assert.fail("Exception " + e.getClass().getName() + "Error while trying to read file path : " + filePath);

		}
		return cdir + "/" + fileName;
	}

	public static void setPropFilePath(String filename) {
		File filePath = new File(getFilePath(filename));
		HealingConfig.filePath = filePath;
	}

	public static String makeDirs(String path) {
		String pattern = Pattern.quote(System.getProperty("file.separator"));
		String[] folders = new File(path).getAbsolutePath().split(pattern);
		String filePath = "";
		File newFile = null;
		try {
			for (String folder : folders) {
				filePath = filePath + System.getProperty("file.separator") + folder;
				newFile = new File(filePath);
				if (!newFile.exists() && !folder.contains(":") && !folder.contains(".")) {
					newFile.mkdir();
					waitforFile(newFile.getAbsolutePath(), 10000);
					if (!newFile.isDirectory()) {
						throw new RuntimeException("Unable to create parent directories of " + filePath);
					}
					LOGGER.info("Created : " + newFile.getAbsolutePath());
				}
			}
		} catch (Exception e) {
			LOGGER.error("Exception " + e.getClass().getName() + " while creating folder : " + filePath);
			Assert.fail("Exception " + e.getClass().getName() + " while creating folder : " + filePath);
		}

		return newFile.getAbsolutePath();
	}

	public static void waitforFile(String filename, long milliseconds) {
		long val = 0;
		File file = new File(filename);
		while (!file.exists()) {
			if (val >= milliseconds)
				new RuntimeException("File : " + filename + " Not Found.");
			try {
				Thread.sleep(100);
				val = val + 100;
			} catch (Exception e) {
				LOGGER.error("Exception " + e.getClass().getName() + " while waiting for file : " + filename);
				Assert.fail("Exception " + e.getClass().getName() + " while waiting for file : " + filename);
			}
		}
	}

	public static void afterSuite() {

		try {
			if (shFlg || impctFlg) {
				DateFormat dateFormatFolder = new SimpleDateFormat("dd_MM_yyyy_hh_mm_ss_ms_aa");
				String dateStringFolder = dateFormatFolder.format(new Date()).toString();
				jsonRptNme = "jsonResults_" + dateStringFolder;
				JsonHealingInfoWriter.jsonWriter();
			}

			if (shFlg) {
				if (SMWebHealer.healStsFlg || SMMobHealer.healStsFlg) {
					reportPathCreate();
					ReportGenerator reportGenJson = new ReportGenerator();
					reportGenJson.reportGen();
				}
				FileUtils.cleanDirectory(new File(scrshtPath));
			}
			if (impctFlg) {
				if (SMWebHealer.healStsFlg || SMMobHealer.healStsFlg) {
					impactReportGeneration impactRptGen = new impactReportGeneration(jsonRptNme);
				}
			}

			/*
			 * JsonObjectParser jsonparser = new JsonObjectParser();
			 * jsonparser.JsonUpdate();
			 */
		} catch (Exception e) {
			LOGGER.error("Exception " + e.getClass().getName() + " while generating self-healing reports");
		}
	}

}
