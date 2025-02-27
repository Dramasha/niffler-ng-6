package guru.qa.niffler.test.web;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.Spending;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.MainPage;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selenide.open;
import static guru.qa.niffler.utils.RandomDataUtils.getRandomPassword;

public class LoginWebTests {
    private final Config CFG = Config.getInstance();
    private final LoginPage loginPage = new LoginPage();
    private final MainPage mainPage = new MainPage();
    private final String invalidPassword = getRandomPassword(3, 11);

    @User(
            categories = {
                    @Category(title = "cat1"),
                    @Category(title = "cat2", archived = true)
            },
            spendings = {
                    @Spending(
                            category = "cat3",
                            description = "test",
                            amount = 100
                    )
            }
    )

    @Test
    void loginTest(UserJson user) {
        open(CFG.frontDockerUrl(), LoginPage.class)
                .login(user.username(), user.testData().password());
        mainPage.checkThatPageLoaded();
    }


    @Test
    void checkLoginWithInvalidPasswordUser() {
        String username = "Dramasha";
        open(CFG.frontDockerUrl(), LoginPage.class)
                .login(username, invalidPassword);
        loginPage.checkThatPageLoaded()
                .checkErrorBadCredentials();
    }

}
