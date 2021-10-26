package com.scripted.CRMPomObjects;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.scripted.web.BrowserDriver;
import com.scripted.web.WebHandlers;
import com.scripted.web.WebWaitHelper;

import io.cucumber.core.api.Scenario;

public class CRMContactsPage {
	WebDriver driver;




	@FindBy(xpath = "//input[@name='last_name']")
	private WebElement Lname;
	@FindBy(xpath = "//input[@name='first_name']")
	private WebElement Fname;
	@FindBy(xpath = "//div[@name='company']//input[@class='search']")
	private WebElement Comname;
	@FindBy(xpath = "//input[@name='day']")
	private WebElement Bdate;
	@FindBy(xpath = "//span[text()='May']")
	private WebElement Bmonth;
	@FindBy(xpath = "//input[@name='year']")
	private WebElement Byear;
	@FindBy(xpath = "//i[@class='save icon']")
	private WebElement savebtn;
	@FindBy(xpath = "//button[contains(text(),'New')]")
	private WebElement newbtn;
	//*[@id="dashboard-toolbar"]/div[2]/div/a/button
	//*[@id="dashboard-toolbar"]/div[2]/div/a/button
	@FindBy(xpath = "//div[@name='timezone']/input")
	private WebElement tzone;
	@FindBy(xpath = "//div[@name='month']")
	private WebElement bmonth;
	@FindBy(xpath = "//i[@class='trash icon']")
	private WebElement delbtn;
	@FindBy(xpath = "//i[@class='remove icon']")
	private WebElement okbtn;
	@FindBy(xpath = "//div[@id='top-header-menu']//div[@class='ui buttons']/div")
	private WebElement settingicn;
	@FindBy(xpath = "//span[text()='Log Out']")
	private WebElement Logout;
	@FindBy(xpath = "//*[@id=\"-nav\"]/a[3]/span")
	private WebElement contacts;
	//*[@id=\"main-nav\"]/a[3]/span
	
	//*[@id="main-nav"]/a[3]/span
	
	@FindBy(xpath = ".//div[contains(@class,'main-content')]//table[@class='ui celled sortable striped table custom-grid table-scroll']")
	private WebElement table;
	@FindBy(xpath = "//div[@name='source']")
	private WebElement source;
	@FindBy(xpath = "//div[@name='category']")
	private WebElement category;
	@FindBy(xpath = "//div[@name='status']")
	private WebElement status;
	@FindBy(xpath = "//div[@class='ui toggle checkbox']//input[@name='do_not_call']")
	private WebElement radio1;
	@FindBy(xpath = "//div[@class='ui toggle checkbox']//input[@name='do_not_email']")
	private WebElement radio2;
	@FindBy(xpath = "//button[@class='ui button']")
	private WebElement dltbtn;

	public CRMContactsPage(WebDriver driver) {
		this.driver = BrowserDriver.getDriver();
	}
	WebHandlers webHandlers = new WebHandlers();
	public void enterPersonalDetails(String fname, String lname, String cname, String timezone)
			throws InterruptedException {
		webHandlers.click(newbtn);
		webHandlers.enterText(Fname, fname);
		webHandlers.enterText(Lname, lname);
		webHandlers.enterText(Comname, cname);
		Comname.sendKeys(Keys.ENTER);
		Thread.sleep(2000);
		webHandlers.enterText(tzone, timezone);
		// WebHandlers.click(radio1);
		// WebHandlers.click(radio2);

	}

	public void clickContacts() {
		webHandlers.click(contacts);
	}

	/*public void enterBOD(String date, String month, String year) {
		webHandlers.enterText(Bdate, date);
		webHandlers.divSpanListBox(bmonth, month);
		webHandlers.enterText(Byear, year);
	}

	public void clickSaveBtn() {
		webHandlers.click(savebtn);
	}

	public void deleteRecord(String strValue) throws InterruptedException {
		webHandlers.click(contacts);
		Thread.sleep(3000);
		webHandlers.TblCelChkboxClick(table, strValue, 1);
		TblCelEleClick(table, strValue, "Delete");
	}

	public void TblCelEleClick(WebElement objTable, String tableVal, String objectName) {

		try {
			By byEle = WebHandlers.webElementToBy(objTable);
			String strEle = byEle.toString();
			String[] splitStrEle = strEle.split(":");
			String tblXpath = splitStrEle[1];
			String index = webHandlers.getFirstIndexofVal(objTable, tableVal);
			String[] arrSplit = index.split(",");

			if (objectName.equalsIgnoreCase("Delete")) {
				driver.findElement(By.xpath(
						tblXpath + "//tr[" + arrSplit[0] + "]//td[7]//div//button[@class='ui icon inverted button']"))
						.click();
				WebWaitHelper.waitForElementPresence(dltbtn);
				webHandlers.click(dltbtn);
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public void logout() {
		webHandlers.click(settingicn);
		webHandlers.click(Logout);
	}

	public void headerVal() {
		System.out.println(webHandlers.getTblHeaderVal(table));
	}

	public void BodyVal() {
		System.out.println(webHandlers.getTblBodyVal(table));
	}

	public void tdValue() {
		System.out.println(webHandlers.getTblTdVal(table, 2, 1));
	}

	public void thValue() {
		System.out.println(webHandlers.getTblThVal(table, 0, 3));
	}

	public void inxValue() {
		System.out.println(webHandlers.getIndexofVal(table, "Shamir Ahamed"));
	}

	public void colmapHdrValue(String str) {
		System.out.println(webHandlers.getColMapByHdrVal(table, str));
	}

	public void rowmapHdrValue(int rowHeader) {
		System.out.println(webHandlers.getRowMapByIndxVal(table, rowHeader));
	}

	public void ColMapByIndxVal() {
		System.out.println(webHandlers.getColMapByIndxVal(table, 2));
	}

	public void RowMapByIndxVal() {
		System.out.println(webHandlers.getRowMapByIndxVal(table, 2));
	}

	public void tblLink(String str) {
		webHandlers.TblCelLinkClick(table, str);
	}

	public void selectListBoxes(String src, String cat, String stat) {
		webHandlers.divSpanListBox(source, src);
		webHandlers.divSpanListBox(category, cat);
		webHandlers.divSpanListBox(status, stat);
	}
*/
}
