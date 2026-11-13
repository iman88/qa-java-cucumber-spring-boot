package com.qa.page;

import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

public abstract class Base {

    @Autowired
    protected Page page;

    @Autowired
    private ApplicationContext ctx;

    public void setPage(Page page) {
        this.page = page;
    }

    public Page defaultPage() {
        return ctx.getBean(Page.class);
    }

    protected BrowserContext browserContext() {
        return ctx.getBean(BrowserContext.class);
    }

    public abstract boolean isAt();
}
