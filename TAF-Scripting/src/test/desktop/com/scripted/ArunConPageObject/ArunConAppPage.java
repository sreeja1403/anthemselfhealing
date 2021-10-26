package com.scripted.ArunConPageObject;

import java.awt.Robot;
import java.awt.event.KeyEvent;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.scripted.desktop.DesktopHandlers;

public class ArunConAppPage {
	WebDriver driver;
	
	@FindBy(id = "DropDown")
	WebElement RegDrpDwn;
	@FindBy(xpath = "//*[@AutomationId='TestLevel_Combo']")
	WebElement levelDrpDwn;
	
	@FindBy(name = "APAC")
	WebElement Apac;
	@FindBy(name = "Native Application")
	WebElement Nativeapp;
	@FindBy(name = "Mobile Website")
	WebElement Mobileapp;
	@FindBy(name = "Android")
	WebElement Android;
	@FindBy(name = "Windows")
	WebElement Windows;
	@FindBy(name = "IOS")
	WebElement Ios;
	@FindBy(name = "BlackBerry")
	WebElement Bberry;
	@FindBy(name = "Find Coverage")
	WebElement Fcoveragebtn;
	@FindBy(xpath = "/table[@id='dataGridView2']")
	WebElement str1;

	@FindBy(id = "Region_Combo")
	WebElement sltRegion;
	@FindBy(id = "TestLevel_Combo")
	WebElement sltTestCoverage;
	
	public ArunConAppPage(WebDriver driver) {
		this.driver = driver;
	}

	public void selectRegion() {
		DesktopHandlers.click(RegDrpDwn);
		DesktopHandlers.selectValListBox(sltRegion, "APAC");
	}

	public void selectTestCoverage() throws Exception {
		Robot robot = new Robot();
		robot.keyPress(KeyEvent.VK_TAB);
		robot.keyPress(KeyEvent.VK_M);
		robot.keyPress(KeyEvent.VK_A);
		robot.keyPress(KeyEvent.VK_X);
		robot.keyPress(KeyEvent.VK_DOWN);
	}

	public void selectAppType() {
		DesktopHandlers.click(Nativeapp);
	}

	public void selectOS() {
		DesktopHandlers.click(Android);
		DesktopHandlers.click(Windows);
		DesktopHandlers.click(Ios);
		DesktopHandlers.click(Bberry);
		DesktopHandlers.click(Fcoveragebtn);
	}

	public boolean verifyData(String str) {
		// boolean f=DesktopHandlers.verifyText(str1, str);
		return true;

	}

	public boolean SelectedRadio() {
		boolean flag = DesktopHandlers.radioBtnIsSelected(Nativeapp);

		return flag;
	}

	public boolean NotSelectedRadio() {
		boolean flag = DesktopHandlers.radioBtnIsNotSelected(Mobileapp);

		return flag;

	}

	public void selectTestCoverageLevel() {
		DesktopHandlers.click(levelDrpDwn);
		DesktopHandlers.selectValListBox(sltTestCoverage, "Maximum");
	}

}
