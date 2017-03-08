package com.datadriven.zoho.project.base;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import bsh.StringUtil;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import com.datadriven.zoho.project.util.ExtentManager;

public class BaseTest {

public WebDriver driver;
public Properties prop;
public ExtentReports ext = ExtentManager.getInstance();
public ExtentTest test;


public void init() throws IOException{

	if (prop==null)
	{
	prop = new Properties();
	FileInputStream fs = new FileInputStream(System.getProperty("user.dir")+"\\src\\test\\resources\\projectconf.properties");
	prop.load(fs);
	}	
	
}

/************Open Browser**************/
public void openBrowser(String bType) throws IOException
{	
	if(bType.equals("Mozilla"))
	{
	test.log(LogStatus.INFO, "Opening the browser "+ bType);
	driver= new FirefoxDriver();
	}
	else if (bType.equals("Chrome"))
	{
	test.log(LogStatus.INFO, "Opening the browser "+ bType);
	System.setProperty("webdriver.chrome.driver", prop.getProperty("ChromedriverPath"));
	driver= new ChromeDriver();
	}
	else if (bType.equals("IE"))
	{
	test.log(LogStatus.INFO, "Opening the browser "+ bType);
	System.setProperty("webdriver.chrome.driver", prop.getProperty("IEdriverPath"));
	driver= new InternetExplorerDriver();
	}
	driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS );
	driver.manage().window().maximize();
	test.log(LogStatus.INFO, "Browser opened successfully "+ bType);
}


public WebElement getElement(String locatorKey){
	WebElement e = null;
	try{
		if(locatorKey.endsWith("_xpath"))
			{
		e= driver.findElement(By.xpath(prop.getProperty(locatorKey)));
			}
		else if (locatorKey.endsWith("_id"))
			{
			e= driver.findElement(By.id(prop.getProperty(locatorKey)));
			}
		else if (locatorKey.endsWith("name"))
			{
			e= driver.findElement(By.id(prop.getProperty(locatorKey)));
			}
		else 
			{ 
			reportFail("Locator Not found" + locatorKey );
			Assert.fail("Locator not found" + locatorKey );
			}
		
		}catch(Exception ex)
			{
			//Fail the test case and print the message
			reportFail(ex.getMessage());
			ex.printStackTrace();
			Assert.fail("Failed Test case" + ex.getMessage());
			}
	return e;	
		}

/*****************Navigate**************/
public void navigate(String url)
{
test.log(LogStatus.INFO, "Navigating to "+ url);	
driver.get(prop.getProperty(url));
test.log(LogStatus.INFO, "Successfully navigated to "+ url);
}

/***************Click() function**********/
//Clicking on the element
public void click (String xpathElementKey)
{
	test.log(LogStatus.INFO, "Clicking on "+ xpathElementKey);
	getElement(xpathElementKey).click();
}

/*************type() function **************/
//Entering some text in the text field
public void type(String xpathElementKey,String data)
{
	test.log(LogStatus.INFO, "typing in element "+ xpathElementKey + "Data is " + data);
	getElement(xpathElementKey).sendKeys(data);
	test.log(LogStatus.INFO, "typed successfully in element "+ xpathElementKey);
}
	

/************************Select () function*****************/
//Selecting a value from a drop down

public void select(String xpathElementKey, String option){
	
	WebElement droplist = getElement(xpathElementKey);
	Select S = new Select(droplist);
	test.log(LogStatus.INFO, "Trying to select the "+ option + "from Stage dropdown" );
	S.selectByValue(option);
	
}


/*********************Validations**************/
	public boolean verifyText(String textkey,String TextlocatorKey){
		String actualtext = getElement(TextlocatorKey).getText().trim();
		String expectedtext = prop.getProperty(textkey);
		
		if(actualtext.equals(expectedtext))
			return true;
		else					
			return false;
		}
	
	public boolean isElementPresent(String locatorKey){
		List<WebElement> elementList = null;

		try{
			if(locatorKey.endsWith("_xpath"))
				{
				elementList= driver.findElements(By.xpath(prop.getProperty(locatorKey)));
				}
			else if (locatorKey.endsWith("_id"))
				{
				elementList= driver.findElements(By.id(prop.getProperty(locatorKey)));
				}
			else if (locatorKey.endsWith("name"))
				{
				elementList= driver.findElements(By.id(prop.getProperty(locatorKey)));
				}
			else 
				{ 
				//reportFail("Locator Not found" + locatorKey );
				//Assert.fail("Locator not found" + locatorKey );
				}
		}
		catch(Exception e){
			//reportFail("Element is not present");
			}
		
			if(elementList.size()==0)
				return false;
				else
					return true;
	}
	
	public void clickAndWait(String clickElementXpath, String searchElementXpath )
	{
		test.log(LogStatus.INFO, "Clicking and waiting on element"+clickElementXpath);
		getElement(clickElementXpath).click();
		int count=5;
		for(int i=0;i<count;i++){
			getElement(clickElementXpath).click();
			wait(2);
			if(isElementPresent(searchElementXpath))
				break;
		}
	}
		
	public void AcceptAlert(){
		WebDriverWait wait = new WebDriverWait(driver, 5);
		wait.until(ExpectedConditions.alertIsPresent());
			Alert al = driver.switchTo().alert();
			al.accept();
			driver.switchTo().defaultContent();
				}
	
/**************reporting*******************/
	public void reportPass(String msg){
		test.log(LogStatus.PASS, msg);
		
	}
	public void reportFail(String msg){
		test.log(LogStatus.FAIL, msg);
		takeScreenshot();
		Assert.fail(msg);
	}
	
	public void takeScreenshot(){
		
		Date da = new Date();
		SimpleDateFormat m = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		String filename = m.format(da);
		filename = filename.replace(" ", "_").replace(":", "_").replace("/","_")+".png";

		File srcShot = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		try {
			FileUtils.copyFile(srcShot, new File(System.getProperty("user.dir")+"\\ScreenShots\\"+filename));
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		test.log(LogStatus.INFO, "ScreenShot"+ test.addScreenCapture(System.getProperty("user.dir")+"\\ScreenShots"+filename));
	}

	public void waitforPageToLoad()
	{
		JavascriptExecutor js = (JavascriptExecutor) driver;
		String state = (String)js.executeScript("return document.readyState");
		  String title=(String)js.executeScript("return document.title");
		  System.out.println("title  : "+title);
		
		System.out.print(state);
		
		while(! StringUtils.equals(state, "complete"))
		{
			wait(1);
			state = (String)js.executeScript("return document.readystate");
			System.out.print(state);
		}
	}

	public void wait(int TimeTowaitInSec)
	{
		try {
			Thread.sleep(TimeTowaitInSec*1000);
			} 
		catch (InterruptedException e) 
			{
				e.printStackTrace();
			}
	}
	
/**************************Appfunctions**************************/

	
	
	public boolean doLogin(String username, String password, String browserType){
	
		test.log(LogStatus.INFO, "Trying to login to application");
		click("Login_xpath");
		wait(3);
		waitforPageToLoad();
		
		if(browserType.equals("Chrome")){
			driver.switchTo().frame(1);	
		}
		else{
			driver.switchTo().frame(0);
		}
		getElement("Login_email_xpath").sendKeys(username);
		getElement("Login_password_xpath").sendKeys(password);
		click("Login_submit_xpath");
		
		if( isElementPresent("CrmLink_xpath"))
		{
			test.log(LogStatus.INFO, "Login to application is successful");
			return true;
		}
		else
		{
			test.log(LogStatus.INFO, "Login to application is Failed");
			return true;
		}
	}

	public int getLeadRownum(String name){
		
		test.log(LogStatus.INFO, "Searching for lead name");
		List<WebElement> leads = driver.findElements(By.xpath(prop.getProperty("LeadNames_xpath")));
		 	
		System.out.println(name);
		
		int leadNo=0;
		for(int i=0 ; i< leads.size(); i++)
		{
			test.log(LogStatus.INFO, "Searching for and lead name found was "+ leads.get(i).getText().trim());
			if( leads.get(i).getText().trim().equals(name.trim()))
			{
				leadNo= i+1;
				test.log(LogStatus.INFO, "Lead is found at row"+ leadNo );
				return leadNo;
			}
		}	
		test.log(LogStatus.INFO, "Lead is not found");
		return -1 ;
	}
	
	public void clickLeadname(String leadname){
	int leadPosition;
	leadPosition = getLeadRownum(leadname);
	String leadnameXpath = prop.getProperty("LeadNamePart1")+leadPosition+prop.getProperty("LeadNamePart2");
	driver.findElement(By.xpath(leadnameXpath)).click();
		
	}
	
public void selectDate(String d) throws ParseException{
	
	String appmonth,appyear,appdate,calMonYear,SelectMonYear;
	String calenderNaviageteXpath,calederDateXpath;
	
	
	SimpleDateFormat m = new SimpleDateFormat("MM/dd/yyyy");
	Date applicationdate = m.parse(d);
	Date presentdate = new Date();
		
	m = new SimpleDateFormat("MMMM");
	appmonth =  m.format(applicationdate);
	System.out.println(appmonth);
	
	m = new SimpleDateFormat("yyyy");
	appyear =  m.format(applicationdate);
	
	m=new SimpleDateFormat("d");
	appdate =  m.format(applicationdate);

	SelectMonYear = appmonth+ " " + appyear ;
	calMonYear = getElement("Create_potential_cal_MonthYear_xpath").getText().trim();
	
	//Finding the navigate button xpath(front/back button) in the calender by comapring the present date with date need to be selected 
	//in the application
	
	if(presentdate.before(applicationdate)){
		calenderNaviageteXpath = "Create_potential_cal_navigate_front_xpath"; 
	}
	else
		calenderNaviageteXpath= "Create_potential_cal_navigate_back_xpath";
	
	//Finding out the date element xpath in the calender control
	calederDateXpath =
	prop.getProperty("Create_potential_Cal_date_part1_xpath")+appdate+prop.getProperty("Create_potential_Cal_date_part2_xpath");
	
	int counter=1;
	
	if(StringUtils.equals(SelectMonYear, calMonYear)){
		counter = 0;
	}
	
while (counter != 0)
	{
		test.log(LogStatus.INFO, "Navigating the calender control to match the calender date to date that needs to be selected");
		click(calenderNaviageteXpath);
		calMonYear = getElement("Create_potential_cal_MonthYear_xpath").getText().trim();
		
		if(StringUtils.equals(SelectMonYear, calMonYear)){
			counter = 0;
			break;
		}
	}

	if(counter == 0)
	{
		test.log(LogStatus.INFO, "Calender date and moths are selected");
		driver.findElement(By.xpath(calederDateXpath)).click();
	}
	else{
		reportFail("There is some issue with the date given in the test date");
	}
}

public void validateDeal(String Stage, String accountname, String potentialName){
	String dealaccount_Validator_xpath="";
	String dealpotenial_validator_xpath="";
	
	
	if (StringUtils.equals(Stage, "Qualification")){
		
	dealaccount_Validator_xpath = "//div[@id='stageContainer']/div[1]/div[2]//a[text()='"+accountname+"']" ;
	dealpotenial_validator_xpath = "//div[@id='stageContainer']/div[1]/div[2]//a[text()='"+potentialName+"']" ;

	}
	
	if( driver.findElement(By.xpath(dealaccount_Validator_xpath)).getText().trim().equals(accountname)){
		test.log(LogStatus.INFO, "Deal account name is successfully found");
	}
	else{
		test.log(LogStatus.FAIL, "Deal account name is not found");
		takeScreenshot();
	}
	
	if( driver.findElement(By.xpath(dealpotenial_validator_xpath)).getText().trim().equals(potentialName)){
		test.log(LogStatus.PASS, "Deal potential name is successfully found");
	}
	else{
		test.log(LogStatus.FAIL, "Deal potential name is not found");
		takeScreenshot();
	}
	
	
}

}