package com.qa.page.window;

import com.qa.kelvin.annotation.Page;
import com.qa.page.Base;
import org.springframework.beans.factory.annotation.Value;

@Page
public class MainPage extends Base {

    @Value("${window.url}")
    private String url;

    public void goTo() {
        page.navigate(url);
    }

    public void launchAllWindows() {
        int count = page.locator("a").count();
        for (int i = 0; i < count; i++) {
            final int index = i;
            browserContext().waitForPage(() -> page.locator("a").nth(index).click());
        }
    }

    @Override
    public boolean isAt() {
        return page.locator("a").first().isVisible();
    }
}
