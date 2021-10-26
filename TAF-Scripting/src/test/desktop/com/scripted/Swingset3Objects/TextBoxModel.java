package com.scripted.Swingset3Objects;


import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.log4testng.Logger;

import com.scripted.javaswing.JavaSwingHandlers;
import com.scripted.javaswing.JavaSwingwaitHelper;


public class TextBoxModel {

	private static final Logger LOGGER = Logger.getLogger(TabbedPaneModel.class);
	@FindBy(css = "text-field:nth(1)")
	WebElement txtData;
	@FindBy(css = "password-field:nth(1)")
	WebElement txtPwd;
	@FindBy(css = "password-field:nth(2)")
	WebElement txtConfirmPwd;

	private WebDriver driver;

	public TextBoxModel(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

	public void test() {
		try {
			WebElement txtPanel = driver.findElement(By.cssSelector("toggle-button[label*='TextField']"));
			if (null != txtPanel) {
				JavaSwingwaitHelper.waitForPresence(txtPanel);
				JavaSwingHandlers.click(txtPanel);
				JavaSwingHandlers.enterText(txtData, "user1");
				JavaSwingHandlers.enterText(txtPwd, "password123");
				JavaSwingHandlers.enterText(txtConfirmPwd, "password123");
			}
		} catch (Throwable e) {
			e.printStackTrace();
			LOGGER.error("Exception occured while testing the scrollbar:", e);
		}
	}
}
