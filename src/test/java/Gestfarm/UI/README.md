# Selenium UI Tests for GestFarm Application

This directory contains automated UI tests for the GestFarm application using Selenium WebDriver and JUnit 5.

## Test Structure

The tests are organized as follows:

- `BaseSeleniumTest.java` - Base class that handles browser setup and teardown
- `pages/` - Page Object Model classes that encapsulate page interactions
  - `LoginPage.java` - Page object for login functionality
  - `SheepPage.java` - Page object for sheep management
  - `CategoriesPage.java` - Page object for category management
  - `SalesPage.java` - Page object for sales management
  - `UsersPage.java` - Page object for user management
- `tests/` - Test classes with actual test cases
  - `LoginTest.java` - Tests for login functionality
  - `SheepManagementTest.java` - Tests for sheep management
  - `CategoryManagementTest.java` - Tests for category management
  - `SaleManagementTest.java` - Tests for sales management
  - `UserManagementTest.java` - Tests for user management
- `utils/` - Utility classes for Selenium testing
  - `SeleniumUtils.java` - Helper methods for Selenium tests
- `AllSeleniumTests.java` - Test suite to run all UI tests

## Running the Tests

### Prerequisites

1. Make sure both the backend server and frontend application are running:
   - Backend should be running on its default port
   - Frontend should be running on http://localhost:5173 (or update the BASE_URL in BaseSeleniumTest.java)

2. Ensure you have Chrome browser installed (or modify the code to use a different browser)

3. Update the test credentials:
   - In `LoginTest.java` and other test classes, replace the test credentials with valid ones for your application
   - For example, replace `"testuser"` and `"password123"` with actual valid credentials

### Run from Maven

To run all UI tests:

```bash
mvn test -Dtest=Gestfarm.UI.AllSeleniumTests
```

To run specific test classes:

```bash
mvn test -Dtest=Gestfarm.UI.tests.LoginTest
mvn test -Dtest=Gestfarm.UI.tests.SheepManagementTest
mvn test -Dtest=Gestfarm.UI.tests.CategoryManagementTest
mvn test -Dtest=Gestfarm.UI.tests.SaleManagementTest
mvn test -Dtest=Gestfarm.UI.tests.UserManagementTest
```

To run a specific test method:

```bash
mvn test -Dtest=Gestfarm.UI.tests.LoginTest#testSuccessfulLogin
```

### Run from IDE

You can also run the tests directly from your IDE by right-clicking on the test class or method and selecting "Run".

## Adding New Tests

To add new test classes:

1. Create new Page Object classes in the `pages/` directory
2. Create new test classes in the `tests/` directory
3. Add the new test classes to `AllSeleniumTests.java`

## Customizing for Your Application

Before running the tests, you need to adjust the following:

1. **Element Selectors**: Update the selectors in the page object classes to match your actual application's HTML structure
   - For example, update `By.id("username")` to match the actual ID used in your login form

2. **Test Data**: Update the test data in the test classes to match your application's requirements
   - For example, update sheep names, categories, etc.

3. **URL Paths**: Update the URL paths in the page object classes to match your application's routing
   - For example, update `driver.get("http://localhost:5173/login")` to match your actual login page URL

4. **Waiting Strategies**: Adjust waiting times and conditions based on your application's loading behavior

## Test Reporting

The tests use JUnit 5 reporting. You can integrate with tools like Allure for more advanced reporting:

1. Add Allure dependencies to your pom.xml
2. Run tests with Allure listener
3. Generate and view Allure reports

## Notes

- The tests are designed to be independent of each other, but they assume a clean state of the application.
- You may need to adjust the locators in the Page Object classes based on your actual implementation.
- For CI environments, you can enable headless mode in the ChromeOptions by uncommenting the headless option in `BaseSeleniumTest.java`.
- Consider using test data setup and teardown to ensure tests don't interfere with each other.
- These tests serve as a template and should be customized to match your specific application structure and requirements.
