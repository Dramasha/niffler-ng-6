package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.CollectionCondition.sizeGreaterThan;
import static com.codeborne.selenide.Condition.*;
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

    public void checkWhatUserHaveFriends() {
        friendsArea.findAll("tr").shouldHave(sizeGreaterThan(0));
    }

    public void checkWhatUserHaveIncomeRequestForFriendship() {
        request.shouldHave(text("Accept"));
    }

    public FriendsPage clickToAllPeople() {
        friendsAndAllPeopleBar.$(byText("All people")).click();
        return new FriendsPage();
    }

    public void checkWhatUserHaveRequestForFriendship() {
        allArea.findAll("tr").find(text("Waiting...")).shouldBe(visible);
    }

}