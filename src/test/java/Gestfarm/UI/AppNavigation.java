package Gestfarm.UI;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Helper class for navigating the application sidebar
 */
public class AppNavigation {

    private final WebDriver driver;
    private final WebDriverWait wait;

    // Sidebar menu item locators
    // Updated locator for profile icon to match the actual HTML structure
    private final By sidebarSheepLink = By.xpath("//a[contains(@href, '/app/sheep') or contains(., 'Sheep')]");
    private final By sidebarCategoriesLink = By.xpath("//a[contains(@href, '/app/categories') or contains(., 'Categories')]");
    private final By sidebarSalesLink = By.xpath("//a[contains(@href, '/app/sales') or contains(., 'Sales')]");
    private final By sidebarShipmentsLink = By.xpath("//a[contains(@href, '/app/shipments') or contains(., 'Shipments')]");
    private final By sidebarUsersLink = By.xpath("//a[contains(@href, '/app/users') or contains(., 'Users')]");
    private final By sidebarRolesLink = By.xpath("//a[contains(@href, '/app/roles') or contains(., 'Roles')]");

    public AppNavigation(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    /**
     * Navigate from root to dashboard
     * Updated to skip navigation as admin login already lands on the sheep page
     */
    public void goToDashboardFromRoot() {
        // No navigation needed as admin login lands directly on the sheep page
    }

    /**
     * Navigate to Sheep page
     */
    public void goToSheepPage() {
        wait.until(ExpectedConditions.elementToBeClickable(sidebarSheepLink)).click();
        wait.until(ExpectedConditions.urlContains("/app/sheep"));
    }

    /**
     * Navigate to Categories page
     */
    public void goToCategoriesPage() {
        wait.until(ExpectedConditions.elementToBeClickable(sidebarCategoriesLink)).click();
        wait.until(ExpectedConditions.urlContains("/app/categories"));
    }

    /**
     * Navigate to Sales page
     */
    public void goToSalesPage() {
        wait.until(ExpectedConditions.elementToBeClickable(sidebarSalesLink)).click();
        wait.until(ExpectedConditions.urlContains("/app/sales"));
    }

    /**
     * Navigate to Shipments page
     */
    public void goToShipmentsPage() {
        wait.until(ExpectedConditions.elementToBeClickable(sidebarShipmentsLink)).click();
        wait.until(ExpectedConditions.urlContains("/app/shipments"));
    }

    /**
     * Navigate to Users page
     */
    public void goToUsersPage() {
        wait.until(ExpectedConditions.elementToBeClickable(sidebarUsersLink)).click();
        wait.until(ExpectedConditions.urlContains("/app/users"));
    }

    /**
     * Navigate to Roles page
     */
    public void goToRolesPage() {
        wait.until(ExpectedConditions.elementToBeClickable(sidebarRolesLink)).click();
        wait.until(ExpectedConditions.urlContains("/app/roles"));
    }

    /**
     * Example method to explicitly use the driver field
     */
    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }
}
