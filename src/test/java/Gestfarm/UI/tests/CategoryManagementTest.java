package Gestfarm.UI.tests;

import Gestfarm.UI.AppNavigation;
import Gestfarm.UI.BaseSeleniumTest;
import Gestfarm.UI.pages.CategoriesPage;
import Gestfarm.UI.pages.LoginPage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for category management functionality
 */
public class CategoryManagementTest extends BaseSeleniumTest {

    private CategoriesPage categoriesPage;
    private WebDriverWait wait;

    @BeforeEach
    public void login() {
        // Login before each test
        LoginPage loginPage = new LoginPage(driver);
        loginPage.navigateTo();
        loginPage.login("admin@gmail.com", "password123"); // Using admin credentials

        // Navigate from root to dashboard and then categories
        AppNavigation navigation = new AppNavigation(driver);
        navigation.goToDashboardFromRoot();
        navigation.goToCategoriesPage();

        // Initialize the categories page
        categoriesPage = new CategoriesPage(driver);
    }

    @BeforeEach
    public void setupWait() {
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @Test
    @DisplayName("Test adding a new category")
    public void testAddCategory() {
        // Generate unique name to avoid conflicts
        String uniqueCategoryName = "Test Category " + UUID.randomUUID().toString().substring(0, 8);
        String description = "Test description for automated testing";

        // Get initial count of categories
        int initialCount = categoriesPage.getCategoryCount();

        // Add a new category
        categoriesPage.clickAddCategory();

        // Add a new category with all required fields
        String price = "100"; // Example price

        categoriesPage.fillCategoryForm(uniqueCategoryName, description, price);
        categoriesPage.submitCategoryForm();
        // Verify the category was added
        int newCount = categoriesPage.getCategoryCount();
        assertEquals(initialCount + 1, newCount, "Category count should increase by 1");

        // Verify the added category is in the list
        assertTrue(categoriesPage.categoryExists(uniqueCategoryName), "Added category should appear in the list");
    }

    @Test
    @DisplayName("Test editing the first category")
    public void testEditFirstCategory() {
       // New details for the category
        String newName = "Category B";
        String newDescription = "Updated description for Category B";
        String newPrice = "200";

        // Edit the first category
        categoriesPage.editFirstCategory(newName, newDescription, newPrice);

        // Verify the changes
        assertTrue(categoriesPage.categoryExists(newName), "Edited category should appear in the list with the new name");
    }


    @Test
    @DisplayName("Test deleting the first category")
    public void testDeleteFirstCategory() {
        // Get initial count of categories
        int initialCount = categoriesPage.getCategoryCount();

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

        // Verify the category count decreased by 1
        int newCount = categoriesPage.getCategoryCount();
        assertEquals(initialCount - 1, newCount, "Category count should decrease by 1 after deletion");
    }

    @Test
    @DisplayName("Test searching for a category by name")
    public void testSearchCategory() {
        // Example category name to search for
        String categoryName = "Test Category";

        // Enter the category name in the search input
        By searchInput = By.xpath("//div[@id='root']/div[3]/div/div/div/section/div/div/div[1]/div[1]/div/div//input[@type='search']");
        wait.until(ExpectedConditions.visibilityOfElementLocated(searchInput));
        driver.findElement(searchInput).sendKeys(categoryName);

        // Verify the category with the given name is displayed in the table
        By categoryNameLocator = By.xpath(String.format("//td[contains(text(),'%s')]", categoryName));
        wait.until(ExpectedConditions.visibilityOfElementLocated(categoryNameLocator));
        assertTrue(driver.findElements(categoryNameLocator).size() > 0, "Category with the given name should be displayed in the table");
    }
}
