package guru.qa.niffler.page.component;

import com.codeborne.selenide.SelenideElement;

public abstract class BaseComponent <T> {

    protected final SelenideElement self;

    public BaseComponent(SelenideElement self) {
        this.self = self;
    }
}
