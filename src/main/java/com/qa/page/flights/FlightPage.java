package com.qa.page.flights;

import com.qa.kelvin.annotation.Page;
import com.qa.kelvin.annotation.TakeScreenshot;
import com.qa.page.Base;

import java.util.List;
import java.util.stream.Collectors;

@Page
public class FlightPage extends Base {

    private static final String NAV_LINKS = "nav.rlGvde a";

    public void goTo(final String url) {
        page.navigate(url);
        page.setViewportSize(1920, 1080);
    }

    @TakeScreenshot
    public List<String> getLabels() {
        return page.locator(NAV_LINKS).allInnerTexts().stream()
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());
    }

    @Override
    public boolean isAt() {
        return page.locator(NAV_LINKS).first().isVisible();
    }
}
