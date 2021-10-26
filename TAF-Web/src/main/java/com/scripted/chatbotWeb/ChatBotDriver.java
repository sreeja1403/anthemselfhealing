package com.scripted.chatbotWeb;

import static org.testng.Assert.assertEquals;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;

import com.scripted.dataload.PropertyDriver;
import com.scripted.web.BrowserDriver;
import com.scripted.web.ChromeSettings;
import com.scripted.web.FireFoxSettings;
import com.scripted.web.IExplorerSettings;
import com.scripted.web.PhatomJSSettings;

public class ChatBotDriver {

	public static WebDriver driver;
	public static String strBrowserName = null;
	public static String strBrowserVersion = null;
	public static Logger LOGGER = Logger.getLogger(BrowserDriver.class);
	public static WebElement CbtInputEle;
	public static List<WebElement> CbtOutputEle;
	public static String objChatbotInputElement;
	public static String objChatbotOutputElement;

	public static void invokeChatBot(String strChatBotProp) {
		PropertyDriver prop = new PropertyDriver();
		prop.setPropFilePath("src/main/resources/ChatBotProp/" + strChatBotProp + ".properties");
		strBrowserName = prop.readProp("Browser");
		String strURL = prop.readProp("ChatBotURL");
		objChatbotInputElement = prop.readProp("ChatbotInputElement");
		objChatbotOutputElement = prop.readProp("ChatbotOutputElement");
		funcGetWebdriver(strBrowserName);
		launchWebURL(strURL);
	}

	public static WebDriver funcGetWebdriver(String strBrowserName) {
		try {
			PropertyDriver p = new PropertyDriver();
			LOGGER.info("Browser : " + strBrowserName);
			strBrowserName = strBrowserName.toLowerCase();

			switch (strBrowserName) {

			case "chrome":
				ChromeSettings chromeSettings = new ChromeSettings();
				driver = new ChromeDriver(chromeSettings.setBychromeOptions(p.getFilePath()));
				break;

			case "ie":
				IExplorerSettings iExplorerSettings = new IExplorerSettings();
				driver = new InternetExplorerDriver(iExplorerSettings.setByIExplorerOptions(p.getFilePath()));
				break;

			case "firefox":
				FireFoxSettings fireFoxSettings = new FireFoxSettings();
				driver = new FirefoxDriver(fireFoxSettings.setByFirefoxOptions(p.getFilePath()));
				break;

			case "phantom":

				PhatomJSSettings phatomJSSettings = new PhatomJSSettings();
				driver = new PhantomJSDriver(phatomJSSettings.setByPhatomJSSettings(p.getFilePath()));
				break;
			}

		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("Error occurred while configuring webdrivers" + "Exception :" + e);
			Assert.fail("Webdriver initialisation issues" + "Exception :" + e);
		}

		driver.manage().deleteAllCookies();
		driver.manage().window().maximize();
		driver.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);
		driver.manage().timeouts().setScriptTimeout(60, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		return driver;
	}

	public static void launchWebURL(String strURL) {
		try {
			getDriver().get(strURL);
			pageWait();
			LOGGER.info("Application launched successfully");
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("Error occurred while launching Web URL" + "Exception :" + e);
			Assert.fail("Error occurred while launching Web URL" + "Exception :" + e);
		}
	}

	public static void closeDriver() {
		try {
			getDriver().close();
			driver = null;
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("Error occurred while closing browser" + "Exception :" + e);
			Assert.fail("Error occurred while closing browser" + "Exception :" + e);
		}
	}

	public static void quitBrowser() {
		try {
			getDriver().quit();
			driver = null;
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("Error occurred while quit browser" + "Exception :" + e);
			Assert.fail("Error occurred while quit browser" + "Exception :" + e);
		}
	}

	public static WebDriver getDriver() {
		return driver;
	}

	public static void pageWait() {
		getDriver().manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);
		getDriver().manage().timeouts().setScriptTimeout(60, TimeUnit.SECONDS);
		getDriver().manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
	}

	public static void enterText(WebElement locator, String value) {
		try {
			WebWaitHelper.waitForClear(locator);
			locator.clear();
			locator.sendKeys(new String[] { value });
			locator.sendKeys(Keys.ENTER);
			LOGGER.info("Text entered successfully" + locator);
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("Error  while entering text for the locator: " + locator + "Exception :" + e);
			Assert.fail("Error  while entering text for the locator: " + locator + "Exception :" + e);

		}
	}

	public static void click(WebElement locator) {
		try {
			// Need to add the assertions when we decide the reporting
			WebWaitHelper.waitForElement(locator);
			locator.click();
			pageWait();
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
			e.printStackTrace();
			LOGGER.error("Error while trying to verify the text " + "Exception :" + e);
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

	public static void UserSaysText(String strText) {
		CbtInputEle = driver.findElement(By.xpath(objChatbotInputElement));
		enterText(CbtInputEle, strText);
	}

	public static void WaitBotSaysText(String strText) {
		try {
			Thread.sleep(5000);
			CbtOutputEle = driver.findElements(By.xpath(objChatbotOutputElement));
			int pCount = CbtOutputEle.size();
			WebElement pEle = CbtOutputEle.get(pCount - 1);

			WebWaitHelper.waitForElement(pEle);

			String strActText = pEle.getText();
			if (!strActText.contains(strText)) {
				LOGGER.error(
						"Missmatch in the Chatbot response: Actual is : " + strActText + " / Expected is : " + strText);
				Assert.fail(
						"Missmatch in the Chatbot response: Actual is : " + strActText + " / Expected is : " + strText);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
			LOGGER.error("Error while trying to validate the reponse from the chatbot" + e);
		}
	}
}
