package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.api.impl.SpendApiClient;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.service.SpendClient;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;

import java.util.ArrayList;
import java.util.List;

import static guru.qa.niffler.utils.RandomDataUtils.getRandomCategoryName;

public class CategoryExtension implements BeforeEachCallback, AfterTestExecutionCallback, ParameterResolver {
    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(CategoryExtension.class);

    private final SpendClient spendClient = new SpendApiClient();

    @Override
    public void beforeEach(ExtensionContext context) {
        AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), User.class)
                .ifPresent(userAnno -> {
                            if (userAnno.categories().length > 0) {
                                List<CategoryJson> result = new ArrayList<>();

                                UserJson userJson = context.getStore(UserExtension.NAMESPACE)
                                        .get(context.getUniqueId(), UserJson.class);

                                for (Category categoryAnno : userAnno.categories()) {
                                    final String categoryName = "".equals(
                                            categoryAnno.title())
                                            ? getRandomCategoryName()
                                            : categoryAnno.title();

                                    CategoryJson categoryJson = new CategoryJson(
                                            null,
                                            categoryName,
                                            userJson != null ? userJson.username() : userAnno.username(),
                                            categoryAnno.archived()
                                    );

                                    CategoryJson createdCategory = spendClient.createCategory(categoryJson);
                                    result.add(createdCategory);
                                }

                                if (userJson != null) {
                                    userJson.testData().categories().addAll(result);
                                } else
                                    context.getStore(NAMESPACE).put(
                                            context.getUniqueId(),
                                            result
                                    );
                            }
                        }
                );
    }

    @Override
    public void afterTestExecution(ExtensionContext context) {
        UserJson userJson = context.getStore(UserExtension.NAMESPACE)
                .get(context.getUniqueId(), UserJson.class);

        List<CategoryJson> categories = userJson != null
                ? userJson.testData().categories()
                : context.getStore(NAMESPACE).get(context.getUniqueId(), List.class);

        for (CategoryJson categoryJson : categories) {
            spendClient.deleteCategory(categoryJson);
        }
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws
            ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(CategoryJson[].class);
    }

    @Override
    @SuppressWarnings("unchecked")
    public CategoryJson[] resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws
            ParameterResolutionException {
        return (CategoryJson[]) extensionContext.getStore(CategoryExtension.NAMESPACE).get(extensionContext.getUniqueId(), List.class)
                .toArray();
    }
}
