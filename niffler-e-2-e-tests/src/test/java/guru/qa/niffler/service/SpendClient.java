package guru.qa.niffler.service;

import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;

import javax.annotation.Nonnull;

public interface SpendClient {

    @Nonnull
    SpendJson createSpend(@Nonnull SpendJson spend);
    @Nonnull
    CategoryJson createCategory(@Nonnull CategoryJson category);

    void deleteCategory(@Nonnull CategoryJson category);
}
