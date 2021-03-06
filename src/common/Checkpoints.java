package common;

import org.testng.Reporter;

public class Checkpoints
{
	private boolean testFailed = false;
	private String failedCheckDefaultMessage = "The following checks have FAILED in this automated test iteration: "; 
	
	private String failedCheck = failedCheckDefaultMessage;
	
	public void check(String expected, String actual, String checkName)
	{	
		// Set to true to stop test execution when a check failure occurs
		boolean stopTestOnFailure = false;
		boolean takeScreenshotForPassed = false;
		
		// Takes an expected and actual value and performs a simple check
		// Writes the results to the TestNG reporter and Java console
		if (expected.equals(actual))
			{
				Reporter.log("Check <font color='green'>PASSED</font> for " + checkName + " - " + "<< " + expected + " >>", true);
				if (Screenshots.takeScreenshot)
				{
					if (takeScreenshotForPassed) Screenshots.screenshot();
					if (!takeScreenshotForPassed) Reporter.log("<br>");  // Improves report appearance
				}
			}
		
		else
		{
				Reporter.log("Check <font color='red'>FAILED</font> for " + checkName + " - " + "Expected value << " + expected + " >> does not match actual value << " + actual + " >>", true);
				if (Screenshots.takeScreenshot) Screenshots.screenshot();
				testFailed = true;
				failedCheck = failedCheck + "<< " + checkName + " >>, ";
				
				// Stop test execution when a check failure occurs
				if (stopTestOnFailure) throw new AssertionError("* Test aborted! Check FAILED: " + checkName + " *");
		}
	}
	
	public void assertCheck()
	{
		// Asserts check at end of test so execution is not interrupted
		if (testFailed)
		{
			// Trim off extra comma and space for report
			String failedCheckList = failedCheck.substring(0, failedCheck.length()-2);
			
			// Initialize for next test
			testFailed = false;
			failedCheck = failedCheckDefaultMessage;
			
			// Mark test as failed in report with list of failed checks
			throw new AssertionError(failedCheckList);
		}
		else Reporter.log("<font color='green'>* All checks in this automated test iteration have passed! *</font>", true);
	}
}