package tests;

import java.util.Arrays;
import java.util.Hashtable;
import java.util.Properties;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
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
	private Checkpoints checkpoints = new Checkpoints();
	private String siteURL;
	Hashtable<String, Integer> column;
	
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
	}	
	
	@Parameters({"browser", "dataLocation", "screenshotLocation"})
	@BeforeTest
	private void beforeTest(String browser, String dataLocation, String screenshotLocation)
	{
		String dataSourceName = this.getClass().getSimpleName();
		TestConfiguration.beforeTest(browser, dataLocation, screenshotLocation, dataSourceName);
		
		String[][] site = DataDriver.getData("Site");
		column = DataDriver.getColumnNamesFromSheet("Site");
		siteURL = site[1][column.get("Site URL")];
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
	
	@Test(enabled = true, description="Header and Footer", dataProvider="Iteration")
	public void headerFooter(String rowForIteration, String iterationDescription)
	{			
		int dataRowFromSheet = Integer.parseInt(rowForIteration);	
		Browser.launchSite(siteURL);
		
		prop = Util.getPageProperties("LoginPage");
		
		String[][] headerFooter = DataDriver.getData("LoginHeaderFooter");
		column = DataDriver.getColumnNamesFromSheet("LoginHeaderFooter");
		
		// Check Header and Footer text
		String loginHeaderExpected = headerFooter[dataRowFromSheet][column.get("Header Text")];
		String loginHeaderActual = Browser.driver.findElement(By.className(prop.getProperty("header"))).getText();
		checkpoints.check(loginHeaderExpected, loginHeaderActual, "Login Page Header Text");
		
		String footerExpected = headerFooter[dataRowFromSheet][column.get("Footer Text")];
		String footerActual = Browser.driver.findElement(By.xpath(prop.getProperty("footer"))).getText();
		checkpoints.check(footerExpected, footerActual, "Login Page Footer Text");
		
		checkpoints.assertCheck();
	}
	
	@Test(enabled = true, description="Successful Login", dataProvider="Iteration")
	public void loginSuccessful(String rowForIteration, String iterationDescription)
	{	
		int dataRowFromSheet = Integer.parseInt(rowForIteration);		
		Browser.launchSite(siteURL);
		
		prop = Util.getPageProperties("LoginPage");
		
		String expectedTitle = prop.getProperty("loginPageTitle");
		String actualTitle = Browser.driver.getTitle();
		checkpoints.check(actualTitle, expectedTitle, "Login Page Title");		
		
		String[][] LoginSuccessful = DataDriver.getData("LoginSuccessful");
		column = DataDriver.getColumnNamesFromSheet("LoginSuccessful");
		
		String userIDExpected = LoginSuccessful[dataRowFromSheet][column.get("User ID")];
		String passwordExpected = LoginSuccessful[dataRowFromSheet][column.get("Password")];
		
		String defaultUserIDTextExpected = LoginSuccessful[dataRowFromSheet][column.get("Default User ID Text")];
		String defaultPasswordTextExpected = LoginSuccessful[dataRowFromSheet][column.get("Default Password Text")];
		
		// Check placeholder text in User ID field
		String defaultUserIDTextActual = Browser.driver.findElement(By.id(prop.getProperty("userIDField"))).getAttribute("placeholder");
		checkpoints.check(defaultUserIDTextExpected, defaultUserIDTextActual, "Default User ID Text");
		
		// Check placeholder text in Password field
		String defaultPasswordTextActual = Browser.driver.findElement(By.id(prop.getProperty("passwordField"))).getAttribute("placeholder");
		checkpoints.check(defaultPasswordTextExpected, defaultPasswordTextActual, "Default Password Text");

		// Enter User ID and check text appears in User ID field
		Browser.driver.findElement(By.id(prop.getProperty("userIDField"))).sendKeys(userIDExpected);
		String userIDActual = Browser.driver.findElement(By.id(prop.getProperty("userIDField"))).getAttribute("value");
		checkpoints.check(userIDExpected, userIDActual, "User ID");
				
		// Enter Password and check text appears in Password field
		Browser.driver.findElement(By.id(prop.getProperty("passwordField"))).sendKeys(passwordExpected);
		String passwordActual = Browser.driver.findElement(By.id(prop.getProperty("passwordField"))).getAttribute("value");
		checkpoints.check(passwordExpected, passwordActual, "Password");
		
		// Click Login button
		Browser.driver.findElement(By.className(prop.getProperty("loginButton"))).click();
		Browser.pause(5);
		
		prop = Util.getPageProperties("DashboardPage");
		
		expectedTitle = prop.getProperty("dashboardPageTitle");
		actualTitle = Browser.driver.getTitle();
		checkpoints.check(actualTitle, expectedTitle, "Dashboard Page Title");
		
		checkpoints.assertCheck();
	}
	
	@Test(enabled = true, description="Missing User ID", dataProvider="Iteration")
	public void missingUserID(String rowForIteration, String iterationDescription)
	{			
		int dataRowFromSheet = Integer.parseInt(rowForIteration);		
		Browser.launchSite(siteURL);
		
		prop = Util.getPageProperties("LoginPage");
		
		String[][] MissingUserID = DataDriver.getData("MissingUserID");
		column = DataDriver.getColumnNamesFromSheet("MissingUserID");
		String userIDRequiredExpected = MissingUserID[dataRowFromSheet][column.get("Missing User ID Text")];
		
		Browser.driver.findElement(By.className(prop.getProperty("loginButton"))).click();
		String userIDRequiredActual = Browser.driver.findElement(By.className(prop.getProperty("userIDRequired"))).getText();
		checkpoints.check(userIDRequiredExpected, userIDRequiredActual, "Missing User ID Text");
		
		checkpoints.assertCheck();
	}
	
	@Test(enabled = true, description="Incorrect User ID or Password", dataProvider="Iteration")
	public void incorrectUserIDPassword(String rowForIteration, String iterationDescription)
	{	
		int dataRowFromSheet = Integer.parseInt(rowForIteration);	
		Browser.launchSite(siteURL);
		
		prop = Util.getPageProperties("LoginPage");
		
		String[][] IncorrectUserID = DataDriver.getData("IncorrectUserID");
		column = DataDriver.getColumnNamesFromSheet("IncorrectUserID");
		
		String incorrectUserIDPasswordExpected = IncorrectUserID[dataRowFromSheet][column.get("Incorrect User ID Text")];
		String userID = IncorrectUserID[dataRowFromSheet][column.get("User ID")];
		
		Browser.driver.findElement(By.id(prop.getProperty("userIDField"))).sendKeys(userID);
		Browser.driver.findElement(By.className(prop.getProperty("loginButton"))).click();
		Browser.pause(5);
		
		String incorrectUserIDPasswordActual = Browser.driver.findElement(By.xpath(prop.getProperty("incorrectUserIDPassword"))).getText();
		checkpoints.check(incorrectUserIDPasswordExpected, incorrectUserIDPasswordActual, "Incorrect User ID or Password Text");
		
		checkpoints.assertCheck();
	}

	@Test(enabled = true, description="Forgot Password", dataProvider="Iteration")
	public void forgotPasswordLink(String rowForIteration, String iterationDescription)
	{	
		int dataRowFromSheet = Integer.parseInt(rowForIteration);		
		Browser.launchSite(siteURL);
		
		prop = Util.getPageProperties("ForgotPasswordPage");
		
		WebElement forgotPasswordLink = Browser.driver.findElements(By.tagName("a")).get(1);
		JavascriptExecutor exec = (JavascriptExecutor)Browser.driver;
		exec.executeScript("arguments[0].click()", forgotPasswordLink);
		Browser.pause(5);
		
		String expectedTitle = prop.getProperty("forgotPasswordPageTitle");
		String actualTitle = Browser.driver.getTitle();
		checkpoints.check(actualTitle, expectedTitle, "Forgot Password Page Title");
		
		String[][] headerFooter = DataDriver.getData("ForgotPasswordHeaderFooter");
		column = DataDriver.getColumnNamesFromSheet("ForgotPasswordHeaderFooter");
		
		// Check Header and Footer text
		String forgotPasswordHeaderExpected1 = headerFooter[dataRowFromSheet][column.get("Header Text 1")];
		String forgotPasswordHeaderActual1 = Browser.driver.findElement(By.className(prop.getProperty("header1"))).getText();
		checkpoints.check(forgotPasswordHeaderExpected1, forgotPasswordHeaderActual1, "Forgot Password Page Header 1 Text");
		
		String forgotPasswordHeaderExpected2 = headerFooter[dataRowFromSheet][column.get("Header Text 2")];
		String forgotPasswordHeaderActual2 = Browser.driver.findElement(By.xpath(prop.getProperty("header2"))).getText();
		checkpoints.check(forgotPasswordHeaderExpected2, forgotPasswordHeaderActual2, "Forgot Password Page Header 2 Text");
		
		String forgotPasswordFooterExpected = headerFooter[dataRowFromSheet][column.get("Footer Text")];
		String forgotPasswordFooterActual = Browser.driver.findElement(By.xpath(prop.getProperty("footer"))).getText();
		checkpoints.check(forgotPasswordFooterExpected, forgotPasswordFooterActual, "Forgot Password Page Footer Text");
		
		checkpoints.assertCheck();
	}	
}