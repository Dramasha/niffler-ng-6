package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.api.impl.UserApiClient;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.*;
import guru.qa.niffler.service.UsersClient;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;

import java.io.IOException;
import java.util.ArrayList;

import static guru.qa.niffler.utils.RandomDataUtils.getRandomUsername;

public class UserExtension implements BeforeEachCallback, ParameterResolver {

    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(UserExtension.class);
    private static final String defaultPassword = "12345";

    private final UsersClient usersClient = new UserApiClient();

    @Override
    public void beforeEach(ExtensionContext context) {
        AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), User.class)
                .ifPresent(user -> {
                            if ("".equals(user.username())) {
                                final String username = getRandomUsername();
                                UserJson testUser;
                                try {
                                    testUser = usersClient.registerUser(username, defaultPassword);
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                                context.getStore(NAMESPACE).put(
                                        context.getUniqueId(),
                                        testUser.addTestData(
                                                new TestData(defaultPassword,
                                                        new ArrayList<>(),
                                                        new ArrayList<>()
                                                )
                                        )
                                );
                            }
                        }
                );
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws
            ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(UserJson.class);
    }

    @Override
    public UserJson resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws
            ParameterResolutionException {
        return extensionContext.getStore(UserExtension.NAMESPACE).get(extensionContext.getUniqueId(), UserJson.class);
    }
}
