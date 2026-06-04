package com.qa.kelvin.scope;

import com.microsoft.playwright.BrowserContext;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.context.support.SimpleThreadScope;

public class BrowserScope extends SimpleThreadScope {

    private static final String[] ALL_BEANS = {"playwright", "browser", "browserContext", "page"};

    @Override
    public Object get(String name, ObjectFactory<?> objectFactory) {
        Object bean = super.get(name, objectFactory);
        if (bean instanceof BrowserContext ctx) {
            try {
                if (!ctx.browser().isConnected()) {
                    super.remove(name);
                    bean = super.get(name, objectFactory);
                }
            } catch (Exception e) {
                super.remove(name);
                bean = super.get(name, objectFactory);
            }
        }
        return bean;
    }

    @Override
    public void registerDestructionCallback(String name, Runnable callback) {
    }

    public void clearCurrentThread() {
        for (String name : ALL_BEANS) {
            super.remove(name);
        }
    }
}
