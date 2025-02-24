package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

public class RegisterPage {
    private final SelenideElement
            usernameInput = $("#username"),
            passwordInput = $("#password"),
            passwordSubmitInput = $("#passwordSubmit"),
            signUpButton = $(".form__submit"),
            registeredSuccessful = $(".form__paragraph_success"),
            registeredUnsuccessful = $(".form__error"),
            signInButton = $(".form_sign-in");

    @Step("Регистрация нового пользователя с Логин {username], Пароль {password}")
    public RegisterPage registeredUser(String username, String password) {
        usernameInput.setValue(username);
        passwordInput.setValue(password);
        passwordSubmitInput.setValue(password);
        signUpButton.click();

        return new RegisterPage();
    }

    @Step("Проверка успешной регистрации пользователя")
    public LoginPage checkSuccessfulCreateUserAndReturnToLogin() {
        registeredSuccessful.shouldHave(text("Congratulations! You've registered!"));
        signInButton.click();

        return new LoginPage();
    }

    @Step("Проверка неудачной регистрации пользователя")
    public void checkUnsuccessfulCreateUser(String Username) {
        registeredUnsuccessful.shouldHave(text("Username `" + Username + "` already exists"));
    }

    @Step("Проверка сообщения о длине пароля от 3 до 12 знаков, при регистрации пользователя")
    public void checkLengthPasswordError() {
        registeredUnsuccessful.shouldHave(text("Allowed password length should be from 3 to 12 characters"));
    }
}
