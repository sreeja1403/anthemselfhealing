package com.scripted.selfHealingTests;

import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.scripted.web.BrowserDriver;

public class TrialTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		WebDriver driver = null;
		driver=BrowserDriver.funcGetWebdriver();
		// BrowserDriver.launchWebURL("http://localhost:9001/MyApp/");
		 try {
		throw new StaleElementReferenceException("");
		 }
		 catch(Exception e)
		 {
			System.out.println("exception name :"+e.getClass().getName());
			System.out.println("exception simple name :"+e.getClass().getSimpleName());
		 }

	}

}
