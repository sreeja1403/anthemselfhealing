package com.scripted.testscripts;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.scripted.web.BrowserDriver;
import com.scripted.web.WebHandlers;

public class Trial {
	
	static WebDriver driver = null;
	
	
	public static void main(String[] args) {
		
		BrowserDriver.funcGetWebdriver();
		BrowserDriver.launchWebURL("https://login.salesforce.com/");
		driver = BrowserDriver.getDriver();
		WebElement ele1 = driver.findElement(By.xpath("//input[@type='email']"));
		WebElement ele2 = driver.findElement(By.xpath("//input[@id='password']"));
		WebElement ele3 = driver.findElement(By.xpath("//input[@id='']"));
		WebHandlers.enterText(ele1, "shamir.ahamed@ust-global.com");
		WebHandlers.enterText(ele2, "Ustglobal123");
		WebHandlers.click(ele3);
	}

}
