package Gestfarm.UI.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Page object for the login page.
 */
public class LoginPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    // Locators for elements on the login page - updated based on actual implementation
    private final By usernameInput = By.name("username"); 
    private final By passwordInput = By.name("password");
    private final By loginButton = By.xpath("//button[contains(text(), 'Log in') or contains(text(), 'Login')]");
    private final By errorMessage = By.className("error-message"); // Adjust if error message has specific class

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    /**
     * Navigate to the login page
     */
    public void navigateTo() {
        driver.get("http://localhost:5173/login"); // Login URL doesn't have /app prefix
    }

    /**
     * Perform login with the specified credentials
     */
    public void login(String username, String password) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(usernameInput));
        driver.findElement(usernameInput).sendKeys(username);
        driver.findElement(passwordInput).sendKeys(password);
        driver.findElement(loginButton).click();
    }

    /**
     * Check if login was successful
     */
    public boolean isLoginSuccessful() {
        // Check if redirected to dashboard or home page
        // This depends on your app's behavior after login
        try {
            wait.until(ExpectedConditions.urlContains("dashboard")); // or any URL that indicates successful login
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Get error message if login failed
     */
    public String getErrorMessage() {
        try {
            WebElement errorElement = wait.until(ExpectedConditions.visibilityOfElementLocated(errorMessage));
            return errorElement.getText();
        } catch (Exception e) {
            return null;
        }
    }
}
