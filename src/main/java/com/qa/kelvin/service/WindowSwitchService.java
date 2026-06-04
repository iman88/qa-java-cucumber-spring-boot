package com.qa.kelvin.service;

import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class WindowSwitchService {

    @Autowired
    private ApplicationContext ctx;

    public Page getPageByTitle(final String title) {
        BrowserContext context = ctx.getBean(BrowserContext.class);
        return context.pages().stream()
                .filter(p -> p.title().startsWith(title))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No page with title starting: " + title));
    }

    public Page getPageByIndex(final int index) {
        return ctx.getBean(BrowserContext.class).pages().get(index);
    }
}
