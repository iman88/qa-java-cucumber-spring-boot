package com.qa.testNG.googletest;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.qa.TestNGTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class MultiBrowserTest extends TestNGTest {

    @Autowired
    private Playwright playwright;

    @Test
    public void multiBrowserTest() {
        try (Browser chromium = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
             Browser firefox = playwright.firefox().launch(new BrowserType.LaunchOptions().setHeadless(false))) {

            Page chromePage = chromium.newPage();
            Page firefoxPage = firefox.newPage();

            chromePage.navigate("https://www.google.com");
            firefoxPage.navigate("https://www.yahoo.com");

            assertThat(chromePage.title()).containsIgnoringCase("google");
            assertThat(firefoxPage.url()).containsIgnoringCase("yahoo");
        }
    }

}
