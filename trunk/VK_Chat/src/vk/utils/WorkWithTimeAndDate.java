package vk.utils;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import vk.chat.R;

import android.content.Context;

public class WorkWithTimeAndDate {
	
	public static String getTime(String time, Context context){
		DateTimeFormatter dtf = DateTimeFormat.forPattern("dd.MM");		
		DateTimeFormatter date = DateTimeFormat.forPattern("yyyy-MM-dd");
		DateTimeFormatter hhmm = DateTimeFormat.forPattern("hh:mm");		
		
		DateTime now = new DateTime();
		DateTime dt = new DateTime(Long.parseLong(time+"000"));		
		
		if(now.toString(date).equals(dt.toString(date)))
			return dt.toString(hhmm);
		
		Days dd = Days.daysBetween(dt, now);
		
		if(dd.getDays() == 1)
			return context.getResources().getString(R.string.yesterday).toString();		
		return dt.toString(dtf);
	}
	
	public static String getCurrentTime(){
		DateTime now = new DateTime();
		return Long.toString(now.getMillis());
	}
	
	public static String convertToTime(String ms){
		int milliseconds = Integer.parseInt(ms);
		int seconds = (int) ((milliseconds) % 60);
		int minutes = (int) ((milliseconds) / 60);
		return minutes+":"+seconds;
	}
}
