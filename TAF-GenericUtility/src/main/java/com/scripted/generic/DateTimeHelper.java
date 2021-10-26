package com.scripted.generic;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import org.apache.log4j.Logger;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatterBuilder;
import org.joda.time.format.DateTimeParser;

public class DateTimeHelper {
	private static final Logger log = Logger.getLogger(GenericUtils.class);

	private static DateTimeHelper instance; 
	org.joda.time.format.DateTimeFormatter formatter;  
	private static String timeZone="";

	private DateTimeHelper() { 
		DateTimeParser[] parsers = {  
				DateTimeFormat.forPattern( "yyyy-MM-dd" ).getParser(), 
				DateTimeFormat.forPattern( "yyyyMMdd" ).getParser(),  
				DateTimeFormat.forPattern( "yyyy.MM.dd" ).getParser(),  
				DateTimeFormat.forPattern("dd.MM.yyyy").getParser(),
				DateTimeFormat.forPattern("dd.MM.yy").getParser(),
				DateTimeFormat.forPattern("dd-MM-yy").getParser(),
				DateTimeFormat.forPattern("MM/dd/yy").getParser(),
				DateTimeFormat.forPattern("MMM dd,yyyy").getParser(),
				DateTimeFormat.forPattern("yyyy/MM/dd").getParser(),
				DateTimeFormat.forPattern("dd-MM-yyyy").getParser(), 
				DateTimeFormat.forPattern("dd MMMM yyyy").getParser(), 
				DateTimeFormat.forPattern("dd/MM/yyyy").getParser(),
				DateTimeFormat.forPattern("dd/MM/yy").getParser(),
				DateTimeFormat.forPattern("EEE MMM dd HH:mm:ss z yyyy").getParser(),
				DateTimeFormat.forPattern("EEE MMM dd HH:mm:ss yyyy").getParser(),
				DateTimeFormat.forPattern("yyyy.MM.dd G 'at' HH:mm:ss z").getParser(),
				DateTimeFormat.forPattern("yyyyy.MMMMM.dd GGG hh:mm aaa").getParser(),
				DateTimeFormat.forPattern("EEE, d MMM yyyy HH:mm:ss Z").getParser(),
				DateTimeFormat.forPattern("yyMMddHHmmssZ").getParser(),
				DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ").getParser(),
				DateTimeFormat.forPattern( "MM-dd-yyyy" ).getParser(),
				DateTimeFormat.forPattern("ddMMyyyy").getParser()}; 
		formatter = new DateTimeFormatterBuilder().append(null, parsers).toFormatter(); 
	}; 

	public static synchronized DateTimeHelper getInstance() { 
		if (instance == null) 
			instance = new DateTimeHelper(); 
		return instance; 
	} 


	public static synchronized void setTimeZone(String zone) { 
		DateTimeHelper.getInstance().timeZone=zone;
	} 

	public static synchronized Date getCurrentDate() throws IllegalArgumentException{ 	
		String date = new SimpleDateFormat("yyyy/MM/dd").format(new Date());
		if(timeZone.length()>0){
			
			//return DateTimeHelper.getInstance().formatter.parseDateTime(new Date().toString()).toDateTime(DateTimeZone.forID(DateTimeHelper.getInstance().timeZone)).toDate();
			 Date currentDate = DateTimeHelper.getInstance().formatter.parseDateTime(date).toDateTime(DateTimeZone.forID(DateTimeHelper.getInstance().timeZone)).toDate();
			 return currentDate;
		}
			
		return DateTimeHelper.getInstance().formatter.parseDateTime(date).toDate();
	} 

	public static synchronized Date getDate(String date) throws IllegalArgumentException{ 
		if(timeZone.length()>0)
			return DateTimeHelper.getInstance().formatter.parseDateTime(date).toDateTime(DateTimeZone.forID(DateTimeHelper.getInstance().timeZone)).toDate();
		return DateTimeHelper.getInstance().formatter.parseDateTime(date).toDate();
	}

	public String getTimeZone() {
		return timeZone;
	} 
	public static String getTimeStamp() {
		log.info("Inside DateUtil.getTimeStamp method");
		return new java.text.SimpleDateFormat("yyyy-MM-dd_HH-mm-ss-SSS")
		.format(new Date());
	}

	public static boolean isCurrentDateEquelTo(String date) throws ParseException{ 
		//Date newDate = new Date(date);
		//String actualDate = new SimpleDateFormat("MM-dd-yy").parse(newDate.toString()).toString();
		return DateTimeHelper.getCurrentDate().compareTo(DateTimeHelper.getDate(date)) ==0;
	} 

	public static boolean isCurrentDateGraterThan(String date){ 
		return DateTimeHelper.getCurrentDate().compareTo(DateTimeHelper.getDate(date)) >0;
	}

	public  static boolean isCurrentDateLesserThan(String date){ 
		return DateTimeHelper.getCurrentDate().compareTo(DateTimeHelper.getDate(date)) <0;
	}
	
	public  static String getCurrentDateString(){ 
		Date date = DateTimeHelper.getCurrentDate();
		return date.toString();
	}
	
	public static boolean isDateEquelTo(String date1,String date2){ 
		return DateTimeHelper.getDate(date1).compareTo(DateTimeHelper.getDate(date2)) ==0;
	} 
	
	
	public static String getDateWithFormatAndZone(String dateFormat,String timeZone,Long floater) {
		LocalDate datee = LocalDate.now(ZoneId.of(timeZone));
		datee = datee.plus(floater,ChronoUnit.DAYS);
		System.out.println(datee);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat);
	    System.out.println(formatter.format(datee).toString()); 
		return formatter.format(datee).toString();
	}
}
