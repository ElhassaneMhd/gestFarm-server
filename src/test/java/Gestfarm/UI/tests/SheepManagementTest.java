package Gestfarm.UI.tests;

import Gestfarm.UI.AppNavigation;
import Gestfarm.UI.BaseSeleniumTest;
import Gestfarm.UI.pages.LoginPage;
import Gestfarm.UI.pages.SheepPage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for sheep management functionality
 */
public class SheepManagementTest extends BaseSeleniumTest {

    private SheepPage sheepPage;
    
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
        // Generate unique name to avoid conflicts
        String uniqueSheepName = "Test Sheep " + UUID.randomUUID().toString().substring(0, 8);
        String breed = "Merino";
        int age = 2;
        int number = 1001; // Example number
        double weight = 45.5; // Example weight in kg
        String category = "Category A"; // Example category
        String status = "Listed"; // Example status

        // Get initial count of sheep
        int initialCount = sheepPage.getSheepCount();

        // Add a new sheep
        sheepPage.clickAddSheep();
        sheepPage.fillSheepForm(uniqueSheepName, breed, age, number, weight, category, status);
        sheepPage.submitSheepForm();

        // Verify the sheep was added
        int newCount = sheepPage.getSheepCount();
        assertEquals(initialCount + 1, newCount, "Sheep count should increase by 1");

        // Verify the added sheep is in the list
        assertTrue(sheepPage.sheepExists(uniqueSheepName), "Added sheep should appear in the list");
    }

    @Test
    @DisplayName("Test searching for sheep")
    public void testSearchSheep() {
        // First add a sheep with a unique name
        String uniqueSheepName = "SearchTest " + UUID.randomUUID().toString().substring(0, 8);
        int number = 1002; // Example number
        double weight = 50.0; // Example weight in kg
        String category = "Category B"; // Example category
        String status = "Available"; // Example status

        sheepPage.clickAddSheep();
        sheepPage.fillSheepForm(uniqueSheepName, "Suffolk", 3, number, weight, category, status);
        sheepPage.submitSheepForm();

        // Verify the sheep was added
        assertTrue(sheepPage.sheepExists(uniqueSheepName), "Added sheep should appear in the list");

        // Search for the sheep
        sheepPage.searchSheep(uniqueSheepName);

        // Verify search results
        assertEquals(1, sheepPage.getSheepCount(), "Search should return exactly one result");
        assertTrue(sheepPage.sheepExists(uniqueSheepName), "Search result should contain the searched sheep");
    }

    @Test
    @DisplayName("Test deleting a sheep")
    public void testDeleteSheep() {
        // First add a sheep to delete
        String sheepToDelete = "DeleteTest " + UUID.randomUUID().toString().substring(0, 8);
        int number = 1003; // Example number
        double weight = 55.0; // Example weight in kg
        String category = "Category C"; // Example category
        String status = "Reserved"; // Example status

        sheepPage.clickAddSheep();
        sheepPage.fillSheepForm(sheepToDelete, "Dorper", 1, number, weight, category, status);
        sheepPage.submitSheepForm();

        // Verify the sheep was added
        assertTrue(sheepPage.sheepExists(sheepToDelete), "Added sheep should appear in the list");

        // Get initial count
        int initialCount = sheepPage.getSheepCount();

        // Delete the sheep
        sheepPage.deleteSheep(sheepToDelete);

        // Verify the sheep was deleted
        int newCount = sheepPage.getSheepCount();
        assertEquals(initialCount - 1, newCount, "Sheep count should decrease by 1");
        assertFalse(sheepPage.sheepExists(sheepToDelete), "Deleted sheep should not appear in the list");
    }
}
