package basic;

import com.microsoft.playwright.*;
import com.microsoft.playwright.assertions.PlaywrightAssertions;
import org.junit.jupiter.api.*;

import java.util.Arrays;

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

    @DisplayName("Locating elements by Text")
    @Nested
    class LocatingElementByText {

       @DisplayName("Using text")
       @Test
       void byText(){
           page.navigate("https://practicesoftwaretesting.com");
           page.getByText("Combination Pliers").click();

           PlaywrightAssertions.assertThat(page.getByText("ForgeFlex Tools")).isVisible();
       }

       @DisplayName("Using Alt Text")
       @Test
       void byAltText(){
           page.navigate("https://practicesoftwaretesting.com");
           page.getByAltText("Combination Pliers").click();

           PlaywrightAssertions.assertThat(page.getByText("ForgeFlex Tools")).isVisible();
       }

        @DisplayName("Using Title")
        @Test
        void byTitle(){
            page.navigate("https://practicesoftwaretesting.com");

            page.getByText("Combination Pliers").click();
            page.getByTitle("Practice Software Testing - Toolshop").click();

        }
    }
}
