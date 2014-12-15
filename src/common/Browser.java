package common;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

public class Browser
{
	public static WebDriver driver = null;
	
	// Required for links in Test Results Report to be displayed
	public static final String ESCAPE_PROPERTY = "org.uncommons.reportng.escape-output";
	
	public static void browserConfig(String browser, String screenshotLocation)
	{	
		// Set the screenshot storage location
		Screenshots.setScreenshotLocation(screenshotLocation);
		
		// Required for links in Test Results Report to be displayed
		System.setProperty(ESCAPE_PROPERTY, "false");
		
		// Internet Explorer
		if (browser.equals("Internet Explorer")) driver = new InternetExplorerDriver();
		
		// Firefox
		else if (browser.equals("Firefox")) driver = new FirefoxDriver();
		
		// Chrome
		else if (browser.equals("Chrome"))
		{
			// Workaround for "You are using an unsupported command-line flag: // --ignore-certificate-errors.
			// Stability and security will suffer." message after launching Chrome browser
			DesiredCapabilities capabilities = DesiredCapabilities.chrome();
			ChromeOptions options = new ChromeOptions();
			options.addArguments("test-type");
			capabilities.setCapability(ChromeOptions.CAPABILITY, options);
			
			driver = new ChromeDriver(capabilities);
		}
	}
	
	public static void launchSite(String site)
	{
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		driver.get("about:blank");
		driver.get(site);
	}
		
	public static void clickLinkByHref(String href)
	{
		try
		{
			List<WebElement> anchors = Browser.driver.findElements(By.tagName("a"));
		    Iterator<WebElement> index = anchors.iterator();
		 
		    while(index.hasNext())
		    {
		        WebElement anchor = index.next();
		        if(anchor.getAttribute("href").contains(href)) anchor.click();
			}
		} catch (StaleElementReferenceException e) {}
	}
	
	public static void pause(int timeInSeconds)
	{
		// Generic pause function needed to slow things down occasionally
		int timeInMilliseconds = timeInSeconds * 1000;
		try
		{
			Thread.sleep(timeInMilliseconds);
		}
			catch (InterruptedException e)
			{
				Thread.currentThread().interrupt();
			}
	}
}