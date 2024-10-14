package guru.qa.niffler.data.dao.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.AuthAuthorityDao;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.data.mapper.AuthAuthorityEntityRowMapper;
import guru.qa.niffler.data.tpl.DataSources;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class AuthAuthorityDaoSpringJdbc implements AuthAuthorityDao {

    private static final Config CFG = Config.getInstance();

    @Override
    public void create(AuthorityEntity... authAuthority) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.getDataSource(CFG.authJdbcUrl()));
        jdbcTemplate.batchUpdate(
                "INSERT INTO authority (user_id, authority) VALUES (?,?)",
                new BatchPreparedStatementSetter() {

                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setObject(1, authAuthority[i].getUser().getId());
                        ps.setString(2, authAuthority[i].getAuthority().name());

                    }

                    @Override
                    public int getBatchSize() {
                        return authAuthority.length;
                    }
                }
        );
    }

    @Override
    public Optional<AuthorityEntity> findById(UUID id) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.getDataSource(CFG.authJdbcUrl()));
        return Optional.ofNullable(
                jdbcTemplate.queryForObject(
                        "SELECT * FROM authority WHERE user_id = ?",
                        AuthAuthorityEntityRowMapper.instance,
                        id
                )
        );
    }

    @Override
    public Optional<AuthorityEntity> findByUserId(UUID userId) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.getDataSource(CFG.authJdbcUrl()));
        return Optional.ofNullable(
                jdbcTemplate.queryForObject(
                        "SELECT * FROM authority WHERE user_id = ?",
                        AuthAuthorityEntityRowMapper.instance,
                        userId
                )
        );
    }

    @Override
    public void delete(AuthorityEntity authAuthority) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.getDataSource(CFG.authJdbcUrl()));
        jdbcTemplate.update(
                "DELETE FROM authority WHERE id = ?",
                authAuthority.getId()
        );
    }

    @Override
    public List<AuthorityEntity> findAll() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.getDataSource(CFG.authJdbcUrl()));
        return jdbcTemplate.query(
                "SELECT * FROM authority",
                AuthAuthorityEntityRowMapper.instance
        );
    }
}
