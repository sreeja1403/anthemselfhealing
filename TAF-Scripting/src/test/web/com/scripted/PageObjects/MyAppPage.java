package com.scripted.PageObjects;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.scripted.web.WebHandlers;

public class MyAppPage {
	WebDriver driver;
	public static Logger LOGGER = Logger.getLogger(MyAppPage.class);
	
	@FindBy(name = "radio11")
	private WebElement input_radioBtn1;
	//input[@value='radio1']
	
	@FindBy(xpath = "//input[@value='radio2']")
	public WebElement rbtnRadio2;
	
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
	
	//iframe 
	@FindBy(xpath="/html/body/app-root/div/header/div[2]/div/div/div[2]/nav/div[2]/ul/li[2]/a")
	private WebElement courses;
	
	@FindBy(xpath="/html/body/app-root/div/header/div[1]/div/div/div[2]/div[2]/a")
	private WebElement iframeLogin;
	
    @FindBy(name = "framelink")
    private WebElement a_linkFrameContact;
    // /html[1]/body[1]/app-root[1]/div[1]/header[1]/div[2]/div[1]/div[1]/div[2]/nav[1]/div[2]/ul[1]/li[9]/a[1]
    
    @FindBy(id="courses-iframe")
    private WebElement iframeTab;
    
    ///////Alerts
    @FindBy(id="alertbtn")
    private WebElement alertBtn;
    
    @FindBy(id="confirmbtn")
    private WebElement cnrmBtn;
    
    @FindBy(id="name")
    private WebElement nameTxt;
    
	
	public MyAppPage(WebDriver driver) {
		this.driver = driver;
	}
	
	public void checkAlert()
	{
	//	WebHandlers.click(alertBtn);
		WebHandlers.enterText(nameTxt, "Flower");
	}
	
	public void clkRadioBtn1() {
		WebHandlers.click(input_radioBtn1);
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
	
	public void checkIframe()
	{

		WebHandlers.switchToFrame(iframeTab);
		WebHandlers.click(a_linkFrameContact);
		
	}
	public void checkIframewithoutswitch()
	{

		//WebHandlers.switchToFrame(iframeTab);
		WebHandlers.click(a_linkFrameContact);
		
	}
	public void dropdown()
	{
		WebHandlers.dropDownSetByVal(drpEg, "Option3");
	}

}
