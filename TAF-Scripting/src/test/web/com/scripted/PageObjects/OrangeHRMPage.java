package com.scripted.PageObjects;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.scripted.mobile.MobileHandlers;
import com.scripted.web.WebHandlers;

import io.appium.java_client.MobileDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;

public class OrangeHRMPage {

	@FindBy(id = "txtUsername")
	private WebElement loginUserName;

	@FindBy(id = "txtPassword")
	private WebElement loginPassword;

	@FindBy(id = "btnLogin")
	private WebElement btnLogin;

	@FindBy(id = "welcome")
	private WebElement welcomeAdmin;

	@FindBy(name = "admin")
	private WebElement b_admintab;
	//menu_admin_viewAdminModule

	@FindBy(id = "btnAdd")
	private WebElement addUser;

	@FindBy(id = "systemUser_userType")
	private WebElement userType;

	@FindBy(id = "systemUser_employeeName_empName")
	private WebElement employeeName;

	@FindBy(id = "systemUser_userName")
	private WebElement employee_userName;

	@FindBy(id = "systemUser_password")
	private WebElement employee_password;

	@FindBy(id = "systemUser_confirmPassword")
	private WebElement employee_Confirmpassword;

	@FindBy(id = "btnSave")
	private WebElement saveUser;

	@FindBy(id = "btnDelete")
	private WebElement deleteUser;

	@FindBy(id = "dialogDeleteBtn")
	private WebElement delete_OK;

	@FindBy(id = "resultTable")
	private WebElement usertable;

	@FindBy(xpath = ".//a[contains(@href,'saveSystemUser') and text()='Admin']")
	private WebElement admintab;

	@FindBy(xpath = "//*[@id='systemUser_userType']/option[1]")
	private WebElement userrole_Admin;

	@FindBy(id = "btnCancel")
	private WebElement btncancel;

	@FindBy(xpath = "//a[text()='Logout']")
	private WebElement linkLogout;

	//WebHandlers webHandlers = new WebHandlers();
	public void login(String userName, String passWord) {
		WebHandlers.enterText(loginUserName, userName);
		WebHandlers.enterText(loginPassword, passWord);
		WebHandlers.clickByJsExecutor(btnLogin);
		// click(btnLogin);
	}
	
	

	public void navigate_SystemUser() {
		WebHandlers.click(b_admintab);
	}

	public void adduser(String empType, String empName, String empUserName, String empPwd, String empConfirmPwd) {
		WebHandlers.click(addUser);
		WebHandlers.dropDownSetByVal(userType, empType);
		WebHandlers.enterText(employeeName, empName);
		WebHandlers.enterText(employee_userName, empUserName);
		WebHandlers.enterText(employee_password, empPwd);
		WebHandlers.enterText(employee_Confirmpassword, empConfirmPwd);
	}

	public void saveUser() {
		WebHandlers.clickByJsExecutor(saveUser);
	}

	public void checkUser(String userName) {
		WebHandlers.getIndexofVal(usertable, userName);
	}

	public void deleteUser(String userName) {
		// WebHandlers.TblCelChkboxClick(usertable, userName);
		WebHandlers.click(deleteUser);
		WebHandlers.click(delete_OK);
	}

	public void navigate_userPage(String user) {
		WebHandlers.clickByJsExecutor(admintab);
	}

	public void verifyUser() {
		WebHandlers.verifyText(userrole_Admin, "Admin");
		WebHandlers.click(btncancel);
	}

	public void logOut() {
		WebHandlers.click(welcomeAdmin);
		WebHandlers.click(linkLogout);
	}

	public void orangeHRMTableLogin_Android(String userName, String passWord) {
		WebHandlers.enterText(loginUserName, userName);
		WebHandlers.verifyText(loginUserName, "Hello");
		WebHandlers.clearText(loginUserName);
		System.out.println("text cleared");
		WebHandlers.existText(loginUserName);
		WebHandlers.enterText(loginUserName, userName);
		WebHandlers.enterText(loginPassword, passWord);
		WebHandlers.objExists(btnLogin);
		WebHandlers.objDisabled(btnLogin);
		// WebHandlers.verifyProperty(btnLogin,"name", "Submit");
		WebHandlers.clickByJsExecutor(btnLogin);
		WebHandlers.click(b_admintab);
	}
}
