package guru.qa.niffler.jupiter.extension;

import io.qameta.allure.Allure;
import org.apache.commons.lang3.time.StopWatch;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

public class UsersQueueExtension implements
        BeforeEachCallback, AfterEachCallback, ParameterResolver {

    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(UsersQueueExtension.class);

    public record StaticUser(String username,
                             String password,
                             String friends,
                             String income,
                             String outcome) {
    }

    private static final Queue<StaticUser> emptyUsers = new ConcurrentLinkedQueue<>();
    private static final Queue<StaticUser> withFriendsUsers = new ConcurrentLinkedQueue<>();
    private static final Queue<StaticUser> withIncomeFriendUsers = new ConcurrentLinkedQueue<>();
    private static final Queue<StaticUser> withOutcomeFriendUsers = new ConcurrentLinkedQueue<>();

    static {
        emptyUsers.add(new StaticUser(
                "testEmptyUser",
                "testPassword",
                null,
                null,
                null
        ));
        withFriendsUsers.add(new StaticUser(
                "testUserWithFriends",
                "testPassword",
                "Dramasha",
                null,
                null
        ));
        withIncomeFriendUsers.add(new StaticUser(
                "testUserWithIncomeFriend",
                "testPassword",
                null,
                "testUserWithOutcomeFriend",
                null));
        withOutcomeFriendUsers.add(new StaticUser(
                "testUserWithOutcomeFriend",
                "testPassword",
                "testOutcome",
                null,
                "testUserWithIncomeFriend"
        ));
    }

    @Target(ElementType.PARAMETER)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface UserType {
        Type value() default Type.empty;

        enum Type {
            empty,
            withFriends,
            withIncomeFriendRequest,
            withOutcomeFriendRequest
        }
    }

    private Queue<StaticUser> getQueueType(UserType.Type type) {
        return switch (type) {
            case empty -> emptyUsers;
            case withFriends -> withFriendsUsers;
            case withIncomeFriendRequest -> withIncomeFriendUsers;
            case withOutcomeFriendRequest -> withOutcomeFriendUsers;
            default -> throw new IllegalArgumentException("Unknown type: " + type);
        };
    }


    @Override
    public void beforeEach(ExtensionContext context) {
        Arrays.stream(context.getRequiredTestMethod().getParameters())
                .filter(p -> AnnotationSupport.isAnnotated(p, UserType.class))
                .forEach(p -> {
                            UserType ut = p.getAnnotation(UserType.class);
                            Queue<StaticUser> queue = getQueueType(ut.value());
                            Optional<StaticUser> user = Optional.empty();
                            StopWatch stopWatch = StopWatch.createStarted();
                            while (user.isEmpty() && stopWatch.getTime(TimeUnit.SECONDS) < 30) {
                                user = Optional.ofNullable(queue.poll());
                            }
                            Allure.getLifecycle().updateTestCase(
                                    testCase -> testCase.setStart(new Date().getTime())
                            );
                            user.ifPresentOrElse(
                                    u -> {
                                        Map<UserType, StaticUser> map = getUserMap(context);
                                        map.put(ut, u);

                                    },
                                    () -> {
                                        throw new IllegalStateException("Can`t find user after 30 sec");
                                    }
                            );
                        }
                );
    }

    @Override
    public void afterEach(ExtensionContext context) {
        Map<UserType, StaticUser> map = context.getStore(NAMESPACE).get(
                context.getUniqueId(),
                Map.class
        );
        for (Map.Entry<UserType, StaticUser> entry : map.entrySet()) {
            UserType userType = entry.getKey();
            StaticUser staticUser = entry.getValue();
            Queue<StaticUser> queue = getQueueType(userType.value());
            queue.add(staticUser);
        }
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(StaticUser.class)
                && AnnotationSupport.isAnnotated(parameterContext.getParameter(), UserType.class);
    }

    @Override
    public StaticUser resolveParameter(ParameterContext parameterContext, ExtensionContext context) throws ParameterResolutionException {
        Map<UserType, StaticUser> map = getUserMap(context);
        UserType userType = parameterContext.findAnnotation(UserType.class).orElseThrow(
                () -> new ParameterResolutionException("Annotation not found")
        );


        StaticUser staticUser = map.get(userType);
        if (staticUser == null) {
            throw new IllegalStateException("Can`t find user after 30 sec");
        }
        return staticUser;
    }

    public Map<UserType, StaticUser> getUserMap(ExtensionContext context) {
        return (Map<UserType, StaticUser>) context.getStore(NAMESPACE)
                .getOrComputeIfAbsent(
                        context.getUniqueId(),
                        key -> new HashMap<>());
    }

}
