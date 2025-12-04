package base;

import com.microsoft.playwright.Page;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

public class BaseTest {

    protected Page page;
    private PlaywrightFactory factory;

    @BeforeMethod(alwaysRun = true)
    public void setup() {
        factory = new PlaywrightFactory();
        page = factory.initBrowser();
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        factory.close();
    }
}
