package guru.qa.niffler.data.repository.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.auth.Authority;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
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
    public AuthUserEntity create(AuthUserEntity user) {
        try (PreparedStatement userStatement = holder(CFG.authJdbcUrl()).connection().prepareStatement(
                "INSERT INTO \"user\" (username, password, enabled, account_non_expired," +
                        "account_non_locked, credentials_non_expired) VALUES (?,?,?,?,?,?)", PreparedStatement.RETURN_GENERATED_KEYS);
             PreparedStatement authorityStatement = holder(CFG.authJdbcUrl()).connection().prepareStatement(
                     "INSERT INTO \"authority\" (user_id, authority) VALUES (?, ?)"
             )) {
            userStatement.setString(1, user.getUsername());
            userStatement.setString(2, user.getPassword());
            userStatement.setBoolean(3, user.getEnabled());
            userStatement.setBoolean(4, user.getAccountNonExpired());
            userStatement.setBoolean(5, user.getAccountNonLocked());
            userStatement.setBoolean(6, user.getCredentialsNonExpired());
            userStatement.executeUpdate();
            final UUID generatedKey;
            try (ResultSet resultSet = userStatement.getGeneratedKeys()) {
                if (resultSet.next()) {
                    generatedKey = resultSet.getObject("id", UUID.class);
                } else {
                    throw new SQLException("Cant find id in ResultSet");
                }
            }
            user.setId(generatedKey);
            for (AuthorityEntity ae : user.getAuthorities()) {
                authorityStatement.setObject(1, generatedKey);
                authorityStatement.setString(2, ae.getAuthority().name());
                authorityStatement.addBatch();
                authorityStatement.clearParameters();
            }
            authorityStatement.executeBatch();
            return user;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public AuthUserEntity update(AuthUserEntity user) {
        String updateUserSql = "UPDATE \"user\" SET password = ?, enabled = ?, " +
                "account_non_expired = ?, account_non_locked = ?, credentials_non_expired = ? " +
                "WHERE id = ?";
        String clearAuthoritySql = "DELETE FROM \"authority\" WHERE user_id = ?";
        String insertAuthoritySql = "INSERT INTO \"authority\" (user_id, authority) VALUES (?, ?)";
        try (PreparedStatement updateUserPs = holder(CFG.authJdbcUrl()).connection().prepareStatement(updateUserSql);
             PreparedStatement clearAuthorityPs = holder(CFG.authJdbcUrl()).connection().prepareStatement(clearAuthoritySql);
             PreparedStatement authorityPs = holder(CFG.authJdbcUrl()).connection().prepareStatement(insertAuthoritySql)) {
            clearAuthorityPs.setObject(1, user.getId());
            clearAuthorityPs.executeUpdate();
            for (AuthorityEntity authority : user.getAuthorities()) {
                authorityPs.setObject(1, user.getId());
                authorityPs.setString(2, authority.getAuthority().name());
                authorityPs.addBatch();
            }
            authorityPs.executeBatch();
            updateUserPs.setString(1, user.getPassword());
            updateUserPs.setBoolean(2, user.getEnabled());
            updateUserPs.setBoolean(3, user.getAccountNonExpired());
            updateUserPs.setBoolean(4, user.getAccountNonLocked());
            updateUserPs.setBoolean(5, user.getCredentialsNonExpired());
            updateUserPs.setObject(6, user.getId());
            updateUserPs.executeUpdate();
            return user;
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


    public Optional<AuthUserEntity> findByUsername(String username) {
        AuthUserEntity authUser = new AuthUserEntity();
        try (PreparedStatement statement = holder(CFG.authJdbcUrl()).connection().prepareStatement(
                "SELECT * FROM \"user\" WHERE username = ?"
        )) {
            statement.setString(1, username);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {

                    authUser.setId(resultSet.getObject("id", UUID.class));
                    authUser.setUsername(resultSet.getString("username"));
                    authUser.setPassword(resultSet.getString("password"));
                    authUser.setEnabled(resultSet.getBoolean("enabled"));
                    authUser.setAccountNonExpired(resultSet.getBoolean("account_non_expired"));
                    authUser.setAccountNonLocked(resultSet.getBoolean("account_non_locked"));
                    authUser.setCredentialsNonExpired(resultSet.getBoolean("credentials_non_expired"));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.ofNullable(authUser);
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
}
