package tests;

import common.Util;

import java.util.Properties;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class Login
{		
	private String baseURL = "http://memphis.reviewerconnect.com/reviewerconnect#/page/login";
	public WebDriver driver = new FirefoxDriver();
	private Properties prop;
	
	@BeforeClass
	public void launchBrowser()
	{
		driver.get(baseURL);
		prop = Util.getPageProperties();
	}
	
	@Test(description="Successful Login")
	public void loginSuccessful()
	{
		System.out.println("Successful Login");
		
		driver.findElement(By.id(prop.getProperty("userIDField"))).sendKeys("admin");
		driver.findElement(By.id(prop.getProperty("passwordField"))).sendKeys("microedge");
		driver.findElement(By.id(prop.getProperty("loginButton"))).click();
	}
	
	@Test(description="Missing User ID")
	public void missingUserID()
	{
		System.out.println("Missing User ID");
	}
	
	@Test(description="Incorrect User ID or Password")
	public void incorrectUserIDPassword()
	{
		System.out.println("Incorrect User ID or Password");
	}
	
	@Test(description="Forgot Password")
	public void forgotPassword()
	{
		System.out.println("Forgot Password");
	}
	
	@Test(description="Header")
	public void header()
	{
		System.out.println("Header");
	}	
	
	@Test(description="Footer")
	public void footer()
	{
		System.out.println("Footer");
	}	
}