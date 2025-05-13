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

    // Locators for elements on the categories page - updated based on actual
    // implementation
    private final By addCategoryButton = By
            .xpath("//button[contains(@class, 'add-button') or contains(text(), 'New')]");
    private final By categoryTable = By.xpath("//table");
    private final By categoryItems = By.xpath("//table//tbody/tr");
    private final By searchInput = By.xpath("//input[@placeholder='Search...']");

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
    public void fillCategoryForm(String name, String description, String price) {
        // Fill name
        By nameInput = By.xpath("//input[@placeholder='Name']");
        wait.until(ExpectedConditions.visibilityOfElementLocated(nameInput));
        WebElement nameField = driver.findElement(nameInput);
        nameField.clear();
        nameField.sendKeys(name);

        // Fill price
        By priceInput = By.xpath("//input[@placeholder='Price (Dh/Kg)']");
        WebElement priceField = driver.findElement(priceInput);
        priceField.clear();
        priceField.sendKeys(price);

        // Fill description
        By descriptionInput = By.xpath("//textarea[@placeholder='Enter category description ...']");
        WebElement descriptionField = driver.findElement(descriptionInput);
        descriptionField.clear();
        descriptionField.sendKeys(description);

    }

    /**
     * Submit the category form
     */
    public void submitCategoryForm() {
        // Ensure the 'Save' button is clicked
        By saveButton = By.xpath("//button[contains(text(), 'Add Categories')]"); // Adjusted XPath to match the modal
                                                                                  // structure
        WebElement saveButtonElement = wait.until(ExpectedConditions.elementToBeClickable(saveButton));
        saveButtonElement.click();
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
        By categoryNameLocator = By.xpath(String.format("//td[contains(text(),'%s')]", categoryName));

        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(categoryNameLocator));

            // Look for a category with the given name in the table
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
        WebElement deleteButton = row
                .findElement(By.xpath(".//button[contains(@class, 'delete') or contains(@aria-label, 'Delete')]"));
        deleteButton.click();

        // Confirm deletion if there's a confirmation dialog
        try {
            By confirmButtonLocator = By.xpath(
                    "//div[contains(@class, 'modal')]//button[contains(text(), 'Delete') or contains(text(), 'Confirm') or contains(text(), 'Yes')]");
            wait.until(ExpectedConditions.elementToBeClickable(confirmButtonLocator)).click();
        } catch (Exception e) {
            // No confirmation dialog or couldn't find the button
        }
    }

    /**
     * Edit the name of a category
     */
    public void editFirstCategory(String newName, String newDescription, String newPrice) {
        // Locate the first row's action icon
        By firstRowActionIcon = By.xpath(
                "//table//tbody/tr[1]//td[contains(@class, 'place-items-end')]//button[contains(@class, 'rounded-[4px]')]");
        wait.until(ExpectedConditions.elementToBeClickable(firstRowActionIcon)).click();

        // Wait for the dropdown and click the Edit option
        By editOption = By.xpath("//div[contains(@class, 'tippy-content')]//li[contains(text(), 'Edit')]");
        wait.until(ExpectedConditions.visibilityOfElementLocated(editOption));
        driver.findElement(editOption).click();

        // Fill the form with new details
        fillCategoryForm(newName, newDescription, newPrice);

        // Submit the form by clicking 'Save Changes'
        WebElement submitButton = driver.findElement(By.xpath("//button[text()='Save Changes']"));
        submitButton.click();
        // Wait for the table to update with the new sheep details
        By updatedSheepLocator = By.xpath(String.format("//td[contains(text(),'%s')]", newName));
        wait.until(ExpectedConditions.presenceOfElementLocated(updatedSheepLocator));

    }
}
