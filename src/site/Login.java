package site;

import java.io.IOException;
import java.util.Arrays;
import java.util.Hashtable;

import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import common.Browser;
import common.Checkpoints;
import common.DataDriver;
import common.TestConfiguration;
import elements.DashboardPage;
import elements.LoginPage;

public class Login
{		
	String dataSourceName = "systemLogin";
	
	LoginPage loginPage = new LoginPage();
	DashboardPage dashboardPage = new DashboardPage();
	
	private void initElements()
	{
		PageFactory.initElements(Browser.driver, loginPage);
	}
	
	@Parameters({"browser", "dataLocation", "screenshotLocation"})
	@BeforeTest
	private void beforeTest(String browser, String dataLocation, String screenshotLocation)
	{
		TestConfiguration.beforeTest(browser, dataLocation, screenshotLocation, dataSourceName);
		initElements();
	}
	
	@AfterTest
	private void afterTest() throws IOException
	{
		TestConfiguration.afterTest();
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
	
	@Test(description="Login", dataProvider="Iteration")
	public void systemLogin(String rowForIteration, String iterationDescription)
	{
		Checkpoints.testPassed = true;
		
		Hashtable<String, Integer> column;
		
		int dataRowFromSheet = Integer.parseInt(rowForIteration);
		
		String[][] webSite = DataDriver.getData("Site");
		column = DataDriver.getColumnNamesFromSheet("Site");
		String siteURL = webSite[dataRowFromSheet][column.get("Site URL")];		
		Browser.launchSite(siteURL);
		
		String[][] login = DataDriver.getData("Login");
		column = DataDriver.getColumnNamesFromSheet("Login");
		
		String userIDExpected = login[dataRowFromSheet][column.get("User ID")];
		String passwordExpected = login[dataRowFromSheet][column.get("Password")];
		
		String userIDActual = loginPage.enterUserID(userIDExpected);
		Checkpoints.check(userIDExpected, userIDActual, "User ID");
		
		String passwordActual = loginPage.enterPassword(passwordExpected);
		Checkpoints.check(passwordExpected, passwordActual, "Password");
		
		loginPage.clickLoginButton();
		
		dashboardPage.clickLogoutButton();
		
		Checkpoints.failureHandler(Checkpoints.testPassed);
	}
}