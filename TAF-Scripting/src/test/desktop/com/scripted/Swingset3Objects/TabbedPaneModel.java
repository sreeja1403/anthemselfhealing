package com.scripted.Swingset3Objects;



import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.log4testng.Logger;

import com.scripted.javaswing.JavaSwingHandlers;
import com.scripted.javaswing.JavaSwingwaitHelper;

import net.sourceforge.marathon.javadriver.JavaDriver;

public class TabbedPaneModel {

	private static final Logger LOGGER = Logger.getLogger(TabbedPaneModel.class);
	@FindBy(css = "tabbed-pane")
	WebElement tabbedPane;

	@FindBy(css = "tabbed-pane::all-tabs[text='Camille']")
	WebElement firstTab;

	@FindBy(css = "tabbed-pane::all-tabs[text='Miranda']")
	WebElement secondTab;

	@FindBy(css = "tabbed-pane::all-tabs[text='Ewan']")
	WebElement thirdTab;

	@FindBy(css = "tabbed-pane::all-tabs[text='<html><font color=blue><bold><center>Bouncing Babies!</center></bold></font></html>']")
	WebElement fourthTab;

	private JavaDriver driver;

	public TabbedPaneModel(JavaDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

	public void test() {
		try {
			WebElement tabbedPane = driver.findElement(By.cssSelector("toggle-button[label*='JTabbedPane']"));
			if (null != tabbedPane) {
				JavaSwingwaitHelper.waitForPresence(tabbedPane);
				tabbedPane.click();
				testTextElements();
			}
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("Error occured while tesing the tabbed pane: ", e);
		}
	}

	public void testTextElements() throws Exception {
		System.out.println("Starting...");
		JavaSwingHandlers.click(firstTab);
		JavaSwingHandlers.click(secondTab);
		JavaSwingHandlers.click(thirdTab);
		JavaSwingHandlers.click(fourthTab);
	}

}
