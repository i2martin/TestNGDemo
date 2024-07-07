package pageObjects;

import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class LandingPage {
  WebDriver driver;

  public static String baseUrl = "https://www.saucedemo.com/";

  public String successfulLoginUrl = baseUrl + "inventory.html";

  @FindBy(id = "react-burger-menu-btn")
  public WebElement sidebarBtn;

  @FindBy(id = "user-name")
  WebElement userEmail;

  @FindBy(id = "password")
  WebElement userPassword;

  @FindBy(id = "login-button")
  WebElement submit;

  @FindBy(id = "logout_sidebar_link")
  public WebElement logOut;

  By errorLocator = By.cssSelector("h3[data-test='error']");

    public LandingPage(WebDriver driver) {
    this.driver = driver;
    PageFactory.initElements(driver, this);
  }

  public void logIn(String email, String password) {
    userEmail.sendKeys(email);
    userPassword.sendKeys(password);
    submit.click();
  }

  public String getLoginErrorMessage() {
    String errorMessage = driver.findElement(errorLocator).getText();
    // close error message
    driver.findElement(By.className("error-button"));
    return errorMessage.split(":")[1].trim();

  }

  public void goTo() {
    driver.get(baseUrl);
  }

}
