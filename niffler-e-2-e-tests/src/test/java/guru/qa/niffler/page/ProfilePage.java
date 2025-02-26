package guru.qa.niffler.page;

import com.codeborne.selenide.ClickOptions;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class ProfilePage extends BasePage<ProfilePage> {
    private static final ElementsCollection
            searchCategory = $$("[class='MuiBox-root css-1lekzkb']");

    private final SelenideElement
            imageUpload = $(".image__input-label"),
            saveChangesButton = $(":r1:"),
            inputName = $("#name"),
            showArchivedCheckBox = $("[type='checkbox']"),
            inputCategory = $("#category"),
            alertSuccessUpdate = $("[role='alert']"),
            closeAlert = $("[data-testid='CloseIcon']"),
            categoryNames = $("div.MuiGrid-spacing-xs-2"),
            closeOrArchiveCategoryOrUnarchive = $(".MuiDialogActions-spacing"),
            nameInput = $("#name").as("поле ввода имени пользователя"),
            submitBtn = $("[type='submit']").as("кнопка сохранения изменений");

    @Step("Ввести имя")
    public ProfilePage setName(String name) {
        inputName.setValue(name);

        return new ProfilePage();
    }

    @Step("Ввести имя Категории")
    public ProfilePage setCategory(String category) {
        inputCategory.setValue(category);

        return new ProfilePage();
    }

    @Step("Кликнуть на чекбокс архивации Категории")
    public ProfilePage clickOnCheckboxShowArchived() {
        showArchivedCheckBox.click(ClickOptions.usingJavaScript());

        return new ProfilePage();
    }

    @Step("Кликнуть на сохранение изменений")
    public ProfilePage clickOnSaveChangesButton() {
        saveChangesButton.click();

        return new ProfilePage();
    }

    @Step("Загрузить картинку")
    public void uploadImage() {
        imageUpload.click();
    }

    @Step("Проверить уведомление успешной загрузки и закрыть окно")
    public void checkAlertSuccessfulUpdateAndCloseAlert() {
        alertSuccessUpdate.shouldHave(text("Profile successfully updated"));
        closeAlert.click();
    }

    @Step("Кликнуть на {archiveOrUnarchive} Категорию")
    public ProfilePage clickArchiveOrUnarchiveCategory(String archiveOrUnarchive) {
        closeOrArchiveCategoryOrUnarchive.$(byText(archiveOrUnarchive)).click();

        return new ProfilePage();
    }

    @Step("Проверка присутствия Категории по имени {nameCategory}")
    public void checkCategoryByNameInProfile(String nameCategory) {
        categoryNames.shouldHave(text(nameCategory));
    }

    @Step("Проверка отсутствия Категории по имени {nameCategory}")
    public void checkNotCategoryByNameInProfile(String nameCategory) {
        categoryNames.shouldNotHave(text(nameCategory));
    }

    @Step("Кликнуть на Архивацию Категорию")
    public ProfilePage clickArchiveCategory(String name) {
        searchCategory.filter(text(name)).first().$("button[aria-label='Archive category']")
                .click(ClickOptions.usingJavaScript());

        return new ProfilePage();
    }

    @Step("Кликнуть на Разархивацию Категорию")
    public ProfilePage clickUnarchiveCategory(String name) {
        searchCategory.filter(text(name)).first().$("[data-testid='UnarchiveOutlinedIcon']")
                .click(ClickOptions.usingJavaScript());

        return new ProfilePage();
    }

    @Step("Проверка имени пользователя")
    public void checkName(String name) {
        nameInput.shouldHave(value(name));
    }

    @Override
    public ProfilePage checkThatPageLoaded() {
        return null;
    }
}
