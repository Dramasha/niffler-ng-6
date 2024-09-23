package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.service.SpendDbClient;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;

import static guru.qa.niffler.utils.RandomDataUtils.getRandomCategoryName;

public class CategoryExtension implements BeforeEachCallback, AfterTestExecutionCallback, ParameterResolver {
    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(CategoryExtension.class);

    private final SpendDbClient spendDbClient = new SpendDbClient();

    @Override
    public void beforeEach(ExtensionContext context) {
        AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), User.class)
                .ifPresent(user -> {
                    if (user.categories().length > 0) {
                        Category category = user.categories()[0];
                        CategoryJson categoryJson = new CategoryJson(
                                null,
                                category.title().isEmpty() ? getRandomCategoryName() : category.title(),
                                user.username(),
                                false
                        );
                        CategoryJson createCategoryJson = spendDbClient.createCategory(categoryJson);
                        if (category.archived()) {
                            CategoryJson archivedCategoryJson = new CategoryJson(
                                    createCategoryJson.id(),
                                    createCategoryJson.name(),
                                    createCategoryJson.username(),
                                    true
                            );
                            spendDbClient.deleteCategory(archivedCategoryJson);
                        }
                        context.getStore(NAMESPACE).put(context.getUniqueId(), createCategoryJson);
                    }
                });
    }

    @Override
    public void afterTestExecution(ExtensionContext context) {
        CategoryJson categoryJson = context.getStore(NAMESPACE).get(context.getUniqueId(), CategoryJson.class);
        if (categoryJson != null) {
            CategoryJson archivedCategoryJson = new CategoryJson(
                    categoryJson.id(),
                    categoryJson.name(),
                    categoryJson.username(),
                    true
            );
            spendDbClient.deleteCategory(archivedCategoryJson);
        }
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws
            ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(CategoryJson.class);
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws
            ParameterResolutionException {
        return extensionContext.getStore(CategoryExtension.NAMESPACE).get(extensionContext.getUniqueId(), CategoryJson.class);
    }
}
