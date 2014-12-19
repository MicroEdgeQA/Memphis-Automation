package android;

import io.selendroid.SelendroidCapabilities;
import io.selendroid.server.model.SelendroidStandaloneDriver;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

public class selendroid_test {
	
	@Test
	public void seledroidTest() 
	{
		SelendroidCapabilities capa = new SelendroidCapabilities("io.selendroid.testapp:0.12.0");

		SelendroidStandaloneDriver driver = new SelendroidStandaloneDriver(capa);
		WebElement inputField = ((WebDriver) driver).findElement(By.id("my_text_field"));
		Assert.assertEquals("true", inputField.getAttribute("enabled"));
		inputField.sendKeys("Selendroid");
		Assert.assertEquals("Selendroid", inputField.getText());
		driver.quit();
	}

}
