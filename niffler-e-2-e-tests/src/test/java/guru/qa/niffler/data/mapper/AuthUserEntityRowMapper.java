package guru.qa.niffler.data.mapper;

import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class AuthUserEntityRowMapper implements RowMapper<AuthUserEntity> {

    public static final AuthUserEntityRowMapper instance = new AuthUserEntityRowMapper();

    private AuthUserEntityRowMapper() {
    }


    @Override
    public AuthUserEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        AuthUserEntity authUser = new AuthUserEntity();

        authUser.setId(rs.getObject("id", UUID.class));
        authUser.setUsername(rs.getString("username"));
        authUser.setPassword(rs.getString("password"));
        authUser.setEnabled(rs.getBoolean("enabled"));
        authUser.setAccountNonExpired(rs.getBoolean("account_non_expired"));
        authUser.setAccountNonLocked(rs.getBoolean("account_non_locked"));
        authUser.setCredentialsNonExpired(rs.getBoolean("credentials_non_expired"));
        return authUser;
    }
}
