package common;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

/**
 * This class provides various methods that can be reused in different test files.
 * 
 * @author lbello
 */
public class Util1 {
	
	/**
	 * Loads content from a .properties file into a java Properties object. If there is any IO exception, returns null;
	 * 
	 * @return Properties object with content from properties file loaded.
	 */
	public static Properties getPageProperties(){
		
		Properties prop = new Properties();
		InputStream input = null;
		String propertyFile = "Memphis_Automation/LoginPage.properties";
		
		try{
			input = new FileInputStream(propertyFile);
			prop.load(input);	
		}
	    catch (IOException ex) {
	    	ex.printStackTrace();
	    	return null;
	    } finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
					return null;
				}
			}
	    }
		
		return prop;
	}
	
	/**
	 * Takes a screenshot of the current open browser. Creates a folder with the name of today's date and saves the screenshot inside with a timestamp.
	 * 
	 * @param testName the name of the test case for which the screenshot is being taken
	 * @param driver The current running Webdriver
	 * @throws IOException
	 */
	public static void saveScreenshot(String testName, WebDriver driver) throws IOException{
		DateFormat dateFormat = new SimpleDateFormat("EEE, MMM d, yyyy");
		Date dateTime = new Date();
		String day = dateFormat.format(dateTime);
		dateFormat = new SimpleDateFormat("hh!mm-a");
		String date = dateFormat.format(dateTime);
		
		File screenShot = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		
		if (screenShot != null)
			FileUtils.copyFile(screenShot, new File(String.format("Failed Case Screenshots/%s/%s_%s.png",day,testName,date)));
		}
	
	/**
	 * Queries the page DOM using javascript to check it's ready state. 
	 * 
	 * @param driver The Webdriver being used for the tests
	 * @return true if DOM times out after 10 seconds, false if it completes loading
	 */
	static public boolean IsDOMReady(WebDriver driver){
		long timeout = System.currentTimeMillis() + 10000;
		while(!((JavascriptExecutor)driver).executeScript("return document.readyState;").equals("complete")){
			if (System.currentTimeMillis() > timeout)
				return false;
		}
		return true;
	}
}