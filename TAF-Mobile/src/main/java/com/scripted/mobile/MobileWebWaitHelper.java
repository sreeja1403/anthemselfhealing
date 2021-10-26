package com.scripted.mobile;

import java.time.Duration;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.common.base.Function;

import io.appium.java_client.MobileElement;
import junit.framework.Assert;

public class MobileWebWaitHelper {

	public static Logger LOGGER = Logger.getLogger(MobileWebWaitHelper.class);
	public static Actions action = new Actions(MobileDriverSettings.getCurrentDriver());
	
	protected static int getElementTimeout() {
		return 10;
	}

	private static long getPollingTimeoutInMilliSeconds() {
		return 30;
	}

	private static Duration getPollingTimeoutInDuration() {
		return Duration.ofMillis(300);
	}

	private static Duration getElementWaitTimeoutInDuration() {
		return Duration.ofSeconds(30);
	}

	public static void waitForElement(WebElement webElement) {

		new FluentWait<WebDriver>(MobileDriverSettings.getCurrentDriver()).withTimeout(getElementWaitTimeoutInDuration())
				.pollingEvery(getPollingTimeoutInDuration()).ignoring(TimeoutException.class)
				.ignoring(ClassNotFoundException.class)
				.until(new Function<WebDriver, Boolean>() {
					boolean flag = false;

					public Boolean apply(WebDriver d) {
						try {
							By byEle = MobileWebHandlers.webElementToBy(webElement);
							waitForPresence(byEle, getElementTimeout());
							waitForNotStale(byEle, getElementTimeout());
							waitForVisibleble(byEle, getElementTimeout());
							waitForClickable(byEle, getElementTimeout());
							flag = true;
							LOGGER.info("waitForElement action completed successfully");
						} catch (Exception e) 
						{
							e.printStackTrace();
							LOGGER.error("Error  while performing  waitForElement action "+"Exception :"+e);
							Assert.fail("Error  while performing  waitForElement action "+"Exception :"+e);
						}
						return flag;
					}
				});

	}

	public static void waitForElementPresence(WebElement webEle) {

		new FluentWait<WebDriver>(MobileDriverSettings.getCurrentDriver()).withTimeout(getElementWaitTimeoutInDuration())
				.pollingEvery(getPollingTimeoutInDuration()).ignoring(TimeoutException.class)
				.until(new Function<WebDriver, Boolean>() {
					boolean flag = false;

					public Boolean apply(WebDriver d) {
						try {
							By byEle = MobileWebHandlers.webElementToBy(webEle);
							waitForPresence(byEle, getElementTimeout());
							waitForNotStale(byEle, getElementTimeout());
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
		WebDriverWait wait = new WebDriverWait(MobileDriverSettings.getCurrentDriver(), time, 10);
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
		WebDriverWait wait = new WebDriverWait(MobileDriverSettings.getCurrentDriver(), time, getPollingTimeoutInMilliSeconds());
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
		WebDriverWait wait = new WebDriverWait(MobileDriverSettings.getCurrentDriver(), time, getPollingTimeoutInMilliSeconds());
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
		WebDriverWait wait = new WebDriverWait(MobileDriverSettings.getCurrentDriver(), time, getPollingTimeoutInMilliSeconds());
		wait.until(stalenessOf(byEle));
		}
			
		catch(Exception e)
		{
			e.printStackTrace();
			LOGGER.error("Error  while performing  waitForNotStale action "+"Exception :"+e);
			Assert.fail("Error  while performing  waitForNotStale action "+"Exception :"+e);
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
		WebDriverWait wait = new WebDriverWait(MobileDriverSettings.getCurrentDriver(), time, getPollingTimeoutInMilliSeconds());
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
		return new Actions(MobileDriverSettings.getCurrentDriver());
	}


}
