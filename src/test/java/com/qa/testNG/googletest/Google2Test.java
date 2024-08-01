package com.qa.testNG.googletest;

import com.qa.TestNGTest;
import com.qa.kelvin.annotation.LazyAutowired;
import com.qa.kelvin.service.ScreenshotService;
import com.qa.page.google.GooglePage;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;

public class Google2Test extends TestNGTest {

    @LazyAutowired
    private GooglePage googlePage;

    @LazyAutowired
    private ScreenshotService screenshotService;

    @Test
    public void googleTest() throws IOException {
        this.googlePage.goTo();
        Assert.assertTrue(this.googlePage.isAt());
        this.googlePage.getSearchComponent().search("selenium");
        Assert.assertTrue(this.googlePage.getSearchResult().isAt());
        Assert.assertTrue(this.googlePage.getSearchResult().getCount() > 25);
        this.screenshotService.takeScreenShot();
        this.googlePage.close();
    }

}
