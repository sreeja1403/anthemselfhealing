package com.scripted.selfHealingPgObj;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.scripted.web.WebHandlers;

public class AlertPg {
	
	WebDriver driver;
	public static Logger LOGGER = Logger.getLogger(AlertPg.class);
	
	@FindBy(xpath = "//button[@id='button1']")
	private WebElement btnNwBrwsWndw;
	
	//Title - Selenium Framework|
	
	@FindBy(xpath = "//button[contains(text(),'New Message Window')]")
	private WebElement btnNwMsgWndw;
	
	//Title - Untitled; Message - This message window is only for viewing purposes
	
	@FindBy(xpath = "//button[@id='alert']")
	private WebElement btnJvScrAlrt;
	
	//Content - www.seleniumframework.com says
	
	@FindBy(xpath = "//button[@id='timingAlert']")
	private WebElement btnJvScrTmngAlrt;
	
	//Content - www.seleniumframework.com says
	
	public AlertPg(WebDriver driver) {
		this.driver = driver;
	}
	
	public void genJvScptAlerts() {
		WebHandlers.click(btnJvScrAlrt);
	}
	
	public void genJvScptTmngAlerts() {
		WebHandlers.click(btnJvScrTmngAlrt);
	}
	
	public void genBrwsrMsgWndw() {
		WebHandlers.click(btnNwBrwsWndw);
	}
	
	public void genMsgWndw() {
		WebHandlers.click(btnNwMsgWndw);
	}

}
