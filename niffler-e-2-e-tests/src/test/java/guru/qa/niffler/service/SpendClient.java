package guru.qa.niffler.service;

import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;

import javax.annotation.Nonnull;

public interface SpendClient {

    @Nonnull
    SpendJson createSpend(@Nonnull SpendJson spend);

//    @Nonnull
//    SpendJson updateSpend(SpendJson spend);
//
//    Optional<SpendJson> findSpendById(UUID id);
//
//    List<SpendJson> findSpendByIdAndUsername(String id, String username);
//
//    void deleteSpend(SpendJson spend);

    @Nonnull
    CategoryJson createCategory(@Nonnull CategoryJson category);

//    CategoryJson updateCategory(CategoryJson category);
//
//    Optional<CategoryJson> findCategoryById(UUID id);
//
//    Optional<CategoryJson> findCategoryByUsernameAndCategoryName(String username, String name);

    void deleteCategory(@Nonnull CategoryJson category);
}
