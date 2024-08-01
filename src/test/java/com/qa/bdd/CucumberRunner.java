package com.qa.bdd;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.DataProvider;

@SuppressWarnings("SpellCheckingInspection")
@CucumberOptions(
        features = "classpath:features",
        glue = "com.qa.bdd",
        tags =  "@visa",
        plugin = {
                "pretty",
                "json:target/cucumber-report-json/report.json",
                "html:target/cucumber-report-html/report.html"
        }
)
public class CucumberRunner extends AbstractTestNGCucumberTests {

    @Override
    @DataProvider(parallel = true)
    public Object[][] scenarios() {
        return super.scenarios();
    }

}
