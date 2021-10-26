package com.scripted.reporting.selfhealing;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
//import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

import com.scripted.selfhealing.SMWebHealer;

public class ReportGenerationDummy {
	public static Logger LOGGER = Logger.getLogger(ReportGenerationDummy.class);
	private static String cdir = System.getProperty("user.dir");

	static StringBuffer buffer = new StringBuffer();
	static StringBuffer mobileBuffer = new StringBuffer();
	static StringBuffer trendBuffer = new StringBuffer();
	// static String reportPath;
	static Boolean mobileTechnology = false;
	static Boolean webTechnology = false;
	static int NoSuchElementFnlCount = 0;
	static int networkErrorFnlCount = 0;
	static int staleTimeoutFnlCount = 0;
	static int othersFnlCount = 0;
	static String[] errorMsg;
	static int errorItr;
	static int successCount = 0;
	static int failureCount = 0;
	static int loopCount = 0;
	static int successPercent;
	static String reportFilePath;
	static String scrnshotFilePath;
	static String artefactFilePath;
	static String healUserInfo;

	public ReportGenerationDummy() {

	}

	public ReportGenerationDummy(String reportPath, String screenshotPath, String artefactpath, String userInfo) {
		ReportGenerationDummy.reportFilePath = reportPath;
		ReportGenerationDummy.scrnshotFilePath = screenshotPath;
		ReportGenerationDummy.artefactFilePath = artefactpath;
		ReportGenerationDummy.healUserInfo = userInfo;
	}

	public void reportWriter() {
		PrintWriter pw;
		try {
			successPercentCount();
			// System.out.println("Screenshot path in report generator :"+scrnshotFilePath);
			// System.out.println("Report path in report generator :"+reportFilePath);
			// System.out.println("Artefact path in report generator :"+artefactFilePath);
			DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh.mm.ss aa");
			String dateString = dateFormat.format(new Date()).toString();
			// System.out.println("Current date and time in AM/PM: " + dateString);
			// System.out.println("Report file path :"+reportFilePath);
			pw = new PrintWriter(new FileWriter(reportFilePath + "\\" + "SelfHealingReport.html"));
			pw.println("<!DOCTYPE html>\r\n" + "<html lang=\"en\">\r\n" + "\r\n" + "<head>\r\n"
					+ "    <meta charset=\"utf-8\">\r\n"
					+ "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\r\n"
					+ "    <title>Self Healing Report</title>\r\n"
					+ "    <link rel=\"stylesheet\" href=\"css/bootstrap.min.css\">\r\n"
					+ "    <link rel=\"stylesheet\" type=\"text/css\" href=\"css/jquery.dataTables.min.css\">\r\n"
					+ "    <link rel=\"stylesheet\" type=\"text/css\" href=\"css/SH_style.css\">\r\n"
					+ "    <script src=\"js/jquery.min.js\"></script>\r\n"
					+ "    <script src=\"js/jquery-3.3.1.min.js\"></script>\r\n"
					+ "    <script src=\"js/bootstrap.min.js\"></script>\r\n"
					+ "    <script src=\"js/jquery.dataTables.min.js\"></script>\r\n"
					+ "    <script src=\"js/Chart.js\"></script>");
			// jquery plugins for pagination(web and mobile) and also script for displaying
			// screenshot
			pw.println("    <script>\r\n"
					+ "        $('body').on('click', 'filterImageMob', function () { alert('it works'); })\r\n" + "\r\n"
					+ "        $(document).ready(function () {\r\n" + "            $('#tblWeb').DataTable({\r\n"
					+ "                \"lengthMenu\": [[10, 25, 50, -1], [10, 25, 50, \"All\"]]\r\n"
					+ "            });\r\n" + "        });\r\n" + "\r\n" + "        $(document).ready(function () {\r\n"
					+ "            $('#tblMob').DataTable({\r\n"
					+ "                \"lengthMenu\": [[10, 25, 50, -1], [10, 25, 50, \"All\"]]\r\n"
					+ "            });\r\n" + "        });\r\n" + "    </script>");
			// display image on screenshot
			pw.println("    <script>\r\n" + "        function displayImage(imgsrc, text) {\r\n"
					+ "            document.getElementById('myHeader').innerText = text;\r\n"
					+ "            document.getElementById('myImg').src = imgsrc;\r\n" + "        }\r\n" + "\r\n"
					+ "\r\n" + "\r\n" + "    </script>");
			pw.println("	<script>\r\n" + "	function displayImage1(imgsrc, text) {\r\n"
					+ "		document.getElementById('myHeader').innerText = text;\r\n"
					+ "		document.getElementById('myImg1').src = imgsrc;\r\n" + "	}\r\n" + "</script>");
			/// Heading , name, time
			pw.println("</head>\r\n" + "<body>\r\n" + "    <div class=\"container\">\r\n"
					+ "        <div class=\"header\">\r\n" + "            <div class=\"logo_sec\">\r\n"
					+ "                <img src=\"img/sklogo.png\" class=\"logo-size\" alt=\"logo-skrptmate\" />\r\n"
					+ "            </div>\r\n" + "            <div class=\"text_sec\">\r\n"
					+ "                <div class=\"heading1\">Healing Test Report</div>\r\n"
					+ "                <div class=\"heading2\"><span>Executed By:</span><span>" + healUserInfo
					+ "</span></div>\r\n" + "<div class=\"heading2\"><span>Execution Time:</span><span>" + dateString
					+ "</span></div>\r\n" + "</div>\r\n" + "</div>");
			// Whole counts for different exceptions
			pw.println("<div class=\"row section_1\">\r\n" + "\r\n"
					+ "            <div class=\"col-md-6 col-lg-6 martp_style\">\r\n"
					+ "                <div style=\"margin-top:20px;\">\r\n"
					+ "                    <div class=\"box_style1\">\r\n"
					+ "                        <div class=\"box_1\">\r\n"
					+ "                            <div class=\"boxlabel1\">NoSuchElement</div>\r\n"
					+ "                            <div class=\"boxlabel2\">" + NoSuchElementFnlCount + "</div>\r\n"
					+ "                        </div>\r\n" + "                        <div class=\"logo_sec1\">\r\n"
					+ "                            <img src=\"img/noelement.png\" class=\"logo-size1\" alt=\"NoSuchElement\" />\r\n"
					+ "                        </div>\r\n" + "                    </div>\r\n" + "\r\n"
					+ "                    <div class=\"box_style2\">\r\n"
					+ "                        <div class=\"box_1\">\r\n"
					+ "                            <div class=\"boxlabel1\">Stale/Timeout</div>\r\n"
					+ "                            <div class=\"boxlabel2\">" + staleTimeoutFnlCount + "</div>\r\n"
					+ "                        </div>\r\n" + "                        <div class=\"logo_sec1\">\r\n"
					+ "                            <img src=\"img/timeout.png\" class=\"logo-size1\" alt=\"Stale/Timeout\" />\r\n"
					+ "                        </div>\r\n" + "                    </div>\r\n"
					+ "                </div>\r\n" + "\r\n" + "                <div class=\"btm_box\">\r\n"
					+ "                    <div class=\"box_style3\">\r\n"
					+ "                        <div class=\"box_1\">\r\n"
					+ "                            <div class=\"boxlabel1\">Network Failures</div>\r\n"
					+ "                            <div class=\"boxlabel2\">" + networkErrorFnlCount + "</div>\r\n"
					+ "                        </div>\r\n" + "                        <div class=\"logo_sec1\">\r\n"
					+ "                            <img src=\"img/networkfailures.png\" class=\"logo-size1\" alt=\"Network Failures\" />\r\n"
					+ "                        </div>\r\n" + "                    </div>\r\n" + "\r\n"
					+ "                    <div class=\"box_style4\">\r\n"
					+ "                        <div class=\"box_1\">\r\n"
					+ "                            <div class=\"boxlabel1\">Others</div>\r\n"
					+ "                            <div class=\"boxlabel2\">" + othersFnlCount + "</div>\r\n"
					+ "                        </div>\r\n" + "                        <div class=\"logo_sec1\">\r\n"
					+ "                            <img src=\"img/others.png\" class=\"logo-size1\" alt=\"Others\" />\r\n"
					+ "                        </div>\r\n" + "                    </div>\r\n"
					+ "                </div>");

			pw.println(
					" <span class=\"trend_style\" data-toggle=\"modal\" data-target=\"#myModal2\">Last 5 Execution Trend</span>\r\n"
							+ "            </div>\r\n" + "\r\n" + "            <div class=\"col-md-6 col-lg-6\">\r\n"
							+ "\r\n" + "                <div>\r\n"
							+ "                    <canvas id=\"myChart\"></canvas>\r\n" + "                </div>\r\n"
							+ "                <div class=\"perc_style\">" + successPercent + "%" + "</div>\r\n"
							+ "            </div>\r\n" + "        </div>");

			pw.println("<div class=\"table_wrap\">\r\n" + "<div class=\"panel-group\" id=\"accordion\">");
			if (webTechnology.equals(true)) {
				pw.println(" <div class=\"panel panel-default\">\r\n"
						+ "                    <div class=\"panel-heading\">\r\n"
						+ "                        <h4 class=\"panel-title\">\r\n"
						+ "                            <a class=\"accordion-toggle\" data-toggle=\"collapse\" data-parent=\"#accordion\" href=\"#collapseOne\">\r\n"
						+ "                                Web\r\n" + "                            </a>\r\n"
						+ "                        </h4>\r\n" + "                    </div>\r\n"
						+ "                    <div id=\"collapseOne\" class=\"panel-collapse collapse in\">\r\n"
						+ "                        <div class=\"panel-body\">\r\n"
						+ "                            <div class=\"filter_wrap\"><a href=\"#\" id=\"filterImageWeb\"><img src=\"img/clear_filter.png\" class=\"filter_sz\" alt=\"Clear filter\" /></a></div>\r\n"
						+ "                            <table id=\"tblWeb\" style=\"width: 100%; word-wrap:break-word; table-layout:fixed;\">\r\n"
						+ "                                <thead>\r\n" + "                                    <tr>\r\n"
						+ "                                        <th style=\"width: 10px;\">TestCase Name</th>\r\n"
						+ "                                        <th style=\"width: 5px;\">Page Name</th>\r\n"
						+ "                                        <th style=\"width: 5px;\">Object Name</th>\r\n"
						+ "                                        <th style=\"width: 5px;\">TestStep Action</th>\r\n"
						+ "                                        <th style=\"width: 5px;\">Failed Element</th>\r\n"
						+ "                                        <th style=\"width: 5px;\">Exception</th>\r\n"
						+ "                                        <th style=\"width: 5px;\">Healed Element</th>\r\n"
						+ "                                        <th style=\"width: 5px;\">Healed status</th>\r\n"
						+ "                                    </tr>\r\n"
						+ "                                </thead>\r\n" + "<tbody>");
				pw.println(buffer);
				pw.println("           </tbody>\r\n" + "                            </table>\r\n"
						+ "                        </div>\r\n" + "                    </div>\r\n"
						+ "                </div>");
			}
			if (mobileTechnology.equals(true)) {
				// Mobile table

				pw.println("<div class=\"panel panel-default\">\r\n"
						+ "                    <div class=\"panel-heading\">\r\n"
						+ "                        <h4 class=\"panel-title\">\r\n"
						+ "                            <a class=\"accordion-toggle\" data-toggle=\"collapse\" data-parent=\"#accordion\" href=\"#collapseTwo\">\r\n"
						+ "                                Mobile\r\n" + "                            </a>\r\n"
						+ "                        </h4>\r\n" + "                    </div>\r\n"
						+ "                    <div id=\"collapseTwo\" class=\"panel-collapse collapse\">\r\n"
						+ "                        <div class=\"panel-body\">\r\n"
						+ "                            <br />\r\n"
						+ "                            <div class=\"filter_wrap\"><a href=\"#\" id=\"filterImageMob\"><img src=\"img/clear_filter.png\" class=\"filter_sz\" alt=\"Clear filter\" /></a></div>\r\n"
						+ "                            <table id=\"tblMob\" style=\"width: 100%; word-wrap:break-word; table-layout:fixed;\">\r\n"
						+ "                                <thead>\r\n" + "                                    <tr>\r\n"
						+ "                                        <th style=\"width: 10px;\">TestCase Name</th>\r\n"
						+ "                                        <th style=\"width: 5px;\">Page Name</th>\r\n"
						+ "                                        <th style=\"width: 5px;\">Object Name</th>\r\n"
						+ "                                        <th style=\"width: 5px;\">TestStep Action</th>\r\n"
						+ "                                        <th style=\"width: 5px;\">Failed Element</th>\r\n"
						+ "                                        <th style=\"width: 5px;\">Exception</th>\r\n"
						+ "                                        <th style=\"width: 5px;\">Healed Element</th>\r\n"
						+ "                                        <th style=\"width: 5px;\">Healed status</th>\r\n"
						+ "                                    </tr>\r\n"
						+ "                                </thead>\r\n" + "                                <tbody>");
				pw.println(mobileBuffer);
				pw.print("           </tbody>\r\n" + "                            </table>\r\n"
						+ "                        </div>\r\n" + "                    </div>\r\n"
						+ "                </div>");
			}
			pw.println(" </div>\r\n" + "\r\n" + "        </div>\r\n" + "\r\n" + "\r\n" + "        <!-- Modal1-->\r\n"
					+ "        <div class=\"modal fade\" id=\"myModal\" role=\"dialog\">\r\n"
					+ "            <div class=\"modal-dialog\">\r\n" + "\r\n"
					+ "                <!-- Modal content-->\r\n" + "                <div class=\"modal-content\">\r\n"
					+ "                    <div class=\"modal-header\">\r\n"
					+ "                        <button type=\"button\" class=\"close\" data-dismiss=\"modal\">&times;</button>\r\n"
					+ "                        <h5 class=\"modal-title\" id=\"myHeader\">Screenshot</h5>\r\n"
					+ "                    </div>\r\n" + "                    <div class=\"modal-body\">\r\n"
					+ "                        <p><img id=\"myImg\" alt='Screenshot_icon' style=\"width:98%;height:400px;border: 1px solid #ddd;border-radius: 4px;padding: 5px;\" /></p>\r\n"
					+ "                    </div>\r\n" + "\r\n" + "                </div>\r\n" + "\r\n"
					+ "            </div>\r\n" + "        </div>");

			pw.println("<!-- Modal2 -->\r\n" + "        <div class=\"modal fade\" id=\"myModal2\" role=\"dialog\">\r\n"
					+ "            <div class=\"modal-dialog\">\r\n" + "\r\n"
					+ "                <!-- Modal content-->\r\n" + "                <div class=\"modal-content\">\r\n"
					+ "                    <div class=\"modal-header\">\r\n"
					+ "                        <button type=\"button\" class=\"close\" data-dismiss=\"modal\">&times;</button>\r\n"
					+ "                        <h5 class=\"modal-title\">Last 5 Execution Trend</h5>\r\n"
					+ "                    </div>\r\n" + "                    <div class=\"modal-body\">\r\n"
					+ "                        <div>\r\n"
					+ "                            <canvas id=\"line-chart\"></canvas>\r\n"
					+ "                        </div>\r\n" + "                    </div>\r\n" + "\r\n"
					+ "                </div>\r\n" + "\r\n" + "            </div>\r\n" + "        </div>\r\n" + "\r\n"
					+ "\r\n");
			pw.println("<!-- Modal3-->\r\n" + "<div class=\"modal fade\" id=\"myModal3\" role=\"dialog\">\r\n"
					+ "	<div class=\"modal-dialog\" style=\"width: fit-content;\">\r\n" + "\r\n"
					+ "		<!-- Modal content-->\r\n" + "		<div class=\"modal-content\">\r\n"
					+ "			<div class=\"modal-header\">\r\n"
					+ "				<button type=\"button\" class=\"close\" data-dismiss=\"modal\">&times;</button>\r\n"
					+ "				<h5 class=\"modal-title\" id=\"myHeader\">Screenshot</h5>\r\n"
					+ "			</div>\r\n" + "			<div class=\"modal-body\">\r\n" + "				<p>\r\n"
					+ "					<img id=\"myImg1\" alt='Screenshot_icon'\r\n"
					+ "						style=\"width: 300px; border: 1px solid #ddd; border-radius: 4px; padding: 5px;\" />\r\n"
					+ "				</p>\r\n" + "			</div>\r\n" + "\r\n" + "		</div>\r\n" + "\r\n"
					+ "	</div>\r\n" + "</div>");
			pw.println("</div>  </body> <head>");
			pw.println("<script src=\"js/Chart.js\"></script>\r\n" + "\r\n" + "<script>\r\n"
					+ "    var ctx = document.getElementById(\"myChart\").getContext('2d');\r\n"
					+ "    var myChart = new Chart(ctx, {\r\n" + "      type: 'doughnut',\r\n" + "      data: {\r\n"
					+ "        labels: [\"Success\", \"Failure\"],\r\n" + "        datasets: [{\r\n"
					+ "          backgroundColor: [\r\n" + "            \"#97cc64\",\r\n"
					+ "            \"#fd5a3e\"\r\n" + "          ],\r\n" + "          data: [" + successCount + ","
					+ failureCount + "]\r\n" + "        }]\r\n" + "      }\r\n" + "    });\r\n" + "    </script>");

			pw.println(trendBuffer);
			pw.println("<script>\r\n" + "\r\n" + "        $(document).ready(function () {\r\n"
					+ "            $(\"#tblWeb_filter\").remove();\r\n"
					+ "            $(\"<div class='dataTables_filter' id='tblWeb_exfilter'><label>   Filter <select id='tblWeb_select_exfilter' aria-controls='example'></select></label></div>\").insertAfter($('#tblWeb_length'));\r\n"
					+ "\r\n" + "\r\n" + "            $(\"#tblMob_filter\").remove();\r\n"
					+ "            $(\"<div class='dataTables_filter' id='tblMob_exfilter'><label>   Filter <select id='tblMob_select_exfilter' aria-controls='example'></select></label></div>\").insertAfter($('#tblMob_length'));\r\n"
					+ "\r\n" + "\r\n" + "            AppendFilter('#tblWeb', '#tblWeb_select_exfilter')\r\n"
					+ "            AppendFilter('#tblMob', '#tblMob_select_exfilter')\r\n" + "\r\n" + "\r\n" + "\r\n"
					+ "            $('#tblWeb_select_exfilter').change(function () {\r\n"
					+ "                filterGlobal('#tblWeb', '#tblWeb_select_exfilter');\r\n" + "            });\r\n"
					+ "\r\n" + "            $('#tblMob_select_exfilter').change(function () {\r\n"
					+ "                filterGlobal('#tblMob', '#tblMob_select_exfilter');\r\n" + "            });\r\n"
					+ "\r\n" + "\r\n" + "            function filterGlobal(tablename, filtername) {\r\n"
					+ "                if ($(filtername).val() == \"Select\") {\r\n"
					+ "                    $(tablename).DataTable().search('').draw();\r\n"
					+ "                } else {\r\n" + "                    $(tablename).DataTable().search(\r\n"
					+ "\r\n" + "                        $(filtername).val()\r\n" + "\r\n"
					+ "                    ).draw();\r\n" + "\r\n" + "                }\r\n" + "\r\n"
					+ "            }\r\n" + "\r\n" + "            function AppendFilter(tablename, filtername) {\r\n"
					+ "\r\n" + "                var table = $(tablename).DataTable();\r\n" + "\r\n" + "\r\n"
					+ "                var data = table.rows().data();\r\n"
					+ "                $(filtername).append(`<option value=\"Select\">Select</option>`);\r\n"
					+ "                $.each(data, function (index, value) {\r\n" + "\r\n"
					+ "                    optionValue = value[5];\r\n" + "\r\n" + "\r\n"
					+ "                    var exists = $(filtername + \" option\")\r\n"
					+ "                        .filter(function (i, o) { return o.value === optionValue; })\r\n"
					+ "                        .length > 0;\r\n" + "\r\n" + "                    if (!exists) {\r\n"
					+ "                        $(filtername).append(`<option value=\"${optionValue}\">${optionValue}</option>`);\r\n"
					+ "                    }\r\n" + "\r\n" + "\r\n" + "\r\n" + "                });\r\n"
					+ "            }\r\n" + "\r\n" + "        });\r\n" + "\r\n"
					+ "        $(document).ready(function () {\r\n"
					+ "            $('#filterImageWeb').on('click', function () {\r\n"
					+ "                $('#tblWeb_select_exfilter').val(\"Select\");\r\n"
					+ "                $('#tblWeb_select_exfilter').trigger(\"change\");\r\n" + "            });\r\n"
					+ "        });\r\n" + "\r\n" + "        $(document).ready(function () {\r\n"
					+ "            $('#filterImageMob').on('click', function () {\r\n"
					+ "                $('#tblMob_select_exfilter').val(\"Select\");\r\n"
					+ "                $('#tblMob_select_exfilter').trigger(\"change\");\r\n" + "            });\r\n"
					+ "        });\r\n" + "\r\n" + "    </script>\r\n" + "</head>\r\n" + "\r\n" + "\r\n" + "</html>");
			// System.out.println("Buffer value :" + buffer);
			pw.close();
			System.out.println("report created successfully");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void getReportCommonValues(ConcurrentHashMap<String, String> HealingInformation,
			ConcurrentHashMap<String, Integer> HealingCount) {
		NoSuchElementFnlCount = HealingCount.get("NoSuchElement").intValue() + NoSuchElementFnlCount;
		networkErrorFnlCount = HealingCount.get("networkErrorCount").intValue() + networkErrorFnlCount;
		staleTimeoutFnlCount = HealingCount.get("staleTimeoutCount").intValue() + staleTimeoutFnlCount;
		othersFnlCount = HealingCount.get("othersCount").intValue() + othersFnlCount;
	}

	public void staleReportFormatter(ConcurrentHashMap<String, String> HealingInformation,
			ConcurrentHashMap<String, Integer> HealingCount, String ScriptTech) {
		if (ScriptTech.equalsIgnoreCase("web")) {
			webTechnology = true;
			try {
				getReportCommonValues(HealingInformation, HealingCount);

				buffer.append("<tr>" + "<td class=\"values_table\">" + HealingInformation.get("TestCase") + "</td>"
						+ "<td>" + HealingInformation.get("PageName") + "</td>" + "<td>"
						+ HealingInformation.get("ElementName") + "</td>" + "<td>"
						+ HealingInformation.get("stepActionName") + "</td>" + "<td>"
						+ HealingInformation.get("locatorType") + ": " + HealingInformation.get("locatorValue")
						+ "</td>" + "<td>" + HealingInformation.get("ExceptionType") + "</td>");
				healingstatus(HealingInformation, ScriptTech);

			}

			catch (Exception e) {
				e.printStackTrace();
				System.out.println("Failed at report formatting");
			}
		} else {
			try {
				mobileTechnology = true;
				getReportCommonValues(HealingInformation, HealingCount);

				mobileBuffer.append("<tr>" + "<td class=\"values_table\">" + HealingInformation.get("TestCase")
						+ "</td>" + "<td>" + HealingInformation.get("PageName") + "</td>" + "<td>"
						+ HealingInformation.get("ElementName") + "</td>" + "<td>"
						+ HealingInformation.get("stepActionName") + "</td>" + "<td>"
						+ HealingInformation.get("locatorType") + ": " + HealingInformation.get("locatorValue")
						+ "</td>" + "<td>" + HealingInformation.get("ExceptionType") + "</td>");
				healingstatus(HealingInformation, ScriptTech);

			}

			catch (Exception e) {
				e.printStackTrace();
				System.out.println("Failed at report formatting");
			}
		}

	}

	public void Reportformatter(ConcurrentHashMap<String, String> HealingInformation,
			ConcurrentHashMap<String, Integer> HealingCount, String ScriptTech) {
		if (ScriptTech.equalsIgnoreCase("web")) {
			try {
				// System.out.println("Healing Information in reporting : " +
				// HealingInformation);
				webTechnology = true;
				getReportCommonValues(HealingInformation, HealingCount);

				buffer.append("<tr>" + "<td class=\"values_table\">" + HealingInformation.get("TestCase") + "</td>"
						+ "<td>" + HealingInformation.get("PageName") + "</td>" + "<td>"
						+ HealingInformation.get("ElementName") + "</td>" + "<td>"
						+ HealingInformation.get("stepActionName") + "</td>" + "<td>" + "Locator-type:"
						+ HealingInformation.get("locatorType") + ": " + HealingInformation.get("locatorValue")
						+ "</td>" + "<td>" + HealingInformation.get("ExceptionType") + "</td>");
				healingstatus(HealingInformation, ScriptTech);

			}

			catch (Exception e) {
				e.printStackTrace();
				System.out.println("Failed at report formatting");
			}
		} else {
			try {
				mobileTechnology = true;
				getReportCommonValues(HealingInformation, HealingCount);

				mobileBuffer.append("<tr>" + "<td class=\"values_table\">" + HealingInformation.get("TestCase")
						+ "</td>" + "<td>" + HealingInformation.get("PageName") + "</td>" + "<td>"
						+ HealingInformation.get("ElementName") + "</td>" + "<td>" 
						+ HealingInformation.get("stepActionName") + "</td>" + "<td>" + "Locator-type:"
						+ HealingInformation.get("locatorType") + ": " + HealingInformation.get("locatorValue")
						+ "</td>" + "<td>" + HealingInformation.get("ExceptionType") + "</td>");

				healingstatus(HealingInformation, ScriptTech);

			}

			catch (Exception e) {
				e.printStackTrace();
				System.out.println("Failed at report formatting");
			}
		}

	}

	public void alertHealingFormatter(ConcurrentHashMap<String, String> HealingInformation,
			ConcurrentHashMap<String, Integer> HealingCount, String ScriptTech) {
		if (ScriptTech.equalsIgnoreCase("web")) {
			try {
				webTechnology = true;
				getReportCommonValues(HealingInformation, HealingCount);
				buffer.append("<tr>" + "<td class=\"values_table\">" + HealingInformation.get("TestCase") + "</td>"
						+ "<td>" + "AlertHandling" + "</td>" + "<td>" + HealingInformation.get("PageName") + "</td>"
						+ "<td>" + HealingInformation.get("ElementName") + "</td>" + "<td>" + "Alert" + "</td>" + "<td>"
						+ HealingInformation.get("ExceptionType") + "</td>");
				if (HealingInformation.get("HealStatus").equalsIgnoreCase("success")) {
					if (HealingInformation.get("HealedLocator").contains("dismiss")) {
						buffer.append(
								"<td style='text-align:center;cursor:pointer;' data-toggle='modal' data-target='#myModal'"
										+ "onclick='displayImage(\"" + "screenshots/"
										+ HealingInformation.get("ImageFileName") + ".png\",\""
										+ HealingInformation.get("ImageFileName") + "\");'>" + "<a>"
										+ "Alert dismissed as per user requirement" + "</a>" + "</td>");
					} else {
						buffer.append(
								"<td style='text-align:center;cursor:pointer;' data-toggle='modal' data-target='#myModal'"
										+ "onclick='displayImage(\"" + "screenshots/"
										+ HealingInformation.get("ImageFileName") + ".png\",\""
										+ HealingInformation.get("ImageFileName") + "\");'>" + "<a>"
										+ "Alert accepted as per user requirement" + "</a>" + "</td>");
					}
					buffer.append("<td style=\"color: #438207;\" align=\"center\"><b>Success</b></td></tr>");
					successCount++;
				} else {
					buffer.append("<td>" + "Error handling alert" + "</td>");
					buffer.append("<td style=\"color: #fd5a3e;\" align=\"center\"><b>Failed</b></td></>tr");
					failureCount++;
				}

			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("Failed at report formatting");
			}
		} else {
			try {
				mobileTechnology = true;
				getReportCommonValues(HealingInformation, HealingCount);
				mobileBuffer.append("<tr>" + "<td class=\"values_table\">" + HealingInformation.get("TestCase")
						+ "</td>" + "<td>" + "AlertHandling" + "</td>" + "<td>" + HealingInformation.get("PageName")
						+ "</td>" + "<td>" + HealingInformation.get("ElementName") + "</td>" + "<td>" + "Alert"
						+ "</td>" + "<td>" + HealingInformation.get("ExceptionType") + "</td>");
				if (HealingInformation.get("HealStatus").equalsIgnoreCase("success")) {
					if (HealingInformation.get("HealedLocator").contains("dismiss")) {
						mobileBuffer.append(
								"<td style='text-align:center;cursor:pointer;' data-toggle='modal' data-target='#myModal3'"
										+ "onclick='displayImage1(\"" + "screenshots/"
										+ HealingInformation.get("ImageFileName") + ".png\",\""
										+ HealingInformation.get("ImageFileName") + "\");'>" + "<a>"
										+ "Alert dismissed as per user requirement" + "</a>" + "</td>");
					} else {
						mobileBuffer.append(
								"<td style='text-align:center;cursor:pointer;' data-toggle='modal' data-target='#myModal3'"
										+ "onclick='displayImage1(\"" + "screenshots/"
										+ HealingInformation.get("ImageFileName") + ".png\",\""
										+ HealingInformation.get("ImageFileName") + "\");'>" + "<a>"
										+ "Alert accepted as per user requirement" + "</a>" + "</td>");
					}
					mobileBuffer.append("<td style=\"color: #438207;\" align=\"center\"><b>Success</b></td></tr>");
					successCount++;
				} else {
					mobileBuffer.append("<td>" + "Error handling alert" + "</td>");
					mobileBuffer.append("<td style=\"color: #fd5a3e;\" align=\"center\"><b>Failed</b></td></tr>");
					failureCount++;
				}

			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("Failed at report formatting");
			}
		}

	}

	public void successPercentCount() {
		successPercent = (int) (((float) successCount / ((float) successCount + (float) failureCount)) * 100);
		// System.out.println("Success percentage :" + successPercent);
	}

	public void healingstatus(ConcurrentHashMap<String, String> HealingInformation, String ScriptTech) {
		if (ScriptTech.equalsIgnoreCase("web")) {
			try {
				if (HealingInformation.get("HealStatus").equalsIgnoreCase("success")) {

					if (HealingInformation.get("ExceptionType").contains("TimeoutException")
							|| HealingInformation.get("ExceptionType").contains("StaleStateException")) {
						buffer.append(
								"<td style='text-align:center;cursor:pointer;' data-toggle='modal' data-target='#myModal'"
										+ "onclick='displayImage(\"" + "screenshots/"
										+ HealingInformation.get("ImageFileName") + ".png\",\""
										+ HealingInformation.get("ImageFileName") + "\");'>" + "<a>"
										+ "Wait time added to locator:" + HealingInformation.get("HealedLocator")
										+ "</a>" + "</td>");
					} else {
						buffer.append(
								"<td style='text-align:center;cursor:pointer;' data-toggle='modal' data-target='#myModal'"
										+ "onclick='displayImage(\"" + "screenshots/"
										+ HealingInformation.get("ImageFileName") + ".png\",\""
										+ HealingInformation.get("ImageFileName") + "\");'>" + "<a>"
										+ "Object identified for locator:" + HealingInformation.get("HealdEleTyp") + ":"
										+ HealingInformation.get("HealedLocator") + "</a>" + "</td>");
					}

					buffer.append("<td style=\"color: #438207;\" align=\"center\"><b>Success</b></td></tr>");
					/*
					 * buffer.append("<td align=\"center\"><a href=\"screenshots\\" +
					 * HealingInformation.get("ImageFileName") + ".png" +
					 * "\"target=blank><img src=\"img\\screenshot.png\" class=\"icon_table\" alt=\"Screenshot_icon\" ></img></td></tr>"
					 * );
					 */
					successCount++;
				} else {

					if (HealingInformation.containsKey("iframe")) {
						if (HealingInformation.get("iframe").equalsIgnoreCase("false")) {
							buffer.append("<td>" + "Control is not in iframe" + "</td>");
						} else {
							buffer.append("<td>" + "Self-Healing engine was not able to heal the element" + "</td>");
						}
					} else {
						buffer.append("<td>" + "Self-Healing engine was not able to heal the element" + "</td>");
					}
					buffer.append("<td style=\"color:#fd5a3e;\" align=\"center\"><b>Failed</b></td></tr>");
					failureCount++;
				}
			} catch (Exception e) {
				System.out.println("Issues while reporting :" + e);
			}
		} else {
			try {
				if (HealingInformation.get("HealStatus").equalsIgnoreCase("success")) {

					if (HealingInformation.get("ExceptionType").contains("TimeoutException")
							|| HealingInformation.get("ExceptionType").contains("StaleStateException")) {
						mobileBuffer.append(
								"<td style='text-align:center;cursor:pointer;' data-toggle='modal' data-target='#myModal3'"
										+ "onclick='displayImage1(\"" + "screenshots/"
										+ HealingInformation.get("ImageFileName") + ".png\",\""
										+ HealingInformation.get("ImageFileName") + "\");'>" + "<a>"
										+ "Wait time added to locator:" + HealingInformation.get("HealedLocator")
										+ "</a>" + "</td>");
					} else {
						mobileBuffer.append(
								"<td style='text-align:center;cursor:pointer;' data-toggle='modal' data-target='#myModal3'"
										+ "onclick='displayImage1(\"" + "screenshots/"
										+ HealingInformation.get("ImageFileName") + ".png\",\""
										+ HealingInformation.get("ImageFileName") + "\");'>" + "<a>"
										+ "Object identified for locator:" + HealingInformation.get("HealdEleTyp") + ":"
										+ HealingInformation.get("HealedLocator") + "</a>" + "</td>");
					}

					mobileBuffer.append("<td style=\"color: #438207;\" align=\"center\"><b>Success</b></td></tr>");
					/*
					 * buffer.append("<td align=\"center\"><a href=\"screenshots\\" +
					 * HealingInformation.get("ImageFileName") + ".png" +
					 * "\"target=blank><img src=\"img\\screenshot.png\" class=\"icon_table\" alt=\"Screenshot_icon\" ></img></td></tr>"
					 * );
					 */
					successCount++;
				} else {
					if (HealingInformation.containsKey("iframe")) {
						if (HealingInformation.get("iframe").equalsIgnoreCase("false")) {
							mobileBuffer.append("<td>" + "Control is not in iframe" + "</td>");
						} else {
							mobileBuffer.append("<td>" + "Self-Healing engine was not able to heal the element" + "</td>");
						}
					} else {
						buffer.append("<td>" + "Self-Healing engine was not able to heal the element" + "</td>");
					}
					mobileBuffer.append("<td style=\"color:#fd5a3e;\" align=\"center\"><b>Failed</b></td></tr>");
					failureCount++;
				}
			} catch (Exception e) {
				LOGGER.error("Error while reporting :" + e);
				System.out.println("Issues while reporting :" + e);
			}
		}

	}

	public void getSuggestion() {
		if ((NoSuchElementFnlCount > networkErrorFnlCount) && (NoSuchElementFnlCount > staleTimeoutFnlCount)
				&& (NoSuchElementFnlCount > othersFnlCount)) {
			// System.out.println("NoSuchElementFnlCount is having a higher value");
		} else if ((networkErrorFnlCount > NoSuchElementFnlCount) && (networkErrorFnlCount > staleTimeoutFnlCount)
				&& (networkErrorFnlCount > othersFnlCount)) {
			// System.out.println("networkErrorFnlCount is having a higher value");
		} else if ((staleTimeoutFnlCount > networkErrorFnlCount) && (staleTimeoutFnlCount > NoSuchElementFnlCount)
				&& (staleTimeoutFnlCount > othersFnlCount)) {
			// System.out.println("staleTimeoutFnlCount is having a higher value ");
		} else if ((othersFnlCount > networkErrorFnlCount) && (othersFnlCount > staleTimeoutFnlCount)
				&& (othersFnlCount > NoSuchElementFnlCount)) {
			// System.out.println("othersFnlCount is having a higher value ");
		}

	}

	public void getTrendData(StringBuffer nosuchcounts, StringBuffer stleCounts, StringBuffer nwCounts,
			StringBuffer otrsCounts) {
		trendBuffer.append("<script>\r\n" + "  new Chart(document.getElementById(\"line-chart\"), {\r\n"
				+ "type: 'line',\r\n" + "data: {\r\n" + "  labels: [\"Run1\",\"Run2\",\"Run3\",\"Run4\",\"Run5\"],\r\n"
				+ "  datasets: [{");
		trendBuffer.append(" data:");

		trendBuffer.append(nosuchcounts + ",");

		trendBuffer.append("label: \"NoSuchElement\",\r\n" + "      backgroundColor: '#3e95cd',\r\n"
				+ "      borderColor: \"#3e95cd\",\r\n" + "      fill: false\r\n" + "    }, { \r\n" + "      data: ");

		trendBuffer.append(stleCounts);

		trendBuffer.append(",\r\n" + "      label: \"Stale/TimeOut\",\r\n" + "      backgroundColor: '#8e5ea2',\r\n"
				+ "      borderColor: \"#8e5ea2\",\r\n" + "      fill: false\r\n" + "    }, { \r\n" + "      data: ");

		trendBuffer.append(nwCounts);

		trendBuffer.append(",\r\n" + "      label: \"Network Failures\",\r\n" + "      backgroundColor: '#3cba9f',\r\n"
				+ "      borderColor: \"#3cba9f\",\r\n" + "      fill: false\r\n" + "    }, { \r\n" + "      data: ");

		trendBuffer.append(otrsCounts);

		trendBuffer.append(",\r\n" + "      label: \"Others\",\r\n" + "      backgroundColor: '#e8c3b9',\r\n"
				+ "      borderColor: \"#e8c3b9\",\r\n" + "      fill: false\r\n" + "    }\r\n" + "  ]\r\n" + "},");

		trendBuffer.append("options: {\r\n" + "  title: {\r\n" + "    display: true,\r\n"
				+ "    text: 'Healing Trend (per run)',\r\n" + "    fontSize: 16,\r\n" + "  },\r\n" + "  legend: {\r\n"
				+ "      labels: {\r\n" + "        boxWidth: 20,\r\n" + "        padding: 10\r\n" + "    }\r\n"
				+ "        },\r\n" + "  scales: {\r\n" + "    yAxes: [{\r\n" + "	ticks:{\r\n" + "	stepSize:1,\r\n"
				+ "	beginAtZero:true,\r\n" + "	},\r\n" + "      scaleLabel: {\r\n" + "        display: true,\r\n"
				+ "        labelString: 'Count'\r\n" + "      }\r\n" + "    }],\r\n" + "    xAxes: [{\r\n"
				+ "      scaleLabel: {\r\n" + "        display: true,\r\n"
				+ "        labelString: 'Last 5 Executions',\r\n" + "        fontStyle: 'bold',\r\n" + "      }\r\n"
				+ "     \r\n" + "    }]\r\n" + "  }     \r\n" + "\r\n" + "}\r\n" + "\r\n" + "});\r\n" + "</script>");
	}

}
