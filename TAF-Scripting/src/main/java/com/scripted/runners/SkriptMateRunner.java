package com.scripted.runners;

import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

import com.scripted.configurations.SkriptmateConfigurations;

public class SkriptMateRunner {

	@BeforeSuite
	public static void before() {
		SkriptmateConfigurations.beforeSuite();
	}

	@AfterSuite
	public static void after() {
		SkriptmateConfigurations.afterSuite();
	}
}
