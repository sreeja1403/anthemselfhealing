package com.scripted.selfhealing;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
//import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.FindBy;

import com.scripted.jsonParser.JsonObjectParser;
import com.scripted.jsonWriter.JsonHealingInfoWriter;
import com.scripted.reporting.selfhealing.ReportGenerationDummy;

import io.appium.java_client.MobileDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.pagefactory.AndroidFindBy;

public class SMMobHealer {

	public static Logger LOGGER = Logger.getLogger(SMMobHealer.class);
	JsonObjectParser jsonObjectParser = new JsonObjectParser();
	static MobileDriver driver;
	static String Technology = "mobile";
	// public static ThreadLocal<WebDriver> lthdriver = new ThreadLocal<>();
	public static ThreadLocal<RemoteWebDriver> thdriver = new ThreadLocal<>();


	// Parallely check locators
	MobileElement locator = null;
	MobileElement locator_id = null;
	MobileElement locator_name = null;
	MobileElement locator_xpath = null;
	MobileElement locator_css = null;
	MobileElement locator_class = null;
	MobileElement locator_xpath_Follows = null;
	MobileElement locator_xpath_default = null;
	MobileElement locator_xpath_Parent = null;
	MobileElement locator_xpath_Attribute = null;
	MobileElement locator_xpath_Descendant = null;
	MobileElement locator_xpath_Position = null;
	MobileElement locator_xpath_fullXPath = null;
	MobileElement locator_xpath_Contains = null;
	MobileElement locator_xpath_Precedance = null;
	MobileElement locator_xpath_Ancestor = null;
	MobileElement locator_xpath_child = null;

	// String strClass = "";
	public static ConcurrentHashMap<String, ElementDetails> healdElementData = new ConcurrentHashMap<>();
	boolean verifyeleFound = false;
	boolean alertStatus = false;
	boolean windowStatus = false;

	static boolean healStsFlg = false;

	// Element Healing Parameters
	int count;
	long maxCount = 0;
	long maxCountCheck = 0;
	ReportGenerationDummy reportgeneration = new ReportGenerationDummy();

	

	String testCaseName = "";

	String errorMsg;

	private static String cdir = System.getProperty("user.dir");


	static List<String> networkMsgs = new ArrayList<String>();

	public static boolean shflag;
	public static boolean impactFlag;


	

	public void setShflag(boolean shflag) {
		this.shflag = shflag;
	}
	public boolean isShflag() {
		return shflag;
	}
	public void setImpactflag(boolean impactflag) {
		this.impactFlag = impactflag;
	}

	public boolean isImpactflag() {
		return impactFlag;
	}

	public SMMobHealer() {

	}

	public boolean gethealStatusFlag() {
		return healStsFlg;
	}
	SelfHealingUtil util = new SelfHealingUtil();
	public MobileElement initiateHealing(Exception Msg, MobileElement locator, MobileDriver mobiledriver) {
		System.out.println("Object healing in progress");
		driver = mobiledriver;
		String exTyp = Msg.getClass().getSimpleName();
		ConcurrentHashMap<String, String> healRptInfo = new ConcurrentHashMap<>();
		boolean exChkSts = false;
		healStsFlg = true;
		String excMsg = Msg.toString();
		MobileElement healLoc = null;
		healRptInfo.put("TestCase", getTestCaseName(Msg));
		healRptInfo.put("stepActionName", gettestStepAction(Msg));
		healRptInfo.put("ExcptnMsg", excMsg);
		healRptInfo.put("ExceptionType", exTyp);
		healRptInfo.putAll(getPgAndElmtName(locator, excMsg));
		System.out.println("healcommondata:" + healRptInfo);
		System.out.println("finished getting pagename,elename and action");

		/**
		 * 
		 * Check whether exception occurred already for same element
		 */
		if (healdElementData.containsKey(healRptInfo.get("PageName") + "_" + healRptInfo.get("ElementName"))) {
			String PgeEleDtls=healRptInfo.get("PageName") + "_" + healRptInfo.get("ElementName");
			while (healdElementData.get(healRptInfo.get("PageName") + "_" + healRptInfo.get("ElementName"))
					.getStatus().equalsIgnoreCase("inprogress")) {

			}
			// System.out.println("Outside the while loop of checking status");
			if ((healdElementData.get(PgeEleDtls).getExceptionType().equalsIgnoreCase("NoSuchElementException")
					|| healdElementData.get(PgeEleDtls)
							.getExceptionType().equalsIgnoreCase("InvalidSelectorException"))
					&& (healRptInfo.get("ExcptnMsg").contains("NoSuchElementException")
							|| healRptInfo.get("ExcptnMsg").contains("InvalidSelectorException")))

			{
				ConcurrentHashMap<String, Integer> countMap = new ConcurrentHashMap<String, Integer>();
				countMap.put("NoSuchElement", 1);
				ConcurrentHashMap<String, String> jsonDataMap = new ConcurrentHashMap<String, String>();
				if (healdElementData.get(PgeEleDtls)
						.getStatus().equalsIgnoreCase("success")) {
					MobileElement elevrfy=(MobileElement) healdElementData.get(PgeEleDtls).getHealedLoc();
					MobileElement verifyHldLctr=verifyElement(healRptInfo, elevrfy);
					if(verifyHldLctr!=null)
					{
					healRptInfo.put("HealStatus", "success");
					healRptInfo.put("HealdEleTyp", healdElementData.get(PgeEleDtls).getHealedEleTyp());
					healRptInfo.put("HealedLocator", healdElementData.get(PgeEleDtls).getHealedEleVle());
					healRptInfo.put("ImageFileName", healdElementData.get(PgeEleDtls).getScrnShtPath());//Avoid null pointer exceptions
					jsonDataMap=healdElementData.get(PgeEleDtls).getLocDataMap();
					JsonHealingInfoWriter.AddStep(healRptInfo, getHealCountInfoCheck(countMap), jsonDataMap, Technology);
					}
					return verifyHldLctr;
				}
				if (healdElementData.get(healRptInfo.get("PageName") + "_" + healRptInfo.get("ElementName"))
						.getStatus().equalsIgnoreCase("failure")) {
					return null;
				}

			}
		} else {
			ElementDetails elementDtls = new ElementDetails();
			elementDtls.setStatus("InProgress");
			elementDtls.setFailedLoc(healRptInfo.get("locatorType") + ":" + healRptInfo.get("locatorValue"));
			healdElementData.put(healRptInfo.get("PageName") + "_" + healRptInfo.get("ElementName"), elementDtls);
		}

		try {
				switch (exTyp) {
				case "TimeoutException":
					exChkSts = true;
					healLoc = timeoutCheck(locator, healRptInfo);
					break;
				case "StaleElementReferenceException":
					exChkSts = true;
					healLoc = staleChk(locator, healRptInfo);
					break;
				case "ElementNotVisibleException":
					exChkSts = true;
					healLoc = staleChk(locator, healRptInfo);
					break;
				case "ElementNotSelectableException":
					exChkSts = true;
					healLoc = staleChk(locator, healRptInfo);
					break;
				case "NoAlertPresentException":
					exChkSts = true;
					healLoc = locator;
					break;
				case "UnhandledAlertException":
					exChkSts = true;
					healLoc = unhandledAlertCheck(locator, healRptInfo);
					break;
				case "NoSuchElementException":
					exChkSts = true;
					healLoc = noSuchEleExcChk(healRptInfo, locator);
					break;
				case "InvalidSelectorException":
					exChkSts = true;
					healLoc = noSuchEleExcChk(healRptInfo, locator);
					break;
				default:
					int othersCount = 0;
					ConcurrentHashMap<String, String> healInfoOthrExcptn = new ConcurrentHashMap<>();
					ConcurrentHashMap<String, Integer> OthrsHealCountInfo = new ConcurrentHashMap<String, Integer>();
					othersCount++;
					OthrsHealCountInfo.put("othersCount", othersCount);
					healInfoOthrExcptn.put("ExceptionType", exTyp);
					healInfoOthrExcptn.putAll(healRptInfo);
					ConcurrentHashMap<String, String> jsonMap = new ConcurrentHashMap<>();
					jsonMap.put(healRptInfo.get("locatorValue"), healRptInfo.get("locatorType"));
					getReportValues(healLoc, healInfoOthrExcptn, getHealCountInfoCheck(OthrsHealCountInfo), jsonMap);

				}

		} catch (Exception e) {
			// Need to handle the exception here
			// System.out.println(e);
			healLoc = null;
			// Assert.fail("Exception occurred while trying to heal locator :" + element);

		}

		return healLoc;
	}

	private MobileElement unhandledAlertCheck(MobileElement alertLocator,
		ConcurrentHashMap<String, String> healCmmnData) {
		String alertText="";
		ConcurrentHashMap<String, String> HealInfoAlertCheck = new ConcurrentHashMap<>();
		int othersCount = 0;
		HealInfoAlertCheck.putAll(healCmmnData);
		MobileElement alertHldLctr;
		try {
			// excptnCheckStatus = true;
			othersCount++;
			// HealInfoAlertCheck.putAll(getPgAndElmtName(alertLocator));
			ConcurrentHashMap<String, Integer> AlertHealCountInfo = new ConcurrentHashMap<String, Integer>();
			AlertHealCountInfo.put("othersCount", othersCount);
			Alert alert = driver.switchTo().alert();
			alertText=alert.getText();
			LOGGER.info("text inside alert :" + alert.getText());

			for (String acceptAlertTxt : HealingConfig.acptAlrtTxts) {
				if (alert.getText().equalsIgnoreCase(acceptAlertTxt)) {
					alert.accept();
					HealInfoAlertCheck.put("HealedLocator", "alert accepted");
					alertStatus = true;
					System.out.println("Alert accepted");
					break;
				}
			}
			if (!alertStatus) {
				for (String dismissAlertTxt : HealingConfig.dsmsAlrtTxts) {
					if (alert.getText().equalsIgnoreCase(dismissAlertTxt)) {
						alert.dismiss();
						alertStatus = true;
						HealInfoAlertCheck.put("HealedLocator", "alert dismissed");
						System.out.println("Alert dismissed");
						break;

					}
				}
			}
			alertHldLctr = alertLocator;
			HealInfoAlertCheck.put("ExceptionType", "Unhandled Alert Exception");
			// getHealCountInfo();
			ConcurrentHashMap<String, String> jsonMap = new ConcurrentHashMap<>();
			jsonMap.put("Alert", alertText);
			HealInfoAlertCheck.put("HealdEleTyp", "Alert");
			HealInfoAlertCheck.put("locatorType", "Alert");
			HealInfoAlertCheck.put("locatorValue", "alertText");
			if (alertStatus) {
				String timestampImage = captureScreenScrnshot(driver);
				HealInfoAlertCheck.put("ImageFileName", timestampImage);
				HealInfoAlertCheck.put("HealStatus", "success"); // add to write file;
				reportgeneration.alertHealingFormatter(HealInfoAlertCheck, AlertHealCountInfo, Technology);
				JsonHealingInfoWriter.AddStep(HealInfoAlertCheck, getHealCountInfoCheck(AlertHealCountInfo),jsonMap,Technology);
			} else {
				HealInfoAlertCheck.put("HealStatus", "failure"); // add to write file;
				JsonHealingInfoWriter.AddStep(HealInfoAlertCheck, getHealCountInfoCheck(AlertHealCountInfo),jsonMap,Technology);
				reportgeneration.alertHealingFormatter(HealInfoAlertCheck, getHealCountInfoCheck(AlertHealCountInfo),
						Technology);
			}

		}

		catch (Exception Ex) {
			alertHldLctr = alertLocator;

		}
		return alertLocator;
	}

	private MobileElement staleChk(MobileElement staleLocator, ConcurrentHashMap<String, String> healCmmnData) {
		int staleCount = 0;
		ConcurrentHashMap<String, String> healInfoStleCheck = new ConcurrentHashMap<>();
		ConcurrentHashMap<String, Integer> StleHealCountInfo = new ConcurrentHashMap<String, Integer>();
		healInfoStleCheck.putAll(healCmmnData);
		MobileElement staleHldLctr = null;
		staleCount++;
		StleHealCountInfo.put("staleTimeoutCount", staleCount);
		try
		{
			util.mobWaitForPresence(staleLocator, HealingConfig.staleTime, driver);
			staleHldLctr=staleLocator;
		}
		catch(Exception e)
		{
			if(e.getClass().getSimpleName().equalsIgnoreCase(CnstLookup.NOSUCHELE))
				noSuchEleExcChk(healInfoStleCheck, staleLocator);
			else
				staleHldLctr=null;	
		}
		
		if(staleHldLctr!=null)
		healInfoStleCheck.put("HealedLocatorVle", getHealedLocatorValue(staleHldLctr));
		ConcurrentHashMap<String, String> jsonMap = new ConcurrentHashMap<>();
		jsonMap.put(healCmmnData.get("locatorValue"), healCmmnData.get("locatorType"));
		getReportValues(staleHldLctr, healInfoStleCheck, getHealCountInfoCheck(StleHealCountInfo), jsonMap);
		return staleHldLctr;
	}

	private String getHealedLocatorValue(MobileElement HealedlocatorVle) {
		String HealedLocatorValue;
		if (HealedlocatorVle != null) {
			String[] HealedLocatorValues = HealedlocatorVle.toString().split("->");
			int HealedLocatorLength = HealedLocatorValues.length;
			HealedLocatorValue = HealedLocatorValues[HealedLocatorLength - 1];
		} else {
			HealedLocatorValue = null;
		}
		return HealedLocatorValue;
	}

	private MobileElement timeoutCheck(MobileElement timeLocator, ConcurrentHashMap<String, String> healCmmnDataMap) {
		ConcurrentHashMap<String, String> healInfoTmeOutCheck = new ConcurrentHashMap<>();
		ConcurrentHashMap<String, Integer> TimeOutHealCountInfo = new ConcurrentHashMap<String, Integer>();
		healInfoTmeOutCheck.putAll(healCmmnDataMap);
		MobileElement timeOutHldlctr = null;
		int TimeoutCount = 0;
		TimeoutCount++;
		TimeOutHealCountInfo.put("staleTimeoutCount", TimeoutCount);
		try
		{
			util.mobWaitForPresence(timeLocator, HealingConfig.timeoutTime, driver);
		}
		catch(Exception e)
		{
			if(e.getClass().getSimpleName().equalsIgnoreCase(CnstLookup.NOSUCHELE))
				noSuchEleExcChk(healInfoTmeOutCheck, timeLocator);
			else
				timeOutHldlctr=null;	
		}
		if (timeOutHldlctr != null) 
		healInfoTmeOutCheck.put("HealedLocatorVle", getHealedLocatorValue(timeOutHldlctr));
		ConcurrentHashMap<String, String> jsonMap = new ConcurrentHashMap<>();
		jsonMap.put(healCmmnDataMap.get("locatorValue"), healCmmnDataMap.get("locatorType"));
		getReportValues(timeOutHldlctr, healInfoTmeOutCheck, getHealCountInfoCheck(TimeOutHealCountInfo), jsonMap);
		return timeOutHldlctr;
	}

	private MobileElement verifyElement(String eletyp, String ele) {
		System.out.println("Inside verify condition :" + ele);
		MobileElement verifyHldLctr = null;
		try {
			switch (eletyp) {
			case "id":
				verifyHldLctr = (MobileElement) driver.findElement(By.id(ele));
			case "name":
				verifyHldLctr = (MobileElement) driver.findElement(By.name(ele));
			case "xpath":
				verifyHldLctr = (MobileElement) driver.findElement(By.xpath(ele));
			case "classname":
				verifyHldLctr = (MobileElement) driver.findElement(By.className(ele));
			case "css":
				verifyHldLctr = (MobileElement) driver.findElement(By.cssSelector(ele));
			}

		} catch (Exception e) {
			// System.out.println("Issues while verifying element");
		}

		return verifyHldLctr;
	}

	private MobileElement verifyElement(ConcurrentHashMap<String, String> vrfyCheckMap, MobileElement ele) {
		MobileElement verifyHldLctr = null;
		Boolean vereleFound = false;
		try {
			String[] attrValue = ele.toString().split("->")[1].split(":");
			switch (attrValue[0].trim()) {
			case "id":
				verifyHldLctr = (MobileElement) driver.findElement(By.id(attrValue[1].split("]")[0].trim()));
				vereleFound = true;
				break;
			case "name":
				verifyHldLctr = (MobileElement) driver.findElement(By.name(attrValue[1].split("]")[0].trim()));
				vereleFound = true;
				break;
			case "xpath":
				verifyHldLctr = (MobileElement) driver.findElement(By.xpath(attrValue[1].split("]")[0].trim() + "]"));
				vereleFound = true;
				break;
			case "class name":
				verifyHldLctr = (MobileElement) driver.findElement(By.className(attrValue[1].split("]")[0].trim()));
				vereleFound = true;
				break;

			}
		} catch (Exception e) {
			// System.out.println("Exception : " + e);
			if (e.toString().contains("NoSuchElementException") || e.toString().contains("InvalidSelectorException")) {
				vrfyCheckMap.put("ExcptnMsg", e.toString());
				noSuchEleExcChk(vrfyCheckMap, ele);
			}
			// verifyHldLctr = null;
		}
		return verifyHldLctr;
	}

	private MobileElement noSuchEleExcChk(ConcurrentHashMap<String, String> noSuchExcMap, MobileElement ele) {
		int noSuchElementCount = 0;
		Boolean selAttrFlag = false;
		ConcurrentHashMap<String, String> jsonValuesMap = new ConcurrentHashMap<>();
		ConcurrentHashMap<String, String> networkDataMap = new ConcurrentHashMap<>();
		Boolean iframeCheck = true;
		ConcurrentHashMap<String, String> healInfonoSuchEleExcCheck = new ConcurrentHashMap<String, String>();
		MobileElement noShEleExcHldElmnt = null;
		String noShEleExcHldElmntHldLctVle = null;
		healInfonoSuchEleExcCheck.putAll(noSuchExcMap);
		String errMsg = noSuchExcMap.get("ExcptnMsg");
		if (errMsg.contains("NoSuchElementException")) {
			errorMsg = "NoSuchElementException";
		} else {
			errorMsg = "InvalidSelectorException";
		}
		healInfonoSuchEleExcCheck.put("iframe", "true");
		// driver.
		// noShEleExcHldElmnt = multipleWindowCheck(ele, healInfonoSuchEleExcCheck);
		if (noShEleExcHldElmnt != null)
			return noShEleExcHldElmnt;
		/*
		 * networkDataMap = networkCheck(ele, healInfonoSuchEleExcCheck); if
		 * (networkDataMap.get("NetworkStatus") == "true" ||
		 * networkDataMap.get("NetworkDown") == "true") return null;
		 */
		noSuchElementCount++;
		healInfonoSuchEleExcCheck.put("ExceptionType", errorMsg);
		ConcurrentHashMap<String, Integer> NoShHealCountInfo = new ConcurrentHashMap<String, Integer>();
		NoShHealCountInfo.put("NoSuchElement", noSuchElementCount);
		try {
			ConcurrentHashMap<String, String> attributeMap = new ConcurrentHashMap<>();

			if (healInfonoSuchEleExcCheck.get("PageName") != null
					&& healInfonoSuchEleExcCheck.get("ElementName") != null) {
				attributeMap = jsonObjectParser.jsonMapper(healInfonoSuchEleExcCheck.get("PageName"),
						healInfonoSuchEleExcCheck.get("ElementName"), healInfonoSuchEleExcCheck.get("locatorType"),
						null);// check
				System.out.println("Attribute map :" + attributeMap);
			}

			if (attributeMap != null && attributeMap.get("iframe") != null) {
				if (attributeMap.get("iframe").length() > 1) {
					JavascriptExecutor exe = (JavascriptExecutor) driver;
					Object isInIframe = exe.executeScript("return !(window.parent == window.self);");
					System.out.println("element is in frame " + isInIframe.toString());
					if (isInIframe.toString() == "false") {
						iframeCheck = false;
						healInfonoSuchEleExcCheck.put("ExceptionType", "NoSuchElementException-iframeswitch");
						healInfonoSuchEleExcCheck.put("iframe", "false");
					}
				}
			}

			if (attributeMap != null && iframeCheck == true) {
				attributeMap.put("LocatorType", healInfonoSuchEleExcCheck.get("locatorType"));
				attributeMap.put("PageName", healInfonoSuchEleExcCheck.get("PageName"));
				attributeMap.put("ElementName", healInfonoSuchEleExcCheck.get("ElementName"));
				Iterator<Entry<String, String>> itr = attributeMap.entrySet().iterator();
				while (itr.hasNext()) {
					Entry<String, String> key = itr.next();
					if (key.toString().contains("sel_attr")) {
						System.out.println("Checking selected attributes");
						String attrTyp = key.getKey().split(":")[1];
						String attrVle = attributeMap.get(key.getKey());
						System.out.println("Sel attribute :" + attrTyp + ":" + attrVle);
						noShEleExcHldElmnt = verifyElement(attrTyp, attrVle);
						if (noShEleExcHldElmnt != null) {
							selAttrFlag = true;
							updateCollection(healInfonoSuchEleExcCheck, noShEleExcHldElmnt);
							healInfonoSuchEleExcCheck.put("HealdEleTyp", attrTyp);
							healInfonoSuchEleExcCheck.put("HealedLocatorVle", attrVle);
							jsonValuesMap.put("iframe", attributeMap.get("iframe"));
						}
					}

				}
				ElementValue vrfyValue = new ElementValue();
				if (!selAttrFlag) {
					// System.out.println("Checking other attributes");
					vrfyValue = verifyElement(attributeMap, driver);

					if (vrfyValue.eleType != null && vrfyValue.eleValue != null && vrfyValue.eleLocVle != null) {
						if(vrfyValue.locatorsMap!=null)
						jsonValuesMap.putAll(vrfyValue.locatorsMap);
						noShEleExcHldElmnt = (MobileElement) vrfyValue.eleLocVle;
						if (noShEleExcHldElmnt != null) {
							noShEleExcHldElmntHldLctVle = vrfyValue.eleValue;
							healInfonoSuchEleExcCheck.put("HealdEleTyp", vrfyValue.eleType);
							healInfonoSuchEleExcCheck.put("HealedLocatorVle", noShEleExcHldElmntHldLctVle);
							updateCollection(healInfonoSuchEleExcCheck, noShEleExcHldElmnt);
						}
					} else {
						String bSoupXpath = bSoupCall(attributeMap);
						if (bSoupXpath != null) {
							System.out.println("Output from bSoup: " + bSoupXpath);
							noShEleExcHldElmnt = verifyElement("xpath", bSoupXpath);
							if (noShEleExcHldElmnt != null) {
								healInfonoSuchEleExcCheck.put("HealdEleTyp", "xpath");
								healInfonoSuchEleExcCheck.put("HealedLocatorVle", bSoupXpath);
								updateCollection(healInfonoSuchEleExcCheck, noShEleExcHldElmnt);
								healInfonoSuchEleExcCheck.put("key", "xpath");
								healInfonoSuchEleExcCheck.put("value", bSoupXpath);
								jsonObjectParser.jsonMapper(attributeMap.get("PageName"),
										attributeMap.get("ElementName"), "xpath", healInfonoSuchEleExcCheck);
							}
						} else {
							noShEleExcHldElmnt = null;
							noShEleExcHldElmntHldLctVle = null;
						}
					}
				}
			}

			getReportValues(noShEleExcHldElmnt, healInfonoSuchEleExcCheck, getHealCountInfoCheck(NoShHealCountInfo),jsonValuesMap);
		} catch (Exception e) {
			e.printStackTrace();
			// System.out.println("Exception :" + e);
		}
		return noShEleExcHldElmnt;
	}

	public void updateCollection(ConcurrentHashMap<String, String> updMap, MobileElement vle) {
		ElementDetails eleNoSuchChkDtls = healdElementData
				.get(updMap.get("PageName") + "_" + updMap.get("ElementName"));
		eleNoSuchChkDtls.setHealedLoc(vle);
		healdElementData.put(updMap.get("PageName") + "_" + updMap.get("ElementName"), eleNoSuchChkDtls);
	}

	public String bSoupCall(ConcurrentHashMap<String, String> bSoupData) {
		// System.out.println("Inside bSoup method");
		String xpath = null;
		try {
			String tempFileLoc = "src/main/resources/HealingConfigurations/htmldata.txt";
			String htmlContent = driver.getPageSource();
			Process process;
			Process mProcess;

			FileWriter fw = new FileWriter(tempFileLoc);
			fw.write(htmlContent);
			fw.close();
			// System.out.println("");
			String fullxpath = bSoupData.get("xpath_fullXPath");
			String parent = bSoupData.get("xpath_Parent");
			String ancestor = bSoupData.get("xpath_Ancestor");
			String child = bSoupData.get("xpath_child");
			String pagename = bSoupData.get("PageName");
			String elementname = bSoupData.get("ElementName");
			// Split Object Tag
			String[] parts = elementname.split("_");
			String objtag = parts[0];
			String pyFile = "src/main/resources/HealingConfigurations/final_xpath.py";

			String command = "python \"" + pyFile + "\" \"" + tempFileLoc + "\" \"" + objtag + "\" \"" + pagename
					+ "\" \"" + fullxpath + "\" \"" + parent + "\" \"" + ancestor + "\" \"" + child;
			jsonObjectParser.decryptFile(pyFile);
			process = Runtime.getRuntime().exec(command);
			mProcess = process;
			InputStream stdout = mProcess.getInputStream();
			// System.out.println("output: " + stdout.toString());
			BufferedReader reader = new BufferedReader(new InputStreamReader(stdout, StandardCharsets.UTF_8));
			jsonObjectParser.encryptFile(pyFile);
			String line;
			while ((line = reader.readLine()) != null) {
				// System.out.println("stdout: " + line);
				if (line.contains("Object Locator:")) {
					String[] strSplit = line.split("Object Locator:");
					xpath = strSplit[1];
				}
			}

		} catch (Exception e) {
			System.out.println("Exception occured in bSoup call" + e);
		}

		return xpath;

	}

	public ConcurrentHashMap<String, String> networkCheck(MobileElement ele,
			ConcurrentHashMap<String, String> healCmmnDataMap) {// return map with network
		// status(avoid going to nosuchelement exception
		int networkErrorCount = 0;
		ConcurrentHashMap<String, Integer> NtwrkHealCountInfo = new ConcurrentHashMap<String, Integer>();
		ConcurrentHashMap<String, String> healInfoNtwrkCheck = new ConcurrentHashMap<String, String>();
		healInfoNtwrkCheck.putAll(healCmmnDataMap);
		MobileElement NetwrkHealedlocator = null;
		boolean ntwrkStatus = false;
		boolean ntwrkDown = false;
		try {

			networkMsgs = jsonObjectParser.networkJsonParser();
			for (String msg : networkMsgs) {
				if (driver.getPageSource().contains(msg)) {
					healInfoNtwrkCheck.put("NetworkDown", "true");
					LOGGER.info("The obtained network error message is " + msg);
					healInfoNtwrkCheck.put("ExceptionType", "NetworkException");
					networkErrorCount++;
					ntwrkStatus = true;
					healInfoNtwrkCheck.put("NetworkStatus", "true");
					NtwrkHealCountInfo.put("networkErrorCount", networkErrorCount);
					healInfoNtwrkCheck.put("NetworkHealedLocator", "true");
					ConcurrentHashMap<String, String> jsonMap = new ConcurrentHashMap<>();
					jsonMap.put(healCmmnDataMap.get("locatorValue"), healCmmnDataMap.get("locatorType"));

					getReportValues(NetwrkHealedlocator, healInfoNtwrkCheck, getHealCountInfoCheck(NtwrkHealCountInfo),
							jsonMap);
					driver.close();
					if (ntwrkStatus)
						break;
					NetwrkHealedlocator = null;
					ntwrkDown = true;

				}
			}

		} catch (Exception e) {
			NetwrkHealedlocator = null;
		}
		return healInfoNtwrkCheck;
	}

	private MobileElement multipleWindowCheck(MobileElement windwCheck,
			ConcurrentHashMap<String, String> healCmmnDataMap) {
		MobileElement windwCheckHldLctr = null;
		int otrCount = 0;
		ConcurrentHashMap<String, Integer> WndwHealCountInfo = new ConcurrentHashMap<String, Integer>();
		ConcurrentHashMap<String, String> healnfoWindwCheck = new ConcurrentHashMap<String, String>();
		healnfoWindwCheck.putAll(healCmmnDataMap);
		Set<String> handles = driver.getWindowHandles();
		int handlescount = driver.getWindowHandles().size();
		if (handlescount > 1) {
			try {
				String curWndHndle = driver.getWindowHandle();
				// getlthWebdriver().getWindowHandles().toArray(handles);
				for (String handle : handles) {
					String switchedWindowTitle = driver.switchTo().window(handle).getTitle();
					// healnfoWindwCheck.putAll(getPgAndElmtName(windwCheck));
					for (String windowTitle : HealingConfig.dsmsWndwTtls) {
						if (switchedWindowTitle.trim().equalsIgnoreCase(windowTitle.trim())) {// windowText
							driver.close(); // check how its working --
							driver.switchTo().window(curWndHndle);// curWndHndle-- need to check the logic
							windowStatus = true;
							otrCount++;
							windwCheckHldLctr = windwCheck;
							WndwHealCountInfo.put("othersCount", otrCount);
							healnfoWindwCheck.put("stepActionName", "MultipleWindowCheck");
							healnfoWindwCheck.put("ExceptionType", "MultipleWindowHandleCheck");
							healnfoWindwCheck.put("HealedLocator", "Dismissed window handle");
							ConcurrentHashMap<String, String> jsonMap = new ConcurrentHashMap<>();
							jsonMap.put(healCmmnDataMap.get("locatorValue"), healCmmnDataMap.get("locatorType"));
							getReportValues(windwCheckHldLctr, healnfoWindwCheck,
									getHealCountInfoCheck(WndwHealCountInfo), jsonMap);
							break;
						}
					}
					if (windowStatus) {
						break;
					}

				}
				if (windowStatus) {
					windwCheckHldLctr = verifyElement(healnfoWindwCheck, windwCheck);
				}
				// add verify element
			} catch (Exception e) {
				LOGGER.error("Error occurred while getting window handles" + "Exception" + e);
			}
		}
		return windwCheckHldLctr;

	}

	private ConcurrentHashMap<String, Integer> getHealCountInfoCheck(ConcurrentHashMap<String, Integer> HlCntInfo) {
		Iterator<Entry<String, Integer>> itr = HlCntInfo.entrySet().iterator();
		while (itr.hasNext()) {
			Entry<String, Integer> key = itr.next();
			if (!key.toString().contains("NoSuchElement"))
				HlCntInfo.put("NoSuchElement", 0);
			if (!key.toString().contains("networkErrorCount"))
				HlCntInfo.put("networkErrorCount", 0);
			if (!key.toString().contains("staleTimeoutCount"))
				HlCntInfo.put("staleTimeoutCount", 0);
			if (!key.toString().contains("othersCount"))
				HlCntInfo.put("othersCount", 0);
		}
		return HlCntInfo;

	}

	private void getReportValues(MobileElement Healedlocator, ConcurrentHashMap<String, String> healDataMap,
			ConcurrentHashMap<String, Integer> HealCountInfo,ConcurrentHashMap<String, String> jsonDataMap) {
		// System.out.println("heal map"+healDataMap);
		ConcurrentHashMap<String, String> healReportInfo = new ConcurrentHashMap<>();
		healReportInfo.putAll(healDataMap);
		// getHealCountInfo();
		String excptnMsg = healDataMap.get("ExcptnMsg");

		ElementDetails eleRptDtls = healdElementData
				.get(healReportInfo.get("PageName") + "_" + healReportInfo.get("ElementName"));
		eleRptDtls.setExceptionType(healDataMap.get("ExceptionType"));

		if (healDataMap.get("HealedLocatorVle") != null) {

			String timestampImage = captureScreenScrnshot(driver);
			healReportInfo.put("ImageFileName", timestampImage);
			healReportInfo.put("HealStatus", "success"); // add to write file;
			healReportInfo.put("HealedLocator", healDataMap.get("HealedLocatorVle"));
			// healdElementData.put(, elementDtls);
			healdElementData.get(healReportInfo.get("PageName") + "_" + healReportInfo.get("ElementName"))
					.setStatus("Success");
			eleRptDtls.setStatus("Success");
			if (excptnMsg.contains("Timeout") || excptnMsg.contains("Stale")) {
				/*
				 * // System.out.println("Element name : "+element); String failedloc =
				 * actLocator.toString().split("->")[1]; healReportInfo.put("ElementName",
				 * failedloc);
				 */
				JsonHealingInfoWriter.AddStep(healReportInfo, HealCountInfo, jsonDataMap, Technology);
				reportgeneration.staleReportFormatter(healReportInfo, HealCountInfo, Technology);
			} else {
				JsonHealingInfoWriter.AddStep(healReportInfo, HealCountInfo, jsonDataMap, Technology);
				// System.out.println("Map passed for reporting :" + healReportInfo);
				reportgeneration.Reportformatter(healReportInfo, HealCountInfo, Technology);
			}

		} else {
			healReportInfo.put("HealStatus", "Failure");
			eleRptDtls.setStatus("Failure");
			healReportInfo.put("HealedLocator", "NA");
			healdElementData.get(healReportInfo.get("PageName") + "_" + healReportInfo.get("ElementName"))
					.setStatus("Failure");
			JsonHealingInfoWriter.AddStep(healReportInfo, HealCountInfo, jsonDataMap, Technology);
			if (excptnMsg.contains("Timeout") || excptnMsg.contains("Stale")) {
				reportgeneration.staleReportFormatter(healReportInfo, HealCountInfo, Technology);
			} else {
				reportgeneration.Reportformatter(healReportInfo, HealCountInfo, Technology);
			}

		}
		healdElementData.put(healReportInfo.get("PageName") + "_" + healReportInfo.get("ElementName"), eleRptDtls);
		// System.out.println("Collection Data:"+healdElementData);
	}

	private String captureScreenScrnshot(MobileDriver driver) {
		StringBuffer scrFileNme = new StringBuffer();
		try {
			DateFormat dateFormat = new SimpleDateFormat("dd_MM_yyyy_hh_mm_ss_ms_aa");
			String dateString = dateFormat.format(new Date()).toString();
			if (SelfHealingUtil.tstCseNmeTstNG == null) {
				scrFileNme.append(testCaseName);
			} else {
				scrFileNme.append(SelfHealingUtil.tstCseNmeTstNG);
			}
			// TakesScreenshot scrShot = ((TakesScreenshot) driver);
			// File SrcFile = scrShot.getScreenshotAs(OutputType.FILE);
			File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
			// File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
			scrFileNme.append("_" + dateString);
			FileUtils.copyFile(srcFile, new File(HealingConfig.scrshtPath + "\\" + scrFileNme + ".png"), true);
			// System.out.println("screenshot path :"+scrnshotPath + "\\" + scrFileNme);
			// BufferedImage image = new Robot().createScreenCapture(new
			// Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
			// ImageIO.write(image, "png", new File(scrnshotPath + "\\"
			// +scrFileNme+".png"));
		} catch (Exception e) {
			e.printStackTrace();
			// System.out.println("Exception while taking screenshot"+e);
		}
		return scrFileNme.toString();
	}
	/*
	 * private String captureScreenshot(WebDriver driver, WebElement
	 * eleScreenCapture) { StringBuffer filename = new StringBuffer(); try {
	 * DateFormat dateFormat = new SimpleDateFormat("dd_MM_yyyy_hh_mm_ss_ms_aa");
	 * String dateString = dateFormat.format(new Date()).toString(); if
	 * (tstCseNmeTstNG.get() == null) { filename.append(testCaseName); } else {
	 * filename.append(tstCseNmeTstNG.get()); } JavascriptExecutor jse =
	 * (JavascriptExecutor) driver;
	 * jse.executeScript("arguments[0].scrollIntoView(true);", eleScreenCapture);
	 * jse.executeScript("arguments[0].style.border='2px solid red'",
	 * eleScreenCapture);
	 * jse.executeScript("arguments[0].style.outline='2px solid red'",
	 * eleScreenCapture); Thread.sleep(1000); File screenShot = ((TakesScreenshot)
	 * driver).getScreenshotAs(OutputType.FILE); filename.append("_" + dateString);
	 * // System.out.println("screenshot path inside capture screenshot" + //
	 * scrnshotPath); FileUtils.copyFile(screenShot, new File(scrnshotPath +
	 * "\\" + filename + ".png"), true); } catch (Exception e) {
	 * e.printStackTrace(); } return filename.toString(); }
	 */

	private String getTestCaseName(Exception ExcptnMsg) {
		String testCaseName = null;
		if (SelfHealingUtil.tstCseNmeTstNG.get() == null) {

			StackTraceElement[] excptnStackTraceElements = Thread.currentThread().getStackTrace();

			int stackClassLength = excptnStackTraceElements.length;
			String parentClass = excptnStackTraceElements[stackClassLength - 1].toString();
			String[] parenteleArray = parentClass.split("[.]");
			int parentlength = parenteleArray.length;
			testCaseName = parenteleArray[parentlength - 3];

		} else {
			testCaseName = SelfHealingUtil.tstCseNmeTstNG.get();
			// System.out.println("Testcase name in get/testcasename :" +
			// tstCseNmeTstNG.get());
		}
		System.out.println("Testcase name :" + testCaseName);
		return testCaseName;
	}

	public String gettestStepAction(Exception ExcptnMsg) {
		String stepActionName = null;
		StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
		for (StackTraceElement strClasses : stackTraceElements) {// System.out.println("stack trace classes to
																	// string"+strClasses.toString());
			if (strClasses.toString().contains("MobileHandlers")) {
				String stepActionClass = strClasses.toString();
				String[] stepActions = stepActionClass.split("[.]");
				int stepActionLength = stepActions.length;
				String stepActionNames = stepActions[stepActionLength - 2];
				// System.out.println("Step Action name :" + stepActionNames);
				String[] stepActionNme = stepActionNames.toString().split("[(]");
				stepActionName = stepActionNme[stepActionNme.length - 2];

			}

		}
		return stepActionName;
	}

	public ElementValue verifyElement(ConcurrentHashMap<String, String> attributeMap, MobileDriver ldriver) {
		ConcurrentHashMap<Integer, ElementValue> vrfyEleNoShEleMap = new ConcurrentHashMap<Integer, ElementValue>();
		ConcurrentHashMap<String, String> vrfyEleNoShJsonEleMap = new ConcurrentHashMap<String, String>();
		ConcurrentHashMap<String, String> jsonMap = new ConcurrentHashMap<String, String>();
		String locatorType = attributeMap.get("LocatorType");
		ElementValue vrfyEleVle = new ElementValue();
		try {

			// System.out.println("Attribute map in verify element :" + attributeMap);
			// System.out.println("Locator type in verify element : " + locatorType);
			Set<String> keys = attributeMap.keySet();
			keys.parallelStream().forEach((key) -> {
				try {
					if (!key.equals(locatorType)) {

						switch (key) {
						case "id":
							// System.out.println("\n" + "Driver: " + ldriver);
							// System.out.println("checking using id : " + attributeMap.get("id"));
							locator_id = (MobileElement) ldriver.findElement(By.id(attributeMap.get("id")));
							// System.out.println("id Locator value in verify element : " + locator_id);
							ElementValue elementValue1 = new ElementValue("id", attributeMap.get("id"), locator_id);
							vrfyEleNoShEleMap.put(1, elementValue1);
							jsonMap.put("id", attributeMap.get("id"));
							verifyeleFound = true;

							break;

						case "name":
							// System.out.println("\n" + "Driver: " + ldriver);
							// System.out.println("checking using name : " + attributeMap.get("name"));
							locator_name = (MobileElement) ldriver.findElement(By.name(attributeMap.get("name")));
							// System.out.println("verified by name : " + attributeMap.get("name"));
							ElementValue elementValue2 = new ElementValue("name", attributeMap.get("name"),
									locator_name);
							vrfyEleNoShEleMap.put(2, elementValue2);
							jsonMap.put("name", attributeMap.get("name"));
							verifyeleFound = true;
							break;

						case "xpath_Follows":
							if (!(locatorType.equals("id") && attributeMap.get("xpath_Follows").contains("id"))) {
								// System.out.println("\n" + "Driver: " + ldriver);
								// System.out.println("checking using xpath_Follows : " +
								// attributeMap.get("xpath_Follows"));
								locator_xpath_Follows = (MobileElement) ldriver
										.findElement(By.xpath(attributeMap.get("xpath_Follows")));
								ElementValue elementValue3 = new ElementValue("xpath",
										attributeMap.get("xpath_Follows"), locator_xpath_Follows);
								vrfyEleNoShEleMap.put(3, elementValue3);
								jsonMap.put("xpath_Follows", attributeMap.get("xpath_Follows"));
								verifyeleFound = true;
							}
							break;

						case "xpath_default":
							if (!(locatorType.equals("id") && attributeMap.get("xpath_default").contains("id"))) {
								// System.out.println("\n" + "Driver: " + ldriver);
								// System.out.println("checking using xpath_default: " +
								// attributeMap.get("xpath_default"));
								locator_xpath_default = (MobileElement) ldriver
										.findElement(By.xpath(attributeMap.get("xpath_default")));
								ElementValue elementValue4 = new ElementValue("xpath",
										attributeMap.get("xpath_default"), locator_xpath_default);
								vrfyEleNoShEleMap.put(4, elementValue4);
								jsonMap.put("xpath_default", attributeMap.get("xpath_default"));
								verifyeleFound = true;
							}
							break;

						case "xpath_Parent":
							if (!(locatorType.equals("id") && attributeMap.get("xpath_Parent").contains("id"))) {
								// System.out.println("\n" + "Driver: " + ldriver);
								// System.out.println("checking using xpath_Parent " +
								// attributeMap.get("xpath_Parent"));
								locator_xpath_Parent = (MobileElement) ldriver
										.findElement(By.xpath(attributeMap.get("xpath_Parent")));
								ElementValue elementValue5 = new ElementValue("xpath", attributeMap.get("xpath_Parent"),
										locator_xpath_Parent);
								vrfyEleNoShEleMap.put(5, elementValue5);
								jsonMap.put("xpath_Parent", attributeMap.get("xpath_Parent"));
								verifyeleFound = true;
							}
							break;

						case "xpath_Attribute":
							if (!(locatorType.equals("id") && attributeMap.get("xpath_Attribute").contains("id"))) {
								// System.out.println("\n" + "Driver: " + ldriver);
								// System.out.println("checking using xpath_Attribute " +
								// attributeMap.get("xpath_Attribute"));
								locator_xpath_Attribute = (MobileElement) ldriver
										.findElement(By.xpath(attributeMap.get("xpath_Attribute")));
								ElementValue elementValue6 = new ElementValue("xpath",
										attributeMap.get("xpath_Attribute"), locator_xpath_Attribute);
								vrfyEleNoShEleMap.put(6, elementValue6);
								jsonMap.put("xpath_Attribute", attributeMap.get("xpath_Attribute"));
								verifyeleFound = true;
							}
							break;

						case "xpath_Descendant":
							if (!(locatorType.equals("id") && attributeMap.get("xpath_Descendant").contains("id"))) {
								// System.out.println("\n" + "Driver: " + ldriver);
								// System.out.println("checking using xpath_Descendant " +
								// attributeMap.get("xpath_Descendant"));
								locator_xpath_Descendant = (MobileElement) ldriver
										.findElement(By.xpath(attributeMap.get("xpath_Descendant")));
								ElementValue elementValue7 = new ElementValue("xpath",
										attributeMap.get("xpath_Descendant"), locator_xpath_Descendant);
								vrfyEleNoShEleMap.put(7, elementValue7);
								jsonMap.put("xpath_Descendant", attributeMap.get("xpath_Descendant"));
								verifyeleFound = true;
							}
							break;

						case "xpath_Position":
							if (!(locatorType.equals("id") && attributeMap.get("xpath_Position").contains("id"))) {
								// System.out.println("\n" + "Driver: " + ldriver);
								// System.out.println("checking using xpath_Position " +
								// attributeMap.get("xpath_Position"));
								locator_xpath_Position = (MobileElement) ldriver
										.findElement(By.xpath(attributeMap.get("xpath_Position")));
								ElementValue elementValue8 = new ElementValue("xpath",
										attributeMap.get("xpath_Position"), locator_xpath_Position);
								vrfyEleNoShEleMap.put(8, elementValue8);
								jsonMap.put("xpath_Position", attributeMap.get("xpath_Position"));
								verifyeleFound = true;
							}
							break;

						case "xpath_fullXPath":
							if (!(locatorType.equals("id") && attributeMap.get("xpath_fullXPath").contains("id"))) {
								// System.out.println("\n" + "Driver: " + ldriver);
								// System.out.println("checking using xpath_fullXPath : " +
								// attributeMap.get("xpath_fullXPath"));
								locator_xpath_fullXPath = (MobileElement) ldriver
										.findElement(By.xpath(attributeMap.get("xpath_fullXPath")));
								ElementValue elementValue9 = new ElementValue("xpath",
										attributeMap.get("xpath_fullXPath"), locator_xpath_fullXPath);
								vrfyEleNoShEleMap.put(9, elementValue9);
								jsonMap.put("xpath_fullXPath", attributeMap.get("xpath_fullXPath"));
								verifyeleFound = true;
							}
							break;

						case "xpath_Contains":
							if (!(locatorType.equals("id") && attributeMap.get("xpath_Contains").contains("id"))) {
								// System.out.println("\n" + "Driver: " + ldriver);
								// System.out.println("checking using xpath_Contains " +
								// attributeMap.get("xpath_Contains"));
								locator_xpath_Contains = (MobileElement) ldriver
										.findElement(By.xpath(attributeMap.get("xpath_Contains")));
								ElementValue elementValue10 = new ElementValue("xpath",
										attributeMap.get("xpath_Contains"), locator_xpath_Contains);
								vrfyEleNoShEleMap.put(10, elementValue10);
								jsonMap.put("xpath_Contains", attributeMap.get("xpath_Contains"));
								verifyeleFound = true;
							}
							break;

						case "xpath_Precedance":
							if (!(locatorType.equals("id") && attributeMap.get("xpath_Precedance").contains("id"))) {
								// System.out.println("\n" + "Driver: " + ldriver);
								// System.out.println("checking using xpath_Precedance : " +
								// attributeMap.get("xpath_Precedance"));
								locator_xpath_Precedance = (MobileElement) ldriver
										.findElement(By.xpath(attributeMap.get("xpath_Precedance")));
								ElementValue elementValue11 = new ElementValue("xpath",
										attributeMap.get("xpath_Precedance"), locator_xpath_Precedance);
								vrfyEleNoShEleMap.put(11, elementValue11);
								jsonMap.put("xpath_Precedance", attributeMap.get("xpath_Precedance"));
								verifyeleFound = true;
							}
							break;

						case "xpath_Ancestor":
							if (!(locatorType.equals("id") && attributeMap.get("xpath_Ancestor").contains("id"))) {
								// System.out.println("\n" + "Driver: " + ldriver);
								// System.out.println("checking using xpath_Ancestor " +
								// attributeMap.get("xpath_Ancestor"));
								locator_xpath_Ancestor = (MobileElement) ldriver
										.findElement(By.xpath(attributeMap.get("xpath_Ancestor")));
								ElementValue elementValue12 = new ElementValue("xpath",
										attributeMap.get("xpath_Ancestor"), locator_xpath_Ancestor);
								vrfyEleNoShEleMap.put(12, elementValue12);
								jsonMap.put("xpath_Ancestor", attributeMap.get("xpath_Ancestor"));
								verifyeleFound = true;
							}
							break;

						case "xpath_child":
							if (!(locatorType.equals("id") && attributeMap.get("xpath_child").contains("id"))) {
								// System.out.println("\n" + "Driver: " + ldriver);
								// System.out.println("checking using xpath_child : " +
								// attributeMap.get("xpath_child"));
								locator_xpath_child = (MobileElement) ldriver
										.findElement(By.xpath(attributeMap.get("xpath_child")));
								ElementValue elementValue13 = new ElementValue("xpath", attributeMap.get("xpath_child"),
										locator_xpath_child);
								vrfyEleNoShEleMap.put(13, elementValue13);
								jsonMap.put("xpath_child", attributeMap.get("xpath_child"));
								verifyeleFound = true;
							}
							break;

						case "xpath":
							if (!(locatorType.equals("id") && attributeMap.get("xpath").contains("id"))) {
								// System.out.println("\n" + "Driver: " + ldriver);
								// System.out.println("checking using xpath_child : " +
								// attributeMap.get("xpath_child"));
								locator_xpath = (MobileElement) ldriver
										.findElement(By.xpath(attributeMap.get("xpath")));
								ElementValue elementValue14 = new ElementValue("xpath", attributeMap.get("xpath"),
										locator_xpath);
								vrfyEleNoShEleMap.put(14, elementValue14);
								jsonMap.put("xpath", attributeMap.get("xpath"));
								verifyeleFound = true;
							}
							break;

						case "css":
							// System.out.println("\n" + "Driver: " + ldriver);
							// System.out.println("checking using css : " + attributeMap.get("css"));
							locator_css = (MobileElement) ldriver.findElement(By.cssSelector(attributeMap.get("css")));
							// System.out.println("verified by css" + attributeMap.get("css"));
							ElementValue elementValue15 = new ElementValue("xpath", attributeMap.get("css"),
									locator_css);
							vrfyEleNoShEleMap.put(15, elementValue15);
							jsonMap.put("css", attributeMap.get("css"));
							verifyeleFound = true;
							break;
						case "class":
							// System.out.println("\n" + "Driver: " + ldriver);
							// System.out.println("checking using class : " +
							// attributeMap.get("classname"));
							locator_class = (MobileElement) ldriver
									.findElement(By.className(attributeMap.get("classname")));
							// System.out.println("verified by class" + attributeMap.get("classname"));
							ElementValue elementValue16 = new ElementValue("xpath", attributeMap.get("class"),
									locator_class);
							vrfyEleNoShEleMap.put(16, elementValue16);
							jsonMap.put("class", attributeMap.get("class"));
							verifyeleFound = true;
							break;
						}
					}
				} catch (Exception e) {
					// System.out.println("Exception in verfy element :" + e);
				}
				/*
				 * if (eleFound) // if expected element is obtained, will go out from the for
				 * loop { break;
				 * 
				 * }
				 */
			});
			// code added to handle element not found condition
			// System.out.println("value of element found : " + verifyeleFound);

		} catch (Exception e) {
			LOGGER.error("Error while verifying element" + e);
		}
		if (!verifyeleFound) {
			locator = null;
		} else {
			try {
				System.out.println("Alternate locators :"+vrfyEleNoShEleMap);
				ElementValue elementSortedValue = new ElementValue();
				Map<Integer, ElementValue> result = vrfyEleNoShEleMap.entrySet().stream()
						.sorted(Map.Entry.comparingByKey()).collect(Collectors.toMap(Map.Entry::getKey,
								Map.Entry::getValue, (oldValue, newValue) -> oldValue, LinkedHashMap::new));
				// System.out.println("result : " + result);

				elementSortedValue = (new ArrayList<ElementValue>(result.values())).get(0);

				vrfyEleNoShJsonEleMap.put("key", elementSortedValue.eleType);
				vrfyEleNoShJsonEleMap.put("value", elementSortedValue.eleValue);

				// System.out.println("Value to be updated in json : "+vrfyEleNoShJsonEleMap);
				jsonObjectParser.jsonMapper(attributeMap.get("PageName"), attributeMap.get("ElementName"),
						elementSortedValue.eleType, vrfyEleNoShJsonEleMap);
				locator = (MobileElement) elementSortedValue.eleLocVle;
				vrfyEleVle.eleType = elementSortedValue.eleType;
				vrfyEleVle.eleValue = elementSortedValue.eleValue;
				vrfyEleVle.locatorsMap = jsonMap;

			} catch (Exception e) {
				System.out.println("Exception while updating json : " + e);
			}
		}
		vrfyEleVle.eleLocVle = locator;
		return vrfyEleVle;
	}

	

	private ConcurrentHashMap<String, String> getPgAndElmtName(MobileElement ele, String excMsg) {
		ConcurrentHashMap<String, String> PageEleMap = new ConcurrentHashMap<>();
		String eleFlag = null;
		StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
		for (StackTraceElement strClasses : stackTraceElements) {
			if (strClasses.getClassName().contains("Page") || strClasses.getClassName().contains("page")) {
				try {
					String strClass = strClasses.getClassName();
					Class cls = Class.forName(strClass);
					ClassLoader cLoader = cls.getClassLoader();
					Field[] classElements = cls.getDeclaredFields();

					if (excMsg.contains("By.chained")) {
						String eleDetails = ele.toString();
						// System.out.println("Full element details"+eleDetails);
						eleDetails = eleDetails.substring(1, eleDetails.length() - 1); // comment for Android/IOS web
						// System.out.println("Element Details :"+eleDetails);
						String[] data = null;
						data = eleDetails.split("By.")[2].split(":"); // added for Android native
						for (String Datas : data) {
							// System.out.println(Datas);
						}
						String locatorType = data[0];
						String[] locatorValues = data[1].split("}"); // added for android native
						String locatorValue = locatorValues[0].trim(); // added for android native
						PageEleMap.put("locatorValue", locatorValue);
						PageEleMap.put("locatorType", locatorType);
						for (Field classElement : classElements) {
							if (classElement.getType().toString().contains("AndroidElement")) { // changed to android
																								// element for android
																								// native
								String elementName = classElement.toString();
								// System.out.println("Element name passing to annotation:"+elementName);
								AndroidFindBy findByAnnotation = classElement.getAnnotation(AndroidFindBy.class); // check
																													// here
								// System.out.println("Annotation value is :"+findByAnnotation);
								PageEleMap.putAll(identifyAndroidElement(findByAnnotation, elementName, PageEleMap));
								eleFlag = PageEleMap.get("status");
								if (eleFlag == "true")
									break;
							}
						}
					} else {
						String eleDetails = ele.toString();
						// System.out.println("Element Details :"+eleDetails);
						String[] data = null;
						data = eleDetails.split("By.")[1].split(":");// commented for android native

						for (String Datas : data) {
							// System.out.println(Datas);
						}
						String locatorType = data[0];
						String locatorValue = data[1];
						PageEleMap.put("locatorValue", locatorValue);
						PageEleMap.put("locatorType", locatorType);
						for (Field classElement : classElements) {
							if (classElement.getType().toString().contains("MobileElement")
									|| classElement.getType().toString().contains("AndroidElement")
									|| classElement.getType().toString().contains("IOSElement")) { // changed to android
																									// element for
																									// android native
								String elementName = classElement.toString();
								// System.out.println("Element name passing to annotation:"+elementName);
								FindBy findByAnnotation = classElement.getAnnotation(FindBy.class); // check here
								// System.out.println("Annotation value is :"+findByAnnotation);
								PageEleMap.putAll(identifyElement(findByAnnotation, elementName, PageEleMap));
								eleFlag = PageEleMap.get("status");
								if (eleFlag == "true")
									break;
							}
						}
					}

				} catch (Exception e1) {
					e1.printStackTrace();

				}
			}
		}
		return PageEleMap;
	}

	private ConcurrentHashMap<String, String> identifyAndroidElement(AndroidFindBy findByAnnotation, String eleNme,
			ConcurrentHashMap<String, String> PgeEleVleMap) {
		ConcurrentHashMap<String, String> PgeIdentifyEleMap = new ConcurrentHashMap<>();
		switch (PgeEleVleMap.get("locatorType")) {
		case "xpath":
			if (!findByAnnotation.xpath().isEmpty()
					&& findByAnnotation.xpath().trim().equals(PgeEleVleMap.get("locatorValue")))
				PgeIdentifyEleMap = setPgAndElmtMap(eleNme);
			break;
		case "css selector":
			if (!((FindBy) findByAnnotation).css().isEmpty()
					&& ((FindBy) findByAnnotation).css().trim().equals(PgeEleVleMap.get("locatorValue")))
				PgeIdentifyEleMap = setPgAndElmtMap(eleNme);
			break;
		case "id":
			if (!findByAnnotation.id().isEmpty()
					&& findByAnnotation.id().trim().equals(PgeEleVleMap.get("locatorValue")))
				PgeIdentifyEleMap = setPgAndElmtMap(eleNme);
			break;
		case "tag name":
			if (!findByAnnotation.tagName().isEmpty()
					&& findByAnnotation.tagName().trim().equals(PgeEleVleMap.get("locatorValue")))
				PgeIdentifyEleMap = setPgAndElmtMap(eleNme);
			break;
		case "name":
			if (!((FindBy) findByAnnotation).name().isEmpty()
					&& ((FindBy) findByAnnotation).name().trim().equals(PgeEleVleMap.get("locatorValue")))
				PgeIdentifyEleMap = setPgAndElmtMap(eleNme);
			break;
		case "link text":
			if (!((FindBy) findByAnnotation).linkText().isEmpty()
					&& ((FindBy) findByAnnotation).linkText().trim().equals(PgeEleVleMap.get("locatorValue")))
				PgeIdentifyEleMap = setPgAndElmtMap(eleNme);
			break;
		case "class name":
			if (!findByAnnotation.className().isEmpty()
					&& findByAnnotation.className().trim().equals(PgeEleVleMap.get("locatorValue")))
				PgeIdentifyEleMap = setPgAndElmtMap(eleNme);
			break;
		}
		return PgeIdentifyEleMap;
	}

	private ConcurrentHashMap<String, String> identifyElement(FindBy findByAnnotation, String eleNme,
			ConcurrentHashMap<String, String> PgeEleVleMap) {
		ConcurrentHashMap<String, String> PgeIdentifyEleMap = new ConcurrentHashMap<>();
		// System.out.println("Locator value in identifyElement : "+locatorValue);
		// System.out.println("Locator type in identifyElement : "+locatorType);
		// System.out.println("Element name in identifyElement "+eleNme);
		switch (PgeEleVleMap.get("locatorType")) {
		case "xpath":
			if (!findByAnnotation.xpath().isEmpty()
					&& findByAnnotation.xpath().trim().equals(PgeEleVleMap.get("locatorValue").trim()))
				PgeIdentifyEleMap = setPgAndElmtMap(eleNme);
			break;
		case "css selector":
			if (!findByAnnotation.css().isEmpty()
					&& findByAnnotation.css().trim().equals(PgeEleVleMap.get("locatorValue").trim()))
				PgeIdentifyEleMap = setPgAndElmtMap(eleNme);
			break;
		case "id":
			if (!findByAnnotation.id().isEmpty()
					&& findByAnnotation.id().trim().equals(PgeEleVleMap.get("locatorValue").trim()))
				PgeIdentifyEleMap = setPgAndElmtMap(eleNme);
			break;
		case "tag name":
			if (!findByAnnotation.tagName().isEmpty()
					&& findByAnnotation.tagName().trim().equals(PgeEleVleMap.get("locatorValue").trim()))
				PgeIdentifyEleMap = setPgAndElmtMap(eleNme);
			break;
		case "name":
			
			if (!findByAnnotation.name().isEmpty()
					&& findByAnnotation.name().trim().equals(PgeEleVleMap.get("locatorValue").trim()))
				PgeIdentifyEleMap = setPgAndElmtMap(eleNme);
			break;
		case "link text":
			if (!findByAnnotation.linkText().isEmpty()
					&& findByAnnotation.linkText().trim().equals(PgeEleVleMap.get("locatorValue").trim()))
				PgeIdentifyEleMap = setPgAndElmtMap(eleNme);
			break;
		case "class name":
			if (!findByAnnotation.className().isEmpty()
					&& findByAnnotation.className().trim().equals(PgeEleVleMap.get("locatorValue").trim()))
				PgeIdentifyEleMap = setPgAndElmtMap(eleNme);
			break;
		}
		// System.out.println("Page details inside Identify element :" +PgeEleMap);
		return PgeIdentifyEleMap;

	}

	public ConcurrentHashMap<String, String> setPgAndElmtMap(String eleNme) {
		ConcurrentHashMap<String, String> PageElementMap = new ConcurrentHashMap<>();
		// System.out.println("eleNme in setPgAndElmtMap :"+eleNme);
		String[] eleArray = eleNme.split("[.]");
		int length = eleNme.split("[.]").length;
		PageElementMap.put("PageName", eleArray[length - 2]);
		PageElementMap.put("ElementName", eleArray[length - 1]);
		 System.out.println("Pagename is : " + PageElementMap.get("PageName"));
		LOGGER.info("Pagename is : " + PageElementMap.get("PageName"));
		// log file for self healing
		 System.out.println("Element name is : " + PageElementMap.get("ElementName"));
		LOGGER.info("Element name is : " + PageElementMap.get("ElementName"));
		PageElementMap.put("status", "true");
		// System.out.println("Values in setPgAndElmtMap : "+PageElementMap);
		return PageElementMap;
	}

}
