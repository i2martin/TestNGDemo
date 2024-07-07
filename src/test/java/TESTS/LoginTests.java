package TESTS;

import UTILITIES.DriverSetup;
import com.fasterxml.jackson.databind.JsonNode;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import pageObjects.LandingPage;

import java.io.IOException;

public class LoginTests extends BaseTest {

  LandingPage landingPage;

  @Test(dataProvider="getGoodCredentials")
  public void verifyLoginSuccess(JsonNode data)
  {
    landingPage.goTo();
    landingPage.logIn(data.get("username").asText(), data.get("password").asText());
    Assert.assertEquals(landingPage.successfulLoginUrl, driver.getCurrentUrl());
  }

  @Test
  public void verifyEmptyCredentialsError()
  {
    landingPage.goTo();
    landingPage.logIn("", "");
    Assert.assertEquals(landingPage.getLoginErrorMessage(), "Username is required");
  }

  @Test(dataProvider="getBadCredentials")
  public void verifyIncorrectCredentialsError(JsonNode data)
  {
    String username = data.get("username").asText();
    String password = data.get("password").asText();
    landingPage.goTo();
    landingPage.logIn(username, password);
    if (username.equals(""))
    {
      Assert.assertEquals(landingPage.getLoginErrorMessage(), "Username is required");
    }
    else if (password.equals(""))
    {
      Assert.assertEquals(landingPage.getLoginErrorMessage(), "Password is required");
    }
    else
    {
      Assert.assertEquals(landingPage.getLoginErrorMessage(), "Username and password do not match any user in this service");
    }

  }

  @BeforeTest(alwaysRun = true)
  @Parameters("browser")
  public void initialize(String browser) throws IOException {
    driver = initializeDriver(browser);
    landingPage = new LandingPage(driver);
  }

  @AfterTest
  public void closeDriver()
  {
    driver.quit();
  }




}
