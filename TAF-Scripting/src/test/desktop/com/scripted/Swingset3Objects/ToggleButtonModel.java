package com.scripted.Swingset3Objects;


import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.log4testng.Logger;

import com.scripted.javaswing.JavaSwingHandlers;
import com.scripted.javaswing.JavaSwingwaitHelper;


public class ToggleButtonModel {

	private static final Logger LOGGER = Logger.getLogger(TabbedPaneModel.class);
	@FindBy(css = "tabbed-pane::all-tabs[text='Radio Buttons']")
	WebElement rdTab;

	@FindBy(css = "tabbed-pane::all-tabs[text='Check Boxes']")
	WebElement ckTab;

	@FindBy(css = "radio-button[buttonText^='Radio One']")
	WebElement rdBtnOne;
	@FindBy(css = "radio-button[buttonText^='Radio Two']")
	WebElement rdBtnTwo;
	@FindBy(css = "radio-button[buttonText^='Radio Three']")
	WebElement rdBtnThree;

	@FindBy(css = "check-box[buttonText^='One']")
	WebElement ckBxOne;
	@FindBy(css = "check-box[buttonText^='Two']")
	WebElement ckBxTwo;
	@FindBy(css = "check-box[buttonText^='Three']")
	WebElement ckBxThree;

	@FindBy(css = "text-field")
	WebElement txtSearch;

	private WebDriver driver;

	public ToggleButtonModel(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

	public void test() {
		try {
			WebElement togglePane = driver.findElement(By.cssSelector("toggle-button[label*='ToggleButtons']"));
			if (null != togglePane) {
				JavaSwingwaitHelper.waitForPresence(togglePane);
				JavaSwingHandlers.click(togglePane);
				JavaSwingHandlers.click(rdTab);
				testRadioControlElements();
				JavaSwingHandlers.click(ckTab);
				testCehckBoxControlElements();
			}
		} catch (Throwable e) {
			e.printStackTrace();
			LOGGER.error("Exception occured while testing the scrollbar:", e);
		}
	}

	public void testRadioControlElements() {
		System.out.println("Click event is triggered...");
		JavaSwingHandlers.click(rdBtnOne);
		JavaSwingHandlers.click(rdBtnTwo);
		JavaSwingHandlers.click(rdBtnThree);
	}

	public void testCehckBoxControlElements() {
		System.out.println("Click event is triggered...");
		JavaSwingHandlers.click(ckBxOne);
		JavaSwingHandlers.click(ckBxTwo);
		JavaSwingHandlers.click(ckBxThree);
	}

}
