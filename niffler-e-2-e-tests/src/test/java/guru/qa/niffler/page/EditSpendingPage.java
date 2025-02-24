package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.test.web.BaseTest;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Selenide.$;

public class EditSpendingPage extends BaseTest {
    private final SelenideElement
            descriptionInput = $("#description"),
            categoryInput = $("#category"),
            amountInput = $("#amount"),
            saveBtn = $("#save");

    @Step("Установить новое описание траты")
    public EditSpendingPage setNewSpendingDescription(String description) {
        descriptionInput.setValue(description);
        return this;
    }

    @Step("Кликнуть на кнопку сохранения")
    public void save() {
        saveBtn.click();
    }

    @Step("Установить название категории")
    public EditSpendingPage setSpendingCategory(String category) {
        categoryInput.setValue(category);
        return this;
    }

    @Step("Установить стоимость траты")
    public EditSpendingPage setSpendingAmount(String amount) {
        amountInput.setValue(amount);
        return this;
    }
}
