package com.datadriven.zoho.project.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SimpleDate {

	public static void main(String[] args) throws ParseException {
		
		String month,year,date,caldate;
		
		date = "5/8/2016";
		SimpleDateFormat m = new SimpleDateFormat("MM/dd/yyyy");
		Date newdate = m.parse(date);
		
		m = new SimpleDateFormat("MMMM");
		month =  m.format(newdate);
		System.out.println(month);
		
		m = new SimpleDateFormat("yyyy");
		year =  m.format(newdate);
		
		m=new SimpleDateFormat("dd");
		caldate =  m.format(newdate);
		
		System.out.println(caldate);
		System.out.println(month+ " " + year );
		/*String dateString = m.format(da);
		dateString.replace(" ", "_").replace(":", "_").replace("/","_");  */
	}
}