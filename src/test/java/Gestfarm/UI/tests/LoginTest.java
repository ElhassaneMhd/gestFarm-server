package Gestfarm.UI.tests;

import Gestfarm.UI.BaseSeleniumTest;
import Gestfarm.UI.pages.LoginPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for login functionality
 */
public class LoginTest extends BaseSeleniumTest {  
    @Test
    @DisplayName("Test admin login and redirection")
    public void testAdminLogin() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.navigateTo();
        loginPage.login("admin@gmail.com", "password123");
        assertTrue(loginPage.isLoginSuccessful(), "Admin login should be successful");
        assertEquals("http://localhost:5173/app/sheep", driver.getCurrentUrl(), 
                     "Admin should be redirected to the sheep dashboard");
    }

    @Test
    @DisplayName("Test user login and redirection")
    public void testUserLogin() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.navigateTo();
        loginPage.login("user@gmail.com", "password123");
        assertTrue(loginPage.isLoginSuccessful(), "User login should be successful");
        assertEquals("http://localhost:5173/", driver.getCurrentUrl(), 
                     "User should be redirected to the home page");
    }

    @Test
    @DisplayName("Test farmer login and redirection")
    public void testFarmerLogin() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.navigateTo();
        loginPage.login("farmer@gmail.com", "password123");
        assertTrue(loginPage.isLoginSuccessful(), "Farmer login should be successful");
        assertEquals("http://localhost:5173/app/sheep", driver.getCurrentUrl(), 
                     "Farmer should be redirected to the sheep dashboard");
    }

    @Test
    @DisplayName("Test shipper login and redirection")
    public void testShipperLogin() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.navigateTo();
        loginPage.login("shipper@gmail.com", "password123");
        assertTrue(loginPage.isLoginSuccessful(), "Shipper login should be successful");
        assertEquals("http://localhost:5173/app/shipments", driver.getCurrentUrl(), 
                     "Shipper should be redirected to the shipments dashboard");
    }

    @Test
    @DisplayName("Test unsuccessful login with invalid credentials")
    public void testInvalidCredentials() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.navigateTo();

        // Use invalid credentials
        loginPage.login("wronguser@gmail.com", "wrongpassword");

        // Verify login failed
        assertFalse(loginPage.isLoginSuccessful(), "Login should fail with invalid credentials");

        // Wait for the error message to appear
        WebDriverWait errorWait = new WebDriverWait(driver, Duration.ofSeconds(5));
        WebElement errorMessageElement = errorWait.until(ExpectedConditions.visibilityOfElementLocated(
            By.cssSelector(".error-message"))); // Update the selector if necessary

        // Verify error message is displayed
        String errorMsg = errorMessageElement.getText();
        assertNotNull(errorMsg, "Error message should be displayed");
        assertTrue(errorMsg.toLowerCase().contains("invalid") || 
                   errorMsg.toLowerCase().contains("incorrect") || 
                   errorMsg.toLowerCase().contains("failed"),
                  "Error message should indicate invalid credentials");

        // Wait for the toast error message to appear
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        WebElement toast = wait.until(ExpectedConditions.visibilityOfElementLocated(
            By.cssSelector("section[aria-label='Notifications alt+T'] ol li[data-type='error'] div[data-title]")));

        // Verify toast error message is displayed
        assertNotNull(toast, "Toast error message should be displayed");
        assertEquals("Bad credentials", toast.getText(), "Toast should indicate 'Bad credentials'");
    }

    @Test
    @DisplayName("Test empty credentials validation")
    public void testEmptyCredentials() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.navigateTo();

        // Try to login with empty fields
        loginPage.login("", "");

        // Check if form validation prevents submission or shows appropriate errors
        String errorMsg = loginPage.getErrorMessage();
        assertNotNull(errorMsg, "Error message should be displayed for empty credentials");

        // Check we're still on the login page
        assertTrue(driver.getCurrentUrl().contains("login"), 
                  "Should remain on login page when submitting empty credentials");

        // Wait for the login button to be present
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement loginButton = wait.until(ExpectedConditions.presenceOfElementLocated(
            By.xpath("//button[@type='button' and contains(@class, 'cursor-not-allowed') and @disabled]")));

        // Verify the login button is disabled
        assertNotNull(loginButton, "Login button should be disabled when fields are empty");

        // Verify no error labels are displayed when both inputs are empty
        List<WebElement> errorLabels = driver.findElements(By.cssSelector("span.text-red-500.scale-100"));
        assertTrue(errorLabels.isEmpty(), "No error labels should be displayed when both inputs are empty");

        // Fill the email field and leave the password field empty
        WebElement emailInput = driver.findElement(By.cssSelector("input[type='email']"));
        emailInput.sendKeys("admin@gmail.com");

        // Verify the error label appears for the password field
        WebElement passwordLabel = driver.findElement(By.xpath("//label[text()='Password ']/following-sibling::span[contains(@class, 'text-red-500') and contains(@class, 'scale-100')]"));
        assertNotNull(passwordLabel, "Password label should indicate an error when the field is empty");

        // Clear the email field and fill the password field
        emailInput.clear();
        WebElement passwordInput = driver.findElement(By.cssSelector("input[type='password']"));
        passwordInput.sendKeys("password123");

        // Verify the error label appears for the email field
        WebElement emailLabel = driver.findElement(By.xpath("//label[text()='Email Address']/following-sibling::span[contains(@class, 'text-red-500') and contains(@class, 'scale-100')]"));
        assertNotNull(emailLabel, "Email label should indicate an error when the field is empty");
    }
}
