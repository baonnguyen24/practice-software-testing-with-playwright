package pageobjectcomponenets;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

public class SearchComponent {
    private final Page page;

    public SearchComponent(Page page) {
        this.page = page;
    }

    public void searchBy(String keyword) {
        page.waitForResponse("**/products/search?**", () -> {
            page.getByPlaceholder("Search").fill(keyword);
            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Search")).click();
        });
    }

    public void sortBy(String sortFilter) {
        page.waitForResponse("**/products?sort=**", () -> {
            page.getByTestId("sort").selectOption(sortFilter);
        });
    }
}
