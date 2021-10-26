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

public class ComboBoxModel {

	private static final Logger LOGGER = Logger.getLogger(TabbedPaneModel.class);

	@FindBy(css = "combo-box")
	WebElement cmbBox;

	@FindBy(css = "toggle-button[label*='JComboBox']")
	WebElement cmbPanel;
	
	
	private WebDriver driver;

	public ComboBoxModel(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

	public void test() {
		try {
			// WebElement cmbPanel =
			// driver.findElement(By.cssSelector("toggle-button[label*='JComboBox']"));
			if (null != cmbPanel) {
				JavaSwingwaitHelper.waitForPresence(cmbPanel);
				JavaSwingHandlers.click(cmbPanel);
				Thread.sleep(2000);
				List<WebElement> cmbLists = driver.findElements(By.cssSelector("combo-box"));
				WebElement cmbPreset = cmbLists.get(1);
				WebElement cmbHair = cmbLists.get(2);
				WebElement cmbEyesAndNose = cmbLists.get(3);
				WebElement cmbMouth = cmbLists.get(4);
				JavaSwingHandlers.clickCmbBoxByName(cmbPreset, "Jeff, Larry, Philip");
				JavaSwingHandlers.clickCmbBoxByName(cmbHair, "Jeff");
				JavaSwingHandlers.clickCmbBoxByName(cmbEyesAndNose, "Jeff");
				JavaSwingHandlers.clickCmbBoxByName(cmbMouth, "Jeff");
				//JavaSwingHandlers.clickCmbBoxByIndex(cmbMouth, 4);
			}
		} catch (Throwable e) {
			e.printStackTrace();
			LOGGER.error("Exception occured while testing the scrollbar:", e);
		}
	}

}
