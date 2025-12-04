package reporting;

import com.aventstack.extentreports.*;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

public class ExtentManager {

    private static ExtentReports extent;

    public static ExtentReports get() {
        if (extent == null) {
            ExtentSparkReporter spark = new ExtentSparkReporter("ExtentReport.html");
            spark.config().setReportName("FinancesOne Automation Report");
            spark.config().setDocumentTitle("FinancesOne Execution Report");

            extent = new ExtentReports();
            extent.attachReporter(spark);
        }
        return extent;
    }
}
