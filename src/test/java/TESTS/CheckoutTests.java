package TESTS;
import UTILITIES.DriverSetup;
import com.fasterxml.jackson.databind.JsonNode;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import pageObjects.CheckoutPage;
import java.io.IOException;
import pageObjects.CartPage;
import pageObjects.LandingPage;

public class CheckoutTests extends BaseTest{

  CheckoutPage checkout;
  LandingPage landingPage;

  @Test
  public void verifyCancelButton() {
    checkout.goToCheckout();
    checkout.cancel.click();
    Assert.assertEquals(driver.getCurrentUrl(), CartPage.cartUrl, "Cancel button in checkout isn't returning to cart");
  }

  @Test
  public void verifyContinueButton() {
    checkout.goToCheckout();
    checkout.fillInformation("test", "test", "test");
    checkout.cont.click();
    Assert.assertEquals(driver.getCurrentUrl(), checkout.checkoutUrlTwo, "Continue button in checkout isn't moving to checkout part two");
  }

  @Test(dataProvider = "getBadFormData", groups = {"test"})
  public void verifyBadFormData(JsonNode data) {
    String firstName = data.get("firstName").asText();
    String lastName = data.get("lastName").asText();
    String postalCode = data.get("postalCode").asText();
    checkout.goToCheckout();
    checkout.fillInformation(firstName, lastName, postalCode);
    checkout.cont.click();
    if (firstName.equals(""))
      Assert.assertEquals(checkout.checkoutInfoError.getText(), "Error: First Name is required");
    else if (lastName.equals(""))
      Assert.assertEquals(checkout.checkoutInfoError.getText(), "Error: Last Name is required");
    else if (postalCode.equals(""))
      Assert.assertEquals(checkout.checkoutInfoError.getText(), "Error: Postal Code is required");
    //any data is accepted --> there's no way to check for "bad" information besides empty fields

  }

  @BeforeTest(alwaysRun = true)
  @Parameters("browser")
  public void initialize(String browser) throws IOException {
    Object[][] defaultUser = getDefaultCredentials();
    String username = (String) defaultUser[0][0];
    String password = (String) defaultUser[0][1];
    driver = initializeDriver(browser);
    landingPage = new LandingPage(driver);
    landingPage.goTo();
    landingPage.logIn(username,password);
    checkout = new CheckoutPage(driver);
    checkout.goToCheckout();
  }

  @AfterTest(alwaysRun = true)
  public void closeDriver() {
    driver.quit();
  }



}
