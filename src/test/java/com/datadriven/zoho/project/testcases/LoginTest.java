package com.datadriven.zoho.project.testcases;

import java.io.IOException;
import java.util.Hashtable;

import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.datadriven.zoho.project.base.BaseTest;
import com.relevantcodes.extentreports.LogStatus;
import com.datadriven.zoho.project.util.DataUtil;

public class LoginTest extends BaseTest
{
	
	public String testCaseName = "LoginTest";
	SoftAssert soft=null;

	@Test(dataProvider="getData")
	public void doLoginTest(Hashtable<String, String> testdata) throws IOException
	{
		test = ext.startTest("LoginTest");
		test.log(LogStatus.INFO, "Starting the Login test");
		test.log(LogStatus.INFO, "Test Data is" + String.valueOf(testdata));
		
	if ( !DataUtil.isRunnable(testCaseName, prop.getProperty("testDatafolderPath"))|| testdata.get("Runmode").equals("N"))
		{
		test.log(LogStatus.SKIP, "Skipping the test case");
		throw new SkipException("Skipping the test case as run mode is N"); 
		}
	
	System.out.println(testdata.get("Browser"));
	
	openBrowser(testdata.get("Browser"));
	navigate("appUrl");
	boolean actualResult = doLogin(testdata.get("Username"), testdata.get("Password"),testdata.get("Browser"));
	boolean expectedResult = false;
	
	if(testdata.get("ExpectedResult").equals("Y"))
		{
		expectedResult=true;
		}
	else
		{
		expectedResult=false;
		}
	
	if( String.valueOf(expectedResult).equals(String.valueOf(actualResult)) ){
		reportPass("Login test is succcessfully completed");
	}
	else
		reportFail("Login test is failed");
		}

	@BeforeMethod
	public void init() throws IOException
		{
			System.out.println("Before method entered");
			soft = new SoftAssert();	
		}
	
	@AfterMethod
		public void aftertest()
		{
			try{
				soft.assertAll();
				}
			catch(Error e)
				{
				test.log(LogStatus.FAIL, e.getMessage());
				}
			System.out.println("After method entered");
			ext.endTest(test);
			ext.flush();
			if(driver!= null){
				driver.quit();
			}
			
		}
	
	@DataProvider
	public Object[][] getData() throws IOException
		{		
		
		System.out.println("Executing Data provider entered");
			super.init();	
			return DataUtil.getTestData(testCaseName, prop.getProperty("testDatafolderPath"));
		}
}