package common;

import org.openqa.selenium.By;

public class WebMail
{
	public static void webMailLogin(String provider, String userID, String password)
	{
		if (provider == "gmail")
		{
			Browser.driver.findElement(By.id("Email")).sendKeys(userID);
			Browser.driver.findElement(By.id("Passwd")).sendKeys(password);
			Browser.driver.findElement(By.id("signIn")).click();
			Browser.pause(5);
		}
	}
	
	public static void webMailOpenMessage(String provider, String subject)
	{
		if (provider == "gmail")
		{
			Browser.driver.findElement(By.xpath("//span[contains(text(), \"" + subject + "\")]")).click();
			Browser.pause(10);
		}
	}
	
	public static void webMailLogout(String provider)
	{
		if (provider == "gmail")
		{
			Browser.driver.findElement(By.xpath("/html/body/div[7]/div[3]/div/div[1]/div[4]/div[1]/div[1]/div[1]/div/div[3]/div[1]/a")).click();
			Browser.driver.findElement(By.xpath("/html/body/div[7]/div[3]/div/div[1]/div[4]/div[1]/div[1]/div[1]/div/div[3]/div[2]/div[3]/div[2]/a")).click();
			Browser.pause(5);
		}
	}
}