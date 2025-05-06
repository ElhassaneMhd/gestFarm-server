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

    // Updated locators to match the inspected HTML structure
    private final By usernameInput = By.xpath("//input[@type='email' and @placeholder='Email Address']");
    private final By passwordInput = By.xpath("//input[@type='password' and @placeholder='Password ']");
    private final By loginButton = By.xpath("//button[text()='Login']");
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
        // Wait for the form container to be visible
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName("form")));

        // Wait for the username input to be visible and click to focus
        WebElement usernameField = wait.until(ExpectedConditions.visibilityOfElementLocated(usernameInput));
        usernameField.click();
        usernameField.clear();
        usernameField.sendKeys(username);

        // Wait for the password input to be visible and click to focus
        WebElement passwordField = wait.until(ExpectedConditions.visibilityOfElementLocated(passwordInput));
        passwordField.click();
        passwordField.clear();
        passwordField.sendKeys(password);

        // Click the login button
        WebElement loginBtn = wait.until(ExpectedConditions.elementToBeClickable(loginButton));
        loginBtn.click();
    }

    /**
     * Check if login was successful
     */
    public boolean isLoginSuccessful() {
        // According to the app flow, successful login redirects to root URL "/"
        try {
            // Wait for redirect to happen and URL to change to either / or /app
            wait.until(driver -> 
                driver.getCurrentUrl().equals("http://localhost:5173/") || 
                driver.getCurrentUrl().startsWith("http://localhost:5173/app"));
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
