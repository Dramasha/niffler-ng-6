package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.CategoryDao;
import guru.qa.niffler.data.dao.SpendDao;
import guru.qa.niffler.data.dao.impl.CategoryDaoJdbc;
import guru.qa.niffler.data.dao.impl.SpendDaoJdbc;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.data.tpl.JdbcTransactionTemplate;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;

import java.sql.SQLException;
import java.util.Optional;

public class SpendDbClient {

    private static final Config CFG = Config.getInstance();

    private final CategoryDao categoryDao = new CategoryDaoJdbc();
    private final SpendDao spendDao = new SpendDaoJdbc();

    private final JdbcTransactionTemplate jdbcTxTemplate = new JdbcTransactionTemplate(
            CFG.spendJdbcUrl()
    );

    public SpendJson createSpend(SpendJson spend, int isolationLevel) throws SQLException {
        return jdbcTxTemplate.execute(() -> {
                    SpendEntity spendEntity = SpendEntity.fromJson(spend);

                    Optional<CategoryEntity> existingCategory;
                    try {
                        existingCategory = categoryDao.findByUsernameAndCategoryName(
                                spendEntity.getUsername(), spendEntity.getCategory().getName()
                        );
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    existingCategory.ifPresent(spendEntity::setCategory);

                    if (spendEntity.getCategory().getId() == null && existingCategory.isEmpty()) {
                        CategoryEntity categoryEntity = categoryDao.create(spendEntity.getCategory());
                        spendEntity.setCategory(categoryEntity);
                    }
                    return SpendJson.fromEntity(
                            spendDao.create(spendEntity)
                    );
                }, isolationLevel
        );
    }

    public void deleteSpend(SpendJson spend) {
        jdbcTxTemplate.execute(() -> {
                    SpendEntity spendEntity = SpendEntity.fromJson(spend);

                    spendDao.delete(spendEntity);
                    return null;
                }
        );
    }

    public CategoryJson createCategory(CategoryJson spend, int isolationLevel) {
        return jdbcTxTemplate.execute(() -> {
                    CategoryEntity categoryEntity = CategoryEntity.fromJson(spend);
                    return CategoryJson.fromEntity(
                            categoryDao.create(categoryEntity)
                    );
                }, isolationLevel
        );
    }

    public void deleteCategory(CategoryJson category) {
        jdbcTxTemplate.execute(() -> {
                    CategoryEntity categoryEntity = CategoryEntity.fromJson(category);
                    categoryDao.delete(categoryEntity);
                    return null;
                }
        );
    }
}
