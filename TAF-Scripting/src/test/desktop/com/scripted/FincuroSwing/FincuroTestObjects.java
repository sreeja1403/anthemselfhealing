package com.scripted.FincuroSwing;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.scripted.javaswing.JavaSwingHandlers;

public class FincuroTestObjects {

	private WebDriver driver;
	@FindBy(css = "button[text='Do it']")
	WebElement btnDo;
	@FindBy(css = "button[text='Find']")
	WebElement btnFind;
	@FindBy(css = "button[text='Go']")
	WebElement btnGo;
	@FindBy(css = "button[text='Connect']")
	WebElement btnConnect;
	@FindBy(css = "text-field:nth(0)")
	WebElement userName;
	@FindBy(css = "password-field")
	WebElement password;
	@FindBy(css = "text-field:nth(2)")
	WebElement branch;
	@FindBy(css = "button")
	WebElement logOn;
	@FindBy(css = "tree")
	WebElement leftTree;
	@FindBy(id = "btnNew")
	WebElement newBtn;
	@FindBy(css = "combo-box:nth(0)")
	WebElement comboBox1;
	@FindBy(css = "combo-box:nth(1)")
	WebElement comboBox2;
	@FindBy(css = "text-field:nth(3)")
	WebElement txtHeadDesc;
	@FindBy(id = "btnCancel")
	WebElement btnCancel;
	@FindBy(id = "btnClose")
	WebElement btnClose;
	@FindBy(id = "btnSubHeadNew")
	WebElement btnSubHeadNew;
	@FindBy(id = "txtSubHeadDesc")
	WebElement txtSubHeadDesc;
	@FindBy(css = "menu")
	WebElement mainMenu;
	@FindBy(css = "menu0item")
	WebElement menuItem;
	
	public FincuroTestObjects(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}


	public void finCurotest() throws InterruptedException {
		//Thread.sleep(20000);
		JavaSwingHandlers.switchWindow("CBMS++ Login");
		JavaSwingHandlers.enterText(userName, "ftest4");
		JavaSwingHandlers.enterText(password, "100");
		JavaSwingHandlers.enterText(branch, "01");
		JavaSwingHandlers.click(logOn);
		Thread.sleep(5000);
		JavaSwingHandlers.expandTreeNodeByText(leftTree, "Acknowledgment");
		Thread.sleep(3000);
		List<WebElement> nodes = driver.findElements(By.cssSelector("tree::all-nodes[text*='Major/Sub Head']"));
		System.out.println("size---:" + nodes.size());
		nodes.get(0).click();
		Thread.sleep(2000);
		JavaSwingHandlers.click(newBtn);
		JavaSwingHandlers.clickCmbBoxByName(comboBox1, "Assets");
		JavaSwingHandlers.enterText(txtHeadDesc, "TestDesc");
		Thread.sleep(2000);
		JavaSwingHandlers.click(btnSubHeadNew);
		Thread.sleep(2000);
		JavaSwingHandlers.enterText(txtSubHeadDesc, "SubHead Desc");
		Thread.sleep(2000);
		JavaSwingHandlers.click(btnCancel);
		Thread.sleep(1000);
		JavaSwingHandlers.click(btnClose);
		JavaSwingHandlers.moveAndclick(mainMenu);
		JavaSwingHandlers.clickMenuItemByIndex(menuItem,3);
		Thread.sleep(1000);
		JavaSwingHandlers.clickBtnOnAlertWindow("Warning", "Yes");

}
}
