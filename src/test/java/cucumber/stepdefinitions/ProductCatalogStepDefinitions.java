package cucumber.stepdefinitions;

import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.assertj.core.api.Assertions;
import pageobjectcomponenets.*;


public class ProductCatalogStepDefinitions {

    NavBar navBar;
    ProductList productList;
    SearchComponent searchComponent;

    @Before
    public void setupPageObject() {
        navBar = new NavBar(PlaywrightCucumberFixtures.getPage());
        productList = new ProductList(PlaywrightCucumberFixtures.getPage());
        searchComponent = new SearchComponent(PlaywrightCucumberFixtures.getPage());
    }

    @Given("Sally is on the home page")
    public void sally_is_on_the_home_page() {
        // Write code here that turns the phrase above into concrete actions
        navBar.openHomePage();

    }
    @When("she searches for {string}")
    public void she_searches_for(String searchTerm) {
        // Write code here that turns the phrase above into concrete actions
        searchComponent.searchBy(searchTerm);

    }
    @Then("the {string} product should be displayed")
    public void the_product_should_be_displayed(String productName) {
        // Write code here that turns the phrase above into concrete actions
        var matchingProducts = productList.getProductNames();
        Assertions.assertThat(matchingProducts).contains(productName);
    }
}
