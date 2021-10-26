package com.scripted.mobile;

import io.appium.java_client.MobileElement;

public class MobileAutomationException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public MobileAutomationException(MobileElement webEle, String reason) {
		super(String.format("Failed on locator %s due to error : %s", webEle,
				reason));
	}
	public MobileAutomationException(MobileElement webEle, String expected,
			String actual) {
		super(
				String.format(
						"Failed on locator %s with expected :  %s  but the actual : %s",
						webEle, expected, actual));
	} 

}
