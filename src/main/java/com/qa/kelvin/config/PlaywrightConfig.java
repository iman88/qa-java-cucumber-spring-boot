package com.qa.kelvin.config;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.qa.kelvin.annotation.LazyConfiguration;
import com.qa.kelvin.annotation.ThreadScopeBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;

@LazyConfiguration
public class PlaywrightConfig {

    @Value("${browser:chromium}")
    private String browserType;

    @Value("${browser.headless:false}")
    private boolean headless;

    @Value("${default.timeout:30000}")
    private double defaultTimeout;

    // Each thread gets its own Playwright instance (Playwright Java is not thread-safe).
    // destroyMethod is omitted because BrowserScope does not invoke destruction callbacks;
    // cleanup is handled explicitly in the Cucumber @After hook via BrowserScope.clearCurrentThread().
    @ThreadScopeBean
    public Playwright playwright() {
        return Playwright.create();
    }

    @ThreadScopeBean
    @Profile("!remote")
    public Browser browser(Playwright playwright) {
        BrowserType.LaunchOptions options = new BrowserType.LaunchOptions().setHeadless(headless);
        return switch (browserType.toLowerCase()) {
            case "firefox" -> playwright.firefox().launch(options);
            case "webkit"  -> playwright.webkit().launch(options);
            default        -> playwright.chromium().launch(options);
        };
    }

    @ThreadScopeBean
    public BrowserContext browserContext(Browser browser) {
        BrowserContext context = browser.newContext();
        context.setDefaultTimeout(defaultTimeout);
        return context;
    }

    @ThreadScopeBean
    public Page page(BrowserContext browserContext) {
        return browserContext.newPage();
    }
}
