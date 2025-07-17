package basic;

import com.microsoft.playwright.*;
import com.microsoft.playwright.assertions.PlaywrightAssertions;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import pageobjectcomponenets.*;

import java.util.Arrays;
import java.util.List;

public class PlaywrightPageObject {

    protected static Playwright playwright;
    protected static Browser browser;
    protected static BrowserContext browserContext;

    Page page;

    @BeforeAll
    static void setUpBrowser() {
        playwright = Playwright.create();
        playwright.selectors().setTestIdAttribute("data-test");
        browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions().setHeadless(false)
                        .setArgs(Arrays.asList("--no-sandbox", "--disable-extensions", "--disable-gpu"))
        );
    }

    @BeforeEach
    void setUp() {
        browserContext = browser.newContext();
        page = browserContext.newPage();
    }

    @AfterEach
    void closeContext() {
        browserContext.close();
    }

    @AfterAll
    static void tearDown() {
        browser.close();
        playwright.close();
    }

    void openHomePage() {
        page.navigate("https://practicesoftwaretesting.com");
    }

    @Nested
    class WhenSearchingProductByKeyboard {

    }

    @Nested
    class WhenAddingItemsToTheCart {

        @DisplayName("Without Page Object")
        @Test
        void withoutPageObject() {
            // 1. Search for "Pliers"
            openHomePage();
            page.getByPlaceholder("Search").fill("Pliers");
            page.getByPlaceholder("Search").press("Enter");

            // 2. Show details page
            page.locator(".card-title").getByText("Combination Pliers").click();

            // 3. Increase the quantity
            page.locator("#btn-increase-quantity").click();
            page.locator("#btn-increase-quantity").click();

            // 4. Add to cart
            page.locator("#btn-add-to-cart").click();

            // 5. Open the cart and check the correct quantity
            page.getByTestId("nav-cart").click();
            PlaywrightAssertions.assertThat(page.getByTestId("product-title")).hasText("Combination Pliers");
            PlaywrightAssertions.assertThat(page.getByTestId("product-quantity")).hasValue("3");
        }

        @DisplayName("With Page Object")
        @Test
        void withPageObject() {

            SearchComponent searchComponent = new SearchComponent(page);
            ProductList productList = new ProductList(page);
            ProductDetails productDetails = new ProductDetails(page);
            NavBar navbar = new NavBar(page);
            CheckoutCart checkoutCart = new CheckoutCart(page);

            openHomePage();

            // 1. Search for "Pliers
            searchComponent.searchBy("Pliers");

            // 2. Show details page
            productList.viewProductDetails("Combination Pliers");

            // 3. Increase the quantity & add to cart
            productDetails.increaseQuantityBy(2);
            productDetails.addToCart();

            // 5. Open the cart and check the correct quantity
            navbar.openCart();

            List<CheckoutCart.CartLineItem> lineItems = checkoutCart.getLineItems();

            Assertions.assertThat((lineItems))
                    .hasSize(1)
                    .first()
                    .satisfies(item -> {
                        Assertions.assertThat(item.title().contains("Combination Pliers"));
                        Assertions.assertThat(item.quantity()).isEqualTo(3);
                        Assertions.assertThat(item.total()).isEqualTo(item.quantity() * item.price());
                    });
        }
    }
}
