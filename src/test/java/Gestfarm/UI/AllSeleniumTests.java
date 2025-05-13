package Gestfarm.UI;

import Gestfarm.UI.tests.*;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

/**
 * Test suite to run all Selenium UI tests
 */
@Suite
@SelectClasses({
    LoginTest.class,
    CategoryManagementTest.class,
    SheepManagementTest.class,
    SaleManagementTest.class
})
public class AllSeleniumTests {
    // This class serves as a test suite and doesn't need any code
}
