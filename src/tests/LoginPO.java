package tests;

import java.util.Arrays;
import java.util.HashMap;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import properties.ForgotPasswordPage;
import properties.LoginPage;
import common.Browser;
import common.Checkpoints;
import common.DataDriver;
import common.Util;

public class LoginPO
{		
	private String siteURL;
	private WebDriverWait wait;
	private Checkpoints checkpoints = new Checkpoints();
	
	LoginPage loginPage = new LoginPage();
	ForgotPasswordPage forgotPasswordPage = new ForgotPasswordPage();
	
	String [][] site;
	HashMap<String, Integer> siteColumn;
	
	String[][] loginHeaderFooter;
	HashMap<String, Integer> loginHeaderFooterColumn;
	
	String[][] loginSuccessful;
	HashMap<String, Integer> loginSuccessfulColumn;
	
	String[][] missingUserID;
	HashMap<String, Integer> missingUserIDColumn;
	
	String[][] incorrectUserID;
	HashMap<String, Integer> incorrectUserIDColumn;
	
	String[][] forgotPasswordHeaderFooter;
	HashMap<String, Integer> forgotPasswordHeaderFooterColumn;
	
	String[][] forgotPasswordEmail;
	HashMap<String, Integer> forgotPasswordEmailColumn;
	
	public void init()
	{
		PageFactory.initElements(Browser.driver, loginPage);
		PageFactory.initElements(Browser.driver, forgotPasswordPage);
		
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
		init();
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
		
		// Check Header and Footer text
		/* String loginHeaderExpected = loginHeaderFooter[dataRowFromSheet][loginHeaderFooterColumn.get("Header")];
		String loginHeaderActual = loginPage.header..getText();
		checkpoints.check(loginHeaderExpected, loginHeaderActual, "Login Page Header Text"); */
		
		String loginFooterExpected = loginHeaderFooter[dataRowFromSheet][loginHeaderFooterColumn.get("Footer")];
		String loginFooterActual = loginPage.footer.getText();
		checkpoints.check(loginFooterExpected, loginFooterActual, "Login Page Footer Text");
		
		checkpoints.assertCheck();
	}
	
	@Test(enabled = true, description="Successful Login", dataProvider="Iteration")
	public void loginSuccessful(String rowForIteration, String iterationDescription)
	{	
		int dataRowFromSheet = Integer.parseInt(rowForIteration);		
		siteURL = site[dataRowFromSheet][siteColumn.get("Site URL")];
		Browser.launchSite(siteURL);
				
		String expectedTitle = loginSuccessful[dataRowFromSheet][loginSuccessfulColumn.get("Page Title")];
		String actualTitle = Browser.driver.getTitle();
		checkpoints.check(expectedTitle, actualTitle, "Login Page Title");
		
		String userIDExpected = loginSuccessful[dataRowFromSheet][loginSuccessfulColumn.get("User ID")];
		String passwordExpected = loginSuccessful[dataRowFromSheet][loginSuccessfulColumn.get("Password")];
		
		String defaultUserIDTextExpected = loginSuccessful[dataRowFromSheet][loginSuccessfulColumn.get("Default User ID Text")];
		String defaultPasswordTextExpected = loginSuccessful[dataRowFromSheet][loginSuccessfulColumn.get("Default Password Text")];
		
		// Check placeholder text in User ID field
		String defaultUserIDTextActual = loginPage.userIDField.getAttribute("placeholder");
		checkpoints.check(defaultUserIDTextExpected, defaultUserIDTextActual, "Default User ID Text");
		
		// Check placeholder text in Password field
		String defaultPasswordTextActual = loginPage.passwordField.getAttribute("placeholder");
		checkpoints.check(defaultPasswordTextExpected, defaultPasswordTextActual, "Default Password Text");

		// Enter User ID and check text appears in User ID field
		loginPage.userIDField.sendKeys(userIDExpected);
		String userIDActual = loginPage.userIDField.getAttribute("value");
		checkpoints.check(userIDExpected, userIDActual, "User ID");
				
		// Enter Password and check text appears in Password field
		loginPage.passwordField.sendKeys(passwordExpected);
		String passwordActual = loginPage.passwordField.getAttribute("value");
		checkpoints.check(passwordExpected, passwordActual, "Password");
		
		// Click Login button
		loginPage.loginButton.click();
		
		@SuppressWarnings("unused")
		WebElement loginImgVisible = wait.until(ExpectedConditions.visibilityOf(loginPage.dashimg));
		
		expectedTitle = loginSuccessful[dataRowFromSheet][loginSuccessfulColumn.get("Dashboard Title")];
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
		
		String userIDRequiredExpected = missingUserID[dataRowFromSheet][missingUserIDColumn.get("Missing User ID Text")];
		
		loginPage.loginButton.click();
		String userIDRequiredActual = loginPage.userIDRequired.getText();
		checkpoints.check(userIDRequiredExpected, userIDRequiredActual, "Missing User ID Text");
		
		checkpoints.assertCheck();
	}
	
	@Test(enabled = true, description="Incorrect User ID or Password", dataProvider="Iteration")
	public void incorrectUserIDPassword(String rowForIteration, String iterationDescription)
	{	
		int dataRowFromSheet = Integer.parseInt(rowForIteration);	
		siteURL = site[dataRowFromSheet][siteColumn.get("Site URL")];
		Browser.launchSite(siteURL);
		
		String incorrectUserIDPasswordExpected = incorrectUserID[dataRowFromSheet][incorrectUserIDColumn.get("Incorrect User ID Text")];
		String userID = incorrectUserID[dataRowFromSheet][incorrectUserIDColumn.get("User ID")];
		
		loginPage.userIDField.sendKeys(userID);
		loginPage.loginButton.click();
		
		@SuppressWarnings("unused")
		WebElement incorrectUserIDPassword = wait.until(ExpectedConditions.visibilityOf(loginPage.incorrectUserIDPassword));
		
		String incorrectUserIDPasswordActual = loginPage.incorrectUserIDPassword.getText();
		checkpoints.check(incorrectUserIDPasswordExpected, incorrectUserIDPasswordActual, "Incorrect User ID or Password Text");
		
		checkpoints.assertCheck();
	}

	@Test(enabled = true, description="Forgot Password", dataProvider="Iteration")
	public void forgotPasswordLink(String rowForIteration, String iterationDescription)
	{	
		int dataRowFromSheet = Integer.parseInt(rowForIteration);		
		siteURL = site[dataRowFromSheet][siteColumn.get("Site URL")];
		Browser.launchSite(siteURL);
		
		WebElement forgotPasswordLink = Browser.driver.findElements(By.tagName("a")).get(1);
		JavascriptExecutor exec = (JavascriptExecutor)Browser.driver;
		exec.executeScript("arguments[0].click()", forgotPasswordLink);
		
		@SuppressWarnings("unused")
		WebElement incorrectUserIDPassword = wait.until(ExpectedConditions.visibilityOf(forgotPasswordPage.emailField));
		
		String expectedTitle = forgotPasswordHeaderFooter[dataRowFromSheet][forgotPasswordHeaderFooterColumn.get("Page Title")];
		
		System.out.println(dataRowFromSheet);
		System.out.println(forgotPasswordHeaderFooter[1][3]);
		System.out.println(forgotPasswordHeaderFooterColumn.get("Page Title"));
		
		
		String actualTitle = Browser.driver.getTitle();
		checkpoints.check(expectedTitle, actualTitle, "Forgot Password Page Title");
		
		// Check Header and Footer text
		String forgotPasswordHeaderExpected1 = forgotPasswordHeaderFooter[dataRowFromSheet][forgotPasswordHeaderFooterColumn.get("Header Text 1")];
		String forgotPasswordHeaderActual1 = forgotPasswordPage.header1.getText();
		checkpoints.check(forgotPasswordHeaderExpected1, forgotPasswordHeaderActual1, "Forgot Password Page Header 1 Text");
		
		String forgotPasswordHeaderExpected2 = forgotPasswordHeaderFooter[dataRowFromSheet][forgotPasswordHeaderFooterColumn.get("Header Text 2")];
		String forgotPasswordHeaderActual2 = forgotPasswordPage.header2.getText();
		checkpoints.check(forgotPasswordHeaderExpected2, forgotPasswordHeaderActual2, "Forgot Password Page Header 2 Text");
		
		String forgotPasswordFooterExpected = forgotPasswordHeaderFooter[dataRowFromSheet][forgotPasswordHeaderFooterColumn.get("Footer Text")];
		String forgotPasswordFooterActual = forgotPasswordPage.footer.getText();
		checkpoints.check(forgotPasswordFooterExpected, forgotPasswordFooterActual, "Forgot Password Page Footer Text");
		
		// Check placeholder text in E-Mail Address field
		String defaultEmailAddressTextExpected = forgotPasswordEmail[dataRowFromSheet][forgotPasswordEmailColumn.get("Default E-Mail Address Text")];
		String defaultEmailAddressTextActual = forgotPasswordPage.emailField.getAttribute("placeholder");
		checkpoints.check(defaultEmailAddressTextExpected, defaultEmailAddressTextActual, "Default E-Mail Address Text");
		
		// Send e-mail address for forgotten password
		String forgotPasswordEmailExpected = forgotPasswordEmail[dataRowFromSheet][forgotPasswordEmailColumn.get("E-Mail Address")];
		forgotPasswordPage.emailField.sendKeys(forgotPasswordEmailExpected);
		String forgotPasswordEmailActual = forgotPasswordPage.emailField.getAttribute("value");
		checkpoints.check(forgotPasswordEmailExpected, forgotPasswordEmailActual, "Forgot Password E-Mail Address");
		forgotPasswordPage.sendButton.click();
		
		@SuppressWarnings("unused")
		WebElement passwordSent = wait.until(ExpectedConditions.visibilityOf(forgotPasswordPage.okayButton));
		
		// Click header on page after password is sent
		String passwordSentHeaderExpected = forgotPasswordEmail[dataRowFromSheet][forgotPasswordEmailColumn.get("Password Sent Header Text")];
		String passwordSentHeaderActual = forgotPasswordPage.passwordSentHeader.getText();
		checkpoints.check(passwordSentHeaderExpected, passwordSentHeaderActual, "Password Sent Header Text");
		
		// Click OK button to return to login page
		exec.executeScript("arguments[0].click()", forgotPasswordPage.okayButton);
		
		@SuppressWarnings("unused")
		WebElement returnedToLoginPage = wait.until(ExpectedConditions.visibilityOf(loginPage.loginButton));
			
		checkpoints.assertCheck();
	}	
}