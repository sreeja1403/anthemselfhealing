/*package com.scripted.stepdefs;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import com.scripted.CRMPomObjects.CRMContactsPage;
import com.scripted.CRMPomObjects.CRMLoginPage;
import com.scripted.OrangeHRMPageObjectsMobile.OrangeHRMPage;
import com.scripted.api.RequestParams;
import com.scripted.api.RestAssuredWrapper;
import com.scripted.dataload.ExcelConnector;
import com.scripted.dataload.PropertyDriver;
import com.scripted.dataload.TestDataFactory;
import com.scripted.dataload.TestDataObject;
import com.scripted.generic.FileUtils;
import com.scripted.mainframe.MainframeUtil;
import com.scripted.mobile.MobileDriverSettings;
import com.scripted.reporting.AllureListener;
import com.scripted.web.BrowserDriver;

import io.appium.java_client.android.AndroidDriver;
import io.cucumber.core.api.Scenario;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.specification.RequestSpecification;

public class stepdefinitions {

	public static WebDriver driver = null;

	public static HashMap<Integer, String> headermap = new HashMap<Integer, String>();
	public static Map<String, String> fRow;
	public static Map<String, String> fAutoRow;
	public static AndroidDriver<WebElement> androidDriver;
	MainframeUtil mfUtil = new MainframeUtil();
	PropertyDriver propDriver = new PropertyDriver();
	CRMLoginPage crmLPage;
	CRMContactsPage crmCPage;
	OrangeHRMPage orangeHrmpage;
	TestDataFactory test = new TestDataFactory();
	String sheetnameOr = "TC004";
	String key = "";
	RequestParams Attwrapper = new RequestParams();
	RestAssuredWrapper raWrapper = new RestAssuredWrapper();

	@Before("@Web")
	public void setupExcelData() {
		driver = BrowserDriver.funcGetWebdriver();
		AllureListener.setDriver(driver);
		crmLPage = PageFactory.initElements(driver, CRMLoginPage.class);
		crmCPage = PageFactory.initElements(driver, CRMContactsPage.class);
		propDriver.setPropFilePath("src/main/resources/properties/Cogmento.properties");
		String filename = FileUtils.getCurrentDir() + PropertyDriver.readProp("excelName");
		String sheetname = "TC001";
		String key = "";
		ExcelConnector con = new ExcelConnector(filename, sheetname);
		TestDataFactory test = new TestDataFactory();
		TestDataObject obj = test.GetData(key, con);
		LinkedHashMap<String, Map<String, String>> getAllData = obj.getTableData();
		fRow = (getAllData.get("1"));

	}

	@Before("@AndroidWeb")
	public void beforeWeb() {
		androidDriver = MobileDriverSettings.funcGetWebAndroidDriver("androidWebbrowser");
		System.out.println("AndroidWeb started");
	}

	@After("@AndroidWeb")
	public void afterWeb() {
		MobileDriverSettings.closeDriver();
		System.out.println("AndroidWeb completed");
	}

	// ------------------------------Web------------------------------------------

	@Given("I have logged into {string} application")
	public void i_have_logged_into_crm_application(String strProjname) {
		if (strProjname.equalsIgnoreCase("Cogmento")) {
			BrowserDriver.launchWebURL("https://ui.freecrm.com");
			crmLPage.login(fRow.get("userName"), fRow.get("password"));
		} else if (strProjname.equalsIgnoreCase("OrangeHRM")) {
			MobileDriverSettings.launchURL("https://opensource-demo.orangehrmlive.com/index.php/auth/login");
			orangeHrmpage = new OrangeHRMPage(androidDriver);
			propDriver.setPropFilePath("src/main/resources/properties/configOrangehrm.properties");
			String filename = FileUtils.getCurrentDir() + PropertyDriver.readProp("excelName");
			ExcelConnector con = new ExcelConnector(filename, sheetnameOr);
			TestDataObject obj = test.GetData(key, con);
			LinkedHashMap<String, Map<String, String>> getAllData = obj.getTableData();
			fRow = (getAllData.get("1"));
			orangeHrmpage.login(fRow.get("UserName"), fRow.get("Password"));
		}
	}

	@And("I navigate to contact page")
	public void i_navigate_to_contact_page() {
		crmCPage.clickContacts();
	}

	@And("I enter personal details to a create contact")
	public void i_enter_personal_details_to_a_create_contact() throws Throwable {
		crmCPage.enterPersonalDetails(fRow.get("firstName"), fRow.get("lastName"), fRow.get("company"),
				fRow.get("timezone"));
		crmCPage.enterBOD(fRow.get("bdate"), fRow.get("bmonth"), fRow.get("byear"));
	}

	@When("I click on save button")
	public void i_click_on_save_button() {
		crmCPage.clickSaveBtn();
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@And("I should be able to delete contact added")
	public void i_should_be_able_to_delete_contact_added() throws Exception {
		crmCPage.deleteRecord(fRow.get("firstName") + " " + fRow.get("lastName"));
	}

	@And("I logout from the application")
	public void i_logout_from_the_crmapplication() {
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		crmCPage.logout();
	}

	@Then("I should successfully able to see the contact created")
	public void i_should_succeffully_able_to_see_the_contact_created() {
	}

	// ------------------------------Mobile------------------------------------------

	@And("I navigate to system users page")
	public void i_navigate_to_system_users_page() {
		orangeHrmpage.navigate_SystemUser();
	}

	@When("I click on {string} user link")
	public void i_click_on_something_user_link(String user) {
		orangeHrmpage.navigate_userPage(user);
	}

	@Then("I should successfully able to see user details")
	public void i_should_successfully_able_to_see_user_details() {
		orangeHrmpage.verifyUser();
	}

	@And("I logout from the orangeHrm application")
	public void i_logout_from_the_orangeHrm_application() {
		orangeHrmpage.logOut();

	}

	// ------------------------------API------------------------------------------

	@Given("I have sample Get API")
	public void i_have_sample_get_api() throws Throwable {

	}

	@When("I send a {string} Request with {string} properties")
	public void i_send_a_something_request_with_something_properties(String strMethod, String strPropFileName)
			throws Throwable {
		raWrapper.setAPIFileProName(strPropFileName + ".properties");
		RequestSpecification reqSpec = raWrapper.CreateRequest(Attwrapper);
		raWrapper.sendRequest(strMethod, reqSpec);
	}

	@Then("I should get response code {int}")
	public void i_should_get_reposnse_code_something(int strCode) throws Throwable {
		raWrapper.valResponseCode(strCode);
	}

	@And("the response should contain:")
	public void the_response_should_contain(DataTable respTable) throws Throwable {
		List<Map<String, String>> resplist = respTable.asMaps(String.class, String.class);
		for (int i = 0; i < resplist.size(); i++) {
			String jsonPath = resplist.get(i).get("JsonPath");
			String expVal = resplist.get(i).get("ExpectedVal");

			if (expVal.matches("-?(0|[1-9]\\d*)")) {
				int intExpVal = Integer.parseInt(expVal);
				raWrapper.valJsonResponseVal(jsonPath, intExpVal);
			} else {
				raWrapper.valJsonResponseVal(jsonPath, expVal);
			}

		}

	}

	@After("@PcloudyWeb")
	public void afterPcloudy() {
		androidDriver.quit();
		System.out.println("PcloudyWeb completed");
	}

	@After("@Web")
	public void afterScenario(Scenario scenario) {
		if (scenario.isFailed()) {
			TakesScreenshot scrShot = ((TakesScreenshot) driver);
			byte[] screenshot = scrShot.getScreenshotAs(OutputType.BYTES);
			scenario.embed(screenshot, "image/png");
		}
		BrowserDriver.closeBrowser();
	}

	
}
*/