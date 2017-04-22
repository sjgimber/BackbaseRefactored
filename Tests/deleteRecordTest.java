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

import Pages.*;
import HelperFunctions.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)

public class deleteRecordTest 
{
	private WebDriver driver;		// Standard instance used for most tests.
	private WebDriver driver2;		// Extra instance used for concurrency tests.
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
	public void test04_01_DeleteRecord_DeleteValidRecord()
	{
		logger.logTestStart(
				"Test 4.1: Delete Record: Delete Valid Record",
				"Summary: This test verifies that a record can be deleted.");
		utilsTestRecords utils = new utilsTestRecords(driver);

		String testID 			= "SJG Test 4.1";
		String introducedDate 	= "2017-04-01";
		String discontinuedDate = "2017-04-04";
		String companyName 		= "Amstrad";
				
		// Ensure a clean slate.
		utils.deleteTestRecord(testID);
		
		// Now create a single clean record.
		assertTrue(utils.createTestRecord(testID, introducedDate, discontinuedDate, companyName));
		
		// Delete the record.  Verify it has been deleted.
		assertTrue(utils.deleteTestRecord(testID));
		assertFalse(utils.verifyRecordDetails(testID, introducedDate, discontinuedDate, companyName));
	}
	
	@Test
	public void test04_02_DeleteRecord_DeleteInvalidRecord()
	{
		logger.logTestStart(
				"Test 4.2: Delete Record (Delete Invalid Record)",
				"Summary: This test verifies that an attempt to delete a non-existent record is handled correctly.");
		utilsTestRecords utils 	= new utilsTestRecords(driver);

	    // Need an extra instance of the browser for concurrent testing.
		driver2 = new ChromeDriver();
		driver2.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		utilsTestRecords utils2 = new utilsTestRecords(driver2);
	    databasePOM dbPage2 	= new databasePOM(driver2);
	    editRecordPOM editPage2 = new editRecordPOM(driver2);
		
		String testID 			= "SJG Test 4.2";
		String introducedDate 	= "";
		String discontinuedDate = "";
		String companyName 		= "";
				
		System.out.println("Browser #1: Setup");
		// Browser #1:
		// Ensure a clean slate.
		utils.deleteTestRecord(testID);

		// Now create a single clean record.
		assertTrue(utils.createTestRecord(testID, introducedDate, discontinuedDate, companyName));
		
		// Navigate to the created record.
		assertTrue(utils.verifyRecordDetails(testID, introducedDate, discontinuedDate, companyName));
		
		// Browser #2:
		// Navigate to the created record.
		assertTrue(utils2.verifyRecordDetails(testID, introducedDate, discontinuedDate, companyName));
		
		// Browser #1: Delete the record.
		logger.logMessage("Browser #1: Delete record...");
		assertTrue(utils.deleteTestRecord(testID));
		
		// Browser #2: Attempt to delete the non-existent record.
		// Delete the record.  Verify it has been deleted.
		logger.logMessage("Browser #2: Delete non-existent record...");
		editPage2.clickDelete();
		assertFalse(dbPage2.verifyRecordDeletedMessageShown());
		
		// TODO: Functionality is not clear here.  
		// Currently the test is coded to not accept a successful deletion of a non-existent record.
	}
}
