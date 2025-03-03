package guru.qa.niffler.page.component;

import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.*;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

public class Header extends BaseComponent<Header> {

    public Header() {
        super($("#root header"));
    }

    private final SelenideElement
//            header = $("#root header"),
            menu = $("[role='menu']");

    @Step("Перейти к странице друзей")
    public FriendsPage toFriendsPage() {
        self.$("[aria-label='Menu']").click();
        menu.$(byText("Friends")).click();
        return new FriendsPage();
    }

    @Step("Перейти к странице всех людей")
    public PeoplePage toAllPeoplesPage() {
        self.$("[aria-label='Menu']").click();
        menu.$(byText("All people")).click();
        return new PeoplePage();
    }

    @Step("Перейти к странице профиля")
    public ProfilePage toProfilePage() {
        self.$("[aria-label='Menu']").click();
        menu.$(byText("Profile")).click();
        return new ProfilePage();
    }

    @Step("Разлогиниться")
    public LoginPage signOut() {
        self.$("[aria-label='Menu']").click();
        menu.$(byText("Sign out")).click();
        return new LoginPage();
    }

    @Step("Добавить новую трату")
    public EditSpendingPage addSpendingPage() {
        self.$(byText("New spending")).click();
        return new EditSpendingPage();
    }

    @Step("Вернуться на главную страницу")
    public MainPage toMainPage() {
        self.$(".MuiToolbar-gutters").click();
        return new MainPage();
    }
}
