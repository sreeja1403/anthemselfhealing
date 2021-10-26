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

public class DialogModel {

	private static final Logger LOGGER = Logger.getLogger(DialogModel.class);

	@FindBy(css = "button[text='Show JDialog...']")
	WebElement btnShowDialog;

	@FindBy(css = "label")
	WebElement lblText;

	private JavaDriver driver;

	public DialogModel(JavaDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

	public  void testDialogElements() throws Exception {
		WebElement dialogPane = driver.findElement(By.cssSelector("toggle-button[label*='JDialog']"));
		JavaSwingwaitHelper.waitForPresence(dialogPane);
		JavaSwingHandlers.click(dialogPane);
		dialogTest();
	}

	public  void dialogTest() {
		try {
			JavaSwingHandlers.click(btnShowDialog);
			JavaSwingHandlers.switchWindow("Demo JDialog");
			System.out.println("Text: " + lblText.getText());
			boolean isTextMatched = JavaSwingHandlers.verifyText(lblText, "I'm content.");
			System.out.println("Text Matched: " + isTextMatched);
			Assert.assertEquals(JavaSwingHandlers.verifyText(lblText, "I'm content."), true);
			JavaSwingHandlers.closeWindow("Demo JDialog");
			JavaSwingHandlers.switchWindow("SwingSet3 ");
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("Error occured while testing Input Dialog", e);
		}
	}
}
