package cucumber.stepdefinitions;

import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.assertj.core.api.Assertions;
import pageobjectcomponenets.*;

import java.util.List;


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
        navBar.openHomePage();

    }
    @When("she searches for {string}")
    public void she_searches_for(String searchTerm) {
        searchComponent.searchBy(searchTerm);

    }
    @Then("the {string} product should be displayed")
    public void the_product_should_be_displayed(String productName) {
        var matchingProducts = productList.getProductNames();
        Assertions.assertThat(matchingProducts).contains(productName);
    }

    @Then("the following products should be displayed:")
    public void the_following_products_should_be_displayed(List<String> expectedProducts) {
        var matchingProducts = productList.getProductNames();
        Assertions.assertThat(matchingProducts).containsAll(expectedProducts);
    }

    @When("she sorts by {string}")
    public void she_sorts_by(String sortFilter) {
        searchComponent.sortBy(sortFilter);

    }
    @Then("the first product displayed should be {string}")
    public void the_first_product_displayed_should_be(String firstProduct) {
        List<String> productNames = productList.getProductNames();
        Assertions.assertThat(productNames).startsWith(firstProduct);
    }
}
