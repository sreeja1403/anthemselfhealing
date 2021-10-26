package com.scripted.runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(features = "Features/Web", plugin = { "json:target/cucumber.json",
		"io.qameta.allure.cucumber4jvm.AllureCucumber4Jvm" }, glue = {
				"com.scripted.deskstepdefs" }, monochrome = true, tags = { "@test" })
public class DeskTestNGRunner extends AbstractTestNGCucumberTests {
	/* Enable the below code only if you are not using SkpritmateRunner*/
	
	  /*@BeforeSuite
		public void setup() {
			try {
				InputStream log4j = new FileInputStream(
						"D:\\AL-Ways Platform\\CoreWorkspace\\SkriptMate\\TAF-Scripting\\src\\main\\resources\\logConf\\log4j.properties");
				PropertyConfigurator.configure(log4j);
				SkriptMateRunner.delFilesInFolder("allure-results");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		@AfterSuite
		public void teardown() {
			try {
				// JiraUtils.uploadResultsFromCucumberJson("target/cucumber.json");
				SkriptMateReport.customizeReport();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}*/

}