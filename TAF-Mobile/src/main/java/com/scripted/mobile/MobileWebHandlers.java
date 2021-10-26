package com.scripted.mobile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;
import io.appium.java_client.HasOnScreenKeyboard;
import io.appium.java_client.MobileElement;
import junit.framework.Assert;

public class MobileWebHandlers {

	public static Actions action = new Actions(MobileDriverSettings.getCurrentDriver());
	public static Logger LOGGER = Logger.getLogger(MobileWebHandlers.class);
	
	public static WebDriver driver;

	public static WebElement getElement(WebDriver driver, By locator) {
		WebElement element = driver.findElement(locator);
		return element;
	}

	public static By webElementToBy(WebElement mobileElement) {
		String mobileEle = mobileElement.toString();
		String locatorType, locator = null;
		if (mobileEle.contains("{") || mobileEle.contains("}")) {
			mobileEle = (mobileEle.substring(mobileEle.indexOf("{") + 1, mobileEle.indexOf("}")));
			locatorType = mobileEle.substring(mobileEle.indexOf(0) + 1, mobileEle.indexOf(": ")).replace("By.", "");
			locator = mobileEle.substring(mobileEle.indexOf(": ")).replaceFirst(": ", "");
		} else {
			mobileEle = mobileEle.replace("Located by By.", "");
			locatorType = StringUtils.substringBefore(mobileEle, ":");
			locator = StringUtils.substringAfter(mobileEle, ":").trim();

		}
		switch (locatorType) {
		case "xpath":
			return By.xpath(locator);
		case "css selector":
			return By.cssSelector(locator);
		case "id":
			return By.id(locator);
		case "tag name":
			return By.tagName(locator);
		case "name":
			return By.name(locator);
		case "link text":
			return By.linkText(locator);
		case "class name":
			return By.className(locator);
		}
		return null;

	}
	public static void enterText(WebElement locator, String value) {
		try {
			click(locator);
			locator.sendKeys(value);
			hideKeyBoard();
			LOGGER.info("Text entered successfully" + locator);
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("Error  while entering text for the locator: " + locator + "Exception :" + e);
			Assert.fail("Error  while entering text for the locator: " + locator + "Exception :" + e);

		}
	}
	
	public static void hideKeyBoard() {
		boolean isKeyboardShown = ((HasOnScreenKeyboard) MobileDriverSettings.getCurrentDriver()).isKeyboardShown();
		if (isKeyboardShown == true) {
			MobileDriverSettings.getCurrentDriver().hideKeyboard();
		}
	}

	public static void click(WebElement locator) {
		try {
			// Need to add the assertions when we decide the reporting
			MobileWebWaitHelper.waitForElement(locator);
			locator.click();
			LOGGER.info("Click action completed successfully for the locator: " + locator);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			try {
				locator.click();
			} catch (Exception ex) {
				LOGGER.error("Error while performing the click action for the locator: " + locator + "Exception :" + e);
				Assert.fail("Error while performing the click action for the locator: " + locator + "Exception :" + e);

			}
		}
	}

	public static void clickByJsExecutor(By locator) {
		try {

		} catch (Exception e) {
			System.out.println(e.getMessage());
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
				MobileWebWaitHelper.waitForElementPresence(locator);
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
			e.printStackTrace();
			LOGGER.error("Error  while trying to verify the text " + "Exception :" + e);
			Assert.fail("Text verified successfully for the locator: " + locator);

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
			MobileWebWaitHelper.waitForElement(locator);
			if (locator.getAttribute("value").isEmpty()) {
				flag = false;
				Assert.fail("Text does not exists");
			} else {
				flag = true;
				LOGGER.info("Text exits " + locator);
			}
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("Error occurred while trying to check whether the text exists " + "Exception :" + e);
			Assert.fail("Error occurred while trying to check whether the text exists " + "Exception :" + e);

		}
		return flag;
	}

	public static void clearText(WebElement locator) {
		try {
			MobileWebWaitHelper.waitForElement(locator);
			locator.clear();
			LOGGER.info("Text cleared successfully ");
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("Error occurred while trying to clear text " + "Exception :" + e);
			Assert.fail("Error occurred while trying to clear text " + "Exception :" + e);

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
			e.printStackTrace();
			LOGGER.error("Error occurred while checking whether the object is disabled " + "Exception :" + e);
			Assert.fail("Error occurred while checking whether the object is disabled " + "Exception :" + e);

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
			e.printStackTrace();
			LOGGER.error("Error  while fetching  the property value " + "Exception :" + e);
			Assert.fail("Error  while fetching  the property value " + "Exception :" + e);
		}
		return propValue;
	}

	public void verifyProperty(WebElement locator, String property, String expected) {
		String actual = "";
		try {
			actual = fetchPropertyVal(locator, property);
			LOGGER.info("Property value fetched successfully for the locator: " + locator);
		} catch (Throwable e) {
			e.printStackTrace();
			LOGGER.error("Error while trying to verify the property " + "Exception :" + e);
			Assert.fail("Error while trying to verify the property " + "Exception :" + e);
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
			MobileWebWaitHelper.waitForElement(locator);
			action.doubleClick(locator).perform();
			LOGGER.info("Double click performed successfully");
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("Error while performing double click " + "Exception: " + e);
			Assert.fail("Error while performing double click " + "Exception: " + e);
		}

	}

	public static void rightClick(WebElement locator) {
		try {
			MobileWebWaitHelper.waitForElement(locator);
			action.contextClick(locator).perform();
			LOGGER.info("Right click performed successfully");
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("Error while performing right click " + "Exception: " + e);
			Assert.fail("Error while performing right click " + "Exception: " + e);
		}
	}

	public static void objExists(WebElement locator) {
		try {
			By byEle = MobileWebHandlers.webElementToBy(locator);
			MobileWebWaitHelper.waitForPresence(byEle, MobileWebWaitHelper.getElementTimeout());
			LOGGER.info("objExists check completed successfully, Object exists ");
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("Error occurred while checking whether the object exists " + "Exception :" + e);
			Assert.fail("Error occurred while checking whether the object exists " + "Exception :" + e);
		}
	}

	public static boolean chkboxIsChecked(WebElement locator) {
		MobileWebWaitHelper.waitForElement(locator);
		boolean eFlag = false;
		try {
			if (locator.isSelected()) {
				eFlag = true;
			} else {
				eFlag = false;
				Assert.fail("Checkbox is not checked");
			}
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("Error while performing the chkboxIsChecked action for the locator: " + locator + "Exception :"
					+ e);
			Assert.fail("Error while performing the chkboxIsChecked action for the locator: " + locator + "Exception :"
					+ e);
		}
		return eFlag;

	}

	public static boolean radiobtnNotChecked(WebElement locator) {
		MobileWebWaitHelper.waitForElementPresence(locator);
		boolean eFlag = false;
		try {
			if (!locator.isSelected()) {
				eFlag = true;
			} else {
				eFlag = false;
				Assert.fail("Radio button is checked");
			}
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("Error while performing radiobtnNotChecked action " + "Exception :" + e);
			Assert.fail("Error while performing radiobtnNotChecked action " + "Exception :" + e);
		}
		return eFlag;

	}

	public static boolean radioBtnIsSelected(WebElement locator) {
		MobileWebWaitHelper.waitForElementPresence(locator);

		boolean eFlag = false;
		try {
			if (locator.isSelected()) {
				eFlag = true;
			} else {
				eFlag = false;
				Assert.fail("Radio button is not selected");
			}
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("Error while performing radioBtnIsSelected action " + "Exception :" + e);
			Assert.fail("Error while performing radioBtnIsSelected action " + "Exception :" + e);
		}
		return eFlag;

	}

	public static boolean radioBtnIsNotSelected(WebElement locator) {
		MobileWebWaitHelper.waitForElementPresence(locator);
		boolean eFlag = false;
		try {
			if (!locator.isSelected()) {
				eFlag = true;
			} else {
				eFlag = false;
				Assert.fail("Radio button is  selected");
			}
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("Error while performing radioBtnIsNotSelected action " + "Exception :" + e);
			Assert.fail("Error while performing radioBtnIsNotSelected action " + "Exception :" + e);
		}
		return eFlag;

	}

	public static HashMap<Integer, String> sltCtrlReadAllVal(WebElement locator) {
		HashMap<Integer, String> comboValuesMap = new HashMap<Integer, String>();
		try {
			MobileWebWaitHelper.waitForElement(locator);
			comboValuesMap = new HashMap<Integer, String>();
			Select dropdown = new Select(locator);
			List<WebElement> dd = dropdown.getOptions();
			for (int j = 0; j < dd.size(); j++) {
				comboValuesMap.put(j, dd.get(j).getText());
			}
			System.out.println(comboValuesMap);
			LOGGER.info("The values are " + comboValuesMap);
			return comboValuesMap;
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("Error while performing the sltCtrlReadAllVal action for the locator: " + locator
					+ "Exception :" + e);
			Assert.fail("Error while performing the sltCtrlReadAllVal action for the locator: " + locator
					+ "Exception :" + e);
		}
		return comboValuesMap;
	}

	public static String dropDownGetCurrentSelection(WebElement locator) {
		String cmbSelectedValue = "";
		try {
			MobileWebWaitHelper.waitForElement(locator);
			Select seleObj = new Select(locator);
			cmbSelectedValue = seleObj.getFirstSelectedOption().getText().trim();
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("Error while trying to get current selection of dropdown for the locator: " + locator
					+ "Exception :" + e);
			Assert.fail("Error while trying to get current selection of dropdown for the locator " + locator
					+ "Exception :" + e);
		}
		return cmbSelectedValue;

	}

	public static void dropDownSelectByIndex(WebElement locator, int index) {
		try {
			MobileWebWaitHelper.waitForElement(locator);
			String cmbSelectedValue = "";
			Select dropdown = new Select(locator);
			List<WebElement> cmbOptions = dropdown.getOptions();
			cmbSelectedValue = cmbOptions.get(index).getText();
			LOGGER.info("Selected value :" + cmbSelectedValue);
			System.out.println(cmbSelectedValue);
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error(
					"Error while trying to select index from drop down  the locator: " + locator + "Exception :" + e);
			Assert.fail(
					"Error while trying to select index from drop down  the locator: " + locator + "Exception :" + e);

		}
	}

	public static boolean dropDownSetByVal(WebElement locator, String value) {
		boolean flag = false;
		try {
			MobileWebWaitHelper.waitForElement(locator);
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
			e.printStackTrace();
			LOGGER.error(
					"Error while trying to set  drop down by value for the locator: " + locator + "Exception :" + e);
			Assert.fail("Error while trying to set drop down by value for the locator:" + locator + "Exception :" + e);
		}
		return flag;
	}

	public static boolean dropDownSetByIndex(WebElement locator, int index) {
		boolean flag = false;
		try {
			MobileWebWaitHelper.waitForElement(locator);
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
			e.printStackTrace();
			LOGGER.error(
					"Error while trying to set drop down by index for the locator: " + locator + "Exception :" + e);
			Assert.fail("Error while trying to set drop down by index for the locator:" + locator + "Exception :" + e);
		}
		return flag;
	}

	public static void switchToFrame(WebElement locator) {
		try {
			MobileDriverSettings.getCurrentDriver().switchTo().frame(locator);
			LOGGER.info("Switched to the next frame successfully:" + locator);
		} catch (MobileAutomationException e) {
			e.printStackTrace();
			LOGGER.error("Error while trying to switch to next frame  " + locator + "Exception :" + e);
			Assert.fail("Error while trying to switch to next frame  " + locator + "Exception :" + e);
		}
	}

	public void swithBackFromFrame() {
		try {
			MobileDriverSettings.getCurrentDriver().switchTo().defaultContent();
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
			MobileWebWaitHelper.waitForElement(locator);
			optionList = optionsSplit(options);
			for (int i = 0; i < optionList.size(); i++) {
				Select multiple = new Select(locator);
				multiple.deselectByVisibleText(optionList.get(i));

			}

			flag = true;
			return flag;
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("Error while trying to perform multideselect by text " + " Exception: " + e);
			Assert.fail("Error while trying to perform multideselect by text " + " Exception: " + e);
		}
		return flag;
	}

	public static boolean multiDeselectAll(WebElement locator) {
		boolean flag = false;
		try {
			MobileWebWaitHelper.waitForElement(locator);
			Select multiple = new Select(locator);
			multiple.deselectAll();
			flag = true;
			return flag;
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("Error while trying to perform multideselect" + " Exception: " + e);
			Assert.fail("Error while trying to perform multideselect" + " Exception: " + e);
		}
		return flag;

	}

	public static boolean multiSelectByText(WebElement locator, String options) {
		boolean flag = false;
		ArrayList<String> optionList = null;
		try {
			MobileWebWaitHelper.waitForElement(locator);
			optionList = optionsSplit(options);
			for (int i = 0; i < optionList.size(); i++) {
				Select multiple = new Select(locator);
				multiple.selectByVisibleText(optionList.get(i));
			}
			flag = true;
			return flag;
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("Error while trying to perform multiSelectByText" + " Exception: " + e);
			Assert.fail("Error while trying to perform multiSelectByText" + " Exception: " + e);
		}
		return flag;

	}

	public static boolean multiSelectByIndex(WebElement locator, String indexes) {
		boolean flag = false;
		ArrayList<String> indexList = null;
		try {
			MobileWebWaitHelper.waitForElement(locator);
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
			e.printStackTrace();
			LOGGER.error("Error while trying to perform multiSelect by providing an index value" + " Exception: " + e);
			Assert.fail("Error while trying to perform multiSelect by providing an index value" + " Exception: " + e);
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
			MobileWebWaitHelper.waitForElement(locator);
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
			e.printStackTrace();
			LOGGER.error("Error  while performing getTblHeaderVal action " + "Exception :" + e);
			Assert.fail("Error  while performing getTblHeaderVal action " + "Exception :" + e);
		}
		return headermap;
	}

	public static int getTblRowCount(WebElement locator) {
		MobileWebWaitHelper.waitForElement(locator);
		WebElement mytableHead = locator.findElement(By.tagName("thead"));
		List<WebElement> rowsTable = mytableHead.findElements(By.tagName("tr"));
		return rowsTable.size();
	}

	public static LinkedHashMap<String, Integer> getTblBodyVal(WebElement locator) {
		LinkedHashMap<String, Integer> bodymap = new LinkedHashMap<String, Integer>();
		try {
			MobileWebWaitHelper.waitForElement(locator);
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
			e.printStackTrace();
			LOGGER.error("Error while performing the getTblBodyVal action " + "Exception :" + e);
			Assert.fail("Error while performing the getTblBodyVal action " + "Exception :" + e);
		}
		return bodymap;
	}

	public static String getTblTdVal(WebElement locator, int rowIndex, int colIndex) {
		WebElement ele = null;
		WebElement rowele = null;
		String tblCellValue = "";

		try {
			MobileWebWaitHelper.waitForElement(locator);
			List<WebElement> mytables = locator.findElements(By.tagName("tr"));
			rowele = mytables.get(rowIndex);
			List<WebElement> rowsTable = rowele.findElements(By.tagName("td"));
			ele = rowsTable.get(colIndex);
			tblCellValue = ele.getText();
			LOGGER.info("The getTblTdVal action completed successfully");
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("Error  while performing the getTblTdVal action " + "Exception :" + e);
			Assert.fail("Error  while performing the getTblTdVal action " + "Exception :" + e);
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
			MobileWebWaitHelper.waitForElement(locator);
			myTblHead = locator.findElement(By.tagName("thead"));
			myTblHeadRow = myTblHead.findElements(By.xpath("tr"));
			for (int row = 0; row < myTblHeadRow.size(); row++) {
				tblEle = myTblHeadRow.get(row);
				myTblHeadRowCol = tblEle.findElements(By.tagName("th"));
				// List<WebElement> hdrRowCol = rowEle.findElements(By.tagName("th"));
				ele = myTblHeadRowCol.get(colIndex);
				tblCellValue = ele.getText();
			}
			LOGGER.info("getTblThVal action completed successfully");
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("Error  while performing the getTblThVal action " + "Exception :" + e);
			Assert.fail("Error  while performing the getTblThVal action " + "Exception :" + e);
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
			MobileWebWaitHelper.waitForElement(locator);
			mytableBody = locator.findElement(By.tagName("tbody"));
			mytableBodyrow = mytableBody.findElements(By.tagName("tr"));
			for (int row = 0; row < mytableBodyrow.size(); row++) {
				tblEle = mytableBodyrow.get(row);
				mytableBodycol = tblEle.findElements(By.tagName("td"));
				for (int column = 0; column < mytableBodycol.size(); column++) {
					if (mytableBodycol.get(column).getText().equalsIgnoreCase(value)) {
						System.out.println("Index of " + value + "is (" + row + "," + column + ")");
						indexVal = row + "," + column;
						break;
					}
				}
			}
			LOGGER.info("getIndexofVal action completed successfully");
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("Error while performing the getIndexofVal action " + "Exception :" + e);
			Assert.fail("Error while performing the getIndexofVal action " + "Exception :" + e);
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
			MobileWebWaitHelper.waitForElementPresence(locator);
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
			e.printStackTrace();
			LOGGER.error("Error while performing the getFirstIndexofVal action " + "Exception :" + e);
			Assert.fail("Error while performing the getFirstIndexofVal action " + "Exception :" + e);
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
			MobileWebWaitHelper.waitForElement(locator);
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
			e.printStackTrace();
			LOGGER.error("Error while performing the getColMapByHdrVal action " + "Exception :" + e);
			Assert.fail("Error while performing the getColMapByHdrVal action " + "Exception :" + e);
		}
		return colMap;
	}

	public static LinkedHashMap<String, Integer> getRowMapByIndxVal(WebElement locator, int rowIndex) {
		LinkedHashMap<String, Integer> rowMap = new LinkedHashMap<String, Integer>();
		// HashMap<String, Integer> bodyMap = new HashMap<String, Integer>();
		WebElement mytableBody = null;
		List<WebElement> mytableBodyrow = null;
		try {
			MobileWebWaitHelper.waitForElement(locator);
			mytableBody = locator.findElement(By.tagName("tbody"));
			mytableBodyrow = mytableBody.findElements(By.tagName("tr"));
			for (int i = 0; i < mytableBodyrow.get(rowIndex).findElements(By.tagName("td")).size(); i++) {

				rowMap.put(mytableBodyrow.get(rowIndex).findElements(By.tagName("td")).get(i).getText(), i);
			}
			return rowMap;
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("Error while perfoming the getRowMapByIndxVal action " + "Exception :" + e);
			Assert.fail("Error while perfoming the getRowMapByIndxVal action " + "Exception :" + e);
		}
		return rowMap;
	}

	public static LinkedHashMap<String, Integer> getRowMapByHdrVal(WebElement locator, String rowHeader) {
		LinkedHashMap<String, Integer> rowMap = new LinkedHashMap<String, Integer>();
		HashMap<String, Integer> bodyMap = new HashMap<String, Integer>();
		WebElement mytableBody = null;
		List<WebElement> mytableBodyrow = null;
		try {
			MobileWebWaitHelper.waitForElement(locator);
			bodyMap = getTblBodyVal(locator);
			int rowNum = bodyMap.get(rowHeader);

			mytableBody = locator.findElement(By.tagName("tbody"));
			mytableBodyrow = mytableBody.findElements(By.tagName("tr"));
			for (int i = 0; i < mytableBodyrow.get(rowNum).findElements(By.tagName("td")).size(); i++) {

				rowMap.put(mytableBodyrow.get(rowNum).findElements(By.tagName("td")).get(i).getText(), i);
			}
			return rowMap;

		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("Error  while performing the getRowMapByHdrVal action " + "Exception :" + e);
			Assert.fail("Error  while performing the getRowMapByHdrVal action " + "Exception :" + e);
		}
		return rowMap;
	}

	public static LinkedHashMap<String, Integer> getColMapByIndxVal(WebElement locator, int colIndex) {
		LinkedHashMap<String, Integer> colMap = new LinkedHashMap<String, Integer>();
		WebElement mytableBody = null;
		List<WebElement> mytableBodyrow = null;
		WebElement mytableBodyrowCol = null;
		try {
			MobileWebWaitHelper.waitForElement(locator);
			mytableBody = locator.findElement(By.tagName("tbody"));
			mytableBodyrow = mytableBody.findElements(By.tagName("tr"));
			for (int i = 0; i < mytableBodyrow.size(); i++) {
				mytableBodyrowCol = mytableBodyrow.get(i).findElements(By.tagName("td")).get(colIndex);
				colMap.put(mytableBodyrowCol.getText() + i, colIndex);
				System.out.println(i);
			}
			return colMap;
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("Error  while performing the getColMapByIndxVal action " + "Exception :" + e);
			Assert.fail("Error  while performing the getColMapByIndxVal action " + "Exception :" + e);
		}
		return colMap;
	}

	public static void TblCelChkboxClick(WebElement tbllocator, String value, int chkColIndex) {
		WebElement mytableBody = null;
		// List<WebElement> mytableBodyrow = null;
		// WebElement eleRow = null;
		String[] arrSplit = null;
		try {
			MobileWebWaitHelper.waitForElementPresence(tbllocator);
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

			MobileWebWaitHelper.waitForElement(tbllocator);
			mytableBody = tbllocator.findElement(By.tagName("tbody"));
			mytableBodyrow = mytableBody.findElements(By.tagName("tr"));
			eleRow = mytableBodyrow.get(Integer.parseInt(arrSplit[0]));
			WebElement eleRowCol = eleRow.findElement(By.xpath("//td[" + chkColIndex + "]" + eleXpath));
			eleRowCol.click();
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("Error  while trying to click an element inside table" + "Exception :" + e);
			Assert.fail("Error  while trying to click an element inside table " + "Exception :" + e);
		}
	}

	public static void TblCelLinkClick(WebElement locator, String value) {
		WebElement mytableBody = null;
		List<WebElement> mytableBodyrow = null;
		try {
			String index = getIndexofVal(locator, value);
			String[] arrSplit = index.split(",");
			MobileWebWaitHelper.waitForElement(locator);
			mytableBody = locator.findElement(By.tagName("tbody"));
			mytableBodyrow = mytableBody.findElements(By.tagName("tr"));
			WebElement eleRow = mytableBodyrow.get(Integer.parseInt(arrSplit[0]));
			WebElement eleRowCol = eleRow.findElements(By.tagName("td")).get(Integer.parseInt(arrSplit[1]));
			WebElement ele = eleRowCol.findElement(By.tagName("a"));
			ele.click();
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("Error  while trying to click a link inside table " + "Exception :" + e);
			Assert.fail("Error  while trying to click a link inside table " + "Exception :" + e);
		}
	}

	public static void divSpanListBox(WebElement locator, String value) {
		try {
			MobileWebWaitHelper.waitForElement(locator);
			locator.click();
			List<WebElement> listSpan = locator.findElements(By.tagName("span"));
			for (WebElement span : listSpan) {
				if (span.getText().trim().equalsIgnoreCase(value)) {
					span.click();
					break;
				}
			}
		} catch (Exception e) {
			LOGGER.error("Exception :" + e);
			Assert.fail("Exception :" + e);
		}
	}

}
