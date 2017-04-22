package HelperFunctions;

public class constants 
{
	// Constant values used across the project.
	
	// ChromeDriver location
	public static final String chromeDriverPath = "/home/john/eclipse/chromedriver";
	
	// The URL to be used for the project.
	// NOTE: In future, it should be possible to set this via command-line, to allow for flexible calling from CI systems.
	public static final String baseURL = "http://computer-database.herokuapp.com/computers";
	
	// List of invalid date values to use for validation checking of each date field.
    public static final String[] invalidDates =	{"2017", "2017-02-29", "invalidvalue"};
}