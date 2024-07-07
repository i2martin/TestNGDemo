package pageObjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class CheckoutPage {

  WebDriver driver;
  String checkoutUrlOne = LandingPage.baseUrl + "checkout-step-one.html";
  public String checkoutUrlTwo = LandingPage.baseUrl + "checkout-step-two.html";

  @FindBy(id = "first-name")
  WebElement firstName;

  @FindBy(id = "last-name")
  WebElement lastName;

  @FindBy(id = "postal-code")
  WebElement postalCode;

  @FindBy(id = "continue")
  public
  WebElement cont;

  @FindBy(id = "cancel")
  public
  WebElement cancel;

  @FindBy(id = "finish")
  public
  WebElement finish;

  @FindBy(css = "h3[data-test='error']")
  public
  WebElement checkoutInfoError;

  @FindBy(css="h2[data-test='complete-header']")
  WebElement complete;

  @FindBy(css = "div[data-test='subtotal-label']")
  WebElement subtotal;

  @FindBy(css="div[data-test='tax-label']")
  WebElement tax;

  @FindBy(css = "div[data-test='total-label']")
  WebElement total;

  public CheckoutPage(WebDriver driver) {
    this.driver = driver;
    PageFactory.initElements(driver, this);
  }

  public void fillInformation(String fName, String lName, String pCode) {
    firstName.sendKeys(fName);
    lastName.sendKeys(lName);
    postalCode.sendKeys(pCode);
  }

  public void goToCheckout() {
    driver.get(checkoutUrlOne);
  }

  public float getSubtotalValue() {
    return Float.parseFloat(subtotal.getText().split(":")[1].replace("$", "").trim());
  }

  public float getTaxValue() {
    return Float.parseFloat(tax.getText().split(":")[1].replace("$", "").trim());
  }

  public float getTotalValue() {
    return Float.parseFloat(total.getText().split(":")[1].replace("$", "").trim());
  }

  public String getCompleteOrderText() {
    return complete.getText();
  }
}
