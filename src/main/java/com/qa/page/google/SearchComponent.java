package com.qa.page.google;

import com.qa.kelvin.annotation.PageFragment;
import com.qa.page.Base;

@PageFragment
public class SearchComponent extends Base {

    private static final String SEARCH_BOX = "[name='q']";

    public void search(final String keyword) {
        page.locator(SEARCH_BOX).fill(keyword);
        page.locator(SEARCH_BOX).press("Enter");
        page.waitForLoadState();
    }

    @Override
    public boolean isAt() {
        return page.locator(SEARCH_BOX).isVisible();
    }
}
