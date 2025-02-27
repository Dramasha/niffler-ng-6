package guru.qa.niffler.service.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.data.entity.auth.Authority;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.data.repository.AuthUserRepository;
import guru.qa.niffler.data.repository.UserdataUserRepository;
import guru.qa.niffler.data.repository.impl.AuthUserRepositoryHibernate;
import guru.qa.niffler.data.repository.impl.UserdataUserRepositoryHibernate;
import guru.qa.niffler.data.tpl.XaTransactionTemplate;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.service.UsersClient;
import io.qameta.allure.Step;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.Arrays;

public class UsersDbClient implements UsersClient {

    private static final Config CFG = Config.getInstance();
    private static final PasswordEncoder pe = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    private final AuthUserRepository authUserRepository = new AuthUserRepositoryHibernate();
    private final UserdataUserRepository userdataUserRepository = new UserdataUserRepositoryHibernate();

    private final XaTransactionTemplate xaTransactionTemplate = new XaTransactionTemplate(
            CFG.authJdbcUrl(),
            CFG.userdataJdbcUrl()
    );

    @Override
    @Step("Регистрация нового пользователя")
    public @Nonnull UserJson registerUser(@Nonnull String username, @Nonnull String password) {
        return xaTransactionTemplate.execute(() -> {
            AuthUserEntity authUser = authUserEntity(username, password);
            authUserRepository.create(authUser);
            return UserJson.fromEntity(
                    userdataUserRepository.create(userEntity(username)),
                    null
            );
        });
    }

    @Override
    @Step("Поиск пользователя по Логину")
    public @Nonnull UserJson getCurrentUser(@Nonnull String username) {
        return xaTransactionTemplate.execute(() -> {
            UserEntity userEntity;
            try {
                userEntity = userdataUserRepository.findByUsername(username)
                        .orElseThrow(() -> new IOException("User not found"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return UserJson.fromEntity(userEntity, null);
        });
    }

    @Override
    @Step("Обновление пользователя")
    public @Nonnull UserJson updateUser(@Nonnull UserJson user) {
        return xaTransactionTemplate.execute(() -> {
            UserEntity userEntity;
            try {
                userEntity = userdataUserRepository.findById(user.id())
                        .orElseThrow(() -> new IOException("User not found"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            userEntity.setUsername(user.username());
            userdataUserRepository.update(userEntity);
            return UserJson.fromEntity(userEntity, null);
        });
    }

    @Override
    @Step("Отправить запрос в друзья")
    public @Nonnull UserJson sendInvitation(@Nonnull String username, @Nonnull String targetUsername) {
        return xaTransactionTemplate.execute(() -> {
            UserEntity requester;
            try {
                requester = userdataUserRepository.findByUsername(username)
                        .orElseThrow(() -> new IOException("User not found: " + username));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            UserEntity addressee;
            try {
                addressee = userdataUserRepository.findByUsername(targetUsername)
                        .orElseThrow(() -> new IOException("Target user not found: " + targetUsername));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            userdataUserRepository.sendInvitation(requester, addressee);
            return UserJson.fromEntity(addressee, null);
        });
    }

    @Override
    @Step("Принять запрос в друзья")
    public @Nonnull UserJson acceptInvitation(@Nonnull String username, @Nonnull String targetUsername) {
        throw new UnsupportedOperationException("Accept invitation is not supported now");
    }

    @Override
    @Step("Отклонить запрос в друзья")
    public @Nonnull UserJson declineInvitation(@Nonnull String username, @Nonnull String targetUsername) {
        throw new UnsupportedOperationException("Decline invitation is not supported now");
    }

    @Override
    @Step("Удалить пользователя из друзей")
    public void removeFriend(@Nonnull String username, @Nonnull String targetUsername) {
        throw new UnsupportedOperationException("Remove friend is not supported now");
    }

    private UserEntity userEntity(String username) {
        UserEntity ue = new UserEntity();
        ue.setUsername(username);
        ue.setCurrency(CurrencyValues.RUB);
        return ue;
    }

    private AuthUserEntity authUserEntity(String username, String password) {
        AuthUserEntity authUser = new AuthUserEntity();
        authUser.setUsername(username);
        authUser.setPassword(pe.encode(password));
        authUser.setEnabled(true);
        authUser.setAccountNonExpired(true);
        authUser.setAccountNonLocked(true);
        authUser.setCredentialsNonExpired(true);
        authUser.setAuthorities(
                Arrays.stream(Authority.values()).map(
                        e -> {
                            AuthorityEntity ae = new AuthorityEntity();
                            ae.setUser(authUser);
                            ae.setAuthority(e);
                            return ae;
                        }
                ).toList()
        );
        return authUser;
    }

}
