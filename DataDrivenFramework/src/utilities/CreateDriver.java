package utilities;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

public class CreateDriver{
	private static CreateDriver instance = null;
	private static final int IMPLICIT_TIMEOUT = 0;
	private ThreadLocal<WebDriver> webDriver = new ThreadLocal<WebDriver>();
	private ThreadLocal<String> sessionId = new ThreadLocal<String>();
	private ThreadLocal<String> sessionBrowser = new ThreadLocal<String>();
	private ThreadLocal<String> sessionPlatform = new ThreadLocal<String>();
	private ThreadLocal<String> sessionVersion = new ThreadLocal<String>();
	
	private String getEnv = null;
	private Properties props = new Properties();
	
	private CreateDriver() {
		
	}
	
	public static CreateDriver getInstance() {
		if (instance == null) {
			instance = new CreateDriver();
		}
		return instance;
	}
	
	@SafeVarargs
	public final void setDriver(String browser, 
			String platform, 
			String environment, 
			Map<String, Object>... optPreferences) 
			throws Exception {
		
		DesiredCapabilities caps = null;
		String getPlatform = null;
		props.load(new FileInputStream(Global_VARS.SE_PROPS));
		
		switch (browser) {
		case "firefox":
			caps = new DesiredCapabilities();
			FirefoxOptions ffOpts = new FirefoxOptions();
			FirefoxProfile ffProfile = new FirefoxProfile();
			
			ffProfile.setPreference("browser.autofocus", true);
			ffProfile.setPreference("browser.tabs.remote.autostart.2", false);
			
			caps.setCapability(FirefoxDriver.Capability.PROFILE, ffProfile);
			caps.setCapability("marionette", true);
			
			if (environment.equalsIgnoreCase("local")) {
				System.setProperty("webdriver.gecko.driver", props.getProperty("gecko.driver.windows.path"));
				System.setProperty("browser.version", props.getProperty("firefox.revision"));
				webDriver.set(new FirefoxDriver(ffOpts.merge(caps)));
			}
			break;
		case "chrome":
			caps = new DesiredCapabilities();
			ChromeOptions chOptions = new ChromeOptions();
			Map<String, Object> chromePrefs = new HashMap<String, Object>();
			
			chromePrefs.put("credentials.enable.service", false);
			
			chOptions.setExperimentalOption("prefs", chromePrefs);
			chOptions.addArguments("--disable-plugins", "--disable-extensions", "disable-popup-blocking");
			
			caps.setCapability(ChromeOptions.CAPABILITY, chOptions);
			caps.setCapability("applicationCacheEnabled", false);
			
			if (environment.equalsIgnoreCase("local")) {
				System.setProperty("webdriver.chrome.driver", props.getProperty("chrome.driver.windows.path"));
				System.setProperty("browser.version", props.getProperty("chrome.revision"));
				webDriver.set(new ChromeDriver(chOptions.merge(caps)));
			}
			break;
		}
		
		getEnv = environment;
		getPlatform = platform;
		
		sessionId.set(((RemoteWebDriver) webDriver.get()).getSessionId().toString());
		sessionBrowser.set(caps.getBrowserName());
		sessionVersion.set(caps.getVersion());
		sessionPlatform.set(getPlatform);
		
		System.out.println("\n*** TEST ENVIRONMENT ="
				+ getSessionBrowser().toUpperCase()
				+ "/" + getSessionPlatform().toUpperCase()
				+ "/" + getEnv.toUpperCase()
				+ "/Selenium Version="
				+ props.getProperty("selenium.revision")
				+ "/Session ID="
				+ getSessionId()
				+ "\n");
		
		System.setProperty("selenium.revision", props.getProperty("selenium.revision"));
		getDriver().manage().timeouts().implicitlyWait(IMPLICIT_TIMEOUT, TimeUnit.SECONDS);
	}
	
	public WebDriver getDriver() {
		return webDriver.get();
	}
	
	public void closeDriver() {
		try {
			getDriver().quit();
		}
		catch (Exception e) {
			
		}
	}
	
	public String getSessionId() throws Exception {
		return sessionId.get();
	}
	
	public String getSessionBrowser() throws Exception {
		return sessionBrowser.get();
	}
	
	public String getSessionVersion() throws Exception {
		return sessionVersion.get();
	}
	
	public String getSessionPlatform() throws Exception {
		return sessionPlatform.get();
	}
}