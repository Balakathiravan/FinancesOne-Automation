package base;

import com.microsoft.playwright.*;
import utils.ConfigManager;

public class PlaywrightFactory {

    private Playwright playwright;
    private Browser browser;
    private BrowserContext context;
    private Page page;

    public Page initBrowser() {

        playwright = Playwright.create();

        // Read from System property OR config.properties
        String browserName = System.getProperty("browser",
                ConfigManager.get("browser")
        ).toLowerCase();

        boolean headless = Boolean.parseBoolean(ConfigManager.get("headless"));

        System.out.println("Launching Browser: " + browserName);

        switch (browserName) {

            case "chrome":
                browser = playwright.chromium().launch(
                        new BrowserType.LaunchOptions()
                                .setChannel("chrome")
                                .setHeadless(headless)
                );
                break;

            case "edge":
                browser = playwright.chromium().launch(
                        new BrowserType.LaunchOptions()
                                .setChannel("msedge")
                                .setHeadless(headless)
                );
                break;

            default:
                throw new RuntimeException("Invalid browser: " + browserName + ". Use chrome or edge.");
        }

        context = browser.newContext();
        page = context.newPage();
        return page;
    }

    public void close() {
        if (browser != null) browser.close();
        if (playwright != null) playwright.close();
    }
}
