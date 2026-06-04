package com.qa.page.window;

import com.qa.kelvin.annotation.Window;
import com.qa.page.Base;

@Window("Page B")
public class PageB extends Base {

    private static final String TEXT_AREA = "#area";

    public void addToArea(final String msg) {
        page.locator(TEXT_AREA).pressSequentially(msg);
    }

    @Override
    public boolean isAt() {
        return page.locator(TEXT_AREA).isVisible();
    }
}
