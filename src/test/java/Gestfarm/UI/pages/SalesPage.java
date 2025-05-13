package Gestfarm.UI.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

/**
 * Page object for the Sales page.
 */
public class SalesPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    // Locators for elements on the sales page - updated based on actual implementation
    private final By addSaleButton = By.xpath("//button[contains(@class, 'add-button') or contains(text(), 'New')]");
    private final By saleTable = By.xpath("//table");
    private final By saleItems = By.xpath("//table//tbody/tr");
    private final By searchInput = By.xpath("//input[@placeholder='Search...']");

    // Locators for the add/edit sale form
    private final By clientNameInput = By.name("name");
    private final By priceInput = By.name("price");
    private final By amountInput = By.name("amount");
    private final By statusSelector = By.xpath("//p[text()='status']/following-sibling::div");
    private final By sheepSelector = By.xpath("//p[text()='Sheep']/following-sibling::div");
    private final By saveButton = By.xpath("//button[contains(text(), 'Save')]");
    private final By cancelButton = By.xpath("//button[contains(text(), 'Cancel')]");

    public SalesPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    /**
     * Navigate to the sales page
     */
    public void navigateTo() {
        driver.get("http://localhost:5173/app/sales");
    }

    /**
     * Click the add sale button to open the form
     */
    public void clickAddSale() {
        wait.until(ExpectedConditions.elementToBeClickable(addSaleButton)).click();
    }

    /**
     * Fill the sale form with the provided details
     */
    public void fillSaleForm(String customerName, String date, String sheepName, int quantity, double price) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(clientNameInput));
        
        // Enter client name
        WebElement nameField = driver.findElement(clientNameInput);
        nameField.clear();
        nameField.sendKeys(customerName);
        
        // Enter price
        WebElement priceField = driver.findElement(priceInput);
        priceField.clear();
        priceField.sendKeys(String.valueOf(price));
        
        // Enter amount
        WebElement amountField = driver.findElement(amountInput);
        amountField.clear();
        amountField.sendKeys(String.valueOf(price * quantity)); // Simple calculation for amount
        
        // Select status
        try {
            driver.findElement(statusSelector).click();
            // Select first status option
            wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//div[contains(@class, 'dropdown')]//div[contains(@class, 'option')][1]"))).click();
        } catch (Exception e) {
            // If selecting status fails, continue
        }
        
        // Select sheep
        try {
            driver.findElement(sheepSelector).click();
            // Since we need multiple sheep, click on several checkboxes
            List<WebElement> sheepCheckboxes = driver.findElements(
                By.xpath("//div[contains(@class, 'dropdown')]//input[@type='checkbox']"));
            
            // Select as many sheep as the quantity parameter specifies
            int selectCount = Math.min(quantity, sheepCheckboxes.size());
            for (int i = 0; i < selectCount; i++) {
                sheepCheckboxes.get(i).click();
            }
            
            // Close the dropdown
            driver.findElement(By.xpath("//body")).click();
        } catch (Exception e) {
            // If selecting sheep fails, continue
        }
    }

    /**
     * Submit the sale form
     */
    public void submitSaleForm() {
        driver.findElement(saveButton).click();
    }

    /**
     * Search for sale using the search input
     */
    public void searchSale(String searchTerm) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(searchInput));
        WebElement searchBox = driver.findElement(searchInput);
        searchBox.clear();
        searchBox.sendKeys(searchTerm);
    }

    /**
     * Get the count of sales displayed in the table
     */
    public int getSaleCount() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(saleTable));
        List<WebElement> sales = driver.findElements(saleItems);
        return sales.size();
    }

    /**
     * Check if a sale with the given customer name exists in the table
     */
    public boolean saleExists(String customerName) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(saleTable));
        
        try {
            // Look for a sale with the given customer name in the table
            By saleCustomerNameLocator = By.xpath(String.format("//td[contains(text(),'%s')]", customerName));
            return !driver.findElements(saleCustomerNameLocator).isEmpty();
        } catch (Exception e) {
            return false;
        }
    }

    // /**
    //  * Delete a sale with the given customer name
    //  */
    // public void deleteSale(String customerName) {
    //     wait.until(ExpectedConditions.visibilityOfElementLocated(saleTable));
        
    //     // Find the row that contains the customer name
    //     By rowLocator = By.xpath(String.format("//td[contains(text(),'%s')]/parent::tr", customerName));
    //     WebElement row = wait.until(ExpectedConditions.presenceOfElementLocated(rowLocator));
        
    //     // Find and click the delete button in the actions column
    //     WebElement deleteButton = row.findElement(By.xpath(".//button[contains(@class, 'delete') or contains(@aria-label, 'Delete')]"));
    //     deleteButton.click();
        
    //      // Confirm deletion if there's a confirmation dialog
    //     try {
    //         By confirmButtonLocator = By.xpath("//div[contains(@class, 'modal')]//button[contains(text(), 'Delete') or contains(text(), 'Confirm') or contains(text(), 'Yes')]");
    //         wait.until(ExpectedConditions.elementToBeClickable(confirmButtonLocator)).click();
    //     } catch (Exception e) {
    //         // No confirmation dialog or couldn't find the button
    //     }
    //     //*[@id="root"]/div[1]/div/div[2]/button[2]
    // }

    // /**
    //  * View details of a sale with the given customer name
    //  */
    // public void viewSaleDetails(String customerName) {
    //     wait.until(ExpectedConditions.visibilityOfElementLocated(saleTable));
        
    //     // Find the row that contains the customer name
    //     By rowLocator = By.xpath(String.format("//td[contains(text(),'%s')]/parent::tr", customerName));
    //     WebElement row = wait.until(ExpectedConditions.presenceOfElementLocated(rowLocator));
        
    //     // Find and click the view details button in the actions column
    //     WebElement viewButton = row.findElement(By.xpath(".//button[contains(@class, 'view') or contains(@class, 'details') or contains(@aria-label, 'View')]"));
    //     viewButton.click();
    // }
}
