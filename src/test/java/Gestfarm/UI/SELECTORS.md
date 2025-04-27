# Selenium Page Objects for GestFarm Application

This document explains how the Selenium page objects map to the actual GestFarm application UI structure.

## Page Object Structure

Each page object class represents a specific page or feature in the application:

1. `LoginPage` - Handles authentication
2. `SheepPage` - Handles sheep management
3. `CategoriesPage` - Handles category management  
4. `SalesPage` - Handles sales management
5. `UsersPage` - Handles user management

## Element Locators

The selectors used in these page objects have been updated to match the actual HTML structure of the GestFarm React application. Here's how they were identified:

### LoginPage

- Username/Email field: Located by name attribute `username`
- Password field: Located by name attribute `password`
- Login button: Located by XPath for a button containing "Log in" or "Login" text

### SheepPage

- Add button: Located by XPath for a button with "New" text or "add-button" class
- Table: Located by a generic table selector
- Search input: Located by placeholder text "Search..."
- Form fields: Located by their name attributes and custom selectors for dropdowns

### CategoriesPage

- Add button: Located by XPath for a button with "New" text or "add-button" class
- Table: Located by a generic table selector
- Form fields: Located by their name attributes

### SalesPage

- Add button: Located by XPath for a button with "New" text or "add-button" class
- Table: Located by a generic table selector
- Form fields: Located by their name attributes and custom selectors for dropdowns

### UsersPage

- Table: Located by a generic table selector
- Note: Based on the application code, there appears to be no direct UI for adding users

## Customizing Selectors

If you need to adjust the selectors for your specific application setup:

1. Review the HTML structure of your application using browser developer tools
2. Update the corresponding `By` locators in the page object classes
3. Update the page navigation URLs if your application is hosted at a different location

## Common Issues and Solutions

1. **Element not found**: If a test fails with "Element not found" errors, check if the selector is correct for your application version.

2. **Timing issues**: If elements load dynamically, you may need to adjust the wait times in the `WebDriverWait` instances.

3. **Form structure mismatches**: If the application form structure changes, update the form filling methods in the page objects.

4. **URL mismatches**: Ensure the navigation URLs in each page object match your application's routing structure.

## Advanced Customization

For more complex interactions like file uploads or working with custom React components, you may need to extend these page objects with additional methods specific to your implementation.
