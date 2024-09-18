package guru.qa.niffler.test.web;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selenide.open;
import static guru.qa.niffler.utils.RandomDataUtils.getRandomPassword;

public class LoginWebTests {
    private static final Config CFG = Config.getInstance();
    private static final LoginPage loginPage = new LoginPage();
    private static final String username = "Dramasha";
    private final String invalidPassword = getRandomPassword(3,11);

    @Test
    void checkCreateUser() {
        open(CFG.frontUrl(), LoginPage.class)
                .login(username, invalidPassword);
        loginPage.checkErrorBadCredentials();
    }

}
