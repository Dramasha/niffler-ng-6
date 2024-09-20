package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.CollectionCondition.sizeGreaterThan;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

public class FriendsPage {

    private final SelenideElement
            friendsAndAllPeopleBar = $("[role='navigation']"),
            searchInput = $("[placeholder='Search']"),
            searchButton = $("#input-submit"),
            friendTable = $("#simple-tabpanel-friends"),
            friendsArea = $("#friends"),
            allArea = $("#all"),
            request = $("#requests");


    public void checkWhatUserDontHaveFriends() {
        friendTable.shouldHave(text("There are no users yet"));
    }

    public FriendsPage checkWhatUserHaveFriends() {
        friendsArea.findAll("tr").shouldHave(sizeGreaterThan(0));

        return new FriendsPage();
    }

    public void checkWhatUserHaveSpecificFriends(String usernameFriend) {
        friendsArea.shouldHave(text(usernameFriend));
    }

    public FriendsPage checkWhatUserHaveIncomeRequestForFriendship() {
        request.shouldHave(text("Accept"));

        return new FriendsPage();
    }

    public void checkWhatUserHaveIncomeRequestForFriendshipFromSpecificUser(String username) {
        request.shouldHave(text(username));
    }

    public FriendsPage clickToAllPeople() {
        friendsAndAllPeopleBar.$(byText("All people")).click();
        return new FriendsPage();
    }

    public FriendsPage checkWhatUserHaveRequestForFriendship() {
        allArea.findAll("tr").find(text("Waiting...")).shouldBe(visible);

        return new FriendsPage();
    }

    public void checkWhatUserHaveRequestForFriendshipToSpecificUser(String username) {
        allArea.findAll("tr").filter(text(username)).first().shouldHave(text("Waiting..."));
    }

}