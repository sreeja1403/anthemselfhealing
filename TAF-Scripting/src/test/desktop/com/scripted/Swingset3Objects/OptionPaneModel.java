package com.scripted.Swingset3Objects;


import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.log4testng.Logger;

import com.scripted.javaswing.JavaSwingHandlers;
import com.scripted.javaswing.JavaSwingwaitHelper;

import net.sourceforge.marathon.javadriver.JavaDriver;

public class OptionPaneModel {

	private static final Logger LOGGER = Logger.getLogger(OptionPaneModel.class);
	@FindBy(css = "toggle-button[label*='JOptionPane']")
	WebElement optionPane;

	@FindBy(css = "button[text='Show Input Dialog']")
	WebElement inputDialog;

	@FindBy(css = "option-pane")
	WebElement inputBox;

	@FindBy(css = "button[text='Show Warning Dialog']")
	WebElement btnWarning;

	@FindBy(css = "button[text='Show Message Dialog']")
	WebElement btnMessage;

	@FindBy(css = "button[text='Show Component Dialog']")
	WebElement btnComponent;

	@FindBy(css = "button[text='Show Confirmation Dialog']")
	WebElement btnConfirmation;

	@FindBy(css = "button[text='OK']")
	WebElement btnOk;

	@FindBy(css = "button[text='Yes']")
	WebElement btnYes;

	private JavaDriver driver;

	public OptionPaneModel(JavaDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

	public void test() throws Exception{
		WebElement optionPane = driver.findElement(By.cssSelector("toggle-button[label*='JOptionPane']"));
		JavaSwingwaitHelper.waitForPresence(optionPane);
		JavaSwingHandlers.click(optionPane);
		inputDialogTest();
		warningDialogTest();
		messageDialogTest();
		componentDialogTest();
		confirmationDialogTest();
	}

	public void inputDialogTest() {
		try {
			JavaSwingHandlers.click(inputDialog);
			JavaSwingHandlers.enterText(inputBox, "Test Input");
			JavaSwingHandlers.click(btnOk);
			JavaSwingHandlers.click(btnOk);
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("Error occured while testing Input Dialog", e);
		}
	}

	public void warningDialogTest() {
		try {
			JavaSwingHandlers.click(btnWarning);
			JavaSwingHandlers.click(btnOk);
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("Error occured while testing Warning Dialog", e);
		}
	}

	public void messageDialogTest() {
		try {
			JavaSwingHandlers.click(btnMessage);
			JavaSwingHandlers.click(btnOk);
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("Error occured while testing Message Dialog", e);
		}
	}

	public void componentDialogTest() {
		try {
			JavaSwingHandlers.click(btnComponent);
			JavaSwingHandlers.click(btnYes);
			JavaSwingHandlers.click(btnOk);
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("Error occured while Component Input Dialog", e);
		}
	}

	public void confirmationDialogTest() {
		try {
			JavaSwingHandlers.click(btnConfirmation);
			JavaSwingHandlers.click(btnYes);
			JavaSwingHandlers.click(btnOk);
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("Error occured while testing Confirmation Dialog", e);
		}
	}
}
