package com.libraries.helpers;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class DateTimeHelper {
	
	public static String getStringDateFromTimeStamp(long timeStamp, String format) {
		// "ddd MM, yyyy"
		DateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
        Date netDate = (new Date(timeStamp * 1000));
        
        return sdf.format(netDate);
	}
	
	public static String formatDate(String date, String format) {
		// "ddd MM, yyyy"
		
		DateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
		Date parsedDate = new Date();
		
		try {
			parsedDate = sdf.parse(date);
			parsedDate.toString();
		} 
		catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        return "";
	}
	
	
	public static Date getDateFromTimeStamp(long timeStamp) {
        Date netDate = new Date(timeStamp * 1000);
        
        return netDate;
	}
	
	
	public static long getCurrentDateInMillis() {
//        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        
        Timestamp timeStampDate = new Timestamp(date.getTime());
		
        return timeStampDate.getTime();
	}
	
	
	public static Timestamp getTimeStampFromCurrentDate() {
		Date date = new Date();

		Timestamp ts = new Timestamp(date.getTime());
		
		return ts;
	}

}
