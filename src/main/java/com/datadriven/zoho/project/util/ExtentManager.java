package com.datadriven.zoho.project.util;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import com.relevantcodes.extentreports.DisplayOrder;
import com.relevantcodes.extentreports.ExtentReports;

public class ExtentManager {

	private static ExtentReports extent;
	public static ExtentReports getInstance(){	
	if(extent == null){
		
		Date da = new Date();
		SimpleDateFormat m = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		String filename = m.format(da);
		filename = filename.replace(" ", "_").replace(":", "_").replace("/","_")+".html";
		
		extent = new ExtentReports("E:\\ExtentReports\\"+filename,true,DisplayOrder.NEWEST_FIRST);
		extent.loadConfig(new File(System.getProperty("user.dir")+"\\report-config.xml"));
		extent.addSystemInfo("Selenium Version", "2.53.0").addSystemInfo(
				"Environment", "QA");
		 
	}
	return extent;
}	
}
