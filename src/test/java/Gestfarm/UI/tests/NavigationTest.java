package Gestfarm.UI.tests;

import Gestfarm.UI.AppNavigation;
import Gestfarm.UI.BaseSeleniumTest;
import Gestfarm.UI.pages.LoginPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for navigation functionality
 */
public class NavigationTest extends BaseSeleniumTest {

    @Test
    @DisplayName("Test navigation between different sections")
    public void testSectionNavigation() {
        // Login first
        LoginPage loginPage = new LoginPage(driver);
        loginPage.navigateTo();
        loginPage.login("admin@gmail.com", "password123"); // Using admin credentials
        assertTrue(loginPage.isLoginSuccessful(), "Login should be successful");
        
        // Create navigation helper
        AppNavigation navigation = new AppNavigation(driver);
        
        // Navigate from root to dashboard
        navigation.goToDashboardFromRoot();
        assertTrue(driver.getCurrentUrl().contains("/app/sheep"), 
                  "After navigating to dashboard, should be on sheep page");
        
        // Navigate to categories
        navigation.goToCategoriesPage();
        assertTrue(driver.getCurrentUrl().contains("/app/categories"), 
                  "Should navigate to categories page");
        
        // Navigate to sales
        navigation.goToSalesPage();
        assertTrue(driver.getCurrentUrl().contains("/app/sales"), 
                  "Should navigate to sales page");
        
        // Navigate to shipments
        navigation.goToShipmentsPage();
        assertTrue(driver.getCurrentUrl().contains("/app/shipments"), 
                  "Should navigate to shipments page");
        
        // Navigate to users
        navigation.goToUsersPage();
        assertTrue(driver.getCurrentUrl().contains("/app/users"), 
                  "Should navigate to users page");
        
        // Navigate to roles
        navigation.goToRolesPage();
        assertTrue(driver.getCurrentUrl().contains("/app/roles"), 
                  "Should navigate to roles page");
        
        // Navigate back to sheep
        navigation.goToSheepPage();
        assertTrue(driver.getCurrentUrl().contains("/app/sheep"), 
                  "Should navigate back to sheep page");
    }
}
