package guru.qa.niffler.page;

import com.codeborne.selenide.ClickOptions;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class ProfilePage {
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
            categoryNames = $("[aria-label='Edit category']"),
            closeOrArchiveCategoryOrUnarchive = $(".MuiDialogActions-spacing");

    public ProfilePage setName(String name) {
        inputName.setValue(name);

        return new ProfilePage();
    }

    public ProfilePage setCategory(String category) {
        inputCategory.setValue(category);

        return new ProfilePage();
    }

    public ProfilePage clickOnCheckboxShowArchived() {
        showArchivedCheckBox.click(ClickOptions.usingJavaScript());

        return new ProfilePage();
    }

    public ProfilePage clickOnSaveChangesButton() {
        saveChangesButton.click();

        return new ProfilePage();
    }

    public void uploadImage() {
        imageUpload.click();
    }

    public void checkAlertSuccessfulUpdateAndCloseAlert() {
        alertSuccessUpdate.shouldHave(text("Profile successfully updated"));
        closeAlert.click();
    }

    public ProfilePage clickArchiveOrUnarchiveCategory(String archiveOrUnarchive) {
        closeOrArchiveCategoryOrUnarchive.$(byText(archiveOrUnarchive)).click();

        return new ProfilePage();
    }

    public void checkCategoryByNameInProfile(String nameCategory) {
        categoryNames.parent().parent()
                .shouldHave(text(nameCategory));
    }

    public void checkNotCategoryByNameInProfile(String nameCategory) {
        categoryNames.parent().parent()
                .shouldNotHave(text(nameCategory));
    }

    public ProfilePage clickArchiveCategory(String name) {
        searchCategory.filter(text(name)).first().$("button[aria-label='Archive category']")
                .click();

        return new ProfilePage();
    }

    public ProfilePage clickUnarchiveCategory(String name) {
        searchCategory.filter(text(name)).first().$("[data-testid='UnarchiveOutlinedIcon']")
                .click();

        return new ProfilePage();
    }
}
