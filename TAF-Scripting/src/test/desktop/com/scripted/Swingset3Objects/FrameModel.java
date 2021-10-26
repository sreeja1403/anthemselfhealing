package com.scripted.Swingset3Objects;


import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.log4testng.Logger;

import com.scripted.javaswing.JavaSwingHandlers;
import com.scripted.javaswing.JavaSwingwaitHelper;


public class FrameModel {

	private static final Logger LOGGER = Logger.getLogger(FrameModel.class);
	@FindBy(css = "button[text='Show JFrame...']")
	WebElement btnShowJFrame;
	@FindBy(css = "check-box[buttonText^='Frame busy']")
	WebElement btnFrameBusy;
	@FindBy(css = "[type='JMenu'][actionCommand='File'][visible='true'][showing='true']")
	WebElement mnuFile;
	@FindBy(css = "[type='JMenuItem'][actionCommand='Open'][visible='true'][showing='true']")
	WebElement mnuOpen;
	@FindBy(css = "[type='JMenuItem'][actionCommand='Save'][visible='true'][showing='true']")
	WebElement mnuSave;
	@FindBy(css = "button[text='Toolbar Button']")
	WebElement btnToolbar;

	private WebDriver driver;

	public FrameModel(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

	public void testFrameElements() {
		try {
			WebElement frame = driver.findElement(By.cssSelector("toggle-button[label*='JFrame']"));
			JavaSwingwaitHelper.waitForPresence(frame);
			JavaSwingHandlers.click(frame);
			JavaSwingHandlers.click(btnShowJFrame);
			System.out.println("Click event is triggered..." + btnShowJFrame);
			JavaSwingHandlers.click(btnShowJFrame);
			JavaSwingHandlers.switchWindow("Demo JFrame");
			JavaSwingHandlers.click(mnuFile);
			mnuOpen.click();
			JavaSwingHandlers.click(mnuFile);
			mnuSave.click();
			JavaSwingHandlers.click(btnToolbar);
			JavaSwingHandlers.closeWindow("Demo JFrame");
			JavaSwingHandlers.switchWindow("SwingSet3");
			JavaSwingHandlers.click(btnFrameBusy);
			JavaSwingHandlers.closeWindow("Demo JFrame");
			JavaSwingHandlers.switchWindow("SwingSet3");
			JavaSwingHandlers.click(btnFrameBusy);
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("Error occured while testing the frame: ", e);
		}
	}
}
