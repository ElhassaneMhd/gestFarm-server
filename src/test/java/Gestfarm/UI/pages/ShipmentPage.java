package Gestfarm.UI.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class ShipmentPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

     private void addDelay(int seconds) {
        try {
            Thread.sleep(seconds); // 5-second delay
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public ShipmentPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public int getShipmentCount() {
        return driver.findElements(By.xpath("//table//tbody/tr")).size();
    }

    public void clickAddShipment() {
        WebElement addShipmentButton = wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("//button[contains(text(), 'New shipment')]")
        ));
        addShipmentButton.click();
    }

    public void fillShipmentForm(String phone, String address, String shippingDate) {
        WebElement phoneField = wait.until(ExpectedConditions.visibilityOfElementLocated(
            By.xpath("//input[@placeholder='Phone']")
        ));
        phoneField.sendKeys(phone);

        WebElement addressField = driver.findElement(By.xpath("//input[@placeholder='Address']"));
        addressField.sendKeys(address);

        WebElement shippingDateField = driver.findElement(By.xpath("//input[@placeholder='Shipping Date']"));
        shippingDateField.sendKeys(shippingDate);

        WebElement statusDropdown = driver.findElement(By.xpath("//button[contains(@class, 'bg-background-secondary') and contains(., 'status')]"));
        statusDropdown.click();
 
    }

    public void submitShipmentForm() {
        WebElement submitButton = driver.findElement(By.xpath("//button[contains(text(), 'Add shipment')]"));
        submitButton.click();
    }

    public boolean shipmentExists(String name) {
        return driver.findElements(By.xpath(String.format("//td[contains(text(), '%s')]", name))).size() > 0;
    }

    public void editFirstShipment(String name, String destination, String status) {
        WebElement actionsButton = wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("//table//tbody/tr[1]//button[contains(@class, 'actions-button')]")
        ));
        actionsButton.click();

        WebElement editOption = wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("//li[contains(text(), 'Edit')]")
        ));
        editOption.click();

        // fillShipmentForm(name, destination, status);
        submitShipmentForm();
    }

    public void deleteFirstShipment() {
        WebElement actionsButton = wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("//table//tbody/tr[1]//button[contains(@class, 'actions-button')]")
        ));
        actionsButton.click();

        WebElement deleteOption = wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("//li[contains(text(), 'Delete')]")
        ));
        deleteOption.click();

        WebElement confirmDeleteButton = wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("//button[contains(text(), 'Delete') and contains(@class, 'confirm-button')]")
        ));
        confirmDeleteButton.click();
    }

    public void searchShipment(String name) {
        WebElement searchField = wait.until(ExpectedConditions.visibilityOfElementLocated(
            By.xpath("//input[@type='search']")
        ));
        searchField.sendKeys(name);
    }

    public void openDropdown(String dropdownText) {
        WebElement dropdown = wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath(String.format("//button[contains(@class, 'text-text-primary') and span[contains(text(), '%s')]]", dropdownText))
        ));
        dropdown.click();
    }

    public void selectDropdownOption(String optionText) {
        WebElement option = wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath(String.format("//li[text()='%s']", optionText))
        ));
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
