package Gestfarm.UI.tests;

import Gestfarm.UI.AppNavigation;
import Gestfarm.UI.BaseSeleniumTest;
import Gestfarm.UI.pages.LoginPage;
import Gestfarm.UI.pages.UsersPage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for user management functionality
 * Note: Based on the code review, the Users page doesn't have direct UI for adding users,
 * so we'll adjust these tests to focus on viewing and searching users instead.
 */
public class UserManagementTest extends BaseSeleniumTest {

    private UsersPage usersPage;
    
    @BeforeEach
    public void login() {
        // Login before each test
        LoginPage loginPage = new LoginPage(driver);
        loginPage.navigateTo();
        loginPage.login("admin@gmail.com", "password123"); // Using admin credentials
        
        // Navigate from root to dashboard and then users
        AppNavigation navigation = new AppNavigation(driver);
        navigation.goToDashboardFromRoot();
        navigation.goToUsersPage();
        
        // Initialize the users page
        usersPage = new UsersPage(driver);
    }

    @Test
    @DisplayName("Test searching for users")
    public void testSearchUser() {
        // Search for the admin user
        usersPage.searchUser("admin");
        
        // Verify search results
        assertTrue(usersPage.getUserCount() > 0, "Search should return at least one result");
        assertTrue(usersPage.userExists("admin@gmail.com"), "Search result should contain admin user");
    }

    @Test
    @DisplayName("Test user listing")
    public void testUserListing() {
        // Verify that the users table is displayed
        assertTrue(usersPage.getUserCount() > 0, "User table should have at least one user");
        
        // Verify admin user is in the list
        assertTrue(usersPage.userExists("admin@gmail.com"), "Admin user should appear in the list");
    }
    
    @Test
    @DisplayName("Test deleting a user")
    public void testDeleteUser() {
        // Note: This test would need careful consideration in a real environment
        // as you wouldn't want to delete important users
        
        // If there's only one user (admin), we should skip this test
        if (usersPage.getUserCount() <= 1) {
            System.out.println("Skipping delete user test as there's only the admin user");
            return;
        }
        
        // Find a non-admin user to delete
        WebElement nonAdminUserRow = null;
        try {
            nonAdminUserRow = driver.findElement(By.xpath("//td[not(contains(text(), 'admin@gmail.com'))]/parent::tr"));
        } catch (Exception e) {
            System.out.println("No non-admin user found, skipping delete test");
            return;
        }
        
        // Get the username of the user to delete
        String userToDelete = nonAdminUserRow.findElement(By.xpath("./td[2]")).getText(); // Assuming username is in the second column
        
        // Get initial count
        int initialCount = usersPage.getUserCount();
        
        // Delete the user
        usersPage.deleteUser(userToDelete);
        
        // Verify the user was deleted
        int newCount = usersPage.getUserCount();
        assertEquals(initialCount - 1, newCount, "User count should decrease by 1");
        assertFalse(usersPage.userExists(userToDelete), "Deleted user should not appear in the list");
    }
}
