package com.scripted.ArunConPageObject;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.scripted.desktop.DesktopHandlers;

public class SamplePageObject {
	WebDriver driver;
	
	@FindBy(id = "empname")
	WebElement empname;
	@FindBy(id = "email")
	WebElement email;
	@FindBy(id = "Address")
	WebElement address;
	@FindBy(id = "textbox1")
	WebElement phone;
	@FindBy(id = "pass")
	WebElement password;
	
	@FindBy(id = "radioButton1")
	WebElement rbYes;
	@FindBy(id = "radioButton2")
	WebElement rbNo;
	
	@FindBy(id = "button1")
	WebElement btnSubmit1;
	@FindBy(id = "button2")
	WebElement btnSubmit2;
	
	//Second Page
	
	@FindBy(id = "checkBox1")
	WebElement chkAnswer1;
	@FindBy(id = "checkBox2")
	WebElement chkAnswer2;
	@FindBy(id = "checkBox3")
	WebElement chkAnswer3;
	
	@FindBy(id = "DropDown")
	WebElement dropdown;
	
	@FindBy(id = "ListBox")
	WebElement ltQ2;
	
	@FindBy(id = "checkedListBox1")
	WebElement ltQ3;
	
	/*@FindBy(xpath = "//*[@AutomationId='listBox1']//*[@ControlType='ControlType.ListItem']")
	List<WebElement> ltQ4;*/
	
	@FindBy(id = "listBox1")
	WebElement ltQ4;
	
	@FindBy(id = "label1")
	WebElement thankyou;
	
	public SamplePageObject(WebDriver driver) {
		this.driver = driver;
	}
	
	public void enterDetails()
	{
		DesktopHandlers.enterText(empname, "Testing");
		DesktopHandlers.enterText(email, "Testing@ust-global.com");
		DesktopHandlers.enterText(address, "Testing, Test PO, 12345");
		DesktopHandlers.enterText(phone, "123456789");
		DesktopHandlers.enterText(password, "12345");
	}
	
	public void clickYes()
	{
		DesktopHandlers.click(rbYes);
	}
	public void clickNo()
	{
		DesktopHandlers.click(rbNo);
	}
	
	public void clickSubmit1()
	{
		DesktopHandlers.click(btnSubmit1);
	}
	
	public void selectCheckbox()
	{
		DesktopHandlers.click(chkAnswer1);
		DesktopHandlers.click(chkAnswer2);
		DesktopHandlers.click(chkAnswer3);
	}
	
	public void SelectValFromListBox()
	{
		DesktopHandlers.click(dropdown);
		DesktopHandlers.selectValListBox(ltQ2,"Yes");
	}
	
	public void MultiSelectVal()
	{
		DesktopHandlers.multiSelectbyIndex(ltQ3, "Option 1");
		DesktopHandlers.multiSelectbyIndex(ltQ3, "Option 2");
		
		DesktopHandlers.selectValListBox(ltQ4, "Answer 1");
	}
	
	public void clickSubmit2()
	{
		DesktopHandlers.click(btnSubmit2);
	}
	
	public void validateText()
	{
		DesktopHandlers.verifyText(thankyou, "Thank You");
	}
}
