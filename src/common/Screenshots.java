package common;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.Reporter;

public class Screenshots
{
	// Used to set location for storage of screenshots generated during execution
	public static String storeScreenshots = null;
	
	// Screenshots on or off
	static boolean takeScreenshot = false;
	
	// Needed for screenshots
	static long timestamp = System.currentTimeMillis();
	static String directoryName = "TestExecution" + timestamp;	
	
	public static void setScreenshotLocation(String screenshotLocation)
	{
		if (!(screenshotLocation.equals("DISABLED")))
		{
			takeScreenshot = true;
			storeScreenshots = screenshotLocation;
		}
	}
	
	public static void screenshot()
	{      
		// Takes a screenshot and saves it to a file
		// The directory name used to store the screenshot files is generated randomly to not overwrite previous executions
		timestamp = System.currentTimeMillis();
		File screenshot = ((TakesScreenshot)Browser.driver).getScreenshotAs(OutputType.FILE);
		
		try
		{
			String filePath = storeScreenshots + "\\" + directoryName + "\\" + "TestExecution" + timestamp + ".png";
			FileUtils.copyFile(screenshot, new File(filePath));
			Reporter.log("<a href=" + filePath + " target='_blank' > (Screenshot) </a>");
			Reporter.log("<br>");
		}
		catch (IOException e)
		{
			System.out.println("Problem saving screenshot!");
		};
	}
}