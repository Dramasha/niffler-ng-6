package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.*;
import guru.qa.niffler.data.dao.impl.*;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.data.entity.auth.Authority;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.data.repository.AuthUserRepository;
import guru.qa.niffler.data.repository.UserdataUserRepository;
import guru.qa.niffler.data.repository.impl.AuthUserRepositoryHibernate;
import guru.qa.niffler.data.repository.impl.AuthUserRepositoryJdbc;
import guru.qa.niffler.data.repository.impl.UserdataUserRepositoryHibernate;
import guru.qa.niffler.data.repository.impl.UserdataUserRepositoryJdbc;
import guru.qa.niffler.data.tpl.DataSources;
import guru.qa.niffler.data.tpl.XaTransactionTemplate;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.UserJson;
import org.jetbrains.annotations.NotNull;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.support.TransactionTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class UsersDbClient {

    private static final Config CFG = Config.getInstance();
    private static final PasswordEncoder pe = PasswordEncoderFactories.createDelegatingPasswordEncoder();


    private final AuthUserDao authUserDaoSpring = new AuthUserDaoSpringJdbc();
    private final AuthUserRepository authUserRepository = new AuthUserRepositoryHibernate();

    private final UserdataUserRepository userdataUser = new UserdataUserRepositoryHibernate();
    private final UserdataUserDao userdataUserDao = new UserdataUserDaoJdbc();

    private final UserdataUserRepository userdataUserRepository = new UserdataUserRepositoryJdbc();

    private final TransactionTemplate txTemplate = new TransactionTemplate(
            new JdbcTransactionManager(
                    DataSources.getDataSource(CFG.authJdbcUrl())
            )
    );

    private final XaTransactionTemplate xaTxTemplate = new XaTransactionTemplate(
            CFG.authJdbcUrl(),
            CFG.userdataJdbcUrl()
    );

    public UserJson createUser(String username, String password) {
        return txTemplate.execute(status -> {
            AuthUserEntity authUser = authUserEntity(username, password);
            authUserRepository.create(authUser);
                    return UserJson.fromEntity(
                            userdataUser.create(userEntity(username)),
                            null
                    );
                }
        );
    }


    public UserJson createUserSpringJdbc(String username, String password) {
        return xaTxTemplate.execute(() -> {
                    AuthUserEntity authUser = authUserEntity(username, password);

                    authUserRepository.create(authUser);
                    return UserJson.fromEntity(
                            userdataUser.create(userEntity(username)),
                            null
                    );
                }
        );
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


    public UserJson createUserWithoutSpringJdbcTransaction(String username, String password) {
        AuthUserEntity authUser = authUserEntity(username, password);

        authUserRepository.create(authUser);

        return UserJson.fromEntity(
                userdataUserDao.create(userEntity(username)),
                null
        );
    }

    public UserJson createUserJdbcTransaction(String username, String password) {
        return txTemplate.execute(status -> {
            AuthUserEntity authUser = authUserEntity(username, password);


            authUserDaoSpring.create(authUser);


                    return UserJson.fromEntity(
                            userdataUser.create(userEntity(username)),
                            null
                    );
                }
        );
    }

    public UserJson createUserWithoutJdbcTransaction(String username, String password) {
        AuthUserEntity authUser = authUserEntity(username, password);

        authUserDaoSpring.create(authUser);

        return UserJson.fromEntity(
                userdataUser.create(userEntity(username)),
                null
        );
    }

    public void addIncomeInvitation(UserJson requester, UserJson addressee) {
        xaTxTemplate.execute(() -> {
            userdataUserRepository.addInvitation(
                    UserEntity.fromJson(requester),
                    UserEntity.fromJson(addressee)
            );
            return null;
        });
    }

    public void addOutcomeInvitation(UserJson requester, UserJson addressee) {
        xaTxTemplate.execute(() -> {
            userdataUserRepository.addInvitation(
                    UserEntity.fromJson(addressee),
                    UserEntity.fromJson(requester)
            );
            return null;
        });
    }

    public void addFriend(UserJson requester, UserJson addressee) {
        xaTxTemplate.execute(() -> {
            userdataUserRepository.addFriend(
                    UserEntity.fromJson(requester),
                    UserEntity.fromJson(addressee)
            );
            return null;
        });
    }
}
