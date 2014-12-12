package tests;

import common.Browser;
import common.Util;

import java.util.Properties;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class Login
{		
	private String baseURL = "http://memphis.reviewerconnect.com/reviewerconnect#/page/login";
	public WebDriver driver = new FirefoxDriver();
	private Properties prop;
	
	public void launchSite()
	{
		driver.get("about:blank");
		driver.get(baseURL);
	}
		
	@AfterClass
	public void closeBrowser()
	{
		//driver.quit();
	}
	
	@BeforeMethod
	public void checkPageLoaded()
	{
		Assert.assertEquals(Util.IsDOMReady(driver), true);
	}
	
	@Test(enabled = false, description="Successful Login")
	public void loginSuccessful()
	{	
		launchSite();
		
		prop = Util.getPageProperties("LoginPage");
		
		String userIDExpected = "admin";
		String passwordExpected = "microedge";
		
		String defaultUserIDTextExpected = "User ID";
		String defaultPasswordTextExpected = "Password";
		
		// Check placeholder text in User ID field
		String defaultUserIDTextActual = driver.findElement(By.id(prop.getProperty("userIDField"))).getAttribute("placeholder");
		Assert.assertEquals(defaultUserIDTextExpected, defaultUserIDTextActual);
		
		// Check placeholder text in Password field
		String defaultPasswordTextActual = driver.findElement(By.id(prop.getProperty("passwordField"))).getAttribute("placeholder");
		Assert.assertEquals(defaultPasswordTextExpected, defaultPasswordTextActual);

		// Enter User ID and check text appears in User ID field
		driver.findElement(By.id(prop.getProperty("userIDField"))).sendKeys("admin");
		String userIDActual = driver.findElement(By.id(prop.getProperty("userIDField"))).getAttribute("value");
		Assert.assertEquals(userIDExpected, userIDActual);
				
		// Enter Password and check text appears in Password field
		driver.findElement(By.id(prop.getProperty("passwordField"))).sendKeys("microedge");
		String passwordActual = driver.findElement(By.id(prop.getProperty("passwordField"))).getAttribute("value");
		Assert.assertEquals(passwordExpected, passwordActual);
		
		// Click Login button
		driver.findElement(By.className(prop.getProperty("loginButton"))).click();
	}
	
	@Test(enabled = false, description="Missing User ID")
	public void missingUserID()
	{	
		launchSite();
		
		String userIDRequiredExpected = "User ID is Required.";
		
		driver.findElement(By.className(prop.getProperty("loginButton"))).click();
		String userIDRequiredActual = driver.findElement(By.className(prop.getProperty("userIDRequired"))).getText();
		Assert.assertEquals(userIDRequiredExpected, userIDRequiredActual);
	}
	
	@Test(enabled = false, description="Incorrect User ID or Password")
	public void incorrectUserIDPassword()
	{	
		launchSite();
		
		String incorrectUserIDPasswordExpected = "Incorrect user ID or password.";
		
		driver.findElement(By.id(prop.getProperty("userIDField"))).sendKeys("admin");
		driver.findElement(By.className(prop.getProperty("loginButton"))).click();
		
		String incorrectUserIDPasswordActual = driver.findElement(By.xpath(prop.getProperty("incorrectUserIDPassword"))).getText();
		Assert.assertEquals(incorrectUserIDPasswordExpected, incorrectUserIDPasswordActual);
	}


	@Test
	public void ForgotPasswordLink()
	{
		launchSite();
		
		driver.findElement(By.xpath(prop.getProperty("forgotPasswordLink"))).click();
		Browser.pause(2);
	}
	
}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	