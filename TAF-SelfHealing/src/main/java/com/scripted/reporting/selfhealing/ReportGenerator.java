package com.scripted.reporting.selfhealing;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scripted.jsonWriter.HealingInfo;
import com.scripted.jsonWriter.JsonHealingInfoWriter;
import com.scripted.jsonWriter.TestCases;
import com.scripted.jsonWriter.TestStep;
import com.scripted.selfhealing.CnstLookup;
import com.scripted.selfhealing.HealingConfig;

public class ReportGenerator {
	


	private static String cdir = System.getProperty("user.dir");
	HashMap<String, String> pageEleAsscDtls = new HashMap<String, String>();
	List<TestCases> testCaseDtls;
	HealingInfo healingInfo;
	static int successPercent = 0;
	static int successCount = 0;
	static int failureCount = 0;
	static StringBuffer noSuchTrendCount;
	static StringBuffer staleTrendCount;
	static StringBuffer netwrkTrendCount;
	static StringBuffer othrsTrendCount;
	static int modelId = 1;
	static StringBuffer tbodyData = new StringBuffer();
	static StringBuffer tbodyMobData = new StringBuffer();
	static StringBuffer modalData = new StringBuffer();

	static String userInfo;

	
		
	

	public ReportGenerator() {

	}

	public void readResultJson(String jsonRptNme) {
		try {
			byte[] jsonData = Files.readAllBytes(Paths.get(cdir + "/healing-results/" +HealingConfig.jsonRptNme + ".json"));
			ObjectMapper objectMapper = new ObjectMapper();
			healingInfo = objectMapper.readValue(jsonData, HealingInfo.class);
			testCaseDtls = healingInfo.getTestCase();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void getAsscTstCseDtls() {

		try {
			for (TestCases testcase : testCaseDtls) {
				List<TestStep> testStepDtls = testcase.getTestStep();
				for (TestStep teststep : testStepDtls) {
					String pgeEleDtls = teststep.getPageName() + "_" + teststep.getObjectName();
					// Iterator<Entry<String, String>> itr = pageEleAsscDtls.entrySet().iterator();
					if (pageEleAsscDtls.isEmpty()) {
						pageEleAsscDtls.put(pgeEleDtls, testcase.getTestCaseName());
					} else {
						if (pageEleAsscDtls.containsKey(pgeEleDtls)) {
							pageEleAsscDtls.put(pgeEleDtls,
									pageEleAsscDtls.get(pgeEleDtls) + "~" + testcase.getTestCaseName());
						} else {
							pageEleAsscDtls.put(pgeEleDtls, testcase.getTestCaseName());
						}

					}

					if (teststep.getHealStatus().trim().equalsIgnoreCase("success")) {
						successCount++;
					} else {
						failureCount++;
					}

				}

			}
			// System.out.println("Assosciated testcasename Dtls :" + pageEleAsscDtls);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void reportGen() {
		readResultJson(HealingConfig.jsonRptNme);
		getAsscTstCseDtls();
		successPercentCount();
		tableDataGen();
		File file = new File(HealingConfig.rptPath+ "/SelfHealingReport.html");
		String newtext;
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line = "", oldtext = "";
			while ((line = reader.readLine()) != null) {
				oldtext += line + "\r\n";
			}
			reader.close();
			DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh.mm.ss aa");
			String dateString = dateFormat.format(new Date()).toString();
			newtext = oldtext.replace("{ExecutedBy}", HealingConfig.usrInfo);
			newtext = newtext.replace("{ExecutedOn}", dateString);
			// Count and percentage for graph
			newtext = newtext.replace("{NoSuchElement}", String.valueOf(healingInfo.getNoSuchEleCount()));
			newtext = newtext.replace("{stale}", String.valueOf(healingInfo.getStleTmeOutCount()));
			newtext = newtext.replace("{Network}", String.valueOf(healingInfo.getNetwrkErrCount()));
			newtext = newtext.replace("{Others}", String.valueOf(healingInfo.getOthersCount()));
			newtext = newtext.replace("{percentage}", String.valueOf(successPercent) + "%");
			newtext = newtext.replace("{Success}", String.valueOf(successCount));
			newtext = newtext.replace("{failed}", String.valueOf(failureCount));
			// Values for trendbuffer
			newtext = newtext.replace("{countOfExecution}", String.valueOf(5));
			newtext = newtext.replace("{gNoSuchElement}", noSuchTrendCount);
			newtext = newtext.replace("{gStaleReference}", staleTrendCount);
			newtext = newtext.replace("{gConnectivity}", netwrkTrendCount);
			newtext = newtext.replace("{gOthers}", othrsTrendCount);
			if (tbodyData.length() > 0)
				newtext = newtext.replace("{Webtbody}", tbodyData);
			if (tbodyMobData.length() > 0)
				newtext = newtext.replace("{Mobtbody}", tbodyMobData);
			newtext = newtext.replace("{model}", modalData);

			FileWriter writer = new FileWriter(HealingConfig.rptPath + "/SelfHealingReport.html");
			writer.write(newtext);
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void successPercentCount() {
		successPercent = (int) (((float) successCount / ((float) successCount + (float) failureCount)) * 100);
		// System.out.println("Success percentage :" + successPercent);
	}

	public void tableDataGen() {
		try {

			HashMap<String, String> pageEleMap = new HashMap<String, String>();
			for (TestCases testcase : testCaseDtls) {
				String testCaseNme = testcase.getTestCaseName();
				List<TestStep> testStepDtls = testcase.getTestStep();
				for (TestStep teststep : testStepDtls) {
					String pgeEleDtls = teststep.getPageName() + "_" + teststep.getObjectName()+"_"+teststep.getTestStepFailureReason();
					// Iterator<Entry<String, String>> itr = pageEleMap.entrySet().iterator();
					if (pageEleMap.isEmpty()) {
						pageEleMap.put(pgeEleDtls, testcase.getTestCaseName());
						if (teststep.getTechnology().equalsIgnoreCase("web"))
							tbodygenWeb(teststep, testCaseNme);
						else
							tbodygenMob(teststep, testCaseNme);

					} else {
						if (pageEleMap.containsKey(pgeEleDtls)) {
							pageEleMap.put(pgeEleDtls, pageEleMap.get(pgeEleDtls) + "~" + testcase.getTestCaseName());
						} else {
							pageEleMap.put(pgeEleDtls, testcase.getTestCaseName());
							if (teststep.getTechnology().equalsIgnoreCase("web"))
								tbodygenWeb(teststep, testCaseNme);
							else
								tbodygenMob(teststep, testCaseNme);
						}
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void tbodygenWeb(TestStep teststep, String tstCseNme) {

		tbodyData.append("<div class='list' data='healDetail'>");
		tbodyData.append("<div>");
		if (teststep.getHealStatus().equalsIgnoreCase("success")) {
			tbodyData.append("<img src = 'img/bandage.png' class='logo-table1' alt='Attempted to Heal' />");

		} else {

			tbodyData.append("<img src = 'img/Failed.png' class='logo-table1' alt='Attempted to Heal' />");
		}
		tbodyData.append("</div>");
		tbodyData.append("<div class=\"text_sec\" style=\"flex:auto;\">");
		tbodyData.append("<div class=\"list_heading1\"><span>Page Name : </span><span>");
		tbodyData.append(teststep.getPageName());
		tbodyData.append("</span></div>\r\n" + "<div class=\"list_heading2\"><span>Object Name : </span><span>");
		tbodyData.append(teststep.getObjectName());
		tbodyData.append("</span>&nbsp;&nbsp;&nbsp;<span>Exception : </span><span>");
		tbodyData.append(teststep.getTestStepFailureReason());
		tbodyData.append("</span></div>\r\n" + "</div>\r\n" + "<div>\r\n");
		if (teststep.getHealStatus().equalsIgnoreCase("success")) {
			modaldataGen(String.valueOf(modelId), teststep, tstCseNme);
			tbodyData.append("<img src = 'img/info.png' class='logo-table2' data-toggle='modal' data-target='#Model"
					+ modelId + "' alt='info' />\r\n" + "</div>\r\n" + "</div>");
			modelId++;
		} else {
			modaldataGen(String.valueOf(modelId), teststep, tstCseNme);
			tbodyData.append("<img src = 'img/info.png' class='logo-table2' data-toggle='modal' data-target='#Model"
					+ modelId + "'' alt='info' />\r\n" + "</div>\r\n" + "</div>");

			modelId++;
		}

	}

	public void tbodygenMob(TestStep teststep, String tstCseNme) {
		tbodyMobData.append("<div class='list' data='healDetail'>");
		tbodyMobData.append("<div>");
		if (teststep.getHealStatus().equalsIgnoreCase("success")) {
			tbodyMobData.append("<img src = 'img/bandage.png' class='logo-table1' alt='Attempted to Heal' />");

		} else {

			tbodyMobData.append("<img src = 'img/Failed.png' class='logo-table1' alt='Attempted to Heal' />");
		}
		tbodyMobData.append("</div>");
		tbodyMobData.append("<div class=\"text_sec\" style=\"flex:auto;\">");
		tbodyMobData.append("<div class=\"list_heading1\"><span>Page Name : </span><span>");
		tbodyMobData.append(teststep.getPageName());
		tbodyMobData.append("</span></div>\r\n" + "<div class=\"list_heading2\"><span>Object Name : </span><span>");
		tbodyMobData.append(teststep.getObjectName());
		tbodyMobData.append("</span>&nbsp;&nbsp;&nbsp;<span>Exception : </span><span>");
		tbodyMobData.append(teststep.getTestStepFailureReason());
		tbodyMobData.append("</span></div>\r\n" + "</div>\r\n" + "<div>\r\n");
		if (teststep.getHealStatus().equalsIgnoreCase("success")) {
			modaldataGen(String.valueOf(modelId), teststep, tstCseNme);
			tbodyMobData.append("<img src = 'img/info.png' class='logo-table2' data-toggle='modal' data-target='#Model"
					+ modelId + "' alt='info' />\r\n" + "</div>\r\n" + "</div>");
			modelId++;
		} else {
			modaldataGen(String.valueOf(modelId), teststep, tstCseNme);
			tbodyMobData.append("<img src = 'img/info.png' class='logo-table2' data-toggle='modal' data-target='#Model"
					+ modelId + "'' alt='info' />\r\n" + "</div>\r\n" + "</div>");

			modelId++;
		}
	}

	public void modaldataGen(String modelID, TestStep teststep, String tstCseNme) {
		modalData.append("<div class='modal fade' id='Model" + modelID + "' role='dialog'>\r\n"
				+ "<div class='modal-dialog' style='width:80%;'>\r\n"
				+ "<div class='modal-content'>\r\n"
				+ "<div class='modal-header'>\r\n"
				+ "<button type='button' class='close' data-dismiss='modal'>&times;</button>\r\n"
				+ "<h5 class='modal-title'>" + teststep.getPageName() + " : "
				+ teststep.getObjectName() + "</h5>\r\n" + "</div>\r\n"
				+ "<div class='modal-body'>\r\n"
				+ "<div class='panel-group' id='Accordin" + modelID + "'>");

		AddRecommendations(modelID, teststep, tstCseNme);
		// AddFailureImage(modelId, extractedReportFolder, rptItem, ref model);
		AddAssociatedTestCases(modelID, teststep, tstCseNme);

		modalData.append("</div>\r\n" + "</div>\r\n" + "</div>\r\n" + "</div>\r\n" + "</div>");

	}

	public void AddAssociatedTestCases(String modelID, TestStep teststep, String tstCseNme) {
		// Boolean asscTstCseExsts = false;
		HashSet<String> asscTstCseDtlsFnl = new HashSet<String>();

		if (pageEleAsscDtls != null) {
			if (pageEleAsscDtls.containsKey(teststep.getPageName() + "_" + teststep.getObjectName())) {
				String[] asscTstCseDtls = pageEleAsscDtls.get(teststep.getPageName() + "_" + teststep.getObjectName())
						.split("~");

				if (asscTstCseDtls != null) {
					for (String asscTstCseDtl : asscTstCseDtls) {
						// if (!asscTstCseDtl.equals(tstCseNme)) {
						// asscTstCseExsts = true;
						asscTstCseDtlsFnl.add(asscTstCseDtl);

						// }
					}
					// if (asscTstCseExsts) {

					modalData.append("<div class='panel panel-default'>\r\n" + "<div class='panel-heading'>\r\n"
							+ "<h4 class='panel-title' style='color:#2a3f54;'>\r\n"
							+ "<a class='accordion-toggle' data-toggle='collapse' data-parent='#Accordion" + modelID
							+ "3' href='#Collapse" + modelID + "3'>\r\n" + "Associated Test Cases\r\n" + "</a>\r\n"
							+ "</h4>\r\n" + "</div>\r\n" + "<div id='Collapse" + modelID
							+ "3' class='panel-collapse collapse '>\r\n" + "<div class='panel-body'>");
					for (String asscTstCseDtlFnl : asscTstCseDtlsFnl) {
						modalData.append("<div class='tstcasewrap'>" + asscTstCseDtlFnl + "</div>");
					}

					modalData.append("</div>\r\n" + "</div>\r\n" + "</div>");
					// }
				}
			}
		}

	}

	public String modalHeaderGen(TestStep teststep) {
		String modalHeader;

		if (teststep.getHealStatus().equalsIgnoreCase("success")) {
			if (((teststep.getTestStepFailureReason().equalsIgnoreCase("NoSuchElementException")
					|| teststep.getTestStepFailureReason().equalsIgnoreCase("InvalidSelectorException")))) {
				modalHeader = "Recommendations";
			} else {
				modalHeader = "Information";
			}
		} else {
			modalHeader = "Error Details";
		}
		return modalHeader;
	}

	public String modalMessageGen(TestStep teststep) {
		String modalMessage = "";
		try {
			switch (teststep.getTestStepFailureReason()) {
			case "StaleElementReferenceException":
				modalMessage = "Successfully healed stale issue";
				break;
			case "TimeoutException":
				modalMessage = "Successfully healed timeout issue";
				break;
			case "UnhandledAlertException":
				modalMessage = "Successfully handled alert containing text  " + teststep.getFailedElement();
				break;
			case "NoAlertPresentException":
				modalMessage = "";
				break;

			}
		} catch (Exception e) {

		}

		return modalMessage;
	}

	// "{rptItem.HealObject.TestCase}\r\n"
	public void AddRecommendations(String modelID, TestStep teststep, String tstCseNme) {
		String modalHeader = modalHeaderGen(teststep);
		modalData.append("<div class='panel panel-default'>\r\n" + "<div class='panel-heading'>\r\n"
				+ "<h4 class='panel-title' style='color:#2a3f54;'>\r\n"
				+ "<a class='accordion-toggle' data-toggle='collapse' data-parent='#Accordian" + modelID
				+ "1' href='#Collapse" + modelID + "1'>\r\n" + modalHeader + "</a>\r\n" + "</h4>\r\n" + "</div>\r\n"
				+ "<div id='Collapse" + modelID + "1' class='panel-collapse collapse'>\r\n"
				+ " <div class='panel-body'>\r\n" + "<div class='row'>\r\n");

		if (!teststep.getHealStatus().equalsIgnoreCase("Failure")) {
			modalData.append("<div class='col-sm-12 col-md-7'>\r\n");
		} else
			modalData.append("<div class='col-sm-12'>\r\n");
		if (teststep.getTestStepFailureReason().equalsIgnoreCase(CnstLookup.NOSUCHELE)) {
			modalData.append("<table style='width:100%; table-layout:fixed; word-break:break-word;'>\r\n"
					+ "<tr>\r\n"
					+ "<th style='width:5%;'>&nbsp;</th>\r\n"
					+ "<th>&nbsp;</th>  \r\n"
					+ "<th style='text-align: center;width:15%;'>&nbsp;</th>\r\n"
					+ "</tr>\r\n"
					+ "<tr>\r\n"
					+ "<td><img src='img/failed.png' class='logo-tablepopup' alt='failed' /></td>\r\n"
					+ "<td>Unable to find an element using " + teststep.getFailedElement() + "</td>\r\n"
					+ "</tr>");
		} else if (teststep.getHealStatus().equalsIgnoreCase("Failure")) {
			String failMessage = "";
			String iframeID="";
			if (!teststep.getTestStepFailureReason().equalsIgnoreCase("NoSuchElementException-iframeswitch")) {
				failMessage = "<td>Unable to perform " + teststep.getAction() + " action on an element "
						+ teststep.getFailedElement() + " " + "due to " + teststep.getTestStepFailureReason()
						+ "</td>\r\n" + "</tr>";
			} else {
				if(teststep.getLocatorEleMap().containsKey("iframe"))
					iframeID=teststep.getLocatorEleMap().get("iframe");
				failMessage = "<td>Element cannot be healed . Please switch to frame : " + iframeID
						+ "</td>\r\n" + "</tr>";
			}
			modalData.append("<table style='width:100%; table-layout:fixed; word-break:break-word;'>\r\n"
					+ "<tr>\r\n"
					+ "<th style='width:5%;'>&nbsp;</th>\r\n"
					+ "<th>&nbsp;</th>  \r\n"
					+ "<th style='text-align: center;width:15%;'>&nbsp;</th>\r\n"
					+ "</tr>\r\n"
					+ "<tr>\r\n"
					+ "<td><img src='img/infored.png' class='logo-tablepopup' alt='failed' /></td>\r\n"
					+ failMessage);

		}
		if (teststep.getExcptnMsg() != null && teststep.getHealStatus().equalsIgnoreCase("Failure")) {
			modalData.append("<tr>\r\n" + "<td>&nbsp;</td>\r\n" + "<td style='padding-top: 10px;' colspan='2'>ERROR\r\n"
					+ "	STACKTRACE</td>\r\n" + "</tr>");
			modalData.append("<tr>\r\n" + " <td></td>\r\n" + "<td>" + teststep.getExcptnMsg() + "</td> \r\n" + "</tr>");
			// <img src='img/info1.png' class='logo-tablepopup' alt='failed' />

		}

		if (!teststep.getHealStatus().equalsIgnoreCase("Failure")
				&& !teststep.getTestStepFailureReason().equalsIgnoreCase(CnstLookup.NOSUCHELE)) {
			String modalMessage = modalMessageGen(teststep);
			modalData.append("<table style='width:100%; table-layout:fixed; word-break:break-word;'>\r\n"
					+ "<tr>\r\n"
					+ "<th style='width:5%;'>&nbsp;</th>\r\n"
					+ "<th>&nbsp;</th>  \r\n"
					+ "<th style='text-align: center;width:15%;'>&nbsp;</th>\r\n"
					+ "</tr>\r\n"
					+ "<tr>\r\n"
					+ "<td><img src='img/info1.png' class='logo-tablepopup' alt='failed' /></td>\r\n"
					+ "<td>" + modalMessage + "</td>\r\n"
					+ "</tr>");

		}

		if (teststep.getLocatorEleMap() != null && !teststep.getHealStatus().equalsIgnoreCase("Failure")
				&& teststep.getTestStepFailureReason().equalsIgnoreCase(CnstLookup.NOSUCHELE)) {
			modalData.append("<tr>\r\n"
					+ "<td><img src='img/bandage.png' class='logo-tablepopup' alt='failed' /></td>\r\n"
					+ "<td>Successfully healed element by using Find By "
					+ teststep.getHealedElement() + "</td>\r\n" + "<td>&nbsp;</td> \r\n"
					+ "</tr>");

			modalData.append(" <tr>\r\n"
					+ "<td><img src='img/info1.png' class='logo-tablepopup' alt='failed' /></td>\r\n"
					+ "<td>We recommend updating the test to find the element using one of the following locators</td>\r\n"
					+ "<td>&nbsp;</td> \r\n" + "</tr>");

			modalData.append("<tr>\r\n" + "<td>&nbsp;</td>\r\n" + "<td><b>Alternate Recommendations</b></td>\r\n"
					+ "<td><b>Trust Factor</b></td>\r\n" + "</tr>");
			getTrustFactor(teststep);
			modalData.append("</table>");
		} else {
			modalData.append("</table>");
		}
		modalData.append("</div>");

		if (teststep.getScrnShotPath() != null && !teststep.getHealStatus().equalsIgnoreCase("Failure")) {
			modalData.append("<div class='col-sm-12 col-sm-5' style='padding-top:20px;'>\r\n" + "<a href='screenshots/"
					+ teststep.getScrnShotPath() + ".png" + "' target='_blank'><img src = 'screenshots/"
					+ teststep.getScrnShotPath() + ".png" + "' class='imgpopupsz' alt='screenshot' /></a>\r\n"
					+ "</div>");
		} else {
			modalData.append("<div class='col-sm-12 col-md-5'>\r\n" + "&nbsp;\r\n" + "</div>");
		}

		modalData.append("</div></div></div></div>");

	}

	public void getTrustFactor(TestStep teststep) {
		String hlEleTyp = teststep.getHealedElement().split(":")[0];
		String loctyp;
		Boolean thumpVle = true;
		int countIncr = 0;
		String[] locVles = { "id:100", "name:98", "xpath_default:97", "xpath_Parent:96", "xpath_Ancestor:95",
				"xpath_child:94", "xpath_fullXPath:93", "css:91", "xpath_Contains:88", "xpath_Follows:86",
				"xpath_Precedance:85", "xpath_Descendant:84", "xpath_Position:83", "xpath_Attribute:80" };

		for (int i = 0; i < locVles.length; i++) {

			if (!locVles[i].split(":")[0].equalsIgnoreCase(hlEleTyp.trim())) {
				if (locVles[i].contains("xpath"))
					loctyp = "xpath";
				else
					loctyp = locVles[i].split(":")[0];

				if (teststep.getLocatorEleMap().containsKey(locVles[i].split(":")[0]) && thumpVle.equals(true)) {
					modalData.append("<tr>\r\n"
							+ "<td><img src = 'img/thumbsup.png' class='logo-tablepopup' alt='Thumbs up' /></td>\r\n"
							+ "<td>Find By " + loctyp + " : "
							+ teststep.getLocatorEleMap().get(locVles[i].split(":")[0]) + "</td>\r\n"
							+ "<td style = 'text-align: center;'><b> " + locVles[i].split(":")[1] + "%</b></td>\r\n"
							+ "</tr>");
					countIncr++;
					thumpVle = false;
				} else if (teststep.getLocatorEleMap().containsKey(locVles[i].split(":")[0])
						&& thumpVle.equals(false)) {
					modalData.append("<tr>\r\n" + "<td>&nbsp;</td>\r\n" + "<td>Find By " + loctyp + " : "
							+ teststep.getLocatorEleMap().get(locVles[i].split(":")[0]) + "</td>\r\n"
							+ "<td style = 'text-align: center;'><b> " + locVles[i].split(":")[1] + "%</b></td>\r\n"
							+ "	</tr>");
					countIncr++;
				}

			}
			if (countIncr >= 5)
				break;
		}

	}

	public void getTrendData(StringBuffer nosuchcounts, StringBuffer stleCounts, StringBuffer nwCounts,
			StringBuffer otrsCounts) {
		noSuchTrendCount = nosuchcounts;
		staleTrendCount = stleCounts;
		netwrkTrendCount = nwCounts;
		othrsTrendCount = otrsCounts;

	}

}
