package com.scripted.PageObjects;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import com.scripted.mobile.MobileHandlers;
import com.scripted.web.WebHandlers;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;

public class WebSampleSwitchToWindowPage {



	private AndroidDriver<WebElement> androidDriver = null;

	@FindBy(id = "win1")
	public static  WebElement NewTab;
	
	@FindBy(id = "win2")
	public static  WebElement NewWindow;
	

	@FindBy(xpath = "//*[@id=\"tsf\"]/div[2]/div[1]/div[1]/div/div[2]/input")
	public static  WebElement GoogleTxt;

	@FindBy(id = "enterText")
	public static  WebElement Message;

	@FindBy(id = "alertBox")
	public static  WebElement AlertClk;

	@FindBy(id = "promptBox")
	public static  WebElement PromptClk;

	@FindBy(id = "tables")
	public static  WebElement table;
	
	@FindBy(id = "confirmBox")
	public static  WebElement confirmBox;

	public void CheckSwitchToWindow() throws InterruptedException {
		WebHandlers.click(NewWindow);
		WebHandlers.switchToNewWindow();
		WebHandlers.enterText(Message, "Flower");
		
	}
	 
	public void NewWindow() throws InterruptedException
	{
		WebHandlers.enterText(GoogleTxt, "Flower");
	}
	public void OldWindow() throws InterruptedException
	{
		WebHandlers.enterText(Message, "Flower");
	}

	public void CheckAlert() throws InterruptedException {
		WebHandlers.click(PromptClk);
		//WebHandlers.enterText(Message, "Flower");
	}
	
	public void confirmAlert() throws InterruptedException {
		WebHandlers.click(confirmBox);
	}

}
