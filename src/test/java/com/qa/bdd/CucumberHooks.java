package com.qa.bdd;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Playwright;
import com.qa.kelvin.scope.BrowserScope;
import io.cucumber.java.After;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;

public class CucumberHooks {

    @Autowired
    private ConfigurableApplicationContext applicationContext;

    // order=0 runs last among @After hooks — correct for browser teardown
    @After(order = 0)
    public void cleanupBrowser() {
        BrowserScope scope = (BrowserScope) applicationContext.getBeanFactory()
                .getRegisteredScope("browserscope");
        if (scope == null) return;

        try { applicationContext.getBean(BrowserContext.class).close(); } catch (Exception ignored) {}
        try { applicationContext.getBean(Browser.class).close(); } catch (Exception ignored) {}
        try { applicationContext.getBean(Playwright.class).close(); } catch (Exception ignored) {}

        scope.clearCurrentThread();
    }
}
