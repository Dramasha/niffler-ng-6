package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.component.Header;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

public abstract class BasePage<T> {

    private final Header header = new Header();

    protected final SelenideElement
            alert = $("[role='alert']"),
            closeAlert = $("[data-testid='CloseIcon']");

    @SuppressWarnings("unchecked")
    public T checkAlert(String message) {
        alert.shouldHave(text(message));
        closeAlert.click();
        return (T) this;
    }
}
