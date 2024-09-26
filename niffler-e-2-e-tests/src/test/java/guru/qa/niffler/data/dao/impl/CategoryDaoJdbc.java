package guru.qa.niffler.data.dao.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.CategoryDao;
import guru.qa.niffler.data.entity.spend.CategoryEntity;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class CategoryDaoJdbc implements CategoryDao {
    private final Connection connection;

    public CategoryDaoJdbc(Connection connection) {
        this.connection = connection;
    }

    private static final Config CFG = Config.getInstance();

    @Override
    public CategoryEntity create(CategoryEntity category) {

            try (PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO category (username, name, archived) VALUES (?,?,?)",
                    Statement.RETURN_GENERATED_KEYS
            )) {
                statement.setString(1, category.getUsername());
                statement.setString(2, category.getName());
                statement.setBoolean(3, category.isArchived());

                statement.executeUpdate();

                final UUID generatedKey;
                try (ResultSet resultSet = statement.getGeneratedKeys()) {
                    if (resultSet.next()) {
                        generatedKey = resultSet.getObject("id", UUID.class);
                    } else {
                        throw new SQLException("Cant find id in ResultSet");
                    }
                }
                category.setId(generatedKey);
                return category;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public Optional<CategoryEntity> findById(UUID id) {
            try (PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM category WHERE id = ?"
            )) {
                statement.setObject(1, id);
                statement.execute();
                try (ResultSet resultSet = statement.getResultSet()) {
                    if (resultSet.next()) {

                        CategoryEntity category = new CategoryEntity();
                        category.setId(resultSet.getObject("id", UUID.class));
                        category.setUsername(resultSet.getString("username"));
                        category.setName(resultSet.getString("name"));
                        category.setArchived(resultSet.getBoolean("archived"));

                        return Optional.of(category);
                    } else {
                        return Optional.empty();
                    }
                }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<CategoryEntity> findByUsernameAndCategoryName(String username, String categoryName) {
            try (PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM category WHERE username = ? AND name = ?"
            )) {
                statement.setString(1, username);
                statement.setString(2, categoryName);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        CategoryEntity category = new CategoryEntity();

                        category.setId(resultSet.getObject("id", UUID.class));
                        category.setName(resultSet.getString("name"));
                        category.setUsername(resultSet.getString("username"));
                        category.setArchived(resultSet.getBoolean("archived"));

                        return Optional.of(category);
                    }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            return Optional.empty();
        } catch (SQLException e) {
                throw new RuntimeException(e);
            }
    }

    public List<CategoryEntity> findAllByUsername(String username) {
        List<CategoryEntity> categories = new ArrayList<>();
            try (PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM category WHERE username = ?"
            )) {
                statement.setObject(1, username);
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        CategoryEntity category = new CategoryEntity();

                        category.setId(resultSet.getObject("id", UUID.class));
                        category.setName(resultSet.getString("name"));
                        category.setUsername(resultSet.getString("username"));
                        category.setArchived(resultSet.getBoolean("archived"));

                        categories.add(category);
                    }
                }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return categories;
    }

    public void deleteById(CategoryEntity category) {
        try (PreparedStatement statement = connection.prepareStatement(
                     "DELETE FROM category WHERE id = ?"
             )) {

            statement.setObject(1, category.getId());
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}
