package guru.qa.niffler.test.web;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.MainPage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static com.codeborne.selenide.Selenide.*;
import static guru.qa.niffler.utils.RandomDataUtils.*;
import static org.junit.jupiter.params.provider.Arguments.of;

public class RegisterWebTests {
    private static final Config CFG = Config.getInstance();
    private static final MainPage mainPage = new MainPage();

    private final String userName = getRandomUsername();
    private final String password = getRandomPassword(3, 11);

    @Test
    void checkCreateUser() {
        open(CFG.frontUrl(), LoginPage.class)
                .clickToRegisterPage()
                .registeredUser(userName, password)
                .checkSuccessfulCreateUserAndReturnToLogin()
                .login(userName, password);
        mainPage.checkIsLoaded();
    }

    @Test
    void checkNotCreateUserWithSimilarUsername() {
        open(CFG.frontUrl(), LoginPage.class)
                .clickToRegisterPage()
                .registeredUser(userName, password)
                .checkSuccessfulCreateUserAndReturnToLogin()
                .clickToRegisterPage()
                .registeredUser(userName, password)
                .checkUnsuccessfulCreateUser(userName);
    }

    private static Stream<Arguments> passwordData() {
        return Stream.of(
                of(getRandomPassword(1, 2)),
                of(getRandomPassword(12, 99))
        );
    }

    @ParameterizedTest
    @MethodSource("passwordData")
    void checkNotCreateUserWithWeakOrLongPassword(String password) {
        open(CFG.frontUrl(), LoginPage.class)
                .clickToRegisterPage()
                .registeredUser(userName, password)
                .checkLengthPasswordError();
    }
}
