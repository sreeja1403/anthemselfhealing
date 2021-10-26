package com.scripted.JavaSwingTestscripts;

import org.apache.log4j.BasicConfigurator;
import org.testng.log4testng.Logger;

import com.scripted.FincuroSwing.FincuroTestObjects;
import com.scripted.javaswing.SwingJavaDriver;

import net.sourceforge.marathon.javadriver.JavaDriver;

public class FincuroTestScript {

	private static final Logger LOGGER = Logger.getLogger(SwingSetAppTest.class);
	private static JavaDriver driver ;

	public static void main(String[] args) {
		try {
			driver = SwingJavaDriver.initJDriver("D:\\Fincurotest.bat");
			FincuroTestObjects fincuroTestScript = new FincuroTestObjects(driver);
			fincuroTestScript.finCurotest();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			//SwingJavaDriver.quitDriver();
		}
	}
}
