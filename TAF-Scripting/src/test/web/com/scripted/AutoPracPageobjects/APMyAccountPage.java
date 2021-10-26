package com.scripted.AutoPracPageobjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.scripted.web.WebHandlers;

public class APMyAccountPage {

	WebDriver driver;

	@FindBy(xpath = "//span[text()='Order history and details']")
	public WebElement orderHistoryandDetails;
	
	@FindBy(xpath = "//span[text()='My personal information']")
	public WebElement myPersonalInformation;
	
	//Added by AncyAS
	@FindBy(xpath="//*[@id=\"columns\"]/div[4]/a[2]")
	public WebElement myAccount;
	
	//*[@id="columns"]/div[1]/a[2]
	//span[text()='My personal information']
	
	//   #center_column > div > div:nth-child(1) > ul > li:nth-child(4) > a > span
	//   @FindBy(xpath = "//span[text()='My personal information']")

	public void clickMyPersonalInformation() {
		WebHandlers.click(myPersonalInformation);	
		//WebHandlers.click(myAccount);
	}

}
