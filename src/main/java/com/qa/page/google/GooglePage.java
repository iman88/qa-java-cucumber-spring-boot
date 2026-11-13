package com.qa.page.google;

import com.microsoft.playwright.PlaywrightException;
import com.microsoft.playwright.options.WaitForSelectorState;
import com.qa.kelvin.annotation.Page;
import com.qa.page.Base;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

@Page
public class GooglePage extends Base {

    @Autowired
    private SearchComponent searchComponent;

    @Autowired
    private SearchResult searchResult;

    @Value("${application.url}")
    private String url;

    public void goTo() {
        page.navigate(url);
        page.waitForLoadState();
    }

    public void clickAcceptAllButton() {
        // Dismiss Bing's cookie consent banner if present (EU/regional variants).
        try {
            var btn = page.locator("#bnp_btn_accept, button:has-text('Accept'), button:has-text('Accepta')");
            btn.first().waitFor(new com.microsoft.playwright.Locator.WaitForOptions()
                    .setTimeout(5000)
                    .setState(WaitForSelectorState.VISIBLE));
            btn.first().click();
            page.waitForLoadState();
        } catch (PlaywrightException ignored) {
        }
    }

    public SearchComponent getSearchComponent() {
        return searchComponent;
    }

    public SearchResult getSearchResult() {
        return searchResult;
    }

    public void close() {
        browserContext().close();
    }

    @Override
    public boolean isAt() {
        return searchComponent.isAt();
    }
}
