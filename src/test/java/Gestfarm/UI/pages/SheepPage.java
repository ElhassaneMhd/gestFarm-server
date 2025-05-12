package Gestfarm.UI.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

/**
 * Page object for the Sheep management page.
 */
public class SheepPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    // Locators for elements on the sheep page - updated based on actual React implementation
    private final By sheepTable = By.xpath("//table");
    private final By sheepItems = By.xpath("//table//tbody/tr");
    private final By searchInput = By.xpath("//input[@placeholder='Search...']");

    // Locators for the add/edit sheep form
    private final By numberInput = By.name("number");
    private final By weightInput = By.name("weight");
    private final By categorySelector = By.xpath("//p[text()='Category']/following-sibling::div");
    private final By statusSelector = By.xpath("//p[text()='status']/following-sibling::div");
    private final By ageSelector = By.xpath("//p[text()='age']/following-sibling::div");
    private final By saveButton = By.xpath("//button[contains(text(), 'Save')]");

    public SheepPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    /**
     * Navigate to the sheep page
     */
    public void navigateTo() {
        driver.get("http://localhost:5173/app/sheep");
    }

    /**
     * Click the add sheep button to open the form
     */
    public void clickAddSheep() {
        // Updated the locator to match the correct button
        By addSheepButton = By.cssSelector("button.new-record-button");
        wait.until(ExpectedConditions.visibilityOfElementLocated(addSheepButton));
        wait.until(ExpectedConditions.elementToBeClickable(addSheepButton)).click();
    }

    /**
     * Fill the sheep form with the provided details
     */
    public void fillSheepForm(String name, String breed, int age, int number, double weight, String category, String status) {
        // Fill number
        wait.until(ExpectedConditions.visibilityOfElementLocated(numberInput));
        WebElement numberField = driver.findElement(numberInput);
        numberField.clear();
        numberField.sendKeys(String.valueOf(number));

        // Fill weight
        WebElement weightField = driver.findElement(weightInput);
        weightField.clear();
        weightField.sendKeys(String.valueOf(weight));

        // Select category
        try {
            driver.findElement(categorySelector).click();
            wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath(String.format("//div[contains(@class, 'dropdown')]//div[contains(text(), '%s')]", category)))).click();
        } catch (Exception e) {
            // If selecting category fails, continue
        }

        // Select status
        try {
            driver.findElement(statusSelector).click();
            wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath(String.format("//div[contains(@class, 'dropdown')]//div[contains(text(), '%s')]", status)))).click();
        } catch (Exception e) {
            // If selecting status fails, continue
        }

        // Select age
        try {
            driver.findElement(ageSelector).click();
            wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath(String.format("//div[contains(@class, 'dropdown')]//div[contains(text(), '%s')]", age)))).click();
        } catch (Exception e) {
            // If selecting age fails, continue
        }
    }

    /**
     * Submit the sheep form
     */
    public void submitSheepForm() {
        driver.findElement(saveButton).click();
    }

    /**
     * Search for sheep using the search input
     */
    public void searchSheep(String searchTerm) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(searchInput));
        WebElement searchBox = driver.findElement(searchInput);
        searchBox.clear();
        searchBox.sendKeys(searchTerm);
    }

    /**
     * Get the count of sheep displayed in the table
     */
    public int getSheepCount() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(sheepTable));
        List<WebElement> sheep = driver.findElements(sheepItems);
        return sheep.size();
    }

    /**
     * Check if a sheep with the given number exists in the table
     */
    public boolean sheepExists(String sheepNumber) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(sheepTable));
        
        try {
            // Look for a sheep with the given number in the table
            By sheepNumberLocator = By.xpath(String.format("//td[contains(text(),'%s')]", sheepNumber));
            return !driver.findElements(sheepNumberLocator).isEmpty();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Delete a sheep with the given number
     */
    public void deleteSheep(String sheepNumber) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(sheepTable));
        
        // Find the row that contains the sheep number
        By rowLocator = By.xpath(String.format("//td[contains(text(),'%s')]/parent::tr", sheepNumber));
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
}
