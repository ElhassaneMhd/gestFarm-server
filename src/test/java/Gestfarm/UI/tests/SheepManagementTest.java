package Gestfarm.UI.tests;

import Gestfarm.UI.AppNavigation;
import Gestfarm.UI.BaseSeleniumTest;
import Gestfarm.UI.pages.LoginPage;
import Gestfarm.UI.pages.SheepPage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for sheep management functionality
 */
public class SheepManagementTest extends BaseSeleniumTest {

    private SheepPage sheepPage;
    private final Random rand = new Random();
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

        // Initialize the sheep page
        sheepPage = new SheepPage(driver);
        // No need to navigate again as we're already on the sheep page after dashboard navigation
    }

    @BeforeEach
    public void setupWait() {
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
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

    @Test
    @DisplayName("Test editing the first sheep")
    public void testEditFirstSheep() {
        // New details for the sheep
        int newNumber = 2000;
        int newWeight = 100;
        String newCategory = "Category B";


        // Edit the first sheep
        sheepPage.editFirstSheep(newNumber, newWeight, newCategory);

        // Verify the changes
        assertTrue(sheepPage.sheepExists(newNumber), "Edited sheep should appear in the list with the new number");
    }

    @Test
    @DisplayName("Test deleting the first sheep")
    public void testDeleteFirstSheep() {
        // Get initial count of sheep
        int initialCount = sheepPage.getSheepCount();

        // Delete the first sheep
        By firstRowActionIcon = By.xpath("//table//tbody/tr[1]//td[contains(@class, 'place-items-end')]//button[contains(@class, 'rounded-[4px]')]");
        wait.until(ExpectedConditions.elementToBeClickable(firstRowActionIcon)).click();

        // Wait for the dropdown and click the Delete option
        By deleteOption = By.xpath("//div[contains(@class, 'tippy-content')]//li[contains(text(), 'Delete')]");
        wait.until(ExpectedConditions.visibilityOfElementLocated(deleteOption));
        driver.findElement(deleteOption).click();

        // Confirm deletion
        By confirmDeleteButton = By.xpath("//button[contains(text(), 'Delete') and contains(@class, 'bg-red-600')]");
        wait.until(ExpectedConditions.elementToBeClickable(confirmDeleteButton)).click();

        // Wait for the table to update after deletion
        wait.until(ExpectedConditions.numberOfElementsToBeLessThan(By.xpath("//table//tbody/tr"), initialCount));

        // Verify the sheep count decreased by 1
        int newCount = sheepPage.getSheepCount();
        assertEquals(initialCount - 1, newCount, "Sheep count should decrease by 1 after deletion");
    }
}
