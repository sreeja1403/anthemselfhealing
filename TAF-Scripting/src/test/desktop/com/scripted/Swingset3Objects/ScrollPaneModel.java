package com.scripted.Swingset3Objects;


import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.log4testng.Logger;

import com.scripted.javaswing.JavaSwingHandlers;
import com.scripted.javaswing.JavaSwingwaitHelper;



public class ScrollPaneModel {

	private static final Logger LOGGER = Logger.getLogger(TabbedPaneModel.class);
	@FindBy(css = "scroll-bar")
	List<WebElement> scrlBar;

	private WebDriver driver;

	public ScrollPaneModel(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

	public void test() {
		try {
			WebElement scrollPane = driver.findElement(By.cssSelector("toggle-button[label*='JScrollPane']"));
			if (null != scrollPane) {
				JavaSwingwaitHelper.waitForPresence(scrollPane);
				JavaSwingHandlers.click(scrollPane);
				JavaSwingHandlers.moveHorizontalScrollBarByOffset(scrlBar, 450);
				JavaSwingHandlers.moveVerticalScrollBarByOffset(scrlBar, 40);
			}
		} catch (Throwable e) {
			e.printStackTrace();
			LOGGER.error("Exception occured while testing the scrollbar:", e);
		}
	}

}
