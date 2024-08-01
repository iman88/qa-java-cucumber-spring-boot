package com.qa.testNG.googletest;

import com.qa.TestNGTest;
import com.qa.kelvin.annotation.LazyAutowired;
import com.qa.kelvin.service.ScreenshotService;
import com.qa.page.google.GooglePage;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class Google2Test extends TestNGTest {

    @LazyAutowired
    private GooglePage googlePage;

    @LazyAutowired
    private ScreenshotService screenshotService;

    @Test
    public void googleTest() throws IOException {
        this.googlePage.goTo();
        this.googlePage.clickAcceptAllButton();
        Assert.assertTrue(this.googlePage.isAt());
        this.googlePage.getSearchComponent().search("selenium");
        assertThat(this.googlePage.getSearchResult().isAt()).isTrue();
        assertThat(this.googlePage.getSearchResult().getCount()).isGreaterThan(1);
        this.screenshotService.takeScreenShot();
        this.googlePage.close();
    }

}
