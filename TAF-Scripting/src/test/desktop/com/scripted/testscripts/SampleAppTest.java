package com.scripted.testscripts;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import com.scripted.ArunConPageObject.SamplePageObject;
import com.scripted.desktop.DesktopDriver;

public class SampleAppTest {
	public static WebDriver driver = DesktopDriver.funGetDriver("D:\\SampleDesktop\\SampleApplication.exe");
	public static void main(String[] args) throws Exception {

		SamplePageObject SPage = PageFactory.initElements(driver, SamplePageObject.class);
		SPage.enterDetails();
		SPage.clickYes();
		SPage.clickSubmit1();
		
		SPage.selectCheckbox();
		SPage.SelectValFromListBox();
		SPage.MultiSelectVal();
		
		SPage.clickSubmit2();
		SPage.validateText();
		DesktopDriver.dDriver.close();
		DesktopDriver.killOpenDrivers();
	}
}
