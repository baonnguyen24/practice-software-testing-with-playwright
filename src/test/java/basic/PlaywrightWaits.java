package basic;

import com.microsoft.playwright.*;
import com.microsoft.playwright.assertions.PlaywrightAssertions;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.WaitForSelectorState;
import org.assertj.core.api.Assert;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;

import java.util.Arrays;
import java.util.List;

public class PlaywrightWaits {

    protected static Playwright playwright;
    protected static Browser browser;
    protected static BrowserContext browserContext;
    Page page;

    @BeforeAll
    static void setUpBrowser() {
        playwright = Playwright.create();
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

    void openPage() {
        page.navigate("https://practicesoftwaretesting.com");

        // LESSON LEARN: Without wait, the test will fail because it will take a little bit for the components to load
        page.waitForSelector("[data-test='product-name']");
    }

    @DisplayName("Introducing Wait")
    @Test
    void shouldShowAllProductNames() {
        openPage();
        List<String> productNames = page.locator(".card-title").allInnerTexts();
        Assertions.assertThat(productNames).contains("Pliers", "Bolt Cutters", "Hammer");
    }

    @Test
    void shouldShowAllProductImage() {
        openPage();
        List<String> productTitles = page.locator(".card-img-top").all()
                .stream()
                .map(img -> img.getAttribute("alt"))
                .toList();
        Assertions.assertThat(productTitles).contains("Pliers", "Bolt Cutters", "Hammer");
    }

    @Test
    @DisplayName("Should filter product by category")
    void shouldFilterProductByCategory() {
        openPage();
        page.getByRole(AriaRole.MENUBAR).getByText("Categories").click();
        page.getByRole(AriaRole.MENUBAR).getByText("Power Tools").click();

        // LESSON LEARN: The page will take a while to load all components. So need to set a wait here
        page.waitForSelector(".card",
                new Page.WaitForSelectorOptions().setState(WaitForSelectorState.VISIBLE));

        var filteredProduct = page.locator(".card-title").allInnerTexts();

        Assertions.assertThat(filteredProduct).contains("Sheet Sander", "Belt Sander");
    }

    @Test
    @DisplayName("Waiting for element to appear and disappear")
    void shouldDisplayToasterMessage() {
        openPage();
        page.getByText("Bolt Cutters").click();
        page.getByText("Add to cart").click();

        // Wait for Toaster message to appear
        PlaywrightAssertions.assertThat(page.getByRole(AriaRole.ALERT)).isVisible();
        PlaywrightAssertions.assertThat(page.getByRole(AriaRole.ALERT)).hasText("Product added to shopping cart.");

        // Wait for Toaster message to disappear
        page.waitForCondition( () -> page.getByRole(AriaRole.ALERT).isHidden());
    }

    @Test
    @DisplayName("Should update the cart item count")
    void shouldUpdateCartItemCount() {
        openPage();
        page.getByText("Bolt Cutters").click();
        page.getByText("Add to cart").click();

        page.waitForSelector("[data-test='cart-quantity']:has-text('1')");
    }

}
