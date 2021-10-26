package com.scripted.selfhealing;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.By;
import com.paulhammant.ngwebdriver.ByAngular;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;

import org.apache.log4j.Logger;
import io.appium.java_client.MobileDriver;
import io.appium.java_client.MobileElement;

public class SelfHealingUtil {
	public static Logger LOGGER = Logger.getLogger(SelfHealingUtil.class);
	
	public static ThreadLocal<String> tstCseNmeTstNG = new ThreadLocal<String>();

	

		public  ThreadLocal<String> getTstCseNmeTstNG() {
			return tstCseNmeTstNG;
		}

		public void setTstCseNmeTstNG(String testcasename) {
			tstCseNmeTstNG.set(testcasename);
			// System.out.println("Testcase name from scenarios :" + tstCseNmeTstNG.get());

		}

	public static void webWaitForPresence(int time, WebElement locator, WebDriver driver) {
		try {
			By byEle = webElementToBy(locator);
			WebDriverWait wait = new WebDriverWait(driver, time, 10);
			wait.until(ExpectedConditions.presenceOfElementLocated(byEle));
		} catch (Exception e) {

		}
	}

	/*
	 * public static void waitForElementPresence(int time,WebElement webEle) {
	 * 
	 * new FluentWait<WebDriver>(BrowserDriver.getDriver()).withTimeout(time)
	 * .pollingEvery(5)//.ignoring(NoSuchElementException.class) .until(new
	 * Function<WebDriver, Boolean>() { boolean flag = false;
	 * 
	 * public Boolean apply(WebDriver d) { try { By byEle = webElementToBy(webEle);
	 * waitForPresence(byEle, time, webEle); waitForNotStale(byEle, time, webEle);
	 * flag = true; } catch (Exception e) { e.printStackTrace(); LOGGER.
	 * error("Error occurred while performing  waitForElementPresence action for  locator :"
	 * + webEle + "Exception :" + e); Assert.
	 * fail("Error occurred while performing  waitForElementPresence action for  locator :"
	 * + webEle + "Exception :" + e); } return flag; } });
	 * 
	 * }
	 * 
	 * public static void waitForNotStale(By byEle, int time, WebElement locator) {
	 * try { WebDriverWait wait = new WebDriverWait(BrowserDriver.getDriver(), time,
	 * getPollingTimeoutInMilliSeconds()); wait.until(stalenessOf(byEle)); } catch
	 * (Exception e) { if (shflag) { //HealdLctr = smHealer.initiateHealing(e,
	 * locator,driver); LOGGER.info("Element healed: " + HealdLctr);
	 * waitForNotStale(byEle, time, locator); } else { e.printStackTrace(); LOGGER.
	 * error("Error occurred while performing  waitForNotStale action for  locator :"
	 * + byEle + "Exception :" + e); Assert.
	 * fail("Error occurred while performing  waitForNotStale action for  locator :"
	 * + byEle + "Exception :" + e); } } }
	 */
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

	public  void mobWaitForPresence(MobileElement ele, int time, MobileDriver mobDriver) {
		try {
			By byEle = mobileElementBy(ele);
			WebDriverWait wait = new WebDriverWait(mobDriver, time, 10);
			wait.until(ExpectedConditions.presenceOfElementLocated(byEle));
			LOGGER.info("waitForPresence action completed successfully");
		} catch (Exception e) {
			
		}
	}

	public static By mobileElementBy(MobileElement mobileElement) {
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

}
