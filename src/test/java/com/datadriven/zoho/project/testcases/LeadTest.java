package com.datadriven.zoho.project.testcases;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Set;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.datadriven.zoho.project.base.BaseTest;
import com.datadriven.zoho.project.util.DataUtil;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

public class LeadTest extends BaseTest {

	SoftAssert soft=null;
	
	/************** TEST1 *************************/
	@Test(priority=1,dataProvider="getData")
	public void createLeadtest(Hashtable<String, String> testdata) throws IOException{
		test = ext.startTest(" Starting LeadTest");
		
		if ( !DataUtil.isRunnable("CreateLeadTest", prop.getProperty("testDatafolderPath"))|| testdata.get("Runmode").equals("N"))
		{
		test.log(LogStatus.SKIP, "Skipping the test case");
		throw new SkipException("Skipping the test case as run mode is N"); 
		}		
		
		openBrowser(testdata.get("Browser"));
		navigate("appUrl");
		doLogin(prop.getProperty("DefaultUserName"),prop.getProperty("DefaultPassword"),testdata.get("Browser"));
		click("CrmLink_xpath");
		click("Leads_xpath");
		click("Create_lead_xpath");
		
		type("CreateLead_Company_xpath", testdata.get("LeadCompany"));
		type("CreateLead_LastName_xpath", testdata.get("LeadLastName"));
		
		WebElement element = getElement("CreateLead_Save_xpath"); 
		JavascriptExecutor executor = (JavascriptExecutor)driver;
		executor.executeScript("arguments[0].click();", element);
		
		wait(2);
		takeScreenshot();
		clickAndWait("Leads_xpath", "LeadTab_importButton_xpath");
		takeScreenshot();

		int leadPosition = getLeadRownum( testdata.get("LeadLastName") );
		
		takeScreenshot();
		System.out.println("Lead position is " + leadPosition );
		if(leadPosition > 0){
			reportPass("Lead name " + testdata.get("LeadLastName")+ "is found");
		}
		else{
			reportFail("Lead name " + testdata.get("LeadLastName")+ "is not found");
		}
	}
	
	@Test(priority=2,dataProvider="getData")
	public void convertLeadTest(Hashtable<String, String> testdata ) throws IOException{
	test = ext.startTest(" Starting ConvertTest");
	
	if ( !DataUtil.isRunnable("ConvertLeadTest", prop.getProperty("testDatafolderPath"))|| testdata.get("Runmode").equals("N"))
	{
	test.log(LogStatus.SKIP, "Skipping the test case");
	throw new SkipException("Skipping the test case as run mode is N"); 
	}
	
	openBrowser(testdata.get("Browser"));
	navigate("appUrl");
	doLogin(prop.getProperty("DefaultUserName"),prop.getProperty("DefaultPassword"),testdata.get("Browser"));
	click("CrmLink_xpath");
	click("Leads_xpath");
	clickLeadname(testdata.get("LeadLastName")); 
	click("Convert_button_xpath");
	click("Lead_Convert_save_xpath");
	
	/*	click("LeadTab_ConvertLead_span_xpath");
	click("LeadTab_MassConvert_xpath");
	*/
	/*Set<String> winIds = driver.getWindowHandles();
	System.out.println(winIds.size());
	*/
	//isAlertPresent();
	}
	
	@Test(priority=3,dataProvider="deleteLeadData")
	public void DeleteLeadAccountTest(Hashtable<String, String> testdata) throws IOException{
		
		test = ext.startTest(" Starting deleteLead");
		
		if ( !DataUtil.isRunnable("DeleteLeadAccountTest", prop.getProperty("testDatafolderPath"))|| testdata.get("Runmode").equals("N"))
		{
		test.log(LogStatus.SKIP, "Skipping the test case");
		throw new SkipException("Skipping the test case as run mode is N"); 
		}
		
		openBrowser(testdata.get("Browser"));
		navigate("appUrl");
		doLogin(prop.getProperty("DefaultUserName"),prop.getProperty("DefaultPassword"),testdata.get("Browser"));
		click("CrmLink_xpath");
		click("Leads_xpath");
		clickLeadname(testdata.get("LeadLastName"));
		waitforPageToLoad();
		click("Lead_delete_span_xpath");
		click("Lead_delete_button_xpath");
		wait(1);
		click("Lead_delete_prompt_xpath");
		//AcceptAlert();
		wait(3);
		click("Leads_xpath");
		int leadrowNum = getLeadRownum(testdata.get("LeadLastName"));
		
		if (leadrowNum != -1){
			reportFail("could not delete the lead");
		}
		else
			reportPass("Lead was deleted successfully");
	}
	
	@BeforeMethod
	public void init() throws IOException
		{	
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
			if(ext != null){
			ext.endTest(test);
			ext.flush();
			}
			if(driver!= null){
			driver.quit();
			}
		}
	
	@DataProvider
	public Object[][] getData() throws IOException
		{		
			super.init();	
			return DataUtil.getTestData("CreateLeadTest", prop.getProperty("testDatafolderPath"));
		}
	@DataProvider
	public Object[][] deleteLeadData() throws IOException{
		super.init();
		return DataUtil.getTestData("DeleteLeadAccountTest", prop.getProperty("testDatafolderPath"));
	}
	
}
