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
import org.openqa.selenium.support.ui.ExpectedConditions;

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
        // Navigate to sheep page to ensure there's at least one sheep in the system
        AppNavigation navigation = new AppNavigation(driver);

        // Navigate to sales page
        navigation.goToSalesPage();
        salesPage = new SalesPage(driver);

        // Generate unique customer name to avoid conflicts
        String uniqueCustomerName = "Test Customer " ;
        int amount = 1000;
        int price = 5000;

        // Get initial count of sales
        int initialCount = salesPage.getSaleCount();

        // Add a new sale using the form structure
        salesPage.clickAddSale();

        // Interact with the form fields to add a sale
        WebElement clientField = driver.findElement(By.cssSelector("input[placeholder='Client']"));
        clientField.sendKeys(uniqueCustomerName);

        WebElement priceField = driver.findElement(By.cssSelector("input[placeholder='Price']"));
        priceField.sendKeys(String.valueOf(price));

        WebElement amountField = driver.findElement(By.cssSelector("input[placeholder='Amount']"));
        amountField.sendKeys(String.valueOf(amount));

        WebElement statusButton = driver.findElement(By.xpath("//button[contains(@class, 'text-text-primary') and contains(., 'status')]"));
        statusButton.click();
        // Select a status option (assuming a dropdown or similar structure)
        WebElement statusOption = driver.findElement(By.xpath("//li[text()='partially']"));
        statusOption.click();

        // Open the dropdown for selecting sheep
        WebElement sheepDropdown = driver.findElement(By.xpath("//button[contains(@class, 'sheep-toggler')]"));
        sheepDropdown.click();

        // Wait until the checkboxes inside the dropdown are present
        WebElement firstCheckbox = driver.findElement(By.xpath("(//div[contains(@class, 'tippy-box')]//input[@type='checkbox'])[1]"));

        if (!firstCheckbox.isSelected()) {
            firstCheckbox.click();
        }

        // Submit the form
        WebElement submitButton = driver.findElement(By.xpath("//button[text()='Add Sales']"));
        submitButton.click();

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
                sheepPage.fillSheepForm(uniqueSheepName, "Test Breed", 3, 0, 0, uniqueSheepName, uniqueSheepName);
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
                sheepPage.fillSheepForm(uniqueSheepName, "Test Breed", 3, 0, 0, uniqueSheepName, uniqueSheepName);
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

    @Test
    @DisplayName("Test editing a sale")
    public void testEditSale() {
        // Navigate to sales page
        AppNavigation navigation = new AppNavigation(driver);
        navigation.goToSalesPage();
        salesPage = new SalesPage(driver);

        // Locate the actions button (three dots) for the first sale
        WebElement actionsButton = driver.findElement(By.cssSelector("button svg.lucide-ellipsis"));
        actionsButton.click();

        // Wait for the dropdown to appear and click on the Edit option
        WebElement editOption = driver.findElement(By.xpath("//li[contains(@class, 'dropdown-option') and contains(., 'Edit')]"));
        editOption.click();

        // Interact with the form fields to edit the sale
        WebElement clientField = driver.findElement(By.cssSelector("input[placeholder='Client']"));
        clientField.clear();
        clientField.sendKeys("Updated Customer");

        WebElement priceField = driver.findElement(By.cssSelector("input[placeholder='Price']"));
        priceField.clear();
        priceField.sendKeys("6000");

        WebElement amountField = driver.findElement(By.cssSelector("input[placeholder='Amount']"));
        amountField.clear();
        amountField.sendKeys("1200");


        // Submit the form
        WebElement submitButton = driver.findElement(By.xpath("//button[text()='Save Changes']"));
        submitButton.click();

        // Verify the sale was updated
        WebElement updatedClient = driver.findElement(By.xpath("//tr[1]//td[contains(., 'Updated Customer')]"));
        assertNotNull(updatedClient, "The sale should be updated with the new client name");

        WebElement updatedPrice = driver.findElement(By.xpath("//tr[1]//td[contains(., '6000 Dh')]"));
        assertNotNull(updatedPrice, "The sale should be updated with the new price");

        WebElement updatedAmount = driver.findElement(By.xpath("//tr[1]//td[contains(., '1200 Dh')]"));
        assertNotNull(updatedAmount, "The sale should be updated with the new amount");

    }
}
