package TESTS;
import java.io.IOException;
import java.util.Objects;
import UTILITIES.DriverSetup;
import org.testng.Assert;
import org.testng.annotations.*;
import org.testng.asserts.SoftAssert;
import org.openqa.selenium.WebDriver;
import pageObjects.InventoryPage;
import pageObjects.LandingPage;

public class InventoryTests extends BaseTest{

  InventoryPage inventoryPage;

  @Test
  public void verifyItemsInInventory() {
    Assert.assertTrue(inventoryPage.inventoryItems.size() > 0, "Product list is empty");
  }

  @Test
  public void verifySortingPriceHighToLow() {
    inventoryPage.sortItems("hilo");
    Assert.assertTrue(inventoryPage.itemsSortedByPrice("hilo"), "Items aren't properly sorted.");

  }

  @Test
  public void verifyAllProductsCanBeAddedToCart() {
    SoftAssert softAssert = new SoftAssert();
    for (int i = 0; i < inventoryPage.items.size() - 1; i++) {
      inventoryPage.addToCart(i);
      // verify that cart item count is updated
      softAssert.assertTrue(!Objects.equals(inventoryPage.cartItemsCount.getText(), ""),
        "Cart item count isn't properly showing");
      // verify that text on item's button is updated
      softAssert.assertTrue(inventoryPage.addToCartButtonText(i).equals("Remove"),
        "Item button text's not updating properly");
      inventoryPage.removeFromCart(i);
    }
    softAssert.assertAll();
  }

  @Test(dependsOnMethods = "verifySortingPriceHighToLow")
  public void addLowestPriceItemToCart() {
    int itemIndex = inventoryPage.items.size() - 1;
    SoftAssert softAssert = new SoftAssert();
    inventoryPage.addToCart(itemIndex);
    // verify that cart item count is updated
    softAssert.assertTrue(!Objects.equals(inventoryPage.cartItemsCount.getText(), ""), "Cart item count isn't properly showing");
    // verify that text on item's button is updated
    softAssert.assertTrue(inventoryPage.addToCartButtonText(itemIndex).equals("Remove"),
      "Item button text's not updating properly");
    softAssert.assertAll();
  }

  @BeforeTest(alwaysRun = true)
  @Parameters("browser")
  public void initialize(String browser) throws IOException {
    Object[][] defaultUser = getDefaultCredentials();
    String username = (String) defaultUser[0][0];
    String password = (String) defaultUser[0][1];
    driver = initializeDriver(browser);
    LandingPage landingPage = new LandingPage(driver);
    landingPage.goTo();
    landingPage.logIn(username, password);
    inventoryPage = new InventoryPage(driver);
    inventoryPage.getItems();
  }

  @AfterTest
  public void closeDriver() {
    this.driver.quit();
  }
}
