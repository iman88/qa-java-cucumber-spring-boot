package com.qa.page.visa;

import com.microsoft.playwright.options.SelectOption;
import com.qa.kelvin.annotation.Page;
import com.qa.page.Base;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDate;
import java.util.Objects;

@Page
public class VisaRegistrationPage extends Base {

    private static final Logger logger = LoggerFactory.getLogger(VisaRegistrationPage.class);

    @Value("${visa.url}")
    private String url;

    public void goTo() {
        page.navigate(url);
    }

    public void setNames(String firstName, String lastName) {
        logger.info("Setting names: {}", firstName);
        page.locator("#first_4").fill(firstName);
        page.locator("#last_4").fill(lastName);
    }

    public void setCountryFromAndTo(String countryFrom, String countryTo) {
        page.locator("#input_46").selectOption(countryFrom);
        page.locator("#input_47").selectOption(countryTo);
    }

    public void setBirthDate(LocalDate date) {
        page.locator("#input_24_year").selectOption(new SelectOption().setLabel(String.valueOf(date.getYear())));
        page.locator("#input_24_day").selectOption(new SelectOption().setLabel(String.valueOf(date.getDayOfMonth())));
        page.locator("#input_24_month").selectOption(date.getMonth().toString());
    }

    public void setContactDetails(String email, String phone) {
        page.locator("#input_6").fill(email);
        page.locator("#input_27_phone").fill(phone);
    }

    public void setComments(String comments) {
        page.locator("#input_45").fill(Objects.toString(comments, ""));
    }

    public void submit() {
        page.locator("#submitBtn").click();
    }

    public String getConfirmationNumber() {
        page.locator("#requestnumber").waitFor();
        return page.locator("#requestnumber").textContent();
    }

    @Override
    public boolean isAt() {
        return page.locator("#first_4").isVisible();
    }
}
