package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.extension.UserExtension;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.MainPage;
import guru.qa.niffler.page.component.Header;
import guru.qa.niffler.service.impl.UsersDbClient;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static com.codeborne.selenide.Selenide.*;
import static guru.qa.niffler.utils.RandomDataUtils.getRandomPassword;
import static guru.qa.niffler.utils.RandomDataUtils.getRandomUsername;

@ExtendWith(BrowserExtension.class)
public class ProfileTests {
    private final Config CFG = Config.getInstance();
    private final MainPage mainPage = new MainPage();

    @ExtendWith(UserExtension.class)
    @User(categories = @Category())
    @Test
    void activeCategoryShouldPresentInCategoriesList(UserJson user) {
        open(CFG.frontDockerUrl(), LoginPage.class)
                .login(user.username(), user.testData().password());
        mainPage.checkIsLoaded();
        mainPage.goToProfile()
                .clickArchiveCategory(user.testData().categories().getFirst().name())
                .clickArchiveOrUnarchiveCategory("Archive")
                .checkNotCategoryByNameInProfile(user.testData().categories().getFirst().name());
    }


    @User(categories = {
            @Category(title = "cat1",archived = true)
    })
    @Test
    void archiveCategoryShouldPresentInCategoriesList(UserJson user) {
        open(CFG.frontDockerUrl(), LoginPage.class)
                .login(user.username(), user.testData().password());
        mainPage.goToProfile()
                .clickOnCheckboxShowArchived()
                .clickUnarchiveCategory(user.testData().categories().getFirst().name())
                .clickArchiveOrUnarchiveCategory("Unarchive")
                .clickOnCheckboxShowArchived()
                .checkCategoryByNameInProfile(user.testData().categories().getFirst().name());
    }

    @Test
    @DisplayName("Проверка сохранения имени пользователя")
    void setNameTest() {
        UsersDbClient usersDbClient = new UsersDbClient();
        String username = getRandomUsername();
        String password = getRandomPassword(3,12);
        String newName = getRandomUsername();

        usersDbClient.registerUser(username, password);

        Selenide.open(CFG.frontDockerUrl(), LoginPage.class)
                .login(username, password);

        new Header().toProfilePage()
                .setName(newName)
                .checkName(newName);
    }
}
