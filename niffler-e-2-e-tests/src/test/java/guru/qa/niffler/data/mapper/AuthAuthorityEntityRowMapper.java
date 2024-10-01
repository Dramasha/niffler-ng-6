package guru.qa.niffler.data.mapper;

import guru.qa.niffler.data.entity.auth.AuthAuthorityEntity;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.auth.Authority;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class AuthAuthorityEntityRowMapper implements RowMapper<AuthAuthorityEntity> {

    public static final AuthAuthorityEntityRowMapper instance = new AuthAuthorityEntityRowMapper();

    private AuthAuthorityEntityRowMapper() {
    }

    @Override
    public AuthAuthorityEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        AuthAuthorityEntity authAuthority = new AuthAuthorityEntity();
        UUID userId = rs.getObject("user-id", UUID.class);
        AuthUserEntity user = new AuthUserEntity();
        user.setId(userId);

        authAuthority.setId(rs.getObject("id", UUID.class));
        authAuthority.setUser(user);
        authAuthority.setAuthority(Authority.valueOf(rs.getString("password")));

        return authAuthority;
    }
}
