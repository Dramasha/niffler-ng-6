package guru.qa.niffler.page.component;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Selenide.$;

public class SearchField {
    private final SelenideElement searchInput = $("input[type='text']");

    @Step("Осуществить поиск")
    public SearchField search(String query) {
        searchInput.setValue(query);
        searchInput.pressEnter();
        return this;
    }

    @Step("Очистить строку поиска")
    public SearchField clear() {
        searchInput.clear();
        return this;
    }
}
