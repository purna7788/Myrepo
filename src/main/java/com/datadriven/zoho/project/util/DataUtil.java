package com.datadriven.zoho.project.util;

import java.util.Hashtable;

public class DataUtil {

	
	
public static Object[][] getTestData(String testName, String path) {

	Xls_Reader xls = new Xls_Reader(path);
	String sheetName= "Data";
	int dataRowNum,columnRowNum,noOfColumns,noOfrows;
	
	int testStartRowNum = 1;
		while(!xls.getCellData(sheetName, 0, testStartRowNum).equals(testName))
		{
			testStartRowNum+=1;
		}
		dataRowNum = testStartRowNum+2;
		columnRowNum = testStartRowNum+1;
	
	//Calculating number of columns
		noOfColumns = 0;
		while(!xls.getCellData(sheetName, noOfColumns,columnRowNum).equals(""))
		{
		noOfColumns++;
		}
	System.out.println("Total bumber of colns" + noOfColumns);
	
	//Calculating number of rows
	noOfrows=0;
		while(!xls.getCellData(sheetName, 0,dataRowNum+noOfrows).equals(""))
		{
			noOfrows += 1;	
		}
	System.out.println("Total no Of rows" + noOfrows);

	
	//Hash table Object
	Object[][] data= new Object[noOfrows][1];
	
		int hashDatarow=0;
		Hashtable<String,String> hashData = null; 
		for (int rows= dataRowNum;rows<dataRowNum+noOfrows ; rows++)
		{
			hashData = new Hashtable<String,String>();
			
			for(int cols=0;cols<noOfColumns;cols++)
			{
				
				hashData.put(xls.getCellData(sheetName, cols, columnRowNum), xls.getCellData(sheetName, cols, rows));
				//System.out.println(xls.getCellData(sheetName, cols, rows));
				//data[datarow][cols]=xls.getCellData(sheetName, cols, rows);
			}
			data[hashDatarow][0] = hashData ;
			hashDatarow++;
		}
	
	return data;
}

public static boolean isRunnable( String tcid , String path ){
	
	Xls_Reader xls = new Xls_Reader(path);
	int tcIdrowNum=1;
	
	while( !xls.getCellData("TestCases", 0, tcIdrowNum).equals(tcid) )
	{
		tcIdrowNum++;
	}
	
	if(xls.getCellData("TestCases", 1, tcIdrowNum).equals("Y"))
		return true;	
	else
		return false;

}



}