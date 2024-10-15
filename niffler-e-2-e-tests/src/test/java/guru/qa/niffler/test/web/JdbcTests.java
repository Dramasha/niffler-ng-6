package guru.qa.niffler.test.web;

import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.service.UsersDbClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class JdbcTests {

    @ValueSource(strings = {
            "asfadfпрапрa",
            "asfапраadfa123",
            "asfaапраdfa321"
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

    @Test
    void springJdbcWithoutTransactionTest() {
        UsersDbClient usersDbClient = new UsersDbClient();
        UserJson user = usersDbClient.createUserSpringJdbc(
                "asdasda",
                "12345"
        );
        System.out.println(user);
    }

    @Test
    void jdbcTest() {
        UsersDbClient usersDbClient = new UsersDbClient();
        UserJson user = usersDbClient.createUserSpringJdbc(
                "asdasda",
                "12345"
        );
        System.out.println(user);
    }

    @Test
    void jdbcWithoutTransactionTest() {
        UsersDbClient usersDbClient = new UsersDbClient();
        UserJson user = usersDbClient.createUserSpringJdbc(
                "asdasda",
                "12345"
        );
        System.out.println(user);
    }

}
