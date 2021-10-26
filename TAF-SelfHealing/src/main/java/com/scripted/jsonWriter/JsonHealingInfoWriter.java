package com.scripted.jsonWriter;

import java.io.File;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scripted.reporting.selfhealing.ReportGenerator;
import com.scripted.reporting.selfhealing.ReportGenerationDummy;
import com.scripted.reporting.selfhealing.SortFilesByDate;
import com.scripted.selfhealing.HealingConfig;

public class JsonHealingInfoWriter {
	private static String cdir = System.getProperty("user.dir");
	static StringBuffer trendBuffer = new StringBuffer();
	static List<String> fileNamesList = new ArrayList<String>();
	static List<String> sortedFileNameList = new ArrayList<String>();
	static StringBuffer nosuchcounts = new StringBuffer();
	static StringBuffer stleCounts = new StringBuffer();
	static StringBuffer nwCounts = new StringBuffer();
	static StringBuffer otrsCounts = new StringBuffer();
	static int[] otrscounts;
	static LinkedHashMap<String, Integer> prevCycleCounts = new LinkedHashMap<>();
	static ObjectMapper mapper = new ObjectMapper();
	static HealingInfo healingInfo = new HealingInfo();
	static int index = 0;
	static int NoSuchElementFnlCount = 0;
	static int networkErrorFnlCount = 0;
	static int staleTimeoutFnlCount = 0;
	static int othersFnlCount = 0;
	static SortFilesByDate sortFilesByDate = new SortFilesByDate();
	static int counter;
	//


	private static HealingInfo createHealingInfo() {
		healingInfo.setTestRunName("TestRun1");
		healingInfo.setNoSuchEleCount(NoSuchElementFnlCount);
		healingInfo.setStleTmeOutCount(staleTimeoutFnlCount);
		healingInfo.setNetwrkErrCount(networkErrorFnlCount);
		healingInfo.setOthersCount(othersFnlCount);
		counter++;
		return healingInfo;

	}

	public static void setCountValues(ConcurrentHashMap<String, Integer> HealingCount) {
		NoSuchElementFnlCount = HealingCount.get("NoSuchElement").intValue() + NoSuchElementFnlCount;
		networkErrorFnlCount = HealingCount.get("networkErrorCount").intValue() + networkErrorFnlCount;
		staleTimeoutFnlCount = HealingCount.get("staleTimeoutCount").intValue() + staleTimeoutFnlCount;
		othersFnlCount = HealingCount.get("othersCount").intValue() + othersFnlCount;
	}

	public static void AddStep(ConcurrentHashMap<String, String> HealingInfo,
			ConcurrentHashMap<String, Integer> HealingCount,ConcurrentHashMap<String, String> jsonDataValMap,String Technology) {
	//	System.out.println("Healing count map : "+HealingCount +" testcase name : "+HealingInfo.get("TestCase"));
		setCountValues(HealingCount);
		TestStep step = new TestStep();
		step.setFailedElement(HealingInfo.get("locatorType") + " : " + HealingInfo.get("locatorValue"));
		step.setTestStepFailureReason(HealingInfo.get("ExceptionType"));
		step.setHealedElement(HealingInfo.get("HealdEleTyp")+" : "+HealingInfo.get("HealedLocator"));
		step.setHealStatus(HealingInfo.get("HealStatus"));
		step.setPageName(HealingInfo.get("PageName"));
		step.setObjectName(HealingInfo.get("ElementName"));
		step.setLocatorEleMap(jsonDataValMap);
		step.setTechnology(Technology);
		step.setScrnShotPath(HealingInfo.get("ImageFileName"));
		step.setExcptnMsg(HealingInfo.get("ExcptnMsg"));//ExcptnMsg
		step.setAction(HealingInfo.get("stepActionName"));
		// System.out.println(step);

		if (healingInfo.testCase.size() == 0) {
			TestCases testCase = new TestCases();
			testCase.setTestCaseName(HealingInfo.get("TestCase"));
			testCase.testSteps.add(step);
			healingInfo.testCase.add(testCase);
			return;
		}

		// System.out.println("Existing: "+healingInfo.testCase.get(index).TestCaseName
		// + " New: " + TestCaseName);
		Boolean flag= false;
		for(TestCases testcase:healingInfo.getTestCase())
		{
			if(testcase.getTestCaseName().equalsIgnoreCase(HealingInfo.get("TestCase")))
			{
				flag=true;
				testcase.testSteps.add(step);
			}
		}
		
		if(!flag)
		{
			TestCases testCase = new TestCases();
			testCase.setTestCaseName(HealingInfo.get("TestCase"));
			testCase.testSteps.add(step);
			healingInfo.testCase.add(testCase);
			index++;
		}
		/*if (healingInfo.testCase.get(index).TestCaseName.equalsIgnoreCase(HealingInfo.get("TestCase"))) {
		
			healingInfo.testCase.get(index).testSteps.add(step);
		} else {
			TestCases testCase = new TestCases();
			testCase.setTestCaseName(HealingInfo.get("TestCase"));
			testCase.testSteps.add(step);
			healingInfo.testCase.add(testCase);
			index++;
		}*/

	}

	public static void jsonWriter() {
		try {
			/*
			 * DateFormat dateFormat = new SimpleDateFormat("dd_MM_yyyy_hh_mm_ss_ms_aa");
			 * String dateString = dateFormat.format(new Date()).toString(); jsonReportNme =
			 * "jsonResults_" + dateString; // dateFormatFolder.format(new
			 * Date()).toString();
			 * 
			 */
			//System.out.println("Counter value :"+counter);
			HealingInfo healinginfo = createHealingInfo();
			//mapper.writeValue(new File(cdir + "\\healing-results\\" + jsonReportNme + ".json"), healinginfo);
			mapper.writerWithDefaultPrettyPrinter().writeValue(new File(cdir + "/healing-results/" + HealingConfig.jsonRptNme + ".json"), healinginfo);
			String jsonInString = mapper.writeValueAsString(healinginfo); // For printing json value
			// System.out.println(jsonInString);
			getTrendValue();
			ReportGenerator reportGenJson = new ReportGenerator();
			reportGenJson.getTrendData(nosuchcounts, stleCounts, nwCounts, otrsCounts);

		} catch (Exception e) {
			e.printStackTrace();
			// System.out.println("Error while trying to create healing report json");
		}
	}

	public static void jsonReader(String jsonName) {

		try {

			byte[] jsonData = Files.readAllBytes(Paths.get(cdir + "\\healing-results\\" + jsonName));
			ObjectMapper objectMapper = new ObjectMapper();

			HealingInfo healingInfo = objectMapper.readValue(jsonData, HealingInfo.class);
			String jsonInString = mapper.writeValueAsString(healingInfo);
			// System.out.println("Json in string: " + jsonInString);

			prevCycleCounts.put("othersFnlCount", healingInfo.getOthersCount());
			otrsCounts.append(healingInfo.getOthersCount() + ",");

			prevCycleCounts.put("noSuchEleCount", healingInfo.getNoSuchEleCount());
			nosuchcounts.append(healingInfo.getNoSuchEleCount() + ",");

			prevCycleCounts.put("staleTimeoutFnlCount", healingInfo.getStleTmeOutCount());
			stleCounts.append(healingInfo.getStleTmeOutCount() + ",");

			prevCycleCounts.put("networkErrorFnlCount", healingInfo.getNetwrkErrCount());
			nwCounts.append(healingInfo.getNetwrkErrCount() + ",");

			// System.out.println(prevCycleCounts);

		} catch (Exception e) {
			System.out.println("Exception :" + e);
		}
	}

	public static void getTrendValue() {

		getFileList();

		nosuchcounts.append("[");
		stleCounts.append("[");
		nwCounts.append("[");
		otrsCounts.append("[");
		for (String sortedFileNme : sortedFileNameList) {
			//System.out.println("Sorted File Name :" + sortedFileNme);
			jsonReader(sortedFileNme);
		}
		nosuchcounts.append("]");
		stleCounts.append("]");
		nwCounts.append("]");
		otrsCounts.append("]");
		//System.out.println("nosuchcounts :" + nosuchcounts);
		//System.out.println("stleCounts :" + stleCounts);
		//System.out.println("nwCounts :" + nwCounts);
		//System.out.println("otrsCounts :" + otrsCounts);

	}

	public static void getFileList() {
		/*
		 * File file = new File(cdir + "\\healing-results\\"); String[] fileNames;
		 * fileNames = file.list();
		 */

		File dir = new File(cdir + "/healing-results/");
		File[] files = dir.listFiles();
		File[] sortedFiles;

		sortedFiles = SortFilesByDate.sortFilesByDateCreated(files);
		for (File file : sortedFiles) {

			fileNamesList.add(file.getName());

		}
		//System.out.println("After sorting :" +fileNamesList);
		int fileNumCount = fileNamesList.size();
		// System.out.println("File number count :" + fileNumCount);
		if (fileNumCount > 5) {
			int j = 1;
			while (j < 6) {
				// System.out.println("Filenames after sorting : " +
				// fileNamesList.get(fileNumCount - j));
				sortedFileNameList.add(fileNamesList.get(fileNumCount - j));
				j++;
			}
		} else {
			sortedFileNameList.addAll(fileNamesList);
		}
		//System.out.println("Sorted list size" + sortedFileNameList.size());
		//System.out.println("Sorted List : " + sortedFileNameList);
	}

}
