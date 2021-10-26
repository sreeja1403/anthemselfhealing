package com.scripted.runners;

import org.apache.log4j.BasicConfigurator;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;

import com.scripted.configurations.SkriptmateConfigurations;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(features = "Features/Mainframe", plugin = { "json:target/cucumber.json",
		"com.epam.reportportal.cucumber.ScenarioReporter",
		"html:target/cucumber.html" }, glue = { "com.scripted.mfstepdefs" }, monochrome = true, tags = { "@Mainframe" })
public class MainframeTestNGRunner extends AbstractTestNGCucumberTests {
	@BeforeSuite
	public void setup() {
		SkriptmateConfigurations.beforeSuite();
	}

	@BeforeClass(alwaysRun = true)
	public void jagacySetup() {
		System.setProperty("jagacy.properties.dir", "./src/main/resources/JagacyProp");
		System.setProperty("test.window", "true");
		BasicConfigurator.configure();
	}

	/*
	 * @AfterSuite public void teardown() { SkriptmateConfigurations.afterSuite(); }
	 */
}