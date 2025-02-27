package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.jupiter.extension.UserExtension;
import guru.qa.niffler.jupiter.extension.UsersQueueExtension;
import guru.qa.niffler.jupiter.extension.UsersQueueExtension.StaticUser;
import guru.qa.niffler.jupiter.extension.UsersQueueExtension.UserType;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.page.FriendsPage;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.MainPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static com.codeborne.selenide.Selenide.open;
import static guru.qa.niffler.jupiter.extension.UsersQueueExtension.UserType.Type.*;

@ExtendWith(BrowserExtension.class)
public class FriendsTests {

    private final Config CFG = Config.getInstance();
    private final MainPage mainPage = new MainPage();

    @ExtendWith(UsersQueueExtension.class)
    @Test
    void checkEmptyUser(@UserType(empty) StaticUser user) {
        open(CFG.frontDockerUrl(), LoginPage.class)
                .login(user.username(), user.password());
        mainPage.goToFriendsUser()
                .checkThatPageLoaded()
                .checkWhatUserDontHaveFriends();
    }

    @ExtendWith(UsersQueueExtension.class)
    @Test
    void checkUserWithFriends(@UserType(withFriends) StaticUser user) {
        open(CFG.frontDockerUrl(), LoginPage.class)
                .login(user.username(), user.password());
        mainPage.goToFriendsUser()
                .checkThatPageLoaded()
                .checkWhatUserHaveFriends()
                .checkWhatUserHaveSpecificFriends(user.friends());
    }

    @ExtendWith(UsersQueueExtension.class)
    @Test
    void checkUserWithIncomeRequestFriend(@UserType(withIncomeFriendRequest) StaticUser user) {
        open(CFG.frontDockerUrl(), LoginPage.class)
                .login(user.username(), user.password());
        mainPage.goToFriendsUser()
                .checkThatPageLoaded()
                .checkWhatUserHaveIncomeRequestForFriendship()
                .checkWhatUserHaveIncomeRequestForFriendshipFromSpecificUser(user.income());
    }

    @ExtendWith(UsersQueueExtension.class)
    @Test
    void checkUserWithOutcomeRequestFriend(@UserType(withOutcomeFriendRequest) StaticUser user) {
        open(CFG.frontDockerUrl(), LoginPage.class)
                .login(user.username(), user.password());
        mainPage.goToFriendsUser()
                .clickToAllPeople()
                .checkWhatUserHaveRequestForFriendship()
                .checkWhatUserHaveRequestForFriendshipToSpecificUser(user.outcome());
    }

    @ExtendWith(UserExtension.class)
    @User(incomeInvitations = 1)
    @Test
    @DisplayName("Прием заявки в друзья")
    void acceptInvitationToFriendsTest(UserJson user) {

        Selenide.open(CFG.frontDockerUrl(), LoginPage.class)
                .login(user.username(), user.testData().password());

        new FriendsPage()
                .getHeader()
                .toFriendsPage()
                .checkThatPageLoaded()
                .acceptFriend()
                .unfriendBtnIsVisibleCheck();
    }

    @ExtendWith(UserExtension.class)
    @User(outcomeInvitations = 1)
    @Test
    @DisplayName("Отклонение заявки в друзья")
    void declineInvitationToFriendsTest(UserJson user) {
        Selenide.open(CFG.frontDockerUrl(), LoginPage.class)
                .login(user.username(), user.testData().password());

        new FriendsPage()
                .getHeader()
                .toFriendsPage()
                .declineFriend()
                .checkWhatUserDontHaveFriends();
    }
}
