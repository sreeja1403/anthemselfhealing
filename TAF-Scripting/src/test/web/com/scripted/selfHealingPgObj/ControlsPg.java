package com.scripted.selfHealingPgObj;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.scripted.web.WebHandlers;

public class ControlsPg {
	
	WebDriver driver;
	public static Logger LOGGER = Logger.getLogger(ControlsPg.class);
	
	@FindBy(xpath = "//button[@id='target']")
	private WebElement btnDelyd;
	
	@FindBy(xpath ="//div[contains(text(),'This is test div')]")
	private WebElement divDelyd;
	
	@FindBy(xpath="//button[@title='Mute Toggle']")
	private WebElement button;
	
	public ControlsPg(WebDriver driver) {
		this.driver = driver;
	}
	
	public void btnClick() {
		WebHandlers.click(btnDelyd);
	}
	public void divClick() {
		WebHandlers.click(divDelyd);
	}
	public void frmBtnClick() {
		WebHandlers.click(button);
	}
	

}
