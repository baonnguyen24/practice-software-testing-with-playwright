package basic;

import com.microsoft.playwright.*;
import com.microsoft.playwright.assertions.PlaywrightAssertions;
import com.microsoft.playwright.options.AriaRole;
import org.junit.jupiter.api.*;

import java.util.Arrays;
import java.util.List;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import org.assertj.core.api.Assertions;

public class PlaywrightLocators {

    protected static Playwright playwright;
    protected static Browser browser;
    protected static BrowserContext browserContext;
    Page page;

    @BeforeAll
    static void setUpBrowser() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions().setHeadless(true)
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

    void openContactPage() {
        page.navigate("https://practicesoftwaretesting.com/contact");
    }

    @DisplayName("Locating elements by Text")
    @Nested
    class LocatingElementByText {

       @DisplayName("By id")
       @Test
       void locateFirstNameById() {
           openContactPage();
           page.locator("#first_name").fill("Jonathan");

           assertThat(page.locator("#first_name")).hasValue("Jonathan");
       }

       @DisplayName("By CSS class")
       @Test
       void locateTheSendButton() {
           openContactPage();
           page.locator("#first_name").fill("Jonathan");
           page.locator(".btnSubmit").click();
           List<String> alerts = page.locator(".alert").allTextContents();

           Assertions.assertThat(alerts).isNotEmpty();
       }

        @DisplayName("By Attribute")
        @Test
        void locateSendButtonByAttribute() {
           openContactPage();

           page.locator("[placeholder = 'Your last name *']").fill("Smith");
           assertThat(page.locator("#last_name")).hasValue("Smith");
        }

        @DisplayName("Search for Pliers")
        @Test
        void searchPliers() {

            // 1. Search for Pliers
            // 2. Assert there are 4 items
            // 3. Assert all names contains "Pliers"
            // 4. Assert there is 1 out of stock item
            // 5. Assert out of stock item is "Long Nose Pliers"

            // NOTE:
            // - PlaywrightAssertion only accept Playwright Object (Locator, API Response, JSHandle)
            // - For other data type e.g List, use AssertJ (org.assertj.core.api.Assertions)
            page.navigate("https://practicesoftwaretesting.com");

            page.getByPlaceholder("Search").fill("Pliers");
            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Search")).click();

            assertThat(page.locator(".card")).hasCount(4);

            List<String> productNames = page.locator(".card-title").allTextContents();
            Assertions.assertThat(productNames).allMatch(name -> name.contains("Pliers"));

            Locator outOfStockItem = page.locator(".card")
                    .filter(new Locator.FilterOptions().setHasText("Out of stock"))
                    .locator(".card-title");

            assertThat(outOfStockItem).hasCount(1);
            assertThat(outOfStockItem).hasText("Long Nose Pliers");
        }
    }
}
