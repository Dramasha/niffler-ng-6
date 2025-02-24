package guru.qa.niffler.test.web;

import guru.qa.niffler.api.impl.UserApiClient;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.service.UsersClient;
import guru.qa.niffler.service.impl.SpendDbClient;
import guru.qa.niffler.service.impl.UsersDbClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.IOException;


public class JdbcTests {

    private final SpendDbClient spendDbClient = new SpendDbClient();

    @ValueSource(strings = {
            "asfadfпрапрa",
            "asfапраadfa123",
            "asfaапраdfa321"
    })
    @ParameterizedTest
    void springJdbcTest(String testName) {
        UsersDbClient usersDbClient = new UsersDbClient();
        UserJson user = usersDbClient.registerUser(
                testName,
                "12345"
        );
        System.out.println(user);
    }

    @Test
    void springJdbcWithoutTransactionTest() {
        UsersDbClient usersDbClient = new UsersDbClient();
        UserJson user = usersDbClient.registerUser(
                "asdasda",
                "12345"
        );
        System.out.println(user);
    }

    @Test
    void jdbcTest() {
        UsersDbClient usersDbClient = new UsersDbClient();
        UserJson user = usersDbClient.registerUser(
                "asdasda",
                "12345"
        );
        System.out.println(user);
    }

    @Test
    void jdbcWithoutTransactionTest() {
        UsersDbClient usersDbClient = new UsersDbClient();
        UserJson user = usersDbClient.registerUser(
                "asdasda",
                "12345"
        );
        System.out.println(user);
    }

    @Test
    void apiTest() throws IOException {
        UsersClient userApi = new UserApiClient();
        UserJson user = userApi.registerUser("TESTFORTEST767t6", "12345");
    }
}
