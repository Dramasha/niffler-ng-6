package guru.qa.niffler.page.compinent;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import org.openqa.selenium.Keys;

import static com.codeborne.selenide.Selenide.$;

public class SearchField {
    private final SelenideElement searchInput = $("input[type='text']");

    @Step("Осуществить поиск")
    public SearchField search(String query) {
        searchInput.setValue(query);
        searchInput.sendKeys(Keys.ENTER);
        return this;
    }

    @Step("Очистить строку поиска")
    public SearchField clear() {
        searchInput.clear();
        return this;
    }
}
