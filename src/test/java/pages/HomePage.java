package pages;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.WaitForSelectorState;

public class HomePage {
    private final Page page;

    public HomePage(Page page) {
        this.page = page;
    }

    public void navigate() {
        page.navigate("https://financesone.worldbank.org/");
        handleOverlays();
    }

    // Handles Terms-of-Use popup, tutorial popup, and blocking overlays
    public void handleOverlays() {

        handleTermsPopup();

        // Tutorial / help popups that may show "Skip"
        try {
            Locator skip = page.locator("button.btn-wrp-secondary:has-text('skip')");
            skip.waitFor();
            skip.click();

        } catch (Exception e) {
            // ignore
        }

        // Remove white overlays or backdrops that block clicking
        try {
            page.evaluate("document.querySelectorAll('.modal-backdrop, #white-before').forEach(e => e.remove());");
        } catch (Exception e) {
            // ignore
        }
    }

    private void handleTermsPopup() {
        // Retry a few times in case popup appears slightly late
        for (int i = 0; i < 5; i++) {
            try {
                Locator agreeBtn = page.locator("button:has-text('I Agree')");
                if (agreeBtn.isVisible()) {
                    agreeBtn.click(new Locator.ClickOptions().setForce(true));
                    page.waitForTimeout(500);
                    break;
                }

                // Backup xpath-style locator
                agreeBtn = page.locator("//button[contains(text(),'Agree')]");
                if (agreeBtn.isVisible()) {
                    agreeBtn.click(new Locator.ClickOptions().setForce(true));
                    page.waitForTimeout(500);
                    break;
                }

            } catch (Exception e) {
                // ignored
            }

            page.waitForTimeout(300);
        }
    }

    public boolean isLoaded() {
        String title = page.title();
        return title != null && title.toLowerCase().contains("finances");
    }

    public void gotoDatasets() {
        handleOverlays();
        page.locator("nav a:has-text('DATA') , nav a:has-text('Datasets')").first().click();
    }

    public void gotoFinanceResults() {
        handleOverlays();
        page.locator("nav a:has-text('Financial Results') , nav a:has-text('Datasets')").first().click();
    }

    public void gotoSummaries() {
        handleOverlays();
        page.locator("nav a:has-text('SUMMARIES') , nav a:has-text('Datasets')").first().click();
    }

    public void gotoCountries() {
        handleOverlays();
        page.locator("nav a:has-text('COUNTRIES / ECONOMIES') , nav a:has-text('Datasets')").first().click();
    }

    public void gotoPublications() {
        handleOverlays();
        page.locator("nav a:has-text('PUBLICATIONS') , nav a:has-text('Datasets')").first().click();
    }

    public void gotoGlossary() {
        handleOverlays();
        page.locator("nav a:has-text('GLOSSARY') , nav a:has-text('Datasets')").first().click();
    }
}
