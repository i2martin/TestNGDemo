package TESTS;

import java.io.IOException;
import java.time.Duration;
import java.util.*;
import data.InventoryItem;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import pageObjects.CartPage;
import pageObjects.CheckoutPage;
import pageObjects.InventoryPage;
import pageObjects.LandingPage;

public class EndToEndTest extends BaseTest {

  LandingPage landingPage;
  InventoryPage inventory;
  CartPage cart;
  InventoryItem itemToCart;
  InventoryItem newItem;

  @Test
  public void testLogin() throws IOException {
    Object[][] defaultUser = getDefaultCredentials();
    String username = (String) defaultUser[0][0];
    String password = (String) defaultUser[0][1];
    landingPage.goTo();
    landingPage.logIn(username, password);
    //verify successful login
    Assert.assertEquals(landingPage.successfulLoginUrl, driver.getCurrentUrl());
  }
  @Test(dependsOnMethods = {"testLogin"})
  public void testSortItems(){
    String sortOption = "hilo";
    inventory = new InventoryPage(driver);
    inventory.getItems();
    inventory.sortItems(sortOption);
    //verify that items are sorted from highest to lowest price
    Assert.assertTrue(inventory.itemsSortedByPrice(sortOption));
  }

  @Test(dependsOnMethods = {"testSortItems"})
  public void testAddLowestPriceItemToCart(){
    //collect all the items again, after sorting
    List<WebElement> items = inventory.getItemsWithoutStoring();
    List<InventoryItem> newItems = new ArrayList<>();
    for (WebElement i: items)
    {
      newItems.add(inventory.WebElementToInventoryItem(i));
    }
    //add last item to cart
    itemToCart = newItems.get(newItems.size()-1);
    inventory.addToCart(items.get(items.size()-1));
    //verify item's added to cart
    Assert.assertEquals(inventory.addToCartButtonText(items.get(items.size()-1)), "Remove", "Item's not added to cart");
    inventory.goToCart();
    cart.getItems();
    //verify that correct item's in the cart
    Assert.assertTrue(cart.compareProducts(itemToCart), "Added item and item in cart do not match");
  }

  @Test(dependsOnMethods = {"testAddLowestPriceItemToCart"})
  public void testAddSpecificIndexItemToCart(){
    int itemIndex = 4;
    //return to inventory
    driver.navigate().back();
    //open specific item in inventory
    inventory.getProductByProductIndex(itemIndex).click();
    newItem = inventory.collectItemInformationFromItemPage();
    //add item to cart
    inventory.addToCartButton.click();
    //verify that the product is added to cart
    Assert.assertEquals(inventory.removeFromCartButton.getText(), "Remove", "Item's not correctly updating");
    inventory.goToCart();
    cart.getItems();
    //verify new product's in the cart as well
    Assert.assertTrue(cart.compareProducts(newItem), "Added item and item in cart do not match");

  }
  @Test(dependsOnMethods = {"testAddSpecificIndexItemToCart"})
  public void verifyCheckout() throws IOException {

    Object[][] checkoutInfo = getDefaultCheckoutInfo();
    cart.goToCheckout();
    CheckoutPage checkout = new CheckoutPage(driver);
    checkout.fillInformation((String) checkoutInfo[0][0], (String) checkoutInfo[0][1], (String) checkoutInfo[0][2]);
    checkout.cont.click();

    //verify information's filled and accepted
    Assert.assertEquals(driver.getCurrentUrl(), checkout.checkoutUrlTwo, "Checkout step one wasn't completed correctly");

    float expectedSubtotal = itemToCart.itemPrice + newItem.itemPrice;
    //verify if subtotal value matches expected value
    Assert.assertEquals(expectedSubtotal, checkout.getSubtotalValue(), "Subtotal value doesn't match the items in cart");

    float expectedTotal = expectedSubtotal + checkout.getTaxValue();
    //verify if total value matches expected value
    Assert.assertEquals(expectedTotal, checkout.getTotalValue(), "Total value doesn't match with items in the cart");

    checkout.finish.click();
    //verify that order's complete
    Assert.assertEquals(checkout.getCompleteOrderText(), "Thank you for your order!", "Order confirmation not present!");
  }

  @Test(dependsOnMethods = {"verifyCheckout"})
  public void testLogOut(){
    landingPage.sidebarBtn.click();

    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
    wait.until(ExpectedConditions.visibilityOf(landingPage.logOut));
    landingPage.logOut.click();
    //verify successful logout
    Assert.assertEquals(LandingPage.baseUrl, driver.getCurrentUrl());
  }

  @BeforeTest
  @Parameters("browser")
  public void initialize(String browser) throws IOException {
    driver = initializeDriver(browser);
    landingPage = new LandingPage(driver);
    cart = new CartPage(driver);
  }

  @AfterTest
  public void closeDriver() {
    driver.quit();
  }

}
