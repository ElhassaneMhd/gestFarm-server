package Gestfarm.UI.utils;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

/**
 * Utility class for Selenium tests
 */
public class SeleniumUtils {

    /**
     * Take a screenshot (implementation depends on your reporting tool)
     */
    public static void takeScreenshot(WebDriver driver, String fileName) {
        // Implementation depends on your reporting tool
        // This is just a placeholder
        // Example with Selenium's built-in TakesScreenshot:
        /*
        try {
            TakesScreenshot ts = (TakesScreenshot) driver;
            File source = ts.getScreenshotAs(OutputType.FILE);
            FileUtils.copyFile(source, new File("./screenshots/" + fileName + ".png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        */
    }

    /**
     * Wait for page to load completely
     */
    public static void waitForPageLoad(WebDriver driver, int timeoutSeconds) {
        new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds)).until(
            webDriver -> ((JavascriptExecutor) webDriver)
                .executeScript("return document.readyState")
                .equals("complete"));
    }

    /**
     * Wait for AJAX calls to complete
     */
    public static void waitForAjax(WebDriver driver, int timeoutSeconds) {
        new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds)).until(
            webDriver -> ((JavascriptExecutor) webDriver)
                .executeScript("return jQuery.active == 0"));
    }

    /**
     * Scroll to element
     */
    public static void scrollToElement(WebDriver driver, WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
    }

    /**
     * Check if an element exists
     */
    public static boolean elementExists(WebDriver driver, By locator, int timeoutSeconds) {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds))
                .until(ExpectedConditions.presenceOfElementLocated(locator));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Get text from all elements matching a locator
     */
    public static List<String> getTextsFromElements(WebDriver driver, By locator) {
        return driver.findElements(locator).stream()
                .map(WebElement::getText)
                .toList();
    }
}
