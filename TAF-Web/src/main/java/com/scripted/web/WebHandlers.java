package com.scripted.web;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.UnhandledAlertException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.winium.DesktopOptions;
import org.openqa.selenium.winium.WiniumDriver;
import org.openqa.selenium.winium.WiniumDriverService;

import com.paulhammant.ngwebdriver.ByAngular;
import com.scripted.generic.FileUtils;
import com.scripted.selfhealing.SMWebHealer;

import junit.framework.Assert;

public class WebHandlers {
	// public Actions action = new Actions(BrowserDriver.getDriver());
	public static Logger LOGGER = Logger.getLogger(WebHandlers.class);
	public WiniumDriver desktopDriver = null;
	public WebDriver driver;
	public String technology = "web";
	static WebElement HealdLctr = null;
	

	static SMWebHealer smhealer=new SMWebHealer();

	public WebElement getElement(WebDriver driver, By locator) {
		WebElement element = driver.findElement(locator);
		return element;
	}

	public static By webElementToBy(WebElement webEle) {
		try {
			String webEleString = webEle.toString();
			if (webEleString.contains("unknown locator")) {

				Object proxyOrigin = FieldUtils.readField(webEle, "h", true);
				Object loc = FieldUtils.readField(proxyOrigin, "locator", true);

				Object findBy = FieldUtils.readField(loc, "by", true);

				String flag = findBy.toString();
				String[] data = null;
				data = flag.split("\\(");

				String locator = data[0];
				String term = data[1].replace(")", "");
				switch (locator) {
				case "model":
					return ByAngular.model(term);
				case "searchText":
					return ByAngular.buttonText(term);
				case "binding":
					return ByAngular.binding(term);
				case "repeater":
					return ByAngular.repeater(term);
				case "exactBinding":
					return ByAngular.exactBinding(term);
				case "partialButtonText":
					return ByAngular.partialButtonText(term);
				case "options":
					return ByAngular.options(term);
				}

			} else {
				String flag = webEle.toString();
				flag = flag.substring(1, flag.length() - 1);
				String[] data = null;
				if (flag.contains("DefaultElementLocator"))
					data = flag.split("By.")[1].split(": ");
				else
					data = flag.split(" -> ")[1].split(": ");

				String locator = data[0];
				String term = data[1];

				switch (locator) {
				case "xpath":
					return By.xpath(term);
				case "css selector":
					return By.cssSelector(term);
				case "id":
					return By.id(term);
				case "tag name":
					return By.tagName(term);
				case "name":
					return By.name(term);
				case "link text":
					return By.linkText(term);
				case "class name":
					return By.className(term);
				}
			}
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return (By) webEle;
	}

	public static void initHeal(Exception e, WebElement locator) {
		LOGGER.info("Object healing in progress...");
		HealdLctr = smhealer.initiateHealing(e, locator, BrowserDriver.getlthDriver());
		if (HealdLctr == null || smhealer.isShflag()==false) {
			Assert.fail("Object healing  unsuccesful,please refer self healing report.");
		}
		LOGGER.info("Object healing completed. Healed locator : " + HealdLctr);
	}

	public static void enterText(WebElement locator, String value) {
		try {
			// WebWaitHelper.waitForClear(locator);
	//		throw new StaleElementReferenceException("");
			locator.clear();
//			// locator.sendKeys(new String[] { value });	
//			//throw new TimeoutException();
//			
		locator.sendKeys(value);
			LOGGER.info("Text entered successfully" + locator);
		
		} catch (Exception e) {
		
			if (smhealer.isShflag() || smhealer.isImpactflag()) {
				initHeal(e, locator);
				enterText(HealdLctr, value);
			} else {
				LOGGER.error("Error while enterting the text for the locator: " + locator + "Exception :" + e);
				 Assert.fail("Error while entering the text for the locator: " + locator +
				 "Exception :" + e);
			}

		}
	}

	public static void click(WebElement locator) {
		try {
		//	 WebWaitHelper.waitForElement(locator);
			locator.click();
			LOGGER.info("Click action completed successfully for the locator: " + locator);
		} catch (Exception e) {
			if (smhealer.isShflag() || smhealer.isImpactflag()) {
				initHeal(e, locator);
				click(HealdLctr);
			} else {
				LOGGER.error("Error while performing the click action for the locator: " + locator + "Exception :" + e);
				Assert.fail("Error while performing the click action for the locator: " + locator + "Exception :" + e);
			}

		}
	}

	public static void clickByJsExecutor(WebElement locator) {
		try {
			JavascriptExecutor executor = (JavascriptExecutor) BrowserDriver.getDriver();
			executor.executeScript("arguments[0].click();", locator);
		} catch (Exception e) {
			if (smhealer.isShflag() || smhealer.isImpactflag()) {
				initHeal(e, locator);
				clickByJsExecutor(HealdLctr);
			} else {
				LOGGER.error("Error while enterting the text for the locator: " + locator + "Exception :" + e);
				 Assert.fail("Error while entering the text for the locator: " + locator +
				 "Exception :" + e);
			}

		}
	}

	public static boolean verifyText(WebElement locator, String strVText) {
		boolean vflag = true;
		String actualText = "";
		try {
			if (locator.getTagName().equals("select")) {
				Select seleObj = new Select(locator);
				actualText = seleObj.getFirstSelectedOption().getText().trim();
				vflag = compareText(actualText, strVText);
			} else {
				WebWaitHelper.waitForElementPresence(locator);
				actualText = locator.getText();
				if (actualText == null || actualText.isEmpty()) {
					actualText = locator.getAttribute("innerText");
					if (actualText == null || actualText.isEmpty()) {
						actualText = locator.getAttribute("value");
						vflag = compareText(actualText, strVText);
					} else {
						vflag = compareText(actualText, strVText);
					}

				} else {
					vflag = compareText(actualText, strVText);
				}
			}
			LOGGER.info("Text verified successfully for the locator: " + locator);
		} catch (Exception e) {
			if (smhealer.isShflag() || smhealer.isImpactflag()) {
				initHeal(e, locator);
				verifyText(HealdLctr, strVText);
			} else {
				LOGGER.error("Error while verifying the text for the locator: " + locator + "Exception :" + e);
				 Assert.fail("Error while entering the text for the locator: " + locator +
				 "Exception :" + e);
			}

			

		}
		return vflag;
	}

	public static boolean compareText(String strActualText, String strCompText) {
		boolean compFlag = false;
		try {
			if (strActualText.equals(strCompText)) {
				compFlag = true;
			} else {
				compFlag = false;
				Assert.fail(strActualText + " doesnot match with " + strCompText);
			}
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("Error occurred while doing the compareText action " + "Exception :" + e);
			Assert.fail("Error occurred while doing the compareText action " + "Exception :" + e);
		}
		return compFlag;
	}

	public static boolean existText(WebElement locator) {
		boolean flag = true;
		try {
			WebWaitHelper.waitForElement(locator);
			if (locator.getAttribute("value").isEmpty()) {
				flag = false;
				Assert.fail("Text does not exists");
			} else {
				flag = true;
				LOGGER.info("Text exits " + locator);
			}
		} catch (Exception e) {
			if (smhealer.isShflag() || smhealer.isImpactflag()) {
				initHeal(e, locator);
				existText(HealdLctr);
			} else {
				e.printStackTrace();
				LOGGER.error("Error occurred while trying to check whether the text exists " + "Exception :" + e);
				Assert.fail("Error occurred while trying to check whether the text exists " + "Exception :" + e);
			}
		}
		return flag;
	}

	public static void clearText(WebElement locator) {
		try {
			WebWaitHelper.waitForElement(locator);
			locator.clear();
			LOGGER.info("Text cleared successfully ");
		} catch (Exception e) {
			if (smhealer.isShflag() || smhealer.isImpactflag()) {
				initHeal(e, locator);
				clearText(HealdLctr);
			} else {
				e.printStackTrace();
				LOGGER.error("Error occurred while trying to clear text " + "Exception :" + e);
				Assert.fail("Error occurred while trying to clear text " + "Exception :" + e);
			}

		}
	}

	public static boolean objDisabled(WebElement locator) {
		boolean eFlag = false;
		try {
			if (!locator.isEnabled()) {
				eFlag = true;
				LOGGER.info("Object is disabled");
			} else {
				eFlag = false;
				LOGGER.info("Object is enabled");
			}
			LOGGER.info("objDisabled check completed successfully ");
		} catch (Exception e) {
			if (smhealer.isShflag() || smhealer.isImpactflag()) {
				initHeal(e, locator);
				objDisabled(HealdLctr);
			} else {
				e.printStackTrace();
				LOGGER.error("Error occurred while checking whether the object is disabled " + "Exception :" + e);
				Assert.fail("Error occurred while checking whether the object is disabled " + "Exception :" + e);
			}
		}
		return eFlag;
	}

	public static String fetchPropertyVal(WebElement locator, String property) {
		String propValue = "";
		try {
			propValue = locator.getAttribute(property).toString();
			LOGGER.info(
					"Property value fetched  successfully for the locator: " + locator + "prop value :" + propValue);
			return propValue;
		} catch (Exception e) {
			if (smhealer.isShflag() || smhealer.isImpactflag()) {
				initHeal(e, locator);
				fetchPropertyVal(HealdLctr, property);
			} else {
				e.printStackTrace();
				LOGGER.error("Error  while fetching  the property value " + "Exception :" + e);
				Assert.fail("Error  while fetching  the property value " + "Exception :" + e);
			}
		}
		return propValue;
	}

	public static void verifyProperty(WebElement locator, String property, String expected) {
		String actual = "";
		try {
			actual = fetchPropertyVal(locator, property);
			LOGGER.info("Property value fetched successfully for the locator: " + locator);
		} catch (Exception e) {
			if (smhealer.isShflag() || smhealer.isImpactflag()) {
				initHeal(e, locator);
				verifyProperty(HealdLctr, property, expected);
			} else {
				e.printStackTrace();
				LOGGER.error("Error while trying to verify the property " + "Exception :" + e);
				Assert.fail("Error while trying to verify the property " + "Exception :" + e);
			}
		}
		if (!expected.equals(actual)) {
			LOGGER.info("property does not  matches" + "locator value: " + locator + " Expected value :" + expected
					+ "Actual value :" + actual);
			Assert.fail("Property does not matches");
		} else {
			LOGGER.info("Property matches" + "locator value: " + locator + " Expected value :" + expected
					+ "Actual value :" + actual);
		}
	}

	public static void doubleClick(WebElement locator) {
		try {
			WebWaitHelper.waitForElement(locator);
			new Actions(BrowserDriver.getDriver()).doubleClick(locator).perform();
			LOGGER.info("Double click performed successfully");
		} catch (Exception e) {
			if (smhealer.isShflag() || smhealer.isImpactflag()) {
				initHeal(e, locator);
				doubleClick(HealdLctr);
			} else {
				e.printStackTrace();
				LOGGER.error("Error while performing double click " + "Exception: " + e);
				Assert.fail("Error while performing double click " + "Exception: " + e);
			}
		}

	}

	public static void rightClick(WebElement locator) {
		try {
			WebWaitHelper.waitForElement(locator);
			new Actions(BrowserDriver.getDriver()).contextClick(locator).perform();
			LOGGER.info("Right click performed successfully");
		} catch (Exception e) {
			if (smhealer.isShflag() || smhealer.isImpactflag()) {
				initHeal(e, locator);
				rightClick(HealdLctr);
			} else {
				e.printStackTrace();
				LOGGER.error("Error while performing right click " + "Exception: " + e);
				Assert.fail("Error while performing right click " + "Exception: " + e);
			}
		}
	}

	public static void objExists(WebElement locator) {
		try {
			By byEle = WebHandlers.webElementToBy(locator);
			WebWaitHelper.waitForPresence(byEle, WebWaitHelper.getElementTimeout(), locator);
			LOGGER.info("objExists check completed successfully, Object exists ");
		} catch (Exception e) {
			if (smhealer.isShflag() || smhealer.isImpactflag()) {
				initHeal(e, locator);
				objExists(HealdLctr);
			} else {
				e.printStackTrace();
				LOGGER.error("Error occurred while checking whether the object exists " + "Exception :" + e);
				Assert.fail("Error occurred while checking whether the object exists " + "Exception :" + e);
			}
		}
	}

	public static boolean chkboxIsChecked(WebElement locator) {
		WebWaitHelper.waitForElement(locator);
		boolean eFlag = false;
		try {
			if (locator.isSelected()) {
				eFlag = true;
			} else {
				eFlag = false;
				Assert.fail("Checkbox is not checked");
			}
		} catch (Exception e) {
			if (smhealer.isShflag() || smhealer.isImpactflag()) {
				initHeal(e, locator);
				chkboxIsChecked(HealdLctr);
			} else {
				e.printStackTrace();
				LOGGER.error("Error while performing the chkboxIsChecked action for the locator: " + locator
						+ "Exception :" + e);
				Assert.fail("Error while performing the chkboxIsChecked action for the locator: " + locator
						+ "Exception :" + e);
			}
		}
		return eFlag;

	}

	public static boolean radiobtnNotChecked(WebElement locator) {
		WebWaitHelper.waitForElementPresence(locator);
		boolean eFlag = false;
		try {
			if (!locator.isSelected()) {
				eFlag = true;
			} else {
				eFlag = false;
				Assert.fail("Radio button is checked");
			}
		} catch (Exception e) {
			if (smhealer.isShflag() || smhealer.isImpactflag()) {
				initHeal(e, locator);
				radiobtnNotChecked(HealdLctr);
			} else {
				e.printStackTrace();
				LOGGER.error("Error while performing radiobtnNotChecked action " + "Exception :" + e);
				Assert.fail("Error while performing radiobtnNotChecked action " + "Exception :" + e);
			}
		}
		return eFlag;

	}

	public static boolean radioBtnIsSelected(WebElement locator) {
		WebWaitHelper.waitForElementPresence(locator);

		boolean eFlag = false;
		try {
			if (locator.isSelected()) {
				eFlag = true;
			} else {
				eFlag = false;
				Assert.fail("Radio button is not selected");
			}
		} catch (Exception e) {
			if (smhealer.isShflag() || smhealer.isImpactflag()) {
				initHeal(e, locator);
				radioBtnIsSelected(HealdLctr);
			} else {
				e.printStackTrace();
				LOGGER.error("Error while performing radioBtnIsSelected action " + "Exception :" + e);
				Assert.fail("Error while performing radioBtnIsSelected action " + "Exception :" + e);
			}
		}
		return eFlag;

	}

	public static boolean radioBtnIsNotSelected(WebElement locator) {
		WebWaitHelper.waitForElementPresence(locator);
		boolean eFlag = false;
		try {
			if (!locator.isSelected()) {
				eFlag = true;
			} else {
				eFlag = false;
				Assert.fail("Radio button is  selected");
			}
		} catch (Exception e) {
			if (smhealer.isShflag() || smhealer.isImpactflag()) {
				initHeal(e, locator);
				radioBtnIsNotSelected(HealdLctr);
			} else {
				e.printStackTrace();
				LOGGER.error("Error while performing radioBtnIsNotSelected action " + "Exception :" + e);
				Assert.fail("Error while performing radioBtnIsNotSelected action " + "Exception :" + e);
			}
		}
		return eFlag;

	}

	public static HashMap<Integer, String> sltCtrlReadAllVal(WebElement locator) {
		HashMap<Integer, String> comboValuesMap = new HashMap<Integer, String>();
		try {
			WebWaitHelper.waitForElement(locator);
			comboValuesMap = new HashMap<Integer, String>();
			Select dropdown = new Select(locator);
			List<WebElement> dd = dropdown.getOptions();
			for (int j = 0; j < dd.size(); j++) {
				comboValuesMap.put(j, dd.get(j).getText());
			}
			LOGGER.info("The values are " + comboValuesMap);
			return comboValuesMap;
		} catch (Exception e) {
			if (smhealer.isShflag() || smhealer.isImpactflag()) {
				initHeal(e, locator);
				sltCtrlReadAllVal(HealdLctr);
			} else {
				e.printStackTrace();
				LOGGER.error("Error while performing the sltCtrlReadAllVal action for the locator: " + locator
						+ "Exception :" + e);
				Assert.fail("Error while performing the sltCtrlReadAllVal action for the locator: " + locator
						+ "Exception :" + e);
			}
		}
		return comboValuesMap;
	}

	public static String dropDownGetCurrentSelection(WebElement locator) {
		String cmbSelectedValue = "";
		try {
			WebWaitHelper.waitForElement(locator);
			Select seleObj = new Select(locator);
			cmbSelectedValue = seleObj.getFirstSelectedOption().getText().trim();
		} catch (Exception e) {
			if (smhealer.isShflag() || smhealer.isImpactflag()) {
				initHeal(e, locator);
				dropDownGetCurrentSelection(HealdLctr);
			} else {
				e.printStackTrace();
				LOGGER.error("Error while trying to get current selection of dropdown for the locator: " + locator
						+ "Exception :" + e);
				Assert.fail("Error while trying to get current selection of dropdown for the locator " + locator
						+ "Exception :" + e);
			}
		}
		return cmbSelectedValue;

	}

	public static void dropDownSelectByIndex(WebElement locator, int index) {
		try {
			WebWaitHelper.waitForElement(locator);
			String cmbSelectedValue = "";
			Select dropdown = new Select(locator);
			List<WebElement> cmbOptions = dropdown.getOptions();
			cmbSelectedValue = cmbOptions.get(index).getText();
			LOGGER.info("Selected value :" + cmbSelectedValue);
		} catch (Exception e) {
			if (smhealer.isShflag() || smhealer.isImpactflag()) {
				initHeal(e, locator);
				dropDownSelectByIndex(HealdLctr, index);
			} else {
				e.printStackTrace();
				LOGGER.error("Error while trying to select index from drop down  the locator: " + locator
						+ "Exception :" + e);
				Assert.fail("Error while trying to select index from drop down  the locator: " + locator + "Exception :"
						+ e);

			}
		}
	}

	public static boolean dropDownSetByVal(WebElement locator, String value) {
		boolean flag = false;
		try {
			WebWaitHelper.waitForElement(locator);
			Select dropdown = new Select(locator);
			List<WebElement> cmbOptions = dropdown.getOptions();
			for (int i = 0; i <= cmbOptions.size(); i++) {
				if (value.equalsIgnoreCase(cmbOptions.get(i).getText())) {
					dropdown.selectByIndex(i);
					flag = true;
					break;
				}
			}
			LOGGER.info(" The action dropDownSetByVal completed successfully ");
			return flag;
		} catch (Exception e) {
			if (smhealer.isShflag() || smhealer.isImpactflag()) {
				initHeal(e, locator);
				dropDownSetByVal(HealdLctr, value);
			} else {
				e.printStackTrace();
				LOGGER.error("Error while trying to set  drop down by value for the locator: " + locator + "Exception :"
						+ e);
				Assert.fail(
						"Error while trying to set drop down by value for the locator:" + locator + "Exception :" + e);
			}
		}
		return flag;
	}

	public static boolean dropDownSetByIndex(WebElement locator, int index) {
		boolean flag = false;
		try {
			WebWaitHelper.waitForElement(locator);
			Select dropdown = new Select(locator);
			List<WebElement> cmbOptions = dropdown.getOptions();
			for (int i = 0; i <= cmbOptions.size(); i++) {
				if (!cmbOptions.get(i).getText().isEmpty()) {
					dropdown.selectByIndex(index);
					flag = true;
					break;
				}
			}
			LOGGER.info(" The action dropDownSetByIndex completed successfully ");
			return flag;
		} catch (Exception e) {
			if (smhealer.isShflag() || smhealer.isImpactflag()) {
				initHeal(e, locator);
				dropDownSetByIndex(HealdLctr, index);
			} else {
				e.printStackTrace();
				LOGGER.error(
						"Error while trying to set drop down by index for the locator: " + locator + "Exception :" + e);
				Assert.fail(
						"Error while trying to set drop down by index for the locator:" + locator + "Exception :" + e);
			}
		}
		return flag;
	}

	// Code added to handle windows - Needs to be tested
	public static String[] getWindowHandles() {
		String[] handles = new String[BrowserDriver.getDriver().getWindowHandles().size()];
		try {
			BrowserDriver.getDriver().getWindowHandles().toArray(handles);
		} catch (Exception e) {
			LOGGER.error("Error occurred while getting window handles" + "Exception" + e);
		}

		return handles;
	}

	public static void switchToDefaultWindow() throws InterruptedException {
		Thread.sleep(1000);
		try {
			String[] handles = getWindowHandles();
			int i = handles.length - 1;
			BrowserDriver.getDriver().switchTo().window(getWindowHandles()[0]);
			LOGGER.info("switched back to default window successfully");
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("Error occurred while trying to switch back to default window" + "Exception :" + e);
			Assert.fail("Error occurred while trying to switch back to default window" + "Exception :" + e);
		}
	}

	public static void switchToNewWindow() throws InterruptedException { // Need to test
		Thread.sleep(1000);
		try {
			String[] handles = getWindowHandles();
			int i = handles.length - 1;
			BrowserDriver.getDriver().switchTo().window(getWindowHandles()[i]);
			LOGGER.info("switched to new window successfully");
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("Error while trying to switch to the new window " + "Exception :" + e);
			Assert.fail("Error while trying to switch to the new window " + "Exception :" + e);
		}
	}

	public static void switchToFrame(WebElement locator) {
		String actual = "";
		try {
			//actual = String.valueOf(locator.isDisplayed());
			BrowserDriver.getDriver().switchTo().frame(locator);
		/*	if (actual == null)
				throw new WebAutomationException(locator, "Switch to Frame", "Frame not Found");
			actual = actual.trim().toLowerCase();*/
		} catch (WebAutomationException e) {
			if (smhealer.isShflag() || smhealer.isImpactflag()) {
				initHeal(e, locator);
				switchToFrame(HealdLctr);
			} else {
				e.printStackTrace();
				LOGGER.error("Tried switching to next frame, not found " + locator + "Exception: " + e);
				Assert.fail("Tried switching to next frame, not found " + locator + "Exception: " + e);
				// throw new WebAutomationException(locator, "Switch to Frame",
				// "Frame not Found");
			}
		}
	}

	public static void swithBackFromFrame() {
		try {
			BrowserDriver.getDriver().switchTo().defaultContent();
			// getFrame().swithBackFromFrame();
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("Error while trying to perform switch back from frame" + " Exception: " + e);
			Assert.fail("Error while trying to perform switch back from frame" + " Exception: " + e);
		}
	}

	public static boolean multiDeselectByText(WebElement locator, String options) {
		boolean flag = false;
		ArrayList<String> optionList = null;
		try {
			WebWaitHelper.waitForElement(locator);
			optionList = optionsSplit(options);
			for (int i = 0; i < optionList.size(); i++) {
				Select multiple = new Select(locator);
				multiple.deselectByVisibleText(optionList.get(i));
			}
			flag = true;
			return flag;
		} catch (Exception e) {
			if (smhealer.isShflag() || smhealer.isImpactflag()) {
				initHeal(e, locator);
				multiDeselectByText(HealdLctr, options);
			} else {
				e.printStackTrace();
				LOGGER.error("Error while trying to perform multideselect by text " + " Exception: " + e);
				Assert.fail("Error while trying to perform multideselect by text " + " Exception: " + e);
			}
		}
		return flag;
	}

	// Code added to handle alerts
	public static Alert switchToAlert() {

		Alert alert = BrowserDriver.getDriver().switchTo().alert();
		return alert;

	}

	public static void acceptAlert() {
		try {
			Thread.sleep(1000);
			switchToAlert().accept();
			LOGGER.info("Alert accepted");
		} catch (Exception e) {
			if (smhealer.isShflag() || smhealer.isImpactflag()) {
				initHeal(e, null);
			} else {
				e.printStackTrace();
				LOGGER.error("Error while trying accept alert " + " Exception: " + e);
				Assert.fail("Error while trying to accept alert " + " Exception: " + e);
			}
		}
	}

	public static void dismissAlert() {
		try {
			Thread.sleep(1000);
			switchToAlert().dismiss();
			LOGGER.info("dismissAlert action completed successfully");
		} catch (Exception e) {
			if (smhealer.isShflag() || smhealer.isImpactflag()) {
				initHeal(e, null);
			} else {
				e.printStackTrace();
				LOGGER.error("Error while trying to dismiss alert " + " Exception: " + e);
				Assert.fail("Error while trying to dismiss alert " + " Exception: " + e);
			}
		}

	}

	public static boolean multiDeselectAll(WebElement locator) {
		boolean flag = false;
		try {
			WebWaitHelper.waitForElement(locator);
			Select multiple = new Select(locator);
			multiple.deselectAll();
			flag = true;
			return flag;
		} catch (Exception e) {
			if (smhealer.isShflag() || smhealer.isImpactflag()) {
				initHeal(e, locator);
				multiDeselectAll(HealdLctr);
			} else {
				e.printStackTrace();
				LOGGER.error("Error while trying to perform multideselect" + " Exception: " + e);
				Assert.fail("Error while trying to perform multideselect" + " Exception: " + e);
			}
		}
		return flag;

	}

	public static boolean multiSelectByText(WebElement locator, String options) {
		boolean flag = false;
		ArrayList<String> optionList = null;
		try {
			WebWaitHelper.waitForElement(locator);
			optionList = optionsSplit(options);
			for (int i = 0; i < optionList.size(); i++) {
				Select multiple = new Select(locator);
				multiple.selectByVisibleText(optionList.get(i));
			}
			flag = true;
			return flag;
		} catch (Exception e) {
			if (smhealer.isShflag() || smhealer.isImpactflag()) {
				initHeal(e, locator);
				multiSelectByText(HealdLctr, options);
			} else {
				e.printStackTrace();
				LOGGER.error("Error while trying to perform multiSelectByText" + " Exception: " + e);
				Assert.fail("Error while trying to perform multiSelectByText" + " Exception: " + e);
			}
		}
		return flag;

	}

	public static boolean multiSelectByIndex(WebElement locator, String indexes) {
		boolean flag = false;
		ArrayList<String> indexList = null;
		try {
			WebWaitHelper.waitForElement(locator);
			indexList = optionsSplit(indexes);
			ArrayList<Integer> intList = (ArrayList<Integer>) indexList.stream().map(Integer::parseInt)
					.collect(Collectors.toList());
			for (int i = 0; i < intList.size(); i++) {
				Select multiple = new Select(locator);
				multiple.selectByIndex(intList.get(i));
			}

			flag = true;
			return flag;

		} catch (Exception e) {
			if (smhealer.isShflag() || smhealer.isImpactflag()) {
				initHeal(e, locator);
				multiSelectByText(HealdLctr, indexes);
			} else {
				e.printStackTrace();
				LOGGER.error(
						"Error while trying to perform multiSelect by providing an index value" + " Exception: " + e);
				Assert.fail(
						"Error while trying to perform multiSelect by providing an index value" + " Exception: " + e);
			}
		}
		return flag;
	}

	public static ArrayList<String> optionsSplit(String options) {
		ArrayList<String> tempOptionList = null;
		try {
			String[] items = options.split(":");
			tempOptionList = new ArrayList<String>(Arrays.asList(items));
			return tempOptionList;
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("Error while performing  action optionsSplit" + " Exception: " + e);
			Assert.fail("Error while performing  action optionsSplit" + " Exception: " + e);
		}
		return tempOptionList;
	}

	public static LinkedHashMap<String, Integer> getTblHeaderVal(WebElement locator) {
		LinkedHashMap<String, Integer> headermap = new LinkedHashMap<String, Integer>();
		try {
			WebWaitHelper.waitForElement(locator);
			WebElement mytableHead = locator.findElement(By.tagName("thead"));
			List<WebElement> rowsTable = mytableHead.findElements(By.tagName("tr"));
			for (int row = 0; row < rowsTable.size(); row++) {
				List<WebElement> colRow = rowsTable.get(row).findElements(By.tagName("th"));
				for (int column = 0; column < colRow.size(); column++) {
					headermap.put(colRow.get(column).getText(), column);
				}
			}
			LOGGER.info("getTblHeaderVal action completed successfully");
		} catch (Exception e) {
			if (smhealer.isShflag() || smhealer.isImpactflag()) {
				initHeal(e, locator);
				getTblHeaderVal(HealdLctr);
			} else {
				e.printStackTrace();
				LOGGER.error("Error  while performing getTblHeaderVal action " + "Exception :" + e);
				Assert.fail("Error  while performing getTblHeaderVal action " + "Exception :" + e);
			}
		}
		return headermap;
	}

	public static LinkedHashMap<String, Integer> getTblBodyVal(WebElement locator) {
		LinkedHashMap<String, Integer> bodymap = new LinkedHashMap<String, Integer>();
		try {
			WebWaitHelper.waitForElement(locator);
			WebElement mytableBody = locator.findElement(By.tagName("tbody"));
			List<WebElement> rowsTable = mytableBody.findElements(By.tagName("tr"));
			for (int row = 0; row < rowsTable.size(); row++) {
				List<WebElement> colRow = rowsTable.get(row).findElements(By.tagName("td"));
				for (int column = 0; column < colRow.size(); column++) {
					bodymap.put(colRow.get(column).getText(), column);
				}
			}
			LOGGER.info("getTblBodyVal action completed successfully");
		} catch (Exception e) {
			if (smhealer.isShflag() || smhealer.isImpactflag()) {
				initHeal(e, locator);
				getTblBodyVal(HealdLctr);
			} else {
				e.printStackTrace();
				LOGGER.error("Error while performing the getTblBodyVal action " + "Exception :" + e);
				Assert.fail("Error while performing the getTblBodyVal action " + "Exception :" + e);
			}
		}
		return bodymap;
	}

	public static String getTblTdVal(WebElement locator, int rowIndex, int colIndex) {
		WebElement ele = null;
		WebElement rowele = null;
		String tblCellValue = "";

		try {
			WebWaitHelper.waitForElement(locator);
			List<WebElement> mytables = locator.findElements(By.tagName("tr"));
			rowele = mytables.get(rowIndex);
			List<WebElement> rowsTable = rowele.findElements(By.tagName("td"));
			ele = rowsTable.get(colIndex);
			tblCellValue = ele.getText();
			LOGGER.info("The getTblTdVal action completed successfully");
		} catch (Exception e) {
			if (smhealer.isShflag() || smhealer.isImpactflag()) {
				initHeal(e, locator);
				getTblTdVal(HealdLctr, rowIndex, colIndex);
			} else {
				e.printStackTrace();
				LOGGER.error("Error  while performing the getTblTdVal action " + "Exception :" + e);
				Assert.fail("Error  while performing the getTblTdVal action " + "Exception :" + e);
			}
		}
		return tblCellValue;
	}

	public static String getTblThVal(WebElement locator, int rowIndex, int colIndex) {

		WebElement ele = null;
		WebElement tblEle = null;
		// WebElement rowEle = null;

		WebElement myTblHead = null;
		List<WebElement> myTblHeadRow = null;
		List<WebElement> myTblHeadRowCol = null;
		String tblCellValue = "";
		try {
			WebWaitHelper.waitForElement(locator);
			myTblHead = locator.findElement(By.tagName("thead"));
			myTblHeadRow = myTblHead.findElements(By.xpath("tr"));
			for (int row = 0; row < myTblHeadRow.size(); row++) {
				tblEle = myTblHeadRow.get(row);
				myTblHeadRowCol = tblEle.findElements(By.tagName("th"));
				// List<WebElement> hdrRowCol =
				// rowEle.findElements(By.tagName("th"));
				ele = myTblHeadRowCol.get(colIndex);
				tblCellValue = ele.getText();
			}
			LOGGER.info("getTblThVal action completed successfully");
		} catch (Exception e) {
			if (smhealer.isShflag() || smhealer.isImpactflag()) {
				initHeal(e, locator);
				getTblThVal(HealdLctr, rowIndex, colIndex);
			} else {
				e.printStackTrace();
				LOGGER.error("Error  while performing the getTblThVal action " + "Exception :" + e);
				Assert.fail("Error  while performing the getTblThVal action " + "Exception :" + e);
			}
		}
		return tblCellValue;

	}

	public static String getIndexofVal(WebElement locator, String value) {
		// WebElement ele = null;
		WebElement mytableBody = null;
		List<WebElement> mytableBodyrow = null;
		WebElement tblEle = null;
		List<WebElement> mytableBodycol = null;
		String indexVal = "";
		try {
			WebWaitHelper.waitForElement(locator);
			mytableBody = locator.findElement(By.tagName("tbody"));
			mytableBodyrow = mytableBody.findElements(By.tagName("tr"));
			for (int row = 0; row < mytableBodyrow.size(); row++) {
				tblEle = mytableBodyrow.get(row);
				mytableBodycol = tblEle.findElements(By.tagName("td"));
				for (int column = 0; column < mytableBodycol.size(); column++) {
					if (mytableBodycol.get(column).getText().equalsIgnoreCase(value)) {
						indexVal = row + "," + column;
						break;
					}
				}
			}
			LOGGER.info("getIndexofVal action completed successfully");
		} catch (Exception e) {
			if (smhealer.isShflag() || smhealer.isImpactflag()) {
				initHeal(e, locator);
				getIndexofVal(HealdLctr, value);
			} else {
				e.printStackTrace();
				LOGGER.error("Error while performing the getIndexofVal action " + "Exception :" + e);
				Assert.fail("Error while performing the getIndexofVal action " + "Exception :" + e);
			}
		}
		return indexVal;

	}

	public static String getFirstIndexofVal(WebElement locator, String value) {
		WebElement mytableBody = null;
		List<WebElement> mytableBodyrow = null;
		WebElement tblEle = null;
		List<WebElement> mytableBodycol = null;
		String indexVal = "";
		boolean flag = false;
		try {
			WebWaitHelper.waitForElementPresence(locator);
			mytableBody = locator.findElement(By.tagName("tbody"));
			mytableBodyrow = mytableBody.findElements(By.tagName("tr"));
			for (int row = 0; row < mytableBodyrow.size(); row++) {
				tblEle = mytableBodyrow.get(row);
				mytableBodycol = tblEle.findElements(By.tagName("td"));
				for (int column = 0; column < mytableBodycol.size(); column++) {
					if (mytableBodycol.get(column).getText().equalsIgnoreCase(value)) {
						row = row + 1;
						column = column + 1;
						LOGGER.info("Index of " + value + "is (" + row + "," + column + ")");
						indexVal = row + "," + column;
						flag = true;
						break;
					}
				}
				if (flag) {
					break;
				}
			}
		} catch (Exception e) {
			if (smhealer.isShflag() || smhealer.isImpactflag()) {
				initHeal(e, locator);
				getFirstIndexofVal(HealdLctr, value);
			} else {
				e.printStackTrace();
				LOGGER.error("Error while performing the getFirstIndexofVal action " + "Exception :" + e);
				Assert.fail("Error while performing the getFirstIndexofVal action " + "Exception :" + e);
			}
		}
		return indexVal;

	}

	public static LinkedHashMap<String, String> getColMapByHdrVal(WebElement locator, String colHeader) {
		LinkedHashMap<String, String> colMap = new LinkedHashMap<String, String>();
		LinkedHashMap<String, Integer> headerMap = new LinkedHashMap<String, Integer>();
		// WebElement ele = null;
		WebElement mytableBody = null;
		List<WebElement> mytableBodyrow = null;
		WebElement tblEle = null;
		List<WebElement> mytableBodycol = null;
		try {
			WebWaitHelper.waitForElement(locator);
			headerMap = getTblHeaderVal(locator);
			int colNum = headerMap.get(colHeader);
			mytableBody = locator.findElement(By.tagName("tbody"));
			mytableBodyrow = mytableBody.findElements(By.tagName("tr"));
			for (int row = 0; row < mytableBodyrow.size(); row++) {
				tblEle = mytableBodyrow.get(row);
				mytableBodycol = tblEle.findElements(By.tagName("td"));
				colMap.put(mytableBodycol.get(colNum).getText(), "Row Number is " + row);
			}
			return colMap;

		} catch (Exception e) {
			if (smhealer.isShflag() || smhealer.isImpactflag()) {
				initHeal(e, locator);
				getColMapByHdrVal(HealdLctr, colHeader);
			} else {
				e.printStackTrace();
				LOGGER.error("Error while performing the getColMapByHdrVal action " + "Exception :" + e);
				Assert.fail("Error while performing the getColMapByHdrVal action " + "Exception :" + e);
			}
		}
		return colMap;
	}

	public static LinkedHashMap<String, Integer> getRowMapByIndxVal(WebElement locator, int rowIndex) {
		LinkedHashMap<String, Integer> rowMap = new LinkedHashMap<String, Integer>();
		// HashMap<String, Integer> bodyMap = new HashMap<String, Integer>();
		WebElement mytableBody = null;
		List<WebElement> mytableBodyrow = null;
		try {
			WebWaitHelper.waitForElement(locator);
			mytableBody = locator.findElement(By.tagName("tbody"));
			mytableBodyrow = mytableBody.findElements(By.tagName("tr"));
			for (int i = 0; i < mytableBodyrow.get(rowIndex).findElements(By.tagName("td")).size(); i++) {

				rowMap.put(mytableBodyrow.get(rowIndex).findElements(By.tagName("td")).get(i).getText(), i);
			}
			return rowMap;
		} catch (Exception e) {
			if (smhealer.isShflag() || smhealer.isImpactflag()) {
				initHeal(e, locator);
				getRowMapByIndxVal(HealdLctr, rowIndex);
			} else {
				e.printStackTrace();
				LOGGER.error("Error while perfoming the getRowMapByIndxVal action " + "Exception :" + e);
				Assert.fail("Error while perfoming the getRowMapByIndxVal action " + "Exception :" + e);
			}
		}
		return rowMap;
	}

	public static LinkedHashMap<String, Integer> getRowMapByHdrVal(WebElement locator, String rowHeader) {
		LinkedHashMap<String, Integer> rowMap = new LinkedHashMap<String, Integer>();
		HashMap<String, Integer> bodyMap = new HashMap<String, Integer>();
		WebElement mytableBody = null;
		List<WebElement> mytableBodyrow = null;
		try {
			WebWaitHelper.waitForElement(locator);
			bodyMap = getTblBodyVal(locator);
			int rowNum = bodyMap.get(rowHeader);

			mytableBody = locator.findElement(By.tagName("tbody"));
			mytableBodyrow = mytableBody.findElements(By.tagName("tr"));
			for (int i = 0; i < mytableBodyrow.get(rowNum).findElements(By.tagName("td")).size(); i++) {

				rowMap.put(mytableBodyrow.get(rowNum).findElements(By.tagName("td")).get(i).getText(), i);
			}
			return rowMap;

		} catch (Exception e) {
			if (smhealer.isShflag() || smhealer.isImpactflag()) {
				initHeal(e, locator);
				getRowMapByHdrVal(HealdLctr, rowHeader);
			} else {
				e.printStackTrace();
				LOGGER.error("Error  while performing the getRowMapByHdrVal action " + "Exception :" + e);
				Assert.fail("Error  while performing the getRowMapByHdrVal action " + "Exception :" + e);
			}
		}
		return rowMap;
	}

	public static LinkedHashMap<String, Integer> getColMapByIndxVal(WebElement locator, int colIndex) {
		LinkedHashMap<String, Integer> colMap = new LinkedHashMap<String, Integer>();
		WebElement mytableBody = null;
		List<WebElement> mytableBodyrow = null;
		WebElement mytableBodyrowCol = null;
		try {
			WebWaitHelper.waitForElement(locator);
			mytableBody = locator.findElement(By.tagName("tbody"));
			mytableBodyrow = mytableBody.findElements(By.tagName("tr"));
			for (int i = 0; i < mytableBodyrow.size(); i++) {
				mytableBodyrowCol = mytableBodyrow.get(i).findElements(By.tagName("td")).get(colIndex);
				colMap.put(mytableBodyrowCol.getText() + i, colIndex);
			}
			return colMap;
		} catch (Exception e) {
			if (smhealer.isShflag() || smhealer.isImpactflag()) {
				initHeal(e, locator);
				getColMapByIndxVal(HealdLctr, colIndex);
			} else {
				e.printStackTrace();
				LOGGER.error("Error  while performing the getColMapByIndxVal action " + "Exception :" + e);
				Assert.fail("Error  while performing the getColMapByIndxVal action " + "Exception :" + e);
			}
		}
		return colMap;
	}

	public static void TblCelChkboxClick(WebElement tbllocator, String value, int chkColIndex) {
		WebElement mytableBody = null;
		// List<WebElement> mytableBodyrow = null;
		// WebElement eleRow = null;
		String[] arrSplit = null;
		try {
			WebWaitHelper.waitForElementPresence(tbllocator);
			String index = getFirstIndexofVal(tbllocator, value);
			arrSplit = index.split(",");

			mytableBody = tbllocator.findElement(By.tagName("tbody"));
			WebElement eleRowCol = mytableBody
					.findElement(By.xpath("tr[" + arrSplit[0] + "]//td[" + chkColIndex + "]//input"));
			eleRowCol.click();
		} catch (Exception e) {
			try {
				WebElement eleRowCol = mytableBody
						.findElement(By.xpath("tr[" + arrSplit[0] + "]//td[" + chkColIndex + "]"));
				eleRowCol.click();
			} catch (Exception e1) {

				e.printStackTrace();
				LOGGER.error("Error  while trying to click a checkbox inside table" + "Exception :" + e);
				Assert.fail("Error  while trying to click a checkbox inside table " + "Exception :" + e);
			}
		}
	}

	public static void TblCelEleClick(WebElement tbllocator, String value, int chkColIndex, String eleXpath) {
		WebElement mytableBody = null;
		List<WebElement> mytableBodyrow = null;
		WebElement eleRow = null;
		try {
			String index = getFirstIndexofVal(tbllocator, value);
			String[] arrSplit = index.split(",");

			WebWaitHelper.waitForElement(tbllocator);
			mytableBody = tbllocator.findElement(By.tagName("tbody"));
			mytableBodyrow = mytableBody.findElements(By.tagName("tr"));
			eleRow = mytableBodyrow.get(Integer.parseInt(arrSplit[0]));
			WebElement eleRowCol = eleRow.findElement(By.xpath("//td[" + chkColIndex + "]" + eleXpath));
			eleRowCol.click();
		} catch (Exception e) {
			if (smhealer.isShflag() || smhealer.isImpactflag()) {
				initHeal(e, tbllocator);
				TblCelEleClick(HealdLctr, value, chkColIndex, eleXpath);
			} else {
				e.printStackTrace();
				LOGGER.error("Error  while trying to click an element inside table" + "Exception :" + e);
				Assert.fail("Error  while trying to click an element inside table " + "Exception :" + e);
			}
		}
	}

	public static void TblCelLinkClick(WebElement locator, String value) {
		WebElement mytableBody = null;
		List<WebElement> mytableBodyrow = null;
		try {
			String index = getIndexofVal(locator, value);
			String[] arrSplit = index.split(",");
			WebWaitHelper.waitForElement(locator);
			mytableBody = locator.findElement(By.tagName("tbody"));
			mytableBodyrow = mytableBody.findElements(By.tagName("tr"));
			WebElement eleRow = mytableBodyrow.get(Integer.parseInt(arrSplit[0]));
			WebElement eleRowCol = eleRow.findElements(By.tagName("td")).get(Integer.parseInt(arrSplit[1]));
			WebElement ele = eleRowCol.findElement(By.tagName("a"));
			ele.click();
		} catch (Exception e) {
			if (smhealer.isShflag() || smhealer.isImpactflag()) {
				initHeal(e, locator);
				TblCelLinkClick(HealdLctr, value);
			} else {
				e.printStackTrace();
				LOGGER.error("Error  while trying to click a link inside table " + "Exception :" + e);
				Assert.fail("Error  while trying to click a link inside table " + "Exception :" + e);
			}
		}
	}

	public void uploadFile(String fileNameWithExtention) {
		try {
			String uploadFilePath = FileUtils.getFilePath("src\\main\\resources\\Upload\\" + fileNameWithExtention);
			String DriverPath = WebDriverPathUtil.getWiniumDriverPath();
			Process process = Runtime.getRuntime().exec("taskkill /F /IM Winium.Desktop.Driver.exe");
			process.waitFor();
			process.destroy();
			DesktopOptions options = new DesktopOptions();
			options.setApplicationPath("C:\\Windows\\System32\\openfiles.exe");
			WiniumDriverService service = new WiniumDriverService.Builder().usingDriverExecutable(new File(DriverPath))
					.usingPort(9999).withVerbose(true).withSilent(false).buildDesktopService();
			service.start();
			desktopDriver = new WiniumDriver(service, options);
			desktopDriver.findElementByName("File name:").sendKeys(uploadFilePath);
			desktopDriver.findElementByXPath("//*[@Name='Cancel']//preceding-sibling::*[@Name='Open']").click();
		} catch (Exception e) {
			LOGGER.error("Error while uploading file" + e);
			Assert.fail("Error while uploading file" + e);
		}
	}

	public static void divSpanListBox(WebElement locator, String value) {
		try {
			WebWaitHelper.waitForElement(locator);
			locator.click();
			List<WebElement> listSpan = locator.findElements(By.tagName("span"));
			for (WebElement span : listSpan) {
				if (span.getText().trim().equalsIgnoreCase(value)) {
					span.click();
					break;
				}
			}
		} catch (Exception e) {
			if (smhealer.isShflag() || smhealer.isImpactflag()) {// check selfhealing logic
				initHeal(e, locator);
				divSpanListBox(HealdLctr, value);
			} else {
				LOGGER.error("Exception :" + e);
				Assert.fail("Exception :" + e);
			}
		}
	}

}
