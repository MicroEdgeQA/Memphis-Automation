package tests;

import java.util.Arrays;
import java.util.Hashtable;
import java.util.Properties;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import common.Browser;
import common.Checkpoints;
import common.DataDriver;
import common.Util;
import common.WebMail;

public class Login
{		
	private String siteURL;
	private Properties prop;
	private WebDriverWait wait;
	private Checkpoints checkpoints = new Checkpoints();
	
	String [][] site;
	Hashtable<String, Integer> siteColumn;
	
	String[][] loginHeaderFooter;
	Hashtable<String, Integer> loginHeaderFooterColumn;
	
	String[][] loginSuccessful;
	Hashtable<String, Integer> loginSuccessfulColumn;
	
	String[][] missingUserID;
	Hashtable<String, Integer> missingUserIDColumn;
	
	String[][] incorrectUserID;
	Hashtable<String, Integer> incorrectUserIDColumn;
	
	String[][] forgotPasswordHeaderFooter;
	Hashtable<String, Integer> forgotPasswordHeaderFooterColumn;
	
	String[][] forgotPasswordEmail;
	Hashtable<String, Integer> forgotPasswordEmailColumn;
	
	private void getData()
	{
		site = DataDriver.getData("Site");
		siteColumn = DataDriver.getColumnNamesFromSheet("Site");
		
		loginHeaderFooter = DataDriver.getData("LoginHeaderFooter");
		loginHeaderFooterColumn = DataDriver.getColumnNamesFromSheet("LoginHeaderFooter");
		
		loginSuccessful = DataDriver.getData("LoginSuccessful");
		loginSuccessfulColumn = DataDriver.getColumnNamesFromSheet("LoginSuccessful");
		
		missingUserID = DataDriver.getData("MissingUserID");
		missingUserIDColumn = DataDriver.getColumnNamesFromSheet("MissingUserID");
		
		incorrectUserID = DataDriver.getData("IncorrectUserID");
		incorrectUserIDColumn = DataDriver.getColumnNamesFromSheet("IncorrectUserID");
		
		forgotPasswordHeaderFooter = DataDriver.getData("ForgotPasswordHeaderFooter");
		forgotPasswordHeaderFooterColumn = DataDriver.getColumnNamesFromSheet("ForgotPasswordHeaderFooter");
		
		forgotPasswordEmail = DataDriver.getData("ForgotPasswordEmail");
		forgotPasswordEmailColumn = DataDriver.getColumnNamesFromSheet("ForgotPasswordEmail");
	}
		
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
	private void beforeTest(String browser, String dataLocation, String screenshotLocation) throws Exception
	{
		String dataSourceName = this.getClass().getSimpleName();
		Browser.browserConfig(browser, screenshotLocation, dataLocation);
		DataDriver.assignDataSource(dataLocation, dataSourceName);
		getData();
		wait = new WebDriverWait(Browser.driver, 10);
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
		siteURL = site[dataRowFromSheet][siteColumn.get("Site URL")];
		Browser.launchSite(siteURL);
		
		prop = Util.getPageProperties("LoginPage");
		
		// Check Header and Footer text
		String loginHeaderExpected = loginHeaderFooter[dataRowFromSheet][loginHeaderFooterColumn.get("Header")];
		String loginHeaderActual = Browser.driver.findElement(By.className(prop.getProperty("header"))).getText();
		checkpoints.check(loginHeaderExpected, loginHeaderActual, "Login Page Header Text");
		
		String loginFooterExpected = loginHeaderFooter[dataRowFromSheet][loginHeaderFooterColumn.get("Footer")];
		String loginFooterActual = Browser.driver.findElement(By.xpath(prop.getProperty("footer"))).getText();
		checkpoints.check(loginFooterExpected, loginFooterActual, "Login Page Footer Text");
		
		checkpoints.assertCheck();
	}
	
	@Test(enabled = true, description="Successful Login", dataProvider="Iteration")
	public void loginSuccessful(String rowForIteration, String iterationDescription)
	{	
		int dataRowFromSheet = Integer.parseInt(rowForIteration);		
		siteURL = site[dataRowFromSheet][siteColumn.get("Site URL")];
		Browser.launchSite(siteURL);
		
		prop = Util.getPageProperties("LoginPage");
		
		String expectedTitle = prop.getProperty("loginPageTitle");
		String actualTitle = Browser.driver.getTitle();
		checkpoints.check(expectedTitle, actualTitle, "Login Page Title");		
		
		String userIDExpected = loginSuccessful[dataRowFromSheet][loginSuccessfulColumn.get("User ID")];
		String passwordExpected = loginSuccessful[dataRowFromSheet][loginSuccessfulColumn.get("Password")];
		
		String defaultUserIDTextExpected = loginSuccessful[dataRowFromSheet][loginSuccessfulColumn.get("Default User ID Text")];
		String defaultPasswordTextExpected = loginSuccessful[dataRowFromSheet][loginSuccessfulColumn.get("Default Password Text")];
		
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
		Browser.driver.findElement(By.xpath(prop.getProperty("loginButton"))).click();
		WebElement loginImgVisible = wait.until(ExpectedConditions.presenceOfElementLocated(By.className(prop.getProperty("dash-img"))));
		
		prop = Util.getPageProperties("DashboardPage");
		
		expectedTitle = prop.getProperty("dashboardPageTitle");
		actualTitle = Browser.driver.getTitle();
		
		checkpoints.check(expectedTitle, actualTitle, "Dashboard Page Title");
		
		checkpoints.assertCheck();
	}
	
	@Test(enabled = true, description="Missing User ID", dataProvider="Iteration")
	public void missingUserID(String rowForIteration, String iterationDescription)
	{			
		int dataRowFromSheet = Integer.parseInt(rowForIteration);		
		siteURL = site[dataRowFromSheet][siteColumn.get("Site URL")];
		Browser.launchSite(siteURL);
		
		prop = Util.getPageProperties("LoginPage");
		
		String userIDRequiredExpected = missingUserID[dataRowFromSheet][missingUserIDColumn.get("Missing User ID Text")];
		
		Browser.driver.findElement(By.xpath(prop.getProperty("loginButton"))).click();
		String userIDRequiredActual = Browser.driver.findElement(By.className(prop.getProperty("userIDRequired"))).getText();
		checkpoints.check(userIDRequiredExpected, userIDRequiredActual, "Missing User ID Text");
		
		checkpoints.assertCheck();
	}
	
	@Test(enabled = true, description="Incorrect User ID or Password", dataProvider="Iteration")
	public void incorrectUserIDPassword(String rowForIteration, String iterationDescription)
	{	
		int dataRowFromSheet = Integer.parseInt(rowForIteration);	
		siteURL = site[dataRowFromSheet][siteColumn.get("Site URL")];
		Browser.launchSite(siteURL);
		
		prop = Util.getPageProperties("LoginPage");
		
		String incorrectUserIDPasswordExpected = incorrectUserID[dataRowFromSheet][incorrectUserIDColumn.get("Incorrect User ID Text")];
		String userID = incorrectUserID[dataRowFromSheet][incorrectUserIDColumn.get("User ID")];
		
		Browser.driver.findElement(By.id(prop.getProperty("userIDField"))).sendKeys(userID);
		Browser.driver.findElement(By.xpath(prop.getProperty("loginButton"))).click();
		
		WebElement incorrectUserIDPassword = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(prop.getProperty("incorrectUserIDPassword"))));
		
		String incorrectUserIDPasswordActual = Browser.driver.findElement(By.xpath(prop.getProperty("incorrectUserIDPassword"))).getText();
		checkpoints.check(incorrectUserIDPasswordExpected, incorrectUserIDPasswordActual, "Incorrect User ID or Password Text");
		
		checkpoints.assertCheck();
	}

	@Test(enabled = true, description="Forgot Password", dataProvider="Iteration")
	public void forgotPasswordLink(String rowForIteration, String iterationDescription)
	{	
		int dataRowFromSheet = Integer.parseInt(rowForIteration);		
		siteURL = site[dataRowFromSheet][siteColumn.get("Site URL")];
		Browser.launchSite(siteURL);
		
		prop = Util.getPageProperties("ForgotPasswordPage");
		
		WebElement forgotPasswordLink = Browser.driver.findElements(By.tagName("a")).get(1);
		JavascriptExecutor exec = (JavascriptExecutor)Browser.driver;
		exec.executeScript("arguments[0].click()", forgotPasswordLink);
		WebElement emailFieldVisible = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(prop.getProperty("emailField"))));
		
		String expectedTitle = prop.getProperty("forgotPasswordPageTitle");
		String actualTitle = Browser.driver.getTitle();
		checkpoints.check(expectedTitle, actualTitle, "Forgot Password Page Title");
		
		// Check Header and Footer text
		String forgotPasswordHeaderExpected1 = forgotPasswordHeaderFooter[dataRowFromSheet][forgotPasswordHeaderFooterColumn.get("Header Text 1")];
		String forgotPasswordHeaderActual1 = Browser.driver.findElement(By.className(prop.getProperty("header1"))).getText();
		checkpoints.check(forgotPasswordHeaderExpected1, forgotPasswordHeaderActual1, "Forgot Password Page Header 1 Text");
		
		String forgotPasswordHeaderExpected2 = forgotPasswordHeaderFooter[dataRowFromSheet][forgotPasswordHeaderFooterColumn.get("Header Text 2")];
		String forgotPasswordHeaderActual2 = Browser.driver.findElement(By.xpath(prop.getProperty("header2"))).getText();
		checkpoints.check(forgotPasswordHeaderExpected2, forgotPasswordHeaderActual2, "Forgot Password Page Header 2 Text");
		
		String forgotPasswordFooterExpected = forgotPasswordHeaderFooter[dataRowFromSheet][forgotPasswordHeaderFooterColumn.get("Footer Text")];
		String forgotPasswordFooterActual = Browser.driver.findElement(By.xpath(prop.getProperty("footer"))).getText();
		checkpoints.check(forgotPasswordFooterExpected, forgotPasswordFooterActual, "Forgot Password Page Footer Text");
		
		// Check placeholder text in E-Mail Address field
		String defaultEmailAddressTextExpected = forgotPasswordEmail[dataRowFromSheet][forgotPasswordEmailColumn.get("Default E-Mail Address Text")];
		String defaultEmailAddressTextActual = Browser.driver.findElement(By.id(prop.getProperty("emailField"))).getAttribute("placeholder");
		checkpoints.check(defaultEmailAddressTextExpected, defaultEmailAddressTextActual, "Default E-Mail Address Text");
		
		/* Send e-mail address for forgotten password
		String forgotPasswordEmailExpected = forgotPasswordEmail[dataRowFromSheet][forgotPasswordEmailColumn.get("E-Mail Address")];
		Browser.driver.findElement(By.id(prop.getProperty("emailField"))).sendKeys(forgotPasswordEmailExpected);
		String forgotPasswordEmailActual = Browser.driver.findElement(By.id(prop.getProperty("emailField"))).getAttribute("value");
		checkpoints.check(forgotPasswordEmailExpected, forgotPasswordEmailActual, "Forgot Password E-Mail Address");
		Browser.driver.findElement(By.className(prop.getProperty("sendButton"))).click();
		
		WebElement passwordSent = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(prop.getProperty("okayButton"))));
		
		// Click header on page after password is sent
		String passwordSentHeaderExpected = forgotPasswordEmail[dataRowFromSheet][forgotPasswordEmailColumn.get("Password Sent Header Text")];
		String passwordSentHeaderActual = Browser.driver.findElement(By.xpath(prop.getProperty("passwordSentHeader"))).getText();
		checkpoints.check(passwordSentHeaderExpected, passwordSentHeaderActual, "Password Sent Header Text");
		
		// Click OK button to return to login page
		WebElement okayButton = Browser.driver.findElement(By.xpath((prop.getProperty("okayButton"))));
		exec.executeScript("arguments[0].click()", okayButton);
		prop = Util.getPageProperties("LoginPage");
		WebElement loginPage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(prop.getProperty("loginButton"))));
		
		// Log into e-mail account and confirm e-mail received
		String provider = "gmail";
		WebMail.launchWebMail(provider);
		emailFieldVisible = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("Email")));
		
		String emailSubjectTextExpected = forgotPasswordEmail[dataRowFromSheet][forgotPasswordEmailColumn.get("Forgot Password E-Mail Subject Text")];
		WebMail.webMailLogin(provider, "me.automaton", "microedge123");
		WebMail.webMailOpenMessage(provider, emailSubjectTextExpected);
		
		// Check text of e-mail
		String emailBodyTextExpected = forgotPasswordEmail[dataRowFromSheet][forgotPasswordEmailColumn.get("Forgot Password E-Mail Body Text")];
		String emailBodyTextActual = WebMail.webMailReadEmail(provider);
		checkpoints.check(emailBodyTextExpected, emailBodyTextActual, "Forgot Password E-Mail Body Text");
		WebMail.webMailLogout(provider); */
		
		checkpoints.assertCheck();
	}	
}