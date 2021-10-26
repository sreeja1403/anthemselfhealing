package com.scripted.Swingset3Objects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.log4testng.Logger;

import com.scripted.javaswing.JavaSwingHandlers;
import com.scripted.javaswing.JavaSwingwaitHelper;

public class ButtonModel {

	private static final Logger LOGGER = Logger.getLogger(TabbedPaneModel.class);

	@FindBy(css = "button[text='Do it']")
	WebElement btnDo;
	@FindBy(css = "button[text='Find']")
	WebElement btnFind;
	@FindBy(css = "button[text='Go']")
	WebElement btnGo;
	@FindBy(css = "button[text='Connect']")
	WebElement btnConnect;

	private WebDriver driver;

	public ButtonModel(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

	public void test() {
		try {
			WebElement toggleButton = driver.findElement(By.cssSelector("toggle-button[label*='JButton']"));
			JavaSwingwaitHelper.waitForPresence(toggleButton);
			if (null != toggleButton) {
				JavaSwingHandlers.click(toggleButton);
				JavaSwingHandlers.click(btnDo);
				JavaSwingHandlers.click(btnFind);
				JavaSwingHandlers.click(btnGo);
				JavaSwingHandlers.click(btnConnect);
			}
		} catch (Throwable e) {
			e.printStackTrace();
			LOGGER.error("Exception occured while testing the scrollbar:", e);
		}
	}

}
