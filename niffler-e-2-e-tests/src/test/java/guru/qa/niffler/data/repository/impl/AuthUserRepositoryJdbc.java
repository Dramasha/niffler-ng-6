package guru.qa.niffler.data.repository.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.auth.Authority;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.data.mapper.AuthAuthorityEntityRowMapper;
import guru.qa.niffler.data.mapper.AuthUserEntityRowMapper;
import guru.qa.niffler.data.repository.AuthUserRepository;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static guru.qa.niffler.data.tpl.Connections.holder;

public class AuthUserRepositoryJdbc implements AuthUserRepository {

    private static final Config CFG = Config.getInstance();
    private static final PasswordEncoder pe = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    @Override
    public AuthUserEntity create(AuthUserEntity authUser) {
        try (PreparedStatement userStatement = holder(CFG.authJdbcUrl()).connection().prepareStatement(
                "INSERT INTO \"user\" (username, password, enabled, account_non_expired," +
                        "account_non_locked, credentials_non_expired) VALUES (?,?,?,?,?,?)", PreparedStatement.RETURN_GENERATED_KEYS);
             PreparedStatement authorityStatement = holder(CFG.authJdbcUrl()).connection().prepareStatement(
                     "INSERT INTO \"authority\" (user_id, authority) VALUES (?, ?)"
             )) {
            userStatement.setString(1, authUser.getUsername());
            userStatement.setString(2, authUser.getPassword());
            userStatement.setBoolean(3, authUser.getEnabled());
            userStatement.setBoolean(4, authUser.getAccountNonExpired());
            userStatement.setBoolean(5, authUser.getAccountNonLocked());
            userStatement.setBoolean(6, authUser.getCredentialsNonExpired());

            userStatement.executeUpdate();

            final UUID generatedKey;
            try (ResultSet resultSet = userStatement.getGeneratedKeys()) {
                if (resultSet.next()) {
                    generatedKey = resultSet.getObject("id", UUID.class);
                } else {
                    throw new SQLException("Cant find id in ResultSet");
                }
            }
            authUser.setId(generatedKey);

            for (AuthorityEntity ae : authUser.getAuthorities()) {
                authorityStatement.setObject(1, generatedKey);
                authorityStatement.setString(2, ae.getAuthority().name());
                authorityStatement.addBatch();
                authorityStatement.clearParameters();
            }
            authorityStatement.executeBatch();

            return authUser;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<AuthUserEntity> findById(UUID id) {
        try (PreparedStatement statement = holder(CFG.authJdbcUrl()).connection().prepareStatement(
                "SELECT * FROM \"user\" u " +
                        "JOIN \"authority\" a ON u.id = a.user_id " +
                        "WHERE u.id = ?"
        )) {
            statement.setObject(1, id);
            statement.execute();

            try (ResultSet resultSet = statement.getResultSet()) {
                AuthUserEntity user = null;
                List<AuthorityEntity> authorities = new ArrayList<>();
                while (resultSet.next()) {
                    if (user == null) {
                        user = AuthUserEntityRowMapper.instance.mapRow(resultSet, 1);
                    }

                    AuthorityEntity authority = new AuthorityEntity();
                    authority.setUser(user);
                    authority.setId(resultSet.getObject("a.id", UUID.class));
                    authority.setAuthority(Authority.valueOf(resultSet.getString("authority")));
                    authorities.add(authority);
                }
                if (user == null) {
                    return Optional.empty();
                } else {
                    user.setAuthorities(authorities);
                    return Optional.of(user);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public List<AuthUserEntity> findByUsername(String username) {
        List<AuthUserEntity> authUsers = new ArrayList<>();
        try (PreparedStatement statement = holder(CFG.authJdbcUrl()).connection().prepareStatement(
                "SELECT * FROM \"user\" WHERE username = ?"
        )) {
            statement.setString(1, username);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {

                    AuthUserEntity authUser = new AuthUserEntity();

                    authUser.setId(resultSet.getObject("id", UUID.class));
                    authUser.setUsername(resultSet.getString("username"));
                    authUser.setPassword(resultSet.getString("password"));
                    authUser.setEnabled(resultSet.getBoolean("enabled"));
                    authUser.setAccountNonExpired(resultSet.getBoolean("account_non_expired"));
                    authUser.setAccountNonLocked(resultSet.getBoolean("account_non_locked"));
                    authUser.setCredentialsNonExpired(resultSet.getBoolean("credentials_non_expired"));

                    authUsers.add(authUser);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return authUsers;
    }

    public void delete(AuthUserEntity authUser) {
        try (PreparedStatement statement = holder(CFG.authJdbcUrl()).connection().prepareStatement(
                "DELETE FROM \"user\" WHERE id = ?"
        )) {

            statement.setObject(1, authUser.getId());
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<AuthUserEntity> findAll() {
        List<AuthUserEntity> authUsers = new ArrayList<>();
        try (PreparedStatement statement = holder(CFG.authJdbcUrl()).connection().prepareStatement(
                "SELECT * FROM spend"
        )) {
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    AuthUserEntity authUser = new AuthUserEntity();

                    authUser.setId(resultSet.getObject("id", UUID.class));
                    authUser.setUsername(resultSet.getString("username"));
                    authUser.setPassword(resultSet.getString("password"));
                    authUser.setEnabled(resultSet.getBoolean("enabled"));
                    authUser.setAccountNonExpired(resultSet.getBoolean("account_non_expired"));
                    authUser.setAccountNonLocked(resultSet.getBoolean("account_non_locked"));
                    authUser.setCredentialsNonExpired(resultSet.getBoolean("credentials_non_expired"));

                    authUsers.add(authUser);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return authUsers;
    }
}
