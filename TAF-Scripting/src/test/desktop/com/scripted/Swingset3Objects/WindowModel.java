package com.scripted.Swingset3Objects;


import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import org.testng.log4testng.Logger;

import com.scripted.javaswing.JavaSwingHandlers;
import com.scripted.javaswing.JavaSwingwaitHelper;

import net.sourceforge.marathon.javadriver.JavaDriver;

public class WindowModel {

	private static final Logger LOGGER = Logger.getLogger(WindowModel.class);

	@FindBy(css = "button[text='Show JWindow...']")
	WebElement btnShowWindow;

	@FindBy(css = "window")
	WebElement emptyWindow;

	@FindBy(css = "label")
	WebElement lblText;

	private JavaDriver driver;

	public WindowModel(JavaDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

	public void testWindowElements() throws Exception {
		WebElement windowPane = driver.findElement(By.cssSelector("toggle-button[label*='JWindow']"));
		JavaSwingwaitHelper.waitForPresence(windowPane);
		JavaSwingHandlers.click(windowPane);
		dialogTest();
	}

	public void dialogTest() {
		try {
			JavaSwingHandlers.click(btnShowWindow);
			System.out.println("label Text: " + lblText.getText());
			boolean isTextMatched = JavaSwingHandlers.verifyText(lblText, "I have no system border..");
			Assert.assertEquals(JavaSwingHandlers.verifyText(lblText, "I have no system border.."), false);
			System.out.println("Text Matched: " + isTextMatched);
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("Error occured while testing Input Dialog", e);
		}
	}
}
