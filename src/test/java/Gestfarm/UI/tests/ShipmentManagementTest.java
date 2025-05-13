package Gestfarm.UI.tests;

import Gestfarm.UI.AppNavigation;
import Gestfarm.UI.BaseSeleniumTest;
import Gestfarm.UI.pages.LoginPage;
import Gestfarm.UI.pages.ShipmentBasePage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for shipment management functionality
 */
public class ShipmentManagementTest extends BaseSeleniumTest {

    private ShipmentBasePage shipmentPage;
    private WebDriverWait wait;

    @BeforeEach
    public void login() {
        // Login before each test
        LoginPage loginPage = new LoginPage(driver);
        loginPage.navigateTo();
        loginPage.login("admin@gmail.com", "password123"); // Using admin credentials

        // Navigate from root to dashboard
        AppNavigation navigation = new AppNavigation(driver);
        navigation.goToDashboardFromRoot();

        // Navigate to shipments page and initialize the shipment page
        navigation.goToShipmentsPage();
        shipmentPage = new ShipmentBasePage(driver);
    }

    @BeforeEach
    public void setupWait() {
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @Test
    @DisplayName("Test adding a new shipment with sale, status, and shipper selection")
    public void testAddShipment() {
        String phone = "06" + UUID.randomUUID().toString().substring(0, 9).replaceAll("-", "");
        String address = "Test Address";
        String shippingDate = "01-01-2025";

        // Get initial count of shipments
        int initialCount = shipmentPage.getShipmentCount();

        // Add a new shipment
        shipmentPage.clickAddShipment();
        shipmentPage.fillShipmentForm(phone, address, shippingDate,false);
        shipmentPage.submitShipmentForm(false);

        // Wait for the shipment count to update after form submission
        wait.until(ExpectedConditions.numberOfElementsToBe(
                By.xpath("//table//tbody/tr"), initialCount + 1));

        // Verify the shipment was added
        int newCount = shipmentPage.getShipmentCount();
        assertEquals(initialCount + 1, newCount, "Shipment count should increase by 1");

        // Verify the added shipment is in the list
        assertTrue(shipmentPage.shipmentExists(phone), "Added shipment should appear in the list");
    }

    @Test
    @DisplayName("Test editing a shipment")
    public void testEditShipment() {
        String phone = "0666666666"; 
        String address = "the new Address";
        String shippingDate = "01-01-2025";

        // Edit the first shipment
        shipmentPage.editFirstShipment(phone, address, shippingDate);

        // Verify the changes
        assertTrue(shipmentPage.shipmentExists(phone), "Edited shipment should appear in the list with the updated phone");
    }

    @Test
    @DisplayName("Test deleting a shipment")
    public void testDeleteShipment() {
        // Get initial count of shipments
        int initialCount = shipmentPage.getShipmentCount();

        // Locate the actions button (three dots) for the first shipment
        WebElement actionsButton = driver.findElement(By.cssSelector("button svg.lucide-ellipsis"));
        actionsButton.click();

        // Wait for the dropdown to appear and click on the Delete option
        WebElement deleteOption = driver.findElement(By.xpath("//li[contains(@class, 'dropdown-option') and contains(., 'Delete')]"));
        deleteOption.click();

        // Confirm deletion
        WebElement confirmDeleteButton = wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("//button[contains(@class, 'confirmation-button') and text()='Delete']")
        ));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", confirmDeleteButton);
        confirmDeleteButton.click();

        // Wait for the table to update after deletion
        wait.until(ExpectedConditions.numberOfElementsToBeLessThan(By.xpath("//table//tbody/tr"), initialCount));

        // Verify the shipment was deleted
        int newCount = shipmentPage.getShipmentCount();
        assertEquals(initialCount - 1, newCount, "Shipment count should decrease by 1");
    }

    @Test
    @DisplayName("Test multiple delete with checkboxes")
    public void testMultipleDelete() {
        // Get initial count of shipments
        int initialCount = shipmentPage.getShipmentCount();

        // Locate and check the last two checkboxes
        List<WebElement> checkboxes = driver.findElements(By.xpath("//input[@type='checkbox']"));
        int checkboxesToSelect = 2; // Number of checkboxes to select
        for (int i = checkboxes.size() - 1; i >= checkboxes.size() - checkboxesToSelect; i--) {
            if (!checkboxes.get(i).isSelected()) {
                checkboxes.get(i).click();
            }
        }

        // Click the delete button
        WebElement deleteButton = wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("//div[contains(@class, 'absolute')]//button[contains(@class, 'bg-red-600') and text()='Delete']")
        ));
        deleteButton.click();

        // Confirm deletion
        WebElement confirmDeleteButton = wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("//button[contains(@class, 'confirmation-button') and text()='Delete']")
        ));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", confirmDeleteButton);
        confirmDeleteButton.click();

        // Wait for the table to update after deletion
        wait.until(ExpectedConditions.numberOfElementsToBeLessThan(By.xpath("//table//tbody/tr"), initialCount));

        // Verify the rows are deleted (assuming shipment count decreases by 2)
        int newCount = shipmentPage.getShipmentCount();
        assertEquals(initialCount - checkboxesToSelect, newCount, "Shipment count should decrease by the number of selected checkboxes");
    }

    @Test
    @DisplayName("Test searching for a shipment")
    public void testSearchShipment() {
        String shipmentName = "Test Shipment";

        // Search for the shipment
        shipmentPage.searchShipment(shipmentName);

        // Verify the shipment is displayed in the search results
        assertTrue(shipmentPage.shipmentExists(shipmentName), "Shipment should appear in the search results");
    }
}
