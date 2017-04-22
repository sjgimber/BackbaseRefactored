package HelperFunctions;

import static org.junit.Assert.assertTrue;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import Pages.addNewRecordPOM;
import Pages.databasePOM;
import Pages.editRecordPOM;

public class utilsTestRecords 
{
	WebDriver driver;
	private logFunctions logger = new logFunctions();
	
	public utilsTestRecords(WebDriver driver)
	{
		this.driver = driver;
	}
	
	public boolean verifyRecordDetails(String computerName, String introducedDate, String discontinuedDate, String companyName)
	{
		// Helper function to locate a record, view it, and verify that the requested details are present.
		logger.logMessage("Verifying record details: (Computer: " + computerName + ", introduced: " + introducedDate + ", discontinued: " + discontinuedDate + ", company: " + companyName + ")... ");
	    driver.get(constants.baseURL);

	    databasePOM dbPage = new databasePOM(driver);
	    editRecordPOM editPage = new editRecordPOM(driver);
	    boolean verified = true;
	    
	    dbPage.searchForComputer(computerName);
	    if (dbPage.getMatchCount() == 0)
	    {
	    	logger.logMessage("\tRecord doesn't exist.");
	    	verified = false;
	    }
	    else
	    {
	    	dbPage.viewRecord(computerName);
	    	verified = editPage.verifyRecordDetails(computerName, introducedDate, discontinuedDate, companyName);
	    }
	    return verified;
	}
	
	public boolean createTestRecord(String computerName, String introducedDate, String discontinuedDate, String companyName)
	{
		// Helper function to create a new computer record, and verify that it has been created.
		logger.logMessage("Creating new test record: " + computerName);
		
	    driver.get(constants.baseURL);

	    databasePOM dbPage = new databasePOM(driver);
	    addNewRecordPOM addNewRecordPage = new addNewRecordPOM(driver);
	    
	    // Request a new record.
	    dbPage.clickAdd();
	    assertTrue(addNewRecordPage.verifyScreenShown());
	    
	    // Enter record details.
		addNewRecordPage.enterRecordDetails(computerName, introducedDate, discontinuedDate, companyName);
	    
		// Create the record and verify success.
		addNewRecordPage.clickCreate();

		return dbPage.verifyRecordAddedMessageShown(computerName);
	}
	
	public boolean deleteTestRecord(String computerName)
	{
		// Helper function to delete a new computer record, and verify that it has been deleted.
		logger.logMessage("Deleting test record: " + computerName);

		driver.get(constants.baseURL);
		boolean allRecordsDeleted = true;
		
	    databasePOM dbPage = new databasePOM(driver);
	    editRecordPOM editPage = new editRecordPOM(driver);

	    // From the Database screen, search for and view the record.
	    // NOTE: There may be multiple instances of a search record with the given name.
	    dbPage.searchForComputer(computerName);
	    int recordCount = dbPage.getMatchCount();
	    logger.logMessage("\t" + recordCount + " matching records found for deletion...");
	    for (int i = 0; i < recordCount; i++)
	    {
	    	logger.logMessage("\tDeleting instance #" + (i + 1) + " of " + recordCount);
	    	// View and delete the record.
	    	dbPage.viewRecord(computerName);
	    	editPage.clickDelete();
	    	
	    	// Verify that it deleted.
	    	if (!dbPage.verifyRecordDeletedMessageShown())
	    	{
	    		allRecordsDeleted = false;
	    		break;
	    	}
	    	
	    	// Refresh the search list to show the remaining matches.
		    dbPage.searchForComputer(computerName);
	    }
	    
	    return allRecordsDeleted;
	}	
	
	public boolean validationFailureShown(WebElement element)
	{
		// Check the specified field, and determine if a validation error is displayed.
		// Currently used for date fields on the Add Record and Edit Record screens.
		
		if (element.getAttribute("class").contains("error"))
		{
			// The "clearfix error" class is being used for this field container.
			// Therefore, a validation error is being shown.
			// (The class is the only way to detect the error, as it changes the field background).
			logger.logMessage("\tValidation error detected.");
			return true;
		}
		else
		{
			// The standard "clearfix" class is being used for the field.
			logger.logMessage("\tValidation error not detected.");
			return false;
		}
	}
}