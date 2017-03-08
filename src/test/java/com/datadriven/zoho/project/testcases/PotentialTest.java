package com.datadriven.zoho.project.testcases;

import java.io.IOException;
import java.text.ParseException;
import java.util.Hashtable;

import org.openqa.selenium.By;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.datadriven.zoho.project.base.BaseTest;
import com.datadriven.zoho.project.util.DataUtil;
import com.relevantcodes.extentreports.LogStatus;

public class PotentialTest extends BaseTest {

	SoftAssert soft = null;
	
	
	
	@Test(priority =1,dataProvider="getData")
	public void createPotentialTest(Hashtable<String, String> testdata) throws IOException, ParseException{
		test = ext.startTest("Starting Create Potential test");
		
		if ( !DataUtil.isRunnable("CreatePotentialTest", prop.getProperty("testDatafolderPath"))|| testdata.get("Runmode").equals("N"))
		{
		test.log(LogStatus.SKIP, "Skipping the test case");
		throw new SkipException("Skipping the test case as run mode is N"); 
		}
		
	openBrowser(testdata.get("Browser"));
	navigate("appUrl");
	doLogin(prop.getProperty("DefaultUserName"),prop.getProperty("DefaultPassword"),testdata.get("Browser"));
	click("CrmLink_xpath");
	click("Potentials_tab_xpath");
	click("Create_New_Potential_xpath");
	type("CreateDeal_Name_xpath", testdata.get("PotentialName"));
	click("Create_Potential_Stage_dropdown_xpath");
	String Stageoption_locator_key = 
	prop.getProperty("Create_Potential_stage_option1_xpath")+testdata.get("Stage")+prop.getProperty("Create_Potential_stage_option2_xpath");
	driver.findElement(By.xpath(Stageoption_locator_key)).click();
	click("Create_potential_Closing_Date_xpath");
	selectDate(testdata.get("ClosingDate"));
	type("CreateDeal_AccountName_xpath",testdata.get("AccountName"));
	click("Create_potential_save_button_xpath");
	clickAndWait("Potentials_tab_xpath","Create_New_Potential_xpath");
	
	validateDeal("Qualification", "Ford Motors","Henry");
	}
	
	@Test(priority =2,dependsOnMethods={"createPotentialTest"})
	public void deletePotentialAccountTest(){
		
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
			/*if(driver!= null){
			driver.quit();
			}*/
		}
	
	@DataProvider
	public Object[][] getData() throws IOException
		{		
			super.init();	
			return DataUtil.getTestData("CreatePotentialTest", prop.getProperty("testDatafolderPath"));
		}
		
}
