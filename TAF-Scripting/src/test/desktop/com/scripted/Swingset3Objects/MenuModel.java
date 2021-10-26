package com.scripted.Swingset3Objects;



import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.log4testng.Logger;

import com.scripted.javaswing.JavaSwingHandlers;
import com.scripted.javaswing.JavaSwingwaitHelper;

import net.sourceforge.marathon.javadriver.JavaDriver;

public class MenuModel {
	private static final Logger LOGGER = Logger.getLogger(TabbedPaneModel.class);
	@FindBy(css = "button[text='Show JFrame...']")
	WebElement btnShowJFrame;
	@FindBy(css = "[type='JMenu'][actionCommand='File'][visible='true'][showing='true']")
	WebElement mnuFile;
	@FindBy(css = "[type='JMenuItem'][actionCommand='Open'][visible='true'][showing='true']")
	WebElement mnuOpen;
	@FindBy(css = "[type='JMenuItem'][actionCommand='Save'][visible='true'][showing='true']")
	WebElement mnuSave;
	@FindBy(css = "button[text='Toolbar Button']")
	WebElement btnToolbar;

	private JavaDriver driver;

	public MenuModel(JavaDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

	public void testFrameElements() throws Throwable {
		System.out.println("Click event is triggered..." + btnShowJFrame);
		try {
			WebElement menuModel = driver.findElement(By.cssSelector("toggle-button[label*='JFrame']"));
			JavaSwingwaitHelper.waitForPresence(menuModel);
			JavaSwingHandlers.click(menuModel);
			btnShowJFrame.click();
			driver.switchTo().window("Demo JFrame");
			JavaSwingHandlers.click(mnuFile);
			JavaSwingHandlers.click(mnuOpen);
			JavaSwingHandlers.click(mnuFile);
			JavaSwingHandlers.click(mnuSave);
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("Error occured while testing Input Dialog", e);
		}
	}
}
