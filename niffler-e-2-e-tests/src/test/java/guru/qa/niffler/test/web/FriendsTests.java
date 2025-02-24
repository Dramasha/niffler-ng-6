package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.jupiter.extension.UsersQueueExtension;
import guru.qa.niffler.jupiter.extension.UsersQueueExtension.StaticUser;
import guru.qa.niffler.jupiter.extension.UsersQueueExtension.UserType;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.page.FriendsPage;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.MainPage;
import guru.qa.niffler.service.impl.UsersDbClient;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static com.codeborne.selenide.Selenide.open;
import static guru.qa.niffler.jupiter.extension.UsersQueueExtension.UserType.Type.*;
import static guru.qa.niffler.utils.RandomDataUtils.getRandomName;
import static guru.qa.niffler.utils.RandomDataUtils.getRandomPassword;

@ExtendWith(BrowserExtension.class)
public class FriendsTests {

    private final UsersDbClient usersDbClient = new UsersDbClient();
    private final Config CFG = Config.getInstance();
    private final MainPage mainPage = new MainPage();

    private final String username = getRandomName();
    private final String password = getRandomPassword(3,12);

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

    @Test
    @DisplayName("Прием заявки в друзья")
    void acceptInvitationToFriendsTest() {
        UserJson mainUser = usersDbClient.registerUser(username, password);
        usersDbClient.sendInvitation("Dramasha", mainUser.username());

        Selenide.open(CFG.frontDockerUrl(), LoginPage.class)
                .login(username, password);

        new FriendsPage()
                .getHeader()
                .toFriendsPage()
                .acceptFriend()
                .unfriendBtnIsVisibleCheck();
    }

    @Test
    @DisplayName("Отклонение заявки в друзья")
    void declineInvitationToFriendsTest() {
        UserJson mainUser = usersDbClient.registerUser(username, password);
        usersDbClient.sendInvitation("Dramasha", mainUser.username());

        Selenide.open(CFG.frontDockerUrl(), LoginPage.class)
                .login(username, password);

        new FriendsPage()
                .getHeader()
                .toFriendsPage()
                .declineFriend()
                .checkWhatUserDontHaveFriends();
    }
}
