package tests;

import base.BaseTest;
import com.microsoft.playwright.Page;
import org.testng.annotations.Listeners;
import pages.HomePage;
import org.testng.Assert;
import org.testng.annotations.Test;

@Listeners(reporting.ExtentTestNGListener.class)
public class HomePageTest extends BaseTest {

    @Test
    public void homePageLoads() {
        HomePage hp = new HomePage(page);
        hp.navigate();
        Assert.assertTrue(hp.isLoaded(), "Home page did not load correctly.");
    }

    @Test
    public void datasetsNavigation() {
        HomePage hp = new HomePage(page);
        hp.navigate();
        hp.gotoDatasets();
         String title = page.url();
        Assert.assertTrue(title != null && title.toLowerCase().contains("data"),
                "Data tab did not load. Actual title: " + title);
    }

    @Test
    public void financeResultsNavigation() {
        HomePage hp = new HomePage(page);
        hp.navigate();
        hp.gotoFinanceResults();
        String title = page.url();
        Assert.assertTrue(title != null && title.toLowerCase().contains("finance"),
                "Financial Results tab did not load. Actual title: " + title);
    }

    @Test
    public void countriesNavigation() {
        HomePage hp = new HomePage(page);
        hp.navigate();
        hp.gotoCountries();
        String title = page.url();
        Assert.assertTrue(title != null && title.toLowerCase().contains("countries"),
                "Countries/Economies tab did not load. Actual title: " + title);
    }

    @Test
    public void summariesNavigation() {
        HomePage hp = new HomePage(page);
        hp.navigate();
        hp.gotoSummaries();
        page.waitForURL("https://financesone.worldbank.org/summaries/ibrd-ida");
        String title = page.url();
        Assert.assertTrue(title != null && title.toLowerCase().contains("summaries"),
                "Summaries tab did not load. Actual title: " + title);
    }

    @Test
    public void publicationsNavigation() {
        HomePage hp = new HomePage(page);
        hp.navigate();
        hp.gotoPublications();
        page.waitForURL("https://financesone.worldbank.org/publications");
        String title = page.url();
        Assert.assertTrue(title != null && title.toLowerCase().contains("publications"),
                "Publications tab did not load. Actual title: " + title);
    }

    @Test
    public void glossaryNavigation() {
        HomePage hp = new HomePage(page);
        hp.navigate();
        hp.gotoGlossary();
        page.waitForURL("https://financesone.worldbank.org/glossary");
        String title = page.url();
        Assert.assertTrue(title != null && title.toLowerCase().contains("glossary"),
                "Glossary tab did not load. Actual title: " + title);
    }

    @Test
    public void searchFunctionalityTest() {
        HomePage hp = new HomePage(page);
        hp.navigate();

        page.click("input[placeholder='Search: example “Loans and Credits”']");
        page.fill("input[placeholder='Search: example “Loans and Credits”']", "loans");
        page.press("input[placeholder='Search: example “Loans and Credits”']", "Enter");

        page.waitForURL("https://financesone.worldbank.org/data?q=loans");
        page.waitForSelector("div[class='search-row']");
        System.out.println("Search results count : "+page.locator("div[class='search-row']").count());
        Assert.assertTrue(
                page.locator("div[class='search-row']").count() > 0,
                "Search results not displayed"
        );
    }

    @Test
    public void usageTrendsPopupValidation() {
        HomePage hp = new HomePage(page);
        hp.navigate();
        page.getByText("USAGE TRENDS").click();
        page.waitForSelector("div[class='modal-body']");
        Assert.assertTrue(
                page.locator("div[class='modal-body']").isVisible(),
                "Usage Trends window displayed"
        );
    }



}
