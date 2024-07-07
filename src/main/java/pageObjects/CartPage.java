package pageObjects;
import java.util.ArrayList;
import java.util.List;
import data.InventoryItem;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class CartPage {

  public static String cartUrl = LandingPage.baseUrl + "cart.html";
  public WebDriver driver;
  List<InventoryItem> inventoryItems = new ArrayList<>();

  List<WebElement> items;

  @FindBy(id = "checkout")
  WebElement checkout;

  By cartItemsLocator = By.className("cart_item");

  public CartPage(WebDriver driver) {
    this.driver = driver;
    PageFactory.initElements(driver, this);
  }

  public void getItems() {
    inventoryItems.clear();
    items = driver.findElements(cartItemsLocator);
    for (WebElement item : items) {
      String itemName = item.findElement(By.className("inventory_item_name")).getText();
      String itemDescription = item.findElement(By.className("inventory_item_desc")).getText();
      float itemPrice = Float.parseFloat(
        item.findElement(By.className("inventory_item_price")).getText().replace("$", ""));
      inventoryItems.add(new InventoryItem(itemName, itemDescription, itemPrice));
    }

  }

  public Boolean compareProducts(InventoryItem addedItem) {
    for (InventoryItem item : inventoryItems) {
      if (item.itemName.equals(addedItem.itemName) && item.itemDescription.equals(addedItem.itemDescription)
        && item.itemPrice == addedItem.itemPrice) {
        return true;
      }
    }
    return false;
  }

  public void goToCheckout() {
    checkout.click();
  }
}
