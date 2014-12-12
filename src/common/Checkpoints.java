package common;

import org.testng.Reporter;

public class Checkpoints
{
	// Used to track if a check has failed instead of assert so result can be logged without halting test execution
	// Assert at end of test, fail if testPassed = false, continues on to next test
	// Must be public or test results will be useless!
	// A failure will set testPassed to false and allow the failureHandler method to detect this
	// and throw an assertion which will be used to mark the test as failed in the results
	// Subsequent tests must initialize testPassed to true again to reset the failure tracking
	public static boolean testPassed = true;
	
	public static void check(String expected, String actual, String checkName)
	{	
		// Takes an expected and actual value and performs a simple check
		// Writes the results to the TestNG reporter and Java console
		if (expected.equals(actual))
			{
				Reporter.log("Check Passed for " + checkName + " - " + "<< " + expected + " >>", true);
			}
		
		else
		{
				testPassed = false;  // Needed by failureHandler method to flag test as failed
				Reporter.log("Check FAILED for " + checkName + " - " + "Expected value << " + expected + " >> does not match actual value << " + actual + " >>", true);
		}
		
		if (Screenshots.takeScreenshot == true) Screenshots.screenshot();
	}
	
	public static void failureHandler(boolean testPassed)
	{
		// Must be last line of a test to avoid an assertion from halting test execution
		// Failed checks will set testPassed variable to false which will trigger
		// failed assertion to flag test iteration as failed in results report
		// Tests must initialize testPassed to true and end by passing testPassed to the failureHandler method
		if (testPassed == false)
		{
			Reporter.log("* One or more FAILURES have occurred in this test iteration!  Please examine the results.");
			Reporter.log("<br>");
			assert testPassed;  // Test case failed - Assert to count failure in test results percentage
		}
		else  // Test case passed
		{
			Reporter.log("* All checks in this test iteration have passed.");
			Reporter.log("<br>");
		}
	}
}