package guru.qa.niffler.test.web;

import guru.qa.niffler.api.impl.UserApiClient;
import guru.qa.niffler.jupiter.extension.UsersClientExtension;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.service.UsersClient;
import guru.qa.niffler.service.impl.SpendDbClient;
import guru.qa.niffler.service.impl.UsersDbClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.IOException;

@ExtendWith(UsersClientExtension.class)
public class JdbcTests {

    private UsersClient usersClient;

    UsersDbClient usersDbClient = new UsersDbClient();


    @ValueSource(strings = {
            "fhgdfhdffdh",
            "dfhfdf",
            "eryreyeryreyre"
    })
    @ParameterizedTest
    void springJdbcTest(String testName) throws IOException {

        UserJson user = usersClient.registerUser(
                testName,
                "12345"
        );
        System.out.println(user);
    }

    @Test
    void springJdbcWithoutTransactionTest() throws IOException {

        UserJson user = usersClient.registerUser(
                "asdasda",
                "12345"
        );
        System.out.println(user);
    }

    @Test
    void jdbcTest() throws IOException {

        UserJson user = usersClient.registerUser(
                "asdasda",
                "12345"
        );
        System.out.println(user);
    }

    @Test
    void jdbcWithoutTransactionTest() throws IOException {

        UserJson user = usersClient.registerUser(
                "asdasda",
                "12345"
        );
        System.out.println(user);
    }
}
