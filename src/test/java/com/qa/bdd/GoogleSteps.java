package com.qa.bdd;

import com.qa.page.google.GooglePage;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import com.qa.kelvin.annotation.LazyAutowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testng.Assert;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@CucumberContextConfiguration
@SpringBootTest
public class GoogleSteps {

    @LazyAutowired
    private GooglePage googlePage;

    @Given("I am on the google site")
    public void launchSite() {
        this.googlePage.goTo();
    }

    @When("I click on Accept All button")
    public void iClickOnAcceptAllButton() {
        this.googlePage.clickAcceptAllButton();
    }

    @When("I enter {string} as a keyword")
    public void enterKeyword(String keyword) {
        this.googlePage.getSearchComponent().search(keyword);
    }

    @Then("I should see search results page")
    public void clickSearch() {
        assertThat(this.googlePage.getSearchResult().isAt()).isTrue();
    }

    @Then("I should see at least {int} results")
    public void verifyResults(int count) {
        assertThat(this.googlePage.getSearchResult().getCount()).isGreaterThanOrEqualTo(count);
    }

}
