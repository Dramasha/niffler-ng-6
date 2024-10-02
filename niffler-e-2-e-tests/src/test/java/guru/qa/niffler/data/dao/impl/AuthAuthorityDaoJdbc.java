package guru.qa.niffler.data.dao.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.AuthAuthorityDao;
import guru.qa.niffler.data.entity.auth.AuthAuthorityEntity;
import guru.qa.niffler.data.entity.auth.Authority;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static guru.qa.niffler.data.tpl.Connections.holder;

public class AuthAuthorityDaoJdbc implements AuthAuthorityDao {

    private static final Config CFG = Config.getInstance();

    public void create(AuthAuthorityEntity... authAuthorities) {
        for (AuthAuthorityEntity authAuthority : authAuthorities) {
            try (PreparedStatement statement = holder(CFG.authJdbcUrl()).connection().prepareStatement(
                    "INSERT INTO authority (user_id, authority) VALUES (?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            )) {
                statement.setObject(1, authAuthority.getUserId());
                statement.setString(2, authAuthority.getAuthority().name());

                statement.executeUpdate();

                try (ResultSet resultSet = statement.getGeneratedKeys()) {
                    if (resultSet.next()) {
                        UUID generatedKey = resultSet.getObject("id", UUID.class);
                        authAuthority.setId(generatedKey);
                    } else {
                        throw new SQLException("Cant find id in ResultSet");
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public Optional<AuthAuthorityEntity> findById(UUID id) {
        try (PreparedStatement statement = holder(CFG.authJdbcUrl()).connection().prepareStatement(
                "SELECT * FROM authority WHERE id = ?"
        )) {
            statement.setObject(1, id);
            statement.execute();
            try (ResultSet resultSet = statement.getResultSet()) {
                if (resultSet.next()) {

                    AuthAuthorityEntity authAuthority = new AuthAuthorityEntity();

                    authAuthority.setId(resultSet.getObject("id", UUID.class));
                    authAuthority.setUserId(resultSet.getObject("user_id", UUID.class));
                    authAuthority.setAuthority(resultSet.getObject("authority", Authority.class));

                    return Optional.of(authAuthority);
                } else {
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<AuthAuthorityEntity> findByUserId(UUID userId) {
        try (PreparedStatement statement = holder(CFG.authJdbcUrl()).connection().prepareStatement(
                "SELECT * FROM authority WHERE user_id = ?"
        )) {
            statement.setObject(1, userId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {

                    AuthAuthorityEntity authAuthority = new AuthAuthorityEntity();

                    authAuthority.setId(resultSet.getObject("id", UUID.class));
                    authAuthority.setUserId(resultSet.getObject("user_id", UUID.class));
                    authAuthority.setAuthority(resultSet.getObject("authority", Authority.class));

                    return Optional.of(authAuthority);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void delete(AuthAuthorityEntity authAuthority) {
        try (PreparedStatement statement = holder(CFG.authJdbcUrl()).connection().prepareStatement(
                "DELETE FROM authority WHERE id = ?"
        )) {

            statement.setObject(1, authAuthority.getId());
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<AuthAuthorityEntity> findAll() {
        List<AuthAuthorityEntity> authAuthority = new ArrayList<>();
        try (PreparedStatement statement = holder(CFG.authJdbcUrl()).connection().prepareStatement(
                "SELECT * FROM authority"
        )) {
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    AuthAuthorityEntity authority = new AuthAuthorityEntity();

                    authority.setId(resultSet.getObject("id", UUID.class));
                    authority.setUserId(resultSet.getObject("user_id", UUID.class));
                    authority.setAuthority(resultSet.getObject("authority", Authority.class));

                    authAuthority.add(authority);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return authAuthority;
    }
}
