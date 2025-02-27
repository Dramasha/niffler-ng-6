package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.CollectionCondition.sizeGreaterThan;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

public class FriendsPage extends BasePage {

    private final SelenideElement
            friendsAndAllPeopleBar = $("[role='navigation']"),
            searchInput = $("[placeholder='Search']"),
            clickSearch = $("[data-testid='SearchIcon']"),
            friendTable = $("#simple-tabpanel-friends"),
            friendsArea = $("#friends"),
            allArea = $("#all"),
            request = $("#requests"),
            accept = $(byText("Accept")).as("кнопка 'Accept'"),
            unfriend = $(byText("Unfriend")).as("кнопка 'Unfriend'"),
            decline = $(byText("Decline")).as("кнопка 'Decline'"),
            declineBtnInActionMenu = $(".MuiDialogActions-spacing [type='button']:nth-child(2)").as("кнопка 'Decline' в диалоговом окне");


    @Step("Проверка, что у пользователя нет друзей")
    public void checkWhatUserDontHaveFriends() {
        friendTable.shouldHave(text("There are no users yet"));
    }

    @Step("Поиск Друга по имени {usernameFriend}")
    public void searchManByUsername(String usernameFriend) {
        searchInput.setValue(usernameFriend);
        searchInput.shouldHave(text(usernameFriend));
        clickSearch.click();
    }

    @Step("Проверка присутствия записи Друга по имени {username}")
    public void checkUsernameAfterSearch(String username) {
        allArea.shouldHave(text(username));
    }

    @Step("Проверка, что у пользователя есть список Друзей")
    public FriendsPage checkWhatUserHaveFriends() {
        friendsArea.findAll("tr").shouldHave(sizeGreaterThan(0));

        return new FriendsPage();
    }

    @Step("Проверка, что у пользователя есть конкретный Друг {usernameFriend}")
    public void checkWhatUserHaveSpecificFriends(String usernameFriend) {
        friendsArea.shouldHave(text(usernameFriend));
    }

    @Step("Проверка, что у пользователя есть входящий запрос в друзья")
    public FriendsPage checkWhatUserHaveIncomeRequestForFriendship() {
        request.shouldHave(text("Accept"));

        return new FriendsPage();
    }

    @Step("Проверка, что у пользователя есть исходящий запрос к {username}")
    public void checkWhatUserHaveIncomeRequestForFriendshipFromSpecificUser(String username) {
        request.shouldHave(text(username));
    }

    @Step("Клик на кнопку Все люди")
    public FriendsPage clickToAllPeople() {
        friendsAndAllPeopleBar.$(byText("All people")).click();
        return new FriendsPage();
    }

    @Step("Проверка, что у пользователя есть исходящий запрос в друзья")
    public FriendsPage checkWhatUserHaveRequestForFriendship() {
        allArea.findAll("tr").find(text("Waiting...")).shouldBe(visible);

        return new FriendsPage();
    }

    public void checkWhatUserHaveRequestForFriendshipToSpecificUser(String username) {
        allArea.findAll("tr").filter(text(username)).first().shouldHave(text("Waiting..."));
    }

    @Step("Принять заявку в друзья")
    public FriendsPage acceptFriend() {
        accept.click();
        return this;
    }

    @Step("Кнопка 'Unfriend' отображается")
    public FriendsPage unfriendBtnIsVisibleCheck() {
        unfriend.shouldBe(visible);
        return this;
    }


    @Step("Отклонить заявку в друзья")
    public FriendsPage declineFriend() {
        decline.click();
        declineBtnInActionMenu.click();
        return this;
    }

}