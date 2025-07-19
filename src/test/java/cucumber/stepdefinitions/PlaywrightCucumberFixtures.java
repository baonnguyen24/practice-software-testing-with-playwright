package cucumber.stepdefinitions;

import com.microsoft.playwright.*;
import io.cucumber.java.Before;
import io.cucumber.java.After;
import io.cucumber.java.AfterAll;

import java.util.Arrays;

public class PlaywrightCucumberFixtures {

    protected static ThreadLocal<Playwright> playwright
            = ThreadLocal.withInitial( () -> {
                Playwright playwright = Playwright.create();
                playwright.selectors().setTestIdAttribute("data-test");
                return playwright;
    });

    protected static ThreadLocal<Browser> browser
            = ThreadLocal.withInitial( () ->
                playwright.get().chromium().launch(
                    new BrowserType.LaunchOptions()
                        .setHeadless(false)
                        .setArgs(Arrays.asList("--no-sandbox", "--disable-extensions", "--disable-gpu"))
        )
    );

    static private final ThreadLocal<BrowserContext> browserContext = new ThreadLocal<>();

    static private final ThreadLocal<Page> page = new ThreadLocal<>();

    @Before(order = 100)
    public void SetupBrowserContex() {
        System.out.println("Setup Browser Context");
        browserContext.set(browser.get().newContext());
        page.set(browserContext.get().newPage());
    }

    @After
    public void closeContext() {
        System.out.println("Close Context");
        browserContext.get().close();
    }

    @AfterAll
    public static void tearDown() {
        System.out.println("Tear Down");
        browser.get().close();
        browser.remove();
        playwright.get().close();
        playwright.remove();
    }

    public static Page getPage() {
        return page.get();
    }
}
