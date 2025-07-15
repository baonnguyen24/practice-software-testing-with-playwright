package basic;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class WorkingWithFields {

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

    void openContactPage() {
        page.navigate("https://practicesoftwaretesting.com/contact");
    }


    @DisplayName("Interacting with Fields")
    @Nested
    class InteractingWithFields{

       @DisplayName("interacting with fields")
       @Test
       void completeForm() throws URISyntaxException {
           openContactPage();

           // Working with various types of fields
           var firstName = page.locator("#first_name");
           var lastName = page.locator("#last_name");
           var email = page.locator("#email");
           var message = page.locator("#message");
           var dropdown = page.locator("#subject");
           var uploadFile = page.locator("#attachment");

           firstName.fill("Jonathan");
           lastName.fill("Smith");
           email.fill("jsmith@mail.com");
           message.fill("hello world!!!");
           dropdown.selectOption("webmaster");

           // Upload file
           Path fileToUpload = Paths.get(ClassLoader.getSystemResource("data/test.txt").toURI());
           page.setInputFiles("#attachment", fileToUpload);

           // Assertion
           assertThat(firstName).hasValue("Jonathan");
           assertThat(lastName).hasValue("Smith");
           assertThat(email).hasValue("jsmith@mail.com");
           assertThat(message).hasValue("hello world!!!");
           assertThat(dropdown).hasValue("webmaster");

           String uploadedFile = uploadFile.inputValue();
           Assertions.assertThat(uploadedFile).endsWith("test.txt");
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
