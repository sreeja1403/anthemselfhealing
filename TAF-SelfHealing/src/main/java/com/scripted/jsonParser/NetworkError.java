package com.scripted.jsonParser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NetworkError {
	
	public String[] NetworkError;

	public String[] getNetworkError() {
		return NetworkError;
	}

	public void setNetworkError(String[] networkError) {
		NetworkError = networkError;
	}

	@Override
	public String toString() {
		return "NetworkError [NetworkError=" + Arrays.toString(NetworkError) + "]";
	}

	
	

	
}
