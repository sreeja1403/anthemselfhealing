package com.scripted.testscripts;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.Rectangle;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Listeners;

import com.scripted.dataload.ExcelDriver;
import com.scripted.reporting.AllureReport;
import com.scripted.reporting.AllureListener;
import com.scripted.web.BrowserDriver;

import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
@Listeners({ AllureListener.class })
public class Generic {

	public static String[] filePath = { "D:\\TCOE\\CIGNA\\Excel.xlsx", "D:\\TCOE\\CIGNA\\Excel1.xlsx",
			"D:\\TCOE\\CIGNA\\Excel2.xlsx" };
	public static Sheet currentSheet = null;
	public static int colNo = 0;
	public static ExcelDriver excelDriver = null;
	public static Map<String, Integer> headerMap = new LinkedHashMap<String, Integer>();

	public static void getRowMapColumns(int colNumber) {
		try {
			for (Row row : currentSheet) { 
				Cell cell = row.getCell(colNumber); 
				if (cell.toString().equalsIgnoreCase("NULL")) {
					System.out.println(row.getRowNum());
					System.out.println(excelDriver.getRowMap(row.getRowNum()).toString());
					AllureReport.addStep("inside get row map null columns");
					AllureReport.addStep(excelDriver.getRowMap(row.getRowNum()).toString());
				}
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public static void getRowMapBlankColumns(int colNumber) {
		try {
			for (Row row : currentSheet) { // For each Row.
				Cell cell = row.getCell(colNumber); // Get the Cell at the Index / Column you want.
				if (cell.toString().equalsIgnoreCase("")) {

					System.out.println(row.getRowNum());
					System.out.println(excelDriver.getRowMap(row.getRowNum()).toString());
					AllureReport.addStep("inside get row map blank columns");
					AllureReport.addStep(excelDriver.getRowMap(row.getRowNum()).toString());
				}
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	@Step("This step verifies the null values in a given column of excel")
	public static void getExcelData() {
		try {

			for (String str : filePath) {
				excelDriver = new ExcelDriver(str);
				System.out.println(str);
				System.out.println(excelDriver.getHeaderMap().toString());
				colNo = excelDriver.getColumnHeaderNo("Question Text");
				currentSheet = excelDriver.getCurrentSheet();
				AllureReport.addStep("Null value check");
				AllureReport.addStep(Integer.toString(colNo));
				AllureReport.addStep(str);
				getRowMapColumns(colNo);
			}

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	@Step("This step verifies the blank values in a given column of excel")
	public void blankCheck() {
		// System.out.println("Success");

		for (String str : filePath) {
			excelDriver = new ExcelDriver(str);
			System.out.println(str);
			System.out.println(excelDriver.getHeaderMap().toString());
			colNo = excelDriver.getColumnHeaderNo("Question Text");
			currentSheet = excelDriver.getCurrentSheet();
			AllureReport.addStep("Blank value check");
			AllureReport.addStep(str);
			AllureReport.addStep(Integer.toString(colNo));
			getRowMapBlankColumns(colNo);
		}
	}

	@Step("This step asserts the browser title")	
	public void browserCheck() throws Exception {
		try {
			BrowserDriver.funcGetWebdriver();
			AllureListener.setDriver(BrowserDriver.driver);
			BrowserDriver.launchWebURL("https://www.google.com");
			WebElement element = BrowserDriver.getDriver().findElement(By.xpath("//input[@name='q']"));
			JavascriptExecutor jse = (JavascriptExecutor) BrowserDriver.getDriver();
			jse.executeScript("arguments[0].style.border='2px solid red'", element);
			Thread.sleep(1000);
			File screenShot = ((TakesScreenshot)BrowserDriver.getDriver()).getScreenshotAs(OutputType.FILE);
			FileUtils.copyFile(screenShot,new File ("D:\\eleShot.png"), true);
			/*File screenShot = ((TakesScreenshot)BrowserDriver.getDriver()).getScreenshotAs(OutputType.FILE);
			Rectangle eleBorder = new Rectangle(element.getSize().width, element.getSize().height, element.getSize().height, element.getSize().width);
			Point point = element.getLocation();
			int eleWdh = element.getSize().getWidth();
			int eleHgt = element.getSize().getHeight();
			BufferedImage img = ImageIO.read(screenShot);
			BufferedImage eleScrShot = img.getSubimage(point.x,point.y, eleBorder.width, eleBorder.height);
			ImageIO.write(eleScrShot, "png", screenShot);
			File source = new File("D:\\eleShot.png");
			FileUtils.copyFile(screenShot,source);*/
			/*Thread.sleep(6000);
			String title = BrowserDriver.driver.getTitle();
			Allure.step("browser value check");
			Assert.assertEquals(title, "hello");
			System.out.println("Hello");*/
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
	} 
		
}
