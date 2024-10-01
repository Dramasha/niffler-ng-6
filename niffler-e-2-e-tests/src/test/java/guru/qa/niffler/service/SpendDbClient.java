package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.impl.CategoryDaoJdbc;
import guru.qa.niffler.data.dao.impl.SpendDaoJdbc;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;

import java.sql.SQLException;
import java.util.Optional;

import static guru.qa.niffler.data.Databases.transaction;

public class SpendDbClient {

    private static final Config CFG = Config.getInstance();

    public SpendJson createSpend(SpendJson spend, int isolationLevel) throws SQLException {
        return transaction(connection -> {
                    SpendEntity spendEntity = SpendEntity.fromJson(spend);

                    Optional<CategoryEntity> existingCategory = new CategoryDaoJdbc(connection).findByUsernameAndCategoryName(
                            spendEntity.getUsername(), spendEntity.getCategory().getName()
                    );
                    existingCategory.ifPresent(spendEntity::setCategory);

                    if (spendEntity.getCategory().getId() == null && existingCategory.isEmpty()) {
                        CategoryEntity categoryEntity = new CategoryDaoJdbc(connection).create(spendEntity.getCategory());
                        spendEntity.setCategory(categoryEntity);
                    }
                    return SpendJson.fromEntity(
                            new SpendDaoJdbc(connection).create(spendEntity)
                    );
                }, CFG.spendJdbcUrl(), isolationLevel
        );
    }

    public void deleteSpend(SpendJson spend) {
        transaction(connection -> {
                    SpendEntity spendEntity = SpendEntity.fromJson(spend);

                    new SpendDaoJdbc(connection).delete(spendEntity);
                }, CFG.spendJdbcUrl()
        );
    }

    public CategoryJson createCategory(CategoryJson spend, int isolationLevel) {
        return transaction(connection -> {
                    CategoryEntity categoryEntity = CategoryEntity.fromJson(spend);
                    return CategoryJson.fromEntity(
                            new CategoryDaoJdbc(connection).create(categoryEntity)
                    );
                }, CFG.spendJdbcUrl(), isolationLevel
        );
    }

    public void deleteCategory(CategoryJson category) {
        transaction(connection -> {
                    CategoryEntity categoryEntity = CategoryEntity.fromJson(category);
                    new CategoryDaoJdbc(connection).delete(categoryEntity);
                }, CFG.spendJdbcUrl()
        );
    }
}
