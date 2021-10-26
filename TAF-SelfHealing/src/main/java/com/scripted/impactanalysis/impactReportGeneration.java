package com.scripted.impactanalysis;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import com.fasterxml.jackson.databind.ObjectMapper;


import junit.framework.Assert;

public class impactReportGeneration {
	static Map<String, String> healedMap;
	static Map<String, String> failedMap;
	static Map<String, List<String>> elementOccuranceMap;
	static Map<String, List<String>> pageCountMap;
	static Map<String, List<String>> tcMap;
	static Map<String, List<String>> tcCountMap;
	static Map<String, Integer> pageChartMap;
	static List<String> pageNames;
	static List<String> topPageNames;
	static List<String> pageNamesCont;
	static List<Integer> tcCountlist;
	static String scrnshotFilePath;
	static String artefactFilePath;
	static String healUserInfo;
	static List<String> pages;
	static List<String> tcNames;
	static StringBuffer buffer = new StringBuffer();
	static StringBuffer trendBuffer = new StringBuffer();
	static StringBuffer sizeBuffer = new StringBuffer();
	static StringBuffer modalBuffer = new StringBuffer();
	static String reportPath;
	public static String cdir = System.getProperty("user.dir");
	public static String currentTimeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
			.format(Calendar.getInstance().getTime());
	public static String username = System.getProperty("user.name");
	static String reportFilePath;

	static String value;
	
	

	public impactReportGeneration(String reportName) throws Exception {

		jsonReader(reportName);
		pageReportFormat();
		modalReportFormat();
		chartReportFormat();
		sizeReportFormat();
		reportWriter();

	}
	
	public static void jsonReader(String reportName) {

		try {
			pageNames = new ArrayList<>();
			String reportPath = cdir +"/healing-results/" + reportName + ".json";
			byte[] jsonData = Files.readAllBytes(Paths.get(reportPath));
			String data = new String(jsonData);
			System.out.println("jsonData :"+data);
			ObjectMapper objectMapper = new ObjectMapper();
			RootTestCase root = objectMapper.readValue(jsonData, RootTestCase.class);
			List<TestCase> testSteps = root.getTestCase();

			elementOccuranceMap = new HashMap<>();
			healedMap = new HashMap<>();
			failedMap = new HashMap<>();
			tcMap = new HashMap<>();
			pageCountMap = new HashMap<>();
			tcCountMap = new HashMap<>();

			for (TestCase testCase : testSteps) {

				for (TestStep testStep : testCase.getTestStep()) {

					if (!testStep.getObjectName().equalsIgnoreCase("null") && !testStep.getObjectName().isEmpty()) {

						if (testStep.getTestStepFailureReason().equalsIgnoreCase("NoSuchElementException")
								|| testStep.getTestStepFailureReason().equalsIgnoreCase("NoSuchWindowException")
								|| testStep.getTestStepFailureReason().equalsIgnoreCase("NoSuchFrameException")
								|| testStep.getTestStepFailureReason().equalsIgnoreCase("NoAlertPresentException")
								|| testStep.getTestStepFailureReason().equalsIgnoreCase("InvalidSelectorException")
								|| testStep.getTestStepFailureReason().equalsIgnoreCase("UnhandledAlertException")) {

							if (!pageNames.contains(testStep.getPageName()))
								pageNames.add(testStep.getPageName());

							if (pageCountMap.get(testStep.getObjectName()) == null) {
								pages = new ArrayList<>();

								pages.add(testStep.getPageName());

								pageCountMap.put(testStep.getObjectName(), pages);

							} else {
								pages = pageCountMap.get(testStep.getObjectName());

								pages.add(testStep.getPageName());

							}

							if (null == elementOccuranceMap.get(testStep.getPageName())) {
								pages = new ArrayList<>();

								pages.add(testStep.getObjectName());

								elementOccuranceMap.put(testStep.getPageName(), pages);

							} else {
								pages = elementOccuranceMap.get(testStep.getPageName());
								if (!pages.contains(testStep.getObjectName()))
									pages.add(testStep.getObjectName());

							}

							if (null == tcMap.get(testStep.getPageName())) {
								pages = new ArrayList<>();

								pages.add(testCase.getTestCaseName());

								tcMap.put(testStep.getPageName(), pages);

							} else {
								pages = tcMap.get(testStep.getPageName());
								if (!pages.contains(testCase.getTestCaseName()))
									pages.add(testCase.getTestCaseName());

							}

							if (null == tcCountMap.get(testStep.getObjectName())) {
								pages = new ArrayList<>();

								pages.add(testCase.getTestCaseName());

								tcCountMap.put(testStep.getObjectName(), pages);

							} else {

								pages = tcCountMap.get(testStep.getObjectName());
								if (!pages.contains(testCase.getTestCaseName()))
									pages.add(testCase.getTestCaseName());

							}

							healedMap.put(testStep.getObjectName(), testStep.getHealedElement());

							failedMap.put(testStep.getObjectName(), testStep.getFailedElement());
						}
					}
				}
			}

			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void pageReportFormat() {

		int tcCountNo = 0;
		pageChartMap = new HashMap();
		for (int i = 0; i < pageNames.size(); i++) {
			String[] array = new String[elementOccuranceMap.get(pageNames.get(i)).size()];
			for (int k = 0; k < array.length; k++) {
				array[k] = elementOccuranceMap.get(pageNames.get(i)).get(k);
				tcCountNo = tcCountNo + tcCountMap.get(array[k]).size();

			}

			buffer.append("<div class=\"panel panel-default\">\r\n" + "          <div class=\"panel-heading\">\r\n"
					+ "            <h4 class=\"panel-title\">\r\n" + "   \r\n"
					+ "              <a class=\"accordion-toggle\" data-toggle=\"collapse\" data-parent=\"#accordion\" href=\"#collapse"+i+"\">\r\n"
					+ pageNames.get(i) + "              </a>\r\n" + "              &nbsp;\r\n"
					+ "              <span class=\"noti_style\">" + tcCountNo + "</span>\r\n" + "            \r\n"
					+ "            </h4>\r\n" + "          </div>\r\n"
					+ "          <div id=\"collapse"+i+"\" class=\"panel-collapse collapse \">\r\n"
					+ "            <div class=\"panel-body\">\r\n" + "              <table style=\"width:100%\";>\r\n"
					+ "                <thead>\r\n" + "                    <tr>\r\n"
					+ "                        <th>Failed Object</th>\r\n"
					+ "                        <th>Failed Locator</th>\r\n"
					+ "                        <th>Updated Locator</th>\r\n"
					+ "                        <th>Test Cases Impacted</th>\r\n"
					+ "                        <th>Details</th>\r\n" + "                    </tr>\r\n"
					+ "                </thead>    ");


			pageChartMap.put(pageNames.get(i), tcCountNo);
			tcCountNo = 0;

			for (int j = 0; j < array.length; j++) {
				array[j] = elementOccuranceMap.get(pageNames.get(i)).get(j);

				buffer.append("  <tr>\r\n" + "                    <td>" + array[j] + "</td>\r\n"
						+ "                    <td>" + failedMap.get(array[j]) + "</td>\r\n"
						+ "                    <td> " + healedMap.get(array[j]) + "</td>\r\n"
						+ "                    <td><b>" + tcCountMap.get(array[j]).size() + "</b></td>\r\n"
						+ "                    <td><a class=\"hyper\" data-toggle=\"modal\" data-target=\"#myModal" + j
						+ i + "\">view</a></td>\r\n" + "                </tr>   ");
			}
			buffer.append("</table>\r\n" + "            </div>\r\n" + "          </div>\r\n" + "        </div> ");

		}

	}

	public static void modalReportFormat() {

		tcNames = new ArrayList<>();
		for (int i = 0; i < pageNames.size(); i++) {
			String[] array = new String[elementOccuranceMap.get(pageNames.get(i)).size()];
			for (int k = 0; k < array.length; k++) {
				array[k] = elementOccuranceMap.get(pageNames.get(i)).get(k);

			}
			for (int l = 0; l < array.length; l++) {

				tcNames = tcCountMap.get(array[l]);

				modalBuffer.append("<!-- Modal1-->\r\n" + "       <div class=\"modal fade\" id=\"myModal" + l + i
						+ "\" role=\"dialog\">\r\n" + "        <div class=\"modal-dialog\">\r\n" + "        \r\n"
						+ "          <!-- Modal content-->\r\n" + "          <div class=\"modal-content\">\r\n"
						+ "            <div class=\"modal-header\">\r\n"
						+ "              <button type=\"button\" class=\"close\" data-dismiss=\"modal\">&times;</button>\r\n"
						+ "              <h5 class=\"modal-title\">Test Cases</h5>\r\n" + "            </div>\r\n"
						+ "            <div class=\"modal-body\">\r\n" + "              <table style=\"width:100%\";>");
				for (int f = 0; f < tcNames.size(); f++) {

					modalBuffer.append("<tr>\r\n" + "                    <td>" + tcNames.get(f) + "</td>   \r\n"
							+ "                </tr>");

				}
				modalBuffer.append("</table>\r\n" + "            </div>\r\n" + "          </div>\r\n"
						+ "        </div>\r\n" + "      </div>\r\n" + "");
			}

		}

	}

	public static void chartReportFormat() {
		int tcCountNo = 0;
		Map<String, Integer> graphMap = new HashMap();
		for (int i = 0; i < pageNames.size(); i++) {
			String[] array = new String[elementOccuranceMap.get(pageNames.get(i)).size()];
			for (int k = 0; k < array.length; k++) {
				array[k] = elementOccuranceMap.get(pageNames.get(i)).get(k);
				tcCountNo = tcCountNo + tcCountMap.get(array[k]).size();

			}
			graphMap.put((pageNames.get(i)), tcCountNo);
			tcCountNo = 0;

		}

		tcCountlist = new ArrayList<>();
		for (int i = 0; i < pageNames.size(); i++) {
			int val = graphMap.get(pageNames.get(i));
			tcCountlist.add(val);
		}
		topPageNames = new ArrayList<>();
		List<String> listOfKeys = null;
		Collections.sort(tcCountlist, Collections.reverseOrder());
		for (int c = 0; c < tcCountlist.size(); c++) {
			if (c < 10) {
				listOfKeys = getAllKeysForValue(graphMap, tcCountlist.get(c));

				if (!topPageNames.containsAll(listOfKeys))
					topPageNames.addAll(listOfKeys);

			}
		}

		if (topPageNames.size() >= 5) {
			trendBuffer.append("labels: [\"" + topPageNames.get(0) + "\", \"" + topPageNames.get(1) + "\", \""
					+ topPageNames.get(2) + "\", \"" + topPageNames.get(3) + "\",\"" + topPageNames.get(4) + "\"],\r\n"
					+ "        datasets: [\r\n" + "          {\r\n" + "            label: \"Number of TestCase\",\r\n"
					+ "            data: [" + pageChartMap.get(topPageNames.get(0)) + ","
					+ pageChartMap.get(topPageNames.get(1)) + "," + pageChartMap.get(topPageNames.get(2)) + ","
					+ pageChartMap.get(topPageNames.get(3)) + "," + pageChartMap.get(topPageNames.get(4)) + ",],");

		} else if (topPageNames.size() == 4) {
			trendBuffer.append("labels: [\"" + topPageNames.get(0) + "\", \"" + topPageNames.get(1) + "\", \""
					+ topPageNames.get(2) + "\", \"" + topPageNames.get(3) + "\"],\r\n" + "        datasets: [\r\n"
					+ "          {\r\n" + "            label: \"Number of TestCase\",\r\n" + "            data: ["
					+ pageChartMap.get(topPageNames.get(0)) + "," + pageChartMap.get(topPageNames.get(1)) + ","
					+ pageChartMap.get(topPageNames.get(2)) + "," + pageChartMap.get(topPageNames.get(3)) + ",],");
		} else if (topPageNames.size() == 3) {
			trendBuffer.append("labels: [\"" + topPageNames.get(0) + "\", \"" + topPageNames.get(1) + "\", \""
					+ topPageNames.get(2) + "\"],\r\n" + "        datasets: [\r\n" + "          {\r\n"
					+ "            label: \"Number of TestCase\",\r\n" + "            data: ["
					+ pageChartMap.get(topPageNames.get(0)) + "," + pageChartMap.get(topPageNames.get(1)) + ","
					+ pageChartMap.get(topPageNames.get(2)) + ",],");
		} else if (topPageNames.size() == 2) {
			trendBuffer.append("labels: [\"" + topPageNames.get(0) + "\", \"" + topPageNames.get(1) + "\"],\r\n"
					+ "        datasets: [\r\n" + "          {\r\n" + "            label: \"Number of TestCase\",\r\n"
					+ "            data: [" + pageChartMap.get(topPageNames.get(0)) + ","
					+ pageChartMap.get(topPageNames.get(1)) + ",],");
		} else if (topPageNames.size() == 1) {
			trendBuffer.append("labels: [\"" + topPageNames.get(0) + "\"],\r\n" + "        datasets: [\r\n"
					+ "          {\r\n" + "            label: \"Number of TestCase\",\r\n" + "            data: ["
					+ pageChartMap.get(topPageNames.get(0)) + ",],");
		} else {
			trendBuffer.append("labels: [\"\"],\r\n" + "        datasets: [\r\n" + "          {\r\n"
					+ "            label: \"Number of TestCase\",\r\n" + "            data: [],");
		}

	}

	public static void sizeReportFormat() {

		if (pageChartMap.isEmpty()) {
			sizeBuffer.append("1");
		} else if (pageChartMap.get(topPageNames.get(0)) >= 10 && pageChartMap.get(topPageNames.get(0)) <= 20) {
			sizeBuffer.append("2");
		} else if (pageChartMap.get(topPageNames.get(0)) > 20 && pageChartMap.get(topPageNames.get(0)) <= 50) {
			sizeBuffer.append("5");
		} else if (pageChartMap.get(topPageNames.get(0)) > 50) {
			sizeBuffer.append("10");
		} else {
			sizeBuffer.append("1");
		}
	}

	private static void copyFolder(File sourceFolder, File destinationFolder) throws IOException {
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
		} else

		{
			Files.copy(sourceFolder.toPath(), destinationFolder.toPath(), StandardCopyOption.REPLACE_EXISTING);
		}

	}

	static <K, V> List<K> getAllKeysForValue(Map<K, V> mapOfWords, V value) {
		List<K> listOfKeys = null;

		if (mapOfWords.containsValue(value)) {
			listOfKeys = new ArrayList<>();
			for (Map.Entry<K, V> entry : mapOfWords.entrySet()) {
				if (entry.getValue().equals(value)) {
					listOfKeys.add(entry.getKey());
				}
			}
		}
		return listOfKeys;
	}
	public static String makeDirs(String path) {
		String pattern = Pattern.quote(System.getProperty("file.separator"));
		String[] folders = new File(path).getAbsolutePath().split(pattern);
		String filePath = "";
		File newFile = null;
		for (String folder : folders) {
			filePath = filePath + System.getProperty("file.separator") + folder;
			newFile = new File(filePath);
			if (!newFile.exists() && !folder.contains(":") && !folder.contains(".")) {
				newFile.mkdir();
				if (!newFile.isDirectory()) {
				//	log.info("Unable to create parent directories of " + filePath);
					Assert.fail("Unable to create parent directories of " + filePath );
					throw new RuntimeException("Unable to create parent directories of " + filePath);
					
				}
				//log.info("Created : " + newFile.getAbsolutePath());
			}
		}

		return newFile.getAbsolutePath();
	}
	public static void reportWriter() throws Exception {
		PrintWriter pw;
		reportFilePath = cdir + "\\SkriptmateReport\\ImpactReport" + "\\" + currentTimeStamp;
		makeDirs(reportFilePath);
		File destinationFolder = new File(reportFilePath);

		File sourceFolder = new File(cdir + "\\src\\main\\resources\\HealingConfigurations\\ImpactReportArtefacts");
		copyFolder(sourceFolder, destinationFolder);
		try {
			System.out.println("Started to create report");

			DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh.mm.ss aa");
			String dateString = dateFormat.format(new Date()).toString();
			System.out.println("Current date and time in AM/PM: " + dateString);
			pw = new PrintWriter(new FileWriter(reportFilePath + "\\" + "Impact_Report.html"));
			pw.println("<!DOCTYPE html>\r\n" + "<html lang=\"en\">\r\n" + "\r\n" + "<head>\r\n"
					+ "  <meta charset=\"utf-8\">\r\n"
					+ "  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\r\n"
					+ "  <title>Impact Analyzer Report</title>\r\n"
					+ "  <link rel=\"stylesheet\" href=\"css/bootstrap.min.css\">\r\n"
					+ "  <link rel=\"stylesheet\" type=\"text/css\" href=\"css/jquery.dataTables.min.css\">\r\n"
					+ "  <link rel=\"stylesheet\" type=\"text/css\" href=\"css/IA_style.css\">\r\n"
					+ "  <script src=\"js/jquery.min.js\"></script>\r\n"
					+ "  <script src=\"js/jquery-3.3.1.min.js\"></script>\r\n"
					+ "  <script src=\"js/bootstrap.min.js\"></script>\r\n"
					+ "  <script src=\"js/jquery.dataTables.min.js\"></script>\r\n" + "\r\n" + "</head>");
			pw.println("<body>\r\n" + "  <div class=\"container\">\r\n" + "    <div class=\"header\">\r\n"
					+ "      <div class=\"logo_sec\">\r\n"
					+ "        <img src=\"img/sklogo.png\" class=\"logo-size\" alt=\"logo-skrptmate\"/>\r\n"
					+ "      </div>\r\n" + "      <div class=\"text_sec\">\r\n"
					+ "        <div class=\"heading1\">Impact Analyzer Report</div>\r\n"
					+ "        <div class=\"heading2\"><span>Executed By:</span><span>" + username + "</span></div>\r\n"
					+ "        <div class=\"heading2\"><span>Execution Time:</span><span>" + dateString
					+ "</span></div>\r\n" + "      </div>\r\n" + "    </div>\r\n" + "\r\n"
					+ "    <div class=\"row section_1\"> \r\n" + "      <div>\r\n"
					+ "        <canvas id=\"mixed-chart\" height=\"100px\"></canvas>\r\n" + "      </div>\r\n" + " \r\n"
					+ "    </div>");
			pw.println("<div class=\"table_wrap\">\r\n" + "      <div class=\"panel-group\" id=\"accordion\">");

			pw.println(buffer);

			pw.println("</div>\r\n" + "\r\n" + "        </div>");

			pw.println(modalBuffer);

			pw.println("\r\n" + "   </div>\r\n" + "</body>\r\n" + "\r\n" + "<script src=\"js/Chart.js\"></script>\r\n"
					+ "\r\n" + "\r\n" + "<script>\r\n" + "  new Chart(document.getElementById(\"mixed-chart\"), {\r\n"
					+ "      type: 'bar',\r\n" + "      data: {");
			pw.println(trendBuffer);
			pw.println("  backgroundColor: [\r\n" + "\r\n" + "                'rgba(3, 169, 244, 0.8)',\r\n"
					+ "                'rgba(0, 188, 212, 0.8)',\r\n" + "                'rgba(0, 150, 136, 0.8)',\r\n"
					+ "                'rgba(76, 175, 80, 0.8)',\r\n" + "                'rgba(139, 195, 74, 0.8)',\r\n"
					+ "                \r\n" + "            ],\r\n" + "            borderColor: [\r\n"
					+ "              \r\n" + "                'rgba(3, 169, 244, 1)',\r\n"
					+ "                'rgba(0, 188, 212, 1)',\r\n" + "                'rgba(0, 150, 136, 1)',\r\n"
					+ "                'rgba(76, 175, 80, 1)',\r\n" + "                'rgba(139, 195, 74, 1)',\r\n"
					+ "               \r\n" + "            ],\r\n" + "            borderWidth: .5,\r\n"
					+ "            \r\n" + "           \r\n" + "          }\r\n" + "        ]\r\n" + "      },\r\n"
					+ "      options: {\r\n" + "        title: {\r\n" + "        display: true,\r\n"
					+ "        text: 'High Impacted Test Cases',\r\n" + "        fontSize: 14,\r\n"
					+ "        fontColor: '#2a3f54'\r\n" + "  },\r\n" + "  legend: {\r\n" + "   display:false\r\n"
					+ "        },\r\n" + "  scales: {\r\n" + "    yAxes: [{\r\n" + "      scaleLabel: {\r\n"
					+ "        display: true,\r\n" + "        labelString: 'Test Cases Impacted',\r\n" + "      },\r\n"
					+ "      ticks: {\r\n" + "        stepSize:");
			pw.println(sizeBuffer);
			pw.println("  ,\r\n" + "        min: 0,\r\n" + "            }\r\n" + "    }],\r\n" + "  }  \r\n"
					+ "       \r\n" + "      }\r\n" + "  });\r\n" + "  \r\n" + "  </script>\r\n" + "\r\n" + "\r\n"
					+ "\r\n" + "</html>");
			pw.close();
			System.out.println(" Impact report created successfully");
		} catch (IOException e) {

			e.printStackTrace();
		}

	}

}
