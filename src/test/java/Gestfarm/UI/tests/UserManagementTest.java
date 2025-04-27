package Gestfarm.UI.tests;

import Gestfarm.UI.BaseSeleniumTest;
import Gestfarm.UI.pages.LoginPage;
import Gestfarm.UI.pages.UsersPage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for user management functionality
 */
public class UserManagementTest extends BaseSeleniumTest {

    private UsersPage usersPage;
    
    @BeforeEach
    public void login() {
        // Login before each test
        LoginPage loginPage = new LoginPage(driver);
        loginPage.navigateTo();
        loginPage.login("admin", "adminPassword"); // Use admin credentials to have permissions
        
        // Navigate to users page
        usersPage = new UsersPage(driver);
        usersPage.navigateTo();
    }

    @Test
    @DisplayName("Test adding a new user")
    public void testAddUser() {
        // Generate unique username to avoid conflicts
        String uniqueUsername = "testuser_" + UUID.randomUUID().toString().substring(0, 8);
        String email = uniqueUsername + "@example.com";
        String password = "Password123!";
        String role = "User"; // Assuming "User" is a valid role
        
        // Get initial count of users
        int initialCount = usersPage.getUserCount();
        
        // Add a new user
        usersPage.clickAddUser();
        usersPage.fillUserForm(uniqueUsername, email, password, password, role);
        usersPage.submitUserForm();
        
        // Verify the user was added
        int newCount = usersPage.getUserCount();
        assertEquals(initialCount + 1, newCount, "User count should increase by 1");
        
        // Verify the added user is in the list
        assertTrue(usersPage.userExists(uniqueUsername), "Added user should appear in the list");
    }

    @Test
    @DisplayName("Test searching for users")
    public void testSearchUser() {
        // First add a user with a unique username
        String uniqueUsername = "searchuser_" + UUID.randomUUID().toString().substring(0, 8);
        String email = uniqueUsername + "@example.com";
        String password = "Password123!";
        String role = "User";
        
        usersPage.clickAddUser();
        usersPage.fillUserForm(uniqueUsername, email, password, password, role);
        usersPage.submitUserForm();
        
        // Verify the user was added
        assertTrue(usersPage.userExists(uniqueUsername), "Added user should appear in the list");
        
        // Search for the user
        usersPage.searchUser(uniqueUsername);
        
        // Verify search results
        assertEquals(1, usersPage.getUserCount(), "Search should return exactly one result");
        assertTrue(usersPage.userExists(uniqueUsername), "Search result should contain the searched user");
    }

    @Test
    @DisplayName("Test editing a user")
    public void testEditUser() {
        // First add a user to edit
        String userToEdit = "edituser_" + UUID.randomUUID().toString().substring(0, 8);
        String originalEmail = userToEdit + "@example.com";
        String password = "Password123!";
        String role = "User";
        
        usersPage.clickAddUser();
        usersPage.fillUserForm(userToEdit, originalEmail, password, password, role);
        usersPage.submitUserForm();
        
        // Verify the user was added
        assertTrue(usersPage.userExists(userToEdit), "Added user should appear in the list");
        
        // Edit the user
        usersPage.editUser(userToEdit);
        
        // Update the form with new data
        String updatedEmail = "updated_" + userToEdit + "@example.com";
        usersPage.fillUserForm(userToEdit, updatedEmail, "", "", "Admin"); // Assuming empty password fields means no password change
        usersPage.submitUserForm();
        
        // Verify the user still exists after update
        assertTrue(usersPage.userExists(userToEdit), "Edited user should still appear in the list");
    }

    @Test
    @DisplayName("Test deleting a user")
    public void testDeleteUser() {
        // First add a user to delete
        String userToDelete = "deleteuser_" + UUID.randomUUID().toString().substring(0, 8);
        String email = userToDelete + "@example.com";
        String password = "Password123!";
        String role = "User";
        
        usersPage.clickAddUser();
        usersPage.fillUserForm(userToDelete, email, password, password, role);
        usersPage.submitUserForm();
        
        // Verify the user was added
        assertTrue(usersPage.userExists(userToDelete), "Added user should appear in the list");
        
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
