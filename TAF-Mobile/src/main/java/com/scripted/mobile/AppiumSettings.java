package com.scripted.mobile;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.GeneralServerFlag;

public class AppiumSettings {
	private static AppiumDriverLocalService appiumService;
	private static String serverIp = "127.0.0.1";
	private String serverPort = "4723";
	private static Map<String, String> appiumProp = null;
	
	public static String getNodeJsPath() {
		if (System.getenv("NODE_BINARY_PATH") != null)
			return System.getenv("NODE_BINARY_PATH");
		return appiumProp.get("NODE_BINARY_PATH");
	}

	public static String getAppiumJsPath() {
		if (System.getenv("APPIUM_BINARY_PATH")!=null)
			return System.getenv("APPIUM_BINARY_PATH");
		return appiumProp.get("NODE_BINARY_PATH");
	}
	
	public static void startAppiumServer(Map<String, String> properties)
	{
		appiumProp = properties;
		appiumService = AppiumDriverLocalService
				.buildService(new AppiumServiceBuilder()
				.withIPAddress(serverIp)		
				.usingPort(4723)
				.withArgument(GeneralServerFlag.LOG_LEVEL,"error")
				.usingDriverExecutable(new File(getNodeJsPath()))
				.withAppiumJS(new File(getAppiumJsPath())));
		appiumService.start();
		System.out.println("Appium Service Started at Address : - " + appiumService.getUrl().toString());
	}

	public static void stopAppiumServer()
	{
		appiumService.stop();
	}
	
	public void checkAppiumserverRunning(int port) {
		boolean isServerRunning = false;
	    ServerSocket serverSocket;
	    try {
	        serverSocket = new ServerSocket(port);
	        serverSocket.close();
	    } catch (IOException e) {
	        isServerRunning = true;
	    } finally {
	        serverSocket = null;
	    }
	  
	}
	
	public static List<String> getDeviceDetails() throws IOException, InterruptedException {
		List<String> deviceName = new ArrayList<String>();
		String line = null;
		Process process = Runtime.getRuntime().exec("cmd.exe /c adb devices");
        process.waitFor();
        BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
        while ((line=input.readLine()) != null) {
            if(!(line.equalsIgnoreCase("list of devices attached") || line.isEmpty()) )
            {
            deviceName.add(line.split("\t")[0]);
            }	
           
        }
		return deviceName;
        
	}

}
