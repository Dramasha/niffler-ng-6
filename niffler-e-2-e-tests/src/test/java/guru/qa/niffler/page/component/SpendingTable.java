package guru.qa.niffler.page.component;

import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.EditSpendingPage;
import io.qameta.allure.Step;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.CollectionCondition.textsInAnyOrder;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class SpendingTable {
    private final SelenideElement table = $(".MuiTableContainer-root");

    @Step("Выбрать период")
    public SpendingTable selectPeriod(String period) {
        table.$("#period").click();
        $$("[role='option']").find(text(period)).click();
        return this;
    }

    @Step("Нажать на кнопку редактирования траты")
    public EditSpendingPage editSpending(String spendingDescription) {
        table.$("tbody").$$("tr").find(text(spendingDescription)).$$("td").get(5).click();
        return new EditSpendingPage();
    }

    @Step("Удалить трату")
    public SpendingTable deleteSpending(String spendingDescription) {
        table.$("tbody").$$("tr").find(text(spendingDescription)).$$("td").get(1).click();
        table.$("#delete").click();
        $(".MuiDialogActions-spacing [type='button']:nth-child(2)").click();
        return this;
    }

    @Step("Найти трату по описанию")
    public SpendingTable searchSpendingByDescription(String spendingDescription) {
        table.$("tbody").$$("tr").find(text(spendingDescription)).shouldBe(visible);
        return this;
    }

    @Step("Проверка того, что в списке есть ожидаемая трата")
    public SpendingTable checkTableContains(String... expectedSpends) {
        table.$("tbody").$$("tr td:nth-child(4)").shouldHave(textsInAnyOrder(expectedSpends));
        return this;
    }

    @Step("Проверка того, что в списке есть ожидаемая трата")
    public SpendingTable checkTableSize(int expectedSize) {
        table.$("tbody").$$("tr").shouldHave(size(expectedSize));
        return this;
    }
}
