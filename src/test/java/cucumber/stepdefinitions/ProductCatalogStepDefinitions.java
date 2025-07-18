package cucumber.stepdefinitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class ProductCatalogStepDefinitions {

    @Given("Sally is on the home page")
    public void sally_is_on_the_home_page() {
        // Write code here that turns the phrase above into concrete actions

    }
    @When("she searches for {string}")
    public void she_searches_for(String searchTerm) {
        // Write code here that turns the phrase above into concrete actions

    }
    @Then("the {string} product should be displayed")
    public void the_product_should_be_displayed(String productName) {
        // Write code here that turns the phrase above into concrete actions

    }
}
