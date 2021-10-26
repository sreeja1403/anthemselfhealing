package com.scripted.mobile;

import static io.appium.java_client.touch.offset.PointOption.point;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
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
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import junit.framework.Assert;


public class MobileWaitHelper {
	
	public static Logger LOGGER = Logger.getLogger(MobileWaitHelper.class);
	protected static int getElementTimeout() {
		return 45;
	}

	private static long getPollingTimeoutInMilliSeconds() {
		return 30;
	}

	private static Duration getPollingTimeoutInDuration() {
		return Duration.ofMillis(100);
	}

	private static Duration getElementWaitTimeoutInDuration() {
		return Duration.ofSeconds(45);
	}

	public static void waitForElement(MobileElement mobileElement) {

		new FluentWait<WebDriver>(MobileDriverSettings.getCurrentDriver()).withTimeout(getElementWaitTimeoutInDuration())
				.pollingEvery(getPollingTimeoutInDuration()).ignoring(TimeoutException.class)
				.ignoring(ClassNotFoundException.class)
				.until(new Function<WebDriver, Boolean>() {
					boolean flag = false;

					public Boolean apply(WebDriver d) {
						try {
							By byEle = MobileHandlers.mobileElementBy(mobileElement);
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

	public static void waitForPresence(By byEle, int time) {
		try
		{
		WebDriverWait wait = new WebDriverWait(MobileDriverSettings.getCurrentDriver(), time, 10);
		wait.until(ExpectedConditions.presenceOfElementLocated(byEle));
		LOGGER.info("waitForPresence action completed successfully");
		}
		catch(Exception e)
		{
			e.printStackTrace();
			LOGGER.error("Error while performing  waitForPresence action "+"Exception :"+e);
			Assert.fail("Error while performing  waitForPresence action "+"Exception :"+e);
		}
	}

	public static void waitForClickable(By byEle, int time) {
		try
		{
		WebDriverWait wait = new WebDriverWait(MobileDriverSettings.getCurrentDriver(), time, getPollingTimeoutInMilliSeconds());
		wait.until(ExpectedConditions.elementToBeClickable(byEle));
		LOGGER.info("waitForClickable action completed successfully");
		}
		catch(Exception e)
		{
			e.printStackTrace();
			LOGGER.error("Error  while performing  waitForClickable action "+"Exception :"+e);
			Assert.fail("Error  while performing  waitForClickable action "+"Exception :"+e);
		}
	}

	public static void waitForVisibleble(By byEle, int time) {
		try
		{
		WebDriverWait wait = new WebDriverWait(MobileDriverSettings.getCurrentDriver(), time, getPollingTimeoutInMilliSeconds());
		wait.until(ExpectedConditions.visibilityOfElementLocated(byEle));
		LOGGER.info("waitForVisibleble action completed successfully");
		}
		catch(Exception e)
		{
			e.printStackTrace();
			LOGGER.error("Error while performing  waitForVisibleble action "+"Exception :"+e);
			Assert.fail("Error while performing  waitForVisibleble action "+"Exception :"+e);
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
	
	
	public static WebElement scrollToElement(By locator, String scrollType) {
			boolean found=false;
			WebElement element = null;
			while(!found)
			{
				try
				{	
					MobileDriverSettings.getCurrentDriver().manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
					// element=driver.findElement(locator);
					element = new WebDriverWait(((AppiumDriver) MobileDriverSettings.getCurrentDriver()), 2, 250)
						.until(ExpectedConditions.presenceOfElementLocated(locator));
					found=true;
				}
				catch(Exception e)
				{
					if(scrollType.equalsIgnoreCase("vertical")) {
						verticalSwipe();
					}else if(scrollType.equalsIgnoreCase("horizontal")) {
						horizontalSwipe();
					}
					
				}
			}
			return element;
		}
	
	
	public static void verticalSwipe () {
		try
		{
		Dimension size = MobileDriverSettings.getCurrentDriver().manage().window().getSize();
		int starty = (int) (size.height * 0.60);
		int endy = (int) (size.height * 0.80);
		int startx = size.width / 2;
		new TouchAction(MobileDriverSettings.getCurrentDriver()).
			press(point(startx, endy)).
			moveTo(point(startx, starty)).
			release().perform();
		LOGGER.info("vertical swipe done successfully");
		}
		catch (Exception e){
			e.printStackTrace();
			LOGGER.error("Error  while performing the verticalSwipe action "+"Exception :"+e);
			Assert.fail("Error  while performing the verticalSwipe action "+"Exception :"+e);
		}
	}
	
	
	public static void horizontalSwipe () {
		
        try {
        	Dimension size = MobileDriverSettings.getCurrentDriver().manage().window().getSize();
    		int startY = (int) (size.height / 2);
            int startX = (int) (size.width * 0.90);
            int  endX = (int) (size.width * 0.05);
            new TouchAction(MobileDriverSettings.getCurrentDriver())
                    .press(point(startX, startY))
                    .moveTo(point(endX, startY))
                    .release()
                    .perform();	
			Thread.sleep(2000);
			LOGGER.info("horizontalSwipe  done successfully");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			LOGGER.error("Error  while performing the horizontalSwipe action "+"Exception :"+e);
			Assert.fail("Error  while performing the horizontalSwipe action "+"Exception :"+e);
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
		try
		{
		WebDriverWait wait = new WebDriverWait(MobileDriverSettings.getCurrentDriver(), time, getPollingTimeoutInMilliSeconds());
		wait.until(clearOf(by));
		}
		catch (Exception e)
		{
			e.printStackTrace();
			LOGGER.error("Error  while performing the waitForClear action "+"Exception :"+e);
			Assert.fail("Error  while performing the waitForClear action "+"Exception :"+e);
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
