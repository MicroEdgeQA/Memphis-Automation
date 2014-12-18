package common;

public class TestConfiguration
{	
	public static void beforeTest(String browser, String dataLocation, String screenshotLocation, String dataSourceName)
	{
		// Configure Web browser for test execution
		Browser.browserConfig(browser, screenshotLocation, dataLocation);
						
		// Specify location of workbook for data driver
		DataDriver.assignDataSource(dataLocation, dataSourceName);
	}
}