package com.scripted.AutoPracPageobjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.scripted.web.WebHandlers;

public class NewPage {

	WebDriver driver;

	  @FindBy(xpath = "//form[@id='get-input']/div[1]/input[1]")
	    private WebElement input_Pleaseenteryour;
	    @FindBy(xpath = "//div[@id='navbar-brand-centered']/ul[1]/li[1]/ul[1]/li[1]/a[1]")
	    private WebElement a_SimpleFormDemo;
	    @FindBy(xpath = "//div[@id='navbar-brand-centered']/ul[1]/li[1]/a[1]")
	    private WebElement a_InputForms;
	
	public void click()
	{
		WebHandlers.click(a_InputForms);
		WebHandlers.click(a_SimpleFormDemo);
	}
	
	public void sendkeys()
	{
		WebHandlers.enterText(input_Pleaseenteryour,"sharath");
		
	}
		
}
