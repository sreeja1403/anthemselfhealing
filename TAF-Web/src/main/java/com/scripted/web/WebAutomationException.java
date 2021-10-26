package com.scripted.web;

import org.openqa.selenium.WebElement;

public class WebAutomationException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public WebAutomationException(WebElement webEle, String reason) {
		super(String.format("Failed on locator %s due to error : %s", webEle, reason));
	}

	public WebAutomationException(WebElement webEle, String expected, String actual) {
		super(String.format("Failed on locator %s with expected :  %s  but the actual : %s", webEle, expected, actual));
	}

	public WebAutomationException(RuntimeException t) {
		super(t);
	}

	public WebAutomationException(String message) {
		super(message);
	}

	public WebAutomationException(String message, RuntimeException e) {
		super(message, e);
	}
}
