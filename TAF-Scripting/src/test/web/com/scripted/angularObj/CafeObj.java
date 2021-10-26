package com.scripted.angularObj;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import com.paulhammant.ngwebdriver.*;
import com.scripted.web.WebHandlers;

public class CafeObj {
	WebDriver driver;

	@ByAngularModel.FindBy(model = "user.name")
	private WebElement uname;

	// self Xpath
	@FindBy(xpath = "//*[@type='password']//self::input")
	private WebElement pass;

	@ByAngularButtonText.FindBy(buttonText = "Login")
	private WebElement login;

	// child Xpath
	@FindBy(xpath = "//*[@id='sub-nav']/child::li[1]")
	private WebElement create;

	// preceding Xpath
	@FindBy(xpath = "//*[@type='submit']//preceding::input[4]")
	private WebElement fname;

	// following Xpath
	@FindBy(xpath = "//*[@type='text']//following::input[1]")
	private WebElement lname;

	@ByAngularModel.FindBy(model = "selectedEmployee.startDate")
	private WebElement sdate;

	@ByAngularModel.FindBy(model = "selectedEmployee.email")
	private WebElement email;

	// parent Xpath
	@FindBy(xpath = "//*[@class='formFooter']//parent::div[1]")
	private WebElement addbtn;

	@ByAngularButtonText.FindBy(buttonText = "Add")
	private WebElement add;

	@FindBy(id = "bDelete")
	private WebElement del;

	@ByAngularButtonText.FindBy(buttonText = "OK")
	private WebElement ok;

	public CafeObj(WebDriver driver) {
		this.driver = driver;
	}

	public void login() throws Exception {
		Thread.sleep(5000);
		WebHandlers.enterText(uname, "Luke");
		WebHandlers.enterText(pass, "Skywalker");
		WebHandlers.click(login);
	}

	public void create() throws Exception {
		WebHandlers.click(create);
		Thread.sleep(5000);
		WebHandlers.enterText(fname, "Abhilash");
		WebHandlers.enterText(lname, "Giridharan");
		WebHandlers.enterText(sdate, "2019-10-23");
		WebHandlers.enterText(email, "Abhilash.Giridharan@ust-global.com");
		WebHandlers.click(addbtn);
	}

	public void delete() throws Exception {
		Thread.sleep(5000);
		driver.findElement(By.xpath("//*[@id='employee-list']/li[contains(text(),'Abhilash Giridharan')]")).click();
		WebHandlers.click(del);
		Thread.sleep(2000);
		driver.switchTo().alert().accept();
	}

}
