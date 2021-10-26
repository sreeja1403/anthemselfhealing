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


public class TreeModel {

	private static final Logger LOGGER = Logger.getLogger(TabbedPaneModel.class);
	@FindBy(css = "scroll-bar")
	List<WebElement> scrlBar;
	@FindBy(css = "tree")
	WebElement tree;

	@FindBy(css = "text-field")
	WebElement txtSearch;

	private WebDriver driver;

	public TreeModel(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

	public void test() {
		try {
			WebElement treePane = driver.findElement(By.cssSelector("toggle-button[label*='JTree']"));
			if (null != treePane) {
				JavaSwingwaitHelper.waitForPresence(treePane);
				JavaSwingHandlers.click(treePane);
				JavaSwingHandlers.expandTreeNodeByText(tree, "Classical");
				JavaSwingHandlers.expandTreeNodeByText(tree, "Beethoven");
				JavaSwingHandlers.expandTreeNodeByText(tree, "concertos");
				JavaSwingHandlers.clickTreebyText("No. 1 - C");
			}
		} catch (Throwable e) {
			e.printStackTrace();
			LOGGER.error("Exception occured while testing the scrollbar:", e);
		}
	}

}
