package Tests;

import static org.junit.Assert.*;
import java.util.concurrent.TimeUnit;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import HelperFunctions.*;
import Pages.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)

public class readRecordTest 
{
	private WebDriver driver;		// Standard instance used for most tests.
	private WebDriver driver2;		// Extra instance used for concurrency tests.
	private String baseUrl;
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
		baseUrl = constants.baseURL;
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	}

	@After
	public void tearDown() throws Exception 
	{
		// Close the first driver instance.  This is used in ALL test cases.
		driver.close();
		
		// Now try to close the 2nd driver instance (used for concurrency testing).
		// This is used in SOME test cases, so may cause issues when closing.
		try
		{
			driver2.close();
		}
		catch (Exception e)
		{
			// Do nothing.
		};
		
		// Log the end of the test.
	    logger.logTestEnd();
	}

	@Test
	public void test02_01_ReadRecord_ValidRecord()
	{
		logger.logTestStart(
				"Test 2.1: Read Record: Valid Record",
				"Summary: This test verifies that a normal record can be read.");
	    driver.get(baseUrl);
		utilsTestRecords utils 	= new utilsTestRecords(driver);
	    
		String testID 			= "SJG Test 2.1";
		String introducedDate 	= "2017-04-01";
		String discontinuedDate = "2017-04-04";
		String companyName 		= "Amstrad";
				
		// Ensure a clean slate.
		utils.deleteTestRecord(testID);
		
		// Now create a single clean record.
		assertTrue(utils.createTestRecord(testID, introducedDate, discontinuedDate, companyName));
		
		// Read the record, and verify that the details are as originally entered.
		assertTrue(utils.verifyRecordDetails(testID, introducedDate, discontinuedDate, companyName));
	}
	
	@Test
	public void test02_02_ReadRecord_InvalidRecord()
	{
		logger.logTestStart(
				"Test 2.2: Read Record: Invalid Record",
				"Summary: This test verifies that a request to read an invalid record is handled.");
	    driver.get(baseUrl);
		utilsTestRecords utils 	= new utilsTestRecords(driver);
	    
	    // Need an extra instance of the browser for concurrent testing.
		driver2 = new ChromeDriver();
		driver2.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	    databasePOM dbPage2 	= new databasePOM(driver2);
	    driver2.get(baseUrl);
	    
		String testID 			= "SJG Test 2.2";
		String introducedDate 	= "";
		String discontinuedDate = "";
		String companyName 		= "";

		// Browser #1
		// Ensure a clean slate.
		utils.deleteTestRecord(testID);
		
		// Now create a single clean record.
		assertTrue(utils.createTestRecord(testID, introducedDate, discontinuedDate, companyName));
		
		// Browser #2
		// Get ready to access a non-existent record.
		dbPage2.searchForComputer(testID);
		
		// Browser #1
		// Delete the record.
		System.out.println("Browser #1: Delete a record.");
		utils.deleteTestRecord(testID);
		
		// Browser #2
		System.out.println("Browser #2: Attempt to access the deleted record via the link to it.");
		dbPage2.viewRecord(testID);
		
		// TODO: Not sure what the expected behaviour is here.
		// Currently, a totally blank screen is shown, so asserting a failure, and awaiting better specification of functionality.
		driver2.close();
		assert(false);
	}
}
