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


public class TableModel {

	private static final Logger LOGGER = Logger.getLogger(TabbedPaneModel.class);
	@FindBy(css = "scroll-bar")
	List<WebElement> scrlBar;
	@FindBy(css = "table")
	WebElement table;

	@FindBy(css = "text-field")
	WebElement txtSearch;

	private WebDriver driver;

	public TableModel(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

	public void test() {
		try {
			WebElement tablePane = driver.findElement(By.cssSelector("toggle-button[label*='JTable']"));
			if (null != tablePane) {
				JavaSwingwaitHelper.waitForPresence(tablePane);
				JavaSwingHandlers.click(tablePane);
				JavaSwingHandlers.enterText(txtSearch, "Sunrise");
				int rowCount = JavaSwingHandlers.getRowCount(table);
				System.out.println("Search results: " + rowCount + " Records Found");
				JavaSwingHandlers.clickTableCellByIndex(table, 1, 1);
				JavaSwingHandlers.clickTableCellByColumn(table, "Movie Title", "Sunrise");
			}
		} catch (Throwable e) {
			e.printStackTrace();
			LOGGER.error("Exception occured while testing the scrollbar:", e);
		}
	}

}
