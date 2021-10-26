package com.scripted.runners;

import org.testng.ITestContext;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;

import com.scripted.configurations.SkriptmateConfigurations;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(features = "Features/Mobile", plugin = { "json:target/cucumber.json","io.qameta.allure.cucumber4jvm.AllureCucumber4Jvm",
		"html:target/cucumber.html" }, glue = { "com.scripted.mobstepdefs" }, monochrome = true, tags = { "@parallelTestNG" }) //@parallelTestNG

public class MobileTestRunner extends AbstractTestNGCucumberTests {
	@Override  //   *commented code for cucumber parallel execution"
	@DataProvider(parallel = true)
	public Object[][] scenarios() {
		return super.scenarios();
	}
	
	@BeforeClass
	public void setupClassName(ITestContext context) {
	    context.getCurrentXmlTest().getSuite().setDataProviderThreadCount(3);
	    context.getCurrentXmlTest().getSuite().setPreserveOrder(false);
	}
	@BeforeSuite
	public void setup() {
		SkriptmateConfigurations.beforeSuite();
	}

	@AfterSuite
	public void teardown() {
		SkriptmateConfigurations.afterSuite();
	}
}
