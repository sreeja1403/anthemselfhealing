package com.scripted.jsonParser;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.scripted.jsonWriter.HealingInfo;

public class JsonObjectMapper {
	private static String cdir = System.getProperty("user.dir");
	static List<String> jsonPaths;
	static String jsonPath = "C:/Users/u38119/Desktop/jsonfiles/sCompleteTestNonEncryp.json";
	static List<String> webJsonPaths = new ArrayList<String>();
	static List<String> MobileJsonPaths = new ArrayList<String>();
	static String pageName;
	static Locators LocatorValue;

	public static ConcurrentHashMap<String, String> jsonMapper(String pageNameValue, String elementNameValue,
			String locType, String HealedEle, String Technology) throws IOException {
		Boolean status = false;
		ObjectMapper objectMapper = new ObjectMapper();
		ConcurrentHashMap<String, String> attributeValuesMap = new ConcurrentHashMap<String, String>();
		try {

			decryptFile(jsonPath);
			System.out.println("File decrypted successfully");
			byte[] jsonData = Files.readAllBytes(Paths.get(jsonPath));
			Project project = objectMapper.readValue(jsonData, Project.class);// comment
			StringBuffer value = new StringBuffer();
			List<Pages> pages = project.getObjectRepository().getWebObjectRepo().getPages();

			for (Pages pageObj : pages) {
				pageName = pageObj.getPageName();
				// System.out.println("Page name :" + pageName);
				if (pageNameValue.equals(pageName)) {
					List<WebElements> webElements = pageObj.getWebElements();
					for (WebElements webElement : webElements) {
						if (webElement.getElementName().equals(elementNameValue)) {
							status = true;
							LocatorValue = webElement.getLocators();

							if (HealedEle != null) {
								if (!locType.trim()
										.equalsIgnoreCase(LocatorValue.getSelectedLocator().get("attribute").trim())) {
									ConcurrentHashMap<String, String> updData = new ConcurrentHashMap<>();
									updData.put("attribute", HealedEle.split(":")[0].trim());

									System.out.println("Attribute to be updated in json :" + updData.get("attribute"));

									if (HealedEle.split(":").length > 2) {
										for (int i = 1; i < HealedEle.split(":").length; i++) {
											if (i == HealedEle.split(":").length - 1) {
												value.append(HealedEle.split(":")[i].split("]")[0].trim());
												if (updData.get("attribute").trim().equalsIgnoreCase("xpath")) {
													value.append("]");
												}
											} else {
												value.append(HealedEle.split(":")[i].trim() + ":");
											}
										}

										updData.put("value", value.toString());

									} else {
										if (updData.get("attribute").trim().equalsIgnoreCase("xpath")) {
											updData.put("value", HealedEle.split(":")[1].split("]")[0].trim() + "]");
										} else {
											updData.put("value", HealedEle.split(":")[1].split("]")[0].trim());
										}
									}

									System.out.println("Attribute to be updated in json :" + updData.get("value"));
									LocatorValue.setSelectedLocator(updData);
									objectMapper.writeValue(new File(jsonPath), project);
									System.out.println("Json file updated"); // Encrypting updated json
									System.out.println("File content before encyption :" + readFile(jsonPath));
									String enc = JsonEncryptor.encryptPass(readFile(jsonPath));
									BufferedWriter writer = new BufferedWriter(new FileWriter(jsonPath));
									writer.write(enc);
									writer.close();
									System.out.println("File updated after encrption");

								}

							}

						}
					}
					if (status == true) {
						break;

					}
				}

			}
		} catch (Exception e) {
			System.out.println("Exception in parser :" + e);
			attributeValuesMap = null;
		}
		if (status == false) {
			attributeValuesMap = null;
		}
		return attributeValuesMap;
	}

	public static void networkJsonParser() {
		try {
			List<String> networkErrorList = new ArrayList<>();
			String cdir = System.getProperty("user.dir");
			System.out.println(cdir);
			byte[] jsonData = Files
					.readAllBytes(Paths.get(cdir + "\\src\\com\\scripted\\jsonParser\\NetworkError.json"));
			ObjectMapper objectMapper = new ObjectMapper();

			NetworkError networkError = objectMapper.readValue(jsonData, NetworkError.class);
			String jsonInString = objectMapper.writeValueAsString(networkError);
			System.out.println(jsonInString);
			// String error = jsonInString.split("[")[1];
			// System.out.println(error);
			// String[] errorLists = jsonInString.split("[")[1].split(",");
			for (String errorList : networkError.NetworkError) {
				System.out.println("Error in list :" + errorList);
				networkErrorList.add(errorList);
			}
			System.out.println("network error list :" + networkErrorList);

		} catch (IOException e) {
			System.out.println("Exception while reading network error json " + e);
			e.printStackTrace();
		}

	}

	public static String readFile(String encyrpath) {
		String fileContent = null;
		try {
			File file = new File(encyrpath);
			FileReader reader = new FileReader(file);
			char[] chars = new char[(int) file.length()];
			reader.read(chars);
			fileContent = new String(chars);
		} catch (Exception e) {
			System.out.println("Exception while reading file content :" + e);
		}

		return fileContent;
	}

	public static void decryptFile(String path) {
		try {
			String dec = JsonEncryptor.decryptPass(readFile(path));
			// System.out.println("Value after decryption :" + dec);
			BufferedWriter writer = new BufferedWriter(new FileWriter(path));
			writer.write(dec);
			writer.close();
			System.out.println("Decrypted file written back to file successfully");
		} catch (Exception e) {
			System.out.println("Exception while decrypting file : " + e);
		}
	}

	public static void encryptFile(String path) {
		try {
			String enc = JsonEncryptor.encryptPass(readFile(path));
			BufferedWriter writer = new BufferedWriter(new FileWriter(path));
			writer.write(enc);
			writer.close();
			System.out.println("Encrypted file written back to file successfully");
		} catch (Exception e) {
			System.out.println("Exception while encrypting file :" + e);
		}
	}

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void checkPythonVersion()
	{
		try {
		Process process=Runtime.getRuntime().exec(new String[] {"cmd", "/C", "python --version"}); 
		System.out.println("checked python version");
		 BufferedReader reader=new BufferedReader(new InputStreamReader(process.getInputStream())); 
             String line; 
             while((line = reader.readLine()) != null) 
             { 
                 System.out.println(line);
             } 
		}
		catch(Exception e)
		{
			System.out.println("Exception : "+e);
		}
	}
	public static void main(String[] args) throws IOException {
		//checkPythonVersion();
	//	decryptFile("C:\\Users\\u38119\\Desktop\\Workspace-SelfHealing-Java-Final\\SKSelfHealing\\TAF-Scripting\\src\\main\\resources\\HealingConfigurations\\final_xpath.py");
		//decryptFile("D:\\spy\\trialpneuron3010\\objectrepo\\trialpneuron3010.sky");
	//	decryptFile("D:\\spy\\pneuronv2\\objectrepo\\pneuronv2.sky");
		
		//encryptFile("C:\\Users\\u38119\\Desktop\\Workspace-SelfHealing-Java-Final\\SKSelfHealing\\TAF-Scripting\\src\\main\\resources\\HealingConfigurations\\skyFiles\\Apvalidation.sky");
		//encryptFile("C:\\Users\\u38119\\Desktop\\Workspace-SelfHealing-Java-Final\\SKSelfHealing\\TAF-Scripting\\src\\main\\resources\\HealingConfigurations\\skyFiles\\MyApp.sky");
		//encryptFile("C:\\Users\\u38119\\Desktop\\Workspace-SelfHealing-Java-Final\\SKSelfHealing\\TAF-Scripting\\src\\main\\resources\\HealingConfigurations\\skyFiles\\OrangeHrm.sky");
	//	encryptFile("C:\\Users\\u38119\\Desktop\\Workspace-SelfHealing-Java-Final\\SKSelfHealing\\TAF-Scripting\\src\\main\\resources\\HealingConfigurations\\skyFiles\\Mobile_automationpractice.sky");
		
	//	encryptFile("C:\\Users\\u38119\\Desktop\\Workspace-SelfHealing-Java-Final\\SKSelfHealing\\TAF-Scripting\\src\\main\\resources\\HealingConfigurations\\skyFiles\\mobiletrial12.sky");
	//	encryptFile("C:\\Users\\u38119\\Desktop\\Workspace-SelfHealing-Java-Final\\SKSelfHealing\\TAF-Scripting\\src\\main\\resources\\HealingConfigurations\\skyFiles\\automationpracticecheck.sky");
		//encryptFile("C:\\Users\\u38119\\Desktop\\jsonfiles\\Mobile_Native.sky");
		decryptFile("C:\\Users\\u38119\\Desktop\\SFDC.sky");
		
	//	(//div[@class='form-group']//input)[2]
	}
}
