package com.qa.page.google;

import com.qa.kelvin.annotation.PageFragment;
import com.qa.page.Base;

@PageFragment
public class SearchResult extends Base {

    // Bing organic search result items.
    private static final String RESULTS = "li.b_algo";

    public int getCount() {
        return page.locator(RESULTS).count();
    }

    @Override
    public boolean isAt() {
        page.locator("#b_results").waitFor();
        return getCount() > 0;
    }
}
