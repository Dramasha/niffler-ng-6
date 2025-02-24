package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.Spending;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.page.EditSpendingPage;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.MainPage;
import guru.qa.niffler.service.impl.UsersDbClient;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selenide.*;
import static guru.qa.niffler.utils.RandomDataUtils.getRandomPassword;
import static guru.qa.niffler.utils.RandomDataUtils.getRandomUsername;

public class SpendingWebTest {

    private final Config CFG = Config.getInstance();
    private final MainPage mainPage = new MainPage();

    @User(
            username = "Dramasha",
            spendings = @Spending(
                    category = "Обучение",
                    description = "Обучение Advanced 2.0",
                    amount = 79990
            )
    )

    @Test
    void categoryDescriptionShouldBeChangedFromTable(SpendJson spend) {
        String newDescription = "ловушка Докера";

        open(CFG.frontDockerUrl(), LoginPage.class)
                .login("Dramasha", "123");
        mainPage.editSpending(spend.description())
                .setNewSpendingDescription(newDescription)
                .save();
        new MainPage().checkThatTableContainsSpending(newDescription);
    }

    @Test
    @DisplayName("Создание траты")
    void addSpendTest() {
        UsersDbClient usersDbClient = new UsersDbClient();
        String username = getRandomUsername();
        String password = getRandomPassword(3,12);
        usersDbClient.registerUser(username, password);

        Selenide.open(CFG.frontDockerUrl(), LoginPage.class)
                .login(username, password);

        new EditSpendingPage()
                .getHeader()
                .addSpendingPage()
                .setSpendingCategory("testForTest")
                .setNewSpendingDescription("forTest")
                .setSpendingAmount("2000")
                .getCalendar()
                .selectDateInCalendar("8.June.1996");

        new EditSpendingPage().save();

        new MainPage().checkThatTableContainsSpending("test");
    }
}
