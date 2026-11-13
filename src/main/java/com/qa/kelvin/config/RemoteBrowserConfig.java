package com.qa.kelvin.config;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.Playwright;
import com.qa.kelvin.annotation.LazyConfiguration;
import com.qa.kelvin.annotation.ThreadScopeBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;

@Profile("remote")
@LazyConfiguration
public class RemoteBrowserConfig {

    @Value("${playwright.remote.url}")
    private String remoteUrl;

    @ThreadScopeBean
    public Browser browser(Playwright playwright) {
        return playwright.chromium().connect(remoteUrl);
    }
}
