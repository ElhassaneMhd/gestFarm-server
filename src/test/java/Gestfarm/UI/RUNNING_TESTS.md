# Running GestFarm Selenium Tests

This document provides step-by-step instructions on how to run the Selenium UI tests for the GestFarm application.

## Prerequisites

1. Java JDK 11 or higher installed
2. Maven installed
3. Chrome browser installed
4. MySQL database set up and configured for GestFarm

## Step 1: Start the MySQL Database

Ensure your MySQL database is running with the correct schema for GestFarm application.

## Step 2: Start the Spring Boot Backend Server

1. Open a command prompt and navigate to the server directory:
   ```
   cd c:\Users\dell\Desktop\gestfarm-full\gestFarm-server
   ```

2. Run the Spring Boot application:
   ```
   mvn spring-boot:run
   ```

3. Wait for the server to start completely. You should see output indicating the application is running.

## Step 3: Start the React Frontend

1. Open a new command prompt and navigate to the client directory:
   ```
   cd c:\Users\dell\Desktop\gestfarm-full\gestFarm-client
   ```

2. If you haven't already installed dependencies, run:
   ```
   npm install
   ```

3. Start the Vite development server:
   ```
   npm run dev
   ```

4. Wait until you see output indicating the server is running at http://localhost:5173

## Step 4: Run the Selenium Tests

### Option 1: Run All Tests

1. Open a new command prompt and navigate to the server directory:
   ```
   cd c:\Users\dell\Desktop\gestfarm-full\gestFarm-server
   ```

2. Run all Selenium tests using the test suite:
   ```
   mvn test -Dtest=Gestfarm.UI.AllSeleniumTests
   ```

### Option 2: Run Specific Test Classes

Run specific test classes individually:

```
mvn test -Dtest=Gestfarm.UI.tests.LoginTest
mvn test -Dtest=Gestfarm.UI.tests.NavigationTest
mvn test -Dtest=Gestfarm.UI.tests.SheepManagementTest
mvn test -Dtest=Gestfarm.UI.tests.CategoryManagementTest
mvn test -Dtest=Gestfarm.UI.tests.SaleManagementTest
mvn test -Dtest=Gestfarm.UI.tests.UserManagementTest
```

### Option 3: Run Specific Test Methods

To run a specific test method:

```
mvn test -Dtest=Gestfarm.UI.tests.LoginTest#testSuccessfulLogin
```

## Troubleshooting

### Common Issues:

1. **Test failures due to timing issues**:
   - Increase the wait times in the WebDriverWait instances in the page objects
   - Check the BaseSeleniumTest class to increase the implicit wait time

2. **Element not found exceptions**:
   - Verify the selectors in the page objects match your actual HTML
   - Check if the element might be in an iframe or shadow DOM
   - Make sure the application has fully loaded before the test attempts to interact with elements

3. **Navigation issues**:
   - Verify the URLs used in navigateTo() methods match your application's actual URLs
   - Check that the AppNavigation class correctly locates and clicks on sidebar links

4. **Authentication problems**:
   - Ensure the test credentials (admin@gmail.com/password123) are valid in your application
   - Verify the login process in the application matches what the tests expect

5. **Chrome version issues**:
   - The tests use WebDriverManager which should automatically download the correct ChromeDriver, but if you encounter issues, you may need to manually download ChromeDriver matching your Chrome version

## Viewing Test Reports

After running the tests, you can find detailed HTML reports in:
```
c:\Users\dell\Desktop\gestfarm-full\gestFarm-server\target\surefire-reports
```

## Extending the Tests

To add new test cases:
1. Create new page objects in `src/test/java/Gestfarm/UI/pages/`
2. Create new test classes in `src/test/java/Gestfarm/UI/tests/`
3. Add your test classes to `AllSeleniumTests.java`
