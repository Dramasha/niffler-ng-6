package guru.qa.niffler.test.web;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.Spending;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.MainPage;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selenide.*;

public class SpendingWebTest {

    private static final Config CFG = Config.getInstance();
    private static final MainPage mainPage = new MainPage();

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
}
