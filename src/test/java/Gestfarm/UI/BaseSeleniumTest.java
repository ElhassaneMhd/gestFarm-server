package Gestfarm.UI;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.time.Duration;

/**
 * Base class for all Selenium tests, handling browser setup and teardown.
 */
public abstract class BaseSeleniumTest {

    protected static WebDriver driver;
    protected static final String BASE_URL = "http://localhost:5173/"; // Updated to point to the app path

    @BeforeAll
    public static void setupClass() {
        // Setup WebDriver using WebDriverManager
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    public void setupTest() {
        // Configure Chrome options
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        // Use headless mode if needed, e.g., in CI environments
        // options.addArguments("--headless");
        
        // Initialize the WebDriver
        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        
        // Navigate to the base URL
        driver.get(BASE_URL);
    }

    @AfterEach
    public void tearDown() {
        // Close the current browser window
        if (driver != null) {
            driver.quit();
        }
    }

    @AfterAll
    public static void tearDownClass() {
        // Clean up any resources
    }
}
