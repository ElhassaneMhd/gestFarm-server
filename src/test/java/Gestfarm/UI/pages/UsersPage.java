package Gestfarm.UI.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

/**
 * Page object for the Users page.
 */
public class UsersPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    // Locators for elements on the users page - updated based on actual implementation
    private final By userTable = By.xpath("//table");
    private final By userItems = By.xpath("//table//tbody/tr");
    private final By searchInput = By.xpath("//input[@placeholder='Search...']");
    // Note: From the code review, the Users page doesn't have add user functionality directly through the UI
    
    public UsersPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    /**
     * Navigate to the users page
     */
    public void navigateTo() {
        driver.get("http://localhost:5173/app/users");
    }

    /**
     * This method would typically click an add user button, but based on the code review,
     * there's no direct UI for adding users. For testing, we might need to add users through API
     * or by navigating to a registration page.
     */
    public void clickAddUser() {
        // In the actual UsersList.jsx, there's no add user button (displayNewRecord: false)
        // This is a placeholder in case the UI changes
        // For testing, we might need to implement a different approach for adding users
        throw new UnsupportedOperationException("Adding users through UI is not supported in the current implementation");
    }

    /**
     * Search for user using the search input
     */
    public void searchUser(String searchTerm) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(searchInput));
        WebElement searchBox = driver.findElement(searchInput);
        searchBox.clear();
        searchBox.sendKeys(searchTerm);
    }

    /**
     * Get the count of users displayed in the table
     */
    public int getUserCount() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(userTable));
        List<WebElement> users = driver.findElements(userItems);
        return users.size();
    }

    /**
     * Check if a user with the given username exists in the table
     */
    public boolean userExists(String username) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(userTable));
        
        try {
            // Look for a user with the given username in the table
            By usernameLocator = By.xpath(String.format("//td[contains(text(),'%s')]", username));
            return !driver.findElements(usernameLocator).isEmpty();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Delete a user with the given username
     */
    public void deleteUser(String username) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(userTable));
        
        // Find the row that contains the username
        By rowLocator = By.xpath(String.format("//td[contains(text(),'%s')]/parent::tr", username));
        WebElement row = wait.until(ExpectedConditions.presenceOfElementLocated(rowLocator));
        
        // Find and click the delete button in the actions column
        WebElement deleteButton = row.findElement(By.xpath(".//button[contains(@class, 'delete') or contains(@aria-label, 'Delete')]"));
        deleteButton.click();
        
        // Confirm deletion if there's a confirmation dialog
        try {
            By confirmButtonLocator = By.xpath("//div[contains(@class, 'modal')]//button[contains(text(), 'Delete') or contains(text(), 'Confirm') or contains(text(), 'Yes')]");
            wait.until(ExpectedConditions.elementToBeClickable(confirmButtonLocator)).click();
        } catch (Exception e) {
            // No confirmation dialog or couldn't find the button
        }
    }
    
    /**
     * Fill user form - this would be used if the UI allows adding/editing users
     * However, based on the code review, this functionality may not be available directly through the UI
     */
    public void fillUserForm(String username, String email, String password, String confirmPassword, String role) {
        // This is a placeholder for future implementation if the UI changes
        throw new UnsupportedOperationException("User form filling is not supported in the current implementation");
    }
    
    /**
     * Submit user form - this would be used if the UI allows adding/editing users
     * However, based on the code review, this functionality may not be available directly through the UI
     */
    public void submitUserForm() {
        // This is a placeholder for future implementation if the UI changes
        throw new UnsupportedOperationException("User form submission is not supported in the current implementation");
    }
    
    /**
     * Edit a user - this would be used if the UI allows editing users
     * However, based on the code review, this functionality may not be available directly through the UI
     */
    public void editUser(String username) {
        // This is a placeholder for future implementation if the UI changes
        throw new UnsupportedOperationException("User editing is not supported in the current implementation");
    }
}
