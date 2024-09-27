package guru.qa.niffler.data.dao.impl;

import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.sql.*;
import java.util.UUID;

public class AuthUserDao implements guru.qa.niffler.data.dao.AuthUserDao {
    private final Connection connection;

    private static final PasswordEncoder pe = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    public AuthUserDao(Connection connection) {
        this.connection = connection;
    }

    @Override
    public AuthUserEntity create(AuthUserEntity authUser) {

        try (PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO \"user\" (username, password, enabled, account_non_expired," +
                        "account_non_locked, credentials_non_expired) VALUES (?,?,?,?,?,?)",
                Statement.RETURN_GENERATED_KEYS
        )) {
            statement.setString(1, authUser.getUsername());
            statement.setString(2, pe.encode(authUser.getPassword()));
            statement.setBoolean(3, authUser.getEnabled());
            statement.setBoolean(4, authUser.getAccountNonExpired());
            statement.setBoolean(5, authUser.getAccountNonLocked());
            statement.setBoolean(6, authUser.getCredentialsNonExpired());

            statement.executeUpdate();

            final UUID generatedKey;
            try (ResultSet resultSet = statement.getGeneratedKeys()) {
                if (resultSet.next()) {
                    generatedKey = resultSet.getObject("id", UUID.class);
                } else {
                    throw new SQLException("Cant find id in ResultSet");
                }
            }
            authUser.setId(generatedKey);
            return authUser;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
