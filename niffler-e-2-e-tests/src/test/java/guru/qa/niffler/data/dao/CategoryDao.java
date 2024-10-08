package guru.qa.niffler.data.dao;

import guru.qa.niffler.data.entity.spend.CategoryEntity;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CategoryDao {
    CategoryEntity create(CategoryEntity category);

    Optional<CategoryEntity> findById(UUID id);

    Optional<CategoryEntity> findByUsernameAndCategoryName(String username, String categoryName) throws SQLException;

    List<CategoryEntity> findAllByUsername(String username);

    void delete(CategoryEntity category);

    List<CategoryEntity> findAll();
}
