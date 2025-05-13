package Gestfarm.UI.tests;

import Gestfarm.UI.AppNavigation;
import Gestfarm.UI.BaseSeleniumTest;
import Gestfarm.UI.pages.LoginPage;
import Gestfarm.UI.pages.SheepPage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for sheep management functionality
 */
public class SheepManagementTest extends BaseSeleniumTest {

    private SheepPage sheepPage;
    private final Random rand = new Random();
    
    @BeforeEach
    public void login() {
        // Login before each test
        LoginPage loginPage = new LoginPage(driver);
        loginPage.navigateTo();
        loginPage.login("admin@gmail.com", "password123"); // Using admin credentials
        
        // Navigate from root to dashboard
        AppNavigation navigation = new AppNavigation(driver);
        navigation.goToDashboardFromRoot();
        
        // Initialize the sheep page
        sheepPage = new SheepPage(driver);
        // No need to navigate again as we're already on the sheep page after dashboard navigation
    }

    @Test
    @DisplayName("Test adding a new sheep")
    public void testAddSheep() {
        int age = 2;
        int number = rand.nextInt(1000); // Example number
        int weight = 80; // Example weight in kg
        String category = "Category A"; // Example category
        String status = "Listed"; // Example status

        // Get initial count of sheep
        int initialCount = sheepPage.getSheepCount();

        // Add a new sheep
        sheepPage.clickAddSheep();
        // Updated to match the correct method signature
        sheepPage.fillSheepForm(number, weight, category, status, String.valueOf(age));
        sheepPage.submitSheepForm();

        // Verify the sheep was added
        int newCount = sheepPage.getSheepCount();
        assertEquals(initialCount + 1, newCount, "Sheep count should increase by 1");

        // Verify the added sheep is in the list
        assertTrue(sheepPage.sheepExists(number), "Added sheep should appear in the list");
    }

      
}
