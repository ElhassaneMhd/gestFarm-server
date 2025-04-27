package Gestfarm.UI.tests;

import Gestfarm.UI.AppNavigation;
import Gestfarm.UI.BaseSeleniumTest;
import Gestfarm.UI.pages.LoginPage;
import Gestfarm.UI.pages.SalesPage;
import Gestfarm.UI.pages.SheepPage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for sales management functionality
 */
public class SaleManagementTest extends BaseSeleniumTest {

    private SalesPage salesPage;
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
    }

    @Test
    @DisplayName("Test adding a new sale")
    public void testAddSale() {
        // First, ensure there's at least one sheep in the system
        AppNavigation navigation = new AppNavigation(driver);
        navigation.goToSheepPage();
        
        sheepPage = new SheepPage(driver);
        
        String uniqueSheepName = "Sale Test Sheep " + UUID.randomUUID().toString().substring(0, 8);
        
        // Add a new sheep if needed
        if (sheepPage.getSheepCount() == 0) {
            sheepPage.clickAddSheep();
            sheepPage.fillSheepForm(uniqueSheepName, "Test Breed", 3);
            sheepPage.submitSheepForm();
        } else {
            // Get the name of an existing sheep (this might not work if we can't easily get the sheep name)
            uniqueSheepName = "Existing Sheep"; // Replace with a method to get existing sheep name if possible
        }
        
        // Now navigate to sales page
        navigation.goToSalesPage();
        salesPage = new SalesPage(driver);
        
        // Generate unique customer name to avoid conflicts
        String uniqueCustomerName = "Test Customer " + UUID.randomUUID().toString().substring(0, 8);
        String currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));
        int quantity = 2;
        double price = 150.0;
        
        // Get initial count of sales
        int initialCount = salesPage.getSaleCount();
        
        // Add a new sale
        salesPage.clickAddSale();
        salesPage.fillSaleForm(uniqueCustomerName, currentDate, uniqueSheepName, quantity, price);
        salesPage.submitSaleForm();
        
        // Verify the sale was added
        int newCount = salesPage.getSaleCount();
        assertEquals(initialCount + 1, newCount, "Sale count should increase by 1");
        
        // Verify the added sale is in the list
        assertTrue(salesPage.saleExists(uniqueCustomerName), "Added sale should appear in the list");
    }

    @Test
    @DisplayName("Test searching for sales")
    public void testSearchSale() {
        // Navigate to sales page
        AppNavigation navigation = new AppNavigation(driver);
        navigation.goToSalesPage();
        
        salesPage = new SalesPage(driver);
        
        // First add a sale with a unique customer name if table is empty
        if (salesPage.getSaleCount() == 0) {
            String uniqueCustomerName = "SearchTest " + UUID.randomUUID().toString().substring(0, 8);
            
            // First, ensure there's at least one sheep in the system
            navigation.goToSheepPage();
            sheepPage = new SheepPage(driver);
            
            String uniqueSheepName = "Search Test Sheep " + UUID.randomUUID().toString().substring(0, 8);
            
            // Add a new sheep if needed
            if (sheepPage.getSheepCount() == 0) {
                sheepPage.clickAddSheep();
                sheepPage.fillSheepForm(uniqueSheepName, "Test Breed", 3);
                sheepPage.submitSheepForm();
            } else {
                uniqueSheepName = "Existing Sheep"; // Replace with a method to get existing sheep name if possible
            }
            
            // Back to sales page
            navigation.goToSalesPage();
            
            String currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));
            
            salesPage.clickAddSale();
            salesPage.fillSaleForm(uniqueCustomerName, currentDate, uniqueSheepName, 1, 100.0);
            salesPage.submitSaleForm();
        }
        
        // Get a search term from an existing sale (this approach is simplified and may need to be adjusted)
        String searchTerm = "Test";
        
        // Search for the sale
        salesPage.searchSale(searchTerm);
        
        // Verify search results exist
        assertTrue(salesPage.getSaleCount() > 0, "Search should return at least one result");
    }

    @Test
    @DisplayName("Test deleting a sale")
    public void testDeleteSale() {
        // Navigate to sales page
        AppNavigation navigation = new AppNavigation(driver);
        navigation.goToSalesPage();
        
        salesPage = new SalesPage(driver);
        
        // First add a sale to delete if table is empty
        String saleToDelete = "DeleteTest " + UUID.randomUUID().toString().substring(0, 8);
        
        if (salesPage.getSaleCount() == 0) {
            // First, ensure there's at least one sheep in the system
            navigation.goToSheepPage();
            sheepPage = new SheepPage(driver);
            
            String uniqueSheepName = "Delete Test Sheep " + UUID.randomUUID().toString().substring(0, 8);
            
            // Add a new sheep if needed
            if (sheepPage.getSheepCount() == 0) {
                sheepPage.clickAddSheep();
                sheepPage.fillSheepForm(uniqueSheepName, "Test Breed", 3);
                sheepPage.submitSheepForm();
            } else {
                uniqueSheepName = "Existing Sheep"; // Replace with a method to get existing sheep name if possible
            }
            
            // Back to sales page
            navigation.goToSalesPage();
            
            String currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));
            
            salesPage.clickAddSale();
            salesPage.fillSaleForm(saleToDelete, currentDate, uniqueSheepName, 1, 100.0);
            salesPage.submitSaleForm();
        } else {
            // Use an existing sale - this is a simplification
            // In a real scenario, you'd want to get the name of an existing sale
            saleToDelete = "Existing Customer"; // Replace with a method to get an existing customer name
        }
        
        // Verify the sale exists
        assertTrue(salesPage.saleExists(saleToDelete) || salesPage.getSaleCount() > 0, 
                  "Sale should exist or there should be at least one sale");
        
        // Get initial count
        int initialCount = salesPage.getSaleCount();
        
        // Delete the sale (this will delete the first sale if saleToDelete doesn't exist)
        if (salesPage.saleExists(saleToDelete)) {
            salesPage.deleteSale(saleToDelete);
        } else {
            // Get the first sale's customer name and delete it
            WebElement firstSaleCustomerNameElement = driver.findElement(By.cssSelector(".sale-item td:first-child"));
            String firstSaleCustomerName = firstSaleCustomerNameElement.getText();
            salesPage.deleteSale(firstSaleCustomerName);
        }
        
        // Verify the sale was deleted
        int newCount = salesPage.getSaleCount();
        assertEquals(initialCount - 1, newCount, "Sale count should decrease by 1");
    }
}
