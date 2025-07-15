package basic;

import com.microsoft.playwright.*;
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


}
