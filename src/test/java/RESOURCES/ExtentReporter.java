package RESOURCES;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

public class ExtentReporter {
  public static ExtentReports getReportObject() {
    String path = System.getProperty("user.dir") + "//report//index.html";
    ExtentSparkReporter reporter = new ExtentSparkReporter(path);
    reporter.config().setReportName("Automation Results");
    reporter.config().setDocumentTitle("Test Results");
    ExtentReports extent = new ExtentReports();
    extent.attachReporter(reporter);
    extent.setSystemInfo("Tester", "Ivan Martinović");
    return extent;
  }
}
