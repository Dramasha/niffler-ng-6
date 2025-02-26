package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.component.Calendar;
import guru.qa.niffler.page.component.Header;
import guru.qa.niffler.page.component.SearchField;
import lombok.Getter;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

@Getter
public abstract class BasePage<T> {

    private final Header header = new Header();
    private final Calendar calendar = new Calendar();
    private final SearchField searchField = new SearchField();

    protected final SelenideElement
            alert = $("[role='alert']"),
            closeAlert = $("[data-testid='CloseIcon']");

    public abstract T checkThatPageLoaded();

    @SuppressWarnings("unchecked")
    public T checkAlert(String message) {
        alert.shouldHave(text(message));
        closeAlert.click();
        return (T) this;
    }
}
