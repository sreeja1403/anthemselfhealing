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
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.FindBy;

import com.scripted.jsonParser.JsonObjectParser;
import com.scripted.jsonWriter.JsonHealingInfoWriter;
import com.scripted.reporting.selfhealing.ReportGenerationDummy;

public class SMWebHealer {

	public static Logger LOGGER = Logger.getLogger(SMWebHealer.class);
	JsonObjectParser jsonObjectParser = new JsonObjectParser();
	// static WebDriver driver;
	static String Technology = "web";
	public static ThreadLocal<WebDriver> lthdriver = new ThreadLocal<>();
	public static ThreadLocal<RemoteWebDriver> thdriver = new ThreadLocal<>();
	public static ConcurrentHashMap<String, ElementDetails> healEleData = new ConcurrentHashMap<>();

	// Parallely check locators
	WebElement locator = null;
	WebElement locator_id = null;
	WebElement locator_name = null;
	WebElement locator_xpath = null;
	WebElement locator_css = null;
	WebElement locator_class = null;
	WebElement locator_xpath_Follows = null;
	WebElement locator_xpath_default = null;
	WebElement locator_xpath_Parent = null;
	WebElement locator_xpath_Attribute = null;
	WebElement locator_xpath_Descendant = null;
	WebElement locator_xpath_Position = null;
	WebElement locator_xpath_fullXPath = null;
	WebElement locator_xpath_Contains = null;
	WebElement locator_xpath_Precedance = null;
	WebElement locator_xpath_Ancestor = null;
	WebElement locator_xpath_child = null;

	// String strClass = "";

	boolean vrfyEle = false;
	boolean alrtSts = false;
	boolean wndwSts = false;

	static boolean healStsFlg = false;

	// Element Healing Parameters
	int count;
	long maxCount = 0;
	long maxCountCheck = 0;

	public static ThreadLocal<String> testNme = new ThreadLocal<String>();
	private static int staleCount;
	private static int timeOutCount;

	public ThreadLocal<String> getTstCseNmeTstNG() {
		return testNme;
	}

	public void setTstCseNmeTstNG(String testcasename) {
		testNme.set(testcasename);
	}

	String testCseNme = "";

	private static String cdir = System.getProperty("user.dir");

	static List<String> networkMsgs = new ArrayList<String>();

	public static boolean shFlg = false;
	public static boolean imptFlg = false;

	public boolean isShflag() {
		return shFlg;
	}

	public void setShflag(boolean shflag) {
		this.shFlg = shflag;
	}

	public void setImpactflag(boolean impactflag) {
		this.imptFlg = impactflag;
	}

	public boolean isImpactflag() {
		return imptFlg;
	}

	public SMWebHealer() {

	}

	SelfHealingUtil util = new SelfHealingUtil();

	public long maxCountCheck(String excptn) {
		if (excptn == "stale") {
			maxCount = staleCount;
		} else if (excptn == "timeout") {
			maxCount = timeOutCount;
		}
		if (maxCount == 0) {
			maxCount = 1;
		}
		return maxCount;
	}

	public boolean gethealStatusFlag() {
		return healStsFlg;
	}

	public WebElement initiateHealing(Exception Msg, WebElement locator, ThreadLocal<WebDriver> lthwebdriver) {
		ConcurrentHashMap<String, String> healRptInfo = new ConcurrentHashMap<>();
		boolean exChkSts = false;
		healStsFlg = true;
		String exTyp = Msg.getClass().getSimpleName();
		WebElement healLoc = null;
		setlthWebdriver(lthwebdriver);
		healRptInfo.put("TestCase", getTestCaseName(Msg));
		healRptInfo.put("stepActionName", gettestStepAction(Msg));
		healRptInfo.put("ExcptnMsg", Msg.toString());
		healRptInfo.putAll(getPgAndElmtName(locator));
		healRptInfo.put("ExceptionType", exTyp);
		String pgEleDtls = healRptInfo.get("PageName") + "_" + healRptInfo.get("ElementName");
		/**
		 * 
		 * Check whether exception occurred already for same element
		 */
		if (healEleData.containsKey(pgEleDtls)) {
			do {
				LOGGER.info("Status :" + healEleData.get(pgEleDtls).getStatus());
			} while (healEleData.get(pgEleDtls).getStatus().equalsIgnoreCase("inprogress"));

			if ((healEleData.get(pgEleDtls).getExceptionType().equalsIgnoreCase("NoSuchElementException")
					&& healRptInfo.get("ExcptnMsg").contains("NoSuchElementException"))
					|| (healEleData.get(pgEleDtls).getExceptionType().equalsIgnoreCase("InvalidSelectorException")
							&& healRptInfo.get("ExcptnMsg").contains("InvalidSelectorException")))

			{
				ConcurrentHashMap<String, Integer> countMap = new ConcurrentHashMap<String, Integer>();
				countMap.put("NoSuchElement", 1);
				ConcurrentHashMap<String, String> jsonDataMap = new ConcurrentHashMap<String, String>();
				if (healEleData.get(pgEleDtls).getStatus().equalsIgnoreCase("success")) {
					WebElement verifyHldLctr = verifyElement(healRptInfo, healEleData.get(pgEleDtls).getHealedLoc());
					if (verifyHldLctr != null) {
						healRptInfo.put("HealStatus", "success");
						healRptInfo.put("HealdEleTyp", healEleData.get(pgEleDtls).getHealedEleTyp());
						healRptInfo.put("HealedLocator", healEleData.get(pgEleDtls).getHealedEleVle());
						healRptInfo.put("ImageFileName", healEleData.get(pgEleDtls).getScrnShtPath());
						jsonDataMap = healEleData.get(pgEleDtls).getLocDataMap();
						JsonHealingInfoWriter.AddStep(healRptInfo, getHealCountInfoCheck(countMap), jsonDataMap,
								Technology);
					}
					return verifyHldLctr;
				}
				if (healEleData.get(pgEleDtls).getStatus().equalsIgnoreCase("failure")) {
					healRptInfo.put("HealStatus", "failure");
					JsonHealingInfoWriter.AddStep(healRptInfo, getHealCountInfoCheck(countMap), jsonDataMap,
							Technology);
					return null;
				}

			}
		} else {
			ElementDetails eleDtls = new ElementDetails();
			eleDtls.setStatus("InProgress");
			eleDtls.setFailedLoc(healRptInfo.get("locatorType") + ":" + healRptInfo.get("locatorValue"));
			healEleData.put(pgEleDtls, eleDtls);
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
			healLoc = null;

		}

		return healLoc;
	}

	private void setlthWebdriver(ThreadLocal<WebDriver> webdriver) {
		lthdriver = webdriver;

	}

	private WebDriver getlthWebdriver() {
		return lthdriver.get();

	}

	private WebElement unhandledAlertCheck(WebElement alertLocator, ConcurrentHashMap<String, String> healCmmnData) {
		ConcurrentHashMap<String, String> healInfoAlrtChk = new ConcurrentHashMap<>();
		String alertText = "";
		int othersCount = 0;
		healInfoAlrtChk.putAll(healCmmnData);
		WebElement alertHldLctr;
		try {
			othersCount++;
			ConcurrentHashMap<String, Integer> alrtHealCntInfo = new ConcurrentHashMap<String, Integer>();
			alrtHealCntInfo.put("othersCount", othersCount);
			Alert alert = getlthWebdriver().switchTo().alert();
			alertText = alert.getText();
			for (String acptAlrtTxt : HealingConfig.acptAlrtTxts) {
				if (alert.getText().equalsIgnoreCase(acptAlrtTxt)) {
					alert.accept();
					healInfoAlrtChk.put("HealedLocator", "Alert accepted");
					alrtSts = true;
					break;
				}
			}
			if (!alrtSts) {
				for (String dsmsAlrtTxt : HealingConfig.dsmsAlrtTxts) {
					if (alert.getText().equalsIgnoreCase(dsmsAlrtTxt)) {
						alert.dismiss();
						alrtSts = true;
						healInfoAlrtChk.put("HealedLocator", "Alert dismissed");
						break;
					}
				}
			}
			alertHldLctr = alertLocator;
			healInfoAlrtChk.put("ExceptionType", "UnhandledAlertException");
			ConcurrentHashMap<String, String> jsonMap = new ConcurrentHashMap<>();
			jsonMap.put("Alert", alertText);
			healInfoAlrtChk.put("HealdEleTyp", "Alert");
			healInfoAlrtChk.put("locatorType", " ");
			healInfoAlrtChk.put("locatorValue", alertText);
			if (alrtSts) {
				healInfoAlrtChk.put("HealStatus", "success"); // add to write file;
				JsonHealingInfoWriter.AddStep(healInfoAlrtChk, getHealCountInfoCheck(alrtHealCntInfo), jsonMap,
						Technology);
			} else {
				healInfoAlrtChk.put("HealStatus", "failure"); // add to write file;
				JsonHealingInfoWriter.AddStep(healInfoAlrtChk, getHealCountInfoCheck(alrtHealCntInfo), jsonMap,
						Technology);

			}

		} catch (Exception Ex) {
			alertHldLctr = null;
		}
		return alertHldLctr;
	}

	private WebElement staleChk(WebElement staleLocator, ConcurrentHashMap<String, String> healCmmnData) {
		int staleCount = 0;
		ConcurrentHashMap<String, String> healInfoStleChk = new ConcurrentHashMap<>();
		ConcurrentHashMap<String, Integer> stleHealCntInfo = new ConcurrentHashMap<String, Integer>();
		healInfoStleChk.putAll(healCmmnData);
		WebElement staleHldLctr = null;
		staleCount++;
		stleHealCntInfo.put("staleTimeoutCount", staleCount);
		healInfoStleChk.put("ExceptionType", "StaleElementReferenceException");

		try {
			util.webWaitForPresence(HealingConfig.staleTime, staleLocator, lthdriver.get());
			staleHldLctr = staleLocator;
		} catch (Exception e) {
			if (e.getClass().getSimpleName().equalsIgnoreCase(CnstLookup.NOSUCHELE))
				noSuchEleExcChk(healInfoStleChk, staleLocator);
			else
				staleHldLctr = null;
		}
		if (staleHldLctr != null)
			healInfoStleChk.put("HealedLocatorVle", getHealedLocatorValue(staleHldLctr));
		healInfoStleChk.put("HealdEleTyp", healCmmnData.get("locatorType"));
		ConcurrentHashMap<String, String> jsonMap = new ConcurrentHashMap<>();
		jsonMap.put(healCmmnData.get("locatorValue"), healCmmnData.get("locatorType"));
		getReportValues(staleHldLctr, healInfoStleChk, getHealCountInfoCheck(stleHealCntInfo), jsonMap);
		return staleHldLctr;
	}

	private String getHealedLocatorValue(WebElement HealedlocatorVle) {
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

	private WebElement timeoutCheck(WebElement timeLocator, ConcurrentHashMap<String, String> healCmmnDataMap) {
		ConcurrentHashMap<String, String> healInfoTmeOutChk = new ConcurrentHashMap<>();
		ConcurrentHashMap<String, Integer> tmeOutHealCntInfo = new ConcurrentHashMap<String, Integer>();
		healInfoTmeOutChk.putAll(healCmmnDataMap);
		WebElement timeOutHldlctr = null;
		count = 0;
		int TimeoutCount = 0;
		TimeoutCount++;
		tmeOutHealCntInfo.put("staleTimeoutCount", TimeoutCount);
		healInfoTmeOutChk.put("ExceptionType", "TimeoutException");
		healInfoTmeOutChk.putAll(getPgAndElmtName(timeLocator));
		try {
			util.webWaitForPresence(HealingConfig.staleTime, timeLocator, lthdriver.get());
			timeOutHldlctr = timeLocator;
		} catch (Exception e) {
			if (e.getClass().getSimpleName().equalsIgnoreCase(CnstLookup.NOSUCHELE))
				noSuchEleExcChk(healInfoTmeOutChk, timeLocator);
			else
				timeOutHldlctr = null;
		}
		if (timeOutHldlctr != null) {
			healInfoTmeOutChk.put("HealedLocatorVle", getHealedLocatorValue(timeOutHldlctr));
		}
		ConcurrentHashMap<String, String> jsonMap = new ConcurrentHashMap<>();
		jsonMap.put(healCmmnDataMap.get("locatorValue"), healCmmnDataMap.get("locatorType"));
		getReportValues(timeOutHldlctr, healInfoTmeOutChk, getHealCountInfoCheck(tmeOutHealCntInfo), jsonMap);
		return timeOutHldlctr;
	}

	private WebElement verifyElement(String ele) {
		WebElement verifyHldLctr = null;
		try {
			switch (ele.split(":")[0]) {
			case "id":
				verifyHldLctr = getlthWebdriver().findElement(By.id(ele.split(":")[1]));
				break;
			case "name":
				verifyHldLctr = getlthWebdriver().findElement(By.name(ele.split(":")[1]));
				break;
			case "xpath":
				verifyHldLctr = getlthWebdriver().findElement(By.xpath(ele.split(":")[1]));
				break;
			case "classname":
				verifyHldLctr = getlthWebdriver().findElement(By.className(ele.split(":")[1]));
				break;
			case "css":
				verifyHldLctr = getlthWebdriver().findElement(By.cssSelector(ele.split(":")[1]));
				break;
			}

		} catch (Exception e) {
			return verifyHldLctr;
		}

		return verifyHldLctr;
	}

	private WebElement verifyElement(ConcurrentHashMap<String, String> vrfyCheckMap, WebElement ele) {
		WebElement verifyHldLctr = null;
		Boolean vereleFound = false;
		try {
			String[] attrValue = ele.toString().split("->")[1].split(":");
			switch (attrValue[0].trim()) {
			case "id":
				verifyHldLctr = getlthWebdriver().findElement(By.id(attrValue[1].split("]")[0].trim()));
				vereleFound = true;
				break;
			case "name":
				verifyHldLctr = getlthWebdriver().findElement(By.name(attrValue[1].split("]")[0].trim()));
				vereleFound = true;
				break;
			case "xpath":
				verifyHldLctr = getlthWebdriver().findElement(By.xpath(attrValue[1].split("]")[0].trim() + "]"));
				vereleFound = true;
				break;
			case "class name":
				verifyHldLctr = getlthWebdriver().findElement(By.className(attrValue[1].split("]")[0].trim()));
				vereleFound = true;
				break;

			}
		} catch (Exception e) {
			// System.out.println("Exception : " + e);
			/*
			 * if (e.toString().contains("NoSuchElementException") ||
			 * e.toString().contains("InvalidSelectorException")) {
			 * vrfyCheckMap.put("ExcptnMsg", e.toString()); noSuchEleExcCheck(vrfyCheckMap,
			 * ele);
			 */
			// }
			// verifyHldLctr = null;
		}
		return verifyHldLctr;
	}

	/**
	 * 
	 * @param noSuchExcMap
	 * @param ele
	 * @return
	 */
	private WebElement noSuchEleExcChk(ConcurrentHashMap<String, String> noSuchExcMap, WebElement ele) {
		int noSuchEleCnt = 0;
		ElementValue vrfyValue = new ElementValue();
		Boolean selAttrFlag = false;
		ConcurrentHashMap<String, String> ntwrkDtMap = new ConcurrentHashMap<>();
		ConcurrentHashMap<String, String> jsonVlsMap = new ConcurrentHashMap<>();
		Boolean iframeCheck = true;
		ConcurrentHashMap<String, String> healInfoNS = new ConcurrentHashMap<String, String>();
		WebElement hldEle = null;
		String noShEleExcHldElmntHldLctVle = null;
		healInfoNS.putAll(noSuchExcMap);
		healInfoNS.put("iframe", "true");
		hldEle = multipleWindowCheck(locator, healInfoNS);
		if (hldEle != null)
			return hldEle;
		ntwrkDtMap = networkCheck(locator, healInfoNS);
		if (ntwrkDtMap.get("NetworkStatus") == "true" || ntwrkDtMap.get("NetworkDown") == "true")
			return null;
		noSuchEleCnt++;

		ConcurrentHashMap<String, Integer> noShHealCnt = new ConcurrentHashMap<String, Integer>();
		noShHealCnt.put("NoSuchElement", noSuchEleCnt);
		try {
			/*
			 * PageEleDetails = getPgAndElmtName(ele); //
			 * System.out.println("Page element details in no such ele check : " + //
			 * PageEleDetails); healInfonoSuchEleExcCheck.putAll(PageEleDetails);
			 */
			ConcurrentHashMap<String, String> attributeMap = new ConcurrentHashMap<>();

			if (healInfoNS.get("PageName") != null && healInfoNS.get("ElementName") != null) {
				attributeMap = jsonObjectParser.jsonMapper(healInfoNS.get("PageName"), healInfoNS.get("ElementName"),
						healInfoNS.get("locatorType"), null);// check
				// System.out.println("Attribute map :" + attributeMap);
			}

			if (attributeMap != null && attributeMap.get("iframe") != null) {
				if (attributeMap.get("iframe").length() > 1) {
					JavascriptExecutor exe = (JavascriptExecutor) lthdriver.get();
					Object isInIframe = exe.executeScript("return !(window.parent == window.self);");
					// System.out.println("element is in frame "+isInIframe.toString());
					if (isInIframe.toString() == "false") {
						iframeCheck = false;
						healInfoNS.put("ExceptionType", "NoSuchElementException-iframeswitch");
						healInfoNS.put("iframe", "false");
						jsonVlsMap.put("iframe", attributeMap.get("iframe"));
					}
				}
			}

			if (attributeMap != null && iframeCheck == true) {
				attributeMap.put("LocatorType", healInfoNS.get("locatorType"));
				attributeMap.put("PageName", healInfoNS.get("PageName"));
				attributeMap.put("ElementName", healInfoNS.get("ElementName"));
				Iterator<Entry<String, String>> itr = attributeMap.entrySet().iterator();
				while (itr.hasNext()) {
					Entry<String, String> key = itr.next();
					if (key.toString().contains("sel_attr")) {
						String attrTyp = key.getKey().split(":")[1];
						String attrVle = attributeMap.get(key.getKey());
						hldEle = verifyElement(attrTyp + ":" + attrVle);
						if (hldEle != null)
							selAttrFlag = true;
						healInfoNS.put("HealdEleTyp", attrTyp);
						healInfoNS.put("HealedLocatorVle", attrVle);
						updateCollection(healInfoNS, hldEle);

					}

				}

				if (!selAttrFlag) {
					vrfyValue = verifyElement(attributeMap, lthdriver.get());
					if (vrfyValue.eleType != null && vrfyValue.eleValue != null && vrfyValue.eleLocVle != null) {
						jsonVlsMap = vrfyValue.locatorsMap;
						hldEle = vrfyValue.eleLocVle;
						noShEleExcHldElmntHldLctVle = vrfyValue.eleValue;
						healInfoNS.put("HealdEleTyp", vrfyValue.eleType);
						healInfoNS.put("HealedLocatorVle", noShEleExcHldElmntHldLctVle);
						updateCollection(healInfoNS, hldEle);
					} else {
						String bSoupXpath = bSoupCall(attributeMap);
						if (bSoupXpath != null) {
							// System.out.println("Output from bSoup: " + bSoupXpath);
							hldEle = verifyElement("xpath" + ":" + bSoupXpath);
							if (hldEle != null) {
								healInfoNS.put("HealdEleTyp", "xpath");
								healInfoNS.put("HealedLocatorVle", bSoupXpath);
								updateCollection(healInfoNS, hldEle);
								healInfoNS.put("key", "xpath");
								healInfoNS.put("value", bSoupXpath);
								jsonObjectParser.jsonMapper(attributeMap.get("PageName"),
										attributeMap.get("ElementName"), "xpath", healInfoNS);
							}
						} else {
							hldEle = null;
							noShEleExcHldElmntHldLctVle = null;
						}
					}
				}
			}

			getReportValues(hldEle, healInfoNS, getHealCountInfoCheck(noShHealCnt), jsonVlsMap);
		} catch (Exception e) {

		}
		return hldEle;
	}

	public void updateCollection(ConcurrentHashMap<String, String> updMap, WebElement vle) {
		ElementDetails eleNoSuchChkDtls = healEleData.get(updMap.get("PageName") + "_" + updMap.get("ElementName"));
		eleNoSuchChkDtls.setHealedLoc(vle);
		eleNoSuchChkDtls.setHealedEleTyp(updMap.get("HealdEleTyp"));
		eleNoSuchChkDtls.setHealedEleVle(updMap.get("HealedLocatorVle"));
		healEleData.put(updMap.get("PageName") + "_" + updMap.get("ElementName"), eleNoSuchChkDtls);
	}

	public static String checkPythonVersion() {
		String line = null;
		try {
			Process process = Runtime.getRuntime().exec(new String[] { "cmd", "/C", "python --version" });
			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

			while ((line = reader.readLine()) != null) {
				// System.out.println("Python version :" + line);
			}
		} catch (Exception e) {
			// System.out.println("Exception : " + e);
		}
		return line;
	}

	public String bSoupCall(ConcurrentHashMap<String, String> bSoupData) {
		// System.out.println("Inside bSoup method: " + bSoupData);
		Boolean fileExists = false;
		Boolean pythonVersionCheck = false;
		String xpath = null;
		File myhtmlFile = new File(cdir + "/target/htmldata.txt");
		File pyFile = new File(cdir + "/target/final_xpath.py");
		try {// Check whether python is installed in machine before executing pythin script
			if (checkPythonVersion().contains("Python")) {
				pythonVersionCheck = true;
			}
			File targDir = new File(cdir + "/target/");
			boolean tarDirExists = targDir.exists();
			if (!tarDirExists) {
				File theDir = new File(cdir + "/target/");
				try {
					theDir.mkdir();
					tarDirExists = targDir.exists();
				} catch (SecurityException se) {
					LOGGER.error("Issues while creating directory :" + se);
				}
			}
			try {

				myhtmlFile.createNewFile();
				pyFile.createNewFile();
				fileExists = myhtmlFile.exists() && pyFile.exists();
			} catch (Exception e) {
				LOGGER.error("Error while creating files :" + e);
			} // Check whether target directory exists if not will create it, also created
				// temp files for python script and pagesource.
			if (tarDirExists && fileExists && pythonVersionCheck) {

				String tempFileLoc = cdir + "/target/htmldata.txt";
				String htmlContent = lthdriver.get().getPageSource();
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
				String pyFileloc = cdir + "/target/final_xpath.py";
				String pyContent = "wwCHbnW1tnSJ2ivbaV0Dvw==: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";
				FileWriter filePy = new FileWriter(pyFile);
				filePy.write(pyContent);
				filePy.close();

				String command = "python \"" + pyFile + "\" \"" + tempFileLoc + "\" \"" + objtag + "\" \"" + pagename
						+ "\" \"" + fullxpath + "\" \"" + parent + "\" \"" + ancestor + "\" \"" + child;
				jsonObjectParser.decryptFile(pyFileloc);
				process = Runtime.getRuntime().exec(command);
				mProcess = process;
				InputStream stdout = mProcess.getInputStream();
				// System.out.println("output from bsoup: " + stdout.toString());
				BufferedReader reader = new BufferedReader(new InputStreamReader(stdout, StandardCharsets.UTF_8));
				jsonObjectParser.encryptFile(pyFileloc);
				String line;
				while ((line = reader.readLine()) != null) {
					System.out.println("stdout: " + line);
					if (line.contains("Object Locator:")) {
						String[] strSplit = line.split("Object Locator:");
						xpath = strSplit[1];
					}
				}
				try {// deletes temp file for python script and html file
					myhtmlFile.delete();
					pyFile.delete();
				} catch (Exception e) {
					LOGGER.error("Error while deleting files :" + e);
				}
			}

		} catch (Exception e) {
			System.out.println("Exception occured in bSoup call" + e);
		}

		return xpath;

	}

	public ConcurrentHashMap<String, String> networkCheck(WebElement ele,
			ConcurrentHashMap<String, String> healCmmnDataMap) {// return map with network
		// status(avoid going to nosuchelement exception
		int networkErrorCount = 0;
		ConcurrentHashMap<String, Integer> NtwrkHealCountInfo = new ConcurrentHashMap<String, Integer>();
		ConcurrentHashMap<String, String> healInfoNtwrkCheck = new ConcurrentHashMap<String, String>();
		healInfoNtwrkCheck.putAll(healCmmnDataMap);
		WebElement NetwrkHealedlocator = null;
		boolean ntwrkStatus = false;
		boolean ntwrkDown = false;
		try {

			networkMsgs = jsonObjectParser.networkJsonParser();
			for (String msg : networkMsgs) {
				if (lthdriver.get().getPageSource().contains(msg)) {
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
					lthdriver.get().close();
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

	private WebElement multipleWindowCheck(WebElement windwCheck, ConcurrentHashMap<String, String> healCmmnDataMap) {
		WebElement windwCheckHldLctr = null;
		int otrCount = 0;
		ConcurrentHashMap<String, Integer> WndwHealCountInfo = new ConcurrentHashMap<String, Integer>();
		ConcurrentHashMap<String, String> healnfoWindwCheck = new ConcurrentHashMap<String, String>();
		healnfoWindwCheck.putAll(healCmmnDataMap);
		Set<String> handles = lthdriver.get().getWindowHandles();
		int handlescount = lthdriver.get().getWindowHandles().size();
		if (handlescount > 1) {
			try {
				String curWndHndle = getlthWebdriver().getWindowHandle();
				for (String handle : handles) {
					String switchedWindowTitle = lthdriver.get().switchTo().window(handle).getTitle();
					for (String windowTitle : HealingConfig.dsmsWndwTtls) {
						if (switchedWindowTitle.trim().equalsIgnoreCase(windowTitle.trim())) {
							getlthWebdriver().close();
							getlthWebdriver().switchTo().window(curWndHndle);
							wndwSts = true;
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

				}
				if (wndwSts) {
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

	private void getReportValues(WebElement Healedlocator, ConcurrentHashMap<String, String> healDataMap,
			ConcurrentHashMap<String, Integer> HealCountInfo, ConcurrentHashMap<String, String> jsonDataMap) {
		ElementValue vrfyEleValue = new ElementValue();
		ConcurrentHashMap<String, String> healReportInfo = new ConcurrentHashMap<>();
		healReportInfo.putAll(healDataMap);
		String excptnMsg = healDataMap.get("ExcptnMsg");

		ElementDetails eleRptDtls = healEleData
				.get(healReportInfo.get("PageName") + "_" + healReportInfo.get("ElementName"));
		eleRptDtls.setExceptionType(healDataMap.get("ExceptionType"));
		eleRptDtls.setExptnMsg(healDataMap.get("ExcptnMsg"));

		if (healDataMap.get("HealedLocatorVle") != null) {

			String timestampImage = captureScreenshot(getlthWebdriver(), Healedlocator);
			healReportInfo.put("ImageFileName", timestampImage);
			healReportInfo.put("HealStatus", "success"); // add to write file;
			healReportInfo.put("HealedLocator", healDataMap.get("HealedLocatorVle"));
			// healdElementData.put(, elementDtls);
			healEleData.get(healReportInfo.get("PageName") + "_" + healReportInfo.get("ElementName"))
					.setStatus("Success");
			eleRptDtls.setStatus("Success");
			eleRptDtls.setLocDataMap(jsonDataMap);
			eleRptDtls.setScrnShtPath(timestampImage);
			if (excptnMsg.contains("Timeout") || excptnMsg.contains("Stale")) {
				JsonHealingInfoWriter.AddStep(healReportInfo, HealCountInfo, jsonDataMap, Technology);

			} else {
				JsonHealingInfoWriter.AddStep(healReportInfo, HealCountInfo, jsonDataMap, Technology);

			}

		} else {
			healReportInfo.put("HealStatus", "Failure");
			eleRptDtls.setStatus("Failure");
			healReportInfo.put("HealedLocator", "NA");
			healEleData.get(healReportInfo.get("PageName") + "_" + healReportInfo.get("ElementName"))
					.setStatus("Failure");
			JsonHealingInfoWriter.AddStep(healReportInfo, HealCountInfo, jsonDataMap, Technology);
			if (excptnMsg.contains("Timeout") || excptnMsg.contains("Stale")) {

			} else {

			}

		}
		healEleData.put(healReportInfo.get("PageName") + "_" + healReportInfo.get("ElementName"), eleRptDtls);
		// System.out.println("Collection Data:" + healdElementData);
	}

	private String captureScreenScrnshot(WebDriver webDriver) {
		StringBuffer scrFileNme = new StringBuffer();
		try {
			DateFormat dateFormat = new SimpleDateFormat("dd_MM_yyyy_hh_mm_ss_ms_aa");
			String dateString = dateFormat.format(new Date()).toString();
			if (testNme == null) {
				scrFileNme.append(testCseNme);
			} else {
				scrFileNme.append(testNme);
			}
			TakesScreenshot scrShot = ((TakesScreenshot) webDriver);
			File SrcFile = scrShot.getScreenshotAs(OutputType.FILE);
			scrFileNme.append("_" + dateString);
			FileUtils.copyFile(SrcFile, new File(HealingConfig.scrshtPath + "\\" + scrFileNme + ".png"), true);
			// BufferedImage image = new Robot().createScreenCapture(new
			// Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
			// ImageIO.write(image, "png", new File(scrnshotPath + "\\"
			// +scrFileNme+".png"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return scrFileNme.toString();
	}

	private String captureScreenshot(WebDriver driver, WebElement eleScreenCapture) {
		StringBuffer filename = new StringBuffer();
		try {
			DateFormat dateFormat = new SimpleDateFormat("dd_MM_yyyy_hh_mm_ss_ms_aa");
			String dateString = dateFormat.format(new Date()).toString();
			if (testNme.get() == null) {
				filename.append(testCseNme);
			} else {
				filename.append(testNme.get());
			}
			JavascriptExecutor jse = (JavascriptExecutor) driver;
			jse.executeScript("arguments[0].scrollIntoView(true);", eleScreenCapture);
			jse.executeScript("arguments[0].style.border='2px solid red'", eleScreenCapture);
			jse.executeScript("arguments[0].style.outline='2px solid red'", eleScreenCapture);
			Thread.sleep(1000);
			File screenShot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
			filename.append("_" + dateString);
			//System.out.println("screenshot path inside capture screenshot : " +HealingConfig.scrshtPath);
			//System.out.println("Screenshot file name :"+filename);
			FileUtils.copyFile(screenShot, new File(HealingConfig.scrshtPath + "/" + filename + ".png"), true);
			//System.out.println("Screenshot copied");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return filename.toString();
	}

	private String getTestCaseName(Exception ExcptnMsg) {
		String testCaseName = null;
		if (testNme.get() == null) {

			StackTraceElement[] excptnStackTraceElements = Thread.currentThread().getStackTrace();

			int stackClassLength = excptnStackTraceElements.length;
			String parentClass = excptnStackTraceElements[stackClassLength - 1].toString();
			String[] parenteleArray = parentClass.split("[.]");
			int parentlength = parenteleArray.length;
			testCaseName = parenteleArray[parentlength - 3];

		} else {
			testCaseName = testNme.get();
			// System.out.println("Testcase name in get/testcasename :" +
			// tstCseNmeTstNG.get());
		}
		// System.out.println("Testcase name :" + testCaseName);
		return testCaseName;
	}

	public String gettestStepAction(Exception ExcptnMsg) {
		String stepActionName = null;
		StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
		for (StackTraceElement strClasses : stackTraceElements) {// System.out.println("stack trace classes to
																	// string"+strClasses.toString());
			if (strClasses.toString().contains("WebHandlers")) {
				String stepActionClass = strClasses.toString();
				String[] stepActions = stepActionClass.split("[.]");
				int stepActionLength = stepActions.length;
				String stepActionNames = stepActions[stepActionLength - 2];
				// System.out.println("Step Action name :" + stepActionNames);
				String[] stepActionNme = stepActionNames.toString().split("[(]");
				stepActionName = stepActionNme[stepActionNme.length - 2];

			}

		}
		// System.out.println("TestStepActionName :" + stepActionName);
		return stepActionName;
	}

	public ElementValue verifyElement(ConcurrentHashMap<String, String> attributeMap, WebDriver ldriver) {
		ConcurrentHashMap<Integer, ElementValue> vrfyEleNoShEleMap = new ConcurrentHashMap<Integer, ElementValue>();
		ConcurrentHashMap<String, String> vrfyEleNoShJsonEleMap = new ConcurrentHashMap<String, String>();
		String locatorType = attributeMap.get("LocatorType");
		ElementValue vrfyEleVle = new ElementValue();
		ConcurrentHashMap<String, String> jsonMap = new ConcurrentHashMap<String, String>();
		try {
			Set<String> keys = attributeMap.keySet();
			keys.parallelStream().forEach((key) -> {
				try {
					if (!key.equals(locatorType)) {

						switch (key) {
						case "id":
							locator_id = ldriver.findElement(By.id(attributeMap.get("id")));
							ElementValue elementValue1 = new ElementValue("id", attributeMap.get("id"), locator_id);
							vrfyEleNoShEleMap.put(1, elementValue1);
							jsonMap.put("id", attributeMap.get("id"));
							vrfyEle = true;

							break;

						case "name":
							// System.out.println("\n" + "Driver: " + ldriver);
							// System.out.println("checking using name : " + attributeMap.get("name"));
							locator_name = ldriver.findElement(By.name(attributeMap.get("name")));
							// System.out.println("verified by name : " + attributeMap.get("name"));
							ElementValue elementValue2 = new ElementValue("name", attributeMap.get("name"),
									locator_name);
							vrfyEleNoShEleMap.put(2, elementValue2);
							jsonMap.put("name", attributeMap.get("name"));
							vrfyEle = true;
							break;

						case "xpath_Follows":
							if (!(locatorType.equals("id") && attributeMap.get("xpath_Follows").contains("id"))) {
								// System.out.println("\n" + "Driver: " + ldriver);
								// System.out.println("checking using xpath_Follows : " +
								// attributeMap.get("xpath_Follows"));
								locator_xpath_Follows = ldriver
										.findElement(By.xpath(attributeMap.get("xpath_Follows")));
								ElementValue elementValue3 = new ElementValue("xpath",
										attributeMap.get("xpath_Follows"), locator_xpath_Follows);
								vrfyEleNoShEleMap.put(3, elementValue3);
								jsonMap.put("xpath_Follows", attributeMap.get("xpath_Follows"));
								vrfyEle = true;
							}
							break;

						case "xpath_default":
							if (!(locatorType.equals("id") && attributeMap.get("xpath_default").contains("id"))) {
								// System.out.println("\n" + "Driver: " + ldriver);
								// System.out.println("checking using xpath_default: " +
								// attributeMap.get("xpath_default"));
								locator_xpath_default = ldriver
										.findElement(By.xpath(attributeMap.get("xpath_default")));
								ElementValue elementValue4 = new ElementValue("xpath",
										attributeMap.get("xpath_default"), locator_xpath_default);
								vrfyEleNoShEleMap.put(4, elementValue4);
								jsonMap.put("xpath_default", attributeMap.get("xpath_default"));
								vrfyEle = true;
							}
							break;

						case "xpath_Parent":
							if (!(locatorType.equals("id") && attributeMap.get("xpath_Parent").contains("id"))) {
								locator_xpath_Parent = ldriver.findElement(By.xpath(attributeMap.get("xpath_Parent")));
								ElementValue elementValue5 = new ElementValue("xpath", attributeMap.get("xpath_Parent"),
										locator_xpath_Parent);
								vrfyEleNoShEleMap.put(5, elementValue5);
								jsonMap.put("xpath_Parent", attributeMap.get("xpath_Parent"));
								vrfyEle = true;
							}
							break;

						case "xpath_Attribute":
							if (!(locatorType.equals("id") && attributeMap.get("xpath_Attribute").contains("id"))) {
								locator_xpath_Attribute = ldriver
										.findElement(By.xpath(attributeMap.get("xpath_Attribute")));
								ElementValue elementValue6 = new ElementValue("xpath",
										attributeMap.get("xpath_Attribute"), locator_xpath_Attribute);
								vrfyEleNoShEleMap.put(6, elementValue6);
								jsonMap.put("xpath_Attribute", attributeMap.get("xpath_Attribute"));
								vrfyEle = true;
							}
							break;

						case "xpath_Descendant":
							if (!(locatorType.equals("id") && attributeMap.get("xpath_Descendant").contains("id"))) {
								locator_xpath_Descendant = ldriver
										.findElement(By.xpath(attributeMap.get("xpath_Descendant")));
								ElementValue elementValue7 = new ElementValue("xpath",
										attributeMap.get("xpath_Descendant"), locator_xpath_Descendant);
								vrfyEleNoShEleMap.put(7, elementValue7);
								jsonMap.put("xpath_Descendant", attributeMap.get("xpath_Descendant"));
								vrfyEle = true;
							}
							break;

						case "xpath_Position":
							if (!(locatorType.equals("id") && attributeMap.get("xpath_Position").contains("id"))) {
								locator_xpath_Position = ldriver
										.findElement(By.xpath(attributeMap.get("xpath_Position")));
								ElementValue elementValue8 = new ElementValue("xpath",
										attributeMap.get("xpath_Position"), locator_xpath_Position);
								vrfyEleNoShEleMap.put(8, elementValue8);
								jsonMap.put("xpath_Position", attributeMap.get("xpath_Position"));
								vrfyEle = true;
							}
							break;

						case "xpath_fullXPath":
							if (!(locatorType.equals("id") && attributeMap.get("xpath_fullXPath").contains("id"))) {
								locator_xpath_fullXPath = ldriver
										.findElement(By.xpath(attributeMap.get("xpath_fullXPath")));
								ElementValue elementValue9 = new ElementValue("xpath",
										attributeMap.get("xpath_fullXPath"), locator_xpath_fullXPath);
								vrfyEleNoShEleMap.put(9, elementValue9);
								jsonMap.put("xpath_fullXPath", attributeMap.get("xpath_fullXPath"));
								vrfyEle = true;
							}
							break;

						case "xpath_Contains":
							if (!(locatorType.equals("id") && attributeMap.get("xpath_Contains").contains("id"))) {
								locator_xpath_Contains = ldriver
										.findElement(By.xpath(attributeMap.get("xpath_Contains")));
								ElementValue elementValue10 = new ElementValue("xpath",
										attributeMap.get("xpath_Contains"), locator_xpath_Contains);
								vrfyEleNoShEleMap.put(10, elementValue10);
								jsonMap.put("xpath_Contains", attributeMap.get("xpath_Contains"));
								vrfyEle = true;
							}
							break;

						case "xpath_Precedance":
							if (!(locatorType.equals("id") && attributeMap.get("xpath_Precedance").contains("id"))) {
								// System.out.println("\n" + "Driver: " + ldriver);
								// System.out.println("checking using xpath_Precedance : " +
								// attributeMap.get("xpath_Precedance"));
								locator_xpath_Precedance = ldriver
										.findElement(By.xpath(attributeMap.get("xpath_Precedance")));
								ElementValue elementValue11 = new ElementValue("xpath",
										attributeMap.get("xpath_Precedance"), locator_xpath_Precedance);
								vrfyEleNoShEleMap.put(11, elementValue11);
								jsonMap.put("xpath_Precedance", attributeMap.get("xpath_Precedance"));
								vrfyEle = true;
							}
							break;

						case "xpath_Ancestor":
							if (!(locatorType.equals("id") && attributeMap.get("xpath_Ancestor").contains("id"))) {
								// System.out.println("\n" + "Driver: " + ldriver);
								// System.out.println("checking using xpath_Ancestor " +
								// attributeMap.get("xpath_Ancestor"));
								locator_xpath_Ancestor = ldriver
										.findElement(By.xpath(attributeMap.get("xpath_Ancestor")));
								ElementValue elementValue12 = new ElementValue("xpath",
										attributeMap.get("xpath_Ancestor"), locator_xpath_Ancestor);
								vrfyEleNoShEleMap.put(12, elementValue12);
								jsonMap.put("xpath_Ancestor", attributeMap.get("xpath_Ancestor"));
								vrfyEle = true;
							}
							break;

						case "xpath_child":
							if (!(locatorType.equals("id") && attributeMap.get("xpath_child").contains("id"))) {
								// System.out.println("\n" + "Driver: " + ldriver);
								// System.out.println("checking using xpath_child : " +
								// attributeMap.get("xpath_child"));
								locator_xpath_child = ldriver.findElement(By.xpath(attributeMap.get("xpath_child")));
								ElementValue elementValue13 = new ElementValue("xpath", attributeMap.get("xpath_child"),
										locator_xpath_child);
								vrfyEleNoShEleMap.put(13, elementValue13);
								jsonMap.put("xpath_child", attributeMap.get("xpath_child"));
								vrfyEle = true;
							}
							break;

						case "xpath":
							if (!(locatorType.equals("id") && attributeMap.get("xpath").contains("id"))) {
								// System.out.println("\n" + "Driver: " + ldriver);
								// System.out.println("checking using xpath_child : " +
								// attributeMap.get("xpath_child"));
								locator_xpath = ldriver.findElement(By.xpath(attributeMap.get("xpath")));
								ElementValue elementValue14 = new ElementValue("xpath", attributeMap.get("xpath"),
										locator_xpath);
								vrfyEleNoShEleMap.put(14, elementValue14);
								jsonMap.put("xpath", attributeMap.get("xpath"));
								vrfyEle = true;
							}
							break;

						case "css":
							// System.out.println("\n" + "Driver: " + ldriver);
							// System.out.println("checking using css : " + attributeMap.get("css"));
							locator_css = ldriver.findElement(By.cssSelector(attributeMap.get("css")));
							// System.out.println("verified by css" + attributeMap.get("css"));
							ElementValue elementValue15 = new ElementValue("xpath", attributeMap.get("css"),
									locator_css);
							vrfyEleNoShEleMap.put(15, elementValue15);
							jsonMap.put("css", attributeMap.get("css"));
							vrfyEle = true;
							break;
						case "class":
							// System.out.println("\n" + "Driver: " + ldriver);
							// System.out.println("checking using class : " +
							// attributeMap.get("classname"));
							locator_class = ldriver.findElement(By.className(attributeMap.get("classname")));
							// System.out.println("verified by class" + attributeMap.get("classname"));
							ElementValue elementValue16 = new ElementValue("xpath", attributeMap.get("class"),
									locator_class);
							vrfyEleNoShEleMap.put(16, elementValue16);
							jsonMap.put("class", attributeMap.get("class"));
							vrfyEle = true;
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
		if (!vrfyEle) {
			locator = null;
		} else {
			try {
				ElementValue elementSortedValue = new ElementValue();
				Map<Integer, ElementValue> result = vrfyEleNoShEleMap.entrySet().stream()
						.sorted(Map.Entry.comparingByKey()).collect(Collectors.toMap(Map.Entry::getKey,
								Map.Entry::getValue, (oldValue, newValue) -> oldValue, LinkedHashMap::new));
				// System.out.println("result : " + result);

				elementSortedValue = (new ArrayList<ElementValue>(result.values())).get(0);

				vrfyEleNoShJsonEleMap.put("key", elementSortedValue.eleType);
				vrfyEleNoShJsonEleMap.put("value", elementSortedValue.eleValue);

				// System.out.println("Value to be updated in json : " + jsonMap);
				jsonObjectParser.jsonMapper(attributeMap.get("PageName"), attributeMap.get("ElementName"),
						elementSortedValue.eleType, vrfyEleNoShJsonEleMap);
				locator = elementSortedValue.eleLocVle;
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

	/**
	 * 
	 * @param ele
	 */
	private ConcurrentHashMap<String, String> getPgAndElmtName(WebElement ele) {
		ConcurrentHashMap<String, String> PageEleMap = new ConcurrentHashMap<>();
		StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
		String eleFlag = null;
		for (StackTraceElement strClasses : stackTraceElements) {
			String locatorType = null;
			String locatorValue = null;
			if (strClasses.getClassName().contains("Page") || strClasses.getClassName().contains("page")) {
				try {
					// System.out.println("Class names :" + strClasses.getClassName());
					String strClass = strClasses.getClassName();
					Class cls = Class.forName(strClass);
					ClassLoader cLoader = cls.getClassLoader();
					Field[] classElements = cls.getDeclaredFields();
					String eleDetails = ele.toString();
					// System.out.println("Element details inside pagename identify : "+eleDetails);
					eleDetails = eleDetails.substring(1, eleDetails.length() - 1);
					String[] data = null;
					if (eleDetails.contains("DefaultElementLocator"))
						data = eleDetails.split("By.")[1].split(": ");
					else
						data = eleDetails.split(" -> ")[1].split(": ");
					locatorType = data[0];
					locatorValue = data[1];
					PageEleMap.put("locatorValue", locatorValue);
					PageEleMap.put("locatorType", locatorType);
					for (Field classElement : classElements) { // issue
						if (classElement.getType().toString().contains("WebElement")) {
							String elementName = classElement.toString();
							FindBy findByAnnotation = classElement.getAnnotation(FindBy.class);
							PageEleMap.putAll(identifyElement(findByAnnotation, elementName, PageEleMap));
							eleFlag = PageEleMap.get("status");
							if (eleFlag == "true")
								break;
						}

					}
				} catch (Exception e1) {
					LOGGER.error("Error while getting locator type and locator value");
					e1.printStackTrace();

				}
			}
		}
		return PageEleMap;
	}

	private ConcurrentHashMap<String, String> identifyElement(FindBy findByAnnotation, String eleNme,
			ConcurrentHashMap<String, String> PgeEleVleMap) {
		ConcurrentHashMap<String, String> PgeIdentifyEleMap = new ConcurrentHashMap<>();
		// System.out.println("Locator value in identifyElement : "+locatorValue);
		// System.out.println("Locator type in identifyElement : "+locatorType);
		// System.out.println("Element name in identifyElement "+eleNme);
		try {
			switch (PgeEleVleMap.get("locatorType")) {
			case "xpath":
				if (!findByAnnotation.xpath().isEmpty()
						&& findByAnnotation.xpath().trim().equals(PgeEleVleMap.get("locatorValue")))
					PgeIdentifyEleMap = setPgAndElmtMap(eleNme);
				break;
			case "css selector":
				if (!findByAnnotation.css().isEmpty()
						&& findByAnnotation.css().trim().equals(PgeEleVleMap.get("locatorValue")))
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
				if (!findByAnnotation.name().isEmpty()
						&& findByAnnotation.name().trim().equals(PgeEleVleMap.get("locatorValue")))
					PgeIdentifyEleMap = setPgAndElmtMap(eleNme);
				break;
			case "link text":
				if (!findByAnnotation.linkText().isEmpty()
						&& findByAnnotation.linkText().trim().equals(PgeEleVleMap.get("locatorValue")))
					PgeIdentifyEleMap = setPgAndElmtMap(eleNme);
				break;
			case "class name":
				if (!findByAnnotation.className().isEmpty()
						&& findByAnnotation.className().trim().equals(PgeEleVleMap.get("locatorValue")))
					PgeIdentifyEleMap = setPgAndElmtMap(eleNme);
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("Error while checking using findBy Annotation");
			// System.out.println("Page details inside Identify element :" +PgeEleMap);
		}

		return PgeIdentifyEleMap;

	}

	public ConcurrentHashMap<String, String> setPgAndElmtMap(String eleNme) {
		ConcurrentHashMap<String, String> PageElementMap = new ConcurrentHashMap<>();
		// System.out.println("eleNme in setPgAndElmtMap :"+eleNme);
		try {
			String[] eleArray = eleNme.split("[.]");
			int length = eleNme.split("[.]").length;
			PageElementMap.put("PageName", eleArray[length - 2]);
			PageElementMap.put("ElementName", eleArray[length - 1]);
			LOGGER.info("Pagename is : " + PageElementMap.get("PageName"));
			LOGGER.info("Element name is : " + PageElementMap.get("ElementName"));
			PageElementMap.put("status", "true");
		} catch (Exception e) {
			LOGGER.error("Error while setting pagename and elementname");
		}
		return PageElementMap;
	}

}
