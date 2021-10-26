package com.scripted.CRMPomObjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.scripted.web.BrowserDriver;
import com.scripted.web.WebHandlers;
import io.cucumber.core.api.Scenario;

public class CRMLoginPage {
	WebDriver driver;
	@FindBy(name = "emailll")
	private WebElement loginUname;
	//emailll
	//*[@id="ui"]/div/div/form/div/div[1]/div/input
	@FindBy(name = "password")
	private WebElement loginPass;
	@FindBy(xpath = "//div[text()='Login']")
	private WebElement loginbtn;
	@FindBy(xpath = "//input[@name='button']")
	private WebElement acceptBtn;

/*	public CRMLoginPage(WebDriver driver) {
		this.driver = BrowserDriver.getDriver();
	}*/
	/*public CRMLoginPage(WebDriver webdriver) {
		this.driver=BrowserDriver.getDriver();
		PageFactory.initElements(driver,this);
	}*/
	
	// WebHandlers webHandlers = new WebHandlers();

	public void login(String user, String pass) {
		//WebHandlers.click(acceptBtn);
		WebHandlers.enterText(loginUname, user);
		WebHandlers.enterText(loginPass, pass);
		WebHandlers.click(loginbtn);
		
	}

}
