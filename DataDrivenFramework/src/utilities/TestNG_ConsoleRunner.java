package utilities;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;

public class TestNG_ConsoleRunner extends TestListenerAdapter{
	private static String logFile = null;
	
	@Override
	public void onStart(ITestContext testContext) {
		super.onStart(testContext);
	}
	
	@Override
	public void onFinish(ITestContext testContext) {
		log("\nTotal Passed = "
				+ getPassedTests().size()
				+ ", Total Failed = "
				+ getFailedTests().size()
				+ ", Total Skipped = "
				+ getSkippedTests().size()
				+ "\n");
		super.onFinish(testContext);
	}
	
	@Override
	public void onTestStart(ITestResult tr) {
		if (logFile == null) {
			logFile = Global_VARS.LOGFILE_PATH
					+ Global_VARS.SUITE_NAME
					+ "-" 
					+ new SimpleDateFormat("MM.dd.yy.HH.mm.ss").format(new Date())
					+ ".log";
			
		}
		
		log("\n--------------------------Test '"
				+ tr.getName()
				+ getTestDescription(tr)
				+ "'-------------------------\n");
		
		log(tr.getStartMillis(),
				"START->" + tr.getName() + "\n");
		
		log("     ***Test Parameters = "
				+ getTestParams(tr) + "\n");
		
		super.onTestStart(tr);
	}
	
	@Override
	public void onTestSuccess(ITestResult tr) {
		log("     ***Result = PASSED\n");
		
		log(tr.getEndMillis(), 
				"END->" + tr.getName()
				+ "\n---\n");
		super.onTestSuccess(tr);
	}
	
	@Override
	public void onTestFailure(ITestResult tr) {
		if (!getTestMessage(tr).equals("")) {
			log(getTestMessage(tr) + "\n");
		}
		
		log("     ***Result = FAILED\n");
		
		log(tr.getEndMillis(), 
				"END->" + tr.getInstanceName() + "." + tr.getName()
				+ "\n---\n");
		super.onTestFailure(tr);
	}
	
	@Override
	public void onTestSkipped(ITestResult tr) {
		if (!getTestMessage(tr).equals("")) {
			log(getTestMessage(tr) + "\n");
		}
		
		log("     ***Result = SKIPPED\n");
		
		log(tr.getEndMillis(), 
				"END->" + tr.getInstanceName() + "." + tr.getName()
				+ "\n---\n");
		super.onTestSkipped(tr);
	}
	
	@Override
	public void onConfigurationSuccess(ITestResult tr) {
		super.onConfigurationSuccess(tr);
	}
	
	@Override
	public void onConfigurationFailure(ITestResult tr) {
		if (!getTestMessage(tr).equals("")) {
			log(getTestMessage(tr) + "\n");
		}
		
		log("     ***Result = CONFIGURATION FAILED\n");
		
		log(tr.getEndMillis(), 
				"END CONFIG->" + tr.getInstanceName() + "." + tr.getName()
				+ "\n---\n");
		super.onConfigurationFailure(tr);
	}
	
	@Override
	public void onConfigurationSkip(ITestResult tr) {
		if (!getTestMessage(tr).equals("")) {
			log(getTestMessage(tr) + "\n");
		}
		
		log("     ***Result = CONFIGURATION SKIPPED\n");
		
		log(tr.getEndMillis(), 
				"END CONFIG->" + tr.getInstanceName() + "." + tr.getName()
				+ "\n---\n");
		super.onConfigurationSkip(tr);
	}
	
	public void log(long dateMillis, String line) {
		System.out.format("%s: %s%n", String.valueOf(new Date(dateMillis)), line);
		
		if (logFile != null) {
			writeTestngLog(logFile, line);
		}
	}
	
	public void log(String line) {
		System.out.format("%s%n", line);
		
		if (logFile != null) {
			writeTestngLog(logFile, line);
		}
	}
	
	public String getTestMessage(ITestResult tr) {
		Boolean found = false;
		
		if (tr != null && tr.getThrowable() != null) {
			found = true;
		}
		
		if (found == true) {
			return tr.getThrowable().getMessage() == null ? "" : tr.getThrowable().getMessage();
		}
		else {
			return "";
		}
	}
	
	public String getTestParams(ITestResult tr) {
		int iLength = tr.getParameters().length;
		String message = "";
		
		try {
			if (tr.getParameters().length > 0) {
				message = tr.getParameters()[0].toString();
				
				for(int i = 0; i < iLength; i++) {
					if(i == 0) {
						message = tr.getParameters()[0].toString();
					}
					else {
						message = message + ", " + tr.getParameters()[i].toString();
					}
				}
			}
		}
		catch (Exception e) {
			
		}
		
		return message;
	}
	
	public String getTestDescription(ITestResult tr) {
		String message = "";
		
		try {
			if (tr.getParameters().length > 0) {
				message = ": " + tr.getParameters()[1].toString();
			}
		}
		catch(Exception e) {
			
		}
		
		return message;
	}
	
	public void writeTestngLog(String logFile, String line) {
		DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		Date date = new Date();
		
		File directory = new File(Global_VARS.LOGFILE_PATH);
		File file = new File(logFile);
		
		try {
			if(!directory.exists()) {
				directory.mkdirs();
			}
			else if (!file.exists()) {
				file.createNewFile();
			}
			
			BufferedWriter writer = new BufferedWriter(new FileWriter(logFile, true));
			
			if(line.contains("START") || line.contains("END")) {
				writer.append("[" + dateFormat.format(date) + "] " + line);
			}
			else {
				writer.append(line);
			}
			writer.newLine();
			writer.close();
		}
		catch(IOException e){
			
		}
	}
}
