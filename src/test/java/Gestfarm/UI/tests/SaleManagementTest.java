package Gestfarm.UI.tests;

import Gestfarm.UI.AppNavigation;
import Gestfarm.UI.BaseSeleniumTest;
import Gestfarm.UI.pages.LoginPage;
import Gestfarm.UI.pages.SalesPage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for sales management functionality
 */
public class SaleManagementTest extends BaseSeleniumTest {

    private SalesPage salesPage;
    
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

    // Add a delay at the end of each test to observe changes in the browser
    private void addDelay(int seconds) {
        try {
            Thread.sleep(seconds); // 5-second delay
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
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

        addDelay(2000);
        // Verify the sale was added
        int newCount = salesPage.getSaleCount();
        assertEquals(initialCount + 1, newCount, "Sale count should increase by 1");

        // Verify the added sale is in the list
        assertTrue(salesPage.saleExists(uniqueCustomerName), "Added sale should appear in the list");
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
        addDelay(3000);
    }


    @Test
    @DisplayName("Test searching for sales")
    public void testSearchSale() {
        // Navigate to sales page
        AppNavigation navigation = new AppNavigation(driver);
        navigation.goToSalesPage();
        salesPage = new SalesPage(driver);
        
        // Locate the search input field and enter the customer name
        WebElement searchField = driver.findElement(By.cssSelector("input[type='search']"));
        searchField.sendKeys("Test Customer");

        // Verify the search results
        WebElement searchResult = driver.findElement(By.xpath("//td[contains(text(), 'Test Customer')]"));
        assertNotNull(searchResult, "Search result should contain the customer name");
            addDelay(3000);

    }

    @Test
    @DisplayName("Test deleting a sale ")
    public void testDeleteSale() {
        // Navigate to sales page
        AppNavigation navigation = new AppNavigation(driver);
        navigation.goToSalesPage();

        salesPage = new SalesPage(driver);

        // Get initial count
        int initialCount = salesPage.getSaleCount();

        // Locate the actions button (three dots) for the sale
        WebElement actionsButton = driver.findElement(By.cssSelector("button svg.lucide-ellipsis"));
        actionsButton.click();

        // Wait for the dropdown to appear and click on the Delete option
        WebElement deleteOption = driver.findElement(By.xpath("//li[contains(@class, 'dropdown-option') and contains(., 'Delete')]"));
        deleteOption.click();

        // Confirm the deletion in the confirmation dialog
        WebElement confirmButton = driver.findElement(By.xpath("//button[contains(@class, 'confirmation-button') and contains(., 'Delete')]"));
        confirmButton.click();

        addDelay(3000);

        // Verify the sale was deleted
        int newCount = salesPage.getSaleCount();
        assertEquals(initialCount - 1, newCount, "Sale count should decrease by 1");
    }

    @Test
    @DisplayName("Test multiple delete with checkboxes")
    public void testMultipleDelete() {
        // Navigate to sales page
        AppNavigation navigation = new AppNavigation(driver);
        navigation.goToSalesPage();
        salesPage = new SalesPage(driver);
        int initialCount = salesPage.getSaleCount();

        
        addDelay(1000);
        // Locate and check the last two checkboxes
        WebElement lastCheckbox1 = driver.findElement(By.xpath("(//input[@type='checkbox'])[last()-1]"));
        WebElement lastCheckbox2 = driver.findElement(By.xpath("(//input[@type='checkbox'])[last()]"));

        if (!lastCheckbox1.isSelected()) {
            lastCheckbox1.click();
        }

        if (!lastCheckbox2.isSelected()) {
            lastCheckbox2.click();
        }

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement deleteButton = wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("//div[contains(@class, 'absolute')]//button[contains(@class, 'bg-red-600') and text()='Delete']")
        ));
        deleteButton.click();

        // Confirm the deletion in the confirmation dialog
        WebElement confirmButton = wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("//button[contains(@class, 'confirmation-button') and contains(., 'Delete')]")
        ));
        confirmButton.click();

        // Add delay to observe changes
        addDelay(2000);

        // Verify the rows are deleted (assuming sale count decreases by 2)
        int newCount = salesPage.getSaleCount();
        assertEquals(initialCount - 2, newCount, "Sale count should decrease by 2 after multiple delete");
    }

}
