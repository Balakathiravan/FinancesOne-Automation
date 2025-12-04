package reporting;

import api.ApiBase;
import com.aventstack.extentreports.*;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.microsoft.playwright.Page;
import org.testng.*;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Base64;

public class ExtentTestNGListener implements ITestListener {

    private static ExtentReports extent;
    private static ThreadLocal<ExtentTest> test = new ThreadLocal<>();

    @Override
    public void onStart(ITestContext context) {

        String reportPath = System.getProperty("user.dir") + "/test-output/ExtentReport.html";

        ExtentSparkReporter spark = new ExtentSparkReporter(reportPath);
        spark.config().setReportName("Automation Test Report");
        spark.config().setDocumentTitle("Extent Report");

        extent = new ExtentReports();
        extent.attachReporter(spark);

        extent.setSystemInfo("Environment", "QA");
        extent.setSystemInfo("Tester", "Automation");
    }

    @Override
    public void onTestStart(ITestResult result) {

        ExtentTest extentTest = extent.createTest(result.getMethod().getMethodName());

        // Add category: ui or api
        if (isApiTest(result)) {
            extentTest.assignAuthor("API Tests");
            extentTest.assignCategory("API");
        } else {
            extentTest.assignAuthor("UI Tests");
            extentTest.assignCategory("UI");
        }

        test.set(extentTest);
    }


    @Override
    public void onTestSuccess(ITestResult result) {
        test.get().log(Status.PASS, "Test Passed");

        if (!isApiTest(result)) {
            captureScreenshot(result, true);
            logUrl(result);
        }

        logAPI(result);
    }

    @Override
    public void onTestFailure(ITestResult result) {
        test.get().log(Status.FAIL, result.getThrowable());

        if (!isApiTest(result)) {
            captureScreenshot(result, false);
            logUrl(result);
        }

        logAPI(result);
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        test.get().log(Status.SKIP, "Test Skipped");
    }

    @Override
    public void onFinish(ITestContext context) {
        extent.flush();
    }

    // Detect if it's an API test
    private boolean isApiTest(ITestResult result) {
        return result.getInstance() instanceof ApiBase;
    }

    // Screenshot capture WITHOUT MediaEntityBuilder
    private void captureScreenshot(ITestResult result, boolean pass) {
        ExtentTest t = test.get();
        try {
            Object instance = result.getInstance();
            Class<?> baseClass = instance.getClass().getSuperclass();
            Field pageField = baseClass.getDeclaredField("page");
            pageField.setAccessible(true);
            Page page = (Page) pageField.get(instance);

            if (page != null && !page.isClosed()) {

                byte[] screenshot = page.screenshot(
                        new Page.ScreenshotOptions().setFullPage(true));

                String base64 = Base64.getEncoder().encodeToString(screenshot);

                t.addScreenCaptureFromBase64String(base64,
                        pass ? "PASS Screenshot" : "FAIL Screenshot");

            } else {
                t.info("API Test - No UI screenshot.");
            }

        } catch (Exception e) {
            t.warning("Screenshot capture error: " + e.getMessage());
        }
    }

    // Log current URL for UI tests
    private void logUrl(ITestResult result) {
        try {
            Object instance = result.getInstance();
            Class<?> baseClass = instance.getClass().getSuperclass();

            Field pageField = baseClass.getDeclaredField("page");
            pageField.setAccessible(true);
            Page page = (Page) pageField.get(instance);

            if (page != null && !page.isClosed()) {
                test.get().info("<b>Page URL:</b> " + page.url());
            }

        } catch (Exception ignored) {
        }
    }

    // Log API request/response
    private void logAPI(ITestResult result) {

        Object request = result.getAttribute("apiRequest");
        Object response = result.getAttribute("apiResponse");

        if (request != null) {
            test.get().info("<b>API Request:</b><br><pre>" + request + "</pre>");
        }

        if (response != null) {
            test.get().info("<b>API Response:</b><br><pre>" + response + "</pre>");
        }
    }
}
