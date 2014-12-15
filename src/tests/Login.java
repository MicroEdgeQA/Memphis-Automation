package tests;

import java.util.Arrays;
import java.util.Hashtable;
import java.util.Properties;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import common.Browser;
import common.DataDriver;
import common.TestConfiguration;
import common.Checkpoints;
import common.Util;

public class Login
{		
	private Properties prop;
		
	@AfterClass
	public void afterClass()
	{
		Browser.driver.quit();
	}
	
	@BeforeMethod
	public void beforeMethod()
	{
		// Make sure page is ready
		Assert.assertEquals(Util.IsDOMReady(Browser.driver), true);
		
		// Reset check variables
		Checkpoints.testPassed = true;
	}
	
	@Parameters({"browser", "dataLocation", "screenshotLocation"})
	@BeforeTest
	private void beforeTest(String browser, String dataLocation, String screenshotLocation)
	{
		String dataSourceName = this.getClass().getSimpleName();
		TestConfiguration.beforeTest(browser, dataLocation, screenshotLocation, dataSourceName);
	}
	
	@DataProvider(name="Iteration")
	private Object[][] dataRowFromSheetForIteration() throws Exception
	{
	    // Gets the iteration number and description of the iteration from the data sheet
		// This controls which row of the data sheets is used for the test in progress
		Object[][] rowForIterationWithColumnNames = DataDriver.getData("Iteration");
		Object[][] rowForIteration = Arrays.copyOfRange(rowForIterationWithColumnNames, 1, rowForIterationWithColumnNames.length);
	    return(rowForIteration);
	}
	
	@Test(enabled = true, description="Successful Login", dataProvider="Iteration")
	public void loginSuccessful(String rowForIteration, String iterationDescription)
	{	
		Hashtable<String, Integer> column;
		int dataRowFromSheet = Integer.parseInt(rowForIteration);
		
		String[][] Site = DataDriver.getData("Site");
		column = DataDriver.getColumnNamesFromSheet("Site");
		
		String siteURL = Site[dataRowFromSheet][column.get("Site URL")];		
		Browser.launchSite(siteURL);
		
		prop = Util.getPageProperties("LoginPage");
		
		String expectedTitle = prop.getProperty("loginPageTitle");
		String actualTitle = Browser.driver.getTitle();
		Checkpoints.check(actualTitle, expectedTitle, "Login Page Title");		
		
		String[][] LoginSuccessful = DataDriver.getData("LoginSuccessful");
		column = DataDriver.getColumnNamesFromSheet("LoginSuccessful");
		
		String userIDExpected = LoginSuccessful[dataRowFromSheet][column.get("User ID")];
		String passwordExpected = LoginSuccessful[dataRowFromSheet][column.get("Password")];
		
		String defaultUserIDTextExpected = LoginSuccessful[dataRowFromSheet][column.get("Default User ID Text")];
		String defaultPasswordTextExpected = LoginSuccessful[dataRowFromSheet][column.get("Default Password Text")];
		
		// Check placeholder text in User ID field
		String defaultUserIDTextActual = Browser.driver.findElement(By.id(prop.getProperty("userIDField"))).getAttribute("placeholder");
		Checkpoints.check(defaultUserIDTextExpected, defaultUserIDTextActual, "Default User ID Text");
		
		// Check placeholder text in Password field
		String defaultPasswordTextActual = Browser.driver.findElement(By.id(prop.getProperty("passwordField"))).getAttribute("placeholder");
		Checkpoints.check(defaultPasswordTextExpected, defaultPasswordTextActual, "Default Password Text");

		// Enter User ID and check text appears in User ID field
		Browser.driver.findElement(By.id(prop.getProperty("userIDField"))).sendKeys(userIDExpected);
		String userIDActual = Browser.driver.findElement(By.id(prop.getProperty("userIDField"))).getAttribute("value");
		Checkpoints.check(userIDExpected, userIDActual, "User ID");
				
		// Enter Password and check text appears in Password field
		Browser.driver.findElement(By.id(prop.getProperty("passwordField"))).sendKeys(passwordExpected);
		String passwordActual = Browser.driver.findElement(By.id(prop.getProperty("passwordField"))).getAttribute("value");
		Checkpoints.check(passwordExpected, passwordActual, "Password");
		
		// Click Login button
		Browser.driver.findElement(By.className(prop.getProperty("loginButton"))).click();
		
		Checkpoints.failureHandler();
	}
	
	@Test(enabled = true, description="Missing User ID", dataProvider="Iteration")
	public void missingUserID(String rowForIteration, String iterationDescription)
	{			
		Hashtable<String, Integer> column;
		int dataRowFromSheet = Integer.parseInt(rowForIteration);
		
		String[][] Site = DataDriver.getData("Site");
		column = DataDriver.getColumnNamesFromSheet("Site");
		String siteURL = Site[dataRowFromSheet][column.get("Site URL")];		
		Browser.launchSite(siteURL);
		
		String[][] MissingUserID = DataDriver.getData("MissingUserID");
		column = DataDriver.getColumnNamesFromSheet("MissingUserID");
		String userIDRequiredExpected = MissingUserID[dataRowFromSheet][column.get("Missing User ID Text")];
		
		Browser.driver.findElement(By.className(prop.getProperty("loginButton"))).click();
		String userIDRequiredActual = Browser.driver.findElement(By.className(prop.getProperty("userIDRequired"))).getText();
		Checkpoints.check(userIDRequiredExpected, userIDRequiredActual, "Missing User ID Text");
		
		Checkpoints.failureHandler();
	}
	
	@Test(enabled = true, description="Incorrect User ID or Password", dataProvider="Iteration")
	public void incorrectUserIDPassword(String rowForIteration, String iterationDescription)
	{	
		Hashtable<String, Integer> column;
		int dataRowFromSheet = Integer.parseInt(rowForIteration);
		
		String[][] Site = DataDriver.getData("Site");
		column = DataDriver.getColumnNamesFromSheet("Site");
		
		String siteURL = Site[dataRowFromSheet][column.get("Site URL")];		
		Browser.launchSite(siteURL);
		
		String[][] IncorrectUserID = DataDriver.getData("IncorrectUserID");
		column = DataDriver.getColumnNamesFromSheet("IncorrectUserID");
		
		String incorrectUserIDPasswordExpected = IncorrectUserID[dataRowFromSheet][column.get("Incorrect User ID Text")];
		String userID = IncorrectUserID[dataRowFromSheet][column.get("User ID")];
		
		Browser.driver.findElement(By.id(prop.getProperty("userIDField"))).sendKeys(userID);
		Browser.driver.findElement(By.className(prop.getProperty("loginButton"))).click();
		Browser.pause(5);
		
		String incorrectUserIDPasswordActual = Browser.driver.findElement(By.xpath(prop.getProperty("incorrectUserIDPassword"))).getText();
		Checkpoints.check(incorrectUserIDPasswordExpected, incorrectUserIDPasswordActual, "Incorrect User ID or Password Text");
		
		Checkpoints.failureHandler();
	}

	@Test(enabled = true, description="Forgot Password", dataProvider="Iteration")
	public void forgotPasswordLink(String rowForIteration, String iterationDescription)
	{
		Hashtable<String, Integer> column;
		int dataRowFromSheet = Integer.parseInt(rowForIteration);
		
		String[][] Site = DataDriver.getData("Site");
		column = DataDriver.getColumnNamesFromSheet("Site");
		String siteURL = Site[dataRowFromSheet][column.get("Site URL")];		
		Browser.launchSite(siteURL);
		
		prop = Util.getPageProperties("ForgotPasswordPage");
		
		WebElement forgotPasswordLink = Browser.driver.findElements(By.tagName("a")).get(1);
		JavascriptExecutor exec = (JavascriptExecutor)Browser.driver;
		exec.executeScript("arguments[0].click()", forgotPasswordLink);
		Browser.pause(5);
		
		String expectedTitle = prop.getProperty("forgotPasswordPageTitle");
		String actualTitle = Browser.driver.getTitle();
		Checkpoints.check(actualTitle, expectedTitle, "Forgot Password Page Title");
		
		Checkpoints.failureHandler();
	}	
}