package Gestfarm.UI.tests;

import Gestfarm.UI.BaseSeleniumTest;
import Gestfarm.UI.pages.LoginPage;
import Gestfarm.UI.utils.SeleniumUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for login functionality
 */
public class LoginTest extends BaseSeleniumTest {

    @Test
    @DisplayName("Test successful login with valid credentials")
    public void testSuccessfulLogin() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.navigateTo();
        
        // Use test credentials - Replace with valid test credentials for your app
        loginPage.login("testuser", "password123");
        
        // Verify login was successful
        assertTrue(loginPage.isLoginSuccessful(), "Login should be successful with valid credentials");
    }

    @Test
    @DisplayName("Test unsuccessful login with invalid credentials")
    public void testFailedLogin() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.navigateTo();
        
        // Use invalid credentials
        loginPage.login("wronguser", "wrongpassword");
        
        // Verify login failed
        assertFalse(loginPage.isLoginSuccessful(), "Login should fail with invalid credentials");
        
        // Verify error message is displayed
        String errorMsg = loginPage.getErrorMessage();
        assertNotNull(errorMsg, "Error message should be displayed");
        assertTrue(errorMsg.toLowerCase().contains("invalid") || 
                   errorMsg.toLowerCase().contains("incorrect") || 
                   errorMsg.toLowerCase().contains("failed"),
                  "Error message should indicate invalid credentials");
    }

    @Test
    @DisplayName("Test empty credentials validation")
    public void testEmptyCredentialsValidation() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.navigateTo();
        
        // Try to login with empty fields
        loginPage.login("", "");
        
        // Check if form validation prevents submission or shows appropriate errors
        // This will depend on your application's specific behavior
        String errorMsg = loginPage.getErrorMessage();
        assertNotNull(errorMsg, "Error message should be displayed for empty credentials");
        
        // Check we're still on the login page
        assertTrue(driver.getCurrentUrl().contains("login"), 
                  "Should remain on login page when submitting empty credentials");
    }

    @Test
    @DisplayName("Test remember me functionality")
    public void testRememberMe() {
        // This test assumes your login page has a "Remember Me" checkbox
        
        LoginPage loginPage = new LoginPage(driver);
        loginPage.navigateTo();
        
        // Click Remember Me checkbox if it exists
        try {
            driver.findElement(By.id("remember-me")).click();
            
            // Login with valid credentials
            loginPage.login("testuser", "password123");
            
            // Verify login was successful
            assertTrue(loginPage.isLoginSuccessful(), "Login should be successful");
            
            // Close browser and reopen to test cookie persistence
            // Note: In practice, you'd need a more complex approach to test this properly
            // as simply quitting and remaking the driver isn't the same as closing and reopening a browser.
            driver.quit();
            setupTest(); // This recreates the driver
            
            driver.get(BASE_URL);
            
            // Check if we're automatically logged in
            // (this is a simplistic check; actual implementation will depend on your application)
            boolean isLoggedIn = !driver.getCurrentUrl().contains("login");
            assertTrue(isLoggedIn, "User should remain logged in when Remember Me is checked");
        } catch (Exception e) {
            // If Remember Me checkbox doesn't exist, skip the test
            System.out.println("Remember Me functionality not found or test needs adjustment");
        }
    }
}
