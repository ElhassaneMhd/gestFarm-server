package Gestfarm.UI.tests;

import Gestfarm.UI.BaseSeleniumTest;
import Gestfarm.UI.pages.LoginPage;
import Gestfarm.UI.pages.CategoriesPage;
import Gestfarm.UI.pages.SheepPage;
import Gestfarm.UI.pages.SalesPage;
import Gestfarm.UI.pages.ShipmentBasePage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Global test for login/logout and CRUD flows for Category, Sheep, Sale, and Shipment
 */
public class GlobalCrudFlowTest extends BaseSeleniumTest {
    private final WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

    private void addDelay(int ms) {
        try { Thread.sleep(ms); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
    }

    @Test
    @DisplayName("Global CRUD Flow: Login, Logout, Category, Sheep, Sale, Shipment")
    public void testGlobalCrudFlow() {
        // 1. Login
        LoginPage loginPage = new LoginPage(driver);
        loginPage.navigateTo();
        loginPage.login("admin@gmail.com", "password123");
        assertTrue(loginPage.isLoginSuccessful(), "Login should be successful");
        addDelay(1000);

        // 2. Logout
        WebElement logoutButton = driver.findElement(By.cssSelector("button.sidebar-element"));
        logoutButton.click();
        addDelay(1000);
       // Wait for the logout confirmation dialog to appear
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        WebElement confirmationButton = wait.until(ExpectedConditions.elementToBeClickable(
            By.cssSelector("button.confirmation-button")));
        confirmationButton.click();
        assertFalse(driver.getCurrentUrl().contains("app"), "URL should not contain 'app' after logout");
        addDelay(2000);

        addDelay(2000);
        // 3. Login again for CRUD flows
        loginPage.navigateTo();
        loginPage.login("admin@gmail.com", "password123");
        assertTrue(loginPage.isLoginSuccessful(), "Login should be successful for CRUD flows");
        addDelay(1000);

        // 4. Category CRUD
        CategoriesPage categoriesPage = new CategoriesPage(driver);
        // Click Categories in sidebar
        WebElement categoriesSidebar = driver.findElement(By.xpath("//a[contains(@class, 'sidebar-element') and contains(., 'Categories')]"));
        categoriesSidebar.click();
        String uniqueCategoryName = "Test Category " + UUID.randomUUID().toString().substring(0, 3);
        String description = "Test description";
        String price = "100";
        int initialCategoryCount;
        try {
            initialCategoryCount = categoriesPage.getCategoryCount();
        } catch (Exception e) {
            initialCategoryCount = 0;
        }
        categoriesPage.clickAddCategory();
        categoriesPage.fillCategoryForm(uniqueCategoryName, description, price);
        categoriesPage.submitCategoryForm();
        addDelay(2000);
        // Search Category
        By searchInput = By.xpath("//div[@id='root']/div[3]/div/div/div/section/div/div/div[1]/div[1]/div/div//input[@type='search']");
        wait.until(ExpectedConditions.visibilityOfElementLocated(searchInput));
        WebElement categorySearchInput = driver.findElement(searchInput);
        categorySearchInput.clear();
        categorySearchInput.sendKeys(" ");
        categorySearchInput.sendKeys(org.openqa.selenium.Keys.BACK_SPACE);
        categorySearchInput.sendKeys(uniqueCategoryName);
        addDelay(1500);
        By categoryNameLocator = By.xpath(String.format("//td[contains(text(),'%s')]", uniqueCategoryName));
        wait.until(ExpectedConditions.visibilityOfElementLocated(categoryNameLocator));
        assertTrue(!driver.findElements(categoryNameLocator).isEmpty(), "Category with the given name should be displayed in the table");
        categorySearchInput.clear();
        categorySearchInput.sendKeys(" ");
        categorySearchInput.sendKeys(org.openqa.selenium.Keys.BACK_SPACE);
        // Remove 'search' query param from URL (if present)
        ((org.openqa.selenium.JavascriptExecutor) driver).executeScript(
            "const url = new URL(window.location); url.searchParams.delete('search'); window.history.replaceState({}, document.title, url); window.dispatchEvent(new PopStateEvent('popstate'));"
        );
        addDelay(2000);
        // Edit
        String newName = uniqueCategoryName + "_edit";
        String newDescription = "Updated desc";
        String newPrice = "200";
        categoriesPage.editFirstCategory(newName, newDescription, newPrice);
        assertTrue(categoriesPage.categoryExists(newName), "Edited category should appear in the list");
        addDelay(2000);
        // Delete
        int beforeDeleteCategoryCount;
        try {
            beforeDeleteCategoryCount = categoriesPage.getCategoryCount();
        } catch (Exception e) {
            beforeDeleteCategoryCount = 1;
        }
        By firstRowActionIcon = By.xpath("//table//tbody/tr[1]//td[contains(@class, 'place-items-end')]//button[contains(@class, 'rounded-[4px]')]");
        wait.until(ExpectedConditions.elementToBeClickable(firstRowActionIcon)).click();
        By deleteOption = By.xpath("//div[contains(@class, 'tippy-content')]//li[contains(text(), 'Delete')]");
        wait.until(ExpectedConditions.visibilityOfElementLocated(deleteOption));
        driver.findElement(deleteOption).click();
        By confirmDeleteButton = By.xpath("//button[contains(text(), 'Delete') and contains(@class, 'bg-red-600')]");
        wait.until(ExpectedConditions.elementToBeClickable(confirmDeleteButton)).click();
        int expectedCategoryCount = beforeDeleteCategoryCount - 1;
        if (expectedCategoryCount == 0) {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//table//tbody/tr")));
        } else {
            wait.until(ExpectedConditions.numberOfElementsToBe(By.xpath("//table//tbody/tr"), expectedCategoryCount));
        }
        assertEquals(expectedCategoryCount, categoriesPage.getCategoryCount(), "Category count should decrease by 1 after deletion");
        addDelay(1000);

        // 5. Sheep CRUD
        SheepPage sheepPage = new SheepPage(driver);
        // Click Sheep in sidebar
        WebElement sheepSidebar = driver.findElement(By.xpath("//a[contains(@class, 'sidebar-element') and contains(., 'Sheep')]"));
        sheepSidebar.click();
        int sheepNumber = (int)(Math.random() * 10000);
        int sheepWeight = 80;
        String sheepCategory = newName;
        String sheepStatus = "Listed";
        String sheepAge = "2";
        int initialSheepCount;
        try {
            initialSheepCount = sheepPage.getSheepCount();
        } catch (Exception e) {
            initialSheepCount = 0;
        }
        sheepPage.clickAddSheep();
        sheepPage.fillSheepForm(sheepNumber, sheepWeight, sheepCategory, sheepStatus, sheepAge);
        sheepPage.submitSheepForm();
        int newSheepCount = sheepPage.getSheepCount();
        addDelay(2000);
        // Search Sheep
        By sheepSearchInput = By.xpath("//div[@id='root']/div[3]/div/div/div/section/div/div/div[1]/div[1]/div/div//input[@type='search']");
        wait.until(ExpectedConditions.visibilityOfElementLocated(sheepSearchInput));
        WebElement sheepSearch = driver.findElement(sheepSearchInput);
        sheepSearch.clear();
        sheepSearch.sendKeys(String.valueOf(sheepNumber));
        addDelay(1500);
        By sheepNumberLocator = By.xpath(String.format("//td[contains(text(),'%d')]", sheepNumber));
        wait.until(ExpectedConditions.visibilityOfElementLocated(sheepNumberLocator));
        assertTrue(!driver.findElements(sheepNumberLocator).isEmpty(), "Sheep with the given number should be displayed in the table");
        sheepSearch.clear();
        ((org.openqa.selenium.JavascriptExecutor) driver).executeScript(
            "const url = new URL(window.location); url.searchParams.delete('search'); window.history.replaceState({}, document.title, url); window.dispatchEvent(new PopStateEvent('popstate'));"
        );
        addDelay(1500);
        assertEquals(initialSheepCount + 1, newSheepCount, "Sheep count should increase by 1");
        assertTrue(sheepPage.sheepExists(sheepNumber), "Added sheep should appear in the list");
        addDelay(2000);

        // Edit
        int newNumber = sheepNumber + 1;
        int newWeight = 100;
        String newSheepCategory = sheepCategory;
        sheepPage.editFirstSheep(newNumber, newWeight, newSheepCategory);
        assertTrue(sheepPage.sheepExists(newNumber), "Edited sheep should appear in the list");
        addDelay(1000);
        // Delete
        int beforeDeleteSheepCount;
        try {
            beforeDeleteSheepCount = sheepPage.getSheepCount();
        } catch (Exception e) {
            beforeDeleteSheepCount = 1;
        }
        By sheepFirstRowActionIcon = By.xpath("//table//tbody/tr[1]//td[contains(@class, 'place-items-end')]//button[contains(@class, 'rounded-[4px]')]");
        wait.until(ExpectedConditions.elementToBeClickable(sheepFirstRowActionIcon)).click();
        By sheepDeleteOption = By.xpath("//div[contains(@class, 'tippy-content')]//li[contains(text(), 'Delete')]");
        wait.until(ExpectedConditions.visibilityOfElementLocated(sheepDeleteOption));
        driver.findElement(sheepDeleteOption).click();
        wait.until(ExpectedConditions.elementToBeClickable(confirmDeleteButton)).click();
        int expectedSheepCount = beforeDeleteSheepCount - 1;
        if (expectedSheepCount == 0) {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//table//tbody/tr")));
        } else {
            wait.until(ExpectedConditions.numberOfElementsToBe(By.xpath("//table//tbody/tr"), expectedSheepCount));
        }
        assertEquals(expectedSheepCount, sheepPage.getSheepCount(), "Sheep count should decrease by 1 after deletion");
        addDelay(1000);

        // 6. Sale CRUD
        SalesPage salesPage = new SalesPage(driver);
        // Click Sales in sidebar
        WebElement salesSidebar = driver.findElement(By.xpath("//a[contains(@class, 'sidebar-element') and contains(., 'Sales')]"));
        salesSidebar.click();
        String uniqueCustomerName = "Test Customer " + UUID.randomUUID().toString().substring(0, 8);
        int amount = 1000;
        int price2 = 5000;
        int initialSaleCount;
        try {
            initialSaleCount = salesPage.getSaleCount();
        } catch (Exception e) {
            initialSaleCount = 0;
        }
        salesPage.clickAddSale();
        WebElement clientField = driver.findElement(By.cssSelector("input[placeholder='Client']"));
        clientField.sendKeys(uniqueCustomerName);
        WebElement priceField = driver.findElement(By.cssSelector("input[placeholder='Price']"));
        priceField.sendKeys(String.valueOf(price2));
        WebElement amountField = driver.findElement(By.cssSelector("input[placeholder='Amount']"));
        amountField.sendKeys(String.valueOf(amount));
        WebElement statusButton = driver.findElement(By.xpath("//button[contains(@class, 'text-text-primary') and contains(., 'status')]"));
        statusButton.click();
        WebElement statusOption = driver.findElement(By.xpath("//li[text()='partially']"));
        statusOption.click();
        WebElement sheepDropdown = driver.findElement(By.xpath("//button[contains(@class, 'sheep-toggler')]"));
        sheepDropdown.click();
        WebElement firstCheckbox = driver.findElement(By.xpath("(//div[contains(@class, 'tippy-box')]//input[@type='checkbox'])[1]"));
        if (!firstCheckbox.isSelected()) { firstCheckbox.click(); }
        WebElement submitButton = driver.findElement(By.xpath("//button[text()='Add Sales']"));
        submitButton.click();
        addDelay(2000);
        // Search Sale
        WebElement searchField = driver.findElement(By.cssSelector("input[type='search']"));
        searchField.clear();
        searchField.sendKeys(uniqueCustomerName);
        addDelay(1500);
        By saleNameLocator = By.xpath(String.format("//td[contains(text(),'%s')]", uniqueCustomerName));
        wait.until(ExpectedConditions.visibilityOfElementLocated(saleNameLocator));
        assertTrue(!driver.findElements(saleNameLocator).isEmpty(), "Sale with the given customer name should be displayed in the table");
        addDelay(2000);
        // Clear search and reset table before checking total count
        searchField.clear();
        ((org.openqa.selenium.JavascriptExecutor) driver).executeScript(
            "const url = new URL(window.location); url.searchParams.delete('search'); window.history.replaceState({}, document.title, url); window.dispatchEvent(new PopStateEvent('popstate'));"
        );
        addDelay(2000);
        int newSaleCount = salesPage.getSaleCount();
        assertEquals(initialSaleCount + 1, newSaleCount, "Sale count should increase by 1");
        assertTrue(salesPage.saleExists(uniqueCustomerName), "Added sale should appear in the list");

        // Edit
        WebElement actionsButton = driver.findElement(By.cssSelector("button svg.lucide-ellipsis"));
        actionsButton.click();
        WebElement editOption = driver.findElement(By.xpath("//li[contains(@class, 'dropdown-option') and contains(., 'Edit')]"));
        editOption.click();
        WebElement clientFieldEdit = driver.findElement(By.cssSelector("input[placeholder='Client']"));
        clientFieldEdit.clear();
        clientFieldEdit.sendKeys("Updated Customer");
        WebElement priceFieldEdit = driver.findElement(By.cssSelector("input[placeholder='Price']"));
        priceFieldEdit.clear();
        priceFieldEdit.sendKeys("6000");
        WebElement amountFieldEdit = driver.findElement(By.cssSelector("input[placeholder='Amount']"));
        amountFieldEdit.clear();
        amountFieldEdit.sendKeys("1200");
        addDelay(1000);
        WebElement submitButtonEdit = driver.findElement(By.xpath("//button[text()='Save Changes']"));
        submitButtonEdit.click();
        WebElement updatedClient = driver.findElement(By.xpath("//tr[1]//td[contains(., 'Updated Customer')]"));
        assertNotNull(updatedClient, "The sale should be updated with the new client name");
        WebElement updatedPrice = driver.findElement(By.xpath("//tr[1]//td[contains(., '6000 Dh')]"));
        assertNotNull(updatedPrice, "The sale should be updated with the new price");
        WebElement updatedAmount = driver.findElement(By.xpath("//tr[1]//td[contains(., '1200 Dh')]"));
        assertNotNull(updatedAmount, "The sale should be updated with the new amount");
        // Delete
        int beforeDeleteSaleCount;
        try {
            beforeDeleteSaleCount = salesPage.getSaleCount();
        } catch (Exception e) {
            beforeDeleteSaleCount = 1;
        }
        WebElement actionsButtonDel = driver.findElement(By.cssSelector("button svg.lucide-ellipsis"));
        actionsButtonDel.click();
        WebElement deleteOptionSale = driver.findElement(By.xpath("//li[contains(@class, 'dropdown-option') and contains(., 'Delete')]"));
        deleteOptionSale.click();
        By confirmDeleteButtonSale = By.xpath("//button[contains(text(), 'Delete') and contains(@class, 'bg-red-600')]");
        wait.until(ExpectedConditions.elementToBeClickable(confirmDeleteButtonSale)).click();
        int expectedSaleCount = beforeDeleteSaleCount - 1;
        if (expectedSaleCount == 0) {
            // Wait for all rows to disappear (table is empty)
            wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//table//tbody/tr")));
        } else {
            wait.until(ExpectedConditions.numberOfElementsToBe(By.xpath("//table//tbody/tr"), expectedSaleCount));
        }
        assertEquals(expectedSaleCount, salesPage.getSaleCount(), "Sale count should decrease by 1 after deletion");
        addDelay(1000);

        // 7. Shipment CRUD
        ShipmentBasePage shipmentPage = new ShipmentBasePage(driver);
        // Click Shipments in sidebar
        WebElement shipmentsSidebar = driver.findElement(By.xpath("//a[contains(@class, 'sidebar-element') and contains(., 'Shipments')]"));
        shipmentsSidebar.click();
        String phone = "06" + (int)(Math.random() * 100000000);
        String address = "Test Address";
        String shippingDate = "01-01-2025";
        int initialShipmentCount;
        try {
            initialShipmentCount = shipmentPage.getShipmentCount();
        } catch (Exception e) {
            initialShipmentCount = 0;
        }
        shipmentPage.clickAddShipment();
        shipmentPage.fillShipmentForm(phone, address, shippingDate, false);
        shipmentPage.submitShipmentForm(false);

        addDelay(2000);
        int newShipmentCount = shipmentPage.getShipmentCount();
        assertEquals(initialShipmentCount + 1, newShipmentCount, "Shipment count should increase by 1");
        assertTrue(shipmentPage.shipmentExists(phone), "Added shipment should appear in the list");
        addDelay(2000);
        // Search Shipment
        WebElement shipmentSearchField = driver.findElement(By.cssSelector("input[type='search']"));
        shipmentSearchField.clear();
        shipmentSearchField.sendKeys(" ");
        shipmentSearchField.sendKeys(org.openqa.selenium.Keys.BACK_SPACE);
        shipmentSearchField.sendKeys(phone);
        addDelay(1500);
        By shipmentPhoneLocator = By.xpath(String.format("//td[contains(text(),'%s')]", phone));
        wait.until(ExpectedConditions.visibilityOfElementLocated(shipmentPhoneLocator));
        assertTrue(!driver.findElements(shipmentPhoneLocator).isEmpty(), "Shipment with the given phone should be displayed in the table");
        addDelay(1000);
        shipmentSearchField.clear();
        shipmentSearchField.sendKeys(" ");
        shipmentSearchField.sendKeys(org.openqa.selenium.Keys.BACK_SPACE);
        ((org.openqa.selenium.JavascriptExecutor) driver).executeScript(
            "const url = new URL(window.location); url.searchParams.delete('search'); window.history.replaceState({}, document.title, url); window.dispatchEvent(new PopStateEvent('popstate'));"
        );

        // Edit
        String newPhone = "06" + (int)(Math.random() * 100000000);
        String newAddress = "the new Address";
        String newShippingDate = "01-01-2025";
        shipmentPage.editFirstShipment(newPhone, newAddress, newShippingDate);
        assertTrue(shipmentPage.shipmentExists(newPhone), "Edited shipment should appear in the list with the updated phone");
        addDelay(1000);
        // Delete
        int beforeDeleteShipmentCount;
        try {
            beforeDeleteShipmentCount = shipmentPage.getShipmentCount();
        } catch (Exception e) {
            beforeDeleteShipmentCount = 1;
        }
        WebElement actionsButtonShipment = driver.findElement(By.cssSelector("button svg.lucide-ellipsis"));
        actionsButtonShipment.click();
        WebElement deleteOptionShipment = driver.findElement(By.xpath("//li[contains(@class, 'dropdown-option') and contains(., 'Delete')]"));
        deleteOptionShipment.click();
        WebElement confirmDeleteButtonShipment = wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("//button[contains(@class, 'confirmation-button') and text()='Delete']")
        ));
        ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", confirmDeleteButtonShipment);
        confirmDeleteButtonShipment.click();
        int expectedShipmentCount = beforeDeleteShipmentCount - 1;
        if (expectedShipmentCount == 0) {
            // Table should be empty, so assert no rows are visible
            wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//table//tbody/tr")));
            // Optionally, assert that no shipment rows exist
            assertTrue(driver.findElements(By.xpath("//table//tbody/tr")).isEmpty(), "No shipments should be displayed in the table after deleting the last one");
        } else {
            wait.until(ExpectedConditions.numberOfElementsToBe(By.xpath("//table//tbody/tr"), expectedShipmentCount));
        }
        assertEquals(expectedShipmentCount, shipmentPage.getShipmentCount(), "Shipment count should decrease by 1 after deletion");
    }
}
