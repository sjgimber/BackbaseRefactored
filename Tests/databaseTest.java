package Tests;

import java.util.concurrent.TimeUnit;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import HelperFunctions.constants;
import HelperFunctions.logFunctions;

public class databaseTest 
{
	private WebDriver driver;
	private logFunctions logger = new logFunctions();

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception 
	{
		// Before each test is run...
		System.setProperty("webdriver.chrome.driver", constants.chromeDriverPath);
		driver = new ChromeDriver();
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	}

	@After
	public void tearDown() throws Exception 
	{
		// Close the first driver instance.  This is used in ALL test cases.
		driver.close();
		
		// Log the end of the test.
	    logger.logTestEnd();
	}

	@Test
	public void test() throws InterruptedException 
	{
		logger.logTestStart(
				"Database tests",
				"These tests have yet to be defined / run.");
		// Currently there are no tests for the Database List page (though there are functions in the POM).
		assert(false);
	}
}
