package guru.qa.niffler.page.component;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.EditSpendingPage;
import io.qameta.allure.Step;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.CollectionCondition.textsInAnyOrder;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class SpendingTable extends BaseComponent<SpendingTable> {

    public SpendingTable() {
        super($(".MuiTableContainer-root"));
    }

    private final ElementsCollection
            tableRows = $("#spendings tbody").$$("tr");

    private final SelenideElement
            table = $(".MuiTableContainer-root"),
            searchInput = $("[placeholder='Search']"),
            clickSearch = $("[data-testid='SearchIcon']"),
            allArea = $("[aria-labelledby='tableTitle']");

    @Step("Выбрать период")
    public SpendingTable selectPeriod(String period) {
        self.$("#period").click();
        $$("[role='option']").find(text(period)).click();
        return this;
    }

    @Step("Нажать на кнопку редактирования траты")
    public EditSpendingPage editSpending(String spendingDescription) {
        self.$("tbody").$$("tr").find(text(spendingDescription)).$$("td").get(5).click();
        return new EditSpendingPage();
    }

    @Step("Удалить трату")
    public SpendingTable deleteSpending(String spendingDescription) {
        self.$("tbody").$$("tr").find(text(spendingDescription)).$$("td").get(1).click();
        self.$("#delete").click();
        $(".MuiDialogActions-spacing [type='button']:nth-child(2)").click();
        return this;
    }

    @Step("Найти трату по описанию")
    public SpendingTable searchSpendingByDescription(String spendingDescription) {
        self.$("tbody").$$("tr").find(text(spendingDescription)).shouldBe(visible);
        return this;
    }

    @Step("Проверка того, что в списке есть ожидаемая трата")
    public SpendingTable checkTableContains(String... expectedSpends) {
        self.$("tbody").$$("tr td:nth-child(4)").shouldHave(textsInAnyOrder(expectedSpends));
        return this;
    }

    @Step("Проверка того, что в списке есть ожидаемая трата")
    public SpendingTable checkTableSize(int expectedSize) {
        self.$("tbody").$$("tr").shouldHave(size(expectedSize));
        return this;
    }

    @Step("Поиск спенда по имени {nameSpending}")
    public void searchSpendsByName(String nameSpending) {
        searchInput.setValue(nameSpending);
        searchInput.shouldHave(text(nameSpending));
        clickSearch.click();
    }

    @Step("Поиск спенда по имени {nameSpending} после поиска")
    public void checkSpendAfterSearch(String name) {
        allArea.shouldHave(text(name));
    }

    @Step("Проверка того, что в списке есть ожидаемый спенд")
    public void checkThatTableContainsSpending(String spendingDescription) {
        tableRows.find(text(spendingDescription)).should(visible);
    }
}


