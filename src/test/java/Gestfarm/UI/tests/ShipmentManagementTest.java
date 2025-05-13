package Gestfarm.UI.tests;

import Gestfarm.UI.AppNavigation;
import Gestfarm.UI.BaseSeleniumTest;
import Gestfarm.UI.pages.LoginPage;
import Gestfarm.UI.pages.ShipmentPage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for shipment management functionality
 */
public class ShipmentManagementTest extends BaseSeleniumTest {

    private ShipmentPage shipmentPage;
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
        shipmentPage = new ShipmentPage(driver);
    }

    @BeforeEach
    public void setupWait() {
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @Test
    @DisplayName("Test adding a new shipment with sale, status, and shipper selection")
    public void testAddShipment() {
        String phone = "0622334455";
        String address = "Test Address";
        String shippingDate = "2025-05-20";
        String status = "pending";
        String shipper = "shipper";

        // Get initial count of shipments
        int initialCount = shipmentPage.getShipmentCount();

        // Add a new shipment
        shipmentPage.clickAddShipment();
        shipmentPage.fillShipmentForm(phone, address, shippingDate);

        
        shipmentPage.openDropdown("status");
        shipmentPage.selectDropdownOption(status);

        shipmentPage.openDropdown("Shipper");   
        shipmentPage.selectDropdownOption(shipper);

        shipmentPage.openDropdown("Sale");
        shipmentPage.selectDropdownOption("First Sale");


        shipmentPage.submitShipmentForm();

        // Verify the shipment was added
        int newCount = shipmentPage.getShipmentCount();
        assertEquals(initialCount + 1, newCount, "Shipment count should increase by 1");

        // Verify the added shipment is in the list
        assertTrue(shipmentPage.shipmentExists(phone), "Added shipment should appear in the list");
    }

    @Test
    @DisplayName("Test editing a shipment")
    public void testEditShipment() {
        String updatedShipmentName = "Updated Shipment";
        String updatedDestination = "Updated Destination";
        String updatedStatus = "Completed";

        // Edit the first shipment
        shipmentPage.editFirstShipment(updatedShipmentName, updatedDestination, updatedStatus);

        // Verify the changes
        assertTrue(shipmentPage.shipmentExists(updatedShipmentName), "Edited shipment should appear in the list with the updated name");
    }

    @Test
    @DisplayName("Test deleting a shipment")
    public void testDeleteShipment() {
        // Get initial count of shipments
        int initialCount = shipmentPage.getShipmentCount();

        // Delete the first shipment
        shipmentPage.deleteFirstShipment();

        // Verify the shipment count decreased by 1
        int newCount = shipmentPage.getShipmentCount();
        assertEquals(initialCount - 1, newCount, "Shipment count should decrease by 1 after deletion");
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
