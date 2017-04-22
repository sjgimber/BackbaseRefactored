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

public class addNewRecordTest 
{
	private WebDriver driver;
	private String baseUrl;
	private logFunctions logger = new logFunctions();
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception 
	{
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
		// After each test is completed...
		driver.close();
		
		// Log the test case end time in the debug trace.
	    logger.logTestEnd();
	}

	@Test
	public void test01_01_CreateRecord_AddValidRecord() throws InterruptedException
	{
		logger.logTestStart(
				"Test 1.1: Create Record: Create Valid Record",
				"Summary: This test verifies that a record can be added.");
	    driver.get(baseUrl);
		utilsTestRecords utils = new utilsTestRecords(driver);
	    
	    // Test values:
	    String testID 			= "SJG Test 1.1";
	    String introducedDate 	= "2017-04-01";
	    String discontinuedDate = "2017-04-04";
	    String companyName 		= "Amstrad";
	    
		// Ensure a clean slate.
		utils.deleteTestRecord(testID);
		
	    // Create a new record.
		utils.createTestRecord(testID, introducedDate, discontinuedDate, companyName);
	    
	    // Verify created record contents.
	    assertTrue(utils.verifyRecordDetails(testID, introducedDate, discontinuedDate, companyName));
	}

	@Test
	public void test01_02_CreateRecord_AddEmptyRecord()
	{
		logger.logTestStart(
				"Test 1.2: Create Record: Create Empty Record",
				"Summary: This test verifies that an empty record cannot be created.");
		driver.get(baseUrl);
		utilsTestRecords utils = new utilsTestRecords(driver);
	    databasePOM dbPage = new databasePOM(driver);
	    addNewRecordPOM addNewRecordPage = new addNewRecordPOM(driver);
	    
	    // Test values:
	    String testID 			= "SJG Test 1.2";
	    String computerName		= "";
	    String introducedDate 	= "";
	    String discontinuedDate = "";
	    String companyName 		= "";

		// Ensure a clean slate.
		utils.deleteTestRecord(testID);
		
	    // Request a new record.
	    dbPage.clickAdd();
	    assertTrue(addNewRecordPage.verifyScreenShown());
	    
	    // Enter record details.
		addNewRecordPage.enterRecordDetails(computerName, introducedDate, discontinuedDate, companyName);
	    
		// Create the record and verify success.
		addNewRecordPage.clickCreate();
		
		logger.logMessage("Checking for validation error on the 'Computer Name' field...");;
		boolean errorShown = addNewRecordPage.isValidationErrorShown_ComputerName();
		if (errorShown)
		{
			logger.logMessage("\tPASS");
		}
		else
		{
			logger.logMessage("\tFAIL");
		}
		assertTrue(errorShown);
	}

	@Test
	public void test01_03_CreateRecord_InvalidIntroducedDate()
	{
		logger.logTestStart(
				"Test 1.3: Create Record: Create Invalid Record (Invalid “Introduced Date”)",
				"Summary: This test verifies that an invalid record cannot be created (invalid “Introduced Date” value).");
	
	    driver.get(baseUrl);
		utilsTestRecords utils = new utilsTestRecords(driver);
	    databasePOM dbPage = new databasePOM(driver);
	    addNewRecordPOM addNewRecordPage = new addNewRecordPOM(driver);
	    editRecordPOM editPage = new editRecordPOM(driver);
	    
	    // Test values:
	    String testID 			= "SJG Test 1.3";
	    String introducedDate 	= "";
	    String discontinuedDate = "";
	    String companyName 		= "";

		// Ensure a clean slate.
		utils.deleteTestRecord(testID);
		
	    // Cycle through a list of invalid date values.
    	// For each invalid date...
	    logger.logMessage("Validating 'Introduced Date' field...");
	    for (int i = 0; i < constants.invalidDates.length; i++)
	    {
		    // Request a new record.
		    dbPage.clickAdd();
		    assertTrue(addNewRecordPage.verifyScreenShown());
		    
		    // Determine the next type of invalid date format to test.
	    	introducedDate = constants.invalidDates[i];
	    	
	    	// Enter record details.
	    	addNewRecordPage.enterRecordDetails(testID, introducedDate, discontinuedDate, companyName);
	    	
			// Create the record and verify date field failure..
			addNewRecordPage.clickCreate();

			logger.logMessage("\tChecking for validation error on the 'Introduced Date' field... (" + introducedDate + ")");
			boolean errorShown = addNewRecordPage.isValidationErrorShown_IntroducedDate(); 
			if (errorShown)
			{
				logger.logMessage("\tPASS");
			}
			else
			{
				logger.logMessage("\tFAIL");
			}
			assertTrue(errorShown);
			
			// Return to the DB List page, to ensure that the field validations are reset.
			editPage.clickCancel();
		}
	}

	@Test
	public void test01_04_CreateRecord_InvalidDiscontinuedDate()
	{
		logger.logTestStart(
				"Test 1.4: Create Record: Create Invalid Record (Invalid “Discontinued Date”)",
				"Summary: This test verifies that an invalid record cannot be created (invalid “Discontinued Date” value).");
	    driver.get(baseUrl);
		utilsTestRecords utils = new utilsTestRecords(driver);
	    databasePOM dbPage = new databasePOM(driver);
	    addNewRecordPOM addNewRecordPage = new addNewRecordPOM(driver);
	    editRecordPOM editPage = new editRecordPOM(driver);
	    
	    // Test values:
	    String testID 			= "SJG Test 1.4";
	    String introducedDate 	= "";
	    String discontinuedDate = "";
	    String companyName 		= "";

		// Ensure a clean slate.
		utils.deleteTestRecord(testID);
		
	    // Cycle through a list of invalid date values.
    	// For each invalid date...
	    logger.logMessage("Validating 'Discontinued Date' field...");
	    for (int i = 0; i < constants.invalidDates.length; i++)
	    {
		    // Request a new record.
		    dbPage.clickAdd();
		    assertTrue(addNewRecordPage.verifyScreenShown());
		    
		    // Get the next invalid date / date format to try.
	    	discontinuedDate = constants.invalidDates[i];
	    	
	    	// Enter record details.
	    	addNewRecordPage.enterRecordDetails(testID, introducedDate, discontinuedDate, companyName);
	    	
			// Create the record and verify date field failure..
			addNewRecordPage.clickCreate();

			logger.logMessage("\tChecking for validation error on the 'Discontinued Date' field... (" + discontinuedDate + ")");
			boolean errorShown = addNewRecordPage.isValidationErrorShown_DiscontinuedDate(); 
			if (errorShown)
			{
				logger.logMessage("\tPASS");
			}
			else
			{
				logger.logMessage("\tFAIL");
			}
			assertTrue(errorShown);
			
			// Return to the DB List page, to ensure that the field validations are reset.
			editPage.clickCancel();
		}
	}
	
	@Test
	public void test01_05_CreateInvalidRecord_InvalidDateOrder()
	{
		logger.logTestStart(
				"Test 1.5: Create Record: Create Invalid Record (“Introduced Date” > “Discontinued Date”)",
				"Summary: This test verifies that an invalid record cannot be created (where the “Introduced Date” is greater than the “Discontinued Date” value).");
		driver.get(baseUrl);
		utilsTestRecords utils = new utilsTestRecords(driver);
	    databasePOM dbPage = new databasePOM(driver);
	    addNewRecordPOM addNewRecordPage = new addNewRecordPOM(driver);
	    
	    // Test values:
	    String testID 			= "SJG Test 1.5";
	    String introducedDate 	= "2017-04-04";			
	    String discontinuedDate = "2017-04-01";
	    String companyName 		= "";

		// Ensure a clean slate.
		utils.deleteTestRecord(testID);
		
	    // Request a new record.
	    dbPage.clickAdd();
	    assertTrue(addNewRecordPage.verifyScreenShown());
	    
	    // Enter record details.
		addNewRecordPage.enterRecordDetails(testID, introducedDate, discontinuedDate, companyName);
	    
		// Create the record and verify validation failure (as the Introduced date is later than the Discontinued date).
		addNewRecordPage.clickCreate();
		
		logger.logMessage("Checking for validation error on the date order (Introduced date is later than the Discontinued date)...");
		if (addNewRecordPage.isValidationErrorShown_IntroducedDate())
		{
			logger.logMessage("\tPASS");
		}
		else
		{
			logger.logMessage("\tFAIL");
		}
		assertTrue(addNewRecordPage.isValidationErrorShown_IntroducedDate());

		logger.logMessage("Checking for validation error on the date order (Introduced date is later than the Discontinued date)...");
		if (addNewRecordPage.isValidationErrorShown_DiscontinuedDate())
		{
			logger.logMessage("\tPASS");
		}
		else
		{
			logger.logMessage("\tFAIL");
		}
		assertFalse(dbPage.verifyRecordAddedMessageShown(testID));
		assertTrue(addNewRecordPage.isValidationErrorShown_DiscontinuedDate());
	}

	@Test
	public void test01_06_CreateRecord_DangerousCharacersInName()
	{
		logger.logTestStart(
				"Test 1.6: Create Record: Dangerous Characters In Name",
				"Summary: This test verifies that a valid record can be created (where the “Computer Name” contains dangerous characters (“<b> & ; ‘ “”)).");
	    driver.get(baseUrl);
		utilsTestRecords utils = new utilsTestRecords(driver);
	    	    
	    // Test values:
	    String testID 			= "SJG Test 1.6 <b> & ; ' \"";
	    String introducedDate 	= "2016-02-29";
	    String discontinuedDate = "2017-04-04";
	    String companyName 		= "Amstrad";
	    
		// Ensure a clean slate.
		utils.deleteTestRecord(testID);
		
	    // Request a new record.
		utils.createTestRecord(testID, introducedDate, discontinuedDate, companyName);
		
	    // Verify created record contents.
	    assertTrue(utils.verifyRecordDetails(testID, introducedDate, discontinuedDate, companyName));
	}

	@Test
	public void test01_07_CreateInvalidRecord_DuplicateRecord()
	{
		logger.logTestStart(
				"Test 1.7: Create Record: Create Duplicate Record",
				"Summary: This test verifies that an attempt to create a duplicate record is handled.");
	    driver.get(baseUrl);
		utilsTestRecords utils = new utilsTestRecords(driver);
	    databasePOM dbPage = new databasePOM(driver);
	    addNewRecordPOM addNewRecordPage = new addNewRecordPOM(driver);
	    
	    // Test values:
	    String testID 			= "SJG Test 1.7";
	    String introducedDate 	= "2017-04-04";			
	    String discontinuedDate = "2017-04-01";
	    String companyName 		= "";

		// Ensure a clean slate.
		utils.deleteTestRecord(testID);
		
	    // Create a new base record.
		utils.createTestRecord(testID, introducedDate, discontinuedDate, companyName);
		
		// Now create a duplicate record.
	    dbPage.clickAdd();
	    assertTrue(addNewRecordPage.verifyScreenShown());
	    
	    // Enter record details.
		addNewRecordPage.enterRecordDetails(testID, introducedDate, discontinuedDate, companyName);
	    
		// Attempt to create the record (it should be blocked).
		addNewRecordPage.clickCreate();

		// TODO: Functionality here is unclear - the app currently allows for multiple duplicate records to be created,
		// but this doesn't make sense.  Verification of expected functionality is required.
		assertFalse(dbPage.verifyRecordAddedMessageShown(testID));
	}
	
	@Test
	public void test01_08_CreateRecord_CancelCreation() throws InterruptedException
	{
		logger.logTestStart(
				"1.8: Create Record: Cancel Record Creation",
				"Summary: This test verifies that an attempt to create a duplicate record can be cancelled.");
	    driver.get(baseUrl);
		utilsTestRecords utils = new utilsTestRecords(driver);
	    databasePOM dbPage = new databasePOM(driver);
	    addNewRecordPOM addNewRecordPage = new addNewRecordPOM(driver);
	    
	    // Test values:
	    String testID 			= "SJG Test 1.8";
	    String introducedDate 	= "2017-04-01";
	    String discontinuedDate = "2017-04-04";
	    String companyName 		= "";
		
	    // Ensure a clean slate.
		utils.deleteTestRecord(testID);
		
	    // Request a new record.
	    dbPage.clickAdd();
	    assertTrue(addNewRecordPage.verifyScreenShown());
	    
	    // Enter record details.
		addNewRecordPage.enterRecordDetails(testID, introducedDate, discontinuedDate, companyName);
	    
		// Cancel the request.
		addNewRecordPage.clickCancel();

		// Verify no message is displayed.
		assertFalse(dbPage.verifyRecordAddedMessageShown(testID));
		
		// Verify that the record was not created.
		dbPage.searchForComputer(testID);
		assertTrue(dbPage.getMatchCount() == 0);
	}
}