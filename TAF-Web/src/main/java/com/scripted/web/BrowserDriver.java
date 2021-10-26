package com.scripted.web;

import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.opera.OperaOptions;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;

import com.paulhammant.ngwebdriver.NgWebDriver;

import com.scripted.dataload.PropertyDriver;

public class BrowserDriver {
	public static String BrowserDriver = null;
	public static WebDriver driver = null;
	public static ThreadLocal<RemoteWebDriver> thDriver = new ThreadLocal<RemoteWebDriver>();
	public static ThreadLocal<WebDriver> lthDriver = new ThreadLocal<>();

	

	public static String strBrowserName = null;
	public static String strBrowserVersion = null;
	public static boolean strenableVideo;
	public static boolean strrecordVideo;
	public static boolean strenableVNC;
	public static String strhostURL = null;
	public static String strBrowserNameAndVersion = null;
	public static Logger LOGGER = Logger.getLogger(BrowserDriver.class);
	
	
	public static WebDriver funcGetWebdriver() {
		try {
			PropertyDriver p = new PropertyDriver();
			if (driver == null) {
				p.setPropFilePath("src/main/resources/Web/Properties/Browser.properties");
				strBrowserName = p.readProp("browserName");
			}
			if (strBrowserName == null || strBrowserName.equals(" ")) {
				LOGGER.info("Browser name is null, please check the value of browserName in config.properties");
				System.exit(0);
			} else {
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
					
				case "edge":

					MsEdgeSettings MsEdgeSettings = new MsEdgeSettings();
					MsEdgeSettings.setByMsEdgeOptions(p.getFilePath());
					driver = new EdgeDriver();
					break;
				}
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
	public static WebDriver funcGetNgWebdriver() {
			try {
				PropertyDriver p = new PropertyDriver();
				if (driver == null) {
					p.setPropFilePath("src/main/resources/Web/Properties/Browser.properties");
					strBrowserName = p.readProp("browserName");
				}
				if (strBrowserName == null || strBrowserName.equals(" ")) {
					LOGGER.info("Browser name is null, please check the value of browserName in config.properties");
					System.exit(0);
				} else {
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
			JavascriptExecutor jsdriver=(JavascriptExecutor)driver;
	        NgWebDriver ngdriver=new NgWebDriver(jsdriver);
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

	public static void closeBrowser() {
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
		if (driver == null) {
			if (thDriver.get() != null) {
				WebDriver rmDriver = thDriver.get();
				return rmDriver;
			} else {
				WebDriver lDriver = lthDriver.get();
				return lDriver;
			}
		} else {
			return driver;
		}
	}
	
	public static ThreadLocal<WebDriver> getlthDriver() {
		return lthDriver;
	}

	public static void pageWait() {
		getDriver().manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);
		getDriver().manage().timeouts().setScriptTimeout(60, TimeUnit.SECONDS);
		getDriver().manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
	}

	public static WebDriver funcSelenoidWebdriver() {
		try {
			PropertyDriver p = new PropertyDriver();
			if (driver == null) {
				p.setPropFilePath("src/main/resources/Web/Properties/Selenoid.properties");
				strBrowserName = p.readProp("browserName");
				strBrowserVersion = p.readProp("browserVersion");
				strenableVideo = Boolean.valueOf(p.readProp("enableVideo"));
				strenableVNC = Boolean.valueOf(p.readProp("enableVNC"));
				strhostURL = p.readProp("hostURL");
			}
			if (strBrowserName == null || strBrowserName.equals(" ")) {
				LOGGER.info("Browser name is null, please check the value of browserName in config.properties");
				System.exit(0);
			} else {
				LOGGER.info("Browser : " + strBrowserName);
				strBrowserName = strBrowserName.toLowerCase();

				DesiredCapabilities dcap = DesiredCapabilities.chrome();
				dcap.setCapability("enableVideo", strenableVideo);
				dcap.setCapability("enableVNC", strenableVNC);
				dcap.setBrowserName(strBrowserName);
				dcap.setVersion(strBrowserVersion);
				URL url = new URL(strhostURL);

				switch (strBrowserName) {

				case "chrome":
					driver = new RemoteWebDriver(url, dcap);
					break;

				case "ie":
					driver = new RemoteWebDriver(url, dcap);
					break;

				case "firefox":
					driver = new RemoteWebDriver(url, dcap);
					break;
				}
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

	public static WebDriver getSelenoidDriverParallel(String browser, String version) {
		try {
			URL url = null;
			PropertyDriver p = new PropertyDriver();
			p.setPropFilePath("src/main/resources/Web/Properties/Selenoid.properties");
			Boolean strenableVideo = Boolean.valueOf(p.readProp("enableVideo"));
			Boolean strenableVNC = Boolean.valueOf(p.readProp("enableVNC"));
			String strhostURL = p.readProp("hostURL");

			switch (browser) {
			case "chrome":
				DesiredCapabilities cCaps = new DesiredCapabilities();
				ChromeOptions cOptions = new ChromeOptions();
				cOptions.setCapability("enableVideo", strenableVideo);
				cOptions.setCapability("enableVNC", strenableVNC);
				cCaps.setBrowserName(browser);
				cCaps.setVersion(version);
				cCaps.setCapability(ChromeOptions.CAPABILITY, cOptions);
				url = new URL(strhostURL);
				driver = new RemoteWebDriver(url, cCaps);
				break;

			case "ie":
				IExplorerSettings iExplorerSettings = new IExplorerSettings();
				driver = new InternetExplorerDriver(iExplorerSettings.setByIExplorerOptions(p.getFilePath()));
				break;

			case "firefox":
				DesiredCapabilities fCaps = new DesiredCapabilities();
				FirefoxOptions fOptions = new FirefoxOptions();
				fOptions.setCapability("enableVideo", strenableVideo);
				fOptions.setCapability("enableVNC", strenableVNC);
				fCaps.setBrowserName(browser);
				fCaps.setVersion(version);
				fCaps.setCapability(FirefoxOptions.FIREFOX_OPTIONS, fOptions);
				url = new URL(strhostURL);
				driver = new RemoteWebDriver(url, fCaps);
				break;

			case "opera":
				DesiredCapabilities oCaps = new DesiredCapabilities();
				// OperaOptions oOptions = new OperaOptions();
				oCaps.setCapability("enableVideo", strenableVideo);
				oCaps.setCapability("enableVNC", strenableVNC);
				oCaps.setBrowserName(browser);
				oCaps.setVersion(version);
				url = new URL(strhostURL);
				driver = new RemoteWebDriver(url, oCaps);
				break;
			}
			driver.manage().deleteAllCookies();
			driver.manage().window().maximize();
			driver.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);
			driver.manage().timeouts().setScriptTimeout(60, TimeUnit.SECONDS);
			driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("Error occurred while configuring webdrivers" + "Exception :" + e);
			Assert.fail("Webdriver initialisation issues" + "Exception :" + e);
		}
		return driver;
	}

	public static WebDriver getSelenoidDriverSeq(String browser, String browserVersion) {
		try {
			URL url = null;
			PropertyDriver p = new PropertyDriver();
			if (driver == null) {
				p.setPropFilePath("src/main/resources/Web/Properties/Selenoid.properties");

				strBrowserNameAndVersion = p.readProp("browserNameAndVersion");
				strBrowserName = browser;
				strBrowserVersion = browserVersion;
				strenableVideo = Boolean.valueOf(p.readProp("enableVideo"));
				strenableVNC = Boolean.valueOf(p.readProp("enableVNC"));
				strhostURL = p.readProp("hostURL");
			}
			if (strBrowserName == null || strBrowserName.equals(" ")) {
				LOGGER.info("Browser name is null, please check the value of browserName in config.properties");
				System.exit(0);
			} else {
				LOGGER.info("Browser : " + strBrowserName);
				strBrowserName = strBrowserName.toLowerCase();

				switch (browser) {
				case "chrome":
					DesiredCapabilities cCaps = new DesiredCapabilities();
					ChromeOptions cOptions = new ChromeOptions();
					cOptions.setCapability("enableVideo", strenableVideo);
					cOptions.setCapability("enableVNC", strenableVNC);
					cCaps.setBrowserName(strBrowserName);
					cCaps.setVersion(strBrowserVersion);
					cCaps.setCapability(ChromeOptions.CAPABILITY, cOptions);
					url = new URL(strhostURL);
					driver = new RemoteWebDriver(url, cCaps);
					break;

				case "ie":
					IExplorerSettings iExplorerSettings = new IExplorerSettings();
					driver = new InternetExplorerDriver(iExplorerSettings.setByIExplorerOptions(p.getFilePath()));
					break;

				case "firefox":
					DesiredCapabilities fCaps = new DesiredCapabilities();
					FirefoxOptions fOptions = new FirefoxOptions();
					fOptions.setCapability("enableVideo", strenableVideo);
					fOptions.setCapability("enableVNC", strenableVNC);
					fCaps.setBrowserName(strBrowserName);
					fCaps.setVersion(strBrowserVersion);
					fCaps.setCapability(FirefoxOptions.FIREFOX_OPTIONS, fOptions);
					url = new URL(strhostURL);
					driver = new RemoteWebDriver(url, fCaps);
					break;

				case "opera":
					DesiredCapabilities oCaps = new DesiredCapabilities();
					OperaOptions oOptions = new OperaOptions();
					oCaps.setCapability("enableVideo", strenableVideo);
					oCaps.setCapability("enableVNC", strenableVNC);
					oCaps.setBrowserName(strBrowserName);
					oCaps.setVersion(strBrowserVersion);
					oCaps.setCapability(OperaOptions.CAPABILITY, oOptions);
					url = new URL(strhostURL);
					driver = new RemoteWebDriver(url, oCaps);
					break;
				}
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
	
	public static WebDriver getZaleniumDriverParallel(String browser, String version) {
		try {
			URL url = null;
			PropertyDriver p = new PropertyDriver();
			p.setPropFilePath("src/main/resources/Web/Properties/Zalenium.properties");
			Boolean strenableVideo = Boolean.valueOf(p.readProp("enableVideo"));
			Boolean strrecordVideo = Boolean.valueOf(p.readProp("recordVideo"));
			Boolean strenableVNC = Boolean.valueOf(p.readProp("enableVNC"));
			String strhostURL = p.readProp("hostURL");

			switch (browser) {
			case "chrome":
				DesiredCapabilities cCaps = new DesiredCapabilities();
				ChromeOptions cOptions = new ChromeOptions();
				cOptions.setCapability("enableVideo", strenableVideo);
				cOptions.setCapability("recordVideo", strrecordVideo);
				cOptions.setCapability("enableVNC", strenableVNC);
				cCaps.setBrowserName(browser);
				cCaps.setVersion(version);
				cCaps.setCapability(ChromeOptions.CAPABILITY, cOptions);
				url = new URL(strhostURL);
				thDriver.set(new RemoteWebDriver(url, cCaps));
				break;

			case "ie":
				IExplorerSettings iExplorerSettings = new IExplorerSettings();
				driver = new InternetExplorerDriver(iExplorerSettings.setByIExplorerOptions(p.getFilePath()));
				break;

			case "firefox":
				DesiredCapabilities fCaps = new DesiredCapabilities();
				FirefoxOptions fOptions = new FirefoxOptions();
				fOptions.setCapability("enableVideo", strenableVideo);
				fOptions.setCapability("recordVideo", strrecordVideo);
				fOptions.setCapability("enableVNC", strenableVNC);
				fCaps.setBrowserName(browser);
				fCaps.setVersion(version);
				fCaps.setCapability(FirefoxOptions.FIREFOX_OPTIONS, fOptions);
				url = new URL(strhostURL);
				thDriver.set(new RemoteWebDriver(url, fCaps));
				break;

			case "opera":
				DesiredCapabilities oCaps = new DesiredCapabilities();
				oCaps.setCapability("enableVideo", strenableVideo);
				oCaps.setCapability("recordVideo", strrecordVideo);
				oCaps.setCapability("enableVNC", strenableVNC);
				oCaps.setBrowserName(browser);
				oCaps.setVersion(version);
				url = new URL(strhostURL);
				thDriver.set(new RemoteWebDriver(url, oCaps));
				break;
			}
			getDriver().manage().deleteAllCookies();
			getDriver().manage().window().maximize();
			getDriver().manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);
			getDriver().manage().timeouts().setScriptTimeout(60, TimeUnit.SECONDS);
			getDriver().manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return getDriver();
	}
	public static WebDriver getZaleniumDriverSeq(String browser, String browserVersion) {
		try {
			URL url = null;
			PropertyDriver p = new PropertyDriver();
			if (driver == null) {
				p.setPropFilePath("src/main/resources/Web/Properties/Zalenium.properties");

				strBrowserNameAndVersion = p.readProp("browserNameAndVersion");
				strBrowserName = browser;
				strBrowserVersion = browserVersion;
				strenableVideo = Boolean.valueOf(p.readProp("enableVideo"));
				strrecordVideo = Boolean.valueOf(p.readProp("recordVideo"));
				strenableVNC = Boolean.valueOf(p.readProp("enableVNC"));
				strhostURL = p.readProp("hostURL");
			}
			if (strBrowserName == null || strBrowserName.equals(" ")) {
				LOGGER.info("Browser name is null, please check the value of browserName in config.properties");
				System.exit(0);
			} else {
				LOGGER.info("Browser : " + strBrowserName);
				strBrowserName = strBrowserName.toLowerCase();

				switch (browser) {
				case "chrome":
					DesiredCapabilities cCaps = new DesiredCapabilities();
					ChromeOptions cOptions = new ChromeOptions();
					cOptions.setCapability("enableVideo", strenableVideo);
					cOptions.setCapability("recordVideo", strrecordVideo);
					cOptions.setCapability("enableVNC", strenableVNC);
					cCaps.setBrowserName(strBrowserName);
					cCaps.setVersion(strBrowserVersion);
					cCaps.setCapability(ChromeOptions.CAPABILITY, cOptions);
					url = new URL(strhostURL);
					thDriver.set(new RemoteWebDriver(url, cCaps));
					break;

				case "ie":
					IExplorerSettings iExplorerSettings = new IExplorerSettings();
					driver = new InternetExplorerDriver(iExplorerSettings.setByIExplorerOptions(p.getFilePath()));
					break;

				case "firefox":
					DesiredCapabilities fCaps = new DesiredCapabilities();
					FirefoxOptions fOptions = new FirefoxOptions();
					fOptions.setCapability("enableVideo", strenableVideo);
					fOptions.setCapability("recordVideo", strrecordVideo);
					fOptions.setCapability("enableVNC", strenableVNC);
					fCaps.setBrowserName(strBrowserName);
					fCaps.setVersion(strBrowserVersion);
					fCaps.setCapability(FirefoxOptions.FIREFOX_OPTIONS, fOptions);
					url = new URL(strhostURL);
					thDriver.set(new RemoteWebDriver(url, fCaps));
					break;

				case "opera":
					DesiredCapabilities oCaps = new DesiredCapabilities();
					OperaOptions oOptions = new OperaOptions();
					oCaps.setCapability("enableVideo", strenableVideo);
					oOptions.setCapability("recordVideo", strrecordVideo);
					oCaps.setCapability("enableVNC", strenableVNC);
					oCaps.setBrowserName(strBrowserName);
					oCaps.setVersion(strBrowserVersion);
					oCaps.setCapability(OperaOptions.CAPABILITY, oOptions);
					url = new URL(strhostURL);
					thDriver.set(new RemoteWebDriver(url, oCaps));
					break;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("Error occurred while configuring webdrivers" + "Exception :" + e);
			Assert.fail("Webdriver initialisation issues" + "Exception :" + e);
		}

		getDriver().manage().deleteAllCookies();
		getDriver().manage().window().maximize();
		getDriver().manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);
		getDriver().manage().timeouts().setScriptTimeout(60, TimeUnit.SECONDS);
		getDriver().manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		return getDriver();
	}

	public static void getSeleniunGridDriver(String browser) {
		try {
			URL url = null;
			LOGGER.info("Browser : " + browser);
			// LOGGER.info("Version : " + version);
			PropertyDriver p = new PropertyDriver();
			p.setPropFilePath("src/main/resources/Web/Properties/SeleniumGrid.properties");
			String strhostURL = p.readProp("hostURL");

			switch (browser) {
			case "chrome":
				DesiredCapabilities cCaps = new DesiredCapabilities();
				ChromeOptions cOptions = new ChromeOptions();
				cOptions.addArguments("--start-maximized");
				cCaps.setBrowserName(browser);
				cCaps.setCapability(ChromeOptions.CAPABILITY, cOptions);
				url = new URL(strhostURL);
				thDriver.set(new RemoteWebDriver(url, cCaps));
				break;

			case "internet explorer":
				DesiredCapabilities iCaps = new DesiredCapabilities();
				iCaps = DesiredCapabilities.internetExplorer();
				iCaps.setCapability(CapabilityType.BROWSER_NAME, browser);
				url = new URL(strhostURL);
				thDriver.set(new RemoteWebDriver(url, iCaps));
				break;

			case "firefox":
				DesiredCapabilities fCaps = new DesiredCapabilities();
				FirefoxOptions fOptions = new FirefoxOptions();
				fCaps.setBrowserName(browser);
				fCaps.setCapability(FirefoxOptions.FIREFOX_OPTIONS, fOptions);
				url = new URL(strhostURL);
				thDriver.set(new RemoteWebDriver(url, fCaps));
				break;

			case "opera":
				DesiredCapabilities oCaps = new DesiredCapabilities();
				OperaOptions oOptions = new OperaOptions();
				oCaps.setBrowserName(browser);
				oCaps.setCapability(OperaOptions.CAPABILITY, oOptions);
				url = new URL(strhostURL);
				thDriver.set(new RemoteWebDriver(url, oCaps));
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

	public static void getCuPalDriver() {
		try {
			URL url = null;
			PropertyDriver p = new PropertyDriver();
			p.setPropFilePath("src/main/resources/Web/Properties/Browser.properties");
			String browser = p.readProp("browserName");
			LOGGER.info("Browser : " + browser);

			switch (browser) {
			case "chrome":
				ChromeSettings chromeSettings = new ChromeSettings();
				lthDriver.set(new ChromeDriver(chromeSettings.setBychromeOptions(p.getFilePath())));
				LOGGER.info("Thread local driver initiated" + lthDriver);
				break;

			case "internet explorer":
				IExplorerSettings iExplorerSettings = new IExplorerSettings();
				lthDriver.set(new InternetExplorerDriver(iExplorerSettings.setByIExplorerOptions(p.getFilePath())));
				LOGGER.info("Thread local driver initiated" + lthDriver);
				break;

			case "firefox":
				FireFoxSettings fireFoxSettings = new FireFoxSettings();
				lthDriver.set(new FirefoxDriver(fireFoxSettings.setByFirefoxOptions(p.getFilePath())));
				LOGGER.info("Thread local driver initiated" + lthDriver);
				break;

			case "phantom":
				PhatomJSSettings phatomJSSettings = new PhatomJSSettings();
				lthDriver.set(new PhantomJSDriver(phatomJSSettings.setByPhatomJSSettings(p.getFilePath())));
				LOGGER.info("Thread local driver initiated" + lthDriver);
				break;
			}
			getDriver().manage().deleteAllCookies();
			getDriver().manage().window().maximize();
			getDriver().manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);
			getDriver().manage().timeouts().setScriptTimeout(60, TimeUnit.SECONDS);
			getDriver().manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String getPopup(String message) {
		return "var infoSpan = document.createElement('div');\r\n" + 
					"infoSpan.id = 'infoSpan';\r\n" + 
					"infoSpan.innerHTML = '"+message+"';\r\n" + 
					"var style = document.createElement('style');\r\n" + 
					"style.innerHTML = '#infoSpan {font-family: Arial;font-size: larger;top: 1px;position: absolute;color: #ffffff;background-color: #ff0000;padding: 20px;;width: 1400px;height: 50px;z-index:2000;}';\r\n" + 
					"document.head.appendChild(style);\r\n" + 
					"document.body.appendChild(infoSpan);\r\n";
		}
	
	public static void launchWebURL_Auth(String strURL, String userName, String password) {
        try {
               strURL = createAuthUrl(strURL, userName, password);
               getDriver().get(strURL);
               pageWait();
               if (getDriver().getCurrentUrl() != strURL) {
                     getDriver().get(strURL);
               }
        } catch (Exception e) {
               e.printStackTrace();
               LOGGER.error("Error occurred while launching Web URL" + "Exception :" + e);
               Assert.fail("Error occurred while launching Web URL" + "Exception :" + e);
        }
  }

  public static String createAuthUrl(String url, String usr, String pwd) throws Exception {
        int p1 = url.indexOf("://");
        String http = "http://";
        String site = "";
        if (p1 != -1) {
               http = url.substring(0, p1) + "://";
               site = url.substring(p1 + 3);
        }
        //return http + usr + ":" + GenericUtils.decryptPass(pwd) + "@" + site;
        return http + usr + ":" + pwd + "@" + site;
  }



}
