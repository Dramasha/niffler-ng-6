package guru.qa.niffler.service.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.data.repository.SpendRepository;
import guru.qa.niffler.data.repository.impl.SpendRepositoryHibernate;
import guru.qa.niffler.data.tpl.XaTransactionTemplate;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.service.SpendClient;

import javax.annotation.Nonnull;

public class SpendDbClient implements SpendClient {

    private static final Config CFG = Config.getInstance();

    private final SpendRepository spendRepository = new SpendRepositoryHibernate();
    private final XaTransactionTemplate xaTransactionTemplate = new XaTransactionTemplate(
            CFG.spendJdbcUrl()
    );

    @Override
    @Nonnull
    public SpendJson createSpend(@Nonnull SpendJson spend) {
        return xaTransactionTemplate.execute(() -> {
            SpendEntity spendEntity = SpendEntity.fromJson(spend);
            if (spendEntity.getCategory().getId() != null) {
                spendEntity.setCategory(spendRepository.updateCategory(spendEntity.getCategory()));
            } else {
                CategoryEntity categoryEntity = spendRepository.createCategory(spendEntity.getCategory());
                spendEntity.setCategory(categoryEntity);
            }
            return SpendJson.fromEntity(spendRepository.create(spendEntity));
        });
    }

    @Override
    @Nonnull
    public CategoryJson createCategory(@Nonnull CategoryJson category) {
        return xaTransactionTemplate.execute(() -> {
                    CategoryEntity categoryEntity = CategoryEntity.fromJson(category);
                    CategoryEntity createdCategoryEntity = spendRepository.createCategory(categoryEntity);
                    return CategoryJson.fromEntity(createdCategoryEntity);
                }
        );
    }

    @Override
    public void deleteCategory(@Nonnull CategoryJson category) {
        xaTransactionTemplate.execute(() -> {
            CategoryEntity categoryEntity = CategoryEntity.fromJson(category);
            spendRepository.removeCategory(categoryEntity);
            return null;
        });
    }
}
