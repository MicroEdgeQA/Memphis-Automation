package elements;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import common.Browser;

public class LoginPage
{
	@FindBy(id="UserId")
	public WebElement userIDField;
		
	@FindBy(id="Password")
	public WebElement passwordField;
	
	@FindBy(xpath="/html/body/div/div/div/div/div[1]/div[2]/form/button")
	public WebElement loginButton;
	
	public String enterUserID(String userIDExpected)
	{
		userIDField.sendKeys(userIDExpected);
		String userIDActual = userIDField.getAttribute("value");
		return userIDActual;
	}
	
	public String enterPassword(String passwordExpected)
	{
		passwordField.sendKeys(passwordExpected);
		String passwordActual = passwordField.getAttribute("value");
		return passwordActual;
	}
	
	public void clickLoginButton()
	{
		loginButton.click();
		Browser.pause(5);
	}
}