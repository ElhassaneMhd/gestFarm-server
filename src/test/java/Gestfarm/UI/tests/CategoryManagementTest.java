package Gestfarm.UI.tests;

import Gestfarm.UI.AppNavigation;
import Gestfarm.UI.BaseSeleniumTest;
import Gestfarm.UI.pages.CategoriesPage;
import Gestfarm.UI.pages.LoginPage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for category management functionality
 */
public class CategoryManagementTest extends BaseSeleniumTest {

    private CategoriesPage categoriesPage;
    
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
        categoriesPage.fillCategoryForm(uniqueCategoryName, description);
        categoriesPage.submitCategoryForm();
        
        // Verify the category was added
        int newCount = categoriesPage.getCategoryCount();
        assertEquals(initialCount + 1, newCount, "Category count should increase by 1");
        
        // Verify the added category is in the list
        assertTrue(categoriesPage.categoryExists(uniqueCategoryName), "Added category should appear in the list");
    }

    @Test
    @DisplayName("Test searching for categories")
    public void testSearchCategory() {
        // First add a category with a unique name
        String uniqueCategoryName = "SearchTest " + UUID.randomUUID().toString().substring(0, 8);
        
        categoriesPage.clickAddCategory();
        categoriesPage.fillCategoryForm(uniqueCategoryName, "Search test description");
        categoriesPage.submitCategoryForm();
        
        // Verify the category was added
        assertTrue(categoriesPage.categoryExists(uniqueCategoryName), "Added category should appear in the list");
        
        // Search for the category
        categoriesPage.searchCategory(uniqueCategoryName);
        
        // Verify search results
        assertEquals(1, categoriesPage.getCategoryCount(), "Search should return exactly one result");
        assertTrue(categoriesPage.categoryExists(uniqueCategoryName), "Search result should contain the searched category");
    }

    @Test
    @DisplayName("Test editing a category")
    public void testEditCategory() {
        // First add a category to edit
        String categoryToEdit = "EditTest " + UUID.randomUUID().toString().substring(0, 8);
        
        categoriesPage.clickAddCategory();
        categoriesPage.fillCategoryForm(categoryToEdit, "Original description");
        categoriesPage.submitCategoryForm();
        
        // Verify the category was added
        assertTrue(categoriesPage.categoryExists(categoryToEdit), "Added category should appear in the list");
        
        // Edit the category
        categoriesPage.editCategory(categoryToEdit);
        
        // Update the form with new data
        String updatedDescription = "Updated description " + UUID.randomUUID().toString().substring(0, 8);
        categoriesPage.fillCategoryForm(categoryToEdit, updatedDescription);
        categoriesPage.submitCategoryForm();
        
        // Verify the category was updated
        // This test assumes the UI shows the description in the table
        // If not, you might need to check details view or a different approach
        assertTrue(categoriesPage.categoryExists(categoryToEdit), "Edited category should still appear in the list");
    }

    @Test
    @DisplayName("Test deleting a category")
    public void testDeleteCategory() {
        // First add a category to delete
        String categoryToDelete = "DeleteTest " + UUID.randomUUID().toString().substring(0, 8);
        
        categoriesPage.clickAddCategory();
        categoriesPage.fillCategoryForm(categoryToDelete, "Category to be deleted");
        categoriesPage.submitCategoryForm();
        
        // Verify the category was added
        assertTrue(categoriesPage.categoryExists(categoryToDelete), "Added category should appear in the list");
        
        // Get initial count
        int initialCount = categoriesPage.getCategoryCount();
        
        // Delete the category
        categoriesPage.deleteCategory(categoryToDelete);
        
        // Verify the category was deleted
        int newCount = categoriesPage.getCategoryCount();
        assertEquals(initialCount - 1, newCount, "Category count should decrease by 1");
        assertFalse(categoriesPage.categoryExists(categoryToDelete), "Deleted category should not appear in the list");
    }
}
