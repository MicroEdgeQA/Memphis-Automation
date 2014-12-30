package android;

import io.selendroid.SelendroidConfiguration;
import io.selendroid.SelendroidLauncher;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class selendroid_test {
	
	private SelendroidLauncher selendroidServer;
	
	@BeforeTest
	public void startSelendroid()
	{
		SelendroidConfiguration config = new SelendroidConfiguration();
		// Add the selendroid-test-app to the standalone server
		//config.addSupportedApp("src/main/resources/selendroid-test-app-0.12.0.apk");
		selendroidServer = new SelendroidLauncher(config);
		selendroidServer.launchSelendroid();
	}
	@Test
	public void seledroidTest() throws Exception 
	{
		WebDriver driver = new RemoteWebDriver(DesiredCapabilities.android());
		driver.get("http://google.com");
	}

}
