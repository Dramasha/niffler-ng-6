package guru.qa.niffler.data.dao.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.SpendDao;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.model.CurrencyValues;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static guru.qa.niffler.data.tpl.Connections.holder;

public class SpendDaoJdbc implements SpendDao {
    private static final Config CFG = Config.getInstance();

    @Override
    public SpendEntity create(SpendEntity spend) {
        try (PreparedStatement statement = holder(CFG.spendJdbcUrl()).connection().prepareStatement(
                "INSERT INTO spend (username, spend_date, currency, amount, description, category_id) VALUES (?,?,?,?,?,?)",
                Statement.RETURN_GENERATED_KEYS
        )) {
            statement.setString(1, spend.getUsername());
            statement.setDate(2, spend.getSpendDate());
            statement.setString(3, spend.getCurrency().name());
            statement.setDouble(4, spend.getAmount());
            statement.setString(5, spend.getDescription());
            statement.setObject(6, spend.getCategory().getId());

            statement.executeUpdate();

            final UUID generatedKey;
            try (ResultSet resultSet = statement.getGeneratedKeys()) {
                if (resultSet.next()) {
                    generatedKey = resultSet.getObject("id", UUID.class);
                } else {
                    throw new SQLException("Cant find id in ResultSet");
                }
            }
            spend.setId(generatedKey);
            return spend;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<SpendEntity> findById(UUID id) {
        try (PreparedStatement statement = holder(CFG.spendJdbcUrl()).connection().prepareStatement(
                "SELECT * FROM spend WHERE id = ?"
        )) {
            statement.setObject(1, id);
            statement.execute();
            try (ResultSet resultSet = statement.getResultSet()) {
                if (resultSet.next()) {
                    SpendEntity spend = new SpendEntity();

                    spend.setId(resultSet.getObject("id", UUID.class));
                    spend.setUsername(resultSet.getString("username"));
                    spend.setSpendDate(resultSet.getDate("spend_date"));
                    spend.setCurrency(resultSet.getObject("currency", CurrencyValues.class));
                    spend.setAmount(resultSet.getDouble("amount"));
                    spend.setDescription(resultSet.getString("description"));
                    spend.setCategory(resultSet.getObject("category_id", CategoryEntity.class));


                    return Optional.of(spend);
                } else {
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<SpendEntity> findAllByUsername(String username) {
        List<SpendEntity> spends = new ArrayList<>();
        try (PreparedStatement statement = holder(CFG.spendJdbcUrl()).connection().prepareStatement(
                "SELECT * FROM spend WHERE username = ?"
        )) {
            statement.setObject(1, username);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    SpendEntity spend = new SpendEntity();

                    spend.setId(resultSet.getObject("id", UUID.class));
                    spend.setUsername(resultSet.getString("username"));
                    spend.setSpendDate(resultSet.getDate("spend_date"));
                    spend.setCurrency(resultSet.getObject("currency", CurrencyValues.class));
                    spend.setAmount(resultSet.getDouble("amount"));
                    spend.setDescription(resultSet.getString("description"));
                    spend.setCategory(resultSet.getObject("category_id", CategoryEntity.class));

                    spends.add(spend);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return spends;
    }

    public void delete(SpendEntity spend) {
        try (PreparedStatement statement = holder(CFG.spendJdbcUrl()).connection().prepareStatement(
                "DELETE FROM spend WHERE id = ?"
        )) {

            statement.setObject(1, spend.getId());
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<SpendEntity> findAll() {
        List<SpendEntity> spends = new ArrayList<>();
        try (PreparedStatement statement = holder(CFG.spendJdbcUrl()).connection().prepareStatement(
                "SELECT * FROM spend"
        )) {
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    SpendEntity spend = new SpendEntity();

                    spend.setId(resultSet.getObject("id", UUID.class));
                    spend.setUsername(resultSet.getString("username"));
                    spend.setSpendDate(resultSet.getDate("spend_date"));
                    spend.setCurrency(resultSet.getObject("currency", CurrencyValues.class));
                    spend.setAmount(resultSet.getDouble("amount"));
                    spend.setDescription(resultSet.getString("description"));
                    spend.setCategory(resultSet.getObject("category_id", CategoryEntity.class));

                    spends.add(spend);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return spends;
    }
}
