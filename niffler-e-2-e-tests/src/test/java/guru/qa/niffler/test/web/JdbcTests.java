package guru.qa.niffler.test.web;

import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.service.UsersDbClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static guru.qa.niffler.utils.RandomDataUtils.getRandomUsername;

public class JdbcTests {


//    @Test
//    void createUserWithRepo() {
//        UsersDbClient usersDbClient = new UsersDbClient();
//        UserJson userMyself = usersDbClient.generateUser("myself1");
//        UserJson userFriend = usersDbClient.generateUser("friend1");
//        UserJson userIncome = usersDbClient.generateUser("income1");
//        UserJson userOutcome = usersDbClient.generateUser("outcome1");
//
//
//        usersDbClient.addIncomeInvitation(userIncome, userMyself);
//        usersDbClient.addOutcomeInvitation(userMyself, userOutcome);
//        usersDbClient.addFriend(userMyself, userFriend);
//
//    }

    @ValueSource(strings = {
            "asfadfa",
            "asfadfa123",
            "asfadfa321"
    })
    @ParameterizedTest
    void springJdbcTest(String testName) {
        UsersDbClient usersDbClient = new UsersDbClient();
        UserJson user = usersDbClient.createUserSpringJdbc(
                testName,
                "12345"
        );
        System.out.println(user);
    }

//    @Test
//    void springJdbcWithoutTransactionTest() {
//        UsersDbClient usersDbClient = new UsersDbClient();
//        UserJson user = usersDbClient.createUserWithoutSpringJdbcTransaction(
//                new UserJson(
//                        null,
//                        getRandomUsername(),
//                        null,
//                        null,
//                        null,
//                        CurrencyValues.RUB,
//                        null,
//                        null,
//                        null
//                )
//        );
//        System.out.println(user);
//    }
//
//    @Test
//    void jdbcTest() {
//        UsersDbClient usersDbClient = new UsersDbClient();
//        UserJson user = usersDbClient.createUserJdbcTransaction(
//                new UserJson(
//                        null,
//                        "userForTestForJdbc",
//                        null,
//                        null,
//                        null,
//                        CurrencyValues.RUB,
//                        null,
//                        null,
//                        null
//                )
//        );
//        System.out.println(user);
//    }
//
//    @Test
//    void jdbcWithoutTransactionTest() {
//        UsersDbClient usersDbClient = new UsersDbClient();
//        UserJson user = usersDbClient.createUserWithoutJdbcTransaction(
//                new UserJson(
//                        null,
//                        getRandomUsername(),
//                        null,
//                        null,
//                        null,
//                        CurrencyValues.RUB,
//                        null,
//                        null,
//                        null
//                )
//        );
//        System.out.println(user);
//    }

}
