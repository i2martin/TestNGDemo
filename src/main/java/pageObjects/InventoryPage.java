package pageObjects;
import java.util.ArrayList;
import java.util.List;

import data.InventoryItem;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;
public class InventoryPage {

  WebDriver driver;

  public List<InventoryItem> inventoryItems = new ArrayList<>();

  public List<WebElement> items;

  @FindBy(css = ".shopping_cart_link")
  WebElement shoppingCart;

  @FindBy(css = ".product_sort_container")
  WebElement sortElement;

  By inventoryItemLocator = By.cssSelector(".inventory_item");

  @FindBy(css = "div[data-test='inventory-item-name']")
  public WebElement inventoryItemName;

  @FindBy(css = "div[data-test='inventory-item-desc']")
  public WebElement inventoryItemDesc;

  @FindBy(css = "div[data-test='inventory-item-price']")
  public WebElement inventoryItemPrice;


  Select filter;

  By cartItemsCountLocator = By.cssSelector(".shopping_cart_badge");

  public WebElement cartItemsCount;

  public InventoryPage(WebDriver driver) {
    this.driver = driver;
    PageFactory.initElements(driver, this);
  }

  public void getItems() {
    inventoryItems.clear();
    items = driver.findElements(inventoryItemLocator);
    items.forEach(this::WebElementToInventoryItem);
  }

  //this method just returns all the items
  public List<WebElement> getItemsWithoutStoring() {
    return driver.findElements(inventoryItemLocator);
  }

  public InventoryItem WebElementToInventoryItem(WebElement element) {
    String itemName = element.findElement(By.className("inventory_item_name")).getText();
    String itemDescription = element.findElement(By.className("inventory_item_desc")).getText();
    float itemPrice = Float
      .parseFloat(element.findElement(By.className("inventory_item_price")).getText().replace("$", ""));
    InventoryItem item = new InventoryItem(itemName, itemDescription, itemPrice);
    inventoryItems.add(item);
    // log all items to console
    System.out.println(
      "Item: " + itemName + "\n" + "Description: " + itemDescription + "\n" + "Price: " + itemPrice + "\n");
    return item;

  }

  public void sortItems(String value) {
    filter = new Select(sortElement);
    filter.selectByValue(value);
    getItems();
  }

  public boolean itemsSortedByPrice(String value) {
    switch (value) {
      case "hilo" -> {
        for (int i = 0; i < items.size() - 1; i++) {
          if (inventoryItems.get(i).itemPrice < inventoryItems.get(i + 1).itemPrice) {
            return false;
          }
        }
        return true;
      }
      case "lohi" -> {
        for (int i = 0; i < items.size() - 1; i++) {
          if (inventoryItems.get(i).itemPrice > inventoryItems.get(i + 1).itemPrice) {
            return false;
          }
        }
        return true;
      }
      default -> {
        return false;
      }
    }

  }

  public WebElement getProductByProductIndex(int itemIndex) {
    String query = "item_" + itemIndex;
    return driver.findElement(By.xpath("//a[contains(@id,'" + query + "')]"));
  }

  public void addToCart(int itemIndex) {
    items.get(itemIndex).findElement(By.tagName("button")).click();
    cartItemsCount = driver.findElement(cartItemsCountLocator);
  }

  public void addToCart(WebElement parentElement) {
    parentElement.findElement(By.tagName("button")).click();
    cartItemsCount = driver.findElement(cartItemsCountLocator);
  }

  public void removeFromCart(int itemIndex) {
    items.get(itemIndex).findElement(By.tagName("button")).click();
  }

  public String addToCartButtonText(int itemIndex) {
    return items.get(itemIndex).findElement(By.tagName("button")).getText();
  }

  public String addToCartButtonText(WebElement parentElement) {
    return parentElement.findElement(By.tagName("button")).getText();
  }

  public void goToCart() {
    shoppingCart.click();
  }

  public InventoryItem collectItemInformationFromItemPage()
  {
    return new InventoryItem(inventoryItemName.getText(),inventoryItemDesc.getText(),
            Float.parseFloat(inventoryItemPrice.getText().replace("$", "").trim()));
  }
}
