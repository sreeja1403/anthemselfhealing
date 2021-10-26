package com.scripted.AutoPracPageobjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.scripted.web.WebHandlers;

public class APSignInpage {

	WebDriver driver;

	@FindBy(name = "emaill")
	public  WebElement input_email;

	@FindBy(id = "passwd")
	public  WebElement password;
	//passwd

	@FindBy(id = "SubmitLogin")
	public  WebElement SubmitButton;
	
/*	public APSignInpage(WebDriver webdriver) {
		this.driver=webdriver;
		PageFactory.initElements(driver,this);
	}*/

	public void login(String username, String pass) {
		WebHandlers.enterText(input_email, username);
		WebHandlers.enterText(password, pass);
		WebHandlers.click(SubmitButton);
	}
}
