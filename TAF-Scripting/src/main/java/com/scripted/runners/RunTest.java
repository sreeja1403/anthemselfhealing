/*package com.scripted.runners;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import com.scripted.configurations.SkriptmateConfigurations;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

@RunWith(Cucumber.class)
@CucumberOptions(features = "Features/Web", plugin = { "json:target/cucumber.json",
		"io.qameta.allure.cucumber4jvm.AllureCucumber4Jvm",
		"html:target/cucumber.html" }, glue = { "com.scripted.webstepdefs" }, monochrome = true, tags = { "@parallelTestNG" })
public class RunTest{
	@BeforeClass
	public static void setup() {
		SkriptmateConfigurations.beforeSuite();
	}
	@AfterClass
	public static void teardown() {
		SkriptmateConfigurations.afterSuite();
	}
}*/