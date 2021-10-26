package com.scripted.selfHealingPgObj;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import com.scripted.web.WebHandlers;

public class SAHomePage {

	WebDriver driver;
	
        
    @FindBy(id = "autocomplete")
    private WebElement input_txtSugBox;
    @FindBy(id = "checkBoxOption1")
    private WebElement input_chkOne;
    @FindBy(xpath = "//input[@value='radio1']")
    private WebElement input_rbtnOne;


	@FindBy(xpath = "//input[@value='radio1']")
	public static WebElement rbtnOne;

	@FindBy(id = "checkBoxOption1")
	public static WebElement chkOne;
	
	@FindBy(xpath = "//input[@id='autocomplete']")
	public static WebElement txtSugBox;
	
	public SAHomePage(WebDriver driver) {
		this.driver = driver;
	}
	
	public void clkRdBtn()
	{
		WebHandlers.click(input_rbtnOne);
	}
	
	public void entSug()
	{
		WebHandlers.enterText(input_txtSugBox, "India");
	}
	
	public void clkCkBox()
	{
		WebHandlers.click(input_chkOne);
	}
		
}
