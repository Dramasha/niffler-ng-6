package guru.qa.niffler.api.impl;

import guru.qa.niffler.api.SpendApi;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.service.RestClient;
import guru.qa.niffler.service.SpendClient;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ParametersAreNonnullByDefault
public class SpendApiClient extends RestClient implements SpendClient{

    private final SpendApi spendApi;

    public SpendApiClient() {
        super(CFG.spendUrl());
        this.spendApi = retrofit.create(SpendApi.class);
    }

    @Override
    @Nonnull
    public SpendJson createSpend(@Nonnull SpendJson spend) {
        final Response<SpendJson> response;
        try {
            response = spendApi.addSpend(spend)
                    .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }

        return Objects.requireNonNull(response.body());

    }

//    @Override
//    @Nonnull
//    public SpendJson updateSpend(SpendJson spend) {
//        final Response<SpendJson> response;
//        try {
//            response = spendApi.editSpend(spend)
//                    .execute();
//        } catch (IOException e) {
//            throw new AssertionError(e);
//        }
//
//        return Objects.requireNonNull(response.body());
//
//    }
//
//    @Override
//    public Optional<SpendJson> findSpendById(UUID id) {
//        final Response<SpendJson> response;
//        try {
//            response = spendApi.getSpend(id.toString())
//                    .execute();
//        } catch (IOException e) {
//            throw new AssertionError(e);
//        }
//        if (response.isSuccessful()) {
//            return Optional.ofNullable(response.body());
//        }
//        return Optional.empty();
//    }
//
//    @Override
//    public List<SpendJson> findSpendByIdAndUsername(String id, String username) {
//        throw new UnsupportedOperationException("Find spend by Id and Username is not supported with API");
//    }
//
//    @Override
//    public void deleteSpend(SpendJson spend) {
//        List<String> ids = List.of(spend.id().toString());
//        Response<Void> response;
//        try {
//            response = spendApi.removeSpend(spend.username(), ids).execute();
//            if (!response.isSuccessful()) {
//                throw new IOException("Error deleting spend: " + response.errorBody().string());
//            }
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }

    @Nonnull
    public List<SpendJson> allSpends(String username,
                                     @Nullable CurrencyValues currency,
                                     @Nullable Date from,
                                     @Nullable Date to) {
        final Response<List<SpendJson>> response;
        try {
            response = spendApi.getSpends(username, currency, from, to)
                    .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(200, response.code());
        return response.body() != null ? Objects.requireNonNull(response.body()) : Collections.emptyList();
    }


    @Override
    @Nonnull
    public CategoryJson createCategory(@Nonnull CategoryJson category) {
        final Response<CategoryJson> response;
        try {
            response = spendApi.addCategory(category)
                    .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        return Objects.requireNonNull(response.body());

    }

//    @Override
//    public @Nullable CategoryJson updateCategory(CategoryJson category) {
//        final Response<CategoryJson> response;
//        try {
//            response = spendApi.updateCategory(category)
//                    .execute();
//        } catch (IOException e) {
//            throw new AssertionError(e);
//        }
//        if (response.isSuccessful()) {
//            return response.body();
//        }
//        return null;
//    }
//
//    @Override
//    public Optional<CategoryJson> findCategoryById(UUID id) {
//        throw new UnsupportedOperationException("Find category by Id is not supported with API");
//    }
//
//    @Override
//    public Optional<CategoryJson> findCategoryByUsernameAndCategoryName(String username, String name) {
//        throw new UnsupportedOperationException("Find category by Username and Category name is not supported with API");
//    }

    @Override
    public void deleteCategory(@Nonnull CategoryJson category) {
        throw new UnsupportedOperationException("Delete category is not supported with API");
    }
}
