package com.scripted.testscripts;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import com.scripted.ArunConPageObject.ArunConAppPage;
import com.scripted.desktop.DesktopDriver;

public class ArunConTest {
	public static WebDriver driver = DesktopDriver.funGetDriver("D:\\SampleDesktop\\ArunCon.exe");
	
	public static void main(String[] args) throws Exception {

		ArunConAppPage APage = PageFactory.initElements(driver, ArunConAppPage.class);
		APage.selectRegion();
		APage.selectTestCoverageLevel();
		/*APage.selectTestCoverage();
		APage.selectAppType();
		APage.selectOS();
		boolean f=APage.verifyData("HTC One M8");
		if(f==true)
		{
			System.out.println("Pass");
		}
		//LinkedHashMap<String, Integer> s=APage.getTable();
		//System.out.println(s);
		boolean flag=APage.SelectedRadio();
		if(flag==true)
		{
			System.out.println("Selected");
		}
		boolean flag1=APage.NotSelectedRadio();
		if(flag1==true)
		{
			System.out.println("NotSelected");
		}
		else{
			System.out.println("Error");
		}*/
	}
}
	