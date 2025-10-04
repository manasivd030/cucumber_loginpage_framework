package steps;

import config.Config;
import org.testng.Assert;
import hooks.TestHooks;
import io.cucumber.java.en.*;
import pages.LoginPage;

public class LoginSteps {

    private LoginPage page;
    @Given("I open the login page")
    public void openLogin() {
        page = new LoginPage(TestHooks.driver);
        page.open(Config.baseUrl());
    }

    @When("I login with valid credentials")
    public void loginValid() {
        page.login(Config.get("valid.username"), Config.get("valid.password"));
    }

    @When("I login with username {string} and password {string}")
    public void loginCustom(String user, String pass) {
        page.login(user, pass);
    }

    @Then("I should see a success message {string}")
    public void assertSuccess(String expected) {
        Assert.assertTrue(page.flashText().contains(expected),
                "Expected success to contain: " + expected + " but got: " + page.flashText());
    }

    @Then("I should see an error message {string}")
    public void assertError(String expected) {
        Assert.assertTrue(page.flashText().contains(expected),
                "Expected error to contain: " + expected + " but got: " + page.flashText());
    }
}
