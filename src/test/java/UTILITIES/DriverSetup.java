package UTILITIES;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxDriverLogLevel;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.testng.Assert;

import java.time.Duration;
import java.util.logging.Level;

public class DriverSetup {

  public WebDriver wd;

  public enum BROWSER {CHROME, FIREFOX}

  public DriverSetup(BROWSER browser) {

    LoggingPreferences logs = new LoggingPreferences();
    logs.enable(LogType.BROWSER, Level.SEVERE);
    switch (browser) {
      case CHROME -> {
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.setCapability(ChromeOptions.LOGGING_PREFS, logs);
        wd = new ChromeDriver(chromeOptions);
      }
      case FIREFOX -> {
        FirefoxOptions firefoxOptions = new FirefoxOptions();
        firefoxOptions.setLogLevel(FirefoxDriverLogLevel.FATAL);
        wd = new FirefoxDriver(firefoxOptions);
      }
      default -> Assert.fail("Missing setup for browser: " + browser);
    }
    wd.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
    wd.manage().window().maximize();
  }
}
