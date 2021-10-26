package com.scripted.roi;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.Date;

import org.apache.http.client.ClientProtocolException;

public class ROIExeTime {
	public static  Timestamp startTime;
	public static Timestamp endTime;

	public static void startTime() {

		Date date = new java.util.Date();
		startTime = new Timestamp(date.getTime());
		
	}

	public static String endTime() throws ClientProtocolException, IOException {
		Date date = new java.util.Date();
		endTime = new Timestamp(date.getTime());
		// get time difference in seconds
		long milliseconds = endTime.getTime() - startTime.getTime();
		double seconds = milliseconds / 1000;
		// calculate hours minutes and seconds
		DecimalFormat df = new DecimalFormat("0.0000");
		double hours = seconds / 3600;
		double minutes = (seconds % 3600) / 60;
		seconds = (seconds % 3600) % 60;
		df = new DecimalFormat("0.0000");
		String totalHours = df.format(hours);
		return totalHours;

	}

}
