package com.scripted.dataload;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;

import com.genRocket.engine.EngineAPI;
import com.genRocket.engine.EngineManual;
import com.scripted.generic.FileUtils;

import junit.framework.Assert;

public class GenRocketDriver {
	public static Logger LOGGER = Logger.getLogger(GenRocketDriver.class);
	private static String cdir = System.getProperty("user.dir");
	private static String genOutPutDir = cdir + "/src/main/resources/GenRocketOutput/";

	public static void executeGenSceFile(String scenarioFileName) {
		try {
			String scenarioPath = cdir + "/src/main/resources/GenRocketScenarios/" + scenarioFileName;
			EngineAPI api = new EngineManual(); // Initializing the EngineAPI
			LOGGER.info("----Loading GenRocket Scenario---");
			api.scenarioLoad(scenarioPath); // Loading the scenario file
			LOGGER.info("----GenRocket Scenario loaded successfully---");
			api.scenarioRun(); // Running the scenario
			LOGGER.info("----Successfully ran GenRocket Scenario---");
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("Error while performing GenRocket scenarios "+"Exception: "+e);
			Assert.fail("Error while performing GenRocket scenarios "+"Exception: "+e);
		}
	}

	public static void executeGenSceFile(String scenarioFileName, String domainName, String receiverName) {
		try {
			String scenarioPath = cdir + "/src/main/resources/GenRocketScenarios/" + scenarioFileName;
			EngineAPI api = new EngineManual(); // Initializing the EngineAPI
			LOGGER.info("----Loading GenRocket Scenario---");
			api.scenarioLoad(scenarioPath); // Loading the scenario file
			LOGGER.info("----GenRocket Scenario loaded successfully---");
			api.receiverParameterSet(domainName, receiverName, "path", genOutPutDir);
			api.scenarioRun(); // Running the scenario
			LOGGER.info("----Successfully ran GenRocket Scenario---");
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("Error while performing GenRocket scenarios "+"Exception: "+e);
			Assert.fail("Error while performing GenRocket scenarios "+"Exception: "+e);
		}
	}

	public static void updateSceLoopCount(String scenarioFileName, String domainName, String receiverName,
			String count) {
		try {
			String scenarioPath = cdir + "/src/main/resources/GenRocketScenarios/" + scenarioFileName;
			EngineAPI api = new EngineManual(); // Initializing the EngineAPI
			LOGGER.info("----Loading GenRocket Scenario---");
			api.scenarioLoad(scenarioPath);
			LOGGER.info("----GenRocket Scenario loaded successfully---");
			api.domainSetLoopCount(domainName, count);
			api.receiverParameterSet(domainName, receiverName, "path", genOutPutDir);
			api.scenarioRun(); // Running the scenario
			LOGGER.info("----Successfully ran GenRocket Scenario---");
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("Error while performing GenRocket scenarios "+"Exception: "+e);
			Assert.fail("Error while performing GenRocket scenarios "+"Exception: "+e);
		}
	}

	public static void rmAndUpdateSceReceiver(String scenarioFileName, String domainName, String oldReceiverName,
			String newReceiverName) {

		Map<String, String> parameterMap = new HashMap<>();
		String canonicalName = null;
		String scenarioPath = cdir + "/src/main/resources/GenRocketScenarios/" + scenarioFileName;
		EngineManual api = new EngineManual(); // Initializing the EngineAPI
		try {
			LOGGER.info("----Loading GenRocket Scenario---");
			api.scenarioLoad(scenarioPath);
			LOGGER.info("----GenRocket Scenario loaded successfully---");
			api.receiverRemove(domainName, oldReceiverName);

			if (newReceiverName.equals("ExcelFileReceiver")) {
				parameterMap = fetchPropValue(newReceiverName);
				canonicalName = "com.genRocket.receiver.ExcelFileReceiver";
			}

			if (newReceiverName.equals("XMLFileReceiver")) {
				parameterMap = fetchPropValue(newReceiverName);
				canonicalName = "com.genRocket.receiver.XMLFileReceiver";
			}

			if (newReceiverName.equals("MySQLInsertReceiver")) {
				parameterMap = fetchPropValue(newReceiverName);

				String configPath = FileUtils.getFilePath(
						"src/main/resources/GenRocketConfigFiles/" + parameterMap.get("resourceName").toString());
				parameterMap.replace("resourceName", configPath);
				canonicalName = "com.genRocket.receiver.MySQLInsertReceiver";
			}

			api.receiverAdd(domainName, canonicalName, newReceiverName, parameterMap);
			api.scenarioRun(); // Running the scenario\
			LOGGER.info("----Successfully ran GenRocket Scenario---");
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("Error while performing GenRocket scenarios "+"Exception: "+e);
			Assert.fail("Error while performing GenRocket scenarios "+"Exception: "+e);
		}
	}

	public static Map<String, String> fetchPropValue(String strVarName) {
		File file = null;
		Map<String, String> map = new HashMap<String, String>();
		try {
			file = new File(
					FileUtils.getFilePath("src/main/resources/GenRocketProperties/" + strVarName + ".properties"));
			Properties properties = new Properties();
			properties.load(new FileInputStream(file));
			map.putAll(properties.entrySet().stream()
					.collect(Collectors.toMap(e -> e.getKey().toString(), e -> e.getValue().toString())));
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("Error while trying to fetch GenRocket property file "+"Exception: "+e);
			Assert.fail("Error while trying to fetch GenRocket property file "+"Exception: "+e);
		}
		return map;
	}

}
