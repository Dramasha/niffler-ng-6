package guru.qa.niffler.test.web;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.MainPage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static com.codeborne.selenide.Selenide.open;

@ExtendWith(BrowserExtension.class)
public class ProfileTests {
    private static final Config CFG = Config.getInstance();
    private static final MainPage mainPage = new MainPage();

    @User(
            username = "Dramasha",
            categories = @Category(
                    archived = false
            )
    )
    @Test
    void activeCategoryShouldPresentInCategoriesList(CategoryJson categoryJson) {
        open(CFG.frontUrl(), LoginPage.class)
                .login("Dramasha", "123");
        mainPage.checkIsLoaded();
        mainPage.goToProfile()
                .clickArchiveCategory(categoryJson.name())
                .clickArchiveOrUnarchiveCategory("Archive")
                .checkNotCategoryByNameInProfile(categoryJson.name());
    }

    @User(
            username = "Dramasha",
            categories = @Category(
                    archived = true
            )
    )
    @Test
    void archiveCategoryShouldPresentInCategoriesList(CategoryJson categoryJson) {
        open(CFG.frontUrl(), LoginPage.class)
                .login("Dramasha", "123");
        mainPage.checkIsLoaded();
        mainPage.goToProfile()
                .clickOnCheckboxShowArchived()
                .clickUnarchiveCategory(categoryJson.name())
                .clickArchiveOrUnarchiveCategory("Unarchive")
                .clickOnCheckboxShowArchived()
                .checkCategoryByNameInProfile(categoryJson.name());
    }
}
