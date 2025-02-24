package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class LoginPage {
    private final SelenideElement
            usernameInput = $("input[name='username']"),
            passwordInput = $("input[name='password']"),
            submitButton = $("button[type='submit']"),
            clickCreateNewAccountButton = $(".form__register"),
            error = $(".form__error");

    @Step("Авторизация")
    public void login(String username, String password) {
        usernameInput.setValue(username);
        passwordInput.setValue(password);
        submitButton.click();
    }

    @Step("Перейти к созданию нового аккаунта")
    public RegisterPage clickToRegisterPage() {
        clickCreateNewAccountButton.shouldBe(visible).click();
        return new RegisterPage();
    }

    @Step("Проверка получения ошибки при вводе невалидных данных")
    public void checkErrorBadCredentials() {
        error.shouldBe(visible).shouldHave(text("Bad credentials"));
    }
}
