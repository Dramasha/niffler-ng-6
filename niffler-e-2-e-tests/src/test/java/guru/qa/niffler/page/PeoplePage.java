package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import org.openqa.selenium.Keys;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

public class PeoplePage {

    private final SelenideElement peopleTable = $("#all").as("список людей и отправленных заявок в друзбя");
    private final SelenideElement searchInput =  $("input[type='text']").as("инпут поиска");

    @Step("Проверка того, что у пользователя есть отправленная заявка в друзья")
    public PeoplePage invitationSentToUserCheck(String username) {
        makeFriendSearch(username);
        SelenideElement friendRow = peopleTable.$$("tr").find(text(username));
        friendRow.shouldHave(text("Waiting..."));
        return this;
    }

    @Step("Осуществить поиск друга")
    private FriendsPage makeFriendSearch(String username) {
        searchInput.setValue(username);
        searchInput.sendKeys(Keys.ENTER);
        return new FriendsPage();
    }
}
