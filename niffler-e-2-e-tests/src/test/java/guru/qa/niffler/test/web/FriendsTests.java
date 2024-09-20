package guru.qa.niffler.test.web;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.jupiter.extension.UsersQueueExtension;
import guru.qa.niffler.jupiter.extension.UsersQueueExtension.StaticUser;
import guru.qa.niffler.jupiter.extension.UsersQueueExtension.UserType;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.MainPage;
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
                .checkWhatUserDontHaveFriends();
    }

    @ExtendWith(UsersQueueExtension.class)
    @Test
    void checkUserWithFriends(@UserType(withFriends) StaticUser user) {
        open(CFG.frontDockerUrl(), LoginPage.class)
        .login(user.username(), user.password());
        mainPage.goToFriendsUser()
                .checkWhatUserHaveFriends()
                .checkWhatUserHaveSpecificFriends(user.friends());
    }

    @ExtendWith(UsersQueueExtension.class)
    @Test
    void checkUserWithIncomeRequestFriend(@UserType(withIncomeFriendRequest) StaticUser user) {
        open(CFG.frontDockerUrl(), LoginPage.class)
                .login(user.username(), user.password());
        mainPage.goToFriendsUser()
                .checkWhatUserHaveIncomeRequestForFriendship().
                checkWhatUserHaveIncomeRequestForFriendshipFromSpecificUser(user.income());
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
}
