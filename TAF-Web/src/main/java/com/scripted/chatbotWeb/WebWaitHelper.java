package com.scripted.chatbotWeb;

import java.time.Duration;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.Coordinates;
import org.openqa.selenium.interactions.Locatable;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.common.base.Function;

import junit.framework.Assert;


public class WebWaitHelper {

	public static Logger LOGGER = Logger.getLogger(WebWaitHelper.class);
	public static Actions action = new Actions(ChatBotDriver.getDriver());
	
	protected static int getElementTimeout() {
		return 60;
	}

	private static long getPollingTimeoutInMilliSeconds() {
		return 100;
	}

	private static Duration getPollingTimeoutInDuration() {
		return Duration.ofMillis(300);
	}

	private static Duration getElementWaitTimeoutInDuration() {
		return Duration.ofSeconds(100);
	}
	
	// return ByType of WebElement
		public static By webElementToBy(WebElement webEle) {
			// By format = "[foundFrom] -> locator: term"
			// see RemoteWebElement toString() implementation
			String flag = webEle.toString();
			flag = flag.substring(1, flag.length() - 1);
			String[] data = null;
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
			return (By) webEle;
		}

	public static void waitForElement(WebElement webEle) {

		new FluentWait<WebDriver>(ChatBotDriver.getDriver()).withTimeout(getElementWaitTimeoutInDuration())
				.pollingEvery(getPollingTimeoutInDuration()).ignoring(NoSuchElementException.class)
				.until(new Function<WebDriver, Boolean>() {
					boolean flag = false;

					public Boolean apply(WebDriver d) {
						try {
							By byEle = webElementToBy(webEle);
							waitForPresence(byEle, getElementTimeout());
							waitForNotStale(byEle, getElementTimeout());
							scrollToElement(byEle);
							waitForVisibleble(byEle, getElementTimeout());
							waitForClickable(byEle, getElementTimeout());
							flag = true;
						} catch (Exception e) {
							e.printStackTrace();
							LOGGER.error("Error occurred while waiting for loading the locator :" +webEle +"Exception :"+e);
							Assert.fail("Error occurred while waiting for loading the locator :" +webEle +"Exception :"+e);
						}
						return flag;
					}
				});

	}

	public static void waitForClear(final WebElement webEle) {
		new FluentWait<WebDriver>(ChatBotDriver.getDriver()).withTimeout(getElementWaitTimeoutInDuration())
				.pollingEvery(getPollingTimeoutInDuration()).ignoring(NoSuchElementException.class)
				.until(new Function<WebDriver, Boolean>() {
					boolean canclick = false;

					public Boolean apply(WebDriver d) {
						try {
							By byEle = webElementToBy(webEle);
							waitForPresence(byEle, getElementTimeout());
							waitForNotStale(byEle, getElementTimeout());
							scrollToElement(byEle);
							waitForVisibleble(byEle, getElementTimeout());
							waitForClickable(byEle, getElementTimeout());
							// waitForClear(byEle, getElementTimeout());
							canclick = true;
						} catch (Exception e) {
							e.printStackTrace();
							LOGGER.error("Error occurred while performing  waitForClear action for  locator :" +webEle +"Exception :"+e);
							Assert.fail("Error occurred while performing  waitForClear action for  locator :" +webEle +"Exception :"+e);
						}
						return canclick;
					}
				});
	}

	public static void waitForElementPresence(WebElement webEle) {

		new FluentWait<WebDriver>(ChatBotDriver.getDriver()).withTimeout(getElementWaitTimeoutInDuration())
				.pollingEvery(getPollingTimeoutInDuration()).ignoring(NoSuchElementException.class)
				.until(new Function<WebDriver, Boolean>() {
					boolean flag = false;

					public Boolean apply(WebDriver d) {
						try {
							By byEle = webElementToBy(webEle);
							waitForPresence(byEle, getElementTimeout());
							waitForNotStale(byEle, getElementTimeout());
							//scrollToElement(byEle);
							flag = true;
						} catch (Exception e) {
							e.printStackTrace();
							LOGGER.error("Error occurred while performing  waitForElementPresence action for  locator :" +webEle +"Exception :"+e);
							Assert.fail("Error occurred while performing  waitForElementPresence action for  locator :" +webEle +"Exception :"+e);
						}
						return flag;
					}
				});

	}

	public static void waitForPresence(By byEle, int time) {
		try
		{
		WebDriverWait wait = new WebDriverWait(ChatBotDriver.getDriver(), time, 10);
		wait.until(ExpectedConditions.presenceOfElementLocated(byEle));
		}
		catch(Exception e)
		{
			e.printStackTrace();
			LOGGER.error("Error occurred while performing  waitForPresence action for  locator :" +byEle +"Exception :"+e);
			Assert.fail("Error occurred while performing  waitForPresence action for  locator :" +byEle +"Exception :"+e);
		}
	}

	public static void waitForClickable(By byEle, int time) {
		try 
		{
		WebDriverWait wait = new WebDriverWait(ChatBotDriver.getDriver(), time, getPollingTimeoutInMilliSeconds());
		wait.until(ExpectedConditions.elementToBeClickable(byEle));
		}
		catch(Exception e)
		{
			e.printStackTrace();
			LOGGER.error("Error occurred while performing  waitForClickable action for  locator :" +byEle +"Exception :"+e);
			Assert.fail("Error occurred while performing  waitForClickable action for  locator :" +byEle +"Exception :"+e);
		}
	}

	public static void waitForVisibleble(By byEle, int time) {
		try
		{
		WebDriverWait wait = new WebDriverWait(ChatBotDriver.getDriver(), time, getPollingTimeoutInMilliSeconds());
		wait.until(ExpectedConditions.visibilityOfElementLocated(byEle));
		}
		catch(Exception e)
		{
			e.printStackTrace();
			LOGGER.error("Error occurred while performing  waitForVisibleble action for  locator :" +byEle +"Exception :"+e);
			Assert.fail("Error occurred while performing  waitForVisibleble action for  locator :" +byEle +"Exception :"+e);
		}
	}

	public static void waitForNotStale(By byEle, int time) {
		try
		{
		WebDriverWait wait = new WebDriverWait(ChatBotDriver.getDriver(), time, getPollingTimeoutInMilliSeconds());
		wait.until(stalenessOf(byEle));
		}
		catch(Exception e)
		{
			e.printStackTrace();
			LOGGER.error("Error occurred while performing  waitForNotStale action for  locator :" +byEle +"Exception :"+e);
		
			Assert.fail("Error occurred while performing  waitForNotStale action for  locator :" +byEle +"Exception :"+e);
		}
	}

	public static void scrollToElement(By locator) {
		try {
		WebElement element = ChatBotDriver.getDriver().findElement(locator);
		Coordinates coordinate = ((Locatable) element).getCoordinates();
		coordinate.onPage();
		coordinate.inViewPort();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			LOGGER.error("Error occurred while performing  scrollToElement action for  locator :" +locator +"Exception :"+e);
			Assert.fail("Error occurred while performing  scrollToElement action for  locator :" +locator +"Exception :"+e);
		}
	}

	public static ExpectedCondition<Boolean> stalenessOf(By byEle) {
		return new ExpectedCondition<Boolean>() {
			@Override
			public Boolean apply(WebDriver ignored) {
				try {
					// Calling any method forces a staleness check
					ignored.findElement(byEle).isEnabled();
					return true;
				} catch (StaleElementReferenceException expected) {
					return false;
				}
			}

			@Override
			public String toString() {
				return String.format("element (%s) to become stale", byEle);
			}
		};
	}

	public static void waitForClear(By by, int time) {
		try {
		WebDriverWait wait = new WebDriverWait(ChatBotDriver.getDriver(), time, getPollingTimeoutInMilliSeconds());
		wait.until(clearOf(by));
		}
		catch(Exception e)
		{
			e.printStackTrace();
			LOGGER.error("Error occurred while performing  waitForClear action" +"Exception :"+e);
			Assert.fail("Error occurred while performing  waitForClear action " +"Exception :"+e);
		}
	}

	public static ExpectedCondition<Boolean> clearOf(final By by) {

		return new ExpectedCondition<Boolean>() {
			@Override
			public Boolean apply(WebDriver ignored) {
				try {
					WebElement element = ignored.findElement(by);

					try {
						getAction().keyDown(Keys.CONTROL).sendKeys("a").keyUp(Keys.CONTROL).pause(1000)
								.sendKeys(Keys.DELETE).build().perform();
					} catch (Throwable e) {
						// TODO: handle exception
					}
					if (element.getText().length() < 1)
						return true;
					try {
						element.click();
						element.clear();
					} catch (Throwable e) {
						// TODO: handle exception
					}
					if (element.getText().length() < 1)
						return true;
					try {
						element.click();
						((JavascriptExecutor) ignored).executeScript("arguments[0].value ='';", element);
					} catch (Throwable e) {
						// TODO: handle exception
					}
					if (element.getText().length() < 1)
						return true;
					return false;
				} catch (Exception expected) {
					return false;
				}
			}

			@Override
			public String toString() {
				return String.format("textbox (%s) to become clear", by);
			}
		};
	}

	public static Actions getAction() {
		return new Actions(ChatBotDriver.getDriver());
	}
}
