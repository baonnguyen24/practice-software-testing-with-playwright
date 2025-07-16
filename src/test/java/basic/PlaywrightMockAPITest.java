package basic;

import com.microsoft.playwright.*;
import com.microsoft.playwright.assertions.PlaywrightAssertions;
import org.junit.jupiter.api.*;

import java.util.Arrays;

public class PlaywrightMockAPITest {

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

        page.navigate("https://practicesoftwaretesting.com");
        page.getByPlaceholder("Search").waitFor();

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

    @DisplayName("When a search return a single product")
    @Test
    void whenSingleProductIsFound() {

        // Mocking API
        // Request URL: https://api.practicesoftwaretesting.com/products/search?q=Pliers
        page.route("**/products/search?q=Pliers", route -> {
            route.fulfill(
                    new Route.FulfillOptions()
                            .setBody(MockSearchResponses.RESPONSE_WITH_A_SINGLE_ENTRY)
                            .setStatus(200)
            );
        });

        page.navigate("https://practicesoftwaretesting.com");
        page.getByPlaceholder("Search").fill("Pliers");
        page.getByPlaceholder("Search").press("Enter");

        PlaywrightAssertions.assertThat(page.getByTestId("product-name")).hasCount(1);
        PlaywrightAssertions.assertThat(page.getByTestId("product-name")).hasText("Super Pliers");
    }

    @DisplayName("When a search return no product")
    @Test
    void whenNoItemsAreFound() {

        // Mocking API
        // Request URL: https://api.practicesoftwaretesting.com/products/search?q=Pliers
        page.route("**/products/search?q=Pliers", route -> {
            route.fulfill(
                    new Route.FulfillOptions()
                            .setBody(MockSearchResponses.RESPONSE_WITH_NO_ENTRIES)
                            .setStatus(200)
            );
        });

        page.navigate("https://practicesoftwaretesting.com");
        page.getByPlaceholder("Search").fill("Pliers");
        page.getByPlaceholder("Search").press("Enter");

        PlaywrightAssertions.assertThat(page.getByTestId("product-name")).hasCount(0);
        PlaywrightAssertions.assertThat(page.getByTestId("search_completed")).hasText("There are no products found.");
    }



}
