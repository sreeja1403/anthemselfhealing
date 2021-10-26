package com.scripted.deskstepdefs;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.Listeners;

import com.scripted.ArunConPageObject.SamplePageObject;
import com.scripted.desktop.DesktopDriver;
import com.scripted.reporting.AllureListener;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

@Listeners({ AllureListener.class })
public class Stepdefinition {
	public static WebDriver driver;
	SamplePageObject SPage;

	@Given("^I have \"([^\"]*)\" application$")
	public void i_have_something_application(String strAppname1) throws Throwable {
		driver = DesktopDriver.funGetDriver("D:\\SampleDesktop\\SampleApplication.exe");
		SPage = PageFactory.initElements(driver, SamplePageObject.class);
	}

	@When("^I click on submit2 button$")
	public void i_click_on_submit2_button() throws Throwable {
		SPage.clickSubmit2();
	}

	@Then("^I should be able to see thank you message$")
	public void i_should_be_able_to_see_thank_you_message() throws Throwable {
		SPage.validateText();
		DesktopDriver.dDriver.close();
		DesktopDriver.killOpenDrivers();
	}

	@And("^I enter the personal details$")
	public void i_enter_the_personal_details() throws Throwable {
		SPage.enterDetails();
		SPage.clickYes();
	}

	@And("^I click on submit1 button$")
	public void i_click_on_submit1_button() throws Throwable {
		SPage.clickSubmit1();
	}

	@And("^I select the details on provided questions$")
	public void i_select_the_details_on_provided_questions() throws Throwable {
		SPage.selectCheckbox();
		SPage.SelectValFromListBox();
		SPage.MultiSelectVal();
	}
}
