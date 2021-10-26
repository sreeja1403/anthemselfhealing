package com.scripted.Swingset3Objects;




import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.log4testng.Logger;

import com.scripted.javaswing.JavaSwingHandlers;
import com.scripted.javaswing.JavaSwingwaitHelper;



public class ListModel {

	private static final Logger LOGGER = Logger.getLogger(TabbedPaneModel.class);
	@FindBy(css = "scroll-bar")
	List<WebElement> scrlBar;
	@FindBy(css = "list")
	WebElement list;

	@FindBy(css = "check-box[buttonText^='Micro']")
	WebElement ckBxMicro;
	@FindBy(css = "check-box[buttonText^='Tera']")
	WebElement ckBxTera;
	@FindBy(css = "check-box[buttonText^='Neo']")
	WebElement ckBxNeo;

	@FindBy(css = "check-box[buttonText^='Solutions']")
	WebElement ckBxSolutions;
	@FindBy(css = "check-box[buttonText^='Tech']")
	WebElement ckBxTech;
	@FindBy(css = "check-box[buttonText^='Soft']")
	WebElement ckBxSoft;

	private WebDriver driver;

	public ListModel(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

	public void test() {
		try {
			WebElement listPane = driver.findElement(By.cssSelector("toggle-button[label*='JList']"));
			if (null != listPane) {
				JavaSwingwaitHelper.waitForPresence(listPane);
				JavaSwingHandlers.click(listPane);
				JavaSwingHandlers.clickListByText(list, "YoYoSystems");
				JavaSwingHandlers.clickListByIndex(list, 10);
				JavaSwingHandlers.click(ckBxMicro);
				JavaSwingHandlers.click(ckBxTera);
				JavaSwingHandlers.click(ckBxNeo);
				JavaSwingHandlers.click(ckBxSolutions);
				JavaSwingHandlers.click(ckBxTech);
				JavaSwingHandlers.click(ckBxSoft);
			}
		} catch (Throwable e) {
			e.printStackTrace();
			LOGGER.error("Exception occured while testing the List:", e);
		}
	}

}
