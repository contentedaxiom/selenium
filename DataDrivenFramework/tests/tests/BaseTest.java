package tests;

import org.testng.annotations.Test;

import utilities.CreateDriver;
import utilities.Global_VARS;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.AfterSuite;

public class BaseTest {
  @Test(groups={"BaseTest"})
  public void f() {
	  Assert.assertTrue(true);
  }
  @BeforeMethod(alwaysRun = true, enabled = true)
  public void beforeMethod(ITestResult result) {
  }

  @AfterMethod(alwaysRun = true, enabled = true)
  public void afterMethod(ITestResult result) throws Exception{
	  //WebDriver driver = CreateDriver.getInstance().getDriver();
  }

  @BeforeClass(alwaysRun = true, enabled = true)
  public void beforeClass(ITestContext context) {
  }

  @AfterClass(alwaysRun = true, enabled = true)
  public void afterClass(ITestContext context) {
  }

  @Parameters({"browser","platform","includePattern","excludePattern"})
  @BeforeTest(alwaysRun = true, enabled = true)
  public void beforeTest(@Optional(Global_VARS.BROWSER)String browser, @Optional(Global_VARS.PLATFORM)String platform, 
		  @Optional String includePattern, @Optional String excludePattern, ITestContext ctxt) throws Exception {
	  if(includePattern != null) {
		  System.setProperty("includePattern", includePattern);
	  }
	  
	  if(excludePattern != null) {
		  System.setProperty("excludePattern", excludePattern);
	  }
	  
	  Global_VARS.DEF_BROWSER = browser;
	  System.setProperty("browser", browser);
	  Global_VARS.DEF_PLATFORM = platform;
	  System.setProperty("platform", platform);
	  
	  CreateDriver.getInstance().setDriver(Global_VARS.DEF_BROWSER, Global_VARS.DEF_PLATFORM, Global_VARS.DEF_ENVIRONMENT);
  }

  @AfterTest(alwaysRun = true, enabled = true)
  public void afterTest() {
	  CreateDriver.getInstance().closeDriver();
  }

  @Parameters({"environment"})
  @BeforeSuite(alwaysRun = true, enabled = true)
  public void beforeSuite(@Optional(Global_VARS.ENVIRONMENT)String environment, ITestContext context ) throws Exception {
	  Global_VARS.DEF_ENVIRONMENT = environment;
	  Global_VARS.SUITE_NAME = context.getSuite().getXmlSuite().getName();
  }

  @AfterSuite(alwaysRun = true, enabled = true)
  public void afterSuite() {
  }

}
