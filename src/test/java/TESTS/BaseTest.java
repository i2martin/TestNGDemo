package TESTS;

import UTILITIES.DriverSetup;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.DataProvider;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BaseTest {

  DriverSetup ds;
  public WebDriver driver;
  ObjectMapper objectMapper = new ObjectMapper();
  File dataFile = new File(System.getProperty("user.dir") + "\\src\\main\\java\\data\\data.json");

  @DataProvider
  public Iterator<Object[]> getGoodCredentials() throws IOException
  {
    JsonNode dataNode = objectMapper.readTree(dataFile).path("good_credentials");
    Iterator<JsonNode> elements = dataNode.elements();
    List<Object[]> dataList = new ArrayList<>();
    while (elements.hasNext()) {
      JsonNode element = elements.next();
      dataList.add(new Object[]{element});
    }
    return dataList.iterator();
  }

  @DataProvider
  public Iterator<Object[]> getBadCredentials() throws IOException
  {
    JsonNode dataNode = objectMapper.readTree(dataFile).path("bad_credentials");
    Iterator<JsonNode> elements = dataNode.elements();
    List<Object[]> dataList = new ArrayList<>();
    while (elements.hasNext()) {
      JsonNode element = elements.next();
      dataList.add(new Object[]{element});
    }
    return dataList.iterator();
  }

  @DataProvider
  public Iterator<Object[]> getBadFormData() throws IOException
  {
    JsonNode dataNode = objectMapper.readTree(dataFile).path("bad_checkout_info");
    Iterator<JsonNode> elements = dataNode.elements();
    List<Object[]> dataList = new ArrayList<>();
    while (elements.hasNext()) {
      JsonNode element = elements.next();
      dataList.add(new Object[]{element});
    }
    return dataList.iterator();
  }

  @DataProvider
  public Object[][] getDefaultCredentials() throws IOException
  {
    JsonNode dataNode = objectMapper.readTree(dataFile).path("default_credentials");
    String user = dataNode.get("username").asText();
    String pass = dataNode.get("password").asText();
    return new Object[][] { {user, pass} };
  }

  @DataProvider
  public Object[][] getDefaultCheckoutInfo() throws IOException
  {
    JsonNode dataNode = objectMapper.readTree(dataFile).path("default_checkout_info");
    String firstName = dataNode.get("firstName").asText();
    String lastName = dataNode.get("lastName").asText();
    String postalCode = dataNode.get("postalCode").asText();
    return new Object[][] { {firstName, lastName, postalCode}};
  }

  public WebDriver initializeDriver(String browser) {
    if (browser.equals("chrome"))
      ds = new DriverSetup(DriverSetup.BROWSER.CHROME);
    else if (browser.equals("firefox"))
      ds = new DriverSetup(DriverSetup.BROWSER.FIREFOX);
    return ds.wd;
  }


}
