package data;

public class InventoryItem {

  public String itemName;
  public String itemDescription;
  public float itemPrice;

  public InventoryItem(String itemName, String itemDescription, float itemPrice)
  {
    this.itemName = itemName;
    this.itemDescription = itemDescription;
    this.itemPrice = itemPrice;
  }
}