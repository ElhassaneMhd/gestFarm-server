package Gestfarm.UI.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Page object for the Shipment page.
 */
public class ShipmentBasePage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    public ShipmentBasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    private void addDelay(int seconds) {
        try {
            Thread.sleep(seconds); // 5-second delay
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public int getShipmentCount() {
        return driver.findElements(By.xpath("//table//tbody/tr")).size();
    }

    public void clickAddShipment() {
        WebElement addShipmentButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(text(), 'New shipment')]")));
        addShipmentButton.click();
    }

    public void fillShipmentForm(String phone, String address, String shippingDate) {
        WebElement phoneField = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//input[@placeholder='Phone']")));
        phoneField.sendKeys(phone);

        WebElement addressField = driver.findElement(By.xpath("//input[@placeholder='Address']"));
        addressField.sendKeys(address);

        WebElement shippingDateField = driver.findElement(By.xpath("//input[@placeholder='Shipping Date']"));
        shippingDateField.sendKeys(shippingDate);

        // Select Sale
        retryClick(By.xpath("//p[text()='Sale']/following-sibling::button"),
                By.xpath("//div[contains(@class, 'tippy-content')]//li[contains(@class, 'dropdown-option')][1]"));

        // Select Status
        retryClick(By.xpath("//p[contains(text(),'status')]/following-sibling::button"),
                By.xpath("//div[contains(@class, 'tippy-content')]//li[contains(@class, 'dropdown-option')][1]"));

        // Select Shipper
        retryClick(By.xpath("//p[text()='Shipper']/following-sibling::button"),
                By.xpath("//div[contains(@class, 'tippy-content')]//li[contains(@class, 'dropdown-option') and text()='shipper']"));
    }

    private void retryClick(By buttonLocator, By optionLocator) {
        for (int i = 0; i < 3; i++) { // Retry up to 3 times
            try {
                wait.until(ExpectedConditions.elementToBeClickable(buttonLocator)).click();
                wait.until(ExpectedConditions.visibilityOfElementLocated(optionLocator));
                driver.findElement(optionLocator).click();
                return; // Exit if successful
            } catch (StaleElementReferenceException e) {
                // Retry if stale element exception occurs
            }
        }
        throw new StaleElementReferenceException("Failed to interact with element after retries");
    }

    public void submitShipmentForm() {
        // Add a delay to ensure dropdown selections are finalized
        addDelay(2000);

        WebElement submitButton = driver.findElement(By.xpath("//button[contains(text(), 'Add shipment')]"));
        wait.until(ExpectedConditions.elementToBeClickable(submitButton)).click();
    }

    public boolean shipmentExists(String name) {
        return driver.findElements(By.xpath(String.format("//td[contains(text(), '%s')]", name))).size() > 0;
    }

    public void editFirstShipment(String name, String destination, String status) {
        WebElement actionsButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//table//tbody/tr[1]//button[contains(@class, 'actions-button')]")));
        actionsButton.click();

        WebElement editOption = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//li[contains(text(), 'Edit')]")));
        editOption.click();

        // fillShipmentForm(name, destination, status);
        submitShipmentForm();
    }

    public void deleteFirstShipment() {
        WebElement actionsButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//table//tbody/tr[1]//button[contains(@class, 'actions-button')]")));
        actionsButton.click();

        WebElement deleteOption = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//li[contains(text(), 'Delete')]")));
        deleteOption.click();

        WebElement confirmDeleteButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(text(), 'Delete') and contains(@class, 'confirm-button')]")));
        confirmDeleteButton.click();
    }

    public void searchShipment(String name) {
        WebElement searchField = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//input[@type='search']")));
        searchField.sendKeys(name);
    }

    public void openDropdown(String dropdownText) {
        WebElement dropdown = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath(String.format(
                        "//button[contains(@class, 'text-text-primary') and span[contains(text(), '%s')]]",
                        dropdownText))));
        dropdown.click();
    }

    public void selectDropdownOption(String optionText) {
        // Re-locate the dropdown option to avoid stale element reference
        By optionLocator = By
                .xpath(String.format("//li[contains(@class, 'dropdown-option') and text()='%s']", optionText));
        WebElement option = wait.until(ExpectedConditions.elementToBeClickable(optionLocator));
        option.click();
    }

    public void selectFirstSale() {
        openDropdown("Sale");
        selectDropdownOption("");
    }

    public void selectStatus(String status) {
        openDropdown("status");
        // Wait for the dropdown options to be visible
        addDelay(2000);
        selectDropdownOption(status);
    }

    public void selectShipper(String shipper) {
        openDropdown("Shipper");
        // Wait for the dropdown options to be visible
        addDelay(2000);
        selectDropdownOption(shipper);
    }
}
