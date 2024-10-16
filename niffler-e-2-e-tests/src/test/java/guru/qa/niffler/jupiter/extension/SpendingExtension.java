package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.jupiter.annotation.Spending;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.service.SpendClient;
import guru.qa.niffler.service.impl.SpendDbClient;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SpendingExtension implements BeforeEachCallback, ParameterResolver {

    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(SpendingExtension.class);

    private final SpendClient spendClient = new SpendDbClient();

    @Override
    public void beforeEach(ExtensionContext context) {
        AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), User.class)
                .ifPresent(userAnno -> {
                            if (userAnno.spendings().length > 0) {
                                List<SpendJson> result = new ArrayList<>();
                                UserJson userJson = context.getStore(UserExtension.NAMESPACE)
                                        .get(context.getUniqueId(), UserJson.class);

                                for (Spending spendAnno : userAnno.spendings()) {
                                    SpendJson spendJson = new SpendJson(
                                            null,
                                            new Date(),
                                            new CategoryJson(
                                                    null,
                                                    spendAnno.category(),
                                                    userJson != null ? userJson.username() : userAnno.username(),
                                                    false
                                            ),
                                            CurrencyValues.RUB,
                                            spendAnno.amount(),
                                            spendAnno.description(),
                                            userJson != null ? userJson.username() : userAnno.username()
                                    );
                                    SpendJson createdSpend = spendClient.createSpend(spendJson);
                                    result.add(createdSpend);
                                }

                                if (userJson != null) {
                                    userJson.testData().spends().addAll(result);
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
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws
            ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(SpendJson[].class);
    }

    @Override
    @SuppressWarnings("unchecked")
    public SpendJson[] resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return (SpendJson[]) extensionContext.getStore(SpendingExtension.NAMESPACE).get(extensionContext.getUniqueId(), List.class)
                .toArray();
    }
}
