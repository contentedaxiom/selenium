package utilities;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class BrowserUtils {
	public static void waitFor(String title, Duration timer) throws Exception{
		WebDriver driver = CreateDriver.getInstance().getDriver();
		WebDriverWait exists = new WebDriverWait(driver, timer);
		exists.until(ExpectedConditions.refreshed(ExpectedConditions.titleContains(title)));
	}
	
	public static void waitForURL(String url, Duration timer) throws Exception {
		WebDriver driver = CreateDriver.getInstance().getDriver();
		WebDriverWait exists = new WebDriverWait(driver, timer);
		exists.until(ExpectedConditions.refreshed(ExpectedConditions.urlContains(url)));
	}
	
	public static void waitForClickable(By by, Duration timer) throws Exception {
		WebDriver driver = CreateDriver.getInstance().getDriver();
		WebDriverWait exists = new WebDriverWait(driver, timer);
		exists.until(ExpectedConditions.refreshed(ExpectedConditions.elementToBeClickable(by)));
	}
	
	public static void click(By by) throws Exception {
		WebDriver driver = CreateDriver.getInstance().getDriver();
		WebElement element = driver.findElement(by);
		JavascriptExecutor js = (JavascriptExecutor)driver;
		js.executeScript("arguments[0].click();", element);
	}
}
