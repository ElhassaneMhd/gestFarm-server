package Gestfarm.UI.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

/**
 * Page object for the Categories page.
 */
public class CategoriesPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    // Locators for elements on the categories page - updated based on actual implementation
    private final By addCategoryButton = By.xpath("//button[contains(@class, 'add-button') or contains(text(), 'New')]");
    private final By categoryTable = By.xpath("//table");
    private final By categoryItems = By.xpath("//table//tbody/tr");
    private final By searchInput = By.xpath("//input[@placeholder='Search...']");

    // Locators for the add/edit category form
    private final By nameInput = By.name("name");
    private final By priceInput = By.name("price");
    private final By descriptionInput = By.name("description");
    private final By imageUploadArea = By.xpath("//div[contains(@class, 'uploader')]");
    private final By saveButton = By.xpath("//button[contains(text(), 'Save')]");
    private final By cancelButton = By.xpath("//button[contains(text(), 'Cancel')]");

    public CategoriesPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    /**
     * Navigate to the categories page
     */
    public void navigateTo() {
        driver.get("http://localhost:5173/app/categories");
    }

    /**
     * Click the add category button to open the form
     */
    public void clickAddCategory() {
        wait.until(ExpectedConditions.elementToBeClickable(addCategoryButton)).click();
    }

    /**
     * Fill the category form with the provided details
     */
    public void fillCategoryForm(String name, String description) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(nameInput));
        
        // Enter name
        WebElement nameField = driver.findElement(nameInput);
        nameField.clear();
        nameField.sendKeys(name);
        
        // Enter price (required field)
        WebElement priceField = driver.findElement(priceInput);
        priceField.clear();
        priceField.sendKeys("100"); // Default price
        
        // Enter description
        WebElement descField = driver.findElement(descriptionInput);
        descField.clear();
        descField.sendKeys(description);
        
        // Handle image upload if needed
        try {
            // This is a simplified approach - actual implementation may need file upload
            WebElement uploadElement = driver.findElement(imageUploadArea);
            // In a real test, you'd use sendKeys with a file path if it's a standard input type=file
            // or interact with custom uploader components
        } catch (Exception e) {
            // If image upload fails, continue
        }
    }

    /**
     * Submit the category form
     */
    public void submitCategoryForm() {
        driver.findElement(saveButton).click();
    }

    /**
     * Search for category using the search input
     */
    public void searchCategory(String searchTerm) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(searchInput));
        WebElement searchBox = driver.findElement(searchInput);
        searchBox.clear();
        searchBox.sendKeys(searchTerm);
    }

    /**
     * Get the count of categories displayed in the table
     */
    public int getCategoryCount() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(categoryTable));
        List<WebElement> categories = driver.findElements(categoryItems);
        return categories.size();
    }

    /**
     * Check if a category with the given name exists in the table
     */
    public boolean categoryExists(String categoryName) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(categoryTable));
        
        try {
            // Look for a category with the given name in the table
            By categoryNameLocator = By.xpath(String.format("//td[contains(text(),'%s')]", categoryName));
            return !driver.findElements(categoryNameLocator).isEmpty();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Delete a category with the given name
     */
    public void deleteCategory(String categoryName) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(categoryTable));
        
        // Find the row that contains the category name
        By rowLocator = By.xpath(String.format("//td[contains(text(),'%s')]/parent::tr", categoryName));
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
     * Edit a category with the given name
     */
    public void editCategory(String categoryName) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(categoryTable));
        
        // Find the row that contains the category name
        By rowLocator = By.xpath(String.format("//td[contains(text(),'%s')]/parent::tr", categoryName));
        WebElement row = wait.until(ExpectedConditions.presenceOfElementLocated(rowLocator));
        
        // Find and click the edit button in the actions column
        WebElement editButton = row.findElement(By.xpath(".//button[contains(@class, 'edit') or contains(@aria-label, 'Edit')]"));
        editButton.click();
    }
}
