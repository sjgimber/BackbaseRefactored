package HelperFunctions;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class logFunctions 
{
	public void logSeparatorLine()
	{
		// Display a separator line in the console.
		System.out.println("--------------------------------------------------------------------------------");
	}
	
	public void logMessage(String...lines)
	{
		// Log the messages to the debug trace.
		String timeStamp = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
		
		for (int i = 0; i < lines.length; i++)
		{
			System.out.println(timeStamp + ": " + lines[i]);
		}
	}
	
	public void logTestStart(String... lines)
	{
		// Place info about the current test being initiated into the debug trace.
		logSeparatorLine();
		for (int i = 0; i < lines.length; i++)
		{
			System.out.println("- " + lines[i]);
		}
		System.out.println("-");

		String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
		System.out.println("- Test started: " + timeStamp);
		logSeparatorLine();
	}
	
	public void logTestEnd()
	{
		// Place info about the test completing in the debug trace.
		logSeparatorLine();
		String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
		System.out.println("- Test completed: " + timeStamp);
		logSeparatorLine();
	}
}
