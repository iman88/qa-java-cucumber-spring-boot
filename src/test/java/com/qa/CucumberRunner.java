package com.qa;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.DataProvider;

@CucumberOptions(
        features = "classpath:features",
        glue = "com.qa.bdd",
        plugin = {
                "pretty",
                "json:target/cucumber-report-json/report.json",
                "html:target/cucumber-report-html/report.html"
        },
        tags =  "@all"
)
public class CucumberRunner extends AbstractTestNGCucumberTests {

    @Override
    @DataProvider(parallel = true)
    public Object[][] scenarios() {
        return super.scenarios();
    }

}
