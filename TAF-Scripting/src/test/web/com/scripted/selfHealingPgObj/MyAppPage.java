package com.scripted.selfHealingPgObj;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.scripted.web.WebHandlers;

public class MyAppPage {
	WebDriver driver;
	public static Logger LOGGER = Logger.getLogger(MyAppPage.class);
	
	@FindBy(xpath = "//input[@value='radio1']")
	private WebElement rbtnRadio1;
	
	@FindBy(xpath = "//input[@value='radio2']")
	private WebElement rbtnRadio2;
	
	@FindBy(xpath = "//input[@value='radio3']")
	private WebElement rbtnRadio3;
	
	@FindBy(xpath = "//input[@id='autocomplete']")
	private WebElement txtSuggstn;
	
	@FindBy(xpath = "//select[@id='dropdown-class-example']")
	private WebElement drpEg;
	
	@FindBy(xpath = "//input[@value='option1']")
	private WebElement chkBx1;
	
	@FindBy(xpath = "//input[@value='option2']")
	private WebElement chkBx2;
	
	@FindBy(xpath = "//input[@value='option3']")
	private WebElement chkBx3;
	
	@FindBy(xpath = "//button[@id='openwindow']")
	private WebElement btnOpnWndw;
	
	@FindBy(xpath = "//a[@id='opentab']")
	private WebElement btnOpnTb;
	
	public MyAppPage(WebDriver driver) {
		this.driver = driver;
	}
	
	public void clkRadioBtn1() {
		WebHandlers.click(rbtnRadio1);
	}
	
	public void clkRadioBtn2() {
		WebHandlers.click(rbtnRadio2);
	}
	
	public void clkRadioBtn3() {
		WebHandlers.click(rbtnRadio3);
	}
	
	public void entSuggstn() {
		WebHandlers.enterText(txtSuggstn, "India");
	}
	
	public void sltChkBx1() {
		WebHandlers.click(chkBx1);
	}
	
	public void sltChkBx2() {
		WebHandlers.click(chkBx2);
	}
	
	public void sltChkBx3() {
		WebHandlers.click(chkBx3);
	}

}
