package guru.qa.niffler.page.component;

import io.qameta.allure.Step;
import org.openqa.selenium.Keys;

import static com.codeborne.selenide.Selenide.$;

public class SearchField extends BaseComponent<SearchField> {

    public SearchField() {
        super($("input[aria-label='search']"));
    }

//    private final SelenideElement
//            searchInput = $("input[type='text']");

    @Step("Осуществить поиск")
    public SearchField search(String query) {
        self.setValue(query);
        self.sendKeys(Keys.ENTER);
        return this;
    }

    @Step("Очистить строку поиска")
    public SearchField clear() {
        self.clear();
        return this;
    }
}
